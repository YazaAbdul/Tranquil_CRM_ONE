package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class DashBoardChangePasswordActivity extends AppCompatActivity {


    AppCompatEditText olPasswordETID, newPasswordETID,reEnternewPasswordETID;
    AppCompatButton registerBtn;
    String oldPasswordStr, newPasswordStr,reEnterStr, deviceID;
    private KProgressHUD hud;
    String userID,storedPassword;

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Utilities.setStatusBarGradiant(this);

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);

        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Change Password");
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });
        RelativeLayout okRLID = findViewById(R.id.okRLID);
        okRLID.setVisibility(View.VISIBLE);

        okRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(DashBoardChangePasswordActivity.this)) {
                    userInputValidations();
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, getResources().getString(R.string.no_internet_title), getResources().getString(R.string.no_internet_des));
                }
            }
        });

        userID= MySharedPreferences.getPreferences(this,""+ AppConstants.SharedPreferenceValues.USER_ID);
        storedPassword= MySharedPreferences.getPreferences(this,""+ AppConstants.SharedPreferenceValues.USER_REG_PASSWORD);

        deviceID = Settings.Secure.getString(DashBoardChangePasswordActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        olPasswordETID = findViewById(R.id.olPasswordETID);
        newPasswordETID = findViewById(R.id.newPasswordETID);
        reEnternewPasswordETID=findViewById(R.id.reEnternewPasswordETID);


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
        oldPasswordStr = olPasswordETID.getText().toString();
        newPasswordStr = newPasswordETID.getText().toString();
        reEnterStr=reEnternewPasswordETID.getText().toString();

        int count = 0;

        if (TextUtils.isEmpty(oldPasswordStr)) {
            AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Old password can't be empty", "Enter Old password");
            return;
        } else {
            count++;
        }

        if (oldPasswordStr.matches(storedPassword)){
            count++;
        }else{
            AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Old password not matched", "Please Enter valid Old password");
            return;
        }

        if (TextUtils.isEmpty(newPasswordStr)) {
            AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "New Password can't be empty", "Enter New Password");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(reEnterStr)) {
            AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Re-enter password", "Please re enter your new password");
            return;
        } else {
            count++;
        }

        if (newPasswordStr.matches(reEnterStr)){
            count++;
        }else{
            AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Password not matched", "Please Enter correct password");
            return;
        }

        if (count == 5) {
            getChangePasswordResponse();
        } else {
            AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Some fields are missing", "All fields are mandatory");
        }
    }

    private void getChangePasswordResponse() {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.getChangePasswordResponse(userID,oldPasswordStr, newPasswordStr);
        Log.d("LOGIN_URL", "" + AppConstants.GLOBAL_MAIN_URL + "login?email_id=" + oldPasswordStr + "&password=" + newPasswordStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> changeResponse = response.body();
                    if (changeResponse != null && changeResponse.size() > 0) {
                        for (int i = 0; i < changeResponse.size(); i++) {

                            if (changeResponse.get(i).getStatus().equals("1")) {
                                AlertUtilities.SuccessAlertDialog(DashBoardChangePasswordActivity.this,"Password changed successfully","");
                            }else{
                                AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Please enter valid details", "Please enter valid email and password");
                            }
                        }
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Alert...!", "Something went wrong at server side");
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Alert...!", "Something went wrong at server side");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                AlertUtilities.bottomDisplayStaticAlert(DashBoardChangePasswordActivity.this, "Alert...!", "Something went wrong at server side");

            }
        });
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(DashBoardChangePasswordActivity.this)
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
