package crm.tranquil_sales_steer.ui.activities.templates;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CommunicationsServices;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.GetCompanyUserdlt;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendTemplatesActivity extends AppCompatActivity {

    String type, numberStr, emailStr,attachement,customerNameStr;
    LinearLayout attachmentLLID, subjectLLID;
    EditText messageETID, subjectETID, attachmentETID;
    TextView tittlesTVID;
    SendSmsAdapter sendAdapter;
    private ArrayList<SmsTemplatesResponse> smsResponse = new ArrayList<>();
    SendEmailAdapter sendEmailAdapter;
    private ArrayList<SendEmailResponse> emailResponse = new ArrayList<>();
    ExpandableHeightGridView templatesEXCVID;
    String companyID,companyName,userMobile, userID,userNameStr, customerID,leadID;
    ProgressBar pb;
    Button fab_add;
    ArrayList<GetCompanyUserdlt> getCompanyUserdlts = new ArrayList<>();
    RelativeLayout okRLID,okDisRLID;
    LinearLayout micLID;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    KProgressHUD kProgressHUD;
    String messageNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_templates);

        Utilities.setStatusBarGradiant(this);
        if(ActivityCompat.checkSelfPermission(SendTemplatesActivity.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(SendTemplatesActivity.this,new String[]{Manifest.permission.SEND_SMS},100);

        }

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                type = bundle.getString("TYPE");
                emailStr = bundle.getString("LEAD_EMAIL");
                numberStr = bundle.getString("LEAD_MOBILE_NUMBER");
                customerID = bundle.getString("LEAD_ID");
                customerNameStr = bundle.getString("CUSTOMER_NAME");

            }

        }

        Log.e("CUSTOMER_NAME==>",""+customerNameStr);

        userID = MySharedPreferences.getPreferences(SendTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userNameStr = MySharedPreferences.getPreferences(SendTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_NAME);
        companyID = MySharedPreferences.getPreferences(SendTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        companyName = MySharedPreferences.getPreferences(SendTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_NAME);
        userMobile = MySharedPreferences.getPreferences(SendTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
        leadID = MySharedPreferences.getPreferences(SendTemplatesActivity.this, "" + AppConstants.LEAD_ID);
        //statusBarUtilities.statusBarSetup(this);
        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        okRLID = findViewById(R.id.okRLID);
        okDisRLID = findViewById(R.id.okDisRLID);

        okRLID.setVisibility(View.VISIBLE);
        okDisRLID.setVisibility(View.GONE);

        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText(type);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });

        pb = findViewById(R.id.pb);
        messageETID = findViewById(R.id.messageETID);
        subjectETID = findViewById(R.id.subjectETID);
        tittlesTVID = findViewById(R.id.tittleTVID);
        attachmentETID = findViewById(R.id.attachmentETID);
        micLID = findViewById(R.id.micLID);

        templatesEXCVID = findViewById(R.id.templatesEXCVID);

        attachmentLLID = findViewById(R.id.attachmentLLID);
        subjectLLID = findViewById(R.id.subjectLLID);
        attachmentLLID.setVisibility(View.GONE);
        subjectLLID.setVisibility(View.GONE);
        getcompanyuserdlt();
        if (type.equalsIgnoreCase("Send Email")) {
            attachmentLLID.setVisibility(View.GONE);
            subjectLLID.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("Web Email")) {
            attachmentLLID.setVisibility(View.GONE);
            subjectLLID.setVisibility(View.VISIBLE);
        }else {
            attachmentLLID.setVisibility(View.GONE);
            subjectLLID.setVisibility(View.GONE);
        }

        if (type.equalsIgnoreCase("Send Sms")) {
            loadSmsTemplates();
        } else if (type.equalsIgnoreCase("Send Email")) {
            loadEmailTemplate();
        }else if (type.equalsIgnoreCase("Web Email")) {
            loadEmailTemplate();
        } else if (type.equalsIgnoreCase("Send Whats App")) {
            loadSmsTemplates();
        }



        okRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = messageETID.getText().toString();
                String subject = subjectETID.getText().toString();

                 messageNew = "Dear  "+ customerNameStr + "\n\n" +   message  + "\n\n" + userNameStr +"\n"+  companyName + "\n" + userMobile;
                 String messageMail = "Dear  "+ customerNameStr + "\n\n" +   message ;

                if (TextUtils.isEmpty(messageNew)) {
                    if (type.equalsIgnoreCase("Send SMS")){
                        Toast.makeText(SendTemplatesActivity.this, "Please enter your message or select template", Toast.LENGTH_SHORT).show();

                    }else if (type.equalsIgnoreCase("Send Email")){
                        Toast.makeText(SendTemplatesActivity.this, "Please enter your message or select template", Toast.LENGTH_SHORT).show();

                    }else if (type.equalsIgnoreCase("Web Email")){
                        Toast.makeText(SendTemplatesActivity.this, "Please enter your message or select template", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    okRLID.setVisibility(View.GONE);
                    okDisRLID.setVisibility(View.VISIBLE);

                    if (type.equalsIgnoreCase("Send Sms")) {
                       CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "2", customerID, userID, messageNew, "");


                  //     checksmspermisionAndOnsmspermission();

                        if(ActivityCompat.checkSelfPermission(SendTemplatesActivity.this,Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){

                           // ActivityCompat.requestPermissions(SendTemplatesActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                            ActivityCompat.requestPermissions(SendTemplatesActivity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    100);

                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(numberStr,null,messageNew,null,null);
                            Log.e("smsmobile", ""+numberStr+", "+messageNew);
                            finish();
                            Toast.makeText(SendTemplatesActivity.this, "SMS Sent Succesessfully", Toast.LENGTH_SHORT).show();
                        }




                        //only sms tesing code
                       /* ActivityCompat.requestPermissions(SendTemplatesActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                100);

                        SmsManager smsManager=SmsManager.getDefault();
                        smsManager.sendTextMessage(numberStr,null,messageNew,null,null);
                        finish();*/




                      /*  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", numberStr, null));
                        intent.putExtra("sms_body",  messageNew );
                        //intent.putExtra("sms_body", "Dear  "+ customerNameStr + " " +   message  + "\n\n" + userNameStr +"\n"+  companyName + "\n" + userMobile );
                        //intent.setType("text/plain");
                        startActivity(intent);
                        finish();*/





                        //Utilities.finishAnimation(SendTemplatesActivity.this);

                        /*   ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
                        Call<ArrayList<StatusResponse>> call = apiInterface.getSendMessage(userID,leadID,message,"2");
                        Log.e("messageResponse==>",call.request().toString());
                        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
                            @Override
                            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                                if (response.body() != null && response.code() == 200){
                                    ArrayList<StatusResponse> statusResponses = response.body();

                                    if (statusResponses.get(0).getStatus().equals("1")){
                                        Toast.makeText(SendTemplatesActivity.this, "Successfully message send", Toast.LENGTH_SHORT).show();
                                        Utilities.finishAnimation(SendTemplatesActivity.this);
                                    }else {
                                        Toast.makeText(SendTemplatesActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {

                                Toast.makeText(SendTemplatesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        });*/


                    }
                    //sendMessageAndWhatsApp("SMS", message, numberStr);
                }

                if (TextUtils.isEmpty(subject)) {
                    if (type.equalsIgnoreCase("Send Whats App")) {

                    } else if (type.equalsIgnoreCase("Send SMS")){
                      //  Toast.makeText(SendTemplatesActivity.this, "Please enter your text or select template", Toast.LENGTH_SHORT).show();

                    }else {

                        if (type.equalsIgnoreCase("Send Email")){
                            Toast.makeText(SendTemplatesActivity.this, "Please enter your message or select template", Toast.LENGTH_SHORT).show();

                        }else if (type.equalsIgnoreCase("Web Email")){
                            Toast.makeText(SendTemplatesActivity.this, "Please enter your message or select template", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    okRLID.setVisibility(View.GONE);
                    okDisRLID.setVisibility(View.VISIBLE);

                    if (type.equalsIgnoreCase("Web Email")) {

                           ApiInterface apiInterface = ApiClient.getClientNew(getParent()).create(ApiInterface.class);
                        Call<ArrayList<StatusResponse>> call = apiInterface.getSendMessage(userID,leadID,subject,"4");
                        Log.e("webMailResponse==>",call.request().toString());
                        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
                            @Override
                            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                                if (response.body() != null && response.code() == 200){
                                    ArrayList<StatusResponse> statusResponses = response.body();

                                    if (statusResponses.get(0).getStatus().equals("1")){
                                        Toast.makeText(SendTemplatesActivity.this, "Successfully message send", Toast.LENGTH_SHORT).show();
                                        Utilities.finishAnimation(SendTemplatesActivity.this);
                                    }else {
                                        Toast.makeText(SendTemplatesActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {

                                Toast.makeText(SendTemplatesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                    //sendMessageAndWhatsApp("SMS", message, numberStr);
                }



                /*if (type.equalsIgnoreCase("Send Sms")) {

                       ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
                    Call<ArrayList<StatusResponse>> call = apiInterface.getSendMessage(userID,leadID,message,"2");
                    Log.e("messageResponse==>",call.request().toString());
                    call.enqueue(new Callback<ArrayList<StatusResponse>>() {
                        @Override
                        public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                            if (response.body() != null && response.code() == 200){
                                ArrayList<StatusResponse> statusResponses = response.body();

                                if (statusResponses.get(0).getStatus().equals("1")){
                                    Toast.makeText(SendTemplatesActivity.this, "Successfully message send", Toast.LENGTH_SHORT).show();
                                    Utilities.finishAnimation(SendTemplatesActivity.this);
                                }else {
                                    Toast.makeText(SendTemplatesActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {

                            Toast.makeText(SendTemplatesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });


                } else*/ if (type.equalsIgnoreCase("Send Email")) {
                    okRLID.setVisibility(View.GONE);
                    okDisRLID.setVisibility(View.VISIBLE);
                    sendMessageToGMail(subject, messageMail, new String[]{emailStr});
                    //sendMessageToGMail(subject, message, new String[]{emailStr});
                } else if (type.equalsIgnoreCase("Send Whats App")) {
                    if (TextUtils.isEmpty(message)) {

                        Toast.makeText(SendTemplatesActivity.this, "Please enter your message or select template", Toast.LENGTH_SHORT).show();
                    } else {
                        okRLID.setVisibility(View.GONE);
                        okDisRLID.setVisibility(View.VISIBLE);
                        sendMessageAndWhatsApp("WHATS_APP", messageNew, numberStr);

                        //sendMessageAndWhatsApp("WHATS_APP", message, numberStr);
                    }
                }
            }



            public void checksmspermisionAndOnsmspermission(){
                if(ActivityCompat.checkSelfPermission(SendTemplatesActivity.this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(SendTemplatesActivity.this,new String[]{Manifest.permission.SEND_SMS},100);


                }else {
                  /*  Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera_intent, 0);*/

                    ActivityCompat.requestPermissions(SendTemplatesActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            100);

                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(numberStr,null,messageNew,null,null);
                    finish();
                }


            }
        });


        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("Send Sms")) {
                    startActivity(new Intent(SendTemplatesActivity.this, SmsTemplatesActivity.class));
                } else if (type.equalsIgnoreCase("Send Email")) {
                    startActivity(new Intent(SendTemplatesActivity.this, EmailTemplatesActivity.class));
                }else if (type.equalsIgnoreCase("Web Email")) {
                    startActivity(new Intent(SendTemplatesActivity.this, EmailTemplatesActivity.class));
                } else if (type.equalsIgnoreCase("Send Whats App")) {
                    startActivity(new Intent(SendTemplatesActivity.this, SmsTemplatesActivity.class));
                }
            }
        });

        micLID.setOnClickListener(v -> {
            startVoiceInput();
        });
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
                    messageETID.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void getcompanyuserdlt(){
        ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
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
                            //    Toast.makeText(SendTemplatesActivity.this, "  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                                micLID.setVisibility(View.VISIBLE);
                                fab_add.setVisibility(View.VISIBLE);

                            }else if(getCompanyUserdlts.get(i).getProduct_id().equals("2")){
                               // Toast.makeText(SendTemplatesActivity.this, "2  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                                micLID.setVisibility(View.VISIBLE);
                                fab_add.setVisibility(View.GONE);
                            }else{
                               // Toast.makeText(SendTemplatesActivity.this, "3", Toast.LENGTH_SHORT).show();
                                micLID.setVisibility(View.GONE);
                                fab_add.setVisibility(View.GONE);

                            }

                        }


                    }
                }



                //    Toast.makeText(DashBoardActivity.this, "onsucccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<GetCompanyUserdlt>> call, Throwable t) {
                Toast.makeText(SendTemplatesActivity.this, "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void sendMessageAndWhatsApp(String types, String msg, String number) {
        /*if (types.equalsIgnoreCase("SMS")) {
            // The number on which you want to send SMS
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
            intent.putExtra("sms_body", msg);

            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "2", customerID, userID, msg, "");

            okRLID.setVisibility(View.VISIBLE);
            okDisRLID.setVisibility(View.GONE);
            //intent.setType("text/plain");
            startActivity(intent);
        }*/

        if (types.equalsIgnoreCase("WHATS_APP")) {
            okRLID.setVisibility(View.VISIBLE);
            okDisRLID.setVisibility(View.GONE);
            PackageManager pm = getPackageManager();
            try {

                final BottomSheetDialog dialog1 = new BottomSheetDialog(SendTemplatesActivity.this);
                dialog1.setContentView(R.layout.alert_business_whatsapp);

                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                int width1 = ViewGroup.LayoutParams.MATCH_PARENT;
                int height1 = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog1.getWindow().setLayout(width1, height1);
                dialog1.show();

                AppCompatImageView whatsappIVID = dialog1.findViewById(R.id.whatsappIVID);
                AppCompatImageView businessWhatsappIVID = dialog1.findViewById(R.id.businessWhatsappIVID);
                LinearLayout businessWhatsappLLID=dialog1.findViewById(R.id.businessWhatsappLLID);
                AppCompatTextView selectappTVID=dialog1.findViewById(R.id.selectappTVID);
             //   InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
               // CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                if (isWhatsAppBusinessInstalled()) {
                    // Open WhatsApp Business
                    //openWhatsAppBusiness();
                    //Toast.makeText(this, "Bussnesswhat is theree", Toast.LENGTH_SHORT).show();
                    businessWhatsappIVID.setOnClickListener(view2 -> {

                        //PackageManager pm = getPackageManager();

                        try {
                            //ApiCommonServices.InsertCommunication(LeadHistoryActivity.this, "5", customerID, userID, "", msg);
                            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                            Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                            String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode(msg, "UTF-8");
                            sendMsg.setPackage("com.whatsapp.w4b");
                            sendMsg.setData(Uri.parse(url));
                            startActivity(sendMsg);
                            finish();
                            dialog1.dismiss();

                            //Utilities.finishAnimation(SendTemplatesActivity.this);

                        /*if (sendMsg.resolveActivity(getPackageManager()) != null) {
                            startActivity(sendMsg);
                        }*/

                        } catch (Exception e) {
                            e.printStackTrace();
                            /*Utilities.showToast(this,e.getMessage());*/
                            Toast.makeText(this, "You don't have business WhatsApp in your device", Toast.LENGTH_SHORT).show();
                        }

                        dialog1.dismiss();

                    });
                } else {
                    // Open regular WhatsApp
                   // openWhatsApp();
                   // Toast.makeText(this, "Normal bussness is theree", Toast.LENGTH_SHORT).show();
                    businessWhatsappLLID.setVisibility(View.GONE);
//                    selectappTVID.setVisibility(View.GONE);
                    whatsappIVID.setOnClickListener(view1 -> {

                        try {
                            //ApiCommonServices.InsertCommunication(LeadHistoryActivity.this, "5", customerID, userID, "", msg);

                            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                            Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                            String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode(msg, "UTF-8");

                            sendMsg.setPackage("com.whatsapp");
                            sendMsg.setData(Uri.parse(url));
                            startActivity(sendMsg);
                            finish();
                            dialog1.dismiss();

                            //Utilities.finishAnimation(SendTemplatesActivity.this);

                       /* if (sendMsg.resolveActivity(getPackageManager()) != null) {
                            startActivity(sendMsg);
                        }*/

                        } catch (Exception e) {
                            e.printStackTrace();
                            /*Utilities.showToast(this,e.getMessage());*/
                            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                            Toast.makeText(this, "e"+e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("sendtemplatesmessage", ""+e.getMessage());
                        }

                    });
                }







               /* if (sendMsg.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendMsg);
                    CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
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





    /*   private void sendMessageAndWhatsApp(String types, String msg, String number) {
        *//*if (types.equalsIgnoreCase("SMS")) {
            // The number on which you want to send SMS
            //startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
            intent.putExtra("sms_body", msg);

            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "2", customerID, userID, msg, "");

            okRLID.setVisibility(View.VISIBLE);
            okDisRLID.setVisibility(View.GONE);
            //intent.setType("text/plain");
            startActivity(intent);
        }*//*

        if (types.equalsIgnoreCase("WHATS_APP")) {
            okRLID.setVisibility(View.VISIBLE);
            okDisRLID.setVisibility(View.GONE);
            PackageManager pm = getPackageManager();
            try {

                final BottomSheetDialog dialog1 = new BottomSheetDialog(SendTemplatesActivity.this);
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
                        //ApiCommonServices.InsertCommunication(LeadHistoryActivity.this, "5", customerID, userID, "", msg);
                        CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");

                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode(msg, "UTF-8");
                        Log.e("whatsappnumber", ""+number);
                        sendMsg.setPackage("com.whatsapp");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        finish();
                        dialog1.dismiss();

                        //Utilities.finishAnimation(SendTemplatesActivity.this);

                        *//*if (sendMsg.resolveActivity(getPackageManager()) != null) {
                            startActivity(sendMsg);
                            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                        }*//*

                    } catch (Exception e) {
                        e.printStackTrace();
                        *//*Utilities.showToast(this,e.getMessage());*//*
                        Toast.makeText(this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                });

                businessWhatsappIVID.setOnClickListener(view2 -> {

                    //PackageManager pm = getPackageManager();

                    try {
                        //ApiCommonServices.InsertCommunication(LeadHistoryActivity.this, "5", customerID, userID, "", msg);
                        CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");

                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + number + "&text=" + URLEncoder.encode(msg, "UTF-8");
                        sendMsg.setPackage("com.whatsapp.w4b");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        finish();
                        dialog1.dismiss();

                        //Utilities.finishAnimation(SendTemplatesActivity.this);

                        *//*if (sendMsg.resolveActivity(getPackageManager()) != null) {
                            startActivity(sendMsg);
                            CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                        }*//*

                    } catch (Exception e) {
                        e.printStackTrace();
                        *//*Utilities.showToast(this,e.getMessage());*//*
                        Toast.makeText(this, "You don't have business WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                    dialog1.dismiss();

                });

               *//* if (sendMsg.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendMsg);
                    CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "3", customerID, userID, msg, "");
                }*//*

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    @SuppressLint({"InlinedApi", "IntentReset", "LongLogTag"})
    private void sendMessageToGMail(String subject, String msg, String[] email) {
        okRLID.setVisibility(View.VISIBLE);
        okDisRLID.setVisibility(View.GONE);
       /* Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_CHOSEN_COMPONENT_INTENT_SENDER, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg+"\n\n\n"+"Attachment : "+attachement);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "4", customerID, userID, msg, subject);
        startActivity(emailIntent);*/

        CommunicationsServices.InsertCommunication(SendTemplatesActivity.this, "4", customerID, userID, msg, subject);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        //emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.setPackage("com.google.android.gm");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        String plainText = Html.fromHtml(msg).toString();
        emailIntent.putExtra(Intent.EXTRA_TEXT, plainText +"\n\n\n"+"Attachment : "+attachement  + "\n\n\n" + userNameStr +"\n"+  companyName + "\n" + userMobile);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendTemplatesActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadEmailTemplate() {
        pb.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SendEmailResponse>> call = apiInterface.getUpdateEmailTemplates(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SendEmailResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SendEmailResponse>> call, retrofit2.Response<ArrayList<SendEmailResponse>> response) {
                pb.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    emailResponse = response.body();
                    if (emailResponse.size() > 0) {
                        for (int i = 0; i < emailResponse.size(); i++) {
                            sendEmailAdapter = new SendEmailAdapter(SendTemplatesActivity.this, emailResponse);
                            templatesEXCVID.setAdapter(sendEmailAdapter);
                            templatesEXCVID.setExpanded(true);
                            templatesEXCVID.setFocusable(false);
                        }
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(SendTemplatesActivity.this, getString(R.string.opps), getString(R.string.temples));
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(SendTemplatesActivity.this, getString(R.string.opps), getString(R.string.temples));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SendEmailResponse>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                // AlertUtilities.bottomDisplayStaticAlert(SendTemplatesActivity.this, getString(R.string.opps), getString(R.string.temples));
            }
        });
    }

    private void loadSmsTemplates() {
        pb.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SmsTemplatesResponse>> call = apiInterface.getUpdateSmsTemplates(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SmsTemplatesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SmsTemplatesResponse>> call, retrofit2.Response<ArrayList<SmsTemplatesResponse>> response) {
                pb.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    smsResponse = response.body();

                    if (smsResponse.size() > 0) {
                        for (int i = 0; i < smsResponse.size(); i++) {
                            sendAdapter = new SendSmsAdapter(SendTemplatesActivity.this, smsResponse);
                            templatesEXCVID.setAdapter(sendAdapter);
                            templatesEXCVID.setExpanded(true);
                            templatesEXCVID.setFocusable(false);
                        }
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(SendTemplatesActivity.this, getString(R.string.opps), getString(R.string.temples));
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(SendTemplatesActivity.this, getString(R.string.opps), getString(R.string.temples));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SmsTemplatesResponse>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                //AlertUtilities.bottomDisplayStaticAlert(SendTemplatesActivity.this, getString(R.string.opps), getString(R.string.temples));

            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

    public class SmsTemplatesResponse {
        /*sms_id: "1",
sms_title: "Sample Template 1",
sms_message:*/

        private String sms_id;
        private String sms_title;
        private String sms_message;

        public SmsTemplatesResponse(String sms_id, String sms_title, String sms_message) {
            this.sms_id = sms_id;
            this.sms_title = sms_title;
            this.sms_message = sms_message;
        }

        public String getSms_id() {
            return sms_id;
        }

        public void setSms_id(String sms_id) {
            this.sms_id = sms_id;
        }

        public String getSms_title() {
            return sms_title;
        }

        public void setSms_title(String sms_title) {
            this.sms_title = sms_title;
        }

        public String getSms_message() {
            return sms_message;
        }

        public void setSms_message(String sms_message) {
            this.sms_message = sms_message;
        }
    }

    public class SendSmsAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<SmsTemplatesResponse> templatesResponses = new ArrayList<>();
        private Activity activity;


        private int mSelectedPosition = -1;
        private RadioButton testRadioButtonID;
        RadioButton mSelectedRB;


        public SendSmsAdapter(Activity activity, ArrayList<SmsTemplatesResponse> templatesResponses) {
            this.activity = activity;
            this.templatesResponses = templatesResponses;
            inflater = LayoutInflater.from(activity);
        }


        @Override
        public int getCount() {
            return templatesResponses.size();
        }

        @Override
        public Object getItem(int i) {
            return templatesResponses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, final View convertView, ViewGroup viewGroup) {

            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.template_list_item, null);

            TextView tittleTVID = view.findViewById(R.id.tittleTVID);
            testRadioButtonID = view.findViewById(R.id.testRadioButtonID);
            testRadioButtonID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((position != mSelectedPosition && mSelectedRB != null)) {
                        mSelectedRB.setChecked(false);
                    }

                    mSelectedPosition = position;
                    mSelectedRB = (RadioButton) v;

                    String msg = templatesResponses.get(position).getSms_message();
                    messageETID.setText(Utilities.CapitalText(msg));
                    tittlesTVID.setText(Utilities.CapitalText(templatesResponses.get(position).getSms_title()));

                }
            });
            tittleTVID.setText(templatesResponses.get(position).getSms_title());

            if (mSelectedPosition != position) {
                testRadioButtonID.setChecked(false);
            } else {
                testRadioButtonID.setChecked(true);
                if (mSelectedRB != null && testRadioButtonID != mSelectedRB) {
                    mSelectedRB = testRadioButtonID;
                }
            }
            return view;
        }
    }

    public class SendEmailResponse {

        /*emai_template_id: "3",
email_title: "email title",
email_subject: "email subject",
email_message: "message",
email_attachement*/

        private String emai_template_id;
        private String email_title;
        private String email_subject;
        private String email_message;
        private String email_attachement;

        public SendEmailResponse(String emai_template_id, String email_title, String email_subject, String email_message, String email_attachement) {
            this.emai_template_id = emai_template_id;
            this.email_title = email_title;
            this.email_subject = email_subject;
            this.email_message = email_message;
            this.email_attachement = email_attachement;
        }

        public String getEmai_template_id() {
            return emai_template_id;
        }

        public void setEmai_template_id(String emai_template_id) {
            this.emai_template_id = emai_template_id;
        }

        public String getEmail_title() {
            return email_title;
        }

        public void setEmail_title(String email_title) {
            this.email_title = email_title;
        }

        public String getEmail_subject() {
            return email_subject;
        }

        public void setEmail_subject(String email_subject) {
            this.email_subject = email_subject;
        }

        public String getEmail_message() {
            return email_message;
        }

        public void setEmail_message(String email_message) {
            this.email_message = email_message;
        }

        public String getEmail_attachement() {
            return email_attachement;
        }

        public void setEmail_attachement(String email_attachement) {
            this.email_attachement = email_attachement;
        }
    }

    public class SendEmailAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList<SendEmailResponse> templatesResponses = new ArrayList<>();
        private Activity activity;
        private int mSelectedPosition = -1;
        private RadioButton testRadioButtonID;
        RadioButton mSelectedRB;


        public SendEmailAdapter(Activity activity, ArrayList<SendEmailResponse> templatesResponses) {
            this.activity = activity;
            this.templatesResponses = templatesResponses;
            inflater = LayoutInflater.from(activity);
        }


        @Override
        public int getCount() {
            return templatesResponses.size();
        }

        @Override
        public Object getItem(int i) {
            return templatesResponses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, final View convertView, ViewGroup viewGroup) {

            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.template_list_item, null);

            TextView tittleTVID = view.findViewById(R.id.tittleTVID);
            testRadioButtonID = view.findViewById(R.id.testRadioButtonID);
            testRadioButtonID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((position != mSelectedPosition && mSelectedRB != null)) {
                        mSelectedRB.setChecked(false);
                    }

                    mSelectedPosition = position;
                    mSelectedRB = (RadioButton) v;

                    String msg = templatesResponses.get(position).getEmail_message();
                    messageETID.setText(msg);
                    subjectETID.setText(templatesResponses.get(position).getEmail_subject());
                    attachmentETID.setText(templatesResponses.get(position).getEmail_attachement());
                    attachement =templatesResponses.get(position).getEmail_attachement();

                }
            });
            tittleTVID.setText(templatesResponses.get(position).getEmail_title());

            if (mSelectedPosition != position) {
                testRadioButtonID.setChecked(false);
            } else {
                testRadioButtonID.setChecked(true);
                if (mSelectedRB != null && testRadioButtonID != mSelectedRB) {
                    mSelectedRB = testRadioButtonID;
                }
            }
            return view;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (type.equalsIgnoreCase("Send Sms")) {
            loadSmsTemplates();
        } else if (type.equalsIgnoreCase("Send Email")) {
            loadEmailTemplate();
        } else if (type.equalsIgnoreCase("Send Whats App")) {
            loadSmsTemplates();
        }
    }
}
