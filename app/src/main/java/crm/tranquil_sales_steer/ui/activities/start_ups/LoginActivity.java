package crm.tranquil_sales_steer.ui.activities.start_ups;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.DashBoardActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.SingUp;
import crm.tranquil_sales_steer.ui.models.GetBaseUrlResponse;
import crm.tranquil_sales_steer.ui.models.GetCompanyUserdlt;
import crm.tranquil_sales_steer.ui.models.Versioncontrol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    AppCompatEditText emailETID, passwordETID,companyETID;
    AppCompatButton registerBtn;
    String emailStr, passwordStr, deviceID,android_id,clientId;
    private KProgressHUD hud;
    String version;
    TextView vTVID;
    ArrayList<GetBaseUrlResponse> getBaseUrlResponses;
    LinearLayout emailLLID,passwordLLID,companyLLID;
    private SharedPreferences sharedPreferences;
    AppCompatImageView logo;
   int properkey=0;
   AppCompatTextView singupTVID;
   String sinupstatus;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;


    @SuppressLint({"HardwareIds", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MySharedPreferences.clearPreferences(this);

        /*sinupstatus="0";
        Toast.makeText(this, ""+sinupstatus, Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();

       if(intent!=null){
           sinupstatus=intent.getStringExtra("SINGUP");
       }else{
           sinupstatus="100";
       }*/




Log.e("sinupstatus", ""+sinupstatus);




        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);

        deviceID = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        emailETID = findViewById(R.id.emailETID);
        companyLLID = findViewById(R.id.companyLLID);
        companyETID = findViewById(R.id.companyETID);
        passwordETID = findViewById(R.id.passwordETID);
        vTVID = findViewById(R.id.vTVID);
        emailLLID = findViewById(R.id.emailLLID);
        passwordLLID = findViewById(R.id.passwordLLID);
        logo = findViewById(R.id.logo);
        registerBtn = findViewById(R.id.registerBtn);
        singupTVID = findViewById(R.id.singupTVID);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                sinupstatus = bundle.getString("SINGUP");


                if (sinupstatus != null && sinupstatus.equalsIgnoreCase("YES") ) {

                    sinupstatus = bundle.getString("SINGUP");

                } else {

                }
            }
        }
        Log.e("sinupstatus", ""+ sinupstatus);



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        version="2";

        vTVID.setText("V E R S I O N:- "+version);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.e("device_id==>", "" + android_id);





        if (sinupstatus != null && sinupstatus.equalsIgnoreCase("YES")){
            companyLLID.setVisibility(View.GONE);
            emailLLID.setVisibility(View.VISIBLE);
            passwordLLID.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.VISIBLE);


        }else {
            companyLLID.setVisibility(View.VISIBLE);
            emailLLID.setVisibility(View.GONE);
            passwordLLID.setVisibility(View.GONE);
            registerBtn.setVisibility(View.GONE);

        }








        //emailETID.setText("satheesh@tranquilwebsolutions.com.com");
        //passwordETID.setText("123");
      //  getcompanyuserdlt();
        checkcamerapermissionAndOpenCamera();

        companyETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key=s.toString();
                if(!key.equals("")){
                    secret_key(key);
                }


                else{
                    Toast.makeText(LoginActivity.this, "Enter a Key", Toast.LENGTH_SHORT).show();
                    emailLLID.setVisibility(View.GONE);
                    passwordLLID.setVisibility(View.GONE);
                    /*  registerBtn.setVisibility(View.GONE);*/
                }


            }
        });


      //  getCompanylogo();



        singupTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SingUp.class);
                startActivity(intent);
            }
        });
        clientId = companyETID.getText().toString();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(LoginActivity.this)) {




                   // CheckVerionControl();
                    userInputValidations();

                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, getResources().getString(R.string.no_internet_title), getResources().getString(R.string.no_internet_des));
                }
            }
        });

      /*  if(sinupstatus=="0"){
            companyLLID.setVisibility(View.GONE);
            emailLLID.setVisibility(View.VISIBLE);
            passwordLLID.setVisibility(View.VISIBLE);
            registerBtn.setVisibility(View.VISIBLE);
        }*/





        /*companyETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    String key=s.toString();
                    secret_key(key);
            }
        });*/


       /* signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                //startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });*/

    }

    public void checkcamerapermissionAndOpenCamera(){
        if(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);

        }else {
         /*   Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 0);*/
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }else{
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();

            }

        }
    }
    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);

    }



    private void userInputValidations() {
        emailStr = emailETID.getText().toString();
        clientId = companyETID.getText().toString();
        passwordStr = passwordETID.getText().toString();

        int count = 0;

        if (TextUtils.isEmpty(emailStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Email-id can't be empty", "Enter registered Email-id");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(passwordStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Password can't be empty", "Enter your password");
            return;
        } else {
            count++;
        }
        if (TextUtils.isEmpty(clientId)) {
            AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Company ID can't be empty", "Enter Company ID");
            return;
        } else {
            count++;
        }

        if (count == 3) {
            getLoginResponse();
        } else {
            AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Some fields are missing", "All fields are mandatory");
        }
    }

/*    private void secret_key(String key){
        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetBaseUrlResponse>> call=apiInterface.getBaseUrl(key);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetBaseUrlResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetBaseUrlResponse>> call, Response<ArrayList<GetBaseUrlResponse>> response) {
                Toast.makeText(LoginActivity.this, "onREsponse", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<GetBaseUrlResponse>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "onFail", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

private void getcompanyuserdlt(){
    ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
    Call<ArrayList<GetCompanyUserdlt>> call=apiInterface.getCompanyUserdlt();
    Log.e("api==>", call.request().toString());
    call.enqueue(new Callback<ArrayList<GetCompanyUserdlt>>() {
        @Override
        public void onResponse(Call<ArrayList<GetCompanyUserdlt>> call, Response<ArrayList<GetCompanyUserdlt>> response) {
            Toast.makeText(LoginActivity.this, "onsucccess", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Call<ArrayList<GetCompanyUserdlt>> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "onfail", Toast.LENGTH_SHORT).show();
        }
    });

}


   private void secret_key(String key){
        ApiInterface apiInterface=ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<GetBaseUrlResponse>> call=apiInterface.getBaseUrl(key);
       Log.e("secret_key==>","key : "+key);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetBaseUrlResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetBaseUrlResponse>> call, Response<ArrayList<GetBaseUrlResponse>> response) {
                Log.e("Response==>","" + response);
                if(response.body()!=null&& response.code()==200){
                ArrayList<GetBaseUrlResponse> getBaseUrlResponses=response.body();
                if(getBaseUrlResponses!=null&& getBaseUrlResponses.size()>0){
                    for(int i=0;i<getBaseUrlResponses.size();i++){
                        if(getBaseUrlResponses.get(i).getStatus().equals("1")){
                            emailLLID.setVisibility(View.VISIBLE);
                            passwordLLID.setVisibility(View.VISIBLE);
                            registerBtn.setVisibility(View.VISIBLE);
                            String url =  getBaseUrlResponses.get(i).getBase_url();
                            Log.e("secret_key==>","url : "+url);
                            MySharedPreferences.setPreference(LoginActivity.this,AppConstants.SharedPreferenceValues.GLOBAL_MAIN_URL,url);

                            String check = MySharedPreferences.getPreferences(LoginActivity.this, AppConstants.SharedPreferenceValues.GLOBAL_MAIN_URL);

                            Log.e("URL==>","check "+url);

                        /*    SharedPreferences.Editor editor=sharedPreferences.edit();
                            String newbaseurl=getBaseUrlResponses.get(i).getBase_url();
                            editor.putString("GLOBAL_MAIN_URL",newbaseurl);
                            Log.e("GLOBAL_MAIN_URL"," "+newbaseurl);
                            editor.apply();*/
                           // Toast.makeText(LoginActivity.this, ""+getBaseUrlResponses.get(i).getBase_url(), Toast.LENGTH_SHORT).show();
                          //  CheckVerionControl();
                        }else{
                            properkey=1;
                           /* emailLLID.setVisibility(View.GONE);
                            passwordLLID.setVisibility(View.GONE);
                            registerBtn.setVisibility(View.GONE);*/
                        }
                    }
//                    if(properkey==1){
//                        Toast.makeText(LoginActivity.this, "Please Enter a Valid Key", Toast.LENGTH_SHORT).show();
//
//                    }



                }

                else {
                    AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...1!", "Something went wrong at server side");
                }

                } else {
                   AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...!", "Something went wrong at server side");
                }


            }

            @Override
            public void onFailure(Call<ArrayList<GetBaseUrlResponse>> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "on Failure", Toast.LENGTH_SHORT).show();
            }
        });


    }







    private void CheckVerionControl(){
        showProgressDialog();
        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<Versioncontrol> call=apiInterface.versionControl(version);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<Versioncontrol>() {
            @Override
            public void onResponse(Call<Versioncontrol> call, Response<Versioncontrol> response) {
                Log.e("Response==>","" + response);
                dismissProgressDialog();
                if(response.body()!=null&& response.code()==200){
                Versioncontrol versioncontrol=response.body();
            if(versioncontrol.getStatus().equals("1")){
                userInputValidations();
//                vTVID.setText("v"+version);
             //   getLoginResponse();
            }else {

                AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...!", "Update the app to latest version " );
            }

                }
            }
            @Override
            public void onFailure(Call<Versioncontrol> call, Throwable t) {
                dismissProgressDialog();
                AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...!", "Something went wrong at server side");
            }
        });

    }

    private void getLoginResponse() {
       Log.e("loginAPI==>", "calleddddddddddd");
        showProgressDialog();
        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<LoginResponse>> call = apiInterface.getLoginResponse(emailStr, passwordStr,clientId);
        //Log.d("LOGIN_URL", "" + AppConstants.GLOBAL_MAIN_URL + "login?email_id=" + emailStr + "&password=" + passwordStr);
        Log.e("loginAPI==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<LoginResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LoginResponse>> call, Response<ArrayList<LoginResponse>> response) {
                Log.e("loginAPI==>","response " + response);
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<LoginResponse> loginResponses = response.body();
                    if (loginResponses != null && loginResponses.size() > 0) {
                        for (int i = 0; i < loginResponses.size(); i++) {

                            if (loginResponses.get(i).getStatus().equals("1")) {

                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_ID, "" + loginResponses.get(i).getUser_id());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_TYPE, "" + loginResponses.get(i).getUser_type());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_NAME, "" + loginResponses.get(i).getUser_name());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_DESIGNATION, "" + loginResponses.get(i).getDesignation());

                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_MOBILE, "" + loginResponses.get(i).getMobile_number());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_EMAIL_ID, "" + loginResponses.get(i).getEmail_id());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_COMPANY_ID, "" + loginResponses.get(i).getCompany_id());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_EXPIRY_DAYS, "" + loginResponses.get(i).getExpiry_days());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_REG_COUNT, "6");
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_REG_PASSWORD, "" + passwordStr);
                                //startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));

                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_PROFILE_PIC, "" + loginResponses.get(i).getUser_pic());
                                MySharedPreferences.setPreference(LoginActivity.this, AppConstants.SharedPreferenceValues.USER_COMPANY_NAME, "" + loginResponses.get(i).getCompany_name());


                                if (AppConstants.applicationHandler!=null){
                                    AppConstants.applicationHandler.obtainMessage(1).sendToTarget();
                                }

                                Intent newIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                                finish();
                            } else {
                                AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Invalid Details...!", "Please enter valid email and password");
                            }
                        }
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...!", "Something went wrong at server side");
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...!", "Something went wrong at server side");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LoginResponse>> call, Throwable t) {
                dismissProgressDialog();
                AlertUtilities.bottomDisplayStaticAlert(LoginActivity.this, "Alert...!", "Something went wrong at server side");
            }
        });
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(LoginActivity.this)
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

    public class LoginResponse {

        /*user_id: "20",
user_name: "adkshkd",
mobile_number: "7987978789",
email_id: "satheesh@tranquilwebsolutions.com.com",
company_id: "41",
user_type: "1"*/
        private String status;
        private String user_id;
        private String user_name;
        private String mobile_number;
        private String email_id;
        private String company_id;
        private String active_status;
        private String user_type;
        private String expiry_days;
        private String user_pic;
        private String company_name;
        private String designation;


        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getActive_status() {
            return active_status;
        }

        public void setActive_status(String active_status) {
            this.active_status = active_status;
        }

        public String getUser_pic() {
            return user_pic;
        }

        public void setUser_pic(String user_pic) {
            this.user_pic = user_pic;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getExpiry_days() {
            return expiry_days;
        }

        public void setExpiry_days(String expiry_days) {
            this.expiry_days = expiry_days;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }

}
