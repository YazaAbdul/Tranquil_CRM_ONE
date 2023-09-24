/*
 * Copyright (c) 2019. This code has been developed by Fabio Ciravegna, The University of Sheffield. All rights reserved. No part of this code can be used without the explicit written permission by the author
 */

package crm.tranquil_sales_steer.ui.activities.realTime;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.realTime.utilities.Notification;
import crm.tranquil_sales_steer.ui.models.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Service extends android.app.Service implements LocationListener {
    protected static final int NOTIFICATION_ID = 1337;
    private static String TAG = "Service";
    private static Service mCurrentService;
    private int counter = 0;

    public Service() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        counter = 0;

        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * it starts the process in foreground. Normally this is done when screen goes off
     * THIS IS REQUIRED IN ANDROID 8 :
     * "The system allows apps to call Context.startForegroundService()
     * even while the app is in the background.
     * However, the app must call that service's startForeground() method within five seconds
     * after the service is created."
     */
    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("getUserLocation==>", "restarting foreground");
            try {
                Notification notification = new Notification();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, getResources().getString(R.string.app_name), "Getting Location...", R.drawable.crmappicon));
                Log.e("getUserLocation==>", "restarting foreground successful");
                startTimer();
            } catch (Exception e) {
                Log.e("getUserLocation==>", "Error in notification " + e.getMessage());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("getUserLocation==>", "onDestroy called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }


    /**
     * this is called when the process is killed by Android
     *
     * @param rootIntent
     */

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("getUserLocation==>", "onTaskRemoved called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        // do not call stoptimertask because on some phones it is called asynchronously
        // after you swipe out the app and therefore sometimes
        // it will stop the timer after it was restarted
        // stoptimertask();
    }


    /**
     * static to avoid multiple timers to be created when the service is called several times
     */
    private static Timer timer;
    private static TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        Log.e("getUserLocation==>", "Starting timer");

        //set a new Timer - if one is already running, cancel it to avoid two running at the same time
        stoptimertask();
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        Log.e("getUserLocation==>", "Scheduling...");
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, TIME_IN_MINUS); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> getUserLocation() );
            }
        };

       /* Log.e(TAG, "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                getUserLocation();



            }
        };*/
    }

    //location
    //location flags

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location

    double latitude; // latitude
    double longitude; // longitude

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 100 * 60 * 1; // 1 minute
    private static final long TIME_IN_MINUS = 60000 * 1; // 1 minute


    protected LocationManager locationManager;


    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        try {
            Log.e("getUserLocation==>", "in timer ++++  " + (counter++));
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.e("getUserLocation==>", "" + false);

            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {


                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e("getUserLocation==>", "Network");
                    if (locationManager != null) {
                        Location  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Double latitude = location.getLatitude();
                            Double   longitude = location.getLongitude();
                            insertInDb(latitude, longitude);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("getUserLocation==>",""+e.getMessage());
        }


    }

    public void insertInDb(double lat, double longi) {
        String address;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String userID= MySharedPreferences.getPreferences(this,"USER_ID");

            Log.e("knownName==>",""+city+" , "+knownName+" , "+userID);

            insertApi(userID,address,city,longi,lat);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("getUserLocation==>",""+e.getMessage());
        }
    }

    private void insertApi(String userID, String address, String knownName, double longi, double lat) {
        ApiInterface apiInterface= ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<MessageResponse>> call = apiInterface.insertLocation(userID,address,knownName,String.valueOf(longi),String.valueOf(lat));
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<MessageResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<MessageResponse>> call, Response<ArrayList<MessageResponse>> response) {
                Log.e("response==>",""+new Gson().toJson(response.body()));
                if (response.body()!=null && response.code()==200){
                    Log.e("insertApi==>","inserted");
                }else{
                    Log.e("insertApi==>","failed");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MessageResponse>> call, Throwable t) {
                Log.e("insertApi==>","onFailure : "+t.getMessage());
            }
        });
    }


    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static Service getmCurrentService() {
        return mCurrentService;
    }

    public static void setmCurrentService(Service mCurrentService) {
        Service.mCurrentService = mCurrentService;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }
}
