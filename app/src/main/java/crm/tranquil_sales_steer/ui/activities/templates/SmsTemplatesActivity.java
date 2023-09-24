package crm.tranquil_sales_steer.ui.activities.templates;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsTemplatesActivity extends AppCompatActivity {

    ListView availableLVID;
    ProgressBar pb;
    TextView noData;
    ImageView noDataImage;
    SmsTemplatesAdapter adapter;
    ArrayList<SmsTemplatesResponse> performanceResponses = new ArrayList<>();
    String companyID, userID;
    private KProgressHUD hud;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_templates);

        //statusBarUtilities.statusBarSetup(this);

        userID = MySharedPreferences.getPreferences(SmsTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(SmsTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("SMS Templates");
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });

        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTemplate();
            }
        });

        availableLVID = findViewById(R.id.availableLVID);
        pb = findViewById(R.id.pb);
        noData = findViewById(R.id.noData);
        noDataImage = findViewById(R.id.noDataImage);

        if (Utilities.isNetworkAvailable(SmsTemplatesActivity.this)) {
            loaProjects();
        } else {
            Utilities.AlertDaiolog(SmsTemplatesActivity.this, "No Internet", "Please check your internet connection...", 1, null, "OK");
        }
    }

    @SuppressLint("SetTextI18n")
    private void addTemplate() {
        final Dialog dialog = new Dialog(SmsTemplatesActivity.this);
        dialog.setContentView(R.layout.add_templates);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        Button submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setText("Save");

        LinearLayout headerID = dialog.findViewById(R.id.headerID);
        headerID.setVisibility(View.VISIBLE);

        TextView titleID = dialog.findViewById(R.id.titleID);
        titleID.setText("Create Sms Template");

        TextInputLayout subject = dialog.findViewById(R.id.subject);
        subject.setVisibility(View.GONE);
        TextInputLayout attachment = dialog.findViewById(R.id.attachment);
        attachment.setVisibility(View.GONE);
        LinearLayout attachmentLLID = dialog.findViewById(R.id.uploadPicLLID);
        attachmentLLID.setVisibility(View.GONE);

        final TextInputEditText titleETID = dialog.findViewById(R.id.tittleETID);
        final TextInputEditText messageETID = dialog.findViewById(R.id.messageETID);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleETID.getText().toString();
                String message = messageETID.getText().toString();

                int count = 0;

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(SmsTemplatesActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(SmsTemplatesActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }

                if (count == 2) {
                    if (Utilities.isNetworkAvailable(SmsTemplatesActivity.this)) {
                        createSms(dialog, title, message, companyID, userID);
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(SmsTemplatesActivity.this, "No Internet..", "Make sure your device is connected to internet");
                    }
                }
            }
        });
    }

    private void createSms(final Dialog dialog, String title, String message, String companyID, String userID) {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        loaProjects();
        Call<ArrayList<StatusResponse>> call = apiInterface.getCreateSms(title, message, companyID, userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {
                                dialog.dismiss();
                                loaProjects();
                                AlertUtilities.EmailSuccessAlertDialog(SmsTemplatesActivity.this, "SMS Template Added Successfully");
                            } else {
                                Toast.makeText(SmsTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(SmsTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SmsTemplatesActivity.this, "Error at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(SmsTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

    private void loaProjects() {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SmsTemplatesResponse>> call = apiInterface.getSmsTemplates(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SmsTemplatesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SmsTemplatesResponse>> call, Response<ArrayList<SmsTemplatesResponse>> response) {
                pb.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    performanceResponses.clear();
                    performanceResponses = response.body();
                    if (performanceResponses.size() > 0) {
                        for (int i = 0; i < performanceResponses.size(); i++) {
                            adapter = new SmsTemplatesAdapter(SmsTemplatesActivity.this, performanceResponses);
                            availableLVID.setAdapter(adapter);
                        }
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        noDataImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SmsTemplatesResponse>> call, Throwable t) {
                noData.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                noDataImage.setVisibility(View.VISIBLE);
            }
        });
    }

    public class SmsTemplatesResponse {

        /*sms_id: "1",
sms_title: "Sample Template 1",
sms_message:*/

        private String sms_id;
        private String sms_title;
        private String sms_message;

        public SmsTemplatesResponse(String sms_id, String sms_title, String sms_message) {
            this.sms_id = sms_id;
            this.sms_title = sms_title;
            this.sms_message = sms_message;
        }

        public String getSms_id() {
            return sms_id;
        }

        public void setSms_id(String sms_id) {
            this.sms_id = sms_id;
        }

        public String getSms_title() {
            return sms_title;
        }

        public void setSms_title(String sms_title) {
            this.sms_title = sms_title;
        }

        public String getSms_message() {
            return sms_message;
        }

        public void setSms_message(String sms_message) {
            this.sms_message = sms_message;
        }
    }

    public class SmsTemplatesAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<SmsTemplatesResponse> availabilityList = new ArrayList<>();

        public SmsTemplatesAdapter(Activity activity, ArrayList<SmsTemplatesResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return availabilityList.size();
        }

        @Override
        public Object getItem(int i) {
            return availabilityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.new_availability_list_item, null);

            if (i % 2 == 1) {
                view.setBackgroundColor(getResources().getColor(R.color.edit_d));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }


            TextView projectNameTVID = view.findViewById(R.id.projectNameTVID);
            projectNameTVID.setText(availabilityList.get(i).getSms_title());

            RelativeLayout mainDisplayRLID = view.findViewById(R.id.mainDisplayRLID);

            mainDisplayRLID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDisplayMessage(availabilityList.get(i).getSms_title(), availabilityList.get(i).getSms_id(), availabilityList.get(i).getSms_message());

                }
            });

            return view;
        }
    }

    @SuppressLint("SetTextI18n")
    private void alertDisplayMessage(String sms_title, final String sms_id, String sms_message) {
        final Dialog dialog = new Dialog(SmsTemplatesActivity.this);
        dialog.setContentView(R.layout.add_templates);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        Button submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setText("Save");

        TextInputLayout subject = dialog.findViewById(R.id.subject);
        subject.setVisibility(View.GONE);
        TextInputLayout attachment = dialog.findViewById(R.id.attachment);
        attachment.setVisibility(View.GONE);
        LinearLayout attachmentLLID = dialog.findViewById(R.id.uploadPicLLID);
        attachmentLLID.setVisibility(View.GONE);

        final TextInputEditText titleETID = dialog.findViewById(R.id.tittleETID);
        final TextInputEditText messageETID = dialog.findViewById(R.id.messageETID);

        LinearLayout headerID = dialog.findViewById(R.id.headerID);
        headerID.setVisibility(View.VISIBLE);

        TextView titleID = dialog.findViewById(R.id.titleID);
        titleID.setText("Edit Sms Template");

        titleETID.setText(sms_title);
        messageETID.setText(sms_message);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleETID.getText().toString();
                String message = messageETID.getText().toString();

                int count = 0;

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(SmsTemplatesActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(SmsTemplatesActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }

                if (count == 2) {
                    if (Utilities.isNetworkAvailable(SmsTemplatesActivity.this)) {
                        editSms(sms_id, dialog, title, message, companyID, userID);
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(SmsTemplatesActivity.this, "No Internet..", "Make sure your device is connected to internet");
                    }
                }
            }
        });


    }

    private void editSms(String sms_id, final Dialog dialog, String title, String message, String companyID, String userID) {

        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        loaProjects();
        Call<ArrayList<StatusResponse>> call = apiInterface.getEditSms(title, message, companyID, userID, sms_id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {
                                dialog.dismiss();
                                performanceResponses.clear();
                                loaProjects();
                                AlertUtilities.EmailSuccessAlertDialog(SmsTemplatesActivity.this, "SMS Template Edited Successfully");
                            } else {
                                Toast.makeText(SmsTemplatesActivity.this, "Edited sms templates Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(SmsTemplatesActivity.this, "Edited sms templates Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SmsTemplatesActivity.this, "Error at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(SmsTemplatesActivity.this, "Edited sms templates Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(SmsTemplatesActivity.this)
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


