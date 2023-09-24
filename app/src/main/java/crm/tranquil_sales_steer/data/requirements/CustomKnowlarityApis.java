package crm.tranquil_sales_steer.data.requirements;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.KnowlarityResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomKnowlarityApis {

    public static void clickToKnowlarityCall (Activity activity, String userID, String mobileNumber, KProgressHUD kProgressHUD){

        //Utilities.showProgress(kProgressHUD,activity,"","");
        ApiInterface apiInterface=ApiClient.getClientNew((Activity) activity.getApplicationContext()).create(ApiInterface.class);

        Call<KnowlarityResponse> call = apiInterface.clickToKnowlarityCall(userID,mobileNumber);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<KnowlarityResponse>() {
            @Override
            public void onResponse(Call<KnowlarityResponse> call, Response<KnowlarityResponse> response) {
                //Utilities.dismissProgress(kProgressHUD);
                if (response.body() != null && response.code() == 200){
                    KnowlarityResponse knowlarityResponse = response.body();

                    if (knowlarityResponse.getMessage() != null && knowlarityResponse.getMessage().equalsIgnoreCase("Call successfully placed")){
                        Toast.makeText(activity, knowlarityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity, knowlarityResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {

                    Toast.makeText(activity, "Invalid Request", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<KnowlarityResponse> call, Throwable t) {

                Toast.makeText(activity, "Invalid Request", Toast.LENGTH_SHORT).show();
                Utilities.dismissProgress(kProgressHUD);

            }
        });
    }
}
