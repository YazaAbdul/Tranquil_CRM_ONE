package crm.tranquil_sales_steer.ui.activities.start_ups;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.GetlogoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashScreen extends AppCompatActivity {


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ImageView imageID;
    String tranquilimgurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        imageID=findViewById(R.id.imageID);

        String  crmappiconurl = MySharedPreferences.getPreferences(SplashScreen.this, AppConstants.SharedPreferenceValues.LOGO_URL);
        Log.e("crmappiconurl",crmappiconurl);
        tranquilimgurl="https://tranquilcrmone.com/attachments/web_images/e787f174dc9c6f0cc95ce2cb9a89e20a.png";


     //   getCompanylogo();

      /*  if(crmappiconurl.equals("")){

        }else{
            Glide.with(SplashScreen.this).load(crmappiconurl).into(imageID);
            Picasso.with(SplashScreen.this).load(crmappiconurl).into(imageID);
        }*/




        
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private void getCompanylogo()   {
        ApiInterface apiInterface= ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<GetlogoResponse>> call=apiInterface.getlogo();
        Log.e("api=hit=>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetlogoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetlogoResponse>> call, Response<ArrayList<GetlogoResponse>> response) {
                if(response.body()!=null&& response.code()==200){

                    //   Toast.makeText(getApplicationContext(), "onresponse logo"+response.body().get(0).getLogo(), Toast.LENGTH_LONG).show();
                    String imageurl=response.body().get(0).getLogo();
                    MySharedPreferences.setPreference(SplashScreen.this,AppConstants.SharedPreferenceValues.LOGO_URL,imageurl);
                    Log.e("imageurl", imageurl);

                    Picasso.with(SplashScreen.this).load(imageurl).into(imageID);


                /*    String  crmappiconurl = MySharedPreferences.getPreferences(DashBoardActivity.this, AppConstants.SharedPreferenceValues.LOGO_URL);
                    Log.e("crmappiconurl",crmappiconurl);*/

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetlogoResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onfail", Toast.LENGTH_SHORT).show();
                Picasso.with(SplashScreen.this).load(tranquilimgurl).into(imageID);
                Log.e("tranquilimgurl", ""+tranquilimgurl);
            }
        });

    }
}
