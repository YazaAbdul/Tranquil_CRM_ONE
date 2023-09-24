package crm.tranquil_sales_steer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.GetCompanyUserdlt;
import crm.tranquil_sales_steer.ui.models.GetlogoResponse;
import crm.tranquil_sales_steer.ui.models.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrmApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AppConstants.applicationHandler= mHandler;
        checkUserLogin();
    }

    private void checkUserLogin() {
        String splashCheck = MySharedPreferences.getPreferences((Activity) getApplicationContext(), AppConstants.SharedPreferenceValues.USER_REG_COUNT);

        if (splashCheck.equals("6")) {

            String userID = MySharedPreferences.getPreferences((Activity) getApplicationContext(), AppConstants.SharedPreferenceValues.SAVED_FCM_ID);
            Log.e("fcm_id==>", userID);
            if (Utilities.isNetworkAvailable(getApplicationContext())){
                getGeneratedFcmID(userID);
                getcompanyuserdlt();
                getCompanylogo();
            }

        }
    }

    private void getGeneratedFcmID(String oldFcmId) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String newToken = task.getResult();
                    Log.e("generated==>", newToken);
                    Log.e("old==>", oldFcmId);
                    if (oldFcmId!=newToken) {
                        Log.e("new_token==>", newToken);
                        sendServerToFcmID(newToken);
                    }
                });
    }

    private void sendServerToFcmID(String fcmID) {

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        String userID = MySharedPreferences.getPreferences((Activity) getApplicationContext(), AppConstants.SharedPreferenceValues.USER_ID);

        Log.e("data==>",android_id+" , "+userID);
        ApiInterface apiInterface=ApiClient.getClientNew((Activity) getApplicationContext()).create(ApiInterface.class);
        Call<MessageResponse> call = apiInterface.updateFCMID(userID, android_id, fcmID, "android");
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.e("response==>", new Gson().toJson(response.body()));
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMsg().equals("1")) {
                        Log.e("fcm_id_update==>", "success");
                        MySharedPreferences.setPreference(getApplicationContext(), AppConstants.SharedPreferenceValues.SAVED_FCM_ID, fcmID);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e("fcm_id_update==>", "error : " + t.getMessage());
            }
        });
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            Log.e("handler==>","called");
            switch (msg.what){
                case 1:
                    Log.e("handler==>","checkinng method");
                    checkUserLogin();
                    break;
            }
        }
    };

    private void getcompanyuserdlt(){
        ApiInterface apiInterface=ApiClient.getClientNew((Activity) getApplicationContext()).create(ApiInterface.class);
        Call<ArrayList<GetCompanyUserdlt>> call=apiInterface.getCompanyUserdlt();
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetCompanyUserdlt>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCompanyUserdlt>> call, Response<ArrayList<GetCompanyUserdlt>> response) {
                if(response.body()!=null&& response.code()==200){
                    MySharedPreferences.setPreference(getApplicationContext(),"PLAN_TYPE",response.body().get(0).getProduct_id());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetCompanyUserdlt>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCompanylogo(){
        ApiInterface apiInterface=ApiClient.getClientNew((Activity) getApplicationContext()).create(ApiInterface.class);
        Call<ArrayList<GetlogoResponse>> call=apiInterface.getlogo();
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetlogoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetlogoResponse>> call, Response<ArrayList<GetlogoResponse>> response) {
                if(response.body()!=null&& response.code()==200){

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetlogoResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }




}
