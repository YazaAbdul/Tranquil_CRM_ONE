package crm.tranquil_sales_steer.ui.activities.start_ups;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.DashBoardActivity;
import crm.tranquil_sales_steer.ui.models.GetlogoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainSplashActivity extends AppCompatActivity {

    private String splashCheck;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    String emailStr, passwordStr;
    KProgressHUD hud;
    ImageView imageID;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AlertUtilities.startAnimation(this);
        Utilities.setStatusBarGradiant(this);
        imageID=findViewById(R.id.imageID);

     //   getCompanylogo();



        splashCheck = MySharedPreferences.getPreferences(this, AppConstants.SharedPreferenceValues.USER_REG_COUNT);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 10 seconds
                    sleep(1 * 1000);

                    if (splashCheck.equals("6")){
                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                    }else{
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }

                } catch (Exception ignored) {

                }
            }
        };
        background.start();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                permissioncheck();

               // checkcamerpermission();


                MainSplashActivity.this.finish();
            }
        }, 1000);




    }

    public void checkcamerapermissionAndOpenCamera(){
        if(ActivityCompat.checkSelfPermission(MainSplashActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainSplashActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);

        }else {
         /*   Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 0);*/
        }


    }


    public void checkcamerpermission(){
        if (ContextCompat.checkSelfPermission(MainSplashActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

        }else{
            ActivityCompat.requestPermissions(MainSplashActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
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

    private void permissioncheck() {


        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.READ_CALL_LOG))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ");

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            // Permission is granted, query the Contacts Provider
            Cursor cursor = getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            // Iterate over the cursor to retrieve the contacts
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));

                    // Do something with the contact's name

                    Log.e("contacts==>","" + name + ", " + phoneNumber);
                }
                cursor.close();
            }
        }*/

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE");

        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("WRITE");


        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("READ");


        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("READ");

        /*if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("READ");*/

        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.CAPTURE_AUDIO_OUTPUT))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.CAPTURE_AUDIO_OUTPUT))
            permissionsNeeded.add("WRITE");

        if (!addPermission(permissionsList, Manifest.permission.PROCESS_OUTGOING_CALLS))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.READ_CALL_LOG))
            permissionsNeeded.add("READ");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    // Marshmallow+
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                } else {
                                    // Pre-Marshmallow
                                }

                            }
                        });
                return;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                // Pre-Marshmallow
            }

            return;
        } else {
            // Toast.makeText(this,"Permission",Toast.LENGTH_LONG).show();
            LaunchApp();
        }

        //insertDummyContact();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        Boolean cond;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    //  return false;

                    cond = false;
            }
            //  return true;
            cond = true;

        } else {
            // Pre-Marshmallow
            cond = true;
        }

        return cond;

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainSplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void LaunchApp() {

        new Handler().postDelayed(
                () -> {
                    String splash=MySharedPreferences.getPreferences(this,"REG_COUNT");
                    if (splash.equals("1")){
                        getLoginResponse(emailStr, passwordStr);
//                        startActivity(new Intent(MainSplashActivity.this, NewDashBoardActivity.class));
//                        finish();
                    }else{
                        startActivity(new Intent(MainSplashActivity.this, LoginActivity.class));
                        finish();
                    }

                },
                100
        );
    }



    private void getCompanylogo(){
        ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<GetlogoResponse>> call=apiInterface.getlogo();
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetlogoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetlogoResponse>> call, Response<ArrayList<GetlogoResponse>> response) {
                if(response.body()!=null&& response.code()==200){
                    MySharedPreferences.setPreference(getApplicationContext(),"GET_LOGO",response.body().get(0).getLogo());
                   // Toast.makeText(getApplicationContext(), "onresponse logo"+response.body().get(0).getLogo(), Toast.LENGTH_LONG).show();
                    String imageurl=response.body().get(0).getLogo();
                    Picasso.with(MainSplashActivity.this).load(imageurl).into(imageID);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetlogoResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void getLoginResponse(final String emailStr, String passwordStr) {
        showProgressDialog();
        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<LoginActivity.LoginResponse>> call = apiInterface.getLoginResponse(emailStr, passwordStr,"4");
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<LoginActivity.LoginResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LoginActivity.LoginResponse>> call, Response<ArrayList<LoginActivity.LoginResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<LoginActivity.LoginResponse> signUpResponses = response.body();
                    if (signUpResponses.size() > 0) {
                        for (int i = 0; i < signUpResponses.size(); i++) {
                            if (signUpResponses.get(i).getStatus() == "1") {
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_ID", "" + signUpResponses.get(i).getUser_id());
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_TYPE", "" + signUpResponses.get(i).getUser_type());
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_NAME", "" + signUpResponses.get(i).getUser_name());
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_MOBILE", "" + signUpResponses.get(i).getMobile_number());
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_EMAIL", "" + emailStr);
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_PASSWORD", "" + passwordStr);
                                MySharedPreferences.setPreference(MainSplashActivity.this, "REG_COUNT", "1");
                                MySharedPreferences.setPreference(MainSplashActivity.this, "USER_PIC", "" + signUpResponses.get(i).getUser_pic());
                                //getLoginResponse(nameStr,mobileNumberStr);
                                startActivity(new Intent(MainSplashActivity.this, DashBoardActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(MainSplashActivity.this,LoginActivity.class));
                                Utilities.showToast(MainSplashActivity.this,"session expired");
                                // Utilities.AlertDaiolog(MainSplashActivity.this, getString(R.string.opps), "Invalid email or password", 1, null, "OK");
                            }
                        }
                    } else {
                        startActivity(new Intent(MainSplashActivity.this,LoginActivity.class));
                        Utilities.AlertDaiolog(MainSplashActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                    }
                } else {
                    startActivity(new Intent(MainSplashActivity.this,LoginActivity.class));
                    Utilities.AlertDaiolog(MainSplashActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LoginActivity.LoginResponse>> call, Throwable t) {
                dismissProgressDialog();
                Log.d("DEBUG_ERROR-->", "" + t);
                if (Utilities.isNetworkAvailable(MainSplashActivity.this)) {
                    startActivity(new Intent(MainSplashActivity.this,LoginActivity.class));
                    Utilities.AlertDaiolog(MainSplashActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                } else {
                    Utilities.AlertDaiolog(MainSplashActivity.this, getString(R.string.no_internet_tittle), "Poor internet connection", 1, null, "OK");
                }
            }
        });
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(MainSplashActivity.this)
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
    protected void onPause() {
        super.onPause();
        finish();
    }

}
