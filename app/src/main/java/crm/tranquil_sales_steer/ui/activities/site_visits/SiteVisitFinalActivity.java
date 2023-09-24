package crm.tranquil_sales_steer.ui.activities.site_visits;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import crm.tranquil_sales_steer.BuildConfig;
import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.gps_utilities.GPSTracker;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.EndPointResponse;
import crm.tranquil_sales_steer.ui.models.MeetingResponse;
import crm.tranquil_sales_steer.ui.models.SiteVisitStartResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteVisitFinalActivity extends AppCompatActivity implements View.OnClickListener {

    String nameID, numberID, vehicel, userID, locationID = "", areaID, customer_id;
    RelativeLayout bikeRL, carRL;
    private Boolean mRequestingLocationUpdates;
    Location location = null;
    GPSTracker mGPS = null;
    Button startEnableBtn, startDisableBtn, meetEnableBtn, meetDisableBtn, endEnableBtn, endDisableBtn;
    String updateid, activitystatus;
    String btnStatusStr;
    double latitude, langitude;
    String latitudeStr, langitudeStr;
    String btnStatusStr1;
    String siteID1;

    String typeOfVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit_final);

        getGeoLocation();
        TextView headerTittleTVID =  findViewById(R.id.headerTittleTVID);

        RelativeLayout backRLID =  findViewById(R.id.backRLID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vehicel = "bike";

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                nameID = bundle.getString("CUSTOMER_NAME");
                numberID = bundle.getString("CUSTOMER_NUMBER");
                //notesID = bundle.getString("CUSTOMER_NOTES");
                customer_id = bundle.getString("CUSTOMER_ID");
                siteID1 = bundle.getString("SITE_ID_1");
            }
        }

        typeOfVehicle= MySharedPreferences.getPreferences(SiteVisitFinalActivity.this, ""+ AppConstants.VEHICLE_TYPE);


        headerTittleTVID.setText(nameID + " MEET ");
        userID = MySharedPreferences.getPreferences(this, "USER_ID");
        bikeRL = findViewById(R.id.bikeRL);
        carRL = findViewById(R.id.carRL);
        bikeRL.setOnClickListener(this);
        carRL.setOnClickListener(this);

        startEnableBtn = findViewById(R.id.startEnableBtn);
        startDisableBtn = findViewById(R.id.startDisableBtn);
        meetEnableBtn = findViewById(R.id.meetEnableBtn);
        meetDisableBtn = findViewById(R.id.meetDisableBtn);
        endEnableBtn = findViewById(R.id.endEnableBtn);
        endDisableBtn = findViewById(R.id.endDisableBtn);

        startEnableBtn.setOnClickListener(this);
        startDisableBtn.setOnClickListener(this);
        meetEnableBtn.setOnClickListener(this);
        meetDisableBtn.setOnClickListener(this);
        endEnableBtn.setOnClickListener(this);
        endDisableBtn.setOnClickListener(this);

        btnStatusStr = MySharedPreferences.getPreferences(this, "BUTTON_STATUS");
        updateid = MySharedPreferences.getPreferences(this, "UPDATE_ID");


        if (btnStatusStr.equals("2")) {
            meetEnableBtn.setVisibility(View.GONE);
            meetDisableBtn.setVisibility(View.GONE);
            endDisableBtn.setVisibility(View.GONE);
            endEnableBtn.setVisibility(View.GONE);
            startDisableBtn.setVisibility(View.GONE);
            startEnableBtn.setVisibility(View.GONE);
        }

        if (btnStatusStr.equals("1")) {
            startEnableBtn.setVisibility(View.GONE);
            startDisableBtn.setVisibility(View.VISIBLE);
            meetDisableBtn.setVisibility(View.GONE);
            meetEnableBtn.setVisibility(View.VISIBLE);
            endDisableBtn.setVisibility(View.GONE);
            endEnableBtn.setVisibility(View.GONE);
        }

        if (btnStatusStr.equals("3")) {
            startEnableBtn.setVisibility(View.GONE);
            startDisableBtn.setVisibility(View.GONE);
            meetDisableBtn.setVisibility(View.GONE);
            meetEnableBtn.setVisibility(View.GONE);
            endDisableBtn.setVisibility(View.GONE);
            endEnableBtn.setVisibility(View.VISIBLE);
        }
        if (btnStatusStr.equals("0")) {
            startDisableBtn.setVisibility(View.GONE);
            startEnableBtn.setVisibility(View.VISIBLE);
            meetEnableBtn.setVisibility(View.GONE);
            meetDisableBtn.setVisibility(View.GONE);
            endEnableBtn.setVisibility(View.GONE);
            endDisableBtn.setVisibility(View.GONE);
        }



        try {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            mRequestingLocationUpdates = true;
                            //startLocationUpdates();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                // open device settings when the permission is
                                // denied permanently
                                openSettings();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    check();
        } catch (Exception e) {
            e.printStackTrace();
        }

        vehicel = AppConstants.VEHICLE_BIKE;
        //vehicel = AppConstants.VEHICLE_BIKE;
        if (typeOfVehicle.equalsIgnoreCase(AppConstants.VEHICLE_BIKE)){
            vehicel = AppConstants.VEHICLE_BIKE;
            carRL.setBackgroundResource(R.drawable.bg_un_selected_circel);
            bikeRL.setBackgroundResource(R.drawable.bg_selected_circel);
        }else if (typeOfVehicle.equalsIgnoreCase(AppConstants.VEHICLE_CAR)){
            vehicel = AppConstants.VEHICLE_CAR;
            carRL.setBackgroundResource(R.drawable.bg_selected_circel);
            bikeRL.setBackgroundResource(R.drawable.bg_un_selected_circel);
        }else if (typeOfVehicle.equals("0")){
            vehicel = AppConstants.VEHICLE_BIKE;
            carRL.setBackgroundResource(R.drawable.bg_un_selected_circel);
            bikeRL.setBackgroundResource(R.drawable.bg_selected_circel);
        }

    }

    private void getGeoLocation(){
        try {
            mGPS=new GPSTracker(this);
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
                        try {
                            locationID = addresses.get(0).getSubLocality();
                            Log.d("LOCATION_ID -- >", "" + locationID);
                            latitude = addresses.get(0).getLatitude();
                            langitude = addresses.get(0).getLongitude();
                            latitudeStr = String.valueOf(latitude);
                            langitudeStr = String.valueOf(langitude);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Address fullAddress = addresses.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < fullAddress.getMaxAddressLineIndex(); i++) {
                            sb.append(fullAddress.getAddressLine(i)).append(",\n");
                        }
                        sb.append(fullAddress.getAddressLine(0));
                        areaID = sb.toString();
                        locationID=sb.toString();
                        //Log.d("LOCATION_ID -- >",""+locationID);

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
    private void openSettings() {

        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carRL:
                vehicel = "car";
                carRL.setBackgroundResource(R.drawable.bg_selected_circel);
                bikeRL.setBackgroundResource(R.drawable.bg_un_selected_circel);
                break;

            case R.id.bikeRL:
                vehicel = "bike";
                carRL.setBackgroundResource(R.drawable.bg_un_selected_circel);
                bikeRL.setBackgroundResource(R.drawable.bg_selected_circel);
                break;

            case R.id.startEnableBtn:
                getGeoLocation();

                if (Utilities.isNetworkAvailable(SiteVisitFinalActivity.this)) {
                    try {
                        if (mGPS.getLocation() != null) {
                            getStartPointLocation();
                        } else {
                            final Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.location_alert);
                            TextView alertOkBtn = dialog.findViewById(R.id.alertOkBtn);

                            alertOkBtn.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onClick(View v) {
                                    String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                                    if (locationProviders == null || locationProviders.equalsIgnoreCase("")) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        dialog.dismiss();

                                    }
                                }
                            });

                            TextView alertCancelBtn = dialog.findViewById(R.id.alertCancelBtn);
                            alertCancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SiteVisitFinalActivity.this, "There is no internet connection...!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.meetEnableBtn:
                getGeoLocation();
                if (Utilities.isNetworkAvailable(SiteVisitFinalActivity.this)) {
                    try {
                        if (mGPS.getLocation() != null) {
                            getMeetPointLocation();
                        } else {
                            final Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.location_alert);
                            TextView alertOkBtn = dialog.findViewById(R.id.alertOkBtn);
                            alertOkBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                                    if (locationProviders == null || locationProviders.equalsIgnoreCase("")) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        dialog.dismiss();
                                    }
                                }
                            });
                            TextView alertCancelBtn = dialog.findViewById(R.id.alertCancelBtn);
                            alertCancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SiteVisitFinalActivity.this, "There is no internet connection.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.endEnableBtn:
                getGeoLocation();
                if (Utilities.isNetworkAvailable(SiteVisitFinalActivity.this)) {
                    try {
                        if (mGPS.getLocation() != null) {
                            getEndPointLocation();
                        } else {
                            final Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.location_alert);
                            TextView alertOkBtn = dialog.findViewById(R.id.alertOkBtn);
                            alertOkBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                                    if (locationProviders == null || locationProviders.equalsIgnoreCase("")) {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        dialog.dismiss();
                                    }
                                }
                            });
                            TextView alertCancelBtn = dialog.findViewById(R.id.alertCancelBtn);
                            alertCancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SiteVisitFinalActivity.this, "There is no internet connection...!", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.startDisableBtn:
                Toast.makeText(SiteVisitFinalActivity.this, "Please complete previous activity", Toast.LENGTH_SHORT).show();
                break;

            case R.id.meetDisableBtn:
                Toast.makeText(SiteVisitFinalActivity.this, "Please complete previous activity", Toast.LENGTH_SHORT).show();
                break;

            case R.id.endDisableBtn:
                Toast.makeText(SiteVisitFinalActivity.this, "Please complete previous activity", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void getStartPointLocation() {
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(SiteVisitFinalActivity.this, null, "Loading please wait...!");
        progressDialog.show();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SiteVisitStartResponse>> call = apiInterface.startPointLocationDetails(userID, locationID, areaID, vehicel, latitudeStr, langitudeStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SiteVisitStartResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SiteVisitStartResponse>> call, Response<ArrayList<SiteVisitStartResponse>> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<SiteVisitStartResponse> siteVisitStartResponses = response.body();
                    try {
                        if (siteVisitStartResponses.size() > 0) {
                            for (int i = 0; i < siteVisitStartResponses.size(); i++) {
                                MySharedPreferences.setPreference(SiteVisitFinalActivity.this, "BUTTON_STATUS", "" + siteVisitStartResponses.get(i).getActivitystatus());
                                if (siteVisitStartResponses.get(i).getActivitystatus().equals("1")) {
                                    updateid = siteVisitStartResponses.get(i).getUpdateid();
                                    MySharedPreferences.setPreference(SiteVisitFinalActivity.this, "UPDATE_ID", "" + updateid);
                                    MySharedPreferences.setPreference(SiteVisitFinalActivity.this, ""+ AppConstants.VEHICLE_TYPE, "" + vehicel);

                                    startEnableBtn.setVisibility(View.GONE);
                                    startDisableBtn.setBackgroundColor(R.drawable.bg_red_);
                                    startDisableBtn.setVisibility(View.VISIBLE);
                                    meetDisableBtn.setVisibility(View.GONE);
                                    meetEnableBtn.setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SiteVisitStartResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SiteVisitFinalActivity.this, "ERROR : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMeetPointLocation() {
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(SiteVisitFinalActivity.this, null, "Loading please wait...!");
        progressDialog.show();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<MeetingResponse>> call = apiInterface.meetPointLocationDetails(updateid, areaID, latitudeStr, langitudeStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<MeetingResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MeetingResponse>> call, Response<ArrayList<MeetingResponse>> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<MeetingResponse> meetingResponses = response.body();
                    try {
                        if (meetingResponses != null && meetingResponses.size() > 0) {
                            for (int i = 0; i < meetingResponses.size(); i++) {
                                if (meetingResponses.get(i).getActivitystatus().equals("2")) {
                                    MySharedPreferences.setPreference(SiteVisitFinalActivity.this, "BUTTON_STATUS", "" + meetingResponses.get(i).getActivitystatus());
                                    meetEnableBtn.setVisibility(View.GONE);
                                    meetDisableBtn.setVisibility(View.VISIBLE);
                                    endEnableBtn.setVisibility(View.VISIBLE);
                                    endDisableBtn.setVisibility(View.GONE);
                                    MySharedPreferences.setPreference(SiteVisitFinalActivity.this, "SITE_ID", "" + meetingResponses.get(i).getSite_id());
                                    Intent intent = new Intent(SiteVisitFinalActivity.this, SiteVisitUpdateActivity.class);
                                    intent.putExtra("SITE_ID", meetingResponses.get(i).getSite_id());
                                    intent.putExtra("CUSTOMER_NAME", nameID);
                                    intent.putExtra("CUSTOMER_NUMBER", numberID);
                                    intent.putExtra("CUSTOMER_ID", customer_id);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MeetingResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEndPointLocation() {
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(SiteVisitFinalActivity.this, null, "Loading please wait...!");
        progressDialog.show();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<EndPointResponse>> call = apiInterface.endPointLocationDetails(siteID1, areaID, latitudeStr, langitudeStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<EndPointResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<EndPointResponse>> call, Response<ArrayList<EndPointResponse>> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<EndPointResponse> endPointResponses = response.body();
                    try {
                        if (endPointResponses != null && endPointResponses.size() > 0) {
                            for (int i = 0; i < endPointResponses.size(); i++) {
                                if (endPointResponses.get(i).getActivitystatus().equals("0")) {
                                    MySharedPreferences.setPreference(SiteVisitFinalActivity.this, "BUTTON_STATUS", "" + endPointResponses.get(i).getActivitystatus());
                                    startDisableBtn.setVisibility(View.GONE);
                                    startEnableBtn.setVisibility(View.VISIBLE);
                                    meetEnableBtn.setVisibility(View.GONE);
                                    meetDisableBtn.setVisibility(View.VISIBLE);
                                    endEnableBtn.setVisibility(View.GONE);
                                    endDisableBtn.setVisibility(View.VISIBLE);
                                    MySharedPreferences.setPreference(SiteVisitFinalActivity.this, ""+ AppConstants.VEHICLE_TYPE, "0");
                                    Toast.makeText(SiteVisitFinalActivity.this, "Successfully completed your customer meet", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EndPointResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SiteVisitFinalActivity.this, "Something went wrong at server side", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                        locationID=sb.toString();
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
}
