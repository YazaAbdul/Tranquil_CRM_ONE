package crm.tranquil_sales_steer.ui.activities.dashboard;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CustomKnowlarityApis;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.folders.FoldersActivity;
import crm.tranquil_sales_steer.ui.activities.start_ups.LoginActivity;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import crm.tranquil_sales_steer.ui.adapters.AllActivityAdapter;
import crm.tranquil_sales_steer.ui.adapters.AllLeadPagesAdapter;
import crm.tranquil_sales_steer.ui.adapters.NewLeadPlansAdapter;
import crm.tranquil_sales_steer.ui.models.AllActivityResponse;
import crm.tranquil_sales_steer.ui.models.AllDataResponse;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeCallCreativesResponse;
import crm.tranquil_sales_steer.ui.models.NewLeadPlansResponse;
import crm.tranquil_sales_steer.ui.models.Versioncontrol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewLeadsDataActivity extends AppCompatActivity implements AllLeadPagesAdapter.LeadClickListener {


    RecyclerView leadsRCVID;
    ProgressBar loadingPb, topPB;
    FloatingActionButton refreshFab;
    RecyclerView mainDataRCID;
    LinearLayout noDataLLID;
    TextView headerTittleTVID;
    RelativeLayout backRLID;
    String version;
    String title, viewType, userID, userType, planID, activityID;
    RelativeLayout foldersRLID,cubeCallRLID;

    //page
    AllLeadPagesAdapter paginationAdapter;
    ArrayList<AllDataResponse> leadsList = new ArrayList<>();
    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    boolean isCallClick=true;
    LinearLayoutManager linearLayoutManager;
    AllActivityAdapter adapter;
    RelativeLayout searchRLID;
    KProgressHUD kProgressHUD;

    String imageStringPath;
    Bitmap image;
    Uri imageUri;
    AppCompatImageView imageID;
    Bitmap imageWhatsApp = null;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //Google Analytics
    private FirebaseAnalytics analytics;

    @SuppressLint({"RestrictedApi", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_data);
        Utilities.startAnimation(this);
        Utilities.setStatusBarGradiant(this);
        version="2";
        CheckVerionControl();
        analytics = FirebaseAnalytics.getInstance(this);
        MySharedPreferences.setPreference(this, "" + AppConstants.PAGE_REFRESH, "NO");
        /*userID = MySharedPreferences.getPreferences(NewLeadsDataActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(NewLeadsDataActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);*/
        mainDataRCID = findViewById(R.id.mainDataRCID);
        mainDataRCID.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        refreshFab = findViewById(R.id.refreshFab);
        refreshFab.setVisibility(View.GONE);
        topPB = findViewById(R.id.topPB);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                viewType = bundle.getString("" + AppConstants.VIEW_TYPE);

                if (viewType != null && viewType.equalsIgnoreCase("ACTIVITIES")) {
                    activityID = bundle.getString("ACTIVITY_ID");
                    Log.e("activityID==>",activityID);
                    title = bundle.getString("TITLE");
                    userID = bundle.getString("USER_ID");
                    userType = bundle.getString("USER_TYPE");
                    loadPlans(activityID);
                } else {
                    planID = bundle.getString("PLAN_ID");
                    Log.e("planID==>",planID);
                    title = bundle.getString("TITLE");
                    userID = bundle.getString("USER_ID");
                    userType = bundle.getString("USER_TYPE");
                    loadActivities(planID);
                }
            }
        }

        Utilities.startAnimation(this);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText(title);
        backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);

            if (viewType != null && viewType.equalsIgnoreCase("ACTIVITIES")) {
                MySharedPreferences.setPreference(this,AppConstants.PAGE_REFRESH,"YES");
            }else {
                MySharedPreferences.setPreference(this,AppConstants.PAGE_REFRESH,"NO");
            }
        });
        loadingPb = findViewById(R.id.loadingPb);
        searchRLID = findViewById(R.id.searchRLID);
        imageID = findViewById(R.id.imageID);


        searchRLID.setVisibility(View.VISIBLE);





        noDataLLID = findViewById(R.id.noDataLLID);

        searchRLID.setOnClickListener(v -> {
            Intent intent = new Intent(NewLeadsDataActivity.this, SearchViewActivity.class);
            intent.putExtra("TYPE", "SEARCH");
            startActivity(intent);
        });

        refreshFab.setOnClickListener(v -> {
            if (isClickable) {
                isClickable = false;
                CheckVerionControl();
                /*loadLeads(planID, activityID);*/

                /*if (viewType != null && viewType.equalsIgnoreCase("ACTIVITIES")) {
                    loadPlans(activityID);
                } else {
                    loadActivities(planID);
                }*/

            }
        });


        //Picasso.with(this).load("https://inwestinfra.co.in/crm/amtra/menu/1d09aedf56f160759afafb51b19f6fec.jpg").into(imageID);


        Drawable drawable = imageID.getDrawable();



    }



    private void CheckVerionControl(){

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<Versioncontrol> call=apiInterface.versionControl(version);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<Versioncontrol>() {
            @Override
            public void onResponse(Call<Versioncontrol> call, Response<Versioncontrol> response) {
                Log.e("Response==>","" + response);

                if(response.body()!=null&& response.code()==200){
                    Versioncontrol versioncontrol=response.body();
                    if(versioncontrol.getStatus().equals("1")){
                        loadLeads(planID, activityID);
                      /*  loadPlans(activityID);
                        loadActivities(planID);*/
                       // getActivesSatus();
                        //   getLoginResponse();
                    }else {

                        AlertUtilities.bottomDisplayStaticAlert(NewLeadsDataActivity.this, "Alert...!", "Update the app to latest version");
                        Intent intent=new Intent(NewLeadsDataActivity.this, LoginActivity.class);
                     /*   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                        startActivity(intent);
                        finish();
                    }

                }
            }
            @Override
            public void onFailure(Call<Versioncontrol> call, Throwable t) {

                AlertUtilities.bottomDisplayStaticAlert(NewLeadsDataActivity.this, "Alert...!", "Something went wrong at server side");

            }
        });

    }


    private void loadLeads(String planID, String activityID) {
        //MySharedPreferences.setPreference(this, AppConstants.CLEAR, AppConstants.NO);
        leadsList.clear();
        start = 0;
        isLoading=false;
        isLastPage=false;
        leadsRCVID = findViewById(R.id.leadsRCVID);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        paginationAdapter = new AllLeadPagesAdapter(this,NewLeadsDataActivity.this,leadsList,this);
        paginationAdapter.notifyDataSetChanged();

        leadsRCVID.setAdapter(paginationAdapter);
        leadsRCVID.setLayoutManager(linearLayoutManager);
        leadsRCVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("pagination","api called");
                isLastPage =false;
                isLoading = true;
                loadNextPage(planID, activityID);
            }

            @Override
            public boolean isLastPage() {
                Log.d("pagination","is last page");
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                Log.d("pagination","is loading");
                return isLoading;
            }
        });

        loadFirstPage(planID, activityID);
    }

    private void loadNextPage(String planID, String activityID) {

        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllDataResponse>> call = apiInterface.getAllData(userID, userType, planID, activityID, start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllDataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllDataResponse>> call, Response<ArrayList<AllDataResponse>> response) {

                isClickable = true;

                if (response.body() != null && response.code() == 200) {

                    leadsList = response.body();
                    paginationAdapter.removeLoadingFooter();
                    isLoading = false;

                    if (leadsList.size() > 0) {
                        paginationAdapter.addAll(leadsList);

                        if (leadsList.size() == 10) {
                            startedValues();
                            paginationAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            paginationAdapter.removeLoadingFooter();
                        }
                    } else {
                        paginationAdapter.removeLoadingFooter();
                    }
                } else {
                    paginationAdapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllDataResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                paginationAdapter.removeLoadingFooter();
            }
        });
    }




    @SuppressLint("RestrictedApi")
    private void loadFirstPage(String planID, String activityID) {
        loadingPb.setVisibility(View.VISIBLE);
        leadsRCVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
        isClickable = false;


           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllDataResponse>> call = apiInterface.getAllData(userID, userType, planID, activityID, start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllDataResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllDataResponse>> call, Response<ArrayList<AllDataResponse>> response) {

                loadingPb.setVisibility(View.GONE);
                leadsRCVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);
                refreshFab.setVisibility(View.VISIBLE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    leadsList.clear();
                    leadsList = response.body();
                    if (leadsList.size() > 0) {
                        isClickable = true;
                        startedValues();
                        paginationAdapter.addAll(leadsList);

                        if (leadsList.size() == 10) {
                            paginationAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        setErrorViews();
                    }
                } else {
                    setErrorViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllDataResponse>> call, Throwable t) {
                setErrorViews();
            }
        });
    }

    private void hideLoading() {
        topPB.setVisibility(View.GONE);
    }

    private void setErrorViews() {
        isClickable = true;
        loadingPb.setVisibility(View.GONE);
        leadsRCVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 10;
    }

    private void loadActivities(String planID) {

       /* if (isClickable){
            isClickable = true;
            activityID.equals("1");
        }*/

        topPB.setVisibility(View.VISIBLE);
        Log.d("ACT_PLAN->", "" + planID);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);

        Call<ArrayList<AllActivityResponse>> call = apiInterface.getAllActivities(planID,userID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllActivityResponse>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ArrayList<AllActivityResponse>> call, Response<ArrayList<AllActivityResponse>> response) {
                hideLoading();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<AllActivityResponse> activitiesResponses = response.body();
                    if (activitiesResponses.size() > 0) {
                        adapter = new AllActivityAdapter(NewLeadsDataActivity.this, activitiesResponses, (holder, plansResponse, typeOfClick) -> {

                            if (isClickable) {
                                isClickable = false;
                                activityID = plansResponse.getActivity_id();
                                loadLeads(planID, activityID);
                            }

                        });
                        mainDataRCID.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(NewLeadsDataActivity.this, "Activities not loading..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewLeadsDataActivity.this, "Activities not loading..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllActivityResponse>> call, Throwable t) {
                hideLoading();
                Toast.makeText(NewLeadsDataActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPlans(String activityID) {

        /*if (isClickable){
            isClickable = true;

            if (planID != null)
            planID.equals("1");
        }*/
        topPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<NewLeadPlansResponse>> call = apiInterface.getAllPlans(activityID,userID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NewLeadPlansResponse>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ArrayList<NewLeadPlansResponse>> call, Response<ArrayList<NewLeadPlansResponse>> response) {
                hideLoading();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<NewLeadPlansResponse> activitiesResponses = response.body();
                    if (activitiesResponses.size() > 0) {
                        NewLeadPlansAdapter adapter = new NewLeadPlansAdapter(NewLeadsDataActivity.this, activitiesResponses, (plansResponse, typeOfClick) -> {

                            if (isClickable) {
                                isClickable = false;
                                planID = plansResponse.getPlan_id();
                                loadLeads(planID, activityID);
                            }

                        });
                        adapter.notifyDataSetChanged();

                        mainDataRCID.setAdapter(adapter);
                    } else {
                        Toast.makeText(NewLeadsDataActivity.this, "Activities not loading..", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewLeadsDataActivity.this, "Activities not loading..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NewLeadPlansResponse>> call, Throwable t) {
                hideLoading();
                Toast.makeText(NewLeadsDataActivity.this, "Activities not loading..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onLeadItemClick(AllDataResponse response, View v, int pos, AllLeadPagesAdapter.ItemViewHolder holder) {
        switch (v.getId()) {
            case R.id.newMainLeadView:
                Intent btdelsale = new Intent(new Intent(this, LeadHistoryActivity.class));
                btdelsale.putExtra("CUSTOMER_ID", response.getS_no());
                btdelsale.putExtra("CUSTOMER_NAME", response.getFirst_name());
                btdelsale.putExtra("CUSTOMER_MOBILE", response.getMobile_number());
                btdelsale.putExtra("ACTIVITY_NAME",response.getActivity_name());
                btdelsale.putExtra("ACTIVITY_ID",response.getActivity_id());
                btdelsale.putExtra("pageFrom", false);
                startActivity(btdelsale);
                break;

            case R.id.newPhoneRL:

                /*CommunicationsServices.InsertCommunication(NewLeadsDataActivity.this, "1", response.getS_no(), userID, "", "");

                Intent intent = new Intent(NewLeadsDataActivity.this,CallCompleteActivity.class);
                intent.putExtra("ACTIVITY_NAME",response.getActivity_name());
                intent.putExtra("ACTIVITY_ID",response.getActivity_id());
                intent.putExtra("CUSTOMER_NAME",response.getFirst_name());
                intent.putExtra("CUSTOMER_MOBILE", response.getMobile_number());
                intent.putExtra("LEAD_ID",response.getS_no());
                startActivity(intent);*/

                getCallerID("1",response,"1");

                getEmployeeCallCreatives(response);




                /*if (isCallClick){
                    isCallClick=false;
                }else{
                    Utilities.showToast(this,"Connecting call please wait.....");
                }*/

                break;

            case R.id.cubeCallRLID:

                //CommunicationsServices.InsertCommunication(NewLeadsDataActivity.this, "1", response.getS_no(), userID, "", "");

                getCallerID("2",response,"5");

                break;

            case R.id.whatsAppCallRLID:



                try {
                    Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getMobile_number() + "&text=" + URLEncoder.encode("", "UTF-8");
                    sendMsg.setPackage("com.whatsapp");
                    sendMsg.setData(Uri.parse(url));
                    startActivity(sendMsg);
                    //dialog1.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    //*Utilities.showToast(this,e.getMessage());*//*
                    Toast.makeText(NewLeadsDataActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                }

                /*final BottomSheetDialog dialog1 = new BottomSheetDialog(NewLeadsDataActivity.this);
                dialog1.setContentView(R.layout.alert_business_whatsapp);

                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                int width1 = ViewGroup.LayoutParams.MATCH_PARENT;
                int height1 = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog1.getWindow().setLayout(width1, height1);
                dialog1.show();

                AppCompatImageView whatsappIVID = dialog1.findViewById(R.id.whatsappIVID);
                AppCompatImageView businessWhatsappIVID = dialog1.findViewById(R.id.businessWhatsappIVID);

                whatsappIVID.setOnClickListener(view1 -> {

                    try {
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getMobile_number() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        *//*Utilities.showToast(this,e.getMessage());*//*
                        Toast.makeText(NewLeadsDataActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                });

                businessWhatsappIVID.setOnClickListener(view2 -> {

                    try {
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getMobile_number() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp.w4b");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        *//*Utilities.showToast(this,e.getMessage());*//*
                        Toast.makeText(NewLeadsDataActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                    dialog1.dismiss();

                });*/

                break;

            case R.id.businessWhatsAppCallRLID:
                if (isWhatsAppBusinessInstalled()) {
                    // Open WhatsApp Business
                    //openWhatsAppBusiness();
                    //Toast.makeText(this, "Bussnesswhat is theree", Toast.LENGTH_SHORT).show();


                    Intent intentW = new Intent(NewLeadsDataActivity.this, SendTemplatesActivity.class);
                    intentW.putExtra("TYPE", "Send Whats App");
                    intentW.putExtra("LEAD_MOBILE_NUMBER", "" +response.getMobile_number());
                    //  intentW.putExtra("LEAD_EMAIL", "" + emailStr);
                    intentW.putExtra("LEAD_ID", "" +response.getS_no());
                    intentW.putExtra("CUSTOMER_NAME",response.getFirst_name());
                    startActivity(intentW);


                   /* try {
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getMobile_number() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp.w4b");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);

                    } catch (Exception e) {
                        e.printStackTrace();

                       // Toast.makeText(NewLeadsDataActivity.this, "You don't have Business WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    // Open regular WhatsApp
                    // openWhatsApp();
                    // Toast.makeText(this, "Normal bussness is theree", Toast.LENGTH_SHORT).show();
                  /*  try {
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getMobile_number() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);

                    } catch (Exception e) {
                        e.printStackTrace();

                        // Toast.makeText(NewLeadsDataActivity.this, "You don't have Business WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }*/


                    Intent intentW = new Intent(NewLeadsDataActivity.this, SendTemplatesActivity.class);
                    intentW.putExtra("TYPE", "Send Whats App");
                    intentW.putExtra("LEAD_MOBILE_NUMBER", "" +response.getMobile_number());
                    //  intentW.putExtra("LEAD_EMAIL", "" + emailStr);
                    intentW.putExtra("LEAD_ID", "" +response.getS_no());
                    intentW.putExtra("CUSTOMER_NAME",response.getFirst_name());
                    startActivity(intentW);
                }




                break;

            case R.id.foldersRLID:
                if (response.getEmail_id().matches(emailPattern)) {
                    Intent folder=new Intent(NewLeadsDataActivity.this, FoldersActivity.class);
                    folder.putExtra("TYPE", "FROM_LEADS");
                    folder.putExtra("LEAD_ID", "" + response.getS_no());
                    startActivity(folder);
                } else {

                    Toast.makeText(this, "This lead don't have email", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.messageBtnRLID:
                Intent intentMsg=new Intent(NewLeadsDataActivity.this, SendTemplatesActivity.class);
                intentMsg.putExtra("TYPE", "Send Sms");
                intentMsg.putExtra("LEAD_MOBILE_NUMBER", "" + response.getMobile_number());
                intentMsg.putExtra("LEAD_EMAIL", "" + response.getEmail_id());
                intentMsg.putExtra("LEAD_ID", "" + response.getS_no());
                intentMsg.putExtra("CUSTOMER_NAME", "" + response.getFirst_name());
                startActivity(intentMsg);
                break;

            case R.id.mailBtnRLID:
                if(response.getEmail_id().isEmpty()){
                    Toast.makeText(this, "No Mail", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent2 = new Intent(NewLeadsDataActivity.this, SendTemplatesActivity.class);
                    intent2.putExtra("TYPE", "Send Email");
                    intent2.putExtra("LEAD_MOBILE_NUMBER", "" + response.getMobile_number());
                    intent2.putExtra("LEAD_EMAIL", "" + response.getEmail_id());
                    intent2.putExtra("LEAD_ID", "" + response.getS_no());
                    intent2.putExtra("CUSTOMER_NAME", "" + response.getFirst_name());
                    startActivity(intent2);
                }

                break;
        }
    }

    private boolean isWhatsAppBusinessInstalled() {
        try {
            getPackageManager().getPackageInfo("com.whatsapp.w4b", 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void getEmployeeCallCreatives(AllDataResponse response) {

        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<EmployeeCallCreativesResponse> call = apiInterface.getEmployeeCallCreatives(response.getS_no(),userID);
        Log.e("apigetemployee==>",call.request().toString());
        call.enqueue(new Callback<EmployeeCallCreativesResponse>() {
            @Override
            public void onResponse(Call<EmployeeCallCreativesResponse> call, Response<EmployeeCallCreativesResponse> response) {

                if (response.body() != null && response.code() == 200){
                    Log.e("creatives==>","" + "Success");
                }else {
                    Log.e("creatives==>","" + "Failed");
                }
            }

            @Override
            public void onFailure(Call<EmployeeCallCreativesResponse> call, Throwable t) {
                Log.e("creatives==>","" + "Error");
            }
        });
    }

    private void getCallerID( String type, AllDataResponse response1,String communications) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(response1.getS_no(),"","",communications,userID,false);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationsResponse>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ArrayList<CommunicationsResponse>> call, Response<ArrayList<CommunicationsResponse>> response) {

                // Utilities.dismissProgress(kProgressHUD);

                if (response.body() != null && response.code() == 200) {
                    ArrayList<CommunicationsResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equals("1")) {

                                if (type.equalsIgnoreCase("1")){

                                    Intent intentCube = new Intent(NewLeadsDataActivity.this,CallCompleteActivity.class);
                                    intentCube.putExtra("ACTIVITY_NAME",response1.getActivity_name());
                                    intentCube.putExtra("ACTIVITY_ID",response1.getActivity_id());
                                    intentCube.putExtra("CUSTOMER_NAME",response1.getFirst_name());
                                    intentCube.putExtra("CUSTOMER_MOBILE", response1.getMobile_number());
                                    intentCube.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    intentCube.putExtra("LEAD_ID",response1.getS_no());
                                    startActivity(intentCube);

                                    numberCalling(response1.getMobile_number());

                                }else if (type.equalsIgnoreCase("2")){

                                    CustomKnowlarityApis.clickToKnowlarityCall(NewLeadsDataActivity.this,userID,response1.getMobile_number(),kProgressHUD);

                                    Intent intentCube = new Intent(NewLeadsDataActivity.this,CallCompleteActivity.class);
                                    intentCube.putExtra("ACTIVITY_NAME",response1.getActivity_name());
                                    intentCube.putExtra("ACTIVITY_ID",response1.getActivity_id());
                                    intentCube.putExtra("CUSTOMER_NAME",response1.getFirst_name());
                                    intentCube.putExtra("CUSTOMER_MOBILE", response1.getMobile_number());
                                    intentCube.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    intentCube.putExtra("LEAD_ID",response1.getS_no());
                                    startActivity(intentCube);

                                }




                                Log.e("caller_id","" + statusResponses.get(i).getCall_id());


                                Log.d("INSERT_MESSAGE", "" + type + " : Inserted");

                            } else {

                                Utilities.AlertDaiolog(NewLeadsDataActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            }
                        }
                    } else {
                        Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                        Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                    }
                } else {
                    Utilities.AlertDaiolog(NewLeadsDataActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                    Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                    Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {
                Utilities.AlertDaiolog(NewLeadsDataActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
            }
        });
    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);

            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    private void numberCalling(String number) {

        Log.e(
                "numberCalling",number
        );
        Utilities.makePhoneCall(number,this);

        /*try {
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
        }*/

        /*try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
                if (telecomManager != null && telecomManager.getDefaultOutgoingPhoneAccount(PhoneAccount.SCHEME_TEL) != null) {
                    callIntent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, telecomManager.getDefaultOutgoingPhoneAccount(PhoneAccount.SCHEME_TEL));
                }

                startActivity(callIntent);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (viewType != null && viewType.equalsIgnoreCase("ACTIVITIES")) {
            loadPlans(activityID);
        } else {
            loadActivities(planID);
        }*/

        String pageRefresh = MySharedPreferences.getPreferences(this, "" + AppConstants.PAGE_REFRESH);
        if (pageRefresh.equalsIgnoreCase(AppConstants.YES)) {
            //MySharedPreferences.setPreference(this, "" + AppConstants.PAGE_REFRESH, "" + AppConstants.NO);
            loadLeads(planID, activityID);
            /*if (viewType.equalsIgnoreCase("ACTIVITIES")) {
                loadPlans(activityID);
            } else {
                loadActivities(planID);
            }*/

           // loadActivities(planID);
        }
        }
    }

