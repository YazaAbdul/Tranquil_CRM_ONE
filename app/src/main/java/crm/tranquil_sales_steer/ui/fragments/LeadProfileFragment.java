package crm.tranquil_sales_steer.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.BuildConfig;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CommunicationsServices;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.GPSTracker;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.CallCompleteActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.LeadEditActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.LeadHistoryActivity;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import crm.tranquil_sales_steer.ui.adapters.NotesAdapter;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.AllStatusResoponse;
import crm.tranquil_sales_steer.ui.models.EmployeeResponse;
import crm.tranquil_sales_steer.ui.models.GetCompanyUserdlt;
import crm.tranquil_sales_steer.ui.models.NotesResponse;
import crm.tranquil_sales_steer.ui.models.RequirementResponse;
import crm.tranquil_sales_steer.ui.models.SourceResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * Created by venkei on 22-Jun-19.
 */
public class LeadProfileFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    TextView noDataID;
    ProgressBar pb;
    LinearLayout completeViewID;
    TextView customerMobileID, customerEmailID,leadstatusTVID, customerRequirementID, customerSourceID, customerWebSiteID,
            customerLocationID, customerGstID,timeTVID,completeTimeTVID,alternateTVID,notesTVID,nextTimeTVID;
    RelativeLayout sendMessageRLID, sendLocationRLID, shareContactRLID, disableRLID, requirementRLID;
    String nameStr, emailStr, mobileStr, companyStr, userNameStr, userID, companyID,nextTimestr,formattedTime;
    RelativeLayout mobileRLID, emailRLID, websiteRLID, locationRLID, gstRLID;
    private KProgressHUD hud;
    ArrayList<ReasonsResponse> activityMainResponses = new ArrayList<>();
    ReasonsAdapter aAdapter;
    Spinner reasonsSPID;
    String id;
    Date date;
    Button requirementBtn,sourceBtn,employeeBtn,activityBtn,submitActivityBtn;
    RelativeLayout dateRLID,timeRLID,submitRLID,alterMobileRLID;
    ArrayList<GetCompanyUserdlt> getCompanyUserdlts = new ArrayList<>();

    private static final String ARG_KEY_LEAD_ID = "lead_id";
    String VALUE_LEAD_ID;
    Location location = null;
    GPSTracker mGPS = null;
    Double latitudeS, langitudeS;
    String latitudeStr, langitudeStr;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    Dialog dialog;
    ExpandableHeightGridView dropDownGVID;
    ExpandableHeightGridView dropDownLVID;
    //LeadCreateActivity.RequirementNewAdapter requirementAdapter;
    RequirementAdapter requirementAdapter;
    ArrayList<RequirementResponse> requirementResponses = new ArrayList<>();
    ArrayList<ActivityMainResponse> activityMainResponses1 = new ArrayList<>();
    ActivitiesNewAdapter activitiesAdapter;

    ArrayList<SourceResponse> sourceResponses = new ArrayList<>();
    SourceNewAdapter sAdapter;

    EmployeeAdapter employeeAdapter;

    AllstatusAdapter allstatusAdapter;


    ArrayList<EmployeeResponse> employeeResponses = new ArrayList<>();
    ArrayList<AllStatusResoponse> allStatusResoponses = new ArrayList<>();

    ProgressBar rePB;
    String requirementOtherStr,selectedRequirementArrays, selectedRequirementNameArrays,activityOtherStr,sourceOtherStr,activityNameStr,requirementNameStr,nextActivityNameStr,requirementNameStrid,next_req,lead_status;
    boolean otherReqIsVisible = false;
    EditText otherReqETID;
    TextView dateTVID,activityTVID,employeeTVID,cityTVID;
    ProgressBar  drPB;
    RelativeLayout otherReqID;
    LinearLayout otherFieldLLID;
    EditText otherFieldETID;
    boolean otherSourceIsVisible = false;
    boolean otherActivityIsVisible = false;

    String sourceStr, activityStr,dateStr,requirementStr,createdId,employeeStr,activityName,leadID,NextActivitydateStr,leadstatusStr,nextrequrimentStr;

    Calendar calendar;
    private int year, month, day;
    private int mYear, mMonth, mHour, mMinute, mDay, finalHour;
    private String mTime;
    ArrayList<StatusResponse> statusResponse = new ArrayList<>();

    EditText noteETID;
    String notesStr;
    RelativeLayout notesRLID,newPhoneRL;
    ImageView notesIVID;

    NotesAdapter notesAdapter;
    ArrayList<NotesResponse> notesResponses = new ArrayList<>();
    RecyclerView notesRVID;
    Spinner activitySPID;
    TextView activityNameTVID,requrimentNameTVID,nextactivityTVID,nextrequrimentNameTVID;

    LinearLayout micLID;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int REQ_CODE_SPEECH_INPUT_NOTE = 101;
    EditText messageETID;

    TimePickerDialog startTimePicker;
    DateFormat dateFormat;
    RelativeLayout speakRLID;
    String alternateNumberStr;

    private FirebaseAnalytics analytics;

    public LeadProfileFragment newInstance(String customerId) {

        LeadProfileFragment subFrag = new LeadProfileFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY_LEAD_ID, customerId);
        subFrag.setArguments(b);
        return subFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VALUE_LEAD_ID = getArguments().getString(ARG_KEY_LEAD_ID);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lead_profile, container, false);

        analytics = FirebaseAnalytics.getInstance(getActivity());
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        companyID = MySharedPreferences.getPreferences( getActivity(), "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        userID = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_ID);
        userNameStr = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_NAME);

        noDataID = view.findViewById(R.id.noDataID);
        noDataID.setVisibility(View.GONE);
        pb = view.findViewById(R.id.pb);
        pb.setVisibility(View.GONE);
        completeViewID = view.findViewById(R.id.completeViewID);
        customerMobileID = view.findViewById(R.id.customerMobileID);
        customerEmailID = view.findViewById(R.id.customerEmailID);
        customerRequirementID = view.findViewById(R.id.customerRequirementID);
        customerSourceID = view.findViewById(R.id.customerSourceID);
        customerWebSiteID = view.findViewById(R.id.customerWebSiteID);
        customerLocationID = view.findViewById(R.id.customerLocationID);
        customerGstID = view.findViewById(R.id.customerGstID);
        timeTVID = view.findViewById(R.id.timeTVID);
//        nextTimeTVID = view.findViewById(R.id.nextTimeTVID);
        sendMessageRLID = view.findViewById(R.id.sendMessageRLID);
        sendLocationRLID = view.findViewById(R.id.sendLocationRLID);
        shareContactRLID = view.findViewById(R.id.shareContactRLID);
        disableRLID = view.findViewById(R.id.disableRLID);
        requirementBtn = view.findViewById(R.id.requirementBtn);
        sourceBtn = view.findViewById(R.id.sourceBtn);
        employeeBtn = view.findViewById(R.id.employeeBtn);
        activityBtn = view.findViewById(R.id.activityBtn);
        dateRLID = view.findViewById(R.id.dateRLID);
        timeRLID = view.findViewById(R.id.timeRLID);
        dateTVID = view.findViewById(R.id.dateTVID);
        activityTVID = view.findViewById(R.id.activityTVID);
        employeeTVID = view.findViewById(R.id.employeeTVID);
        cityTVID = view.findViewById(R.id.cityTVID);
        submitActivityBtn = view.findViewById(R.id.submitActivityBtn);
        submitRLID = view.findViewById(R.id.submitRLID);
        speakRLID = view.findViewById(R.id.speakRLID);
        alternateTVID = view.findViewById(R.id.alternateTVID);
        notesTVID = view.findViewById(R.id.notesTVID);
        alterMobileRLID = view.findViewById(R.id.alterMobileRLID);

        timeRLID.setOnClickListener(this);
        noteETID = view.findViewById(R.id.noteETID);
        notesRLID = view.findViewById(R.id.notesRLID);
        notesIVID = view.findViewById(R.id.notesIVID);
        notesRVID = view.findViewById(R.id.notesRVID);
        newPhoneRL = view.findViewById(R.id.newPhoneRL);
        leadstatusTVID = view.findViewById(R.id.leadstatusTVID);

        Log.d("CUSTOMER_ID-->", "" + VALUE_LEAD_ID);

        if (Utilities.isNetworkAvailable(getActivity())) {
            loadLeadProfileDetails(VALUE_LEAD_ID);
        } else {
            AlertUtilities.bottomDisplayStaticAlert(getActivity(), "No Internet connection...!", "Make sure your device is connected to internet");
        }

        sendMessageRLID.setOnClickListener(this);
        sendLocationRLID.setOnClickListener(this);
        shareContactRLID.setOnClickListener(this);
        disableRLID.setOnClickListener(this);
        newPhoneRL.setOnClickListener(this);

        mobileRLID = view.findViewById(R.id.mobileRLID);
        emailRLID = view.findViewById(R.id.emailRLID);
        websiteRLID = view.findViewById(R.id.websiteRLID);
        locationRLID = view.findViewById(R.id.locationRLID);
        gstRLID = view.findViewById(R.id.gstRLID);
        requirementRLID = view.findViewById(R.id.requirementRLID);

        mobileRLID.setOnClickListener(this);
        emailRLID.setOnClickListener(this);
        websiteRLID.setOnClickListener(this);
        locationRLID.setOnClickListener(this);
        gstRLID.setOnClickListener(this);
        requirementRLID.setOnClickListener(this);
        requirementBtn.setOnClickListener(this);
        sourceBtn.setOnClickListener(this);
        employeeBtn.setOnClickListener(this);
        activityBtn.setOnClickListener(this);
        dateRLID.setOnClickListener(this);

        //submitActivityBtn.setOnClickListener(this);

        submitRLID.setOnClickListener(v -> {
            showCompleteActivityAlert();
        });


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        notesIVID.setOnClickListener(v -> {

            validation();
            getNotesShow();
            noteETID.setText("");
        });

        getNotesShow();
        getcompanyuserdlt();

        speakRLID.setOnClickListener(v -> {
            startVoiceInput(REQ_CODE_SPEECH_INPUT_NOTE);
        });

        return view;
    }


    private void getNotesShow() {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<NotesResponse>> call = apiInterface.getNotesShow(VALUE_LEAD_ID,userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NotesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NotesResponse>> call, Response<ArrayList<NotesResponse>> response) {
                if (response.body() != null && response.code() == 200){
                    notesResponses = response.body();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    notesAdapter = new NotesAdapter(notesResponses,getActivity());
                    notesRVID.setLayoutManager(layoutManager);
                    notesRVID.setAdapter(notesAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotesResponse>> call, Throwable t) {

            }
        });
    }

    private void validation() {

        notesStr = noteETID.getText().toString();

        if (TextUtils.isEmpty(notesStr)){
            Toast.makeText(getActivity(), "Please enter about customer", Toast.LENGTH_SHORT).show();
            return;
        }else {

            if (Utilities.isNetworkAvailable(getActivity())){

                notesResponse(notesStr);
            }else {
                Toast.makeText(getActivity(), "Please check your network", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void getcompanyuserdlt(){
        ApiInterface apiInterface=ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<GetCompanyUserdlt>> call=apiInterface.getCompanyUserdlt();
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetCompanyUserdlt>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCompanyUserdlt>> call, Response<ArrayList<GetCompanyUserdlt>> response) {
                if(response.body()!=null&& response.code()==200){
                    getCompanyUserdlts=response.body();
                    if(getCompanyUserdlts.size()>0){
                        for(int i=0;i<getCompanyUserdlts.size();i++){
                            if(getCompanyUserdlts.get(i).getProduct_id().equals("1")){
                             ///   getcompanydlts=getCompanyUserdlts.get(i).getProduct_id();
                                //   Toast.makeText(DashBoardActivity.this, "1  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                            //    Toast.makeText(getActivity(), "  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                                speakRLID.setVisibility(View.VISIBLE);



                            }else if(getCompanyUserdlts.get(i).getProduct_id().equals("2")){
                               // Toast.makeText(getActivity(), "2  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                                speakRLID.setVisibility(View.VISIBLE);
                            }else{
                              //  Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                                speakRLID.setVisibility(View.GONE);

                            }

                        }


                    }
                }



                //    Toast.makeText(DashBoardActivity.this, "onsucccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<GetCompanyUserdlt>> call, Throwable t) {
                Toast.makeText(getActivity(), "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void notesResponse(String notesStr) {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.getNotes(notesStr,VALUE_LEAD_ID,userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if (response.body() != null && response.code() == 200){

                    StatusResponse statusResponse;
                    statusResponse = response.body();

                    if (statusResponse.getStatus().equalsIgnoreCase("1")){

                    }else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadLeadProfileDetails(String customerID) {
        pb.setVisibility(View.VISIBLE);
        completeViewID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<LeadProfileResponse>> call = apiInterface.getLeadProfileDetails(customerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<LeadProfileResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LeadProfileResponse>> call, Response<ArrayList<LeadProfileResponse>> response) {
                pb.setVisibility(View.GONE);
                completeViewID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200) {
                    ArrayList<LeadProfileResponse> profileResponses = response.body();
                    if (profileResponses != null && profileResponses.size() > 0) {

                       // ArrayList<UserRequirements> userRequirements = profileResponses.get(0).getLead_requirement();

                       /* try {
                            for (int j = 0; j < userRequirements.size(); j++) {
                                callAllRequirements(userRequirements);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        for (int i = 0; i < profileResponses.size(); i++) {

                            ArrayList<UserRequirements> userRequirements = profileResponses.get(i).getLead_requirement();

                            try {
                                for (int j = 0; j < userRequirements.size(); j++) {
                                    callAllRequirements(userRequirements);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            customerMobileID.setText(profileResponses.get(i).getLead_mobile());
                            if (profileResponses.get(i).getAlternate_number().isEmpty()) {

                                alterMobileRLID.setVisibility(View.GONE);

                            } else {

                                alternateTVID.setText(profileResponses.get(i).getAlternate_number());
                                alterMobileRLID.setVisibility(View.VISIBLE);

                            }

                            alternateNumberStr = profileResponses.get(i).getAlternate_number();



                            customerEmailID.setText(profileResponses.get(i).getLead_email());
                            notesTVID.setText(profileResponses.get(i).getNote());
                            customerRequirementID.setText(profileResponses.get(i).getRequirement_name());
                            employeeTVID.setText(profileResponses.get(i).getCreated_name());
                            cityTVID.setText(profileResponses.get(i).getCity());
                            activityTVID.setText(profileResponses.get(i).getActivity_name());
                            dateTVID.setText(profileResponses.get(i).getActivity_date());
                            timeTVID.setText(profileResponses.get(i).getActivity_time());

                            activityNameStr = profileResponses.get(i).getActivity_name();
                            nextActivityNameStr=profileResponses.get(i).getActivity_name();
                            requirementNameStr =profileResponses.get(i).getRequirement_name();
                            requirementNameStrid =profileResponses.get(i).getRequirement_id();


                            customerSourceID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_source()));
                           // customerWebSiteID.setText(profileResponses.get(i).getLead_website());
                            // customerLocationID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_location()));
                           // customerGstID.setText(profileResponses.get(i).getLead_gstnumber());
                            nameStr = profileResponses.get(i).getLead_name();
                            emailStr = profileResponses.get(i).getLead_email();
                            mobileStr = profileResponses.get(i).getLead_mobile();
                            companyStr = profileResponses.get(i).getLead_company_name();

                            sourceStr = profileResponses.get(i).getSource_id();
                            activityStr = profileResponses.get(i).getActivity_id();
                            createdId = profileResponses.get(i).getCreated_id();
                            dateStr = profileResponses.get(i).getActivity_date();
                            requirementStr = profileResponses.get(i).getRequirement_id();
                            mTime = profileResponses.get(i).getActivity_time();
                            activityName = profileResponses.get(i).getActivity_name();
                            leadID = profileResponses.get(i).getLead_id();

                        }


                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(getActivity(), "Oops....!", "Details not loading");
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(getActivity(), "Oops....!", "Details not loading");
                }

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<LeadProfileResponse>> call, Throwable t) {
                completeViewID.setVisibility(View.GONE);
                noDataID.setVisibility(View.VISIBLE);
                noDataID.setText("No Data Available");
                pb.setVisibility(View.GONE);
                //AlertUtilities.bottomDisplayStaticAlert(getActivity(), "Error at server....!", "Details not loading");
            }
        });
    }

    private void numberCalling(String number) {

        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);
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

    private void callAllRequirements(ArrayList<UserRequirements> userRequirements) {
        String listString = "";
        String idList = "";
        for (int i = 0; i < userRequirements.size(); i++) {
            String val = userRequirements.get(i).getReq_name();
            String id = userRequirements.get(i).getReq_id();
            requirementStr = userRequirements.get(i).getReq_id();
            listString += val + ",";
            idList += id + ",";
            requirementStr += id + ",";
            customerRequirementID.setText(listString);
            String te = customerRequirementID.getText().toString();
            te = te.substring(0, te.length() - 1) + "";
            customerRequirementID.setText(te);

            String re = customerRequirementID.getText().toString();
            if (re.contains("null,")) {
                re = re.replace("null,", "");
            }

            if (re.contains(",null")) {
                re = re.replace(",null", "");
            }
            customerRequirementID.setText(Utilities.CapitalText(re));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendMessageRLID:
                Intent intentMe = new Intent(getActivity(), SendTemplatesActivity.class);
                intentMe.putExtra("TYPE", "Send Sms");
                intentMe.putExtra("LEAD_MOBILE_NUMBER", "" + mobileStr);
                intentMe.putExtra("LEAD_EMAIL", "" + emailStr);
                intentMe.putExtra("LEAD_ID", "" + VALUE_LEAD_ID);
                startActivity(intentMe);
                break;

            case R.id.sendLocationRLID:

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }


                if (!gps_enabled && !network_enabled) {
                    // notify user
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.gps_network_not_enabled)
                            .setNegativeButton(R.string.Cancel, null)
                            .setPositiveButton(getResources().getText(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                                }
                            }).show();

                } else {
                    shareLocationAlert(mobileStr);
                }
                break;

            case R.id.shareContactRLID:
                shareContactDetails();
                break;

            case R.id.disableRLID:
                disableBottomAlert(LeadHistoryActivity.customerID, userID);
                break;

            case R.id.mobileRLID:
                startActivity();
                break;

            case R.id.emailRLID:
                startActivity();
                break;

            case R.id.websiteRLID:
                startActivity();
                break;

            case R.id.locationRLID:
                startActivity();
                break;

            case R.id.gstRLID:
                startActivity();
                break;

            case R.id.requirementRLID:
                startActivity();
                break;

            case R.id.requirementBtn:
                //showRequirementsAlert("REQUIREMENT");
                showActivitiesAlert("REQUIREMENT", "1");
                break;

            case R.id.activityBtn:
                showActivitiesAlert("ACTIVITIES","1");
                break;

            case R.id.sourceBtn:
                showActivitiesAlert("SOURCES", "1");
                break;

            case R.id.employeeBtn:
                showActivitiesAlert("EMPLOYEE", "1");
                break;

            case R.id.submitActivityBtn:
                showCompleteActivityAlert();
                break;

            case R.id.dateRLID:


                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //dateTVID.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                dateStr = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                dateTVID.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                //completeResponse( customerID, companyID,  autoID);
                                getUpdateLead();
                            }

                        }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.timeRLID:

                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(mTime,getActivity(),this,timeTVID,statusResponse,VALUE_LEAD_ID,activityStr,dateStr,sourceStr,requirementStr,createdId,userID,"1");
                newFragment.show(getChildFragmentManager(), "timePicker");

                /*Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.setThemeDark(false);
                tpd.show(getChildFragmentManager(), "Timepickerdialog");*/
                //tpd.show(getChildFragmentManager(), "Timepickerdialog");

                break;

            case R.id.newPhoneRL:
                CommunicationsServices.InsertCommunication(getActivity(), "1", leadID, userID, "", "");

                Intent intent = new Intent(getActivity(), CallCompleteActivity.class);
                intent.putExtra("ACTIVITY_NAME",activityName);
                intent.putExtra("ACTIVITY_ID",activityStr);
                intent.putExtra("CUSTOMER_NAME",nameStr);
                intent.putExtra("CUSTOMER_MOBILE", mobileStr);
                intent.putExtra("LEAD_ID",leadID);
                startActivity(intent);

                numberCalling(alternateNumberStr);
                break;
        }
    }


    public static class TimePickerFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

        private TimePickerDialog.OnTimeSetListener mListener;
        private Context context;
        private String time;
        TextView textView;
        private int mHour;
        private int mMinute;
        private int finalHour;
        Activity activity;
        ArrayList<StatusResponse> statusResponse = new ArrayList<>();
        String VALUE_LEAD_ID,activityStr,dateStr,sourceStr,requirementStr,createdId,userID;



        String updateLead;


        public void setListener(String time,Activity activity,TimePickerDialog.OnTimeSetListener mListener, TextView textView, ArrayList<StatusResponse> statusResponse, String VALUE_LEAD_ID, String activityStr, String dateStr, String sourceStr, String requirementStr, String createdId, String userID, String updateLead) {
            this.time = time;
            this.activity = activity;
            this.mListener = mListener;
            this.textView = textView;
            this.statusResponse = statusResponse;
            this.VALUE_LEAD_ID = VALUE_LEAD_ID;
            this.activityStr = activityStr;
            this.dateStr = dateStr;
            this.sourceStr = sourceStr;
            this.requirementStr = requirementStr;
            this.createdId = createdId;
            this.userID = userID;
            this.updateLead = updateLead;
        }

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            this.context = context;
        }


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new android.app.TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
           // return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }



        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            mHour = hourOfDay;
            mMinute = minute;
            finalHour = mHour;
            if (minute < 10) {
                time = hourOfDay + ":" + "0" + minute;
            } else {
                time = hourOfDay + ":" + minute;
            }

            Log.e("Time==>",""+ time);

            if (updateLead.equalsIgnoreCase("1")){

                getUpdateLead(time);
            }

            textView.setText(time);

        }

        private void getUpdateLead(String time) {

               ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
            Call<ArrayList<StatusResponse>> call = apiInterface.getUpdateLeadProfile(VALUE_LEAD_ID,activityStr,dateStr, time,sourceStr,requirementStr,createdId,userID);
            Log.e("api==>",call.request().toString());
            call.enqueue(new Callback<ArrayList<StatusResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                    if (response.body() != null && response.code() == 200){
                        statusResponse = response.body();

                        if (statusResponse.get(0).getStatus() != null && statusResponse.get(0).getStatus().equalsIgnoreCase("1")){
                            Toast.makeText(activity, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    private void showCompleteActivityAlert() {



        Dialog dialog2 = new Dialog(getActivity());
        dialog2.setContentView(R.layout.complete_activity_alert);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog2.getWindow().setLayout(width, height);
        dialog2.getWindow().setGravity(Gravity.CENTER);

        dialog2.show();

        activitySPID = dialog2.findViewById(R.id.activitySPID);
        TextView date2TVID = dialog2.findViewById(R.id.dateTVID);
        TextView date3TVID = dialog2.findViewById(R.id.nextactivitydateTVID);
        completeTimeTVID = dialog2.findViewById(R.id.completeTimeTVID);


    /*    Date formattedDated=null;*/

        /*handleDateResponse(formattedDated);*/

        if(NextActivitydateStr==null){
            final Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            //date formate



            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            NextActivitydateStr = dateFormat.format(currentDate);
            date3TVID.setText(NextActivitydateStr);
        }

       /* final Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        //date formate



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);
        date3TVID.setText(formattedDate);*/

        // Format time
//        Calendar calendartime = Calendar.getInstance();
//        Date currentTime = calendartime.getTime();
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//        String formattedTime = timeFormat.format(timeFormat);

  //    completeTimeTVID.setText(formattedTime);





        activityNameTVID = dialog2.findViewById(R.id.activityNameTVID);
        nextactivityTVID = dialog2.findViewById(R.id.nextactivityTVID);
        leadstatusTVID = dialog2.findViewById(R.id.leadstatusTVID);

        requrimentNameTVID = dialog2.findViewById(R.id.requrimentNameTVID);
        nextrequrimentNameTVID = dialog2.findViewById(R.id.nextrequrimentNameTVID);
        RelativeLayout dateRLID = dialog2.findViewById(R.id.dateRLID);
        RelativeLayout nextactivitydateRLID = dialog2.findViewById(R.id.nextactivitydateRLID);
      //  RelativeLayout nextTimeRLID = dialog2.findViewById(R.id.nextTimeRLID);
        RelativeLayout timeRLID = dialog2.findViewById(R.id.timeRLID);
        RelativeLayout activityRLID2 = dialog2.findViewById(R.id.activityRLID2);
        RelativeLayout nextActivityRLID2 = dialog2.findViewById(R.id.nextActivityRLID2);
        RelativeLayout leadstatusRLID2 = dialog2.findViewById(R.id.leadstatusRLID2);

        RelativeLayout requrimentRLID2 = dialog2.findViewById(R.id.requrimentRLID2);
        RelativeLayout nextrequrimentRLID2 = dialog2.findViewById(R.id.nextrequrimentRLID2);
        messageETID = dialog2.findViewById(R.id.messageETID);
        Button completeActBtn = dialog2.findViewById(R.id.completeActBtn);





        date2TVID.setText(dateStr);
        activityNameTVID.setText(activityNameStr);
        nextactivityTVID.setText(nextActivityNameStr);
//        leadstatusTVID.setText(allStatusResoponses.get(0).getStatus_name());
       // nextActivityRLID2.setText(nextActivityNameStr);
        requrimentNameTVID.setText(requirementNameStr);
        nextrequrimentNameTVID.setText(requirementNameStr);

        activityRLID2.setOnClickListener(v -> {
            showActivitiesAlert("ACTIVITIES","2");
        });
        nextActivityRLID2.setOnClickListener(v -> {
            showActivitiesAlert("ACTIVITIES","3");
        });

        leadstatusRLID2.setOnClickListener(v -> {
            showActivitiesAlert("ALLSTATUS","1");
        });
        requrimentRLID2.setOnClickListener(v -> {
            showActivitiesAlert("REQUIREMENT","2");
        });

        nextrequrimentRLID2.setOnClickListener(v -> {
            showActivitiesAlert("REQUIREMENT","3");
        });

        /*activitySPID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activityNextId = activityMainResponses1.get(position).getActivity_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        dateRLID.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //dateTVID.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            dateStr = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            date2TVID.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            //completeResponse( customerID, companyID,  autoID);
                            //getUpdateLead();
                        }

                    }, year, month, day);
            datePickerDialog.show();

        });





        nextactivitydateRLID.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
                 Date currentDate2 = calendar.getTime();

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate2 = dateFormat2.format(currentDate2);
            date3TVID.setText(formattedDate2);

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            //dateTVID.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            NextActivitydateStr = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            date3TVID.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);




                            handleDateResponse(calendar.getTime());
                            //completeResponse( customerID, companyID,  autoID);
                            //getUpdateLead();
                        }

                    }, year, month, day);
            datePickerDialog.show();

        });

        Calendar calendartime = Calendar.getInstance();
        Date currentTime = calendartime.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
       formattedTime = timeFormat.format(currentTime);


       completeTimeTVID.setText(formattedTime);

        timeRLID.setOnClickListener(v -> {
            TimePickerFragment newFragment = new TimePickerFragment();
            newFragment.setListener(mTime,getActivity(),this,completeTimeTVID,statusResponse,VALUE_LEAD_ID,activityStr,dateStr,sourceStr,requirementStr,createdId,userID,"0");
            newFragment.show(getChildFragmentManager(), "timePicker");
//            nextTimestr=statusResponse.get()
        });
       // nextTimeTVID.setText(mTime);
     /*   nextTimeRLID.setOnClickListener(v -> {
            TimePickerFragment newFragment = new TimePickerFragment();
            newFragment.setListener(mTime,getActivity(),this,nextTimeTVID,statusResponse,VALUE_LEAD_ID,activityStr,dateStr,sourceStr,requirementStr,createdId,userID,"0");
            newFragment.show(getChildFragmentManager(), "timePicker");
        });*/





        micLID = dialog2.findViewById(R.id.micLID);

            String planType = MySharedPreferences.getPreferences(getContext(),"PLAN_TYPE");
        if(planType.equals("1")){
            micLID.setVisibility(View.VISIBLE);
        }else if (planType.equals("2")){
            micLID.setVisibility(View.VISIBLE);
        }else{
            micLID.setVisibility(View.GONE);
        }

        micLID.setOnClickListener(v -> {
            startVoiceInput(REQ_CODE_SPEECH_INPUT);
        });


        completeActBtn.setOnClickListener(v -> {

            if (Utilities.isNetworkAvailable(getActivity())){

                String message = messageETID.getText().toString();
                Log.e("message==>","" + message );
                if(nextActivityNameStr==null){
                   dialog2.show();
                    Toast.makeText(getContext(), "select any NextActivity", Toast.LENGTH_SHORT).show();
                }else{
                    completeActivity(nextActivityNameStr,activityStr,dateStr,message,userID,VALUE_LEAD_ID,dialog2);
                    getUpdateNewLead(nextActivityNameStr);
                    loadLeadProfileDetails(VALUE_LEAD_ID);
                    dialog2.dismiss();
                }




            }else {
                Toast.makeText(getActivity(), "Please check your network", Toast.LENGTH_SHORT).show();
            }



        });

        //dialog.dismiss();
    }


    private void handleDateResponse(Date selectedDate){
        if(selectedDate==null){
            selectedDate=calendar.getTime();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(selectedDate);
    }

    private void startVoiceInput(int code) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, code);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    messageETID.setText(result.get(0));
                }
                break;
            }
            case REQ_CODE_SPEECH_INPUT_NOTE: {
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    noteETID.setText(result.get(0));
                }
            }

        }
    }

    private void completeActivity(String nextActivityNameStr,String activityStr, String dateStr, String message, String userID, String value_lead_id, Dialog dialog) {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.completeActivity(nextActivityNameStr,dateStr,message,userID,value_lead_id,requirementNameStrid,formattedTime,activityStr,NextActivitydateStr,nextrequrimentStr,leadstatusStr    );
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.body() != null && response.code() == 200){
                    StatusResponse statusResponse = response.body();

                    if (statusResponse.getStatus().equalsIgnoreCase("1")){
                      //  Toast.makeText(getActivity(), "Activity completed successfully", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.dismiss();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showRequirementsAlert(final String type, String alertType) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.search_drop_down);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
        rePB = dialog.findViewById(R.id.rePB);
        rePB.setVisibility(View.GONE);
        Button saveRequirementBtn = dialog.findViewById(R.id.saveRequirementBtn);
        /*otherReqID = dialog.findViewById(R.id.otherReqID);
        otherReqETID = dialog.findViewById(R.id.otherReqETID);*/
        dropDownGVID = dialog.findViewById(R.id.dropDownLVID);
        dropDownGVID.setFocusable(false);

        final EditText searchHintETID = dialog.findViewById(R.id.searchHintETID);
        final LinearLayout searchLLID = dialog.findViewById(R.id.searchLLID);
        searchLLID.setFocusable(false);


        if (Utilities.isNetworkAvailable(getActivity())) {
            requirements(alertType);
        } else {
            AlertUtilities.bottomDisplayStaticAlert(getActivity(), "No Internet connection", "MAke sure your device is connected to internet");
        }

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
                if (type.equalsIgnoreCase("REQUIREMENT")) {
                    String te = searchHintETID.getText().toString();
                    requirementAdapter.getFilter().filter(te);
                }


            }
        });
        saveRequirementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(getActivity())) {


                    getUpdateLead();

                    if (otherReqIsVisible) {
                        requirementOtherStr = otherReqETID.getText().toString();

                        if (TextUtils.isEmpty(requirementOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter New Requirement Name", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            selectedRequirementArrays = "";
                            dateTVID.setText(requirementOtherStr);
                        }

                    } else {
                        //saveSelectedRequirements();
                    }


                } else {
                    AlertUtilities.bottomDisplayStaticAlert(getActivity(), getString(R.string.no_internet_title), getString(R.string.no_internet_desc));
                }
            }
        });
    }

    private void getUpdateNewLead(String actID) {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.getUpdateLeadProfile(VALUE_LEAD_ID,actID,dateStr,mTime,sourceStr,requirementStr,createdId,userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                if (response.body() != null && response.code() == 200){
                    statusResponse = response.body();

                    if (statusResponse.get(0).getStatus().equalsIgnoreCase("1")){
                       // Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUpdateLead() {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.getUpdateLeadProfile(VALUE_LEAD_ID,activityStr,dateStr,mTime,sourceStr,requirementStr,createdId,userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                if (response.body() != null && response.code() == 200){
                    statusResponse = response.body();

                    if (statusResponse.get(0).getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void saveSelectedRequirements() {
        try {
            ArrayList<String> mCheckedItemIDs = requirementAdapter.getCheckedItems();
            ArrayList<String> mCheckedItemIDNames = requirementAdapter.getCheckedItemsNames();

            Log.w("", "" + mCheckedItemIDs.size());
            /// add elements to al, including duplicates
            Set<String> hs = new HashSet<>();
            Set<String> name = new HashSet<>();

            hs.addAll(mCheckedItemIDs);
            name.addAll(mCheckedItemIDNames);

            mCheckedItemIDNames.clear();
            mCheckedItemIDs.clear();

            mCheckedItemIDs.addAll(hs);
            mCheckedItemIDNames.addAll(name);

            Log.w("", "" + mCheckedItemIDs.size());

            StringBuilder sb = new StringBuilder();
            StringBuilder sbn = new StringBuilder();
            if (mCheckedItemIDs.size() == 1 && mCheckedItemIDNames.size() == 1) {
                sb.append(mCheckedItemIDs.get(0));
                sbn.append(mCheckedItemIDNames.get(0));
            } else {
                for (int j = 0; j < mCheckedItemIDs.size(); j++) {
                    if (j == 0) {
                        sb.append(mCheckedItemIDs.get(j)).append(",");
                    } else {
                        int a = mCheckedItemIDs.size() - 1;
                        if (j == a) {
                            sb.append("").append(mCheckedItemIDs.get(j));
                        } else {
                            sb.append("").append(mCheckedItemIDs.get(j)).append(",");
                        }
                    }
                }

                for (int k = 0; k < mCheckedItemIDNames.size(); k++) {
                    if (k == 0) {
                        sbn.append(mCheckedItemIDNames.get(k)).append(",");
                    } else {
                        int a = mCheckedItemIDNames.size() - 1;
                        if (k == a) {
                            sbn.append("").append(mCheckedItemIDNames.get(k));
                        } else {
                            sbn.append("").append(mCheckedItemIDNames.get(k)).append(",");
                        }
                    }
                }
            }

            selectedRequirementArrays = sb.toString();
            requirementStr = sb.toString();
            selectedRequirementNameArrays = sbn.toString();
            dialog.dismiss();

            if (!selectedRequirementArrays.equals("")) {
                if (selectedRequirementArrays.contains("1000")) {
                    selectedRequirementArrays = "1000";
                } else {
                    requirementOtherStr = "";
                    customerRequirementID.setText(selectedRequirementNameArrays);

                }
            } else {
                //AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "No Tags selected", "Please select requirements...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @SuppressLint("ClickableViewAccessibility")  private void showActivitiesAlert(final String type, String alertType) {

        dialog = new Dialog(getActivity());
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
        otherFieldSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (type.equalsIgnoreCase("ACTIVITIES")) {

                    activityOtherStr = otherFieldETID.getText().toString();

                    if (otherActivityIsVisible) {
                        if (TextUtils.isEmpty(activityOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter Other Activity Name", Toast.LENGTH_SHORT).show();
                        } else {
                            activityTVID.setText(activityOtherStr);
                            dialog.dismiss();
                        }
                    }
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    sourceOtherStr = otherFieldETID.getText().toString();
                    if (otherSourceIsVisible) {
                        if (TextUtils.isEmpty(sourceOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter Other Source", Toast.LENGTH_SHORT).show();
                        } else {
                            customerSourceID.setText(sourceOtherStr);
                            dialog.dismiss();
                        }
                    }
                } else if (type.equalsIgnoreCase("REQUIREMENT")) {

                    if (otherReqIsVisible) {
                        requirementOtherStr = otherReqETID.getText().toString();

                        if (TextUtils.isEmpty(requirementOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter New Requirement Name", Toast.LENGTH_SHORT).show();
                        } else {

                            selectedRequirementArrays = "";
                            customerRequirementID.setText(requirementOtherStr);
                            dialog.dismiss();
                        }

                    }

                }else if (type.equalsIgnoreCase("EMPLOYEE")) {
                    //sourceOtherStr = otherFieldETID.getText().toString();
                    //employeeTVID.setText(sourceOtherStr);
                    dialog.dismiss();
                    /*if (otherSourceIsVisible) {
                        if (TextUtils.isEmpty(sourceOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter Other Source", Toast.LENGTH_SHORT).show();
                        } else {
                            customerSourceID.setText(sourceOtherStr);
                            dialog.dismiss();
                        }
                    }*/
                }
            }
        });


        if (Utilities.isNetworkAvailable(getActivity())) {

            if (type.equalsIgnoreCase("ACTIVITIES")) {
                activities(alertType);
            } else if (type.equalsIgnoreCase("SOURCES")) {
                sources();
            } else if (type.equalsIgnoreCase("REQUIREMENT")) {
                requirements(alertType);
            }else if (type.equalsIgnoreCase("EMPLOYEE")) {
                employee();
            }else if (type.equalsIgnoreCase("ALLSTATUS")) {
                allstatus();
            }

        } else {
            AlertUtilities.bottomDisplayStaticAlert(getActivity(), "No Internet connection", "MAke sure your device is connected to internet");
        }

        dropDownLVID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> requirementAdapterView, View view, int i, long l) {
                dialog.dismiss();

                if (type.equalsIgnoreCase("ACTIVITIES")) {

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
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    customerSourceID.setText(sourceResponses.get(i).getSource_name());
                } else if (type.equalsIgnoreCase("REQUIREMENT")) {

                    if (alertType.equalsIgnoreCase("1")){
                        customerRequirementID.setText(requirementResponses.get(i).getRequirement_name());
                        // requirementNameStr=requirementResponses.get(i).getRequirement_name();
                    }else if (alertType.equalsIgnoreCase("2")){
                        requrimentNameTVID.setText(requirementResponses.get(i).getRequirement_name());
                        //    requirementNameStr=requirementResponses.get(i).getRequirement_name();
                    }else if (alertType.equalsIgnoreCase("3")){
                        nextrequrimentNameTVID.setText(requirementResponses.get(i).getRequirement_name());
                    }
                    requirementNameStr=requirementResponses.get(i).getRequirement_name();


                }
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

                if (type.equalsIgnoreCase("ACTIVITIES")) {
                    String te = searchHintETID.getText().toString();
                    activitiesAdapter.getFilter().filter(te);
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    String te = searchHintETID.getText().toString();
                    sAdapter.getFilter().filter(te);
                } else if (type.equalsIgnoreCase("REQUIREMENT")) {
                    String te = searchHintETID.getText().toString();
                    requirementAdapter.getFilter().filter(te);
                }else if (type.equalsIgnoreCase("EMPLOYEE")) {
                    String te = searchHintETID.getText().toString();
                    employeeAdapter.getFilter().filter(te);
                }


            }
        });

    }


    private void allstatus(){
        drPB.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<AllStatusResoponse>> call = apiInterface.getAllStatus();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllStatusResoponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllStatusResoponse>> call, Response<ArrayList<AllStatusResoponse>> response) {
               // Toast.makeText(getContext(), "on resoponse", Toast.LENGTH_SHORT).show();
                drPB.setVisibility(View.GONE);

                if (response.body() != null && response.code() == 200) {
                    allStatusResoponses = response.body();
                    if (allStatusResoponses != null && allStatusResoponses.size() > 0) {
                        for (int i = 0; i < allStatusResoponses.size(); i++) {
                            allstatusAdapter = new AllstatusAdapter(getActivity(), allStatusResoponses);
                            dropDownLVID.setAdapter(allstatusAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<ArrayList<AllStatusResoponse>> call, Throwable t) {
            //    Toast.makeText(getContext(), "on fail", Toast.LENGTH_SHORT).show();
                drPB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Employees not loading", Toast.LENGTH_SHORT).show();


            }
        });


    }




    private void employee() {

        drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<EmployeeResponse>> call = apiInterface.getEmployees();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<EmployeeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeeResponse>> call, Response<ArrayList<EmployeeResponse>> response) {
                drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    employeeResponses = response.body();
                    if (employeeResponses != null && employeeResponses.size() > 0) {
                        for (int i = 0; i < employeeResponses.size(); i++) {
                            employeeAdapter = new EmployeeAdapter(getActivity(), employeeResponses);
                            dropDownLVID.setAdapter(employeeAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmployeeResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Employees not loading", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void activities(String alertType) {
        drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<ActivityMainResponse>> call = apiInterface.getActivities(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ActivityMainResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivityMainResponse>> call, Response<ArrayList<ActivityMainResponse>> response) {
                drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    activityMainResponses1 = response.body();
                    if (activityMainResponses1 != null && activityMainResponses1.size() > 0) {
                        for (int i = 0; i < activityMainResponses1.size(); i++) {
                            activitiesAdapter = new ActivitiesNewAdapter(getActivity(), activityMainResponses1,alertType);
                            dropDownLVID.setAdapter(activitiesAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);

                            /*ActivitiesAdapter adapter = new ActivitiesAdapter(getActivity(), R.layout.custom_spinner_view,activityMainResponses1);
                            activitySPID.setAdapter(adapter);*/
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ActivityMainResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Activities not loading", Toast.LENGTH_SHORT).show();

            }
        });

    }



    private void sources() {
        drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<SourceResponse>> call = apiInterface.getSource(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SourceResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SourceResponse>> call, Response<ArrayList<SourceResponse>> response) {
                drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {

                    sourceResponses = response.body();
                    if (sourceResponses != null && sourceResponses.size() > 0) {
                        for (int i = 0; i < sourceResponses.size(); i++) {
                            sAdapter = new SourceNewAdapter(getActivity(), sourceResponses);
                            dropDownLVID.setAdapter(sAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SourceResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Sources not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void requirements(String alertType) {
        drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<RequirementResponse>> call = apiInterface.getRequirements(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RequirementResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RequirementResponse>> call, Response<ArrayList<RequirementResponse>> response) {
                drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {

                    requirementResponses = response.body();
                    if (requirementResponses != null && requirementResponses.size() > 0) {
                        for (int i = 0; i < requirementResponses.size(); i++) {
                            requirementAdapter = new RequirementAdapter(getActivity(), requirementResponses,alertType);
                            if(alertType.equalsIgnoreCase("1")){
                                dropDownLVID.setAdapter(requirementAdapter);
                            }else if (alertType.equalsIgnoreCase("2")){
                                dropDownLVID.setAdapter(requirementAdapter);
                            }else if (alertType.equalsIgnoreCase("3")){
                                dropDownLVID.setAdapter(requirementAdapter);
                            }

                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequirementResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Requirements not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /*private void requirements() {
        rePB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<RequirementResponse>> call = apiInterface.getRequirements(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RequirementResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RequirementResponse>> call, Response<ArrayList<RequirementResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    rePB.setVisibility(View.GONE);

                    requirementResponses = response.body();
                    if (requirementResponses != null && requirementResponses.size() > 0) {
                        for (int i = 0; i < requirementResponses.size(); i++) {
                            requirementAdapter = new LeadCreateActivity.RequirementNewAdapter(getActivity(), requirementResponses);
                            dropDownGVID.setAdapter(requirementAdapter);
                            dropDownGVID.setExpanded(true);
                            dropDownGVID.setFocusable(false);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Requirement not loading", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Requirement not loading", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequirementResponse>> call, Throwable t) {
                rePB.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Requirement not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

    private void startActivity() {
        Intent intent = new Intent(getActivity(), LeadEditActivity.class);
        intent.putExtra("CUSTOMER_ID", "" + LeadHistoryActivity.customerID);
        startActivity(intent);
    }

    private void disableBottomAlert(final String customerID, final String userID) {

        final Dialog dialog = new Dialog(getActivity(), R.style.SheetDialog);
        dialog.setContentView(R.layout.alert_delete);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        RelativeLayout cancelRLID = dialog.findViewById(R.id.cancelRLID);

        final EditText otherETID = dialog.findViewById(R.id.otherETID);
        cancelRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String other = otherETID.getText().toString();

                disableResponse(customerID, companyID, id, other, userID);
            }
        });


        final RelativeLayout otherRLID = dialog.findViewById(R.id.otherRLID);

        reasonsSPID = dialog.findViewById(R.id.reasonsSPID);
        reasons();
        reasonsSPID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id = activityMainResponses.get(i).getReason_id();
                if (id.equalsIgnoreCase("1001")) {
                    otherRLID.setVisibility(View.VISIBLE);
                } else {
                    otherRLID.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }

    public class ActivitiesNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<ActivityMainResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private ActivitiesNewAdapter.ValueFilter valueFilter;
        private ArrayList<ActivityMainResponse> mStringFilterList;
        String type;

        public ActivitiesNewAdapter(Activity context, ArrayList<ActivityMainResponse> _Contacts,String type) {
            super();
            this.context = context;
            this._Contacts = _Contacts;
            this.type = type;
            mStringFilterList = _Contacts;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }

        @Override
        public int getCount() {
            return _Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return _Contacts.get(position).getActivity_name();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


            ActivitiesNewAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new ActivitiesNewAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (ActivitiesNewAdapter.ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getActivity_name());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    otherActivityIsVisible = false;
                    if (activityStr.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Activity");
                        otherActivityIsVisible = true;
                    } else {
                        activityOtherStr = "";
                        otherActivityIsVisible = false;
                        dialog.dismiss();

                        if(type.equalsIgnoreCase("1")){
                            activityTVID.setText(_Contacts.get(position).getActivity_name());
                            activityStr = _Contacts.get(position).getActivity_id();
                            //activityNameTVID.setText(_Contacts.get(position).getActivity_name());
                        }else {
                            if(type.equalsIgnoreCase("2")){
                                activityNameTVID.setText(_Contacts.get(position).getActivity_name());
                               activityStr = _Contacts.get(position).getActivity_id();
                            }else{
                                nextactivityTVID.setText(_Contacts.get(position).getActivity_name());
                                nextActivityNameStr=_Contacts.get(position).getActivity_id();
                            }



                        }

                    }

                    if(type.equalsIgnoreCase("1")){
                        //getUpdateLead();
                        getUpdateNewLead(activityStr);
                    }

                    //activityStr = _Contacts.get(position).getActivity_id();


                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new ActivitiesNewAdapter.ValueFilter();
            }

            return valueFilter;
        }

        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<ActivityMainResponse> filterList = new ArrayList<ActivityMainResponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getActivity_name().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            ActivityMainResponse contacts = new ActivityMainResponse();
                            contacts.setActivity_name(mStringFilterList.get(i).getActivity_name());
                            contacts.setActivity_id(mStringFilterList.get(i).getActivity_id());
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
                _Contacts = (ArrayList<ActivityMainResponse>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    public class AllstatusAdapter extends BaseAdapter implements Filterable{

        private ArrayList<AllStatusResoponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private ValueFilter valueFilter;
        private ArrayList<AllStatusResoponse> mStringFilterList;
        public AllstatusAdapter(Activity context, ArrayList<AllStatusResoponse> _Contacts) {
            super();
            this.context = context;
            this._Contacts = _Contacts;

            mStringFilterList = _Contacts;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }


        @Override
        public int getCount() {
            return _Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return _Contacts.get(position).getStatus_name();
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

            AllstatusAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new AllstatusAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (AllstatusAdapter.ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getStatus_name());
            holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);

            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leadstatusStr = _Contacts.get(position).getStatus_id();
                    leadstatusTVID.setText(_Contacts.get(position).getStatus_name());
                    getUpdateLead();
                    otherReqIsVisible = false;
                    dialog.dismiss();
                }
            });










            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new AllstatusAdapter.ValueFilter();
            }

            return valueFilter;
        }
        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<AllStatusResoponse> filterList = new ArrayList<AllStatusResoponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getStatus_name().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            AllStatusResoponse contacts = new AllStatusResoponse();
                            contacts.setStatus_name(mStringFilterList.get(i).getStatus_name());
                            contacts.setStatus_id(mStringFilterList.get(i).getStatus_id());
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
                _Contacts = (ArrayList<AllStatusResoponse>) results.values;
                notifyDataSetChanged();
            }
        }
    }



    public class EmployeeAdapter extends BaseAdapter implements Filterable {
        private ArrayList<EmployeeResponse> _Contacts = new ArrayList<>();
        private Activity context;
        private LayoutInflater inflater;
        private EmployeeAdapter.ValueFilter valueFilter;
        private ArrayList<EmployeeResponse> mStringFilterList;

        public EmployeeAdapter(Activity context, ArrayList<EmployeeResponse> _Contacts) {
            super();
            this.context = context;
            this._Contacts = _Contacts;
            mStringFilterList = _Contacts;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }

        @Override
        public int getCount() {
            return _Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return _Contacts.get(position).getUser_name();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


            EmployeeAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new EmployeeAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (EmployeeAdapter.ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getUser_name());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    createdId = _Contacts.get(position).getUser_id();
                    employeeTVID.setText(_Contacts.get(position).getUser_name());
                    getUpdateLead();
                    otherActivityIsVisible = false;
                   /* if (employeeStr.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Activity");
                        otherActivityIsVisible = true;
                    } else {
                        activityOtherStr = "";
                        otherActivityIsVisible = false;
                        dialog.dismiss();
                        employeeTVID.setText(_Contacts.get(position).getUser_name());
                    }*/
                    dialog.dismiss();
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new EmployeeAdapter.ValueFilter();
            }

            return valueFilter;
        }

        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<EmployeeResponse> filterList = new ArrayList<EmployeeResponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getUser_name().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            EmployeeResponse contacts = new EmployeeResponse();
                            contacts.setUser_name(mStringFilterList.get(i).getUser_name());
                            contacts.setUser_id(mStringFilterList.get(i).getUser_id());
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
                _Contacts = (ArrayList<EmployeeResponse>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    public class RequirementAdapter extends BaseAdapter implements Filterable {
        private ArrayList<RequirementResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private RequirementAdapter.ValueFilter valueFilter;
        private ArrayList<RequirementResponse> mStringFilterList;
        String type;
        public RequirementAdapter(Activity context, ArrayList<RequirementResponse> _Contacts,String type) {
            super();
            this.context = context;
            this._Contacts = _Contacts;
            this.type = type;
            mStringFilterList = _Contacts;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }


        @Override
        public int getCount() {
            return _Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return _Contacts.get(position).getRequirement_name();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


            RequirementAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new RequirementAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (RequirementAdapter.ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getRequirement_name());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requirementStr = _Contacts.get(position).getRequirement_id();
                    getUpdateLead();
                    otherReqIsVisible = false;
                    if (requirementStr.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Requirement");
                        otherReqIsVisible = true;
                    } else {
                        requirementOtherStr = "";
                        otherReqIsVisible = false;
                        dialog.dismiss();

                        if(type.equalsIgnoreCase("1")){
                            customerRequirementID.setText(_Contacts.get(position).getRequirement_name());
                           // requirementNameStr=_Contacts.get(position).getRequirement_id();
                        }else if (type.equalsIgnoreCase("2")){
                            requrimentNameTVID.setText(_Contacts.get(position).getRequirement_name());
                         //   requirementNameStr=_Contacts.get(position).getRequirement_id();
                        }else if(type.equalsIgnoreCase("3")){
                            nextrequrimentStr=_Contacts.get(position).getRequirement_id();
                            nextrequrimentNameTVID.setText(_Contacts.get(position).getRequirement_name());
                        }
                        
                        
                        
                       
                    }
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new RequirementAdapter.ValueFilter();
            }

            return valueFilter;
        }

        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<RequirementResponse> filterList = new ArrayList<RequirementResponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getRequirement_name().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            RequirementResponse contacts = new RequirementResponse();
                            contacts.setRequirement_name(mStringFilterList.get(i).getRequirement_name());
                            contacts.setRequirement_id(mStringFilterList.get(i).getRequirement_id());
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
                _Contacts = (ArrayList<RequirementResponse>) results.values;
                notifyDataSetChanged();
            }
        }
    }


    public class SourceNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<SourceResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private SourceNewAdapter.ValueFilter valueFilter;
        private ArrayList<SourceResponse> mStringFilterList;

        public SourceNewAdapter(Activity context, ArrayList<SourceResponse> _Contacts) {
            super();
            this.context = context;
            this._Contacts = _Contacts;
            mStringFilterList = _Contacts;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }

        @Override
        public int getCount() {
            return _Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return _Contacts.get(position).getSource_name();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


            SourceNewAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new SourceNewAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (SourceNewAdapter.ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getSource_name());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sourceStr = _Contacts.get(position).getSource_id();
                    getUpdateLead();
                    otherSourceIsVisible = false;
                    if (sourceStr.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Source");
                        otherSourceIsVisible = true;
                    } else {
                        sourceOtherStr = "";
                        otherSourceIsVisible = false;
                        dialog.dismiss();
                        customerSourceID.setText(_Contacts.get(position).getSource_name());
                    }
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new SourceNewAdapter.ValueFilter();
            }

            return valueFilter;
        }

        private class ValueFilter extends Filter {

            //Invoked in a worker thread to filter the data according to the constraint.
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<SourceResponse> filterList = new ArrayList<SourceResponse>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getSource_name().toUpperCase())
                                .contains(constraint.toString().toUpperCase())) {
                            SourceResponse contacts = new SourceResponse();
                            contacts.setSource_name(mStringFilterList.get(i).getSource_name());
                            contacts.setSource_id(mStringFilterList.get(i).getSource_id());
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
                _Contacts = (ArrayList<SourceResponse>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    private void disableResponse(String customerID, String companyID, String id, String other, String userID) {
        showProgressDialog();

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.getDisableLeadReasons(customerID, companyID, id, other, userID);
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {
                                getActivity().finish();
                                Toast.makeText(getActivity(), "Lead disabled", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void reasons() {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<ReasonsResponse>> call = apiInterface.getDisableReasons(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ReasonsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ReasonsResponse>> call, Response<ArrayList<ReasonsResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    activityMainResponses = response.body();
                    if (activityMainResponses != null && activityMainResponses.size() > 0) {
                        for (int i = 0; i < activityMainResponses.size(); i++) {
                            aAdapter = new ReasonsAdapter(getActivity(), R.layout.custom_spinner_view, activityMainResponses);
                            reasonsSPID.setAdapter(aAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReasonsResponse>> call, Throwable t) {

                Toast.makeText(getActivity(), "Activities not loading", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public class ReasonsResponse {
//        reason_id: "1",
//reason_title: "Not Interested"

        private String reason_id;
        private String reason_title;

        public ReasonsResponse(String reason_id, String reason_title) {
            this.reason_id = reason_id;
            this.reason_title = reason_title;
        }

        public String getReason_id() {
            return reason_id;
        }

        public void setReason_id(String reason_id) {
            this.reason_id = reason_id;
        }

        public String getReason_title() {
            return reason_title;
        }

        public void setReason_title(String reason_title) {
            this.reason_title = reason_title;
        }
    }

    public class ReasonsAdapter extends ArrayAdapter<ReasonsResponse> {

        List<ReasonsResponse> doubtsClassesResponces;

        public ReasonsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ReasonsResponse> doubtsClassesResponces) {
            super(context, resource, doubtsClassesResponces);
            this.doubtsClassesResponces = doubtsClassesResponces;
        }

        @Override
        public int getCount() {

            try {
                if (doubtsClassesResponces != null) {
                    return doubtsClassesResponces.size();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }


        public class ViewHolder {
            public TextView spinnerText;

            public ViewHolder(View view) {
                spinnerText = view.findViewById(R.id.spinnerText);
            }
        }

        public class ParentViewHolder {
            public TextView spinnerText;
            private ImageView dropdown;
            private RelativeLayout spinnerBgColor;

            public ParentViewHolder(View view) {
                spinnerText = view.findViewById(R.id.spinnerItems);
                spinnerBgColor = view.findViewById(R.id.spinnerBgColor);
            }
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


            ReasonsResponse category = getItem(position);
            ReasonsAdapter.ParentViewHolder parentViewHolder = null;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_spinner_view, null);
                parentViewHolder = new ReasonsAdapter.ParentViewHolder(view);
                view.setTag(parentViewHolder);
            } else {
                parentViewHolder = (ReasonsAdapter.ParentViewHolder) view.getTag();
            }
            parentViewHolder.spinnerText.setText(TextUtils.isEmpty(Utilities.CapitalText(category.getReason_title())) ? "" : Utilities.CapitalText(category.getReason_title()));
            return view;
        }


        @Override
        public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
            ReasonsResponse category = getItem(position);
            ReasonsAdapter.ViewHolder holder = null;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.custom_spinner_list_items, null);
                holder = new ReasonsAdapter.ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ReasonsAdapter.ViewHolder) view.getTag();
            }
            holder.spinnerText.setText(TextUtils.isEmpty(Utilities.CapitalText(category.getReason_title())) ? "" : Utilities.CapitalText(category.getReason_title()));
            return view;
        }
    }

    private void shareLocationAlert(final String number) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alert_share_location);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        mGPS = new GPSTracker(getActivity());

        if (mGPS == null) {
            mGPS = new GPSTracker(getActivity());
        }
        // check if mGPS object is created or not

        if (mGPS != null && location == null) {
            location = mGPS.getLocation();
        }
        if (location != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && addresses.size() > 0) {
                    try {
                        latitudeS = addresses.get(0).getLatitude();
                        langitudeS = addresses.get(0).getLongitude();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Location Not found", Toast.LENGTH_SHORT).show();
        }

        langitudeStr = String.valueOf(langitudeS);
        latitudeStr = String.valueOf(latitudeS);


        TextView mobileTVID = dialog.findViewById(R.id.mobileTVID);
        TextView whatsAppTVID = dialog.findViewById(R.id.whatsAppTVID);

        mobileTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                intent.putExtra("sms_body", "http://maps.google.com/maps?daddr=" + latitudeS + "," + langitudeS);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        whatsAppTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getActivity().getPackageManager();
                try {
                    Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode("http://maps.google.com/maps?daddr=" + latitudeS + "," + langitudeS, "UTF-8");
                    sendMsg.setPackage("com.whatsapp");
                    sendMsg.setData(Uri.parse(url));
                    if (sendMsg.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(sendMsg);
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void shareContactDetails() {
        String shareBody = "Name : " + nameStr + ",\n" + "Mobile : " + mobileStr + ",\n" + "Email : " + emailStr + ",\n" + "Company Name : " + companyStr + ".\n" + " \n" + "Best Regards " + "\n" + userNameStr + ".\n\n\n" + "App Link :  " + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    public class LeadProfileResponse {
        private String lead_id;
        private String lead_name;
        private String lead_last_name;
        private String lead_email;
        private String lead_mobile;
        private ArrayList<UserRequirements> lead_requirement = new ArrayList<>();
        private String lead_source;
        private String alternate_number;
        private String lead_company_name;
        private String lead_pic;
        private String created_name;
        private String created_id;
        private String activity_name;
        private String activity_id;
        private String activity_date;
        private String activity_time;
        private String source_id;
        private String city;
        private String location;
        private String requirement_id;
        private String note;
        private String requirement_name;

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getActivity_time() {
            return activity_time;
        }

        public void setActivity_time(String activity_time) {
            this.activity_time = activity_time;
        }

        public String getLead_last_name() {
            return lead_last_name;
        }

        public void setLead_last_name(String lead_last_name) {
            this.lead_last_name = lead_last_name;
        }

        public String getAlternate_number() {
            return alternate_number;
        }

        public void setAlternate_number(String alternate_number) {
            this.alternate_number = alternate_number;
        }

        public String getRequirement_name() {
            return requirement_name;
        }

        public void setRequirement_name(String requirement_name) {
            this.requirement_name = requirement_name;
        }

        public String getLead_pic() {
            return lead_pic;
        }

        public void setLead_pic(String lead_pic) {
            this.lead_pic = lead_pic;
        }

        public String getCreated_name() {
            return created_name;
        }

        public void setCreated_name(String created_name) {
            this.created_name = created_name;
        }

        public String getCreated_id() {
            return created_id;
        }

        public void setCreated_id(String created_id) {
            this.created_id = created_id;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getActivity_date() {
            return activity_date;
        }

        public void setActivity_date(String activity_date) {
            this.activity_date = activity_date;
        }

        public String getSource_id() {
            return source_id;
        }

        public void setSource_id(String source_id) {
            this.source_id = source_id;
        }

        public String getRequirement_id() {
            return requirement_id;
        }

        public void setRequirement_id(String requirement_id) {
            this.requirement_id = requirement_id;
        }

        public String getLead_company_name() {
            return lead_company_name;
        }

        public void setLead_company_name(String lead_company_name) {
            this.lead_company_name = lead_company_name;
        }

        public String getLead_id() {
            return lead_id;
        }

        public void setLead_id(String lead_id) {
            this.lead_id = lead_id;
        }

        public String getLead_name() {
            return lead_name;
        }

        public void setLead_name(String lead_name) {
            this.lead_name = lead_name;
        }

        public String getLead_email() {
            return lead_email;
        }

        public void setLead_email(String lead_email) {
            this.lead_email = lead_email;
        }

        public String getLead_mobile() {
            return lead_mobile;
        }

        public void setLead_mobile(String lead_mobile) {
            this.lead_mobile = lead_mobile;
        }

        public String getLead_source() {
            return lead_source;
        }

        public void setLead_source(String lead_source) {
            this.lead_source = lead_source;
        }



        public ArrayList<UserRequirements> getLead_requirement() {
            return lead_requirement;
        }

        public void setLead_requirement(ArrayList<UserRequirements> lead_requirement) {
            this.lead_requirement = lead_requirement;
        }

    }

    public class UserRequirements implements Serializable {
        private String req_name;
        private String req_id;

        public String getReq_id() {
            return req_id;
        }

        public void setReq_id(String req_id) {
            this.req_id = req_id;
        }

        public String getReq_name() {
            return req_name;
        }

        public void setReq_name(String req_name) {
            this.req_name = req_name;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utilities.isNetworkAvailable(getActivity())) {
            loadLeadProfileDetails(VALUE_LEAD_ID);
        } else {
            AlertUtilities.bottomDisplayStaticAlert(getActivity(), "No Internet connection...!", "Make sure your device is connected to internet");
        }
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(getActivity())
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

}
