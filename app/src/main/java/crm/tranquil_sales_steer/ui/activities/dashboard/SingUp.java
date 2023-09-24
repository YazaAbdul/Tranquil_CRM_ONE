package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.ui.activities.start_ups.LoginActivity;

public class SingUp extends AppCompatActivity {

    AppCompatTextView singinTVID;
    AppCompatEditText UsernameETID,mobileNumberETID, EmailETID,PasswordETID;
    AppCompatButton registerBtn;
    String usenameStr,mobileNumberStr,emailstr,passwordStr;
    private KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        singinTVID=findViewById(R.id.singinTVID);
        UsernameETID=findViewById(R.id.UsernameETID);
        mobileNumberETID=findViewById(R.id.mobileNumberETID);
        EmailETID=findViewById(R.id.EmailETID);
        PasswordETID=findViewById(R.id.PasswordETID);
        registerBtn=findViewById(R.id.registerBtn);
        
        
        registerBtn.setOnClickListener(v -> {
            Validation();
        });
        
        
        
        
        
        singinTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SingUp.this, LoginActivity.class);
                intent.putExtra("SINGUP", "101");
                startActivity(intent);
            }
        });
    }

    private void Validation() {

            usenameStr= UsernameETID.getText().toString();
              mobileNumberStr=mobileNumberETID.getText().toString();
              emailstr=   EmailETID.getText().toString();
              passwordStr=PasswordETID.getText().toString();

        int count = 0;


        if (TextUtils.isEmpty(usenameStr)) {
            AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "Usernamer can't be empty", "Enter  Username");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(mobileNumberStr)) {
            AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "mobile number can't be empty", "Enter your mobile number");
            return;
        } else {
            count++;
        }
        if (TextUtils.isEmpty(emailstr)) {
            AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "Email-id can't be empty", "Enter  Email-id");
            //AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "Company ID can't be empty", "Enter Company ID");
            return;
        } else {
            count++;
        }
        if (TextUtils.isEmpty(passwordStr)) {
            AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "Password can't be empty", "Enter your password");

            return;
        } else {
            count++;
        }

        if (count == 4) {
          //  getSingupResponse();
        } else {
            AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "Some fields are missing", "All fields are mandatory");
        }

    }

   /* private void getSingupResponse() {
        Log.e("loginAPI==>", "calleddddddddddd");
        showProgressDialog();
        ApiInterface apiInterface = ApiClient.getClientdemo().create(ApiInterface.class);
        Call<CreatreUserResponse> call=apiInterface.getSingupResponse(usenameStr,mobileNumberStr,emailstr,passwordStr);
    Log.e("api==>", call.request().toString());
    call.enqueue(new Callback<CreatreUserResponse>() {
        @Override
        public void onResponse(Call<CreatreUserResponse> call, Response<CreatreUserResponse> response) {
            if(response.body()!=null&& response.code()==200){
                CreatreUserResponse creatreUserResponse=response.body();
                if(creatreUserResponse.getStatus().equals("1")){



                    Intent newIntent = new Intent(SingUp.this, LoginActivity.class);
                    newIntent.putExtra("SINGUP", "101");
                    startActivity(newIntent);
                    finish();

                }



            }else{
                Toast.makeText(SingUp.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        public void onFailure(Call<CreatreUserResponse> call, Throwable t) {
            dismissProgressDialog();
            AlertUtilities.bottomDisplayStaticAlert(SingUp.this, "Alert...!", "Something went wrong at server side");

        }
    });







    }*/
    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(SingUp.this)
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