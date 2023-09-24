package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.HandlerUtility;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CustomKnowlarityApis;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import crm.tranquil_sales_steer.ui.adapters.DeadReasonsAdapter;
import crm.tranquil_sales_steer.ui.adapters.NotesAdapter;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.AutoFollowupResponse;
import crm.tranquil_sales_steer.ui.models.CampaignCallResponse;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import crm.tranquil_sales_steer.ui.models.DisableResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeCallCreativesResponse;
import crm.tranquil_sales_steer.ui.models.NextCallResponse;
import crm.tranquil_sales_steer.ui.models.NotesResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallCompleteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private String tag="callService==>";
    ActivitiesNewAdapter activitiesAdapter;
    ArrayList<ActivityMainResponse> activityMainResponses = new ArrayList<>();
    DeadReasonNewAdapter deadReasonNewAdapter;
    ArrayList<NextCallResponse> nextCallResponses = new ArrayList<>();

    TextView headerTittleTVID,activityTVID,dateTVID,newLeadNameTVID,leadMobileTVID,timeTVID;
    RelativeLayout okRLID,activityRLID,dateRLID,timeLLID,whatsAppCallRLID,newPhoneRL,nextLeadRLID,whatsAppRLID;
    AutoCompleteTextView remarksAutoID;
    LinearLayout micLID,whatsAppLLID,callLLID,cubeCallLLID;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    String activityID,actNameStr,leadID,userID,dateStr,date,notesStr,customerNameStr,companyID,companyName,userType,nextMobileStr,presentCall,activityOtherStr,
    nextActID,nextActName,nextLeadName,nextLeadID;
    Dialog dialog;
    ProgressBar drPB;
    ExpandableHeightGridView dropDownLVID;
    CircleImageView leadPic;

    boolean otherSourceIsVisible = false;
    boolean otherActivityIsVisible = false;
    TextView ReasonTVID;
    LinearLayout otherFieldLLID;
    EditText otherFieldETID;

    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay, finalHour;
    MediaPlayer mPlayer;

    private Calendar calendar;
    private int year, month, day;

    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    String submitType="0";

    EditText noteETID,otherReasonETID;
    RelativeLayout reasonRLID,disableRLID;
    Button removeBtn;
    ArrayList<DisableResponse> disableResponses = new ArrayList<>();
    DeadReasonsAdapter deadReasonsAdapter;
    Spinner reasonsSPID;
    String reasonsStr,reasonID,viewType;

    StatusResponse statusResponse;
    TextView activitiesTVID,dashBoardTVID;
    LinearLayout activitiesLLID,dashboardLLID,skipLLID,nextCallLLID;
    String callerID;
    KProgressHUD kProgressHUD;
    CardView nextLeadCVID;
    ProgressBar pb;

    NotesAdapter notesAdapter;
    ArrayList<NotesResponse> notesResponses = new ArrayList<>();
    RecyclerView remarksRVID;
    String screenFrom;
    String nextAutoFollowupName,nextFollowupMobile,nextFollowupActID,nextFollowupLeadID,nextFollowupActName;
    AppCompatImageView mobileIVID,okIVID;
    CampaignCallResponse campaignResponse;
    RelativeLayout callBtnRLID;

    //Google Analytics
    private FirebaseAnalytics analytics;

    String callStartStr, callEndStr, callMinutesStr,callSecondsStr;

    Boolean skip = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_call_complete);

        Utilities.setStatusBarGradiant(this);

        analytics = FirebaseAnalytics.getInstance(this);

        HandlerUtility.insertHandler= uiHandler;

        if (getIntent() != null) {

            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {

                viewType = bundle.getString(AppConstants.VIEW_TYPE);
                screenFrom = bundle.getString("SCREEN_FROM");

                if (viewType != null && viewType.equals(AppConstants.YES)) {

                    activityID = bundle.getString("ACTIVITY_ID");
                    Log.e("activityID==>","" + bundle.getString("ACTIVITY_ID"));
                    actNameStr = bundle.getString("ACTIVITY_NAME");
                    leadID = bundle.getString("LEAD_ID");
                    callerID = bundle.getString("CALL_ID");
                    nextMobileStr = bundle.getString("CUSTOMER_MOBILE");
                    customerNameStr = bundle.getString("CUSTOMER_NAME");
                    presentCall = bundle.getString("CUSTOMER_MOBILE");



                }else {

                    activityID = bundle.getString("ACTIVITY_ID");

                    Log.e("activityID==>","" + bundle.getString("ACTIVITY_ID"));
                    actNameStr = bundle.getString("ACTIVITY_NAME");
                    leadID = bundle.getString("LEAD_ID");
                    nextMobileStr = bundle.getString("CUSTOMER_MOBILE");
                    callerID = bundle.getString("CALL_ID");
                    customerNameStr = bundle.getString("CUSTOMER_NAME");
                    presentCall = bundle.getString("CUSTOMER_MOBILE");


                    if (screenFrom != null && screenFrom.equals("AUTO_FOLLOWUP")) {


                        activityID = bundle.getString("ACTIVITY_ID");

                        Log.e("activityID==>","" + bundle.getString("ACTIVITY_ID"));
                        actNameStr = bundle.getString("ACTIVITY_NAME");
                        leadID = bundle.getString("LEAD_ID");
                        nextMobileStr = bundle.getString("CUSTOMER_MOBILE");
                        customerNameStr = bundle.getString("CUSTOMER_NAME");
                        presentCall = bundle.getString("CUSTOMER_MOBILE");
                    }

                }

            }
        }

        Log.e("CUSTOMER_MOBILE","" + presentCall);


        userID = MySharedPreferences.getPreferences(CallCompleteActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(CallCompleteActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        companyName = MySharedPreferences.getPreferences(CallCompleteActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_NAME);
        userType = MySharedPreferences.getPreferences(CallCompleteActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        activityTVID = findViewById(R.id.activityTVID);
        dateTVID = findViewById(R.id.dateTVID);
        okRLID = findViewById(R.id.okRLID);
        activityRLID = findViewById(R.id.activityRLID);
        dateRLID = findViewById(R.id.dateRLID);
        timeLLID = findViewById(R.id.timeLLID);
        timeTVID = findViewById(R.id.timeTVID);
        remarksAutoID = findViewById(R.id.remarksAutoID);
        micLID = findViewById(R.id.micLID);
        newLeadNameTVID = findViewById(R.id.newLeadNameTVID);
        leadMobileTVID = findViewById(R.id.leadMobileTVID);
        whatsAppCallRLID = findViewById(R.id.whatsAppCallRLID);
        newPhoneRL = findViewById(R.id.newPhoneRL);
        callLLID = findViewById(R.id.callLLID);
        cubeCallLLID = findViewById(R.id.cubeCallLLID);
        nextLeadRLID = findViewById(R.id.nextLeadRLID);
        leadPic = findViewById(R.id.leadPic);
        whatsAppRLID = findViewById(R.id.whatsAppRLID);
        whatsAppLLID = findViewById(R.id.whatsAppLLID);

        disableRLID = findViewById(R.id.disableRLID);
        activitiesTVID = findViewById(R.id.activitiesTVID);
        dashBoardTVID = findViewById(R.id.dashBoardTVID);
        activitiesLLID = findViewById(R.id.activitiesLLID);
        dashboardLLID = findViewById(R.id.dashboardLLID);
        skipLLID = findViewById(R.id.skipLLID);
        nextCallLLID = findViewById(R.id.nextCallLLID);

        nextLeadCVID = findViewById(R.id.nextLeadCVID);
        pb = findViewById(R.id.pb);
        remarksRVID = findViewById(R.id.remarksRVID);
        mobileIVID = findViewById(R.id.mobileIVID);
        okIVID = findViewById(R.id.okIVID);

        callBtnRLID = findViewById(R.id.callBtnRLID);
      /*  reasonTVID = findViewById(R.id.reasonTVID);*/
        ReasonTVID = findViewById(R.id.reasonTVID);

        loadRemarks();

        if (screenFrom != null && screenFrom.equals("AUTO_FOLLOWUP")) {
            disableRLID.setVisibility(View.GONE);
            whatsAppRLID.setVisibility(View.GONE);
            mobileIVID.setVisibility(View.GONE);
            callBtnRLID.setVisibility(View.GONE);
            okIVID.setVisibility(View.VISIBLE);
        }else {
            mobileIVID.setVisibility(View.VISIBLE);
            callBtnRLID.setVisibility(View.VISIBLE);
            okIVID.setVisibility(View.GONE);
        }

        nextCallLLID.setOnClickListener(v -> {

            String submitType = "8";
            String notesStr = remarksAutoID.getText().toString();
            if (Utilities.isNetworkAvailable(this)) {
                callCompleteResponse(submitType,notesStr,"0");
            } else {
                Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
            }
        });

        dashboardLLID.setOnClickListener(v -> {

            String submitType ="2";

            if (Utilities.isNetworkAvailable(this)){
                String notesStr = remarksAutoID.getText().toString();
                callCompleteResponse(submitType,notesStr,"0");

            }else {
                Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
            }

        });

        activitiesLLID.setOnClickListener(v -> {

            submitType ="5";

            if (Utilities.isNetworkAvailable(this)){

                MySharedPreferences.setPreference(CallCompleteActivity.this, "" + AppConstants.PAGE_REFRESH, "YES");
                String notesStr = remarksAutoID.getText().toString();
                callCompleteResponse(submitType,notesStr,"0");

            }else {
                Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
            }

        });


        activityTVID.setText(actNameStr);

        headerTittleTVID.setText(customerNameStr);

        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateTVID.setText("" + day + "-" + (month + 1) + "-" + year);
        dateStr = "" + day + "-" + (month + 1) + "-" + year;
        dateTVID.setText("" + day + "-" + (month + 1) + "-" + year);
        mDay = calendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        finalHour = mHour+1;
        //finalHour = mHour;

        mTime=timeTVID.getText().toString();

        date = dateTVID.getText().toString();*/

        // Initialize default values
        mActive = "true";
        mRepeat = "false";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        finalHour = mHour + 1;

        //mDate = mDay + "/" + mMonth + "/" + mYear;
        dateStr = mDay + "-" + mMonth + "-" + mYear;
        mTime = finalHour + ":" + mMinute;

        dateRLID.setOnClickListener(view -> {
            // Get Current Date
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");

           /* final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);*/

           /* android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this,
                    new android.app.DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                            monthOfYear++;
                            mDay = dayOfMonth;
                            mMonth = monthOfYear;
                            mYear = year;


                            dateStr = "" + day + "-" + (month + 1) + "-" + year;
                            mDate = "" + year + "-" + (monthOfYear) + "-" + dayOfMonth;
                            dateTVID.setText("" + dayOfMonth + "-" + (monthOfYear) + "-" + year);
                            Log.e("date==>","" + dateStr);


                           *//* mPlayer = MediaPlayer.create(CallCompleteActivity.this, R.raw.ringtone);
                            mPlayer.start();*//*
                        }

                    }, year, month, day);
            datePickerDialog.show();*/

        });

        if (mMinute < 10) {
            timeTVID.setText(finalHour + ":0" + mMinute);
        } else {
            timeTVID.setText(finalHour + ":" + mMinute);
        }

        timeLLID.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    CallCompleteActivity.this::onTimeSet,
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    false
            );
            tpd.setThemeDark(false);
            tpd.show(getFragmentManager(), "Timepickerdialog");
        });

        mActive = "true";
        mRepeat = "false";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);
        finalHour=mHour+1;
        //finalHour = mHour;
        mDate = mDay + "-" + mMonth + "-" + mYear;
        dateStr = mDay + "-" + mMonth + "-" + mYear;
        dateTVID.setText(dateStr);
        mTime = finalHour + ":" + mMinute;

        activityRLID.setOnClickListener(v -> {

            showActivitiesAlert("ACTIVITIES","1");

        });

        micLID.setOnClickListener(v -> {

            startVoiceInput();

        });

        okRLID.setOnClickListener(v -> {

            //callingAlert("1");


            if (screenFrom != null && screenFrom.equals("AUTO_FOLLOWUP")) {

                String submitType ="1";
                String notesStr = remarksAutoID.getText().toString();

               // callCompleteResponse(submitType,notesStr,"1");
                getCallerID("1","1");
                loadNextAutoCall();
                repeatCalling();

            }else {

                String submitType ="1";
                String notesStr = remarksAutoID.getText().toString();

                callCompleteResponse(submitType,notesStr,"0");
                showNextCall();
                repeatCalling();
            }

        });

        if (Utilities.isNetworkAvailable(this)){

            if (screenFrom != null && screenFrom.equals("AUTO_FOLLOWUP")) {

                loadNextAutoCall();

            }else {
                showNextCall();
            }


        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }

        if (viewType != null && viewType.equalsIgnoreCase(AppConstants.YES)){

        }


        callLLID.setOnClickListener(v -> {

           // callingAlert("2");

            String notesStr = remarksAutoID.getText().toString();
            String submitType = "6";
            if (Utilities.isNetworkAvailable(this)) {
                callCompleteResponse(submitType,notesStr,"0");
            } else {
                Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
            }

            nextCalling();


        });

        cubeCallLLID.setOnClickListener(v -> {

            // callingAlert("2");

            String notesStr = remarksAutoID.getText().toString();
            String submitType = "cubeCall";
            if (Utilities.isNetworkAvailable(this)) {
                callCompleteResponse(submitType,notesStr,"0");
            } else {
                Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
            }

            getCallerID("cubeCall","5");
            getEmployeeCallCreatives(nextLeadID);



        });

        skipLLID.setOnClickListener(v -> {
            //nextCalling();
            String notesStr = remarksAutoID.getText().toString();
            String submitType = "7";
            if (Utilities.isNetworkAvailable(this)) {
                callCompleteResponse(submitType,notesStr,"0");
            } else {
                Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
            }
        });

        whatsAppRLID.setOnClickListener(v -> {

            String notesStr = remarksAutoID.getText().toString();
            String submitType = "3";
            callCompleteResponse(submitType,notesStr,"0");

        });

        whatsAppLLID.setOnClickListener(v -> {

            String notesStr = remarksAutoID.getText().toString();
            String submitType = "3";
            if (Utilities.isNetworkAvailable(this)) {
                callCompleteResponse(submitType,notesStr,"0");
            } else {
                Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
            }

        });

        whatsAppCallRLID.setOnClickListener(v -> {
            nextWhatsapp();

        });

        headerTittleTVID.setOnClickListener(v -> {
            String notesStr = remarksAutoID.getText().toString();
            String submitType = "4";
            if (Utilities.isNetworkAvailable(this)) {
                callCompleteResponse(submitType,notesStr,"0");
            } else {
                Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
            }

        });

        disableRLID.setOnClickListener(v -> {

            final Dialog disableDialog = new Dialog(CallCompleteActivity.this);
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
            reasonTVID=disableDialog.findViewById(R.id.reasonTVID);
            reasonRLID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showActivitiesAlert("DEADREASON","1");
                }
            });
            reasonsSPID = (Spinner)disableDialog.findViewById(R.id.reasonsSPID);

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

            removeBtn.setOnClickListener(v1 -> {
                validations(disableDialog,noteETID,otherReasonETID,reasonsStr);
            });

        });


    }

    /*@Override
    protected void onResume() {
        super.onResume();

        *//*Intent intent = getIntent();
        callMinutesStr = intent.getStringExtra("minutes");
        callSecondsStr = intent.getStringExtra("seconds");
        callStartStr = intent.getStringExtra("start_time");
        callEndStr = intent.getStringExtra("end_time");

        Log.e("CALL_DETAILS","" + callStartStr+", " +callEndStr+", " +callMinutesStr+", " +callSecondsStr);*//*



    }*/

    private void loadNextAutoCall() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<AutoFollowupResponse> call = apiInterface.getNextAutoFollowupCall(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<AutoFollowupResponse>() {
            @Override
            public void onResponse(Call<AutoFollowupResponse> call, Response<AutoFollowupResponse> response) {

                if (response.body() != null && response.code() == 200){

                    AutoFollowupResponse autoFollowupResponses = response.body();

                    nextAutoFollowupName = autoFollowupResponses.getCustomer_name();
                    nextFollowupMobile = autoFollowupResponses.getMobile_number();
                    nextFollowupActID = autoFollowupResponses.getActivity_id();
                    nextFollowupLeadID = autoFollowupResponses.getLead_id();
                    nextFollowupActName = autoFollowupResponses.getActivity_name();

                }
            }

            @Override
            public void onFailure(Call<AutoFollowupResponse> call, Throwable t) {

            }
        });

    }


    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 10:
                   try {
                       JSONObject obj = (JSONObject) msg.obj;
                       Log.e(tag,"obj==> "+obj.toString());
                       if (callEndStr != null) {
                           Log.e("error==>",""+callEndStr);
                           Log.e(tag,"callEndStr==> "+callEndStr);
                       } else {
                           callMinutesStr = obj.getString("minutes");
                           callSecondsStr = obj.getString("seconds");
                           callStartStr = obj.getString("start_time");
                           callEndStr = obj.getString("end_time");
                           /*Date currentDate = new Date (callEndStr);
                           SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                           String date = dateFormat.format(currentDate);*/
                           Log.e(tag,"Caller_Details==> "+callMinutesStr+", " + callSecondsStr+", " + callStartStr+", " + callEndStr);
                           Log.e("Caller_Details==>",""+callMinutesStr+", " + callSecondsStr+", " + callStartStr+", " + callEndStr);
                           callCompleteResponse("0","","0");
                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                       Log.e(tag,"JSONException==> "+e.getMessage()+", " + callSecondsStr+", " + callStartStr+", " + callEndStr);

                   }
                   break;
           }
        }
    };


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

    private void loadRemarks() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<NotesResponse>> call = apiInterface.getNotesShow(leadID,userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NotesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NotesResponse>> call, Response<ArrayList<NotesResponse>> response) {
                if (response.body() != null && response.code() == 200){
                    notesResponses = response.body();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CallCompleteActivity.this, LinearLayoutManager.VERTICAL, false);
                    notesAdapter = new NotesAdapter(notesResponses,CallCompleteActivity.this);
                    remarksRVID.setLayoutManager(layoutManager);
                    remarksRVID.setAdapter(notesAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotesResponse>> call, Throwable t) {

            }
        });
    }

    private void deadLead(Dialog disableDialog, String notesStr, String otherNotesStr, String reasonsStr) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.getDeadLead(leadID,userID,reasonsStr,notesStr,otherNotesStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.body() != null && response.code() == 200){
                    statusResponse = response.body();

                    if (statusResponse.getStatus().equalsIgnoreCase("1")){
                        finish();
                        Toast.makeText(CallCompleteActivity.this, "Lead Removes", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(CallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                Toast.makeText(CallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        disableDialog.dismiss();


    }


    private void deadReasons(Spinner reasonsSPID) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DisableResponse>> call = apiInterface.getAllDeadReasons();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<DisableResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DisableResponse>> call, Response<ArrayList<DisableResponse>> response) {

                if (response.body() != null && response.code() == 200){
                    disableResponses = response.body();
                    deadReasonsAdapter = new DeadReasonsAdapter(CallCompleteActivity.this,R.layout.custom_spinner_view,disableResponses);
                    reasonsSPID.setAdapter(deadReasonsAdapter);
                }else {
                    Toast.makeText(CallCompleteActivity.this, "Reasons Not Loading", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DisableResponse>> call, Throwable t) {
                Toast.makeText(CallCompleteActivity.this, "Reasons Not Loading", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        int finalHour = mHour;
        if (minute < 10) {
            mTime = finalHour + ":" + "0" + minute;
        } else {
            mTime = finalHour + ":" + minute;
        }

        Log.e("time==>","" + mTime);

        timeTVID.setText(mTime);
        mTime=timeTVID.getText().toString();
        //completeResponse();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        monthOfYear++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;

        dateStr = "" + year + "-" + (monthOfYear ) + "-" + dayOfMonth;
        dateTVID.setText("" + dayOfMonth + "-" + (monthOfYear ) + "-" + year);

        //mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDate = mDay + "-"  + (mMonth) + "-"  + mYear;
        //scheduledActivityDateID.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
        //dateTVID.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year);
        //dateStr = dateTVID.getText().toString().replace(" ", "%20");
        // dateStr = "" + day + "-" + (month + 1) + "-" + year;
        Log.e("date==>","" + dateStr);
        // dateTVID.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year);


        mPlayer = MediaPlayer.create(CallCompleteActivity.this, R.raw.ringtone);
        mPlayer.start();
    }

    private void loadWhatsAppTemplates() {
        Intent intentW = new Intent(CallCompleteActivity.this, SendTemplatesActivity.class);
        intentW.putExtra("TYPE", "Send Whats App");
        intentW.putExtra("LEAD_MOBILE_NUMBER", "" + presentCall );
        intentW.putExtra("CUSTOMER_NAME", "" + customerNameStr);
        //intentW.putExtra("LEAD_EMAIL", "" + emailStr);
        intentW.putExtra("LEAD_ID", "" + leadID);
        startActivity(intentW);


    }

    private void showNextCall() {

        pb.setVisibility(View.VISIBLE);
        nextLeadCVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<NextCallResponse>> call = apiInterface.nextCall(userID,leadID,activityID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<NextCallResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<NextCallResponse>> call, Response<ArrayList<NextCallResponse>> response) {

                pb.setVisibility(View.GONE);
                nextLeadCVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    nextCallResponses = response.body();

                    for (int i = 0; i< nextCallResponses.size(); i++){

                        try {
                            Picasso.with(CallCompleteActivity.this).load(nextCallResponses.get(i).getLead_pic()).error(R.drawable.user).error(R.drawable.user).placeholder(R.drawable.user).rotate(0).into(leadPic);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        newLeadNameTVID.setText(nextCallResponses.get(i).getLead_name());
                        //leadMobileTVID.setText(nextCallResponses.get(i).getActivity_name());

                        if (nextCallResponses.get(i).getCompany_name().isEmpty()){
                            leadMobileTVID.setText("No Company");
                        }else {
                            leadMobileTVID.setText(nextCallResponses.get(i).getCompany_name());
                        }


                        nextMobileStr = nextCallResponses.get(i).getMobile_number();
                        nextActID = nextCallResponses.get(i).getActivity_id();
                        nextActName = nextCallResponses.get(i).getActivity_name();
                        nextLeadName = nextCallResponses.get(i).getLead_name();
                        nextLeadID = nextCallResponses.get(i).getLead_id();
                    }
                }else {

                    Toast.makeText(CallCompleteActivity.this, "Next Customer not loaded", Toast.LENGTH_SHORT).show();
                    nextLeadRLID.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    nextLeadCVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NextCallResponse>> call, Throwable t) {
                Toast.makeText(CallCompleteActivity.this, "Next Customer not loaded", Toast.LENGTH_SHORT).show();
                nextLeadRLID.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                nextLeadCVID.setVisibility(View.GONE);
            }
        });
    }

    private void nextWhatsapp() {
        final BottomSheetDialog dialog1 = new BottomSheetDialog(CallCompleteActivity.this);
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
                String url = "https://api.whatsapp.com/send?phone=" + "+91" + nextMobileStr + "&text=" + URLEncoder.encode("", "UTF-8");
                sendMsg.setPackage("com.whatsapp");
                sendMsg.setData(Uri.parse(url));
                startActivity(sendMsg);
                finish();
                dialog1.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                /*Utilities.showToast(this,e.getMessage());*/
                Toast.makeText(CallCompleteActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
            }

            String notesStr = remarksAutoID.getText().toString();

            callCompleteResponse(submitType,notesStr,"0");

        });

        businessWhatsappIVID.setOnClickListener(view2 -> {

            try {

                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=" + "+91" + nextMobileStr + "&text=" + URLEncoder.encode("", "UTF-8");
                sendMsg.setPackage("com.whatsapp.w4b");
                sendMsg.setData(Uri.parse(url));

                startActivity(sendMsg);
                finish();
                dialog1.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                /*Utilities.showToast(this,e.getMessage());*/
                Toast.makeText(CallCompleteActivity.this, "You don't have business WhatsApp in your device", Toast.LENGTH_SHORT).show();
            }

            String notesStr = remarksAutoID.getText().toString();

            callCompleteResponse(submitType,notesStr,"0");

            dialog1.dismiss();

        });
    }


    private void nextCalling() {

        getCallerID("2","1");
        getEmployeeCallCreatives(nextLeadID);

    }

    private void repeatCalling() {

        if (screenFrom != null && screenFrom.equals("AUTO_FOLLOWUP")) {

            try {
                if (nextFollowupMobile.isEmpty()){

                    finish();


                }else {

                    getCallerID("4","1");
                    getEmployeeCallCreatives(nextFollowupLeadID);

                    //CommunicationsServices.InsertCommunication(CallCompleteActivity.this, "1", leadID, userID, "", "");



                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //getCampaignCall();


        } else {

            getCallerID("1","1");
            getEmployeeCallCreatives(leadID);

        }

    }

    private void getCampaignCall() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<CampaignCallResponse> call = apiInterface.getCampaignCall(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<CampaignCallResponse>() {
            @Override
            public void onResponse(Call<CampaignCallResponse> call, Response<CampaignCallResponse> response) {

                if (response.body() != null && response.code() == 200){

                    campaignResponse = response.body();

                    if (campaignResponse.getStatus().equals(true)) {

                        Toast.makeText(CallCompleteActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(CallCompleteActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }else {

                    Toast.makeText(CallCompleteActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CampaignCallResponse> call, Throwable t) {

                Log.e("Status==>","" + "Failed");

                Toast.makeText(CallCompleteActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void callingAlert(String type) {

        final Dialog dialog = new Dialog(CallCompleteActivity.this);
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

            if (type.equalsIgnoreCase("1")){



                if (Utilities.isNetworkAvailable(this)){


                }else {
                    Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
                }


            }

            if (type.equalsIgnoreCase("2")){




            }

            dialog.dismiss();


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

        });

        knowlarityCallBtn.setOnClickListener(v -> {

            if (type.equalsIgnoreCase("1")) {
                CustomKnowlarityApis.clickToKnowlarityCall(CallCompleteActivity.this,userID,presentCall,kProgressHUD);

                String submitType ="1";

                notesStr = remarksAutoID.getText().toString();

                if (Utilities.isNetworkAvailable(this)){
                    String notesStr = remarksAutoID.getText().toString();
                    callCompleteResponse(submitType,notesStr,"0");
                    showNextCall();
                    //nextCalling();
                    repeatCalling();
                }else {
                    Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
                }
            }

            if (type.equalsIgnoreCase("2")){

                String notesStr = remarksAutoID.getText().toString();
                String submitType = "6";
                if (Utilities.isNetworkAvailable(this)) {
                    callCompleteResponse(submitType,notesStr,"0");
                } else {
                    Toast.makeText(this, "No internet please check your network", Toast.LENGTH_SHORT).show();
                }


                CustomKnowlarityApis.clickToKnowlarityCall(CallCompleteActivity.this,userID,nextMobileStr,kProgressHUD);

                nextCalling();



            }

            dialog.dismiss();

        });

    }

    private void numberCalling(String number) {

      /*  try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }
             //   Uri uri = Uri.parse("tel:" + phoneNumber);
             //   callIntent.setData(Uri.parse("tel:" + number));
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



    private void startVoiceInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    remarksAutoID.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void getEmployeeCallCreatives(String leadIDStr) {

        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<EmployeeCallCreativesResponse> call = apiInterface.getEmployeeCallCreatives(leadIDStr,userID);
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


    private void callCompleteResponse(String submitType,String notesStr,String campaignCall) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.callCompleted(userID,activityID,leadID,notesStr,dateStr,mTime,callerID,campaignCall,callStartStr,callEndStr);
        Log.e("Caller_Details==>",""+callMinutesStr+", " + callSecondsStr+", " + callStartStr+", " + callEndStr);

        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                if (response.body() != null && response.code() == 200){
                    StatusResponse statusResponse = response.body();
                    if (statusResponse.getStatus().equalsIgnoreCase("1")){



                        if (submitType.equalsIgnoreCase("1")){

                           /*Intent intent = new Intent(CallCompleteActivity.this,NextCallActivity.class);
                            intent.putExtra("ACTIVITY_NAME",actNameStr);
                            intent.putExtra("ACTIVITY_ID",activityID);
                            intent.putExtra("CUSTOMER_NAME",customerNameStr);
                            intent.putExtra("LEAD_ID",leadID);
                            startActivity(intent);*/

                            finish();
                        }

                        if (submitType.equalsIgnoreCase("2")){


                            Intent intent = new Intent(CallCompleteActivity.this,DashBoardActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        if (submitType.equalsIgnoreCase("3")){


                           /* Intent intent = new Intent(CallCompleteActivity.this,CallCompleteActivity.class);
                            intent.putExtra("ACTIVITY_NAME",nextActName);
                            intent.putExtra("ACTIVITY_ID",nextActID);
                            intent.putExtra("CUSTOMER_NAME",nextLeadName);
                            intent.putExtra("LEAD_ID",nextLeadID);
                            intent.putExtra("" + AppConstants.VIEW_TYPE, AppConstants.YES);
                            startActivity(intent);*/
                            finish();

                            loadWhatsAppTemplates();

                        }

                        if (submitType.equalsIgnoreCase("4")){

                            Intent btdelsale = new Intent(new Intent(CallCompleteActivity.this, LeadHistoryActivity.class));
                            btdelsale.putExtra("CUSTOMER_ID", leadID);
                            btdelsale.putExtra("CUSTOMER_NAME", customerNameStr);
                            btdelsale.putExtra("CUSTOMER_MOBILE", nextMobileStr);
                            btdelsale.putExtra("ACTIVITY_NAME",actNameStr);
                            btdelsale.putExtra("ACTIVITY_ID",activityID);
                            btdelsale.putExtra("pageFrom", false);
                            startActivity(btdelsale);
                            finish();

                        }

                        if (submitType.equalsIgnoreCase("5")){

                            finish();

                        }

                        if (submitType.equalsIgnoreCase("6")){

                            finish();

                        }

                        if (submitType.equalsIgnoreCase("cubeCall")){

                            finish();

                        }

                        if (submitType.equalsIgnoreCase("7")){

                            Boolean skip = true;

                            getCallerID("3","2");

                        }

                        if (submitType.equalsIgnoreCase("8")) {
                            Intent btdelsale = new Intent(new Intent(CallCompleteActivity.this, LeadHistoryActivity.class));
                            btdelsale.putExtra("CUSTOMER_ID", nextLeadID);
                            btdelsale.putExtra("CUSTOMER_NAME", nextLeadName);
                            btdelsale.putExtra("CUSTOMER_MOBILE", nextMobileStr);
                            btdelsale.putExtra("ACTIVITY_NAME",nextActName);
                            btdelsale.putExtra("ACTIVITY_ID",nextActID);
                            btdelsale.putExtra("pageFrom", false);
                            startActivity(btdelsale);
                            finish();
                        } else {

                        }

                        //Toast.makeText(CallCompleteActivity.this, "Successfully Submitted", Toast.LENGTH_SHORT).show();

                        //finish();
                    }else {
                        Toast.makeText(CallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Toast.makeText(CallCompleteActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCallerID(String type, String communications) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(leadID,"","",communications,userID,skip);
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

                                    Intent intent = new Intent(CallCompleteActivity.this,CallCompleteActivity.class);
                                    intent.putExtra("ACTIVITY_NAME",actNameStr);
                                    intent.putExtra("ACTIVITY_ID",activityID);
                                    intent.putExtra("CUSTOMER_NAME",customerNameStr);
                                    intent.putExtra("CUSTOMER_MOBILE",presentCall);
                                    intent.putExtra("LEAD_ID",leadID);
                                    intent.putExtra("CALL_ID", statusResponses.get(i).getCall_id());

                                    startActivity(intent);

                                    numberCalling(presentCall);

                                }else if (type.equalsIgnoreCase("2")){

                                    Intent intent = new Intent(CallCompleteActivity.this,CallCompleteActivity.class);
                                    intent.putExtra("ACTIVITY_NAME",nextActName);
                                    intent.putExtra("ACTIVITY_ID",nextActID);
                                    intent.putExtra("CUSTOMER_NAME",nextLeadName);
                                    intent.putExtra("LEAD_ID",nextLeadID);
                                    intent.putExtra("CUSTOMER_MOBILE",nextMobileStr);
                                    intent.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    intent.putExtra("" + AppConstants.VIEW_TYPE, AppConstants.YES);
                                    startActivity(intent);

                                    numberCalling(nextMobileStr);

                                }else if (type.equalsIgnoreCase("3")){

                                    Intent intent = new Intent(CallCompleteActivity.this,CallCompleteActivity.class);
                                    intent.putExtra("ACTIVITY_NAME",nextActName);
                                    intent.putExtra("ACTIVITY_ID",nextActID);
                                    intent.putExtra("CUSTOMER_NAME",nextLeadName);
                                    intent.putExtra("CUSTOMER_MOBILE",nextMobileStr);
                                    intent.putExtra("LEAD_ID",nextLeadID);
                                    intent.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    intent.putExtra("" + AppConstants.VIEW_TYPE, AppConstants.YES);
                                    startActivity(intent);
                                    finish();

                                }else if (type.equalsIgnoreCase("4")){

                                    Intent intent = new Intent(CallCompleteActivity.this,CallCompleteActivity.class);
                                    intent.putExtra("ACTIVITY_NAME",nextFollowupActName);
                                    intent.putExtra("ACTIVITY_ID",nextFollowupActID);
                                    intent.putExtra("CUSTOMER_NAME",nextAutoFollowupName);
                                    intent.putExtra("CUSTOMER_MOBILE",nextFollowupMobile);
                                    intent.putExtra("SCREEN_FROM","AUTO_FOLLOWUP");
                                    intent.putExtra("LEAD_ID",nextFollowupLeadID);
                                    intent.putExtra("CALL_ID", statusResponses.get(i).getCall_id());
                                    startActivity(intent);
                                    finish();
                                }




                                Log.e("caller_id","" + statusResponses.get(i).getCall_id());


                                Log.d("INSERT_MESSAGE", "" + type + " : Inserted");

                            } else {

                                Utilities.AlertDaiolog(CallCompleteActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            }
                        }
                    } else {
                        Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                        Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                    }
                } else {

                    Utilities.AlertDaiolog(CallCompleteActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");

                    Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                    Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {
                Utilities.AlertDaiolog(CallCompleteActivity.this, getString(R.string.alert), "Caller id issue please try to make call again", 1, null, "OK");
                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
            }
        });
    }
    AppCompatTextView reasonTVID;
    @SuppressLint("ClickableViewAccessibility")
    private void showActivitiesAlert(final  String type,String alertType) {

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


        if (Utilities.isNetworkAvailable(this)) {

            if (type.equalsIgnoreCase("ACTIVITIES")) {

                activities();
            }else if(type.equalsIgnoreCase("DEADREASON")){

                deadReasons2();
            }


        } else {
            AlertUtilities.bottomDisplayStaticAlert(this, "No Internet connection", "MAke sure your device is connected to internet");
        }

        dropDownLVID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> requirementAdapterView, View view, int i, long l) {
                dialog.dismiss();

            //    activityTVID.setText(activityMainResponses.get(i).getActivity_name());

                if(type.equalsIgnoreCase("ACTIVITIES")){
                    activityTVID.setText(activityMainResponses.get(i).getActivity_name());

                }else if(type.equalsIgnoreCase("DEADREASON")) {
                    reasonsStr = disableResponses.get(i).getDead_reason();
                    reasonTVID.setText(disableResponses.get(i).getDead_reason());

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

                String te = searchHintETID.getText().toString();
                activitiesAdapter.getFilter().filter(te);

            }
        });

    }

    private void deadReasons2(){

        drPB.setVisibility(View.VISIBLE);
        ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DisableResponse>> call = apiInterface.getAllDeadReasons();
        Log.e("api=new=>",call.request().toString());
        call.enqueue(new Callback<ArrayList<DisableResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DisableResponse>> call, Response<ArrayList<DisableResponse>> response) {
                drPB.setVisibility(View.GONE);
                if(response.body()!=null&& response.code()==200){
                    disableResponses = response.body();
                    Log.e("disableResponses==>",disableResponses.size()+" ");
                    if(disableResponses!=null && disableResponses.size()>0){
                        deadReasonNewAdapter = new DeadReasonNewAdapter(CallCompleteActivity.this,disableResponses);
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

    private void activities() {
        drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<ActivityMainResponse>> call = apiInterface.getActivities(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ActivityMainResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivityMainResponse>> call, Response<ArrayList<ActivityMainResponse>> response) {
                drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    activityMainResponses = response.body();
                    if (activityMainResponses != null && activityMainResponses.size() > 0) {
                        for (int i = 0; i < activityMainResponses.size(); i++) {
                            activitiesAdapter = new ActivitiesNewAdapter(CallCompleteActivity.this, activityMainResponses);
                           dropDownLVID.setAdapter(activitiesAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ActivityMainResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(CallCompleteActivity.this, "Activities not loading", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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
            this.data = data;
            mStringFilterList = data;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
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
            String reason=  data.get(position).getDead_reason();
            Log.e("getView==>",reason+" , "+position);
            if (convertView == null) {
                holder = new DeadReasonNewAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.tname.setText(reason);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            }else{
                holder = (DeadReasonNewAdapter.ViewHolder) convertView.getTag();
                holder.tname.setText(data.get(position).getDead_reason());
                holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
                GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
                bgShape.setColor(Color.parseColor(mColors[position % 40]));
                holder.tname.setText(reason);
                holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        // reasonsStr=disableResponses.get(position).getReason_id();
                        reasonTVID.setText(data.get(position).getDead_reason());
                        Log.e("data==>", data.get(position).getDead_reason());

                    }
                });


            }



            return convertView;
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
                data = (ArrayList<DisableResponse>) results.values;
                notifyDataSetChanged();
            }
        }
    }







    public class ActivitiesNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<ActivityMainResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private ActivitiesNewAdapter.ValueFilter valueFilter;
        private ArrayList<ActivityMainResponse> mStringFilterList;

        public ActivitiesNewAdapter(Activity context, ArrayList<ActivityMainResponse> _Contacts) {
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

                    activityID = _Contacts.get(position).getActivity_id();
                    dialog.dismiss();
                    activityTVID.setText(_Contacts.get(position).getActivity_name());

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
}