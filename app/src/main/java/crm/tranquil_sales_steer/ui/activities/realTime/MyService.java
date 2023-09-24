package crm.tranquil_sales_steer.ui.activities.realTime;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.DashBoardActivity;
import crm.tranquil_sales_steer.ui.models.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyService extends Service implements LocationListener {
    final String TAG = "MyService==>";
    public static boolean isServiceRunning;
    final String CHANNEL_ID = "NOTIFICATION_CHANNEL";


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
    private static final long TIME_IN_MINUS =60000 * 2; // 1 minute


    protected LocationManager locationManager;


    public MyService() {
        Log.e(TAG, "constructor called");
        isServiceRunning = false;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate called");
        createNotificationChannel();
        isServiceRunning = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand called");
        Intent notificationIntent = new Intent(this, DashBoardActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Getting Location...")
                .setSmallIcon(R.drawable.favicon)
                .setContentIntent(pendingIntent)
                .setColor(getResources().getColor(R.color.purple_200))
                .build();

        startForeground(1, notification);
        // getLocation();
        timer.schedule(doAsynchronousTask, 0, TIME_IN_MINUS);
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String appName = getString(R.string.app_name);
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    appName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy called");
        isServiceRunning = false;
        stopForeground(true);

        // call MyReceiver which will restart this service via a worker
        Intent broadcastIntent = new Intent(this, MyReceiver.class);
        sendBroadcast(broadcastIntent);

        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e(TAG, "onDestroy called");
        isServiceRunning = false;
        stopForeground(true);

        // call MyReceiver which will restart this service via a worker
        Intent broadcastIntent = new Intent(this, MyReceiver.class);
        sendBroadcast(broadcastIntent);

        super.onTaskRemoved(rootIntent);
    }


    //location
    @SuppressLint("MissingPermission")
    public Location getLocation() {

        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Log.e("isGPSEnabled==>", ""+ false);

            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e(TAG, "Network");
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
        }

        return location;
    }

    final Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask doAsynchronousTask = new TimerTask() {
        @Override
        public void run() {
            handler.post(() -> getLocation());
        }
    };


    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.e(TAG, "onLocationChanged");
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
        Log.e(TAG, "onLocationChanged");
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
        Log.e(TAG, "onFlushComplete");
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

            Thread thread = new Thread() {
                @Override
                public void run() {
                   /* LogsEntity logsEntity = new LogsEntity(0, lat, longi, date(), address);
                    Application.database.getLogsDao().Insert(logsEntity);*/
                    //api
                    insertApi(userID,address,city,longi,lat);
                }
            };

            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void insertApi(String userID,String address, String knownName, double longi, double lat) {
        ApiInterface apiInterface= ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<MessageResponse>>call = apiInterface.insertLocation(userID,address,knownName,String.valueOf(longi),String.valueOf(lat));
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

    private String date(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm:ss a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String localTime = date.format(currentLocalTime);
        Log.e("localTime==>",""+localTime);
        return localTime;
    }
}