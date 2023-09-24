package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CustomKnowlarityApis;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.TeleCallersDataAdapter;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import crm.tranquil_sales_steer.ui.models.TeleCallersDataResponse;
import crm.tranquil_sales_steer.ui.models.TelecallersResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeleCallersDataActivity extends AppCompatActivity implements TeleCallersDataAdapter.TeleLeadClickListener {

    ArrayList<TeleCallersDataResponse> teleCallersDataResponses = new ArrayList<>();
    TeleCallersDataAdapter adapter;
    RelativeLayout backRLID,addTeleDataRLID;
    AppCompatTextView headerTVID;
    RecyclerView teleCallersRVID;
    ProgressBar pb;
    LinearLayoutManager layoutManager;
    LinearLayout noDataLLID;
    String title,source_id,userID;
    KProgressHUD hud;
    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;

    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_callers_data);
        Utilities.setStatusBarGradiant(this);

        analytics = FirebaseAnalytics.getInstance(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                title = bundle.getString("TITLE");
                source_id = bundle.getString("SOURCE_ID");
            }
        }

        MySharedPreferences.setPreference(this,AppConstants.PAGE_REFRESH,"NO");

        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);

        backRLID = findViewById(R.id.backRLID);
        headerTVID = findViewById(R.id.headerTVID);
        teleCallersRVID = findViewById(R.id.teleCallersRVID);
        pb = findViewById(R.id.pb);
        noDataLLID = findViewById(R.id.noDataLLID);
        addTeleDataRLID = findViewById(R.id.addTeleDataRLID);

        headerTVID.setText(title);

        backRLID.setOnClickListener(v -> {
            MySharedPreferences.setPreference(this,AppConstants.PAGE_REFRESH,"YES");
            Utilities.finishAnimation(this);
        });

        addTeleDataRLID.setOnClickListener(v -> {

            alert(source_id);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadTeleData();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadTeleData() {

        start = 0;
        isLoading=false;
        isLastPage=false;

        layoutManager = new LinearLayoutManager(TeleCallersDataActivity.this,LinearLayoutManager.VERTICAL,false);
        adapter = new TeleCallersDataAdapter(teleCallersDataResponses,TeleCallersDataActivity.this,TeleCallersDataActivity.this);

        teleCallersRVID.setAdapter(adapter);
        teleCallersRVID.setLayoutManager(layoutManager);

        teleCallersRVID.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("pagination","api called");
                isLastPage =false;
                isLoading = true;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                Log.d("pagination","is last page");
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                Log.d("pagination","is loading");
                return isLoading;
            }
        });

        loadFirstPage();

        pb.setVisibility(View.VISIBLE);
        teleCallersRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);

    }

    private void loadFirstPage() {

        pb.setVisibility(View.VISIBLE);
        teleCallersRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
        isClickable = false;

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<TeleCallersDataResponse>> call = apiInterface.getTeleDta(source_id,userID,start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<TeleCallersDataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TeleCallersDataResponse>> call, Response<ArrayList<TeleCallersDataResponse>> response) {
                pb.setVisibility(View.GONE);
                teleCallersRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    //teleCallersDataResponses.clear();
                    teleCallersDataResponses = response.body();
                    if (teleCallersDataResponses.size() > 0) {
                        isClickable = true;
                        startedValues();
                        adapter.addAll(teleCallersDataResponses);

                        if (teleCallersDataResponses.size() == 30) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        setErrorViews();
                    }
                } else {
                    setErrorViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TeleCallersDataResponse>> call, Throwable t) {
                setErrorViews();
            }
        });

    }

    private void loadNextPage() {
        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<TeleCallersDataResponse>> call = apiInterface.getTeleDta(source_id,userID,start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<TeleCallersDataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TeleCallersDataResponse>> call, Response<ArrayList<TeleCallersDataResponse>> response) {
                isClickable = true;
                if (response.body() != null && response.code() == 200) {
                    teleCallersDataResponses = response.body();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (teleCallersDataResponses.size() > 0) {
                        adapter.addAll(teleCallersDataResponses);

                        if (teleCallersDataResponses.size() == 30) {
                            startedValues();
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            adapter.removeLoadingFooter();
                        }
                    } else {
                        adapter.removeLoadingFooter();
                    }
                } else {
                    adapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TeleCallersDataResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        teleCallersRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 30;
    }


    private void alert(String sourceID) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_telecaller_data);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        AppCompatEditText nameETID = dialog.findViewById(R.id.nameETID);
        AppCompatEditText mobileNumberETID = dialog.findViewById(R.id.mobileNumberETID);
        AppCompatEditText emailETID = dialog.findViewById(R.id.emailETID);
        AppCompatEditText companyETID = dialog.findViewById(R.id.companyETID);

        Button submitBtn = dialog.findViewById(R.id.submitBtn);

        mobileNumberETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                if (s.length() == 10) {
                    searchCustomer(number,submitBtn);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = nameETID.getText().toString();
                String numberStr = mobileNumberETID.getText().toString();
                String emailStr = emailETID.getText().toString();
                String companyStr = companyETID.getText().toString();

                if (TextUtils.isEmpty(nameStr)) {
                    Toast.makeText(TeleCallersDataActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(numberStr)) {
                    Toast.makeText(TeleCallersDataActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (numberStr.startsWith("0") || numberStr.startsWith("+91")) {
                    numberStr = "" + numberStr.substring(1);
                }


                if (Utilities.isNetworkAvailable(TeleCallersDataActivity.this)) {
                    //Toast.makeText(CallersDataMain.this, "" + numberStr, Toast.LENGTH_SHORT).show();
                    insertDetails(userID, sourceID, nameStr, numberStr, emailStr,companyStr, dialog);
                } else {
                    Utilities.AlertDaiolog(TeleCallersDataActivity.this, "No internet....!", "Make sure your device is connected to internet", 0, null, "OK");
                }
            }
        });
    }

    private void searchCustomer(String number, Button submitBtn) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.checkCustomerNumber(number);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if (response.body() != null && response.code() == 200) {

                    StatusResponse statusResponse = response.body();
                    if (statusResponse.getStatus().equalsIgnoreCase("0")){
                        submitBtn.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(TeleCallersDataActivity.this, "Mobile Number Already Exist", Toast.LENGTH_SHORT).show();
                        submitBtn.setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });
    }

    private void insertDetails(String userID, String sourceID, String nameStr, String numberStr, String emailStr, String companyStr, Dialog dialog) {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<TelecallersResponse>> call = apiInterface.addTeleCallerData(userID, sourceID, nameStr, numberStr, emailStr,companyStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<TelecallersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TelecallersResponse>> call, retrofit2.Response<ArrayList<TelecallersResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<TelecallersResponse> telecallersResponses = response.body();
                    if (telecallersResponses.size() > 0) {
                        for (int i = 0; i < telecallersResponses.size(); i++) {
                            if (telecallersResponses.get(i).getStatus().equals("1")) {

                                teleCallersDataResponses.clear();
                                loadTeleData();
                                dialog.dismiss();
                                Toast.makeText(TeleCallersDataActivity.this, "" + telecallersResponses.get(i).getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(TeleCallersDataActivity.this, "" + telecallersResponses.get(i).getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(TeleCallersDataActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TeleCallersDataActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TelecallersResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(TeleCallersDataActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTeleLeadItemClick(TeleCallersDataResponse response, View v, int pos, TeleCallersDataAdapter.TeleCallersVH holder) {
        switch (v.getId()) {
            case R.id.newPhoneRL:

                //CommunicationsServices.InsertCommunication(TeleCallersDataActivity.this, "1", response.getCaller_id(), userID, "", "");

              /* Intent intent = new Intent(TeleCallersDataActivity.this,TeleCallCompleteActivity.class);
                intent.putExtra("NAME",response.getLead_name());
                intent.putExtra("CALLER_ID",response.getCaller_id());
                intent.putExtra("SOURCE_ID",source_id);
                intent.putExtra("ASSIGNED_TO",response.getAssigned_to());
                intent.putExtra("MOBILE",response.getLead_mobile());
                startActivity(intent);*/

            //    numberCalling(response.getLead_mobile());

              getCallerID("1","1",response.getAssigned_to(),response.getLead_name(),response.getLead_name(),response.getLead_mobile(),response.getCaller_id());



                break;
        }
    }


    private void getCallerID(String s, String type, String asaignedID, String activity_name, String first_name, String mobile_number, String customerID) {

        Log.e("id's==>","" + asaignedID +  ", " + customerID);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(customerID,"","","0",userID,false);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationsResponse>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ArrayList<CommunicationsResponse>> call, Response<ArrayList<CommunicationsResponse>> response) {

                // Utilities.dismissProgress(kProgressHUD);

                if (response.body() != null && response.code() == 200) {
                    ArrayList<CommunicationsResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {

                            if (statusResponses.get(i).getStatus().equals("1")) {

                                if (type.equalsIgnoreCase("1")){

                                    Intent intentCall = new Intent(TeleCallersDataActivity.this,TeleCallCompleteActivity.class);
                                    intentCall.putExtra("SOURCE_ID",source_id);
                                    intentCall.putExtra("ASSIGNED_TO",asaignedID);
                                    intentCall.putExtra("NAME",first_name);
                                    intentCall.putExtra("MOBILE",mobile_number);
                                    intentCall.putExtra("CALLER_ID",customerID);
                                    intentCall.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    startActivity(intentCall);
                                    finish();

                                    numberCalling(mobile_number);

                                }else if (type.equalsIgnoreCase("2")){

                                    CustomKnowlarityApis.clickToKnowlarityCall(TeleCallersDataActivity.this,userID,mobile_number,hud);

                                    Intent intentCall = new Intent(TeleCallersDataActivity.this,TeleCallCompleteActivity.class);
                                    intentCall.putExtra("SOURCE_ID",source_id);
                                    intentCall.putExtra("ASSIGNED_TO",asaignedID);
                                    intentCall.putExtra("NAME",first_name);
                                    intentCall.putExtra("MOBILE",mobile_number);
                                    intentCall.putExtra("CALLER_ID",customerID);
                                    intentCall.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    startActivity(intentCall);
                                    finish();

                                }




                                Log.e("caller_id","" + statusResponses.get(i).getCall_id());


                                Log.d("INSERT_MESSAGE", "" + type + " : Inserted");

                            } else {

                                Utilities.AlertDaiolog(TeleCallersDataActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            }
                        }
                    } else {
                        Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                        Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                    }
                } else {

                    Utilities.AlertDaiolog(TeleCallersDataActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                    Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                    Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {
                Utilities.AlertDaiolog(TeleCallersDataActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
            }
        });
    }


    private void numberCalling(String number) {

   /*     try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
                // Log.d("CONSTRUCTOR_ID_EXE_1->", "" + callID);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/




        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
                if (telecomManager != null && telecomManager.getDefaultOutgoingPhoneAccount(PhoneAccount.SCHEME_TEL) != null) {
                    callIntent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, telecomManager.getDefaultOutgoingPhoneAccount(PhoneAccount.SCHEME_TEL));
                }

                startActivity(callIntent);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
//CommunicationsServices
    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(this)
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

    @Override
    protected void onResume() {
        super.onResume();

        String pageRefresh = MySharedPreferences.getPreferences(TeleCallersDataActivity.this,AppConstants.PAGE_REFRESH);

        if (pageRefresh.equalsIgnoreCase("YES")){
            teleCallersDataResponses.clear();
            loadTeleData();
        }

        /*teleCallersDataResponses.clear();
        loadTeleData();*/
    }
}