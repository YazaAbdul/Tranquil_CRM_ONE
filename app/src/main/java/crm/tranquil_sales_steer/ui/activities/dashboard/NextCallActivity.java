package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.NextCallResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextCallActivity extends AppCompatActivity {

    ProgressBar pb;
    TextView customerNameTVID,activitiesTVID,dashBoardTVID;
    Button nextCallBtn, skipBtn;
    ArrayList<NextCallResponse> nextCallResponses = new ArrayList<>();
    String userID, companyID, userType, leadID,nextMobileStr,nextActID,nextActName,nextLeadName,nextLeadID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_call);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                leadID = bundle.getString("LEAD_ID");
                nextActID = bundle.getString("ACTIVITY_ID");
            }
        }

        customerNameTVID = findViewById(R.id.customerNameTVID);
        nextCallBtn = findViewById(R.id.nextCallBtn);
        skipBtn = findViewById(R.id.skipBtn);
        pb = findViewById(R.id.pb);
        activitiesTVID = findViewById(R.id.activitiesTVID);
        dashBoardTVID = findViewById(R.id.dashBoardTVID);


        userID = MySharedPreferences.getPreferences(NextCallActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(NextCallActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        userType = MySharedPreferences.getPreferences(NextCallActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        showCustomerDetails();

        skipBtn.setOnClickListener(v -> {
            showNextCustomerDetails();
        });

        nextCallBtn.setOnClickListener(v -> {
            nextCalling();
        });

        activitiesTVID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        dashBoardTVID.setOnClickListener(v -> {

            Utilities.finishAnimation(this);

        });


    }



    private void showCustomerDetails() {

        pb.setVisibility(View.VISIBLE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<NextCallResponse>> call = apiInterface.nextCall(userID,leadID,nextActID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NextCallResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NextCallResponse>> call, Response<ArrayList<NextCallResponse>> response) {

                pb.setVisibility(View.GONE);

                if (response.body() != null && response.code() == 200){

                    nextCallResponses = response.body();

                    for (int i = 0; i< nextCallResponses.size(); i++){


                        customerNameTVID.setText(nextCallResponses.get(i).getLead_name());

                        nextMobileStr = nextCallResponses.get(i).getMobile_number();
                        nextActID = nextCallResponses.get(i).getActivity_id();
                        nextActName = nextCallResponses.get(i).getActivity_name();
                        nextLeadName = nextCallResponses.get(i).getLead_name();
                        nextLeadID = nextCallResponses.get(i).getLead_id();
                    }
                }else {
                    Toast.makeText(NextCallActivity.this, "Next Customer not loaded", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<NextCallResponse>> call, Throwable t) {
                Toast.makeText(NextCallActivity.this, "Next Customer not loaded", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showNextCustomerDetails() {

        pb.setVisibility(View.VISIBLE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<NextCallResponse>> call = apiInterface.nextCall(userID,nextLeadID,nextActID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NextCallResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NextCallResponse>> call, Response<ArrayList<NextCallResponse>> response) {

                pb.setVisibility(View.GONE);

                if (response.body() != null && response.code() == 200){

                    nextCallResponses = response.body();

                    for (int i = 0; i< nextCallResponses.size(); i++){


                        customerNameTVID.setText(nextCallResponses.get(i).getLead_name());

                        nextMobileStr = nextCallResponses.get(i).getMobile_number();
                        nextActID = nextCallResponses.get(i).getActivity_id();
                        nextActName = nextCallResponses.get(i).getActivity_name();
                        nextLeadName = nextCallResponses.get(i).getLead_name();
                        nextLeadID = nextCallResponses.get(i).getLead_id();
                    }

                }else {
                    Toast.makeText(NextCallActivity.this, "Next Customer not loaded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NextCallResponse>> call, Throwable t) {
                Toast.makeText(NextCallActivity.this, "Next Customer not loaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextCalling() {

        Intent intent = new Intent(NextCallActivity.this,CallCompleteActivity.class);
        intent.putExtra("ACTIVITY_NAME",nextActName);
        intent.putExtra("ACTIVITY_ID",nextActID);
        intent.putExtra("CUSTOMER_NAME",nextLeadName);
        intent.putExtra("LEAD_ID",nextLeadID);
        startActivity(intent);
        numberCalling(nextMobileStr);

    }

    private void numberCalling(String number) {

        try {
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
        }

    }
}