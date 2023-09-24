package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crm.tranquil_sales_steer.ui.adapters.ActivitiesAdapter;
import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppSingleton;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.AllStatusResponse;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadUpdateActivity extends AppCompatActivity {

    Spinner nextActivity;
    ArrayList<ActivityMainResponse> activityMainResponses = new ArrayList<>();
    ActivitiesAdapter aAdapter;
    String companyID, userID, customerID, autoID, titleID, emailStr, numberStr;
    EditText otherETID;
    AutoCompleteTextView notesETID;
    LinearLayout otherLLID;
    Button submitBtn;
    private KProgressHUD hud;
    String notesStr, dateStr, nextActIDStr, activityNameStr, otherStr;
    RelativeLayout dateRLID;
    Calendar calendar;
    private int year, month, day;
    TextView dateTVID;
    int repeatCount = 1;
    String nextActivityName;
    List<String> responseList = new ArrayList<>();
    boolean isOtherVisible = false;
    RelativeLayout okRLID, okDisRLID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_lead);

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                customerID = bundle.getString("CUSTOMER_ID");
                autoID = bundle.getString("AUTO_ID");
                titleID = bundle.getString("TITLE");
                activityNameStr = bundle.getString("ACTIVITY_NAME");
                emailStr = bundle.getString("LEAD_EMAIL");
                numberStr = bundle.getString("LEAD_MOBILE_NUMBER");
            }
        }

        headerTittleTVID.setText(titleID);
        userID = MySharedPreferences.getPreferences(LeadUpdateActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(LeadUpdateActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        nextActivity = findViewById(R.id.nextActivity);
        otherLLID = findViewById(R.id.otherLLID);
        otherETID = findViewById(R.id.otherETID);
        notesETID = findViewById(R.id.notesETID);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(LeadUpdateActivity.this, R.layout.item, R.id.text1, responseList);
        notesETID.setAdapter(adapter);
        notesStr = notesETID.getText().toString();
        loadAutoSuggestionsList(AppConstants.GLOBAL_MAIN_URL + "remarksautofill?keyword=" + notesStr);

        dateRLID = findViewById(R.id.dateRLID);
        dateTVID = findViewById(R.id.dateTVID);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateTVID.setText("" + year + "-" + (month + 1) + "-" + day);

        dateStr = "" + year + "-" + (month + 1) + "-" + day;
        dateRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LeadUpdateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateTVID.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dateStr = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                dateTVID.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }

                        }, year, month, day);
                datePickerDialog.show();

            }
        });

        activities();

        nextActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nextActIDStr = activityMainResponses.get(i).getActivity_id();

                if (activityMainResponses.get(i).getActivity_id().equals("1000")) {
                    otherLLID.setVisibility(View.VISIBLE);
                    isOtherVisible = true;
                } else {
                    isOtherVisible = false;
                    otherLLID.setVisibility(View.GONE);
                }

                nextActivityName = activityMainResponses.get(i).getActivity_name();

                if (repeatCount == 1) {
                    repeatCount++;
                    for (int j = 0; j < activityMainResponses.size(); j++) {
                        if (activityMainResponses.get(j).getActivity_name().equalsIgnoreCase(activityNameStr)) {
                            nextActivity.setSelection(j);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        okRLID = findViewById(R.id.okRLID);
        okDisRLID = findViewById(R.id.okDisRLID);
        okRLID.setVisibility(View.VISIBLE);
        okDisRLID.setVisibility(View.GONE);

        okRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesStr = notesETID.getText().toString();
                otherStr = otherETID.getText().toString();

                if (!isOtherVisible) {
                    if (titleID.equalsIgnoreCase("COMPLETE ACTIVITY")) {

                        okRLID.setVisibility(View.GONE);
                        okDisRLID.setVisibility(View.VISIBLE);
                        completeResponse(customerID, companyID, autoID);
                    } else if (titleID.equalsIgnoreCase("CANCEL ACTIVITY")) {
                        cancelResponse(customerID, companyID, autoID);
                    }
                } else {
                    if (!TextUtils.isEmpty(otherStr)) {
                        if (titleID.equalsIgnoreCase("COMPLETE ACTIVITY")) {
                            okRLID.setVisibility(View.GONE);
                            okDisRLID.setVisibility(View.VISIBLE);
                            completeResponse(customerID, companyID, autoID);
                        } else if (titleID.equalsIgnoreCase("CANCEL ACTIVITY")) {
                            cancelResponse(customerID, companyID, autoID);
                        }
                    } else {
                        Toast.makeText(LeadUpdateActivity.this, "Please enter Other Activity", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void activities() {
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<ActivityMainResponse>> call = apiInterface.getActivities(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ActivityMainResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivityMainResponse>> call, Response<ArrayList<ActivityMainResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    activityMainResponses = response.body();
                    if (activityMainResponses != null && activityMainResponses.size() > 0) {
                        for (int i = 0; i < activityMainResponses.size(); i++) {
                            aAdapter = new ActivitiesAdapter(LeadUpdateActivity.this, R.layout.custom_spinner_view, activityMainResponses);
                            nextActivity.setAdapter(aAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ActivityMainResponse>> call, Throwable t) {
                Toast.makeText(LeadUpdateActivity.this, "Activities not loading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAutoSuggestionsList(String url) {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response == null) {
                    Toast.makeText(LeadUpdateActivity.this, "No Suggestions Available", Toast.LENGTH_SHORT).show();
                } else if (response.equals("null")) {
                    Toast.makeText(LeadUpdateActivity.this, "No Suggestions Available", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int count = 0;


                        while (count < response.length()) {

                            JSONObject jo = response.getJSONObject(count);

                            String name = jo.getString("suggestions");
                            responseList.add(name);
                            count++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LeadUpdateActivity.this, "Suggestions not loading...", Toast.LENGTH_SHORT).show();
            }
        });

        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(LeadUpdateActivity.this).addToRequestQueue(arrayRequest, "");
    }

    private void cancelResponse(String customerID, String companyID, String autoID) {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllStatusResponse>> call = apiInterface.getUpdateCancelResponse(customerID, autoID, notesStr, nextActIDStr, dateStr, companyID, userID, "test", otherStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllStatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllStatusResponse>> call, Response<ArrayList<AllStatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<AllStatusResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus() == 1) {
                                okRLID.setVisibility(View.GONE);
                                okDisRLID.setVisibility(View.VISIBLE);
                                alertDisplayForTemplates("CANCEL");
                                //Toast.makeText(LeadUpdateActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                                okRLID.setVisibility(View.VISIBLE);
                                okDisRLID.setVisibility(View.GONE);
                                Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        okRLID.setVisibility(View.VISIBLE);
                        okDisRLID.setVisibility(View.GONE);
                        Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    okRLID.setVisibility(View.VISIBLE);
                    okDisRLID.setVisibility(View.GONE);
                    Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllStatusResponse>> call, Throwable t) {

                okRLID.setVisibility(View.VISIBLE);
                okDisRLID.setVisibility(View.GONE);
                dismissProgressDialog();
                Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void completeResponse(String customerID, String companyID, String autoID) {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllStatusResponse>> call = apiInterface.getUpdateCompleteResponse(customerID, autoID, notesStr, nextActIDStr, dateStr, companyID, userID, "test", otherStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllStatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllStatusResponse>> call, Response<ArrayList<AllStatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<AllStatusResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus() == 1) {

                                okRLID.setVisibility(View.GONE);
                                okDisRLID.setVisibility(View.VISIBLE);
                                alertDisplayForTemplates("COMPLETE");
                                //Toast.makeText(LeadUpdateActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                                okRLID.setVisibility(View.VISIBLE);
                                okDisRLID.setVisibility(View.GONE);
                                Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        okRLID.setVisibility(View.VISIBLE);
                        okDisRLID.setVisibility(View.GONE);
                        Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    okRLID.setVisibility(View.VISIBLE);
                    okDisRLID.setVisibility(View.GONE);
                    Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllStatusResponse>> call, Throwable t) {

                okRLID.setVisibility(View.VISIBLE);
                okDisRLID.setVisibility(View.GONE);
                dismissProgressDialog();
                Toast.makeText(LeadUpdateActivity.this, "Not updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void alertDisplayForTemplates(String type) {

        final BottomSheetDialog dialog = new BottomSheetDialog(LeadUpdateActivity.this, R.style.SheetDialog);
        dialog.setContentView(R.layout.alert_lead_update_templates);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        RelativeLayout skipRLID = dialog.findViewById(R.id.skipRLID);
        skipRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        RelativeLayout smsRLID = dialog.findViewById(R.id.smsRLID);
        RelativeLayout emailRLID = dialog.findViewById(R.id.emailRLID);
        RelativeLayout whatsAppRLID = dialog.findViewById(R.id.whatsAppRLID);

        smsRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadUpdateActivity.this, SendTemplatesActivity.class);
                intent.putExtra("TYPE", "Send Sms");
                intent.putExtra("LEAD_MOBILE_NUMBER", "" + numberStr);
                intent.putExtra("LEAD_EMAIL", "" + emailStr);
                intent.putExtra("LEAD_ID", "" + customerID);
                startActivity(intent);
                finish();
            }
        });

        emailRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadUpdateActivity.this, SendTemplatesActivity.class);
                intent.putExtra("TYPE", "Send Email");
                intent.putExtra("LEAD_MOBILE_NUMBER", "" + numberStr);
                intent.putExtra("LEAD_EMAIL", "" + emailStr);
                intent.putExtra("LEAD_ID", "" + customerID);
                startActivity(intent);
                finish();
            }
        });


        whatsAppRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadUpdateActivity.this, SendTemplatesActivity.class);
                intent.putExtra("TYPE", "Send Whats App");
                intent.putExtra("LEAD_MOBILE_NUMBER", "" + numberStr);
                intent.putExtra("LEAD_EMAIL", "" + emailStr);
                intent.putExtra("LEAD_ID", "" + customerID);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(LeadUpdateActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading..")
                    .setDetailsLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }

    }

    private void dismissProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }



}
