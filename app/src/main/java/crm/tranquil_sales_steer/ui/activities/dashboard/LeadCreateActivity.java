package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.PhoneAccount;
import android.telecom.TelecomManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.ActivitiesAdapterNew;
import crm.tranquil_sales_steer.ui.adapters.RequirementsAdapter;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.CampaignCallResponse;
import crm.tranquil_sales_steer.ui.models.CreateLeadResponse;
import crm.tranquil_sales_steer.ui.models.RequirementResponse;
import crm.tranquil_sales_steer.ui.models.SourceResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;

public class LeadCreateActivity extends AppCompatActivity {

    TextInputEditText firstNameETID, lastNameETID, emailETID, mobileETID, alternateETID,
            websiteETID, locationETID,cityETID ,companyETID, gstETID, sourceOtherETID,notesETID,
            activityOtherETID;

    LinearLayout mainView;
    ArrayList<RequirementResponse> requirementResponses = new ArrayList<>();
    //RequirementNewAdapter requirementAdapter;
    RequirementAdapter requirementAdapter;

    ArrayList<SourceResponse> sourceResponses = new ArrayList<>();
    SourceNewAdapter sAdapter;

    ArrayList<ActivityMainResponse> activityMainResponses = new ArrayList<>();
    ActivitiesNewAdapter activitiesAdapter;

    ActivitiesAdapterNew activitiesAdapterNew;


    private KProgressHUD hud;
    String reqStr, sourceStr, activityStr, createdStr;
    Calendar calendar;
    int date, year, month;
    String companyID, userID, firstNameStr, lastNameStr, emailStr, mobileStr, alternateStr,
            websiteStr, locationStr,cityStr,notesStr, companyStr, gstStr, requirementOtherStr, sourceOtherStr,
            activityOtherStr, dateStr;

    LinearLayout sourceOtherLLID, activityOtherLLID;
    TextView dateTVID;


    RelativeLayout choosePicRLID;
    private ImageView leadPic;

    private String selectImagePath = "null";
    Uri selectImageUri;

    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_STORAGE = 2;
    Dialog dialog;
    ExpandableHeightGridView dropDownGVID;
    ExpandableHeightGridView dropDownLVID;
    TextView requirementTVID, sourceTVID, activityTVID, newLeadNameTVID,leadMobileTVID;
    CardView nextLeadCVID;
    //LinearLayout actRLID, souRLID, reqRLID;
    RelativeLayout actRLID, souRLID, reqRLID,dateLLID;
    String num, selectedRequirementArrays, selectedRequirementNameArrays;

    ProgressBar rePB, drPB;
    RelativeLayout otherReqID;
    boolean otherReqIsVisible = false;
    EditText otherReqETID;
    LinearLayout otherFieldLLID;
    EditText otherFieldETID;
    boolean otherSourceIsVisible = false;
    boolean otherActivityIsVisible = false;
    RelativeLayout okRLID, okDisRLID;
    public static final int REQUEST_IMAGE_CAPTURE_USER = 0;
    public static String fileName;

    RelativeLayout altNumberRLID,emailRLID,websiteRLID,locationRLID,cityRLID,notesRLID,companyRLID,gstRLID,leadPicRLIDMain;

    Button longFormLLID,shortFormLLID;

    View lastNameView,emailView,altNumberView,websiteView,locationView,cityView,companyView,gstView;
    String mobile,name,screenFrom,incoming,incomingNumber;
    String teleName,teleCallerID,teleSourceID,teleAssignedTo,teleMobile,callerID;
    CheckBox smsCBID,emailCBID,whatsAppCBID,voiceCallCBID;
    String sms,email,whatsApp,voiceCall,communications;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    AppCompatSpinner activitiesSPID,requirementSPID;
    CampaignCallResponse campaignResponse;
    WebView webView;

    private FirebaseAnalytics analytics;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_lead_new);
        Utilities.setStatusBarGradiant(this);

        analytics = FirebaseAnalytics.getInstance(this);
        webView=findViewById(R.id.webweview);
        userID = MySharedPreferences.getPreferences(LeadCreateActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(LeadCreateActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        webView=findViewById(R.id.Createleadweview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

      //  callerID="0";


     /*  WebView.webSettings().setJavaScriptEnabled(true);*/




      /*  webView.loadUrl("https://tranquilcrmone.com/mobileapp/createleadwebview?user_id="+userID+"&tele_cust_id="+"0");*/


  /*      if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
              *//*  topicid=bundle.getString("TOPICID");*//*


            }
        }
*/

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                screenFrom = bundle.getString("TeleCallLead");
                incoming = bundle.getString("INCOMING");
                incomingNumber = bundle.getString("NUMBER");
                mobile = bundle.getString("Mobile");
                callerID = bundle.getString("CALLER_ID");
                name = bundle.getString("Name");

                if (screenFrom != null && screenFrom.equalsIgnoreCase("YES") ) {

                    teleName = bundle.getString("TELE_NAME");
                    teleCallerID = bundle.getString("TELE_CALLER_ID");
                    teleSourceID = bundle.getString("TELE_SOURCE_ID");
                    callerID = bundle.getString("CALLER_ID");

                    teleAssignedTo = bundle.getString("TELE_ASSIGNED_TO");
                    teleMobile = bundle.getString("TELE_MOBILE");

                } else {

                }
            }
        }

        Log.e("TELE_MOBILE==>", ""+ teleMobile);
        Log.e("TELE_NAME==>", ""+ teleName);
        Log.e("TELE_CALLER_ID==>", ""+ teleCallerID);
        Log.e("TELE_SOURCE_ID==>", ""+ teleSourceID);
        Log.e("TELE_ASSIGNED_TO==>", ""+ teleAssignedTo);
        Log.e("TELE_callerID==>", ""+  callerID);



    /*    if(callerID.isEmpty()){
            callerID="0";
        }else{

        }
*/


        webView.loadUrl("https://tranquilcrmone.com/mobileapp/createleadwebview?user_id="+userID+"&tele_cust_id="+callerID);
        Log.e("webview", ""+webView);




        //setContentView(R.layout.activity_create_list);
        //statusBarUtilities.statusBarSetup(this);

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Create Lead");
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });
        okRLID = findViewById(R.id.okRLID);
        okDisRLID = findViewById(R.id.okDisRLID);

        okDisRLID.setVisibility(View.GONE);
        okRLID.setVisibility(View.VISIBLE);



        Log.d("COMPANY_ID : ", "" + companyID);

        lastNameView = findViewById(R.id.lastNameView);
        emailView = findViewById(R.id.emailView);
        altNumberView = findViewById(R.id.altNumberView);
        websiteView = findViewById(R.id.websiteView);
        locationView = findViewById(R.id.locationView);
        cityView = findViewById(R.id.cityView);
        companyView = findViewById(R.id.companyView);
        gstView = findViewById(R.id.gstView);


        mainView = findViewById(R.id.mainView);
        firstNameETID = findViewById(R.id.firstNameETID);
        lastNameETID = findViewById(R.id.lastNameETID);
        emailETID = findViewById(R.id.emailETID);
        mobileETID = findViewById(R.id.mobileETID);
        alternateETID = findViewById(R.id.alternateETID);
        websiteETID = findViewById(R.id.websiteETID);
        locationETID = findViewById(R.id.locationETID);
        cityETID = findViewById(R.id.cityETID);
        notesETID = findViewById(R.id.notesETID);
        companyETID = findViewById(R.id.companyETID);
        gstETID = findViewById(R.id.gstETID);
        sourceOtherETID = findViewById(R.id.sourceOtherETID);
        activityOtherETID = findViewById(R.id.activityOtherETID);

        altNumberRLID = findViewById(R.id.altNumberRLID);
        emailRLID = findViewById(R.id.emailRLID);
        websiteRLID = findViewById(R.id.websiteRLID);
        locationRLID = findViewById(R.id.locationRLID);
        cityRLID = findViewById(R.id.cityRLID);
        notesRLID = findViewById(R.id.notesRLID);
        companyRLID = findViewById(R.id.companyRLID);
        gstRLID = findViewById(R.id.gstRLID);
        leadPicRLIDMain = findViewById(R.id.leadPicRLIDMain);

        shortFormLLID = findViewById(R.id.shortFormLLID);
        longFormLLID = findViewById(R.id.longFormLLID);

        smsCBID = findViewById(R.id.smsCBID);
        emailCBID = findViewById(R.id.emailCBID);
        whatsAppCBID = findViewById(R.id.whatsAppCBID);
        voiceCallCBID = findViewById(R.id.voiceCallCBID);

       /* voiceCallCBID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                voiceCallCBID.isChecked();
            }
        });*/

        activities();
        requirements();

        activitiesSPID = findViewById(R.id.activitiesSPID);
        requirementSPID = findViewById(R.id.requirementSPID);

        longFormLLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*if (Utilities.isNetworkAvailable(LeadCreateActivity.this)) {

                    AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "Opening Full Form....", "Please Wait....");

                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "No Internet Connection...", "Make sure your device is connected to internet");
                }*/
                //Toast.makeText(LeadCreateActivity.this, "Opening Full Form", Toast.LENGTH_SHORT).show();
                shortFormLLID.setVisibility(View.VISIBLE);
                altNumberRLID.setVisibility(View.VISIBLE);
                emailRLID.setVisibility(View.VISIBLE);
                websiteRLID.setVisibility(View.GONE);
                locationRLID.setVisibility(View.VISIBLE);
                cityRLID.setVisibility(View.VISIBLE);
                notesRLID.setVisibility(View.VISIBLE);
                companyRLID.setVisibility(View.VISIBLE);
                gstRLID.setVisibility(View.GONE);
                leadPicRLIDMain.setVisibility(View.VISIBLE);
                longFormLLID.setVisibility(View.GONE);
                altNumberView.setVisibility(View.GONE);
                companyView.setVisibility(View.VISIBLE);
                emailView.setVisibility(View.GONE);
                gstView.setVisibility(View.GONE);
                lastNameView.setVisibility(View.GONE);
                locationView.setVisibility(View.GONE);
                cityView.setVisibility(View.GONE);
                websiteView.setVisibility(View.GONE);
            }
        });

        shortFormLLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shortFormLLID.setVisibility(View.GONE);
                altNumberRLID.setVisibility(View.GONE);
                emailRLID.setVisibility(View.GONE);
                websiteRLID.setVisibility(View.GONE);
                locationRLID.setVisibility(View.GONE);
                cityRLID.setVisibility(View.GONE);
                notesRLID.setVisibility(View.GONE);
                companyRLID.setVisibility(View.GONE);
                gstRLID.setVisibility(View.GONE);
                leadPicRLIDMain.setVisibility(View.GONE);
                longFormLLID.setVisibility(View.VISIBLE);
            }
        });



       /* if (!num.equals("")) {
            mobileETID.setText(num);
        }*/
        actRLID = findViewById(R.id.actRLID);
        souRLID = findViewById(R.id.souRLID);
        reqRLID = findViewById(R.id.reqRLID);

       /* actRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivitiesAlert("ACTIVITIES");
            }
        });*/

        souRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivitiesAlert("SOURCES");
            }
        });


       /* reqRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showRequirementsAlert("REQUIREMENT");
                showActivitiesAlert("REQUIREMENT");
            }
        });*/

        sourceOtherLLID = findViewById(R.id.sourceOtherLLID);
        activityOtherLLID = findViewById(R.id.activityOtherLLID);
        dateLLID = findViewById(R.id.dateLLID);

        dateTVID = findViewById(R.id.dateTVID);

        requirementTVID = findViewById(R.id.requirementTVID);
        sourceTVID = findViewById(R.id.sourceTVID);
        activityTVID = findViewById(R.id.activityTVID);
        newLeadNameTVID = findViewById(R.id.newLeadNameTVID);
        leadMobileTVID = findViewById(R.id.leadMobileTVID);
        nextLeadCVID = findViewById(R.id.nextLeadCVID);

        if (screenFrom != null && screenFrom.equalsIgnoreCase("YES")){

            nextLeadCVID.setVisibility(View.VISIBLE);

            newLeadNameTVID.setText(teleName);
            leadMobileTVID.setText(teleMobile);

            sourceStr = "72";
            sourceTVID.setText("TeleCaller");

        }else {

            nextLeadCVID.setVisibility(View.GONE);

        }

        if (incoming != null && incoming.equalsIgnoreCase("YES")){

            mobileETID.setText(incomingNumber);

        }




        emailETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                if (Patterns.EMAIL_ADDRESS.matcher(emailETID.getText().toString()).matches()){
                    emailCBID.setChecked(true);
                    email="4";

                    Log.e("EMAIL==>","" + email);

                }else {
                    emailCBID.setChecked(false);
                    email="";
                }
            }
        });




        okRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(LeadCreateActivity.this)) {
                    validations();
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "No Internet Connection...", "Make sure your device is connected to internet");
                }
            }
        });


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        date = calendar.get(Calendar.DAY_OF_MONTH);
        createdStr = "" + year + "-" + (month + 1) + "-" + date;
        dateStr = "" + year + "-" + (month + 1) + "-" + date;
        dateTVID.setText("" + date + "-" + (month + 1) + "-" + year);

        dateLLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                date = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(LeadCreateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateTVID.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                dateStr = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }

                        }, year, month, date);

                datePickerDialog.show();
            }
        });

        choosePicRLID = findViewById(R.id.choosePicRLID);
        leadPic = findViewById(R.id.leadPic);
        choosePicRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utilities.isNetworkAvailable(LeadCreateActivity.this)) {
                    alertDisplay();
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "No Internet connection..", "Make sure your device is connected to internet");
                }
            }
        });

        if (screenFrom != null && screenFrom.equalsIgnoreCase("YES")){
            firstNameETID.setText(name);
            mobileETID.setText(mobile);
        }

        if (screenFrom != null && screenFrom.equalsIgnoreCase("NO")){
           firstNameETID.setText(name);
            mobileETID.setText(mobile);

            sourceStr = "13";
            sourceTVID.setText("Cold Calling");
        }

        mobileETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                String number = s.toString();
                if (s.length() == 10) {
                    searchCustomer(number);
                }
            }
        });

    }

    private void searchCustomer(String number) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.checkCustomerNumber(number);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if (response.body() != null && response.code() == 200){

                    StatusResponse statusResponse = response.body();
                    if (statusResponse.getStatus().equalsIgnoreCase("0")){
                        okRLID.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(LeadCreateActivity.this, "Mobile Number Already Exist", Toast.LENGTH_SHORT).show();
                        okRLID.setVisibility(View.GONE);


                    }
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

            }
        });
    }

    @SuppressLint("IntentReset")
    private void openImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PICK_IMAGE);
    }

    private void alertDisplay() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_image);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView cameraTVID = dialog.findViewById(R.id.cameraTVID);
        TextView galleryTVID = dialog.findViewById(R.id.galleryTVID);

        cameraTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(LeadCreateActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            image();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();

                dialog.dismiss();
            }
        });

        galleryTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(LeadCreateActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(LeadCreateActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LeadCreateActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_STORAGE);
                } else {
                    openImage();
                }

                dialog.dismiss();
            }
        });

        TextView closeTVID = dialog.findViewById(R.id.closeTVID);
        closeTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void image() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectImageUri = data.getData();
                    selectImagePath = getRealPathFromURI(selectImageUri);
                    decodeImage(selectImagePath);
                }
                break;

           /* case REQUEST_IMAGE_CAPTURE_USER:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    loadProfile(tempUri.toString());
                    selectImagePath = getRealPathFromURI2(tempUri);
                } else {
                    setResultCancelled();
                }
                break;*/
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        //picTitleTVID.setText("1 Image Captured");
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                        loadProfile(tempUri.toString());
                        selectImagePath = getRealPathFromURI2(tempUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 0, bmpStream);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(this, getPackageName() + ".provider", image);
    }

    public String getRealPathFromURI2(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    @SuppressLint("SetTextI18n")
    private void loadProfile(String url) {
        Log.d("", "Image cache path: " + url);
        //picTitleTVID.setText("1 File Selected");
        Picasso.with(this).load(url).into(leadPic);
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }

                return;
            }
        }
    }

    private String getRealPathFromURI(Uri selectImageUri) {
        Cursor cursor = getContentResolver().query(selectImageUri, null, null, null, null);
        if (cursor == null) {
            return selectImageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void decodeImage(String selectImagePath) {
        int targetW = leadPic.getWidth();
        int targetH = leadPic.getHeight();

        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectImagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(selectImagePath, bmOptions);
        if (bitmap != null) {
            leadPic.setImageBitmap(bitmap);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showRequirementsAlert(final String type) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.search_drop_down);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
        rePB = dialog.findViewById(R.id.rePB);
        rePB.setVisibility(View.GONE);
        Button saveRequirementBtn = dialog.findViewById(R.id.saveRequirementBtn);
        otherReqID = dialog.findViewById(R.id.otherReqID);
        otherReqETID = dialog.findViewById(R.id.otherReqETID);
        dropDownGVID = dialog.findViewById(R.id.dropDownLVID);
        dropDownGVID.setFocusable(false);

        final EditText searchHintETID = dialog.findViewById(R.id.searchHintETID);
        final LinearLayout searchLLID = dialog.findViewById(R.id.searchLLID);
        searchLLID.setFocusable(false);


        if (Utilities.isNetworkAvailable(this)) {
            requirements();
        } else {
            AlertUtilities.bottomDisplayStaticAlert(this, "No Internet connection", "MAke sure your device is connected to internet");
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
                if (Utilities.isNetworkAvailable(LeadCreateActivity.this)) {

                    if (otherReqIsVisible) {
                        requirementOtherStr = otherReqETID.getText().toString();

                        if (TextUtils.isEmpty(requirementOtherStr)) {
                            Toast.makeText(LeadCreateActivity.this, "Please enter New Requirement Name", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            selectedRequirementArrays = "";
                            requirementTVID.setText(requirementOtherStr);
                        }

                    } else {
                        //saveSelectedRequirements();
                    }


                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, getString(R.string.no_internet_title), getString(R.string.no_internet_desc));
                }
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
            selectedRequirementNameArrays = sbn.toString();
            dialog.dismiss();

            if (!selectedRequirementArrays.equals("")) {
                if (selectedRequirementArrays.contains("1000")) {
                    selectedRequirementArrays = "1000";
                } else {
                    requirementOtherStr = "";
                    requirementTVID.setText(selectedRequirementNameArrays);
                }
            } else {
                //AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "No Tags selected", "Please select requirements...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @SuppressLint("ClickableViewAccessibility")
    private void showActivitiesAlert(final String type) {

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
        otherFieldSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (type.equalsIgnoreCase("ACTIVITIES")) {

                    activityOtherStr = otherFieldETID.getText().toString();

                    if (otherActivityIsVisible) {
                        if (TextUtils.isEmpty(activityOtherStr)) {
                            Toast.makeText(LeadCreateActivity.this, "Please enter Other Activity Name", Toast.LENGTH_SHORT).show();
                        } else {
                            activityTVID.setText(activityOtherStr);
                            dialog.dismiss();
                        }
                    }
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    sourceOtherStr = otherFieldETID.getText().toString();
                    if (otherSourceIsVisible) {
                        if (TextUtils.isEmpty(sourceOtherStr)) {
                            Toast.makeText(LeadCreateActivity.this, "Please enter Other Source", Toast.LENGTH_SHORT).show();
                        } else {
                            sourceTVID.setText(sourceOtherStr);
                            dialog.dismiss();
                        }
                    }
                }
            }
        });


        if (Utilities.isNetworkAvailable(this)) {

            if (type.equalsIgnoreCase("ACTIVITIES")) {
                activities();
            } else if (type.equalsIgnoreCase("SOURCES")) {
                sources();
            } else if (type.equalsIgnoreCase("REQUIREMENT")) {
                requirements();
            }
        } else {
            AlertUtilities.bottomDisplayStaticAlert(this, "No Internet connection", "MAke sure your device is connected to internet");
        }

        dropDownLVID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> requirementAdapterView, View view, int i, long l) {
                dialog.dismiss();

                if (type.equalsIgnoreCase("ACTIVITIES")) {
                    activityTVID.setText(activityMainResponses.get(i).getActivity_name());
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    sourceTVID.setText(sourceResponses.get(i).getSource_name());
                } else if (type.equalsIgnoreCase("REQUIREMENT")) {
                    requirementTVID.setText(requirementResponses.get(i).getRequirement_name());
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
                }


            }
        });

    }

    private void activities() {
        //drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<ActivityMainResponse>> call = apiInterface.getActivities(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ActivityMainResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivityMainResponse>> call, Response<ArrayList<ActivityMainResponse>> response) {
                //drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    activityMainResponses = response.body();
                    if (activityMainResponses != null && activityMainResponses.size() > 0) {
                        for (int i = 0; i < activityMainResponses.size(); i++) {
                            /*activitiesAdapter = new ActivitiesNewAdapter(LeadCreateActivity.this, activityMainResponses);
                            dropDownLVID.setAdapter(activitiesAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);*/

                            activitiesAdapterNew = new ActivitiesAdapterNew(LeadCreateActivity.this, R.layout.custom_spinner_view, activityMainResponses);
                            activitiesSPID.setAdapter(activitiesAdapterNew);

                            activitiesSPID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    activityStr = activityMainResponses.get(position).getActivity_id();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });


                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ActivityMainResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(LeadCreateActivity.this, "Activities not loading", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sources() {
        drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SourceResponse>> call = apiInterface.getSource(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SourceResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SourceResponse>> call, Response<ArrayList<SourceResponse>> response) {
                drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {

                    sourceResponses = response.body();
                    if (sourceResponses != null && sourceResponses.size() > 0) {

                        sAdapter = new SourceNewAdapter(LeadCreateActivity.this, sourceResponses);
                        dropDownLVID.setAdapter(sAdapter);
                        dropDownLVID.setExpanded(true);
                        dropDownLVID.setFocusable(false);
                     /*   for (int i = 0; i < sourceResponses.size(); i++) {
                            sAdapter = new SourceNewAdapter(LeadCreateActivity.this, sourceResponses);
                            dropDownLVID.setAdapter(sAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SourceResponse>> call, Throwable t) {
                drPB.setVisibility(View.GONE);
                Toast.makeText(LeadCreateActivity.this, "Sources not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void requirements() {
        //drPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<RequirementResponse>> call = apiInterface.getRequirements(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RequirementResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RequirementResponse>> call, Response<ArrayList<RequirementResponse>> response) {
               // drPB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {

                    requirementResponses = response.body();
                    if (requirementResponses != null && requirementResponses.size() > 0) {
                        for (int i = 0; i < requirementResponses.size(); i++) {
                            /*requirementAdapter = new RequirementAdapter(LeadCreateActivity.this, requirementResponses);
                            dropDownLVID.setAdapter(requirementAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);*/

                            RequirementsAdapter requirementsAdapter = new RequirementsAdapter(LeadCreateActivity.this, R.layout.custom_spinner_view,requirementResponses);
                            requirementSPID.setAdapter(requirementsAdapter);

                            requirementSPID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    selectedRequirementArrays = requirementResponses.get(position).getRequirement_id();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequirementResponse>> call, Throwable t) {
             //   drPB.setVisibility(View.GONE);
                Toast.makeText(LeadCreateActivity.this, "requriment not loading", Toast.LENGTH_SHORT).show();

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
                            requirementAdapter = new RequirementNewAdapter(LeadCreateActivity.this, requirementResponses);
                            dropDownGVID.setAdapter(requirementAdapter);
                            dropDownGVID.setExpanded(true);
                            dropDownGVID.setFocusable(false);
                        }
                    } else {
                        Toast.makeText(LeadCreateActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeadCreateActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequirementResponse>> call, Throwable t) {
                rePB.setVisibility(View.GONE);
                Toast.makeText(LeadCreateActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

    private void validations() {
        firstNameStr = firstNameETID.getText().toString();
        lastNameStr = lastNameETID.getText().toString();
        emailStr = emailETID.getText().toString();
        mobileStr = mobileETID.getText().toString();
        alternateStr = alternateETID.getText().toString();
        websiteStr = websiteETID.getText().toString();
        locationStr = locationETID.getText().toString();
        cityStr = cityETID.getText().toString();
        notesStr = notesETID.getText().toString();
        companyStr = companyETID.getText().toString();
        gstStr = gstETID.getText().toString();

        int count = 0;

        if (TextUtils.isEmpty(firstNameStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "First name can't empty", "Please enter first name");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(mobileStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "Mobile can't empty", "Please enter mobile");
            return;
        } else {
            count++;
        }

        if (mobileStr.length() <= 9) {
            Utilities.showToast(this, "Enter Valid Number");
            return;
        } else {
            count++;
        }

        if (!otherReqIsVisible) {
            if (TextUtils.isEmpty(selectedRequirementArrays)) {
                AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "Requirement can't empty", "Please select Requirement");
                return;
            } else {
                count++;
            }
        } else {
            count++;
        }

        if (!otherSourceIsVisible) {
            if (TextUtils.isEmpty(sourceStr)) {
                AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "Source can't empty", "Please select Source");
                return;
            } else {
                count++;
            }
        } else {
            count++;
        }

        if (!otherActivityIsVisible) {
            if (TextUtils.isEmpty(activityStr)) {
                AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "Activity can't empty", "Please select Activity");
                return;
            } else {
                count++;
            }
        } else {
            count++;
        }

        if (voiceCallCBID.isChecked()){
            voiceCall = "1";
        }else {
            voiceCall = "";
        }

        if (smsCBID.isChecked()){
            sms = "2";
        }else {

            sms = "";
        }

        if (whatsAppCBID.isChecked()){
            whatsApp = "3";
        }else {

            whatsApp = "";
        }

        if (emailCBID.isChecked()){
            email = "4";
        }else {
            email = "";
        }

        communications = voiceCall + "," + sms + "," + whatsApp + "," + email;

        Log.e("Communications==>", "" + communications);



        if (count == 6) {
            if (Utilities.isNetworkAvailable(LeadCreateActivity.this)) {
                String o = "Other Source : " + sourceOtherStr + " Other Activity : " + activityOtherStr + " Source : " + sourceStr + " Activity : " + activityStr;
                Log.e("Values : ", "" + o);
                okDisRLID.setVisibility(View.VISIBLE);
                okRLID.setVisibility(View.GONE);
                createLeadResponse();
            } else {
                AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "Alert...!", "No Internet connection");
            }
        }

    }

    private void createLeadResponse() {
        RequestBody reqFile;
        final MultipartBody.Part imageBody;

        File file = new File(selectImagePath);

        if (selectImagePath.equalsIgnoreCase("null")) {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            imageBody = MultipartBody.Part.createFormData("upload_pic", "", reqFile);
        } else {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imageBody = MultipartBody.Part.createFormData("upload_pic", file.getName(), reqFile);
        }

        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CreateLeadResponse>> call = apiInterface.getCreateLead(imageBody, firstNameStr, lastNameStr, mobileStr, alternateStr, emailStr, websiteStr, locationStr,cityStr, companyStr, gstStr, userID, createdStr, selectedRequirementArrays, requirementOtherStr, sourceStr, sourceOtherStr, activityStr, dateStr, companyID, activityOtherStr,communications,notesStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CreateLeadResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CreateLeadResponse>> call, Response<ArrayList<CreateLeadResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<CreateLeadResponse> leadResponse = response.body();
                    if (leadResponse != null && leadResponse.size() > 0) {
                        for (int i = 0; i < leadResponse.size(); i++) {
                            if (leadResponse.get(i).getStatus().equals("1")) {
                                okDisRLID.setVisibility(View.VISIBLE);
                                okRLID.setVisibility(View.GONE);

                                if (screenFrom != null && screenFrom.equalsIgnoreCase("YES") ) {

                                    Intent intent = new Intent(LeadCreateActivity.this,TeleCallCompleteActivity.class);
                                    intent.putExtra("NAME",teleName);
                                    intent.putExtra("CALLER_ID",teleCallerID);
                                    intent.putExtra("SOURCE_ID",teleSourceID);
                                    intent.putExtra("ASSIGNED_TO",teleAssignedTo);
                                    intent.putExtra("MOBILE",teleMobile);
                                    startActivity(intent);
                                    finish();

                                    numberCalling(teleMobile);
                                }

                                if (screenFrom != null && screenFrom.equalsIgnoreCase("NO") ) {

                                    getCampaignCall();
                                }

                                MySharedPreferences.setPreference(LeadCreateActivity.this,AppConstants.PAGE_REFRESH,"YES");


                                //AlertUtilities.SuccessAlertDialog(LeadCreateActivity.this, "Lead Created Successfully", "done.josn");
                               /* Toast.makeText(LeadCreateActivity.this, "Lead created Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LeadCreateActivity.this, TagDetailsActivity.class);
                                intent.putExtra("CUSTOMER_NAME", "" + firstNameStr);
                                intent.putExtra("LEAD_ID", "" + leadResponse.get(i).getLead_id());
                                startActivity(intent);*/
                                finish();
                            } else {
                                okDisRLID.setVisibility(View.GONE);
                                okRLID.setVisibility(View.VISIBLE);
                                Utilities.AlertDaiolog(LeadCreateActivity.this, getString(R.string.alert), "Number already exist", 1, null, "OK");
                            }
                        }
                    }

                } else {
                    okDisRLID.setVisibility(View.GONE);
                    okRLID.setVisibility(View.VISIBLE);
                    Utilities.AlertDaiolog(LeadCreateActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CreateLeadResponse>> call, Throwable t) {
                okDisRLID.setVisibility(View.GONE);
                okRLID.setVisibility(View.VISIBLE);
                dismissProgressDialog();
                Utilities.AlertDaiolog(LeadCreateActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
            }
        });
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

                        Toast.makeText(LeadCreateActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(LeadCreateActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }else {

                    Toast.makeText(LeadCreateActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CampaignCallResponse> call, Throwable t) {

                Log.e("Status==>","" + "Failed");

                Toast.makeText(LeadCreateActivity.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

            }
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
*/

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

    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);

    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(LeadCreateActivity.this)
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

    public class RequirementAdapter extends BaseAdapter implements Filterable {
        private ArrayList<RequirementResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private RequirementAdapter.ValueFilter valueFilter;
        private ArrayList<RequirementResponse> mStringFilterList;

        public RequirementAdapter(Activity context, ArrayList<RequirementResponse> _Contacts) {
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
                    selectedRequirementArrays = _Contacts.get(position).getRequirement_id();
                    //getUpdateLead();
                    otherReqIsVisible = false;
                    if (selectedRequirementArrays.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Requirement");
                        otherReqIsVisible = true;
                    } else {
                        requirementOtherStr = "";
                        otherReqIsVisible = false;
                        dialog.dismiss();
                        requirementTVID.setText(_Contacts.get(position).getRequirement_name());
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

    public static class RequirementNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<RequirementResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;

        private ValueFilter valueFilter;
        private ArrayList<RequirementResponse> mStringFilterList;
        public boolean state = true;
        private ArrayList<String> mCheckedItemIDs;
        private ArrayList<String> mCheckedItemNames;

        public ArrayList<String> getCheckedItems() {
            return mCheckedItemIDs;
        }

        public ArrayList<String> getCheckedItemsNames() {
            return mCheckedItemNames;
        }

        public RequirementNewAdapter(Activity context, ArrayList<RequirementResponse> _Contacts) {
            super();
            this.context = context;
            this._Contacts = _Contacts;
            mStringFilterList = _Contacts;
            mCheckedItemIDs = new ArrayList<>();
            mCheckedItemNames = new ArrayList<>();
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            getFilter();
        }

        @Override
        public int getCount() {
            return _Contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return _Contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder {
            TextView tname;
            RelativeLayout spinnerBgColor;
            LinearLayout dropDownListItem;
            CheckBox checkBoxID;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.new_custom_spinner_list_items, null);
                holder.checkBoxID = convertView.findViewById(R.id.checkBoxID);
                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getRequirement_name());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));


            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (state) {
                        state = false;
                        holder.checkBoxID.setChecked(true);
                        mCheckedItemIDs.add(_Contacts.get(position).getRequirement_id());
                        mCheckedItemNames.add(_Contacts.get(position).getRequirement_name());
                        String id = _Contacts.get(position).getRequirement_id();
                        if (id.equals("1000")) {
                            /*otherReqIsVisible = true;
                            otherReqID.setVisibility(View.VISIBLE);*/
                        }
                    } else {

                        String id = _Contacts.get(position).getRequirement_id();
                        if (id.equals("1000")) {
                           /* otherReqIsVisible = false;
                            otherReqID.setVisibility(View.GONE);*/
                        }

                        state = true;
                        holder.checkBoxID.setChecked(false);
                        mCheckedItemIDs.remove(_Contacts.get(position).getRequirement_id());
                        mCheckedItemNames.remove(_Contacts.get(position).getRequirement_name());
                    }
                }
            });


            holder.checkBoxID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean value) {
                    if (holder.checkBoxID.isPressed()) {

                        if (value) {
                            mCheckedItemIDs.add(_Contacts.get(position).getRequirement_id());
                            mCheckedItemNames.add(_Contacts.get(position).getRequirement_name());
                            String id = _Contacts.get(position).getRequirement_id();
                            if (id.equals("1000")) {
                               /* otherReqIsVisible = true;
                                otherReqID.setVisibility(View.VISIBLE);*/
                            }

                        } else {
                            String id = _Contacts.get(position).getRequirement_id();
                            if (id.equals("1000")) {
                               /* otherReqIsVisible = false;
                                otherReqID.setVisibility(View.GONE);*/
                            }
                            mCheckedItemIDs.remove(_Contacts.get(position).getRequirement_id());
                            mCheckedItemNames.remove(_Contacts.get(position).getRequirement_name());
                        }
                    }
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new ValueFilter();
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



    public class ActivitiesNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<ActivityMainResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private ValueFilter valueFilter;
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


            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
            holder.tname.setText(_Contacts.get(position).getActivity_name());
            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activityStr = _Contacts.get(position).getActivity_id();
                    otherActivityIsVisible = false;
                    if (activityStr.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Activity");
                        otherActivityIsVisible = true;
                    } else {
                        activityOtherStr = "";
                        otherActivityIsVisible = false;
                        dialog.dismiss();
                        activityTVID.setText(_Contacts.get(position).getActivity_name());
                    }
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new ValueFilter();
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



    public class SourceNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<SourceResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private ValueFilter valueFilter;
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

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                    "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                    "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                    "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                    "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                    "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (ViewHolder) convertView.getTag();
                holder.tname.setText(_Contacts.get(position).getSource_name());

            holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
            GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));

            holder.dropDownListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sourceStr = _Contacts.get(position).getSource_id();
                    otherSourceIsVisible = false;
                    if (sourceStr.equalsIgnoreCase("1000")) {
                        otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Source");
                        otherSourceIsVisible = true;
                    } else {
                        sourceOtherStr = "";
                        otherSourceIsVisible = false;
                        dialog.dismiss();
                        sourceTVID.setText(_Contacts.get(position).getSource_name());
                    }
                }
            });


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new ValueFilter();
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
}
