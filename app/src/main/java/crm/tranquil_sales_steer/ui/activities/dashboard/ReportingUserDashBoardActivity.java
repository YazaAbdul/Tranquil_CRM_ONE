package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.attendance.AttendanceMainActivity;
import crm.tranquil_sales_steer.ui.activities.folders.FoldersActivity;
import crm.tranquil_sales_steer.ui.activities.site_visits.SiteVisitSearchActivity;
import crm.tranquil_sales_steer.ui.adapters.PlansAdapter;
import crm.tranquil_sales_steer.ui.adapters.ShortcutsAdapter;
import crm.tranquil_sales_steer.ui.models.DashBoardResponseNew;
import crm.tranquil_sales_steer.ui.models.PlansResponse;
import crm.tranquil_sales_steer.ui.models.ShortcutsResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportingUserDashBoardActivity extends AppCompatActivity implements View.OnClickListener, PlansAdapter.PlansClickListener, ShortcutsAdapter.ShortcutsClickListener {

    RecyclerView availableLVID;
    ArrayList<DashBoardResponseNew> dashBoardResponseNew = new ArrayList<>();
    crm.tranquil_sales_steer.ui.adapters.DashBoardAdapter dashBoardAdapter;
    CardView overallCVID, todayCVID, pendingCVID, featureCVID,tomorrowCVID,sourceCVID;
    LinearLayout activitiesLLID;
    AppCompatTextView noData,userNameTVID,userDesignationTVID;
    ProgressBar progress;
    String userID,userType,companyID,userName,userDesignation,profieimg;
    CircleImageView userPic;
    RelativeLayout backRLID,searchRLID;
    RecyclerView shortcutsRVID,plansRVID,postSalesRVID;
    ArrayList<PlansResponse> plansResponses = new ArrayList<>();
    ArrayList<ShortcutsResponse> shortcutsResponses = new ArrayList<>();
    PlansAdapter plansAdapter;
    ShortcutsAdapter shortcutsAdapter;
    LinearLayout topicdetailstxtCVID,createivesCVID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_user_dash_board);

        //Utilities.setStatusBarGradiant(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                userID = bundle.getString("USER_ID");
                userType = bundle.getString("USER_TYPE");
                userName = bundle.getString("USER_NAME");
                userDesignation = bundle.getString("USER_DESIGNATION");


          /*      byte[] byteArray = bundle.getByteArray("IMAGELOG");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
*/

                profieimg = bundle.getString("IMAGEURL");
                Log.e("IMAGELOG2", ""+profieimg);
           //     userPic.setImageURI(Uri.parse(profieimg));
            }
        }

        companyID = MySharedPreferences.getPreferences(ReportingUserDashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        overallCVID = findViewById(R.id.overallCVID);
        todayCVID = findViewById(R.id.todayCVID);
        pendingCVID = findViewById(R.id.pendingCVID);
        featureCVID = findViewById(R.id.featureCVID);
        tomorrowCVID = findViewById(R.id.tomorrowCVID);
        sourceCVID = findViewById(R.id.sourceCVID);
        availableLVID = findViewById(R.id.availableLVID);
        activitiesLLID = findViewById(R.id.activitiesLLID);
        searchRLID = findViewById(R.id.searchRLID);
        progress = findViewById(R.id.progress);
        noData = findViewById(R.id.noData);
        userPic = findViewById(R.id.userPic);
        userNameTVID = findViewById(R.id.userNameTVID);
        userDesignationTVID = findViewById(R.id.userDesignationTVID);
        backRLID = findViewById(R.id.backRLID);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        availableLVID.setLayoutManager(layoutManager);
        availableLVID.setNestedScrollingEnabled(true);
        plansRVID = findViewById(R.id.plansRVID);
        shortcutsRVID = findViewById(R.id.shortcutsRVID);
        userNameTVID.setText(userName);
        userDesignationTVID.setText(userDesignation);
        topicdetailstxtCVID = findViewById(R.id.topicdetailstxtCVID);
        createivesCVID = findViewById(R.id.createivesCVID);
        loadPlans();
        loadShortcuts();
        overallCVID.setOnClickListener(this);
        todayCVID.setOnClickListener(this);
        pendingCVID.setOnClickListener(this);
        featureCVID.setOnClickListener(this);
        tomorrowCVID.setOnClickListener(this);
        sourceCVID.setOnClickListener(this);
        userPic.setOnClickListener(this);
        Picasso.with(this).load(profieimg).error(R.drawable.profpic).rotate(0).into(userPic);
        searchRLID.setOnClickListener(this);
        topicdetailstxtCVID.setOnClickListener(this);
        createivesCVID.setOnClickListener(this);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadReportingUsersDashBoard();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadShortcuts() {

        String planType = MySharedPreferences.getPreferences(this,"PLAN_TYPE");
        Log.e("Plantype", ""+planType);


        shortcutsResponses.add(new ShortcutsResponse("1", "Tele \n Callers", "#1878e7", "#3f7fc0", "#6ca9f0", "#7fb4f2", R.drawable.humanrights));
        //  shortcutsResponses.add(new ShortcutsResponse("2","Change Password","#bb24db","#aa45ba","#d475e8","#dc8dec",R.drawable.password_white));phone_call_white
        //  shortcutsResponses.add(new ShortcutsResponse("8","call \n Recordings","#f51e0a","#ca4435","#f97b70","#fa897f",R.drawable.recordings_white));



        if (planType.equals("1")) {
            //    Toast.makeText(this, "plantype"+planType, Toast.LENGTH_SHORT).show();
            shortcutsResponses.add(new ShortcutsResponse("3", "Attendance Punch In/Out", "#10efc6", "#43bcb6", "#84f7e2", "#90f8e5", R.drawable.attendancebold));

            shortcutsResponses.add(new ShortcutsResponse("7", "File \n Manger", "#ebb014", "#d0b62f", "#f2cc68", "#f4d37c", R.drawable.file));

            shortcutsResponses.add(new ShortcutsResponse("9","Creatives Marketing","#082ff7","#3059cf","#546ff9","#6b83fa",R.drawable.creativity32));
            shortcutsResponses.add(new ShortcutsResponse("4", "Direct Meeting", "#8813ec", "#7e42bd", "#c184f5", "#c995f6", R.drawable.direct_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("5", "Customer Meet", "#ff0088", "#bc4395", "#ff6fbc", "#ff80c4", R.drawable.customer_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("6", "Create Day Report", "#3607f8", "#5939c6", "#704efa", "#8366fb", R.drawable.day_report_white));

        } else if(planType.equals("2")){
            shortcutsResponses.add(new ShortcutsResponse("3", "Attendance Punch In/Out", "#10efc6", "#43bcb6", "#84f7e2", "#90f8e5", R.drawable.attendancebold));

            shortcutsResponses.add(new ShortcutsResponse("7", "File \n Manger", "#ebb014", "#d0b62f", "#f2cc68", "#f4d37c", R.drawable.file));

            shortcutsResponses.add(new ShortcutsResponse("9","Creatives Marketing","#082ff7","#3059cf","#546ff9","#6b83fa",R.drawable.creativity32));
            shortcutsResponses.add(new ShortcutsResponse("4", "Direct Meeting", "#8813ec", "#7e42bd", "#c184f5", "#c995f6", R.drawable.direct_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("5", "Customer Meet", "#ff0088", "#bc4395", "#ff6fbc", "#ff80c4", R.drawable.customer_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("6", "Create Day Report", "#3607f8", "#5939c6", "#704efa", "#8366fb", R.drawable.day_report_white));

        }




        //shortcutsResponses.add(new ShortcutsResponse("10","Tele Callers",R.drawable._phone));

        /*LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this,RecyclerView.VERTICAL,false);
        shortcutsRVID.setLayoutManager(layoutManager);*/

        shortcutsAdapter = new ShortcutsAdapter(shortcutsResponses,ReportingUserDashBoardActivity.this,ReportingUserDashBoardActivity.this);
        shortcutsRVID.setAdapter(shortcutsAdapter);



    }

    private void loadPlans() {
        String planType = MySharedPreferences.getPreferences(this,"PLAN_TYPE");
        Log.e("Plantype", ""+planType);


        plansResponses.add(new PlansResponse("1","Overall \n Activity ","#38068A","#2C066A","#5a0ade","#610aef",R.drawable.humanrights));
        plansResponses.add(new PlansResponse("2","Today\nActivity","#E22C13","#CD230C","#ef5b46","#f2715f",R.drawable.date512));
        plansResponses.add(new PlansResponse("5","Tomorrow\nActivity","#ff0088","#bc4395","#ff6fbc","#ff80c4",R.drawable.tomorrow_white));
        plansResponses.add(new PlansResponse("3","Pending\nActivity","#A3034C","#B6115D","#8EA3034C","#8EA3034C",R.drawable.pending_white));
        plansResponses.add(new PlansResponse("4","Future\nActivity","#8813ec","#7e42bd","#c184f5","#c995f6",R.drawable.diagram));
        // plansResponses.add(new PlansResponse("5","Tomorrow\nActivity","#ff0088","#bc4395","#ff6fbc","#ff80c4",R.drawable.tomorrow_white));


        if (planType.equals("1")) {
            //   Toast.makeText(this, "plantype"+planType, Toast.LENGTH_SHORT).show();
            plansResponses.add(new PlansResponse("6","Source Wise\nActivity","#3607f8","#5939c6","#704efa","#8366fb",R.drawable.source_white));
            plansResponses.add(new PlansResponse("7","Project Wise\nActivity","#38068A","#2C066A","#5a0ade","#610aef",R.drawable.source_white));

        }


        plansAdapter = new PlansAdapter(plansResponses,ReportingUserDashBoardActivity.this, ReportingUserDashBoardActivity.this);
        plansRVID.setAdapter(plansAdapter);

    }

    private void loadReportingUsersDashBoard() {

        progress.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        availableLVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DashBoardResponseNew>> call = apiInterface.getOverallActivities(companyID, "1", userType, userID);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<DashBoardResponseNew>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<DashBoardResponseNew>> call, Response<ArrayList<DashBoardResponseNew>> response) {
                progress.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                availableLVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {

                    dashBoardResponseNew = response.body();

                    if (dashBoardResponseNew.size() > 0) {

                        dashBoardAdapter = new crm.tranquil_sales_steer.ui.adapters.DashBoardAdapter(ReportingUserDashBoardActivity.this, dashBoardResponseNew,userID,userType);
                        availableLVID.setAdapter(dashBoardAdapter);

                    } else {

                        progress.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        availableLVID.setVisibility(View.GONE);

                    }

                } else {
                    progress.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    availableLVID.setVisibility(View.GONE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<DashBoardResponseNew>> call, Throwable t) {

                progress.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                availableLVID.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.overallCVID:
                Intent intent = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent.putExtra("PLAN_ID", "1");
                intent.putExtra("TITLE", "Overall");
                intent.putExtra("USER_ID", userID);
                intent.putExtra("USER_TYPE", userType);
                intent.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent);
                break;
            case R.id.todayCVID:
                Intent intent2 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent2.putExtra("PLAN_ID", "2");
                intent2.putExtra("TITLE", "Today");
                intent2.putExtra("USER_ID", userID);
                intent2.putExtra("USER_TYPE", userType);
                intent2.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent2);
                break;
            case R.id.pendingCVID:
                Intent intent3 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent3.putExtra("PLAN_ID", "3");
                intent3.putExtra("TITLE", "Pending");
                intent3.putExtra("USER_ID", userID);
                intent3.putExtra("USER_TYPE", userType);
                intent3.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent3);
                break;
            case R.id.featureCVID:
                Intent intent4 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent4.putExtra("PLAN_ID", "4");
                intent4.putExtra("TITLE", "Future");
                intent4.putExtra("USER_ID", userID);
                intent4.putExtra("USER_TYPE", userType);
                intent4.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent4);
                break;

            case R.id.userPic:
                Intent intent5 = new Intent(ReportingUserDashBoardActivity.this, DashBoardProfileActivity.class);
                intent5.putExtra("USER_ID",userID);
                intent5.putExtra("USER_NAME",userName);
                intent5.putExtra("DESIGNATION", userDesignation);
                intent5.putExtra("IMAGEURL", profieimg);

                startActivity(intent5);
                break;
            case R.id.searchRLID:
                Intent intent6 = new Intent(ReportingUserDashBoardActivity.this, SearchViewActivity.class);
                intent6.putExtra("TYPE", "SEARCH");
                startActivity(intent6);

                break;

            case R.id.sourceCVID:
                Intent intent7 = new Intent(ReportingUserDashBoardActivity.this, SourceTypeActivity.class);
                intent7.putExtra("USER_ID", userID);
                intent7.putExtra("USER_TYPE", userType);
                intent7.putExtra("ACTIVITY", "SOURCE");
                startActivity(intent7);
                break;
            case R.id.tomorrowCVID:
                //startActivity(new Intent(DashBoardActivity.this, SiteVisitSearchActivity.class));
                Intent intent8 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent8.putExtra("PLAN_ID", "5");
                intent8.putExtra("TITLE", "Tomorrow");
                intent8.putExtra("USER_ID", userID);
                intent8.putExtra("USER_TYPE", userType);
                intent8.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent8);
                break;
            case R.id.topicdetailstxtCVID:
                Intent intent15 = new Intent(ReportingUserDashBoardActivity.this, TopicDetailsActivity.class);
//                intent14.putExtra("NUMBER", "");
                startActivity(intent15);
                break;


            case  R.id.createivesCVID:
                Intent intent17=new Intent(ReportingUserDashBoardActivity.this,TopicDetails_imageview.class);
                startActivity(intent17);
                break;
        }

    }

    @Override
    public void onPlansItemClicked(PlansResponse shortcutsResponse, int position, View v, PlansAdapter.ShortcutsVH holder) {
        switch (shortcutsResponse.getId()) {
            case "1":
                Intent intent = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent.putExtra("PLAN_ID", "1");
                intent.putExtra("TITLE", "Overall");
                intent.putExtra("USER_ID", userID);
                intent.putExtra("USER_TYPE", userType);
                intent.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent);
                break;
            case "2":
                Intent intent2 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent2.putExtra("PLAN_ID", "2");
                intent2.putExtra("TITLE", "Today");
                intent2.putExtra("USER_ID", userID);
                intent2.putExtra("USER_TYPE", userType);
                intent2.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent2);
                break;
            case "3":
                Intent intent3 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent3.putExtra("PLAN_ID", "3");
                intent3.putExtra("TITLE", "Pending");
                intent3.putExtra("USER_ID", userID);
                intent3.putExtra("USER_TYPE", userType);
                intent3.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent3);
                break;

            case "4":
                Intent intent4 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent4.putExtra("PLAN_ID", "4");
                intent4.putExtra("TITLE", "Future");
                intent4.putExtra("USER_ID", userID);
                intent4.putExtra("USER_TYPE", userType);
                intent4.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent4);
                break;
            case "5":
                //startActivity(new Intent(DashBoardActivity.this, SiteVisitSearchActivity.class));
                Intent intent5 = new Intent(ReportingUserDashBoardActivity.this, NewLeadsDataActivity.class);
                intent5.putExtra("PLAN_ID", "5");
                intent5.putExtra("TITLE", "Tomorrow");
                intent5.putExtra("USER_ID", userID);
                intent5.putExtra("USER_TYPE", userType);
                intent5.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent5);
                break;
            case "6":
                Intent intent6 = new Intent(ReportingUserDashBoardActivity.this, SourceTypeActivity.class);
                intent6.putExtra("USER_ID", userID);
                intent6.putExtra("USER_TYPE", userType);
                intent6.putExtra("ACTIVITY", "SOURCE");
                startActivity(intent6);
                break;
            case "7":
                Intent intent7 = new Intent(ReportingUserDashBoardActivity.this, SourceTypeActivity.class);
                intent7.putExtra("USER_ID", userID);
                intent7.putExtra("USER_TYPE", userType);
                intent7.putExtra("ACTIVITY", "PROJECT");
                startActivity(intent7);
                break;
        }
    }

    @Override
    public void onShortcutsItemClicked(ShortcutsResponse shortcutsResponse, int position, View v, ShortcutsAdapter.ShortcutsVH holder) {
        switch (shortcutsResponse.getId()) {
            case "1":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, TeleSourceActivity.class));
                break;
            case "2":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, DashBoardChangePasswordActivity.class));                break;
            case "3":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, AttendanceMainActivity.class));
                break;
            case "4":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, DirectMeeting.class));
                break;
            case "5":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, SiteVisitSearchActivity.class));
                break;
            case "6":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, CreateDayReportActivity.class));
                break;
            case "7":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, FoldersActivity.class));
                break;
            case "8":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, RecordingsActivity.class));
                break;
            case "9":
                startActivity(new Intent(ReportingUserDashBoardActivity.this, CreativesActivity.class));
                break;
        }
    }
}