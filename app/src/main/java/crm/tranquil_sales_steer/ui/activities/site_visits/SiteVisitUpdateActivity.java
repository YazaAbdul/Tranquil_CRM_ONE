package crm.tranquil_sales_steer.ui.activities.site_visits;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.gps_utilities.GPSTracker;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.MeetSubmitionRespons;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteVisitUpdateActivity extends AppCompatActivity {
    EditText name, number, notes;
    String nameID, numberID, notesID, customerID, locationID, areaID, updaeID;
    Button submit;
    RelativeLayout speakRLID;
    LinearLayout micLID;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static final int REQ_CODE_SPEECH_INPUT_NOTE = 101;
    EditText messageETID;


    private Boolean mRequestingLocationUpdates;
    Location location = null;
    GPSTracker mGPS = null;

    double latitude, langitude;
    String latitudeStr, langitudeStr;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit_update2);

        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("UPDATE SITE VISIT");
        RelativeLayout backBtnLL =  findViewById(R.id.backRLID);
        speakRLID = findViewById(R.id.speakRLID);
        backBtnLL.setOnClickListener(v -> finish());

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                nameID = bundle.getString("CUSTOMER_NAME");
                numberID = bundle.getString("CUSTOMER_NUMBER");
                customerID = bundle.getString("CUSTOMER_ID");
                updaeID = bundle.getString("SITE_ID");
            }
        }

        updaeID = MySharedPreferences.getPreferences(this, "SITE_ID");
        name =  findViewById(R.id.nameETID);
        name.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        number =  findViewById(R.id.numberETID);
        notes =  findViewById(R.id.notesUpdateETID);
        submit =  findViewById(R.id.updateSubmitBtn);
        submit.setOnClickListener(v -> validation());

        name.setText(nameID);
        number.setText(numberID);
        getLocation();

        speakRLID.setOnClickListener(v -> {
            startVoiceInput(REQ_CODE_SPEECH_INPUT_NOTE);
        });



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
                    notes.setText(result.get(0));
                }
            }

        }
    }

    private void getLocation(){
        try {
            if (mGPS == null) {
                mGPS = new GPSTracker(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check if mGPS object is created or not
        try {
            if (mGPS != null && location == null) {
                location = mGPS.getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (location != null) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (addresses != null && addresses.size() > 0) {
                        locationID = addresses.get(0).getLocality();
                        latitude = addresses.get(0).getLatitude();
                        langitude = addresses.get(0).getLongitude();

                        latitudeStr = String.valueOf(latitude);
                        langitudeStr = String.valueOf(langitude);

                        Address fullAddress = addresses.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < fullAddress.getMaxAddressLineIndex(); i++) {
                            sb.append(fullAddress.getAddressLine(i)).append(",\n");
                        }
                        sb.append(fullAddress.getAddressLine(0));
                        areaID = sb.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void validation() {
        nameID = name.getText().toString();
        numberID = number.getText().toString();
        notesID = notes.getText().toString();
        getLocation();
        try {
            getMeetSumittionLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMeetSumittionLocation() {
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(SiteVisitUpdateActivity.this, null, "Loading please wait....");
        progressDialog.show();

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<MeetSubmitionRespons>> call = apiInterface.meetSubmittionLocationDetails(updaeID, customerID, nameID, numberID, notesID, areaID, latitudeStr, langitudeStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<MeetSubmitionRespons>>() {
            @Override
            public void onResponse(Call<ArrayList<MeetSubmitionRespons>> call, Response<ArrayList<MeetSubmitionRespons>> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<MeetSubmitionRespons> meetSubmitionResponses = response.body();
                    try {
                        if (meetSubmitionResponses!=null && meetSubmitionResponses.size() > 0) {
                            for (int i = 0; i < meetSubmitionResponses.size(); i++) {
                                MySharedPreferences.setPreference(SiteVisitUpdateActivity.this, "BUTTON_STATUS", "" + meetSubmitionResponses.get(i).getActivitystatus());
                                if (meetSubmitionResponses.get(i).getActivitystatus().equals("3")) {
                                    Intent intent = new Intent(SiteVisitUpdateActivity.this, SiteVisitFinalActivity.class);
                                    intent.putExtra("BUTTON_STATUS", meetSubmitionResponses.get(i).getActivitystatus());
                                    Log.e("STATUS", ""+meetSubmitionResponses.get(i).getActivitystatus());
                                    intent.putExtra("SITE_ID_1", meetSubmitionResponses.get(i).getSite_id());
                                    intent.putExtra("CUSTOMER_NAME", nameID);
                                    intent.putExtra("CUSTOMER_NUMBER", numberID);
                                    intent.putExtra("CUSTOMER_NOTES", notesID);
                                    intent.putExtra("CUSTOMER_ID", customerID);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SiteVisitUpdateActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(SiteVisitUpdateActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SiteVisitUpdateActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MeetSubmitionRespons>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SiteVisitUpdateActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }
}
