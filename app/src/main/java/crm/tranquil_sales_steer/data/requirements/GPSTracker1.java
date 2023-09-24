package crm.tranquil_sales_steer.data.requirements;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Chandu at Tranquil
 */
@SuppressLint("ShowToast")
public class GPSTracker1 implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final Context mContext;
    private GoogleApiClient googleApiClient;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    //private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 sec

    public GPSTracker1(Context context) {
        this.mContext = context;
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }

        getLocation();
    }

    /**
     * Function to get the user's current location
     */

    @TargetApi(23)
    public Location getLocation() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = null;

        try {
            //locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
                // getting GPS status
                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                    Log.d("VENKEI-->", "No network provider enabled");
                } else {
                    Location locationGPS;
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (locationGPS != null) {
                            location = locationGPS;
                        }
                    }

                    if (location == null) {
                        Location locationNP;
                        // if GPS disabled get location from Network Provider
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("Network", "Network");
                            locationNP = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (locationNP != null) {
                                location = locationNP;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d("VENKEI-->", "onLocationChanged() :: Lat -> " + location.getLatitude() + " :: Lon -> " + location.getLongitude());
        }
        getLocation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("VENKEI-->", "provider disabled:: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("VENKEI-->", "provider enabled:: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("VENKEI-->", "provider status changed " + provider);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Location location11 = getLocation();
        if (location11 != null) {
            float accuracy = location11.getAccuracy();
            Log.e("VENKEI-->", "Location Accuracy = " + accuracy);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("VENKEI-->", "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("VENKEI-->", "GoogleApiClient connection has failed");
    }
}