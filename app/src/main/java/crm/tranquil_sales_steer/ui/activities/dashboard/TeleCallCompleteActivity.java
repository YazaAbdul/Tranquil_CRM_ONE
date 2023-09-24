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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.HandlerUtility;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.NextTeleCallersResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import crm.tranquil_sales_steer.ui.models.TeleResponse;
import crm.tranquil_sales_steer.ui.models.TelecallersResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeleCallCompleteActivity extends AppCompatActivity {

    AppCompatTextView headerTVID;
    String callerID,leadName,leadMobile,sourceID,assignedID,CALLID;
    AppCompatButton convertBtn,deleteBtn,skipBtn;
    private KProgressHUD hud;

    TextView newLeadNameTVID,leadMobileTVID;
    LinearLayout callLLID,activitiesLLID;
    String nextCallerID,nextLeadName,nextLeadMobile,nextAssignedTo,userID,userType;
    RelativeLayout createLeadRLID;
    ProgressBar pb;
    CardView nextLeadCVID;

    private FirebaseAnalytics analytics;

    String callStartStr, callEndStr, callMinutesStr,callSecondsStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_call_complete);
        Utilities.setStatusBarGradiant(this);

        analytics = FirebaseAnalytics.getInstance(this);

        HandlerUtility.insertHandler= uiHandler;

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                callerID = bundle.getString("CALLER_ID");
                leadName = bundle.getString("NAME");
                leadMobile = bundle.getString("MOBILE");
                assignedID = bundle.getString("ASSIGNED_TO");
                sourceID = bundle.getString("SOURCE_ID");
            }
        }

        Log.e("source==>","" + sourceID + ", " + assignedID+ ", " + callerID);



        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);


        headerTVID = findViewById(R.id.headerTVID);

        convertBtn = findViewById(R.id.convertBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        skipBtn = findViewById(R.id.skipBtn);

        newLeadNameTVID = findViewById(R.id.newLeadNameTVID);
        leadMobileTVID = findViewById(R.id.leadMobileTVID);
        callLLID = findViewById(R.id.callLLID);
        activitiesLLID = findViewById(R.id.activitiesLLID);
        createLeadRLID = findViewById(R.id.createLeadRLID);
        nextLeadCVID = findViewById(R.id.nextLeadCVID);
        pb = findViewById(R.id.pb);

        headerTVID.setText(leadName);

        convertBtn.setOnClickListener(v -> {
            loadConverting();

            //loadCallCompleted();


        });

        createLeadRLID.setOnClickListener(v -> {
            //loadConverting();

            alert(sourceID);
        });

        deleteBtn.setOnClickListener(v -> {
            loadDeleting();
        });

        skipBtn.setOnClickListener(v -> {
            loadSkipping();
            //loadCallCompleted();
        });

        activitiesLLID.setOnClickListener(v -> {

            MySharedPreferences.setPreference(TeleCallCompleteActivity.this,AppConstants.PAGE_REFRESH,"YES");

            finish();
            //loadCallCompleted();
        });


        if (Utilities.isNetworkAvailable(this)){
            loadNextTeleCaller();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }

        callLLID.setOnClickListener(v -> {
            nextTeleCalling();
            //loadCallCompleted();
        });


        if (Utilities.isNetworkAvailable(this)){
            loadCallID();
        }else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }



    }

    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    try {
                        JSONObject obj = (JSONObject) msg.obj;
                        Log.e("obj==>",""+obj.toString());
                        if (callEndStr != null) {

                            Log.e("error==>",""+callEndStr);

                        } else {

                            callMinutesStr = obj.getString("minutes");
                            callSecondsStr = obj.getString("seconds");
                            callStartStr = obj.getString("start_time");
                            callEndStr = obj.getString("end_time");

                           /*Date currentDate = new Date (callEndStr);
                           SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                           String date = dateFormat.format(currentDate);*/


                            Log.e("Caller_Details==>",""+callMinutesStr+", " + callSecondsStr+", " + callStartStr+", " + callEndStr);


                            loadCallCompleted();

                            //callCompleteResponse("0","","0");

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };



    private void loadCallID() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(callerID,"","","0",userID,false);
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

                                //Log.e("Before_handler_caller_id==>","" + statusResponses.get(i).getCall_id());

                                CALLID = statusResponses.get(i).getCall_id();

                                Log.e("caller_id","" + CALLID);

                                if (HandlerUtility.insertHandler!=null){
                                    try {
                                        JSONObject obj = new JSONObject();
                                        obj.put("id",statusResponses.get(0).getCall_id());
                                        Log.e("caller_id","" + statusResponses.get(i).getCall_id());
                                        HandlerUtility.insertHandler.obtainMessage(12,obj).sendToTarget();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {

                            }
                        }
                    } else {

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {

            }
        });
    }

    private void loadCallCompleted() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.getTeleCallCompleted(CALLID,callStartStr,callEndStr,userID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                Utilities.dismissProgress(hud);

                if (response.body() != null && response.code() == 200){

                    StatusResponse teleResponse = response.body();

                    if (teleResponse.getStatus().equalsIgnoreCase("1")){

                        /*Intent intent = new Intent(TeleCallCompleteActivity.this,TeleCallCompleteActivity.class);
                        intent.putExtra("NAME",nextLeadName);
                        intent.putExtra("CALLER_ID",nextCallerID);
                        intent.putExtra("SOURCE_ID",sourceID);
                        intent.putExtra("ASSIGNED_TO",nextAssignedTo);
                        intent.putExtra("MOBILE",nextLeadMobile);
                        startActivity(intent);
                        finish();*/

                        //nextTeleCalling();

                    }else {

                        Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Utilities.dismissProgress(hud);

                    }

                }else {

                    Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Utilities.dismissProgress(hud);
                }

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Utilities.dismissProgress(hud);
            }
        });


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
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = nameETID.getText().toString();
                String numberStr = mobileNumberETID.getText().toString();
                String emailStr = emailETID.getText().toString();
                String companyStr = companyETID.getText().toString();

                if (TextUtils.isEmpty(nameStr)) {
                    Toast.makeText(TeleCallCompleteActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(numberStr)) {
                    Toast.makeText(TeleCallCompleteActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (numberStr.startsWith("0") || numberStr.startsWith("+91")) {
                    numberStr = "" + numberStr.substring(1);
                }


                if (Utilities.isNetworkAvailable(TeleCallCompleteActivity.this)) {
                    //Toast.makeText(CallersDataMain.this, "" + numberStr, Toast.LENGTH_SHORT).show();
                    insertDetails(userID, sourceID, nameStr, numberStr, emailStr,companyStr, dialog);
                } else {
                    Utilities.AlertDaiolog(TeleCallCompleteActivity.this, "No internet....!", "Make sure your device is connected to internet", 0, null, "OK");
                }
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
                                //loadTeleData();
                                dialog.dismiss();
                                Toast.makeText(TeleCallCompleteActivity.this, "" + telecallersResponses.get(i).getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(TeleCallCompleteActivity.this, "" + telecallersResponses.get(i).getMsg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else {
                        Toast.makeText(TeleCallCompleteActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TeleCallCompleteActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TelecallersResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(TeleCallCompleteActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void loadNextTeleCaller() {

        RequestBody reqFile,requestAssigned,requestCaller;
        final MultipartBody.Part source,assigned,caller;

      /*  String s = new String(sourceID);
        String a = new String(assignedID);
        String c = new String(callerID);

        reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), s);
        source = MultipartBody.Part.createFormData("src_id", "", reqFile);

        requestAssigned = RequestBody.create(MediaType.parse("multipart/form-data"), a);
        assigned = MultipartBody.Part.createFormData("assigned_to", "", requestAssigned);

        requestCaller = RequestBody.create(MediaType.parse("multipart/form-data"), a);
        caller = MultipartBody.Part.createFormData("assigned_to", "", requestAssigned);*/

        pb.setVisibility(View.VISIBLE);
        nextLeadCVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<NextTeleCallersResponse>> call = apiInterface.getNextCallersData(sourceID,assignedID,callerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NextTeleCallersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NextTeleCallersResponse>> call, Response<ArrayList<NextTeleCallersResponse>> response) {

                pb.setVisibility(View.GONE);
                nextLeadCVID.setVisibility(View.VISIBLE);

                Log.e("response==>", "" + response.body());

                if (response.body() != null && response.code() == 200){

                    ArrayList<NextTeleCallersResponse> nextTeleCallersResponses = new ArrayList<>();
                    nextTeleCallersResponses = response.body();

                    if (nextTeleCallersResponses.size()> 0) {
                        newLeadNameTVID.setText(nextTeleCallersResponses.get(0).getLead_name());
                        leadMobileTVID.setText(nextTeleCallersResponses.get(0).getLead_mobile());
                        nextCallerID = nextTeleCallersResponses.get(0).getCaller_id();
                        nextLeadName = nextTeleCallersResponses.get(0).getLead_name();
                        nextLeadMobile = nextTeleCallersResponses.get(0).getLead_mobile();
                        nextAssignedTo = nextTeleCallersResponses.get(0).getAssigned_to();
                    } else {
                        skipBtn.setVisibility(View.GONE);
                    }

                    Log.e("Next_Mobile==>",""+ nextLeadMobile);

                }else {
                    pb.setVisibility(View.GONE);
                    nextLeadCVID.setVisibility(View.GONE);
                    skipBtn.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<NextTeleCallersResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                nextLeadCVID.setVisibility(View.GONE);
                skipBtn.setVisibility(View.GONE);

            }
        });
    }

    private void nextTeleCalling() {

        //CommunicationsServices.InsertCommunication(TeleCallCompleteActivity.this, "0",nextCallerID, userID, "", "");

        Intent intent = new Intent(TeleCallCompleteActivity.this,TeleCallCompleteActivity.class);
        intent.putExtra("NAME",nextLeadName);
        intent.putExtra("CALLER_ID",nextCallerID);
        intent.putExtra("SOURCE_ID",sourceID);
        intent.putExtra("ASSIGNED_TO",nextAssignedTo);
        intent.putExtra("MOBILE",nextLeadMobile);
        startActivity(intent);
        finish();

      numberCalling(nextLeadMobile);

    }

    private void numberCalling(String number) {

     /*   try {
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

    private void loadSkipping() {

        Utilities.showProgress(hud,this,"Loading","Skipping");

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<TeleResponse> call = apiInterface.getTeleCallerSkip(callerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<TeleResponse>() {
            @Override
            public void onResponse(Call<TeleResponse> call, Response<TeleResponse> response) {

                Utilities.dismissProgress(hud);

                if (response.body() != null && response.code() == 200){

                    TeleResponse teleResponse = response.body();

                    if (teleResponse.getMsg().equalsIgnoreCase("1")){

                        /*Intent intent = new Intent(TeleCallCompleteActivity.this,TeleCallCompleteActivity.class);
                        intent.putExtra("NAME",nextLeadName);
                        intent.putExtra("CALLER_ID",nextCallerID);
                        intent.putExtra("SOURCE_ID",sourceID);
                        intent.putExtra("ASSIGNED_TO",nextAssignedTo);
                        intent.putExtra("MOBILE",nextLeadMobile);
                        startActivity(intent);
                        finish();*/

                        nextTeleCalling();

                    }else {

                        Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Utilities.dismissProgress(hud);

                    }

                }else {

                    Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Utilities.dismissProgress(hud);
                }

            }

            @Override
            public void onFailure(Call<TeleResponse> call, Throwable t) {

                Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Utilities.dismissProgress(hud);
            }
        });
    }

    private void loadDeleting() {

        Utilities.showProgress(hud,this,"Loading","Deleting");

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<TeleResponse> call = apiInterface.getTeleCallerDelete(callerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<TeleResponse>() {
            @Override
            public void onResponse(Call<TeleResponse> call, Response<TeleResponse> response) {

                Utilities.dismissProgress(hud);

                if (response.body() != null && response.code() == 200){

                    TeleResponse teleResponse = response.body();

                    if (teleResponse.getMsg().equalsIgnoreCase("1")){

                        /*Intent intent = new Intent(TeleCallCompleteActivity.this,TeleCallCompleteActivity.class);
                        intent.putExtra("NAME",nextLeadName);
                        intent.putExtra("CALLER_ID",nextCallerID);
                        intent.putExtra("SOURCE_ID",sourceID);
                        intent.putExtra("ASSIGNED_TO",nextAssignedTo);
                        intent.putExtra("MOBILE",nextLeadMobile);
                        startActivity(intent);
                        finish();*/

                        nextTeleCalling();

                    }else {

                        Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Utilities.dismissProgress(hud);

                    }

                }else {

                    Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Utilities.dismissProgress(hud);
                }

            }

            @Override
            public void onFailure(Call<TeleResponse> call, Throwable t) {

                Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Utilities.dismissProgress(hud);

            }
        });
    }

    private void loadConverting() {

        Utilities.showProgress(hud,this,"Loading","Converting");

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<TeleResponse> call = apiInterface.getTeleCallerConvert(callerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<TeleResponse>() {
            @Override
            public void onResponse(Call<TeleResponse> call, Response<TeleResponse> response) {

                Utilities.dismissProgress(hud);

                if (response.body() != null && response.code() == 200){

                    TeleResponse teleResponse = response.body();

                    if (teleResponse.getMsg().equalsIgnoreCase("1")){

                        Intent intent = new Intent(TeleCallCompleteActivity.this,LeadCreateActivity.class);
                        intent.putExtra("Name",leadName);
                        intent.putExtra("Mobile",leadMobile);
                        intent.putExtra("TeleCallLead","YES");
                        intent.putExtra("TELE_NAME",nextLeadName);
                        intent.putExtra("CALLER_ID",callerID);
                        intent.putExtra("TELE_CALLER_ID",nextCallerID);
                        intent.putExtra("TELE_SOURCE_ID",sourceID);
                        intent.putExtra("TELE_ASSIGNED_TO",nextAssignedTo);
                        intent.putExtra("TELE_MOBILE",nextLeadMobile);
                        startActivity(intent);

                        Utilities.finishAnimation(TeleCallCompleteActivity.this);

                    }else {

                        Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Utilities.dismissProgress(hud);

                    }

                }else {
                    Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Utilities.dismissProgress(hud);
                }

            }

            @Override
            public void onFailure(Call<TeleResponse> call, Throwable t) {

                Toast.makeText(TeleCallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                Utilities.dismissProgress(hud);

            }
        });
    }



    @Override
    public void onBackPressed() {
       // super.onBackPressed();
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(TeleCallCompleteActivity.this)
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