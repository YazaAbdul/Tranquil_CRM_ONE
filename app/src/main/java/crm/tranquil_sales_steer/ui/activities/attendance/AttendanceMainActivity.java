package crm.tranquil_sales_steer.ui.activities.attendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.GPSTracker;
import crm.tranquil_sales_steer.data.requirements.ImageUtil;
import crm.tranquil_sales_steer.data.requirements.LocationUtils;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.start_ups.LoginActivity;
import crm.tranquil_sales_steer.ui.models.DashboardChildResponse;
import crm.tranquil_sales_steer.ui.models.PunchInResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceMainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    RecyclerView attendanceMenuRCID;
    AttendanceMenuAdapter attendanceMenuAdapter;
    ArrayList<DashboardChildResponse> arrayList = new ArrayList<>();

    LinearLayout textDisplayTVID;

    LottieAnimationView loader;
    RelativeLayout punchViewRLID, punchFullViewRLID;
    TextView punchTVID;
    boolean isPunchIn = false;

    TextView punchTitleTVID, punchSubTVID, punchDesTVID, locationTVID;
    ImageView punchIMVID,click_image_id;
    LinearLayout punchDetailsLLID;
    RelativeLayout logoutRLID;
    String logoutStatus;
    CardView cardview_userpic,cardview_location;

    //location
    Location location;
    GPSTracker mGPS = null;
    String userID, dateStr, timeStr = "", locationStr = "", areaStr = "", reasonStr = "", punchIdStr;
    String timeType,pic;
    private static final int pic_id = 123;

    private CircleImageView img_icon;
    private String selectImagePath = "null";
    Uri selectImageUri;

    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_STORAGE = 2;

    AppCompatButton refreshBtn;
    ProgressBar locationPB;
    Bitmap photo;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};
    int PERMISSION_ALL = 1;
    private static final int REQUEST_CODE = 0;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    CardView punchCVID;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager lm;

    //Google Analytics
    private FirebaseAnalytics analytics;
    private int requestCode;
    private int resultCode;
    private Intent data;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_main);
        logoutStatus = MySharedPreferences.getPreferences(this, "" + AppConstants.LOGOUT_STATUS);
        punchIdStr = MySharedPreferences.getPreferences(this, "" + AppConstants.PUNCH_IN_ID);
        userID = MySharedPreferences.getPreferences(this, "USER_ID");
        pic = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_PROFILE_PIC);

        Utilities.startAnimation(this);
        analytics = FirebaseAnalytics.getInstance(this);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(v -> Utilities.finishAnimation(AttendanceMainActivity.this));

        logoutRLID = findViewById(R.id.logoutRLID);
        punchCVID = findViewById(R.id.punchCVID);
        textDisplayTVID = findViewById(R.id.textDisplayTVID);
        punchViewRLID = findViewById(R.id.punchViewRLID);
        click_image_id = findViewById(R.id.click_image_id);

        punchFullViewRLID = findViewById(R.id.punchFullViewRLID);
        punchTVID = findViewById(R.id.punchTVID);
        TextView nameTVID = findViewById(R.id.nameTVID);
        nameTVID.setText("" + MySharedPreferences.getPreferences(this, "USER_NAME"));
        punchViewRLID.setOnClickListener(this);

        punchDetailsLLID = findViewById(R.id.punchDetailsLLID);
        punchTitleTVID = findViewById(R.id.punchTitleTVID);
        punchSubTVID = findViewById(R.id.punchSubTVID);
        punchDesTVID = findViewById(R.id.punchDesTVID);
        punchIMVID = findViewById(R.id.punchIMVID);
        cardview_location=findViewById(R.id.cardview_location);
        cardview_userpic=findViewById(R.id.cardview_userpic);


        loader = findViewById(R.id.loader);
        CircleImageView userPic = findViewById(R.id.userPic);
        checkLocationPermission();
        try {
            Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(0).into(userPic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        attendanceMenuRCID = findViewById(R.id.attendanceMenuRCID);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        attendanceMenuRCID.setLayoutManager(layoutManager);

        //arrayList.add(new DashboardChildResponse("1", "Attendance", "", ""));
        //arrayList.add(new DashboardChildResponse("2", "Leaves", "", ""));
        //arrayList.add(new DashboardChildResponse("3", "Holidays", "", ""));
        //arrayList.add(new DashboardChildResponse("4", "Reports", "", ""));
        attendanceMenuAdapter = new AttendanceMenuAdapter(this, arrayList);
        attendanceMenuRCID.setAdapter(attendanceMenuAdapter);

        if (logoutStatus.equalsIgnoreCase(AppConstants.YES)) {
            logoutRLID.setVisibility(View.VISIBLE);
        } else {
            logoutRLID.setVisibility(View.GONE);
        }

        logoutRLID.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AttendanceMainActivity.this);
            builder1.setCancelable(false);
            builder1.setTitle(null);
            builder1.setMessage("Do You Want to Logout?");
            builder1.setPositiveButton("Yes", (dialogInterface, i) -> logout());
            builder1.setNegativeButton("No", null);
            builder1.create().show();
        });

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat month_date = new SimpleDateFormat("dd - MMMM - yyyy");
        String ma = month_date.format(calendar.getTime());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int a = calendar.get(Calendar.AM_PM);

        if (a == Calendar.AM) {
            timeType = "AM";
        } else {
            timeType = "PM";
        }

        dateStr = "" + year + "-" + (month + 1) + "-" + day;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        //toDayTVID.setText(dayOfTheWeek);

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                timeStr = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
            }

            public void onFinish() {

            }
        };

        locationTVID = findViewById(R.id.locationTVID);

        locationPB = findViewById(R.id.locationPB);
        refreshBtn = findViewById(R.id.refreshBtn);
        locationPB.setVisibility(View.GONE);
        refreshBtn.setOnClickListener(v -> {
            locationPB.setVisibility(View.VISIBLE);
            locationTVID.setVisibility(View.GONE);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        getLocation();
                    });
                }
            }, 2000, 2000);


        });

        newtimer.start();
        loaderStats();
        checkLocationPermission();

        lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setMessage(R.string.gps_network_not_enabled)
                    .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.Cancel,null)
                    .show();
        }


        img_icon = findViewById(R.id.img_icon);
        RelativeLayout choosePicRLID = findViewById(R.id.choosePicRLID);
        choosePicRLID.setOnClickListener(v -> {
            Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
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
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    public void getLocation() {

        locationPB.setVisibility(View.GONE);
        locationTVID.setVisibility(View.VISIBLE);
        String add = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address obj = addresses.get(0);
        add = obj.getAddressLine(0);
        locationStr = obj.getLocality();
        areaStr = obj.getSubLocality();
        locationTVID.setText(add);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission was denied")
                        .setMessage("Please allow location permission to continue")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AttendanceMainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return true;
        } else {
            return true;
        }
    }



    public void checkcamerapermissionAndOpenCamera(){
        if(ActivityCompat.checkSelfPermission(AttendanceMainActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(AttendanceMainActivity.this,new String[]{Manifest.permission.CAMERA},PICK_IMAGE);
          /*  if (ActivityCompat.checkSelfPermission(AttendanceMainActivity.this,Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
                Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, 0);
            }*/

        }else {
            Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, 0);
        }


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                                            String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, (LocationListener) this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            } case PERMISSION_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }
            }

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location location = LocationUtils.getMyLocation(this);
                    Log.v("", "onRequestPermissionsResult() location = " + location);
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    String add = "";
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                        Address obj = addresses.get(0);
                        add = obj.getAddressLine(0);
                        locationStr = obj.getLocality();
                        areaStr = obj.getSubLocality();
                        Log.v("", "onRequestPermissionsResult() locality = " + locationStr);
                        Log.v("", "onRequestPermissionsResult() locality = " + areaStr);
                        Toast.makeText(this, "Locality : " + locationStr + ", Area : " + areaStr + ", Lat : " + lat + ",Lng : " + lng, Toast.LENGTH_SHORT).show();
                        String userId = "1010";


                        locationTVID.setText(locationStr);


                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void logout() {
        MySharedPreferences.setPreference(AttendanceMainActivity.this, "USER_ID", "");
        MySharedPreferences.setPreference(AttendanceMainActivity.this, "USER_EMAIL", "");
        MySharedPreferences.setPreference(AttendanceMainActivity.this, "USER_MOBILE", "");
        MySharedPreferences.setPreference(AttendanceMainActivity.this, "USER_NAME", "");
        MySharedPreferences.setPreference(AttendanceMainActivity.this, "USER_TYPE", "");
        MySharedPreferences.setPreference(AttendanceMainActivity.this, "REG_COUNT", "");
        MySharedPreferences.setPreference(AttendanceMainActivity.this, AppConstants.LOGOUT_STATUS, "");
        Intent intent = new Intent(AttendanceMainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.finishAnimation(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.punchViewRLID:

                if (areaStr != null && !areaStr.isEmpty() && !areaStr.equals("null")) {
                    areaStr = areaStr;

                }
                else {
                    areaStr = "Area";
                }

                if (locationStr != null && !locationStr.isEmpty() && !locationStr.equals("null")) {
                    locationStr = locationStr;
                }
                else {
                    locationStr = "Location";
                }


               /* if (Utilities.isNetworkAvailable(this)) {

                    if(photo==null){
                        Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(camera_intent, 0);
                    }else if(photo!=null){


                    if (isPunchIn) {
                        newPunchInApi();
                        cardview_location.setVisibility(View.VISIBLE);
                        cardview_userpic.setVisibility(View.VISIBLE);
                    } else {
                        newPunchOutApi();
                    }
                } else {
                    Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }

                }*/
               /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Permission is already granted.
                    // You can proceed with using the camera.
                 //   Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission is not granted.
                    // You need to request the permission.
                //    Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
                   // ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);

                }*/
                checkcamerapermissionAndOpenCamera();
/* Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(camera_intent, 0);*/


                break;
            case R.id.textDisplayTVID:
                startActivity(new Intent(this, AttendanceMainActivity.class));
                break;

        }
    }




 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
        }
    }*/

    @SuppressLint("SetTextI18n")
    private void loaderStats() {
        String punchStatus = MySharedPreferences.getPreferences(this, ATTENDANCE);

        String punchInArea = MySharedPreferences.getPreferences(this, "" + AppConstants.PUNCH_IN_AREA);
        String punchInTime = MySharedPreferences.getPreferences(this, "" + AppConstants.PUNCH_IN_TIME);
        String punchOutArea = MySharedPreferences.getPreferences(this, "" + AppConstants.PUNCH_OUT_AREA);
        String punchOutTime = MySharedPreferences.getPreferences(this, "" + AppConstants.PUNCH_OUT_TIME);
        String lastPunched = MySharedPreferences.getPreferences(this, "" + AppConstants.ATTENDANCE_DATE);

        Log.e("punchStatus==>",punchStatus+" , "+punchInTime);

        punchViewRLID.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = () -> {
            punchDetailsLLID.setVisibility(View.VISIBLE);
            // TODO Auto-generated method stub
            if (punchStatus.equals("1")) {
                punchTitleTVID.setText("Punch Out");
                punchSubTVID.setText("Don't forget to Punch out");
                punchDesTVID.setText("Last Punch in at " + punchInTime);

                punchDesTVID.setTextColor(getResources().getColor(R.color.red_main));
                punchTitleTVID.setTextColor(getResources().getColor(R.color.red_main));
                punchIMVID.setColorFilter(ContextCompat.getColor(AttendanceMainActivity.this, R.color.red_main), PorterDuff.Mode.MULTIPLY);
                punchIMVID.getBackground().setColorFilter(Color.parseColor("#F65252"), PorterDuff.Mode.SRC_ATOP);

                isPunchIn = false;
                loader.setVisibility(View.GONE);
                punchViewRLID.setVisibility(View.VISIBLE);
                punchTVID.setText("Punch Out");
                punchViewRLID.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_punch_out_ring));
                punchFullViewRLID.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_punch_out));
            } else {
                punchTitleTVID.setText("Punch In");
                punchSubTVID.setText("You haven't check in yet.");
                punchDesTVID.setTextColor(getResources().getColor(R.color.green_main));
                punchTitleTVID.setTextColor(getResources().getColor(R.color.green_main));
                punchIMVID.getBackground().setColorFilter(Color.parseColor("#61CE65"), PorterDuff.Mode.SRC_ATOP);
                isPunchIn = true;
                loader.setVisibility(View.GONE);
                punchViewRLID.setVisibility(View.VISIBLE);
                punchTVID.setText("Punch In");
                punchViewRLID.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_punch_in_ring));
                punchFullViewRLID.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_punch_in));

                if (lastPunched.equals(dateStr)) {
                    punchDesTVID.setText("Last Punch Out at " + punchOutTime);
                } else {
                    punchDesTVID.setText("Have a Nice Day...");
                }
            }
        };

        mHandler.postDelayed(mRunnable, 10 * 10);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("attendance", "connected");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        Log.d("attendance", "dis connected");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("attendance", "bundle");
        locationPB.setVisibility(View.VISIBLE);
        refreshBtn.setVisibility(View.GONE);
        punchViewRLID.setVisibility(View.GONE);
        punchFullViewRLID.setVisibility(View.GONE);
        punchCVID.setVisibility(View.GONE);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000 * 60 * 2); // Update location every 2 minutes
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location newLocation) {

                locationPB.setVisibility(View.GONE);
                refreshBtn.setVisibility(View.GONE);
                punchViewRLID.setVisibility(View.VISIBLE);
                punchFullViewRLID.setVisibility(View.VISIBLE);
                punchCVID.setVisibility(View.VISIBLE);

                location=newLocation;
                getLocation();
            }
        });


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("attendance", "disconnected");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("attendance", "failed");
    }



    public class AttendanceMenuAdapter extends RecyclerView.Adapter<AttendanceMenuAdapter.PreSalesVH> {
        Context context;
        List<DashboardChildResponse> attendanceList = new ArrayList<>();
        private String[] colors = {"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#2196F3"};

        private int[] activityIcons = {R.drawable.menu_calls, R.drawable.menu_sale, R.drawable.menu_calls, R.drawable.menu_nego, R.drawable.menu_sale};

        public AttendanceMenuAdapter(Context context, List<DashboardChildResponse> attendanceList) {
            this.context = context;
            this.attendanceList = attendanceList;
        }

        @NonNull
        @Override
        public PreSalesVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new PreSalesVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_pre_sales_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PreSalesVH holder, @SuppressLint("RecyclerView") final int i) {
            holder.activityCountTVID.setText(attendanceList.get(i).getCount());
            holder.activityCountTVID.setVisibility(View.GONE);
            holder.activityNameTVID.setText(attendanceList.get(i).getActvty_typ_nm());
            Picasso.with(context).load(activityIcons[i % 5]).into(holder.activityIconID);
            holder.activityIconID.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            holder.activityLLID.setCardBackgroundColor(Color.parseColor(colors[i % 5]));

            holder.activityLLID.setOnClickListener(v -> {

                String activityTypeID = attendanceList.get(i).getActvty_typ_id();

                switch (activityTypeID) {
                    case "1":
                        startActivity(new Intent(context, AttendanceMainActivity.class));
                        break;

                   /* case "2":
                        startActivity(new Intent(context, LeavesActivity.class));
                        break;*/

                    /*case "3":
                        startActivity(new Intent(context, HolidaysActivity.class));
                        break;*/

                    /*case "4":
                        startActivity(new Intent(context, AttendanceReportActivity.class));
                        break;*/
                }

               /* Intent intent = new Intent(context, LeadViewActivity.class);
                intent.putExtra("ACTIVITY_ID", "" + attendanceList.get(i).getActvty_typ_id());
                intent.putExtra("ACTIVITY_NAME", "" + attendanceList.get(i).getActvty_typ_nm());
                startActivity(intent);*/
            });
        }

        @Override
        public int getItemCount() {
            return attendanceList.size();
        }

        public class PreSalesVH extends RecyclerView.ViewHolder {
            AppCompatImageView activityIconID;
            AppCompatTextView activityCountTVID, activityNameTVID;
            CardView activityLLID;

            public PreSalesVH(@NonNull View itemView) {
                super(itemView);
                activityIconID = itemView.findViewById(R.id.activityIconID);
                activityCountTVID = itemView.findViewById(R.id.activityCountTVID);
                activityNameTVID = itemView.findViewById(R.id.activityNameTVID);
                activityLLID = itemView.findViewById(R.id.activityLLID);
            }
        }
    }

    private void openImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PICK_IMAGE);
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
                    //newPunchInApi(selectImagePath);
                    Log.e("selectImagePath", ""+selectImagePath);

                }
                break;

            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        Uri temp = ImageUtil.getImageUri(this,bitmap);
                        if (temp!=null){
                            selectImagePath = ImageUtil.getRealPathFromURI(temp,this);
                            click_image_id.setImageBitmap(bitmap);
                            cardview_location.setVisibility(View.VISIBLE);
                            cardview_userpic.setVisibility(View.VISIBLE);

                            Log.e("click_image_id", ""+click_image_id);
                            newPunchInApi();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            case pic_id:
                if (requestCode == pic_id) {
                    // BitMap is data structure of image file which store the image in memory
                    photo = (Bitmap) data.getExtras().get("data");
                    // Set the image in imageview for display
                    click_image_id.setImageBitmap(photo);
                    Log.e("photo", ""+photo);
                }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 0, bmpStream);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
        Picasso.with(this).load(url).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).into(img_icon);

    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }

                break;
            }
        }
    }*/

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
        int targetW = img_icon.getWidth();
        int targetH = img_icon.getHeight();

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
            img_icon.setImageBitmap(bitmap);
        }
    }


    private String ATTENDANCE ="attendance";
    private void newPunchInApi() {

        punchViewRLID.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        RequestBody reqFile;
        final MultipartBody.Part imageBody;
        File file = new File(selectImagePath);

        String status = MySharedPreferences.getPreferences(this,ATTENDANCE);

        if (selectImagePath.equalsIgnoreCase("null")) {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            if (status.equals("1")){
                imageBody = MultipartBody.Part.createFormData("punchout_pic", "", reqFile);
            }else{
                imageBody = MultipartBody.Part.createFormData("punchin_pic", "", reqFile);
            }

        } else {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            if (status.equals("1")){
                imageBody = MultipartBody.Part.createFormData("punchout_pic", "", reqFile);
            }else{
                imageBody = MultipartBody.Part.createFormData("punchin_pic", "", reqFile);
            }
        }


           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);



        Call<PunchInResponse> call;

        Log.e("attendstatus", ""+status);
        if (status.equals("0")){
          call   = apiInterface.getPunchRequestResponse(imageBody, locationStr, "12");
        }else{
            call   = apiInterface.getPunchOut(imageBody, locationStr, "12");
        }

        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<PunchInResponse>() {
            @Override
            public void onResponse(Call<PunchInResponse> call, Response<PunchInResponse> response) {
                PunchInResponse allStatusResponses = response.body();

                Log.e("response","success "+new Gson().toJson(allStatusResponses));

                if (response.body() != null && response.code() == 200) {


                    Log.e("response","success "+new Gson().toJson(allStatusResponses));

                    if (status.equals("0")){
                        MySharedPreferences.setPreference(AttendanceMainActivity.this,ATTENDANCE,"1");
                    }else{
                        MySharedPreferences.setPreference(AttendanceMainActivity.this,ATTENDANCE,"0");
                    }

                    MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.PUNCH_IN_TIME, "" + timeStr + " " + timeType);
                    MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.ATTENDANCE_DATE, "" + dateStr);
                    MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.PUNCH_IN_AREA, "" + locationTVID.getText().toString());
                    loaderStats();
                   // Utilities.showToast(AttendanceMainActivity.this,allStatusResponses.getTexmsg());
                   /* if (allStatusResponses.getStatus().equals("1")) {
                        MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.PUNCH_IN_ID, "" + allStatusResponses.getPunchinid());
                        MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.PUNCH_IN_STATUS, "" + allStatusResponses.getPunchiostatus());
                        MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.ATTENDANCE_DATE, "" + dateStr);
                        MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.PUNCH_IN_AREA, "" + locationTVID.getText().toString());
                        MySharedPreferences.setPreference(AttendanceMainActivity.this, "" + AppConstants.PUNCH_IN_TIME, "" + timeStr + " " + timeType);
                        loaderStats();
                        //Picasso.with(AttendanceMainActivity.this).load(R.drawable.pic_d).into(img_icon);
                        selectImagePath="null";
                        Utilities.showToast(AttendanceMainActivity.this, "" + allStatusResponses.getTexmsg());
                    } else {
                        punchViewRLID.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                        Utilities.showToast(AttendanceMainActivity.this, "" + allStatusResponses.getTexmsg());

                    }*/
                } else {
                    punchViewRLID.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                    Utilities.showToast(AttendanceMainActivity.this, "Something went wrong");

                }
            }

            @Override
            public void onFailure(Call<PunchInResponse> call, Throwable t) {
                punchViewRLID.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
                Utilities.showToast(AttendanceMainActivity.this, "Error : " + t.getMessage());

            }
        });
    }


}
