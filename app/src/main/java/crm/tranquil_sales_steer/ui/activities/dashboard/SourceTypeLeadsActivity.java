package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.net.URLEncoder;
import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CustomKnowlarityApis;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.SourceTypeLeadsAdapter;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.SourceTypeLeadsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceTypeLeadsActivity extends AppCompatActivity implements SourceTypeLeadsAdapter.LeadClickListener {

    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    LinearLayoutManager linearLayoutManager;

    RecyclerView sourceTypeLeadsRVID;
    ProgressBar loadingPb;
    FloatingActionButton refreshFab;
    LinearLayout noDataLLID;
    TextView headerTittleTVID;
    RelativeLayout backRLID;
    SourceTypeLeadsAdapter adapter;
    ArrayList<SourceTypeLeadsResponse> sourceTypeLeadsResponses = new ArrayList<>();
    String userID,userType,sourceName,sourceID,activityStr;
    KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_type_leads);
        Utilities.setStatusBarGradiant(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                sourceName = bundle.getString("SOURCE_NAME");
                sourceID = bundle.getString("SOURCE_ID");
                userID = bundle.getString("USER_ID");
                userType = bundle.getString("USER_TYPE");
                activityStr = bundle.getString("ACTIVITY");

            }
        }

        MySharedPreferences.setPreference(SourceTypeLeadsActivity.this, "" + AppConstants.PAGE_REFRESH, "NO");

        Log.e("SourceName==>", "" + sourceName);

        /*userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);*/

        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText(sourceName);
        backRLID = findViewById(R.id.backRLID);
        noDataLLID = findViewById(R.id.noDataLLID);
        loadingPb = findViewById(R.id.loadingPb);
        sourceTypeLeadsRVID = findViewById(R.id.sourceTypeLeadsRVID);
        refreshFab = findViewById(R.id.refreshFab);
        backRLID.setOnClickListener(v -> Utilities.finishAnimation(this));

        refreshFab.setOnClickListener(v -> {
            if (isClickable) {
                isClickable = false;

                loadSourceTypeLeads();

            }
        });

        if (Utilities.isNetworkAvailable(this)){

            loadSourceTypeLeads();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSourceTypeLeads() {

        sourceTypeLeadsResponses.clear();
        start = 0;
        isLoading=false;
        isLastPage=false;
        
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new SourceTypeLeadsAdapter(SourceTypeLeadsActivity.this,SourceTypeLeadsActivity.this,sourceTypeLeadsResponses);

        sourceTypeLeadsRVID.setAdapter(adapter);
        sourceTypeLeadsRVID.setLayoutManager(linearLayoutManager);
        sourceTypeLeadsRVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("pagination","api called");
                isLastPage =false;
                isLoading = true;
                loadNextPage();
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

        loadFirstPage();
    }

    private void loadFirstPage() {

        loadingPb.setVisibility(View.VISIBLE);
        sourceTypeLeadsRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
        isClickable = false;

        Call<ArrayList<SourceTypeLeadsResponse>> call = null;

        if (activityStr.equalsIgnoreCase("SOURCE")) {
               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            call = apiInterface.getSourceTypeLeads(userID, userType,sourceID, start);
        } else {

               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            call = apiInterface.getProjectTypeLeads(userID, userType,sourceID, start);
        }

        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SourceTypeLeadsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SourceTypeLeadsResponse>> call, Response<ArrayList<SourceTypeLeadsResponse>> response) {

                loadingPb.setVisibility(View.GONE);
                sourceTypeLeadsRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);
                refreshFab.setVisibility(View.VISIBLE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    sourceTypeLeadsResponses.clear();
                    sourceTypeLeadsResponses = response.body();
                    if (sourceTypeLeadsResponses.size() > 0) {
                        isClickable = true;
                        startedValues();
                        adapter.addAll(sourceTypeLeadsResponses);

                        if (sourceTypeLeadsResponses.size() == 20) {
                            adapter.addLoadingFooter();
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
            public void onFailure(Call<ArrayList<SourceTypeLeadsResponse>> call, Throwable t) {
                setErrorViews();
            }
        });

    }

    private void loadNextPage() {

        Log.d("START_VALUE",""+start);

        Call<ArrayList<SourceTypeLeadsResponse>> call = null;

        if (activityStr.equalsIgnoreCase("SOURCE")) {
               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            call = apiInterface.getSourceTypeLeads(userID, userType,sourceID, start);
        } else {

               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            call = apiInterface.getProjectTypeLeads(userID, userType,sourceID, start);
        }

        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SourceTypeLeadsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SourceTypeLeadsResponse>> call, Response<ArrayList<SourceTypeLeadsResponse>> response) {
                isClickable = true;
                if (response.body() != null && response.code() == 200) {
                    sourceTypeLeadsResponses = response.body();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (sourceTypeLeadsResponses.size() > 0) {
                        adapter.addAll(sourceTypeLeadsResponses);

                        if (sourceTypeLeadsResponses.size() == 20) {
                            startedValues();
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            adapter.removeLoadingFooter();
                        }
                    } else {
                        adapter.removeLoadingFooter();
                    }
                } else {
                    adapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SourceTypeLeadsResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        loadingPb.setVisibility(View.GONE);
        sourceTypeLeadsRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 20;
    }

    @Override
    public void onLeadItemClick(SourceTypeLeadsResponse response, View v, int pos, SourceTypeLeadsAdapter.ItemViewHolder holder) {

        switch (v.getId()) {
            case R.id.newMainLeadView:
                Intent btdelsale = new Intent(new Intent(this, LeadHistoryActivity.class));
                btdelsale.putExtra("CUSTOMER_ID", response.getLead_id());
                btdelsale.putExtra("CUSTOMER_NAME", response.getLead_name());
                btdelsale.putExtra("CUSTOMER_MOBILE", response.getLead_mobile());
                btdelsale.putExtra("ACTIVITY_NAME",response.getActivity_name());
                btdelsale.putExtra("ACTIVITY_ID",response.getActivity_id());
                btdelsale.putExtra("pageFrom", false);
                startActivity(btdelsale);
                break;

            case R.id.newPhoneRL:

                /*CommunicationsServices.InsertCommunication(SourceTypeLeadsActivity.this, "1", response.getLead_id(), userID, "", "");

                Intent intent = new Intent(SourceTypeLeadsActivity.this,CallCompleteActivity.class);
                intent.putExtra("ACTIVITY_NAME",response.getActivity_name());
                intent.putExtra("ACTIVITY_ID",response.getActivity_id());
                intent.putExtra("CUSTOMER_NAME",response.getLead_name());
                intent.putExtra("CUSTOMER_MOBILE", response.getLead_mobile());
                intent.putExtra("LEAD_ID",response.getLead_id());
                startActivity(intent);

                numberCalling(response.getLead_mobile());*/

                getCallerID("1",response,"1");


                /*if (isCallClick){
                    isCallClick=false;
                }else{
                    Utilities.showToast(this,"Connecting call please wait.....");
                }*/

                break;

            /*case R.id.cubeCallRLID:

                CommunicationsServices.InsertCommunication(SourceTypeLeadsActivity.this, "1", response.getLead_id(), userID, "", "");
                CustomKnowlarityApis.clickToKnowlarityCall(SourceTypeLeadsActivity.this,userID,response.getLead_mobile(),kProgressHUD);

                Intent intentCube = new Intent(SourceTypeLeadsActivity.this,CallCompleteActivity.class);
                intentCube.putExtra("ACTIVITY_NAME",response.getActivity_name());
                intentCube.putExtra("ACTIVITY_ID",response.getActivity_id());
                intentCube.putExtra("CUSTOMER_NAME",response.getLead_name());
                intentCube.putExtra("CUSTOMER_MOBILE", response.getLead_mobile());
                intentCube.putExtra("LEAD_ID",response.getLead_id());
                startActivity(intentCube);

                break;*/

            case R.id.whatsAppCallRLID:
                final BottomSheetDialog dialog1 = new BottomSheetDialog(SourceTypeLeadsActivity.this);
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
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getLead_mobile() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showToast(this,e.getMessage());
                        Toast.makeText(SourceTypeLeadsActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                });

                businessWhatsappIVID.setOnClickListener(view2 -> {

                    try {

                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getLead_mobile() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp.w4b");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);



                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showToast(this,e.getMessage());
                        Toast.makeText(SourceTypeLeadsActivity.this, "You don't have business WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                    dialog1.dismiss();

                });

                break;

           /* case R.id.messageBtnRLID:
                Intent intentMsg=new Intent(SourceTypeLeadsActivity.this, SendTemplatesActivity.class);
                intentMsg.putExtra("TYPE", "Send Sms");
                intentMsg.putExtra("LEAD_MOBILE_NUMBER", "" + response.getLead_mobile());
                intentMsg.putExtra("LEAD_EMAIL", "" + response.getLead_email());
                intentMsg.putExtra("LEAD_ID", "" + response.getLead_id());
                intentMsg.putExtra("CUSTOMER_NAME", "" + response.getLead_name());
                startActivity(intentMsg);
                break;

            case R.id.mailBtnRLID:

                Intent intent2=new Intent(SourceTypeLeadsActivity.this, SendTemplatesActivity.class);
                intent2.putExtra("TYPE", "Send Email");
                intent2.putExtra("LEAD_MOBILE_NUMBER", "" + response.getLead_mobile());
                intent2.putExtra("LEAD_EMAIL", "" + response.getLead_email());
                intent2.putExtra("LEAD_ID", "" + response.getLead_id());
                intent2.putExtra("CUSTOMER_NAME", "" + response.getLead_name());
                startActivity(intent2);

                break;*/
        }
    }

    private void getCallerID( String type, SourceTypeLeadsResponse response1,String communications) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(response1.getLead_id(),"","",communications,userID,false);
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

                                    Intent intentCube = new Intent(SourceTypeLeadsActivity.this,CallCompleteActivity.class);
                                    intentCube.putExtra("ACTIVITY_NAME",response1.getActivity_name());
                                    intentCube.putExtra("ACTIVITY_ID",response1.getActivity_id());
                                    intentCube.putExtra("CUSTOMER_NAME",response1.getLead_name());
                                    intentCube.putExtra("CUSTOMER_MOBILE", response1.getLead_mobile());
                                    intentCube.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    intentCube.putExtra("LEAD_ID",response1.getLead_id());
                                    startActivity(intentCube);

                                    numberCalling(response1.getLead_mobile());

                                }else if (type.equalsIgnoreCase("2")){

                                    CustomKnowlarityApis.clickToKnowlarityCall(SourceTypeLeadsActivity.this,userID,response1.getLead_mobile(),kProgressHUD);

                                    Intent intentCube = new Intent(SourceTypeLeadsActivity.this,CallCompleteActivity.class);
                                    intentCube.putExtra("ACTIVITY_NAME",response1.getActivity_name());
                                    intentCube.putExtra("ACTIVITY_ID",response1.getActivity_id());
                                    intentCube.putExtra("CUSTOMER_NAME",response1.getLead_name());
                                    intentCube.putExtra("CUSTOMER_MOBILE", response1.getLead_mobile());
                                    intentCube.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    intentCube.putExtra("LEAD_ID",response1.getLead_id());
                                    startActivity(intentCube);

                                }




                                Log.e("caller_id","" + statusResponses.get(i).getCall_id());


                                Log.d("INSERT_MESSAGE", "" + type + " : Inserted");

                            } else {

                                Utilities.AlertDaiolog(SourceTypeLeadsActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            }
                        }
                    } else {
                        Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                        Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                    }
                } else {
                    Utilities.AlertDaiolog(SourceTypeLeadsActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                    Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                    Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {
                Utilities.AlertDaiolog(SourceTypeLeadsActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();

        String pageRefresh = MySharedPreferences.getPreferences(SourceTypeLeadsActivity.this,AppConstants.PAGE_REFRESH);

        if (pageRefresh.equalsIgnoreCase("YES")){

            loadSourceTypeLeads();

        }
    }
}