package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.EmployeeCallDetailsAdapter;
import crm.tranquil_sales_steer.ui.adapters.ReportingUsersAdapter;
import crm.tranquil_sales_steer.ui.models.CallHistoryResponse;
import crm.tranquil_sales_steer.ui.models.DashboardProfileResponse;
import crm.tranquil_sales_steer.ui.models.DashboardResponse;
import crm.tranquil_sales_steer.ui.models.ReportingUsersResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardProfileActivity extends AppCompatActivity implements ReportingUsersAdapter.ReportingUsersClickListener {

    RelativeLayout backRLID;
    TextView headerTittleTVID;

    RecyclerView reportingUsersRVID,employeeCallDataRCID;
    ProgressBar reportingUsersPB,pb,mainPB;
    RelativeLayout employeeCallDataRLID,profileRLID;
    String pic,user_designation;
    CircleImageView dashboard_userpic;

    AppCompatTextView userNameTVID,designationTVID,

                    /*Dash Board Details Text Views*/

                      dayDialedCallsTVID,monthDialedCallsTVID,overallDialedCallsTVID,
                      daySpokenCallsTVID,monthSpokenCallsTVID,overallSpokenCallsTVID,
                      dayCustomerMeetTVID,monthCustomerMeetTVID,overallCustomerMeetTVID,
                      dayNewEnquiriesTVID,monthNewEnquiriesTVID,overallNewEnquiriesTVID,
                      dayDroppedTVID,monthDroppedTVID,overallDroppedTVID,
                      dayCustomerMeetScheduledTVID,monthCustomerMeetScheduledTVID,overallCustomerMeetScheduledTVID,
                      dayCustomerMeetCompletedTVID,monthCustomerMeetCompletedTVID,overallCustomerMeetCompletedTVID,
                      daySaleScheduledTVID,monthSaleScheduledTVID,overallSaleScheduledTVID,
                      daySaleCompletedTVID,monthSaleCompletedTVID,overallSaleCompletedTVID,
                      dayTalkTimeTVID,monthTalkTimeTVID,overallTalkTimeTVID;

   ReportingUsersAdapter reportingUsersAdapter;
   DashboardProfileResponse dashboardProfileResponses;
   DashboardResponse dashboard;
   EmployeeCallDetailsAdapter callDetailsAdapter;
   ArrayList<ReportingUsersResponse> reporting_users = new ArrayList<>();
   ArrayList<CallHistoryResponse> call_history = new ArrayList<>();
   String userID,userName,userDesignation,imageUrl;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_profile);

        Utilities.setStatusBarGradiant(this);

        Intent intent=getIntent();

        imageUrl=intent.getStringExtra("IMAGEURL");

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                userID = bundle.getString("USER_ID");
                userName = bundle.getString("USER_NAME");
                userDesignation=bundle.getString("DESIGNATION");
              //  imageUrl=bundle.getString("IMAGEURL");
              //  Log.e("usernameintent", ""+imageUrl);
            }
        }
        Log.e("usernameintent2", ""+imageUrl);
                //userID = MySharedPreferences.getPreferences(DashBoardProfileActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);


        user_designation = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_DESIGNATION);

        backRLID = findViewById(R.id.backRLID);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);

        headerTittleTVID.setText(userName + " Performance");

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        userNameTVID = findViewById(R.id.userNameTVID);
        designationTVID = findViewById(R.id.designationTVID);

        dayDialedCallsTVID = findViewById(R.id.dayDialedCallsTVID);
        monthDialedCallsTVID = findViewById(R.id.monthDialedCallsTVID);
        overallDialedCallsTVID = findViewById(R.id.overallDialedCallsTVID);

        daySpokenCallsTVID = findViewById(R.id.daySpokenCallsTVID);
        monthSpokenCallsTVID = findViewById(R.id.monthSpokenCallsTVID);
        overallSpokenCallsTVID = findViewById(R.id.overallSpokenCallsTVID);

        dayCustomerMeetTVID = findViewById(R.id.dayCustomerMeetTVID);
        monthCustomerMeetTVID = findViewById(R.id.monthCustomerMeetTVID);
        overallCustomerMeetTVID = findViewById(R.id.overallCustomerMeetTVID);

        dayNewEnquiriesTVID = findViewById(R.id.dayNewEnquiriesTVID);
        monthNewEnquiriesTVID = findViewById(R.id.monthNewEnquiriesTVID);
        overallNewEnquiriesTVID = findViewById(R.id.overallNewEnquiriesTVID);

        dayDroppedTVID = findViewById(R.id.dayDroppedTVID);
        monthDroppedTVID = findViewById(R.id.monthDroppedTVID);
        overallDroppedTVID = findViewById(R.id.overallDroppedTVID);

        dayCustomerMeetScheduledTVID = findViewById(R.id.dayCustomerMeetScheduledTVID);
        monthCustomerMeetScheduledTVID = findViewById(R.id.monthCustomerMeetScheduledTVID);
        overallCustomerMeetScheduledTVID = findViewById(R.id.overallCustomerMeetScheduledTVID);

        dayCustomerMeetCompletedTVID = findViewById(R.id.dayCustomerMeetCompletedTVID);
        monthCustomerMeetCompletedTVID = findViewById(R.id.monthCustomerMeetCompletedTVID);
        overallCustomerMeetCompletedTVID = findViewById(R.id.overallCustomerMeetCompletedTVID);

        daySaleScheduledTVID = findViewById(R.id.daySaleScheduledTVID);
        monthSaleScheduledTVID = findViewById(R.id.monthSaleScheduledTVID);
        overallSaleScheduledTVID = findViewById(R.id.overallSaleScheduledTVID);

        daySaleCompletedTVID = findViewById(R.id.daySaleCompletedTVID);
        monthSaleCompletedTVID = findViewById(R.id.monthSaleCompletedTVID);
        overallSaleCompletedTVID = findViewById(R.id.overallSaleCompletedTVID);

        dayTalkTimeTVID = findViewById(R.id.dayTalkTimeTVID);
        monthTalkTimeTVID = findViewById(R.id.monthTalkTimeTVID);
        overallTalkTimeTVID = findViewById(R.id.overallTalkTimeTVID);


        reportingUsersRVID = findViewById(R.id.reportingUsersRVID);
        employeeCallDataRCID = findViewById(R.id.employeeCallDataRCID);
        reportingUsersPB = findViewById(R.id.reportingUsersPB);
        pb = findViewById(R.id.pb);
        mainPB = findViewById(R.id.mainPB);
        employeeCallDataRLID = findViewById(R.id.employeeCallDataRLID);
        profileRLID = findViewById(R.id.profileRLID);
        dashboard_userpic=findViewById(R.id.dashboard_userpic);
        Picasso.with(this).load(imageUrl).error(R.drawable.profpic).rotate(0).into(dashboard_userpic);

        if (Utilities.isNetworkAvailable(this)){
            loadProfile();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfile() {

        mainPB.setVisibility(View.VISIBLE);
        profileRLID.setVisibility(View.GONE);
        reportingUsersRVID.setVisibility(View.GONE);
        employeeCallDataRCID.setVisibility(View.GONE);
        employeeCallDataRLID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<DashboardProfileResponse> call = apiInterface.getProfile(userID);
        Log.e("api23==>",call.request().toString());
        call.enqueue(new Callback<DashboardProfileResponse>() {
            @Override
            public void onResponse(Call<DashboardProfileResponse> call, Response<DashboardProfileResponse> response) {

                mainPB.setVisibility(View.GONE);
                profileRLID.setVisibility(View.VISIBLE);
                reportingUsersRVID.setVisibility(View.VISIBLE);
                employeeCallDataRCID.setVisibility(View.VISIBLE);
                employeeCallDataRLID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    dashboardProfileResponses = response.body();

                    dashboard = dashboardProfileResponses.getDashboard();
                    reporting_users = dashboardProfileResponses.getReporting_users();
                    call_history = dashboardProfileResponses.getCall_history();

                    userNameTVID.setText(dashboard.getUser_name());

                  /*  if(user_designation.isEmpty()){
                        designationTVID.setText(user_designation);
                    }
                  else{

                    }*/


                    Log.e("designationTVID", ""+userDesignation);
                    if(!userDesignation.equalsIgnoreCase("null")){
                        designationTVID.setText(userDesignation);
                    }
                    else{
                        designationTVID.setText("User Designation");
                    }




                    dayDialedCallsTVID.setText(dashboard.getDialed_count());
                    monthDialedCallsTVID.setText(dashboard.getDialed_count_monthly());
                    overallDialedCallsTVID.setText(dashboard.getDialed_count_overall());

                    daySpokenCallsTVID.setText(dashboard.getSpoken_count());
                    monthSpokenCallsTVID.setText(dashboard.getSpoken_count_mnth());
                    overallSpokenCallsTVID.setText(dashboard.getSpoken_count_overall());

                    dayTalkTimeTVID.setText(dashboard.getTalktime_count());
                    monthTalkTimeTVID.setText(dashboard.getTalktime_count_mnth());
                    overallTalkTimeTVID.setText(dashboard.getTalktime_count_overall());

                    dayCustomerMeetTVID.setText(dashboard.getMeets_count());
                    monthCustomerMeetTVID.setText(dashboard.getMeets_count_mnth());
                    overallCustomerMeetTVID.setText(dashboard.getMeets_count_overall());

                    dayNewEnquiriesTVID.setText(dashboard.getEnq_count());
                    monthNewEnquiriesTVID.setText(dashboard.getEnq_count_mnth());
                    overallNewEnquiriesTVID.setText(dashboard.getEnq_count_overall());

                    dayDroppedTVID.setText(dashboard.getDropped_count());
                    monthDroppedTVID.setText(dashboard.getDropped_count_mnth());
                    overallDroppedTVID.setText(dashboard.getDropped_count_overall());

                    dayCustomerMeetScheduledTVID.setText(dashboard.getMeet_sch_count());
                    monthCustomerMeetScheduledTVID.setText(dashboard.getMeet_sch_count_mnth());
                    overallCustomerMeetScheduledTVID.setText(dashboard.getMeet_sch_count_overall());

                    dayCustomerMeetCompletedTVID.setText(dashboard.getMeet_complt_count());
                    monthCustomerMeetCompletedTVID.setText(dashboard.getMeet_complt_count_mnth());
                    overallCustomerMeetCompletedTVID.setText(dashboard.getMeet_complt_count_overall());

                    daySaleScheduledTVID.setText(dashboard.getSale_sch_count());
                    monthSaleScheduledTVID.setText(dashboard.getSale_sch_count_mnth());
                    overallSaleScheduledTVID.setText(dashboard.getSale_sch_count_overall());

                    daySaleCompletedTVID.setText(dashboard.getSale_complt_count());
                    monthSaleCompletedTVID.setText(dashboard.getSale_complt_count_mnth());
                    overallSaleCompletedTVID.setText(dashboard.getSale_complt_count_overall());

                    if (reporting_users.size() > 0){

                        LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardProfileActivity.this,LinearLayoutManager.VERTICAL,false);
                        reportingUsersRVID.setLayoutManager(layoutManager);

                        reportingUsersAdapter = new ReportingUsersAdapter(reporting_users,DashBoardProfileActivity.this,DashBoardProfileActivity.this,dashboard);
                        reportingUsersRVID.setAdapter(reportingUsersAdapter);
                    }else {

                    }

                    if (call_history.size() > 0){

                        LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardProfileActivity.this,RecyclerView.VERTICAL,false);
                        employeeCallDataRCID.setLayoutManager(layoutManager);

                        callDetailsAdapter = new EmployeeCallDetailsAdapter(call_history);
                        employeeCallDataRCID.setAdapter(callDetailsAdapter);
                    }else {
                        employeeCallDataRLID.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<DashboardProfileResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onReportingUsersItemClick(ReportingUsersResponse response, View v, int pos, ReportingUsersAdapter.ReportingUsersVH holder) {

        switch (v.getId()) {

            case R.id.viewRLID:

                Intent intent = new Intent(this, ReportingUserDashBoardActivity.class);
                intent.putExtra("USER_ID",response.getUser_id());
                intent.putExtra("USER_NAME",response.getUser_name());
                intent.putExtra("USER_TYPE",response.getUser_type());
                intent.putExtra("USER_DESIGNATION",response.getUser_role());
                intent.putExtra("IMAGEURL",response.getUser_profile_pic());

                Log.e("getprofilepic", ""+response.getUser_profile_pic());


          /*      Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), Integer.parseInt(response.getUser_profile_pic()));
              ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();*/


                startActivity(intent);

                break;
        }


    }
}