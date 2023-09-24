package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.BuildConfig;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CustomKnowlarityApis;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.remainders.AddReminderActivity;
import crm.tranquil_sales_steer.ui.activities.tags.TagDetailsActivity;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import crm.tranquil_sales_steer.ui.adapters.ActivitiesAdapter;
import crm.tranquil_sales_steer.ui.adapters.DeadReasonsAdapter;
import crm.tranquil_sales_steer.ui.fragments.LeadCommunicationFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadHistoryFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadJourneyFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadProfileFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadRecordingFragment;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.AllStatusResponse;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.DisableResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeCallCreativesResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadHistoryActivity extends AppCompatActivity implements View.OnClickListener {


    DeadReasonNewAdapter deadReasonNewAdapter;
    RelativeLayout mainRLID;
    CoordinatorLayout scroll;
    RelativeLayout callBtnRLID, messageBtnRLID, mailBtnRLID,webMailRLID, whatsAppBtnRLID, estimationBtnRLID, calenderBtnRLID, locationBtnRLID, websiteBtnRLID, linkedInBtnRLID, googleBtnRLID, shareBtnRLID, editRLID,submitRLID;
    TabLayout tabs;
    ViewPager view_pager;
    ViewPagerAdapter adapter;
    ProgressBar  drPB;

    String activityOtherStr;
    TextView ReasonTVID;

    public static String customerID, nameStr, emailStr, mobileStr;
    String autoID, activityName, companyID, userID,activityID;
    TextView companyTVID, customerNaTVID, scheduledActivityNameID, scheduledActivityDateID;
    Button cancelBtnID, completeBtnID;
    ProgressBar detailsPB;
    ExpandableHeightGridView dropDownGVID;
    ExpandableHeightGridView dropDownLVID;
    LinearLayout otherFieldLLID;
    EditText otherFieldETID;
    boolean otherActivityIsVisible = false;

    RelativeLayout bottomActiveAlert, bottomDisAlert, closeMenuRLID,nextActRLID;
    RelativeLayout menuID, details;
    GridView menuGVID;
    ArrayList<LeadEditActivity.LeadProfileResponse> profileResponses;
    //new
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    RelativeLayout leadBasicRLID;
    Toolbar historyToolbar;
    CircleImageView toolBarPicID;
    TextView toolBarNameTVID, toolBarMobileTVID;
    Dialog dialog;
    int repeatCount = 0;

    Calendar calendar;
    private int year, month, day;
    Spinner nextActivity;
    ArrayList<ActivityMainResponse> activityMainResponses = new ArrayList<>();
    String nextActIDStr,dateStr,activityStr,companyNameStr;
    ActivitiesAdapter aAdapter;
    RelativeLayout nextActivityRLID,saleRLID,disableRLID;
    StatusResponse statusResponse;
    EditText noteETID,otherReasonETID;
    RelativeLayout reasonRLID,searchRLID;
    Button removeBtn;
    ArrayList<DisableResponse> disableResponses = new ArrayList<>();
    DeadReasonsAdapter deadReasonsAdapter;
    Spinner reasonsSPID;
    String reasonsStr,reasonID;
    Boolean pageFrom = false;
    KProgressHUD kProgressHUD;

    //Google Analytics
    private FirebaseAnalytics analytics;

    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_history_new);

        Utilities.setStatusBarGradiant(this);

        analytics = FirebaseAnalytics.getInstance(this);

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id._backID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreferences.setPreference(LeadHistoryActivity.this, "" + AppConstants.PAGE_REFRESH, "YES");

                onBackPressedAnimation();
            }
        });


        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                customerID = bundle.getString("CUSTOMER_ID");
                nameStr = bundle.getString("CUSTOMER_NAME");
                mobileStr = bundle.getString("CUSTOMER_MOBILE");
                activityID = bundle.getString("ACTIVITY_ID");
                activityName = bundle.getString("ACTIVITY_NAME");
                pageFrom = bundle.getBoolean("pageFrom",false);
            }
        }

        Log.d("CUSTOMER_ID : ", "" + customerID);


        mainRLID = findViewById(R.id.mainRLID);
        scroll = findViewById(R.id.scrollCordinate);
        //RelativeLayout _remainderID = findViewById(R.id._remainderID);


        userID = MySharedPreferences.getPreferences(LeadHistoryActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(LeadHistoryActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        historyToolbar = findViewById(R.id.historyToolbar);

        //collapsingToolbar = findViewById(R.id.collapse_toolbar);


        appBarLayout = findViewById(R.id.appbar);
        //leadBasicRLID = findViewById(R.id.leadBasicRLID);
        toolBarPicID = findViewById(R.id.toolBarPicID);
        toolBarNameTVID = findViewById(R.id.toolBarNameTVID);
        toolBarMobileTVID = findViewById(R.id.toolBarMobileTVID);
        ReasonTVID = findViewById(R.id.reasonTVID);

//searching dialogbox
     /*   dropDownGVID = dialog.findViewById(R.id.dropDownLVID);
        dropDownGVID.setFocusable(false);*/
      /*  final EditText searchHintETID = dialog.findViewById(R.id.searchHintETID);
        final LinearLayout searchLLID = dialog.findViewById(R.id.searchLLID);
        searchLLID.setFocusable(false);*/

        tabs = findViewById(R.id.tabs);
        view_pager = findViewById(R.id.view_pager);
        tabs.setupWithViewPager(view_pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LeadProfileFragment().newInstance(customerID), "Profile");
        adapter.addFrag(new LeadHistoryFragment().newInstance(customerID), "Completed Activities");
       // adapter.addFrag(new LeadJourneyFragment().newInstance(customerID), "Lead Journey");
        String planType = MySharedPreferences.getPreferences(this,"PLAN_TYPE");
        Log.e("py", ""+planType);
        if(planType.equals("1")){
            adapter.addFrag(new LeadJourneyFragment().newInstance(customerID), "Activity Journey ");
        }else if(planType.equals("2")){
            adapter.addFrag(new LeadJourneyFragment().newInstance(customerID), "Activity Journey ");
        }

        adapter.addFrag(new LeadCommunicationFragment().newInstance(customerID), "Communications");
    //    adapter.addFrag(new LeadRecordingFragment().newInstance(mobileStr), "Recordings");
     //   adapter.addFrag(new FolderFragment().newInstance(customerID), "File Manager");
        adapter.addFrag(new LeadRecordingFragment().newInstance(mobileStr), "Call Recordings");
        //adapter.addFrag(new LeadAccountStatementFragment().newInstance(customerID), "Account Statement");
        //adapter.addFrag(new LeadQuotationsFragment().newInstance(customerID), "Quotations");
        //adapter.addFrag(new LeadClosedBusinessFragment().newInstance(customerID), "Closed Business");
        //adapter.addFrag(new LeadProformaInvoiceFragment().newInstance(customerID), "Proforma Invoice");
        //adapter.addFrag(new LeadInvoiceFragment().newInstance(customerID), "Invoices");
        //adapter.addFrag(new LeadReceiptsFragment().newInstance(customerID), "Receipts");
        //adapter.addFrag(new LeadBillsFragment().newInstance(customerID), "Bills");
        //adapter.addFrag(new LeadPaymentsFragment().newInstance(customerID), "Payments");
        view_pager.setAdapter(adapter);

        int num = 10;
     //   tabs.setBackgroundColor(getResources().getColor(R.color.white));
        appBarLayout.setBackgroundColor(getResources().getColor(R.color.bg_background));
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.edit_b));
        tabs.setTabTextColors(getResources().getColor(R.color.edit_b), getResources().getColor(R.color.white));
        tabs.setTextAlignment(num);

        //customerNaTVID = findViewById(R.id.customerNaTVID);

        editRLID = findViewById(R.id.editRLID);
        callBtnRLID = findViewById(R.id.callBtnRLID);
        messageBtnRLID = findViewById(R.id.messageBtnRLID);
        mailBtnRLID = findViewById(R.id.mailBtnRLID);
        webMailRLID = findViewById(R.id.webMailRLID);
        whatsAppBtnRLID = findViewById(R.id.whatsAppBtnRLID);
        estimationBtnRLID = findViewById(R.id.estimationBtnRLID);
        estimationBtnRLID.setVisibility(View.GONE);
        calenderBtnRLID = findViewById(R.id.calenderBtnRLID);
        locationBtnRLID = findViewById(R.id.locationBtnRLID);
        websiteBtnRLID = findViewById(R.id.websiteBtnRLID);
        linkedInBtnRLID = findViewById(R.id.linkedInBtnRLID);
        googleBtnRLID = findViewById(R.id.googleBtnRLID);

        shareBtnRLID = findViewById(R.id.shareBtnRLID);
        menuID = findViewById(R.id.menuID);
        details = findViewById(R.id.details);
        menuGVID = findViewById(R.id.menuGVID);
        nextActivityRLID = findViewById(R.id.nextActivityRLID);
        saleRLID = findViewById(R.id.saleRLID);
        submitRLID = findViewById(R.id.submitRLID);
        disableRLID = findViewById(R.id.disableRLID);
        searchRLID = findViewById(R.id.searchRLID);


        details.setVisibility(View.VISIBLE);
        menuID.setVisibility(View.GONE);

        editRLID.setOnClickListener(this);
        callBtnRLID.setOnClickListener(this);
        messageBtnRLID.setOnClickListener(this);
        mailBtnRLID.setOnClickListener(this);
        webMailRLID.setOnClickListener(this);
        whatsAppBtnRLID.setOnClickListener(this);
        estimationBtnRLID.setOnClickListener(this);
        calenderBtnRLID.setOnClickListener(this);
        locationBtnRLID.setOnClickListener(this);
        websiteBtnRLID.setOnClickListener(this);
        linkedInBtnRLID.setOnClickListener(this);
        googleBtnRLID.setOnClickListener(this);
        shareBtnRLID.setOnClickListener(this);

        detailsPB = findViewById(R.id.detailsPB);
        scheduledActivityNameID = findViewById(R.id.scheduledActivityNameID);
        scheduledActivityDateID = findViewById(R.id.scheduledActivityDateID);
        cancelBtnID = findViewById(R.id.cancelBtnID);
        completeBtnID = findViewById(R.id.completeBtnID);
        cancelBtnID.setOnClickListener(this);
        completeBtnID.setOnClickListener(this);
        //companyTVID = findViewById(R.id.companyTVID);
        loadLeadProfileDetails(customerID);


        bottomActiveAlert = findViewById(R.id.bottomActiveAlert);
        bottomDisAlert = findViewById(R.id.bottomDisAlert);
        closeMenuRLID = findViewById(R.id.closeMenuRLID);
        nextActRLID = findViewById(R.id.nextActRLID);

        closeMenuRLID.setOnClickListener(this);
        nextActRLID.setOnClickListener(this);
        scheduledActivityDateID.setOnClickListener(this);
        nextActivityRLID.setOnClickListener(this);
        saleRLID.setOnClickListener(this);
        submitRLID.setOnClickListener(this);
        disableRLID.setOnClickListener(this);
        searchRLID.setOnClickListener(this);
        //bottomDisAlert.setOnClickListener(this);
       // bottomActiveAlert.setOnClickListener(this);

        //changeToolbarType();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }


    @Override
    protected void onResume() {
        super.onResume();

        dismissProgressDialog();

        if (Utilities.isNetworkAvailable(this)) {
            loadLeadProfileDetails(customerID);
            loadPresentActivityDetails(customerID);
            //getTagsData();
        } else {
            AlertUtilities.bottomDisplayStaticAlert(this, "No Internet..", "MAke sure your device is connected to internet");
        }
    }

    @Override
    public void onBackPressed() {
        onBackPressedAnimation();

    }

    private void onBackPressedAnimation() {

        if (pageFrom){
            Intent i=new Intent(this,DashBoardActivity.class);
            startActivity(i);
            finish();
        }else{
            finish();
            overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lead_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                AlertUtilities.finishAnimation(this);
                break;

            case R.id.editLead:
                bottomActiveAlert.setVisibility(View.VISIBLE);
                bottomDisAlert.setVisibility(View.GONE);
                menuID.setVisibility(View.GONE);
                Intent intentC = new Intent(LeadHistoryActivity.this, LeadEditActivity.class);
                intentC.putExtra("CUSTOMER_ID", "" + customerID);
                startActivity(intentC);
                break;

            case R.id.remainderLead:
                bottomActiveAlert.setVisibility(View.VISIBLE);
                bottomDisAlert.setVisibility(View.GONE);
                menuID.setVisibility(View.GONE);
                Intent remainder = new Intent(new Intent(LeadHistoryActivity.this, AddReminderActivity.class));
                remainder.putExtra("CUSTOMER_NAME", nameStr);
                remainder.putExtra("CUSTOMER_NUMBER", mobileStr);
                remainder.putExtra("CUSTOMER_ID", customerID);
                remainder.putExtra("ACTIVITY_ID", "" + activityName);
                remainder.putExtra("PROJECT_NAME", "" + activityName);
                remainder.putExtra("CUSTOMER_INFO", profileResponses);
                startActivity(remainder);
                break;

            case R.id.callToLead:
                numberCalling(mobileStr);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.closeMenuRLID:

                bottomActiveAlert.setVisibility(View.VISIBLE);
                bottomDisAlert.setVisibility(View.GONE);
                menuID.setVisibility(View.GONE);
                break;

            case R.id.nextActRLID:

               Dialog dialogS = new Dialog(this);
                dialogS.setContentView(R.layout.activity_alert);
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                dialogS.getWindow().setLayout(width, height);
                dialogS.getWindow().setGravity(Gravity.CENTER);

                dialogS.show();

                Button saveBtn;

                nextActivity = dialogS.findViewById(R.id.nextActivity);
                saveBtn = dialogS.findViewById(R.id.saveBtn);

                activities();


                nextActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        nextActIDStr = activityMainResponses.get(i).getActivity_id();
                        activityStr = activityMainResponses.get(i).getActivity_name();

                        if (repeatCount == 1) {
                            repeatCount++;

                            for (int j = 0; j < activityMainResponses.size(); j++) {

                                nextActivity.setSelection(j);

                            }
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                saveBtn.setOnClickListener(v -> {

                    scheduledActivityNameID.setText(activityStr);
                    //scheduledActivityNameID.setText(activityMainResponses.get(i).getActivity_name());
                    completeResponse( customerID, companyID,  autoID);

                    dialogS.dismiss();

                });

                break;

            case R.id.scheduledActivityDateID:


                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(LeadHistoryActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        //dateTVID.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                        dateStr = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                        scheduledActivityDateID.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                        completeResponse( customerID, companyID,  autoID);
                                    }

                                }, year, month, day);
                        datePickerDialog.show();



                break;

            case R.id.nextActivityRLID:

                   ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
                Call<StatusResponse> call = apiInterface.updateNextActivity(customerID,nextActIDStr,userID,companyID);
                Log.e("api==>",call.request().toString());

                call.enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                        if (response.body() != null && response.code() == 200){

                            statusResponse = response.body();

                            if (statusResponse.getStatus().equalsIgnoreCase("1")){
                                Toast.makeText(LeadHistoryActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(LeadHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Toast.makeText(LeadHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();

                    }
                });

                break;

            case R.id.saleRLID:

                final Dialog dialog = new Dialog(LeadHistoryActivity.this);
                dialog.setContentView(R.layout.sale_alert);
                int height1 = ViewGroup.LayoutParams.WRAP_CONTENT;
                int width1 = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(width1, height1);
                dialog.getWindow().setGravity(Gravity.CENTER);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                EditText saleETID;
                Button saveSaleBtn;

                saveSaleBtn = dialog.findViewById(R.id.saveSaleBtn);
                saleETID = dialog.findViewById(R.id.saleETID);

                saveSaleBtn.setOnClickListener(v -> {

                    validation(dialog,saleETID);
                });

                break;

            case R.id.submitRLID:



                break;

            case R.id.disableRLID:

                final Dialog disableDialog = new Dialog(LeadHistoryActivity.this);
                disableDialog.setContentView(R.layout.disable_alert);
                int height2 = ViewGroup.LayoutParams.WRAP_CONTENT;
                int width2 = ViewGroup.LayoutParams.MATCH_PARENT;
                disableDialog.getWindow().setLayout(width2, height2);
                disableDialog.getWindow().setGravity(Gravity.CENTER);
                Objects.requireNonNull(disableDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                disableDialog.show();

                noteETID = disableDialog.findViewById(R.id.noteETID);
                otherReasonETID = disableDialog.findViewById(R.id.otherReasonETID);
                removeBtn = disableDialog.findViewById(R.id.removeBtn);
                reasonRLID = disableDialog.findViewById(R.id.reasonRLID);
                 reasonRLID.setOnClickListener(this);
                reasonsSPID = (Spinner)disableDialog.findViewById(R.id.reasonsSPID);
                reasonTVID=disableDialog.findViewById(R.id.reasonTVID);
               deadReasons(reasonsSPID);


                reasonsSPID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {

                        reasonsStr = disableResponses.get(i).getDead_reason();
                        reasonID = disableResponses.get(i).getReason_id();
                        if (disableResponses.get(i).getReason_id() != null && disableResponses.get(i).getReason_id().equalsIgnoreCase("0")){
                            otherReasonETID.setVisibility(View.VISIBLE);
                        }else {
                            otherReasonETID.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                removeBtn.setOnClickListener(v -> {

                    validations(disableDialog,noteETID,otherReasonETID,reasonsStr);
                });

                break;

            case R.id.completeBtnID:

                bottomActiveAlert.setVisibility(View.VISIBLE);
                bottomDisAlert.setVisibility(View.GONE);
                menuID.setVisibility(View.GONE);
                Intent intentCo = new Intent(LeadHistoryActivity.this, LeadUpdateActivity.class);
                intentCo.putExtra("CUSTOMER_ID", "" + customerID);
                intentCo.putExtra("AUTO_ID", "" + autoID);
                intentCo.putExtra("TITLE", "COMPLETE ACTIVITY");
                intentCo.putExtra("ACTIVITY_NAME", "" + activityName);
                intentCo.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                intentCo.putExtra("LEAD_EMAIL", "" + emailStr);
                startActivity(intentCo);
                break;

            case R.id.searchRLID:
                Intent intentSearch = new Intent(LeadHistoryActivity.this, SearchViewActivity.class);
                intentSearch.putExtra("TYPE", "SEARCH");
                startActivity(intentSearch);
                break;

            case R.id.cancelBtnID:

                bottomActiveAlert.setVisibility(View.VISIBLE);
                bottomDisAlert.setVisibility(View.GONE);
                menuID.setVisibility(View.GONE);
                Intent intentCa = new Intent(LeadHistoryActivity.this, LeadUpdateActivity.class);
                intentCa.putExtra("CUSTOMER_ID", "" + customerID);
                intentCa.putExtra("AUTO_ID", "" + autoID);
                intentCa.putExtra("TITLE", "CANCEL ACTIVITY");
                intentCa.putExtra("ACTIVITY_NAME", "" + activityName);
                intentCa.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                intentCa.putExtra("LEAD_EMAIL", "" + emailStr);
                startActivity(intentCa);
                break;

            case R.id.editRLID:

                bottomActiveAlert.setVisibility(View.VISIBLE);
                bottomDisAlert.setVisibility(View.GONE);
                menuID.setVisibility(View.GONE);
                Intent intentC = new Intent(LeadHistoryActivity.this, LeadEditActivity.class);
                intentC.putExtra("CUSTOMER_ID", "" + customerID);
                startActivity(intentC);
                break;

            case R.id.callBtnRLID:

                /*CommunicationsServices.InsertCommunication(LeadHistoryActivity.this, "1", customerID, userID, "", "");
                Intent intentCall = new Intent(LeadHistoryActivity.this,CallCompleteActivity.class);
                intentCall.putExtra("ACTIVITY_NAME",activityName);
                intentCall.putExtra("ACTIVITY_ID",activityID);
                intentCall.putExtra("CUSTOMER_NAME",nameStr);
                intentCall.putExtra("CUSTOMER_MOBILE",mobileStr);
                intentCall.putExtra("LEAD_ID",customerID);
                startActivity(intentCall);
                finish();

                numberCalling(mobileStr);*/
              //  getCallerID("1","1");
                getCallerID("1","1");

            //   callingAlert();

                break;

            case R.id.mailBtnRLID:
                if(emailStr.isEmpty()){
                    Toast.makeText(this, "No Mail", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intentM = new Intent(LeadHistoryActivity.this, SendTemplatesActivity.class);
                    intentM.putExtra("TYPE", "Send Email");
                    intentM.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                    intentM.putExtra("LEAD_EMAIL", "" + emailStr);
                    intentM.putExtra("LEAD_ID", "" + customerID);
                    intentM.putExtra("CUSTOMER_NAME",nameStr);
                    startActivity(intentM);
                }



                break;

            case R.id.webMailRLID:
                Intent intentWeb = new Intent(LeadHistoryActivity.this, SendTemplatesActivity.class);
                intentWeb.putExtra("TYPE", "Web Email");
                intentWeb.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                intentWeb.putExtra("LEAD_EMAIL", "" + emailStr);
                intentWeb.putExtra("LEAD_ID", "" + customerID);
                intentWeb.putExtra("CUSTOMER_NAME",nameStr);
                startActivity(intentWeb);
                break;

            case R.id.whatsAppBtnRLID:
                Intent intentW = new Intent(LeadHistoryActivity.this, SendTemplatesActivity.class);
                intentW.putExtra("TYPE", "Send Whats App");
                intentW.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                intentW.putExtra("LEAD_EMAIL", "" + emailStr);
                intentW.putExtra("LEAD_ID", "" + customerID);
                intentW.putExtra("CUSTOMER_NAME",nameStr);
                startActivity(intentW);
                break;

            case R.id.messageBtnRLID:
                Intent intentMe = new Intent(LeadHistoryActivity.this, SendTemplatesActivity.class);
                intentMe.putExtra("TYPE", "Send Sms");
                intentMe.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                intentMe.putExtra("LEAD_EMAIL", "" + emailStr);
                intentMe.putExtra("LEAD_ID", "" + customerID);
                intentMe.putExtra("CUSTOMER_NAME",nameStr);
                Log.e("messageBtnRLID", ""+mobileStr+","+emailStr+","+nameStr+","+customerID);

                startActivity(intentMe);
                break;

            case R.id.calenderBtnRLID:
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                startActivity(intent);
                break;

            case R.id.websiteBtnRLID:
                String url = "https://www.tranquilcrm.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.locationBtnRLID:
                openMap();
                break;

            case R.id.googleBtnRLID:
                startGoogleSearch();
                break;

            case R.id.reasonRLID:
             // showActivitiesAlert("REASON", "1");
                showActivitiesAlert();
                break;


            case R.id.shareBtnRLID:
                shareApp();
                break;

            case R.id.linkedInBtnRLID:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://you"));
                final PackageManager packageManager = getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(intent2, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/profile/view?id=you"));
                }
                startActivity(intent2);
                break;
        }
    }

    AppCompatTextView reasonTVID;

    private void showActivitiesAlert() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.search_drop_down_view);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
        dropDownLVID = dialog.findViewById(R.id.dropDownLVID);
        dropDownLVID.setExpanded(true);
        dropDownLVID.setFocusable(false);

        otherFieldLLID = dialog.findViewById(R.id.otherFieldLLID);
        otherFieldETID = dialog.findViewById(R.id.otherFieldETID);

        drPB = dialog.findViewById(R.id.drPB);
        drPB.setVisibility(View.GONE);
        final EditText searchHintETID = dialog.findViewById(R.id.searchHintETID);
        final LinearLayout searchLLID = dialog.findViewById(R.id.searchLLID);
        searchLLID.setFocusable(false);
        Button otherFieldSaveBtn = dialog.findViewById(R.id.otherFieldSaveBtn);

        if (Utilities.isNetworkAvailable(this)) {

          //  activities();
            deadReasons2();

        } else {
            AlertUtilities.bottomDisplayStaticAlert(this, "No Internet connection", "MAke sure your device is connected to internet");
        }

        dropDownLVID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> requirementAdapterView, View view, int i, long l) {
                dialog.dismiss();
              //  reasonsStr = disableResponses.get(i).getDead_reason();
              //  activityTVID.setText(activityMainResponses.get(i).getActivity_name());
                reasonTVID.setText(disableResponses.get(i).getDead_reason());
              /*  try {
                    reasonTVID.setText(disableResponses.get(i).getDead_reason());
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }
        });


        searchHintETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int i, int i1, int i2) {
                //activitiesAdapter.getFilter().filter(arg0);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

              /*  String te = searchHintETID.getText().toString();

                deadReasonNewAdapter.getFilter().filter(te);
                Log.e("tesxt", ""+te);*/
              try {
                    String te = searchHintETID.getText().toString();
                    deadReasonNewAdapter.getFilter().filter(te);
                   Log.e("tesxt", ""+te);
                }catch (Exception e){
                    Log.e("trycatch", ""+e.getMessage());
                }


            }
        });

    }

 /*   @SuppressLint("ClickableViewAccessibility")  private void showActivitiesAlert(final String type, String alertType) {

        dialog = new Dialog(LeadHistoryActivity.this);
        dialog.setContentView(R.layout.search_drop_down_view);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
        dropDownLVID = dialog.findViewById(R.id.dropDownLVID);
        dropDownLVID.setExpanded(true);
        dropDownLVID.setFocusable(false);

        otherFieldLLID = dialog.findViewById(R.id.otherFieldLLID);
        otherFieldETID = dialog.findViewById(R.id.otherFieldETID);

        drPB = dialog.findViewById(R.id.drPB);
        drPB.setVisibility(View.GONE);
        final EditText searchHintETID = dialog.findViewById(R.id.searchHintETID);
        final LinearLayout searchLLID = dialog.findViewById(R.id.searchLLID);
        searchLLID.setFocusable(false);

        Button otherFieldSaveBtn = dialog.findViewById(R.id.otherFieldSaveBtn);


        if (Utilities.isNetworkAvailable(LeadHistoryActivity.this)) {

            if (type.equalsIgnoreCase("ACTIVITIES")) {
               // activities(alertType);
            }
        } else {
            AlertUtilities.bottomDisplayStaticAlert(LeadHistoryActivity.this, "No Internet connection", "MAke sure your device is connected to internet");
        }

        dropDownLVID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> requirementAdapterView, View view, int i, long l) {
                dialog.dismiss();

           *//*     if (type.equalsIgnoreCase("ACTIVITIES")) {

                    if (alertType.equalsIgnoreCase("1")){
                        activityTVID.setText(activityMainResponses1.get(i).getActivity_name());
                    }else{
                        if(alertType.equalsIgnoreCase("2")){
                            activityNameTVID.setText(activityMainResponses1.get(i).getActivity_name());
                        }else{
                            nextactivityTVID.setText(activityMainResponses1.get(i).getActivity_name());
                            nextActivityNameStr = activityMainResponses1.get(i).getActivity_name();
                        }

                    }

                    activityNameStr = activityMainResponses1.get(i).getActivity_name();
                }  *//*


            }
        });


        searchHintETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int i, int i1, int i2) {
                //activitiesAdapter.getFilter().filter(arg0);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

           *//*     if (type.equalsIgnoreCase("ACTIVITIES")) {
                    String te = searchHintETID.getText().toString();
                    activitiesAdapter.getFilter().filter(te);
                }*//*


            }
        });

    }*/



    private void getEmployeeCallCreatives() {

        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<EmployeeCallCreativesResponse> call = apiInterface.getEmployeeCallCreatives(customerID,userID);
        Log.e("api==>",call.request().toString());
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

    private void getCallerID(String type, String communications) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(customerID,"","",communications,userID,false);
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

                                    Intent intentCall = new Intent(LeadHistoryActivity.this,CallCompleteActivity.class);
                                    intentCall.putExtra("ACTIVITY_NAME",activityName);
                                    intentCall.putExtra("ACTIVITY_ID",activityID);
                                    intentCall.putExtra("CUSTOMER_NAME",nameStr);
                                    intentCall.putExtra("CUSTOMER_MOBILE",mobileStr);
                                    intentCall.putExtra("LEAD_ID",customerID);
                                    intentCall.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    startActivity(intentCall);
                                    finish();

                                    numberCalling(mobileStr);
                                    getEmployeeCallCreatives();

                                }else if (type.equalsIgnoreCase("2")){

                                    CustomKnowlarityApis.clickToKnowlarityCall(LeadHistoryActivity.this,userID,mobileStr,kProgressHUD);

                                    Intent intentCall = new Intent(LeadHistoryActivity.this,CallCompleteActivity.class);
                                    intentCall.putExtra("ACTIVITY_NAME",activityName);
                                    intentCall.putExtra("ACTIVITY_ID",activityID);
                                    intentCall.putExtra("CUSTOMER_NAME",nameStr);
                                    intentCall.putExtra("CUSTOMER_MOBILE",mobileStr);
                                    intentCall.putExtra("LEAD_ID",customerID);
                                    intentCall.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    startActivity(intentCall);
                                    getEmployeeCallCreatives();
                                    finish();

                                }




                                Log.e("caller_id","" + statusResponses.get(i).getCall_id());


                                Log.d("INSERT_MESSAGE", "" + type + " : Inserted");

                            } else {

                                Utilities.AlertDaiolog(LeadHistoryActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            }
                        }
                    } else {
                        Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                        Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                    }
                } else {

                    Utilities.AlertDaiolog(LeadHistoryActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                    Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                    Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {
                Utilities.AlertDaiolog(LeadHistoryActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
            }
        });
    }

    private void callingAlert() {

        final Dialog dialog = new Dialog(LeadHistoryActivity.this);
        dialog.setContentView(R.layout.select_calling_alert);
        int height1 = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width1 = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width1, height1);
        dialog.getWindow().setGravity(Gravity.CENTER);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        AppCompatButton normalCallBtn,knowlarityCallBtn;

        normalCallBtn = dialog.findViewById(R.id.normalCallBtn);
        knowlarityCallBtn = dialog.findViewById(R.id.knowlarityCallBtn);

        normalCallBtn.setOnClickListener(v -> {

           /* CommunicationsServices.InsertCommunication(LeadHistoryActivity.this, "1", customerID, userID, "", "");
            Intent intentCall = new Intent(LeadHistoryActivity.this,CallCompleteActivity.class);
            intentCall.putExtra("ACTIVITY_NAME",activityName);
            intentCall.putExtra("ACTIVITY_ID",activityID);
            intentCall.putExtra("CUSTOMER_NAME",nameStr);
            intentCall.putExtra("CUSTOMER_MOBILE",mobileStr);
            intentCall.putExtra("LEAD_ID",customerID);
            startActivity(intentCall);
            numberCalling(mobileStr);
            dialog.dismiss();*/

            getCallerID("1","1");
            getEmployeeCallCreatives();

        });

        knowlarityCallBtn.setOnClickListener(v -> {

            /*CommunicationsServices.InsertCommunication(LeadHistoryActivity.this, "1", customerID, userID, "", "");
            CustomKnowlarityApis.clickToKnowlarityCall(LeadHistoryActivity.this,userID,mobileStr,kProgressHUD);
            Intent intentCall = new Intent(LeadHistoryActivity.this,CallCompleteActivity.class);
            intentCall.putExtra("ACTIVITY_NAME",activityName);
            intentCall.putExtra("ACTIVITY_ID",activityID);
            intentCall.putExtra("CUSTOMER_NAME",nameStr);
            intentCall.putExtra("CUSTOMER_MOBILE",mobileStr);
            intentCall.putExtra("LEAD_ID",customerID);
            startActivity(intentCall);
            dialog.dismiss();*/

            getCallerID("2","5");
            getEmployeeCallCreatives();

        });

    }

    private void deadReasons2(){

        drPB.setVisibility(View.VISIBLE);
        ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DisableResponse>> call = apiInterface.getAllDeadReasons();
        Log.e("deadReasons2=>",call.request().toString());
        call.enqueue(new Callback<ArrayList<DisableResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DisableResponse>> call, Response<ArrayList<DisableResponse>> response) {
                drPB.setVisibility(View.GONE);
                if(response.body()!=null&& response.code()==200){
                    disableResponses = response.body();
                    Log.e("deadReasons2==>",disableResponses.size()+" ");
                   if(disableResponses!=null && disableResponses.size()>0){
                       deadReasonNewAdapter = new DeadReasonNewAdapter(LeadHistoryActivity.this,disableResponses);
                       dropDownLVID.setAdapter(deadReasonNewAdapter);
                       Log.e("deadReasons2=>","set adapter called");
                       dropDownLVID.setAdapter(deadReasonNewAdapter);
                       dropDownLVID.setExpanded(true);
                       dropDownLVID.setFocusable(false);
                   }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DisableResponse>> call, Throwable t) {

            }
        });

    }

    public class DeadReasonNewAdapter extends BaseAdapter implements Filterable{
        private ArrayList<DisableResponse> data;
        private Activity context;
        private LayoutInflater inflater;
        private DeadReasonNewAdapter.ValueFilter valueFilter;
        private ArrayList<DisableResponse> mStringFilterList;
        public DeadReasonNewAdapter(Activity context, ArrayList<DisableResponse> data) {
            super();
            this.context = context;
            mStringFilterList = data;
            this.data = data;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
            Log.e("mStringFilterList==>",""+data.size());
            Log.e("mStringFilterList==>",""+new Gson().toJson(data));
        }


        @Override
        public int getCount() {
            Log.e("datasize", ""+data.size());
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position).getDead_reason();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            TextView tname;
            RelativeLayout spinnerBgColor;
            LinearLayout dropDownListItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};

            DeadReasonNewAdapter.ViewHolder holder;
            holder = new DeadReasonNewAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);
            holder.tname = convertView.findViewById(R.id.spinnerText);

            holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
            convertView.setTag(holder);

            String reason=  data.get(position).getDead_reason();
            holder.tname.setText(reason);

            holder = (DeadReasonNewAdapter.ViewHolder) convertView.getTag();
            Log.e("getView==>",reason+" , "+position);
            holder.tname.setText(data.get(position).getDead_reason());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
         /*       GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
               bgShape.setColor(Color.parseColor(mColors[position % 40]));*/
            holder.tname.setText(reason);
            reasonTVID.setText(data.get(position).getDead_reason());
            reasonsStr= data.get(position).getReason_id();
            holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reasonsStr= data.get(position).getReason_id();
                    dialog.dismiss();
                    // reasonsStr=disableResponses.get(position).getReason_id();
                    reasonTVID.setText(data.get(position).getDead_reason());
                    Log.e("data==>", data.get(position).getDead_reason());

                }
            });

           /* Log.e("getView==>",reason+" , "+position);
            if (convertView == null) {
                Log.e("getView==>",reason+" , null "+position);

                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);


            }else{



            }*/



            return convertView;
        }

        @Override
        public Filter getFilter() {
            valueFilter = new DeadReasonNewAdapter.ValueFilter();
            return valueFilter;
        }

        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<DisableResponse> filterList = new ArrayList<DisableResponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getDead_reason().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            DisableResponse contacts = new DisableResponse();
                            contacts.setDead_reason(mStringFilterList.get(i).getDead_reason());
                            contacts.setReason_id(mStringFilterList.get(i).getReason_id());
                            filterList.add(contacts);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;
            }


            //Invoked in the UI thread to publish the filtering results in the user interface.
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (ArrayList<DisableResponse>) results.values;

                notifyDataSetChanged();
                 Log.e("data","data "+data.size());
            }
        }
    }


    /*public class DeadReasonNewAdapter extends BaseAdapter implements Filterable{
        private ArrayList<DisableResponse> data;
        private Activity context;
        private LayoutInflater inflater;
        private DeadReasonNewAdapter.ValueFilter valueFilter;
        private ArrayList<DisableResponse> mStringFilterList;

        public DeadReasonNewAdapter(Activity context, ArrayList<DisableResponse> data) {
            super();
            this.context = context;
            mStringFilterList = data;
            this.data = data;

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
            Log.e("mStringFilterList==>",""+data.size());
            Log.e("mStringFilterList==>",""+new Gson().toJson(data));
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position).getDead_reason();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder {
            TextView tname;
            RelativeLayout spinnerBgColor;
            LinearLayout dropDownListItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};

            DeadReasonNewAdapter.ViewHolder holder;

            holder = new DeadReasonNewAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);
            holder.tname = convertView.findViewById(R.id.spinnerText);

            holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
            convertView.setTag(holder);


            String reason=  data.get(position).getDead_reason();
            holder.tname.setText(reason);

            holder = (DeadReasonNewAdapter.ViewHolder) convertView.getTag();
            Log.e("getView==>",reason+" , "+position);
            holder.tname.setText(data.get(position).getDead_reason());

            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            holder.tname.setText(reason);

            reasonTVID.setText(data.get(position).getDead_reason());
            reasonsStr= data.get(position).getReason_id();

            holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reasonsStr= data.get(position).getReason_id();
                    dialog.dismiss();
                    // reasonsStr=disableResponses.get(position).getReason_id();
                    reasonTVID.setText(data.get(position).getDead_reason());
                    Log.e("data==>", data.get(position).getDead_reason());

                }
            });
            return convertView;



          // String reason=  data.get(position).getDead_reason();
         *//*   Log.e("getView==>",data.get(position).getDead_reason()+" , "+position);
            if (convertView == null) {
                Log.e("getView==>",data.get(position).getDead_reason()+" , null "+position);
                holder = new DeadReasonNewAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.tname.setText(data.get(position).getDead_reason());
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            }else{
                holder = (DeadReasonNewAdapter.ViewHolder) convertView.getTag();
                Log.e("getView==>",data.get(position).getDead_reason()+" , "+position);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
                GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
                bgShape.setColor(Color.parseColor(mColors[position % 40]));
                holder.tname.setText(data.get(position).getDead_reason());
                holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                      reasonsStr=disableResponses.get(position).getReason_id();
                        reasonTVID.setText(data.get(position).getDead_reason());
                        Log.e("data==>", data.get(position).getDead_reason());

                    }
                });

            }



            return convertView;*//*
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new DeadReasonNewAdapter.ValueFilter();
            }

            return valueFilter;
        }
        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<DisableResponse> filterList = new ArrayList<DisableResponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getDead_reason().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            DisableResponse contacts = new DisableResponse();
                            contacts.setDead_reason(mStringFilterList.get(i).getDead_reason());
                            contacts.setReason_id(mStringFilterList.get(i).getReason_id());
                            filterList.add(contacts);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;
            }


            //Invoked in the UI thread to publish the filtering results in the user interface.
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                try {
                    data = (ArrayList<DisableResponse>) results.values;
                    notifyDataSetChanged();
                }catch (Exception e){
                    Log.e("tycatch", ""+e);
                }

            }
        }
    }*/


    private void deadReasons(Spinner reasonsSPID) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DisableResponse>> call = apiInterface.getAllDeadReasons();
        Log.e("api=spinner1=>",call.request().toString());
        call.enqueue(new Callback<ArrayList<DisableResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DisableResponse>> call, Response<ArrayList<DisableResponse>> response) {

                if (response.body() != null && response.code() == 200){
                    disableResponses = response.body();
                    deadReasonsAdapter = new DeadReasonsAdapter(LeadHistoryActivity.this,R.layout.custom_spinner_view,disableResponses);
                    reasonsSPID.setAdapter(deadReasonsAdapter);
                }else {
                    Toast.makeText(LeadHistoryActivity.this, "Reasons Not Loading", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DisableResponse>> call, Throwable t) {
                Toast.makeText(LeadHistoryActivity.this, "Reasons Not Loading", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void validations(Dialog disableDialog, EditText noteETID, EditText otherReasonETID,String reasonStr) {
        
        String notesStr = noteETID.getText().toString();
        String otherNotesStr = otherReasonETID.getText().toString();
        
        if (Utilities.isNetworkAvailable(this)){
            deadLead(disableDialog,notesStr,otherNotesStr,reasonStr);
        }else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
        //dialog.dismiss();
        
    }

    private void deadLead(Dialog disableDialog, String notesStr, String otherNotesStr, String reasonsStr) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.getDeadLead(customerID,userID,reasonsStr,otherNotesStr,notesStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.body() != null && response.code() == 200){
                    statusResponse = response.body();

                    if (statusResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(LeadHistoryActivity.this, "Lead Successfully Disable", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(LeadHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(LeadHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        disableDialog.dismiss();

    }

    private void validation(Dialog dialog, EditText saleETID) {
        String saleStr;

        saleStr = saleETID.getText().toString();

        if (TextUtils.isEmpty(saleStr)){
            Toast.makeText(this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utilities.isNetworkAvailable(this)){
            updateSale(dialog,saleStr);
        }else {
            Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSale(Dialog dialog, String saleStr) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.updateSale(customerID,userID,companyID,saleStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if (response.body() != null && response.code() == 200){
                    statusResponse = response.body();
                    if (statusResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(LeadHistoryActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LeadHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(LeadHistoryActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });


       dialog.dismiss();


    }

    private void activities() {
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<ActivityMainResponse>> call = apiInterface.getActivities(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ActivityMainResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivityMainResponse>> call, Response<ArrayList<ActivityMainResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    activityMainResponses = response.body();
                    if (activityMainResponses != null && activityMainResponses.size() > 0) {
                        for (int i = 0; i < activityMainResponses.size(); i++) {
                            aAdapter = new ActivitiesAdapter(LeadHistoryActivity.this, R.layout.custom_spinner_view, activityMainResponses);
                            nextActivity.setAdapter(aAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ActivityMainResponse>> call, Throwable t) {
                Toast.makeText(LeadHistoryActivity.this, "Activities not loading", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class MenuItems {

        private String title;
        private String id;
        private int icon;

        public MenuItems(String title, String id, int icon) {
            this.title = title;
            this.id = id;
            this.icon = icon;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }

    public class MenuAdapter extends BaseAdapter {
        ArrayList<MenuItems> menuItems = new ArrayList<>();
        LayoutInflater inflater;
        Activity activity;

        public MenuAdapter(Activity activity, ArrayList<MenuItems> menuItems) {
            this.activity = activity;
            this.menuItems = menuItems;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return menuItems.size();
        }

        @Override
        public Object getItem(int i) {
            return menuItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.lead_history_menu_list_items, null);

            ImageView imageID = view.findViewById(R.id.imageID);
            TextView titleID = view.findViewById(R.id.titleID);

            titleID.setText(menuItems.get(i).getTitle());
            Picasso.with(activity).load(menuItems.get(i).getIcon()).into(imageID);
            //imageID.setBackgroundTintList(activity.getResources().getColorStateList(R.color.white));
            imageID.setColorFilter(getResources().getColor(R.color.white));
            //imageID.setBackgroundColor(activity.getColor(R.color.white));

            return view;
        }
    }

    private void numberCalling(String number) {
    /*    try {
            if (ActivityCompat.checkSelfPermission(LeadHistoryActivity.this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (LeadHistoryActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LeadHistoryActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        try {
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
        }
    }

    @SuppressLint("NewApi")
    private void openMap() {
        try {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + 17.6849094 + "," + 78.5793022);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        } catch (ActivityNotFoundException innerEx) {
            Toast.makeText(LeadHistoryActivity.this, "Install Google Maps application", Toast.LENGTH_LONG).show();
        }
    }

    private void startGoogleSearch() {
        showProgressDialog();
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        //intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.SearchActivity");
        intent.putExtra(SearchManager.QUERY, nameStr + " "+ mobileStr + " "+ companyNameStr);
        startActivity(intent);
        //dismissProgressDialog();
    }

    private void shareApp() {

        String shareBody = "App Link :  " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    private void showProgressDialog() {
        if (kProgressHUD == null) {
            kProgressHUD = KProgressHUD.create(LeadHistoryActivity.this)
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
        if (kProgressHUD != null && kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void loadLeadProfileDetails(String customerID) {
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<LeadEditActivity.LeadProfileResponse>> call = apiInterface.getEditLeadProfileDetails(customerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<LeadEditActivity.LeadProfileResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LeadEditActivity.LeadProfileResponse>> call, Response<ArrayList<LeadEditActivity.LeadProfileResponse>> response) {

                if (response.body() != null && response.code() == 200) {
                    profileResponses = response.body();
                    if (profileResponses != null && profileResponses.size() > 0) {
                        ArrayList<LeadProfileFragment.UserRequirements> selectedRequirements = profileResponses.get(0).getLead_requirement();
                        MySharedPreferences.setPreference(LeadHistoryActivity.this, "" + AppConstants.LEAD_ID, "" + customerID);


                        for (int i = 0; i < profileResponses.size(); i++) {
                            //companyTVID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_company_name()));
                            //customerNaTVID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_name()));
                            emailStr = profileResponses.get(i).getLead_email();
                            String pic = profileResponses.get(i).getLead_pic();
                            mobileStr  = profileResponses.get(i).getLead_mobile();
                            companyNameStr  = profileResponses.get(i).getLead_company_name();
                            toolBarNameTVID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_name()));
                            toolBarMobileTVID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_company_name()));




                            if (pic.equalsIgnoreCase(AppConstants.GLOBAL_MAIN_URL_FOR_ICONS + "lead_pics/")) {
                               // Picasso.with(LeadHistoryActivity.this).load(R.drawable.pic_d).into(imageDrawable);
                                Picasso.with(LeadHistoryActivity.this).load(R.drawable.pic_d).placeholder(R.drawable.user).into(toolBarPicID);
                            } else {
                                //Picasso.with(LeadHistoryActivity.this).load(pic).into(imageDrawable);
                                Picasso.with(LeadHistoryActivity.this).load(pic).placeholder(R.drawable.user).into(toolBarPicID);
                            }
                        }

                    } else {
                        Log.d("ERROR", "DETAILS_NOT_COMING");
                    }
                } else {
                    Log.d("ERROR", "DETAILS_NOT_COMING");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<LeadEditActivity.LeadProfileResponse>> call, Throwable t) {
                Log.d("ERROR", "DETAILS_NOT_COMING");
            }
        });
    }

    private void loadPresentActivityDetails(String customerID) {
        detailsPB.setVisibility(View.VISIBLE);
        scheduledActivityNameID.setVisibility(View.GONE);
        scheduledActivityDateID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<PresentActivityResponse>> call = apiInterface.getPresentActivityResponse(customerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<PresentActivityResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<PresentActivityResponse>> call, Response<ArrayList<PresentActivityResponse>> response) {

                detailsPB.setVisibility(View.GONE);
                scheduledActivityNameID.setVisibility(View.VISIBLE);
                scheduledActivityDateID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    ArrayList<PresentActivityResponse> profileResponses = response.body();
                    if (profileResponses != null && profileResponses.size() > 0) {
                        for (int i = 0; i < profileResponses.size(); i++) {
                            autoID = profileResponses.get(i).getAct_no();
                            scheduledActivityNameID.setText(profileResponses.get(i).getActivity_name());
                            scheduledActivityDateID.setText(profileResponses.get(i).getActivity_date());

                            activityName = profileResponses.get(i).getActivity_name();
                            activityID = profileResponses.get(i).getActivity_id();
                            nextActIDStr = profileResponses.get(i).getActivity_id();
                            dateStr = profileResponses.get(i).getActivity_date();
                        }
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(LeadHistoryActivity.this, "Oops....!", "Details not loading");
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadHistoryActivity.this, "Oops....!", "Details not loading");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<PresentActivityResponse>> call, Throwable t) {
                detailsPB.setVisibility(View.GONE);
                scheduledActivityNameID.setVisibility(View.GONE);
                scheduledActivityDateID.setVisibility(View.GONE);
                AlertUtilities.bottomDisplayStaticAlert(LeadHistoryActivity.this, "Error at server....!", "Details not loading");
            }
        });
    }

    private void completeResponse(String customerID, String companyID, String autoID) {
        //showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllStatusResponse>> call = apiInterface.getUpdateCompleteResponse(customerID, autoID, "", nextActIDStr, dateStr, companyID, userID, "test", "");
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllStatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllStatusResponse>> call, Response<ArrayList<AllStatusResponse>> response) {
                //dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<AllStatusResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus() == 1) {

                               /* okRLID.setVisibility(View.GONE);
                                okDisRLID.setVisibility(View.VISIBLE);
                                alertDisplayForTemplates("COMPLETE");*/
                                Toast.makeText(LeadHistoryActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            } else {
                              /*  okRLID.setVisibility(View.VISIBLE);
                                okDisRLID.setVisibility(View.GONE);*/
                                Toast.makeText(LeadHistoryActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        /*okRLID.setVisibility(View.VISIBLE);
                        okDisRLID.setVisibility(View.GONE);*/
                        Toast.makeText(LeadHistoryActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    /*okRLID.setVisibility(View.VISIBLE);
                    okDisRLID.setVisibility(View.GONE);*/
                    Toast.makeText(LeadHistoryActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllStatusResponse>> call, Throwable t) {

                /*okRLID.setVisibility(View.VISIBLE);
                okDisRLID.setVisibility(View.GONE);
                dismissProgressDialog();*/
                Toast.makeText(LeadHistoryActivity.this, "Not updated", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public class PresentActivityResponse {

        private String act_no;
        private String activity_id;
        private String activity_name;
        private String activity_date;

        public PresentActivityResponse(String act_no, String activity_id, String activity_name, String activity_date) {
            this.act_no = act_no;
            this.activity_id = activity_id;
            this.activity_name = activity_name;
            this.activity_date = activity_date;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getAct_no() {
            return act_no;
        }

        public void setAct_no(String act_no) {
            this.act_no = act_no;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getActivity_date() {
            return activity_date;
        }

        public void setActivity_date(String activity_date) {
            this.activity_date = activity_date;
        }
    }

    /*private void getTagsData() {
        tagsRCID.setVisibility(View.GONE);
        tagsPB.setVisibility(View.VISIBLE);
        noTagsTVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<TagsDisplayResponse>> call = apiInterface.getLeadTags(customerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<TagsDisplayResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<TagsDisplayResponse>> call, Response<ArrayList<TagsDisplayResponse>> response) {
                tagsPB.setVisibility(View.GONE);
                tagsRCID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    tagsResponse = response.body();
                    if (tagsResponse.size() > 0) {
                        tagsAdapter = new TagsDisplayAdapter(LeadHistoryActivity.this, tagsResponse);
                        tagsRCID.setAdapter(tagsAdapter);
                        tagsAdapter.setTagsList(tagsResponse);
                    } else {
                        tagsRCID.setVisibility(View.GONE);
                        noTagsTVID.setVisibility(View.VISIBLE);
                    }
                } else {
                    tagsRCID.setVisibility(View.GONE);
                    noTagsTVID.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<TagsDisplayResponse>> call, Throwable t) {
                tagsPB.setVisibility(View.GONE);
                tagsRCID.setVisibility(View.GONE);
                noTagsTVID.setVisibility(View.VISIBLE);
            }
        });
    }*/

    public class TagsDisplayResponse implements Serializable {
        private String lead_tag_id;
        private String tag_title;
        private String tag_color;

        public TagsDisplayResponse(String lead_tag_id, String tag_title, String tag_color) {
            this.lead_tag_id = lead_tag_id;
            this.tag_title = tag_title;
            this.tag_color = tag_color;
        }

        public String getLead_tag_id() {
            return lead_tag_id;
        }

        public void setLead_tag_id(String lead_tag_id) {
            this.lead_tag_id = lead_tag_id;
        }

        public String getTag_title() {
            return tag_title;
        }

        public void setTag_title(String tag_title) {
            this.tag_title = tag_title;
        }

        public String getTag_color() {
            return tag_color;
        }

        public void setTag_color(String tag_color) {
            this.tag_color = tag_color;
        }

    }

    public class TagsDisplayAdapter extends RecyclerView.Adapter<TagsDisplayAdapter.SearchTagsViewHolder> {
        private Context context;
        private ArrayList<TagsDisplayResponse> tagsMainList = new ArrayList<>();
        int row_index;
        int num = 1;

        public TagsDisplayAdapter(Context context, ArrayList<TagsDisplayResponse> tagsMainList) {
            this.context = context;
            this.tagsMainList = tagsMainList;
        }

        @NonNull
        @Override
        public SearchTagsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View cardView = inflater.inflate(R.layout.lead_tags_main_list_item, null, false);
            SearchTagsViewHolder viewHolder = new SearchTagsViewHolder(cardView);

            return viewHolder;
        }

        public void setTagsList(ArrayList<TagsDisplayResponse> tagsList) {
            this.tagsMainList = tagsList;
            notifyItemRangeChanged(0, tagsList.size());
        }

        @Override
        public void onBindViewHolder(@NonNull SearchTagsViewHolder searchTagsViewHolder, final int i) {

            TextView activeTagTVID = searchTagsViewHolder.activeTagTVID;
            final RelativeLayout activeTagRLID = searchTagsViewHolder.activeTagRLID;
            activeTagTVID.setText(tagsMainList.get(i).getTag_title());

            String colorCode = tagsMainList.get(i).getTag_color();
            if (colorCode.startsWith("#")) {
                GradientDrawable bgShape = (GradientDrawable) searchTagsViewHolder.activeTagRLID.getBackground();
                bgShape.setColor(Color.parseColor(tagsMainList.get(i).getTag_color()));
            } else {
                searchTagsViewHolder.activeTagRLID.setBackgroundResource(R.drawable.bg_active_label1);
            }

            activeTagRLID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentT = new Intent(LeadHistoryActivity.this, TagDetailsActivity.class);
                    intentT.putExtra("CUSTOMER_NAME", "" + nameStr);
                    intentT.putExtra("LEAD_ID", "" + customerID);
                    startActivity(intentT);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tagsMainList.size();

        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class SearchTagsViewHolder extends RecyclerView.ViewHolder {

            TextView activeTagTVID;

            RelativeLayout activeTagRLID;

            public SearchTagsViewHolder(@NonNull View itemView) {
                super(itemView);
                activeTagTVID = itemView.findViewById(R.id.activeTagTVID);
                activeTagRLID = itemView.findViewById(R.id.activeTagRLID);
            }
        }
    }




}
