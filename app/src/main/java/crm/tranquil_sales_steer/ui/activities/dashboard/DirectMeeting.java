 package crm.tranquil_sales_steer.ui.activities.dashboard;

 import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import crm.tranquil_sales_steer.ui.activities.attendance.AttendanceMainActivity;
import crm.tranquil_sales_steer.ui.activities.start_ups.LoginActivity;
import crm.tranquil_sales_steer.ui.models.DashboardChildResponse;
import crm.tranquil_sales_steer.ui.models.DirectModelResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

 public class DirectMeeting extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


     ArrayList<DashboardChildResponse> arrayList = new ArrayList<>();
     LinearLayout mainViewRLID;
     LinearLayout textDisplayTVID;
     LottieAnimationView loader;
     TextView locationTVID;

     RelativeLayout okRLID;
     String logoutStatus;

     //location
     Location location;
     GPSTracker mGPS = null;
     String userID, dateStr, timeStr = "", locationStr = "", areaStr = "", reasonStr = "", punchIdStr;
     String timeType;
     String nameStr, mobileStr, companystr;


     private CircleImageView img_icon;
     private String selectImagePath = "null";
     Uri selectImageUri;

     private static final int PICK_IMAGE = 100;
     private static final int PERMISSION_STORAGE = 2;

     AppCompatButton refreshBtn;
     ProgressBar locationPB;

     String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE};
     int PERMISSION_ALL = 1;
     private static final int REQUEST_CODE = 0;


     private GoogleApiClient mGoogleApiClient;
     private LocationRequest mLocationRequest;

     EditText etCompanyName, etContactNumber, etCustomerName;
     private Button btnSubmit;
     RadioGroup radioGroup1;
     RadioButton radio1, radio2;
     String submitType = "0";
     String lead_source_type = "0";
     boolean isSpinnerClicked = false;

     public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
     LocationManager lm;

     DirectModelResponse directModelResponse;
     KProgressHUD kProgressHUD;
     RelativeLayout imageRLID;
     AppCompatImageView imageID;
     private int requestCode;
     private String[] permissions;
     private int[] grantResults;

     Bitmap bitmap;

     private FirebaseAnalytics analytics;

     @SuppressLint("SetTextI18n")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_direct_meeting);

         Utilities.setStatusBarGradiant(this);
         analytics = FirebaseAnalytics.getInstance(this);

         userID = MySharedPreferences.getPreferences(this, "USER_ID");

         etCompanyName = findViewById(R.id.etCompanyName);
         mainViewRLID = findViewById(R.id.mainViewRLID);
         etContactNumber = findViewById(R.id.etContactNumber);
         etCustomerName = findViewById(R.id.etCustomerName);
         btnSubmit = findViewById(R.id.btnSubmit);
         imageRLID = findViewById(R.id.imageRLID);
         imageID = findViewById(R.id.imageID);


         logoutStatus = MySharedPreferences.getPreferences(this, "" + AppConstants.LOGOUT_STATUS);
         punchIdStr = MySharedPreferences.getPreferences(this, "" + AppConstants.PUNCH_IN_ID);
         userID = MySharedPreferences.getPreferences(this, "USER_ID");

         Utilities.startAnimation(this);
         RelativeLayout backRLID = findViewById(R.id.backRLID);
         backRLID.setOnClickListener(v -> Utilities.finishAnimation(DirectMeeting.this));

         okRLID = findViewById(R.id.okRLID);
         textDisplayTVID = findViewById(R.id.textDisplayTVID);

         TextView nameTVID = findViewById(R.id.nameTVID);
         nameTVID.setText("" + MySharedPreferences.getPreferences(this, "USER_NAME"));


         loader = findViewById(R.id.loader);
         CircleImageView userPic = findViewById(R.id.userPic);
        // Picasso.with(this).load(MySharedPreferences.getPreferences(this, "USER_PIC")).into(userPic);

         try {
             Picasso.with(this).load(MySharedPreferences.getPreferences(this, "USER_PIC")).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(90).into(userPic);
             // Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(90).into(userPicID);
         } catch (Exception e) {
             e.printStackTrace();
         }


         GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

         etContactNumber.addTextChangedListener(new TextWatcher() {
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
                     //searchCustomer(number);
                 }
             }
         });

         //arrayList.add(new DashboardChildResponse("1", "Attendance", "", ""));
         //arrayList.add(new DashboardChildResponse("2", "Leaves", "", ""));
         //arrayList.add(new DashboardChildResponse("3", "Holidays", "", ""));
         //arrayList.add(new DashboardChildResponse("4", "Reports", "", ""));

         /*if (logoutStatus.equalsIgnoreCase(AppConstants.YES)) {
             logoutRLID.setVisibility(View.VISIBLE);
         } else {
             logoutRLID.setVisibility(View.GONE);
         }*/

        /* logoutRLID.setOnClickListener(v -> {
             AlertDialog.Builder builder1 = new AlertDialog.Builder(DirectMeeting.this);
             builder1.setCancelable(false);
             builder1.setTitle(null);
             builder1.setMessage("Do You Want to Logout?");
             builder1.setPositiveButton("Yes", (dialogInterface, i) -> logout());
             builder1.setNegativeButton("No", null);
             builder1.create().show();
         });*/

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


         img_icon = findViewById(R.id.img_icon);
             /*RelativeLayout choosePicRLID = findViewById(R.id.choosePicRLID);
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
             });*/

         radioGroup1 = findViewById(R.id.radioGroup1);
         radio1 = findViewById(R.id.radio1);
         radio2 = findViewById(R.id.radio2);

         okRLID.setOnClickListener(v -> {

             if (radio1.isChecked()) {

                 submitType = "1";
                 lead_source_type = "3";
                 isSpinnerClicked = true;
             }

             if (radio2.isChecked()) {
                 submitType = "2";
                 isSpinnerClicked = true;
             }



             Log.e("submitType==>", submitType);


             validations();

         });

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                 .addApi(LocationServices.API)
                 .addConnectionCallbacks(this)
                 .addOnConnectionFailedListener(this)
                 .build();

         checkLocationPermission();

         lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         boolean gps_enabled = false;
         boolean network_enabled = false;

         try {
             gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
         } catch (Exception ex) {
         }

         try {
             network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
         } catch (Exception ex) {
         }

         if (!gps_enabled && !network_enabled) {
             // notify user
             new android.app.AlertDialog.Builder(this)
                     .setMessage(R.string.gps_network_not_enabled)
                     .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                             startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                         }
                     })
                     .setNegativeButton(R.string.Cancel, null)
                     .show();
         }

         imageRLID.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (Utilities.isNetworkAvailable(DirectMeeting.this)) {
                     Dexter.withActivity(DirectMeeting.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
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

                    // alertDisplay();
                 } else {
                     //AlertUtilities.bottomDisplayStaticAlert(LeadCreateActivity.this, "No Internet connection..", "Make sure your device is connected to internet");
                 }
             }
         });
     }



     public void showProgress(String msg) {
         kProgressHUD = KProgressHUD.create(this)
                 .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                 .setLabel(msg)
                 .setDetailsLabel("Please wait")
                 .setCancellable(false)
                 .setAnimationSpeed(2)
                 .setDimAmount(0.5f)
                 .show();
     }

     public void dismissProgress() {
         kProgressHUD.dismiss();
     }


     private void validations() {
         nameStr = etCustomerName.getText().toString();
         mobileStr = etContactNumber.getText().toString();
         companystr = etCompanyName.getText().toString();

         // passwordStr = passwordETID.getText().toString();

         if (TextUtils.isEmpty(nameStr)) {
             Utilities.showToast( this, "Enter name");
             return;
         }


         if (TextUtils.isEmpty(mobileStr)) {
             Utilities.showToast(this, "Enter mobile number");
             return;
         }

         if (mobileStr.length() <= 9) {
             Utilities.showToast(this, "Enter valid mobile number");
             return;
         }

         if (TextUtils.isEmpty(companystr)) {
             Utilities.showToast( this, "Enter CompanyName");
             return;
         }

         if(radioGroup1.getCheckedRadioButtonId() == -1) {
             Toast.makeText(this, "Choose interested/Not interested", Toast.LENGTH_SHORT).show();
             return;
         } else {
             // user selected one radio button.
         }

         Log.e("selectImagePath==>", selectImagePath);

         if (selectImagePath.equalsIgnoreCase("null")) {
             /*Utilities.customMessage(mainViewRLID, this, "Upload Image");
             return;*/
             //imageview has image

             Utilities.showToast( this, "Upload Image");
             return;


         }else {
             //Utilities.customMessage(mainViewRLID, this, "Upload Image");
             /*// imageview has no image
             Utilities.showToast( this, "Upload Image");
             return;*/
         }

         if (Utilities.isNetworkAvailable(this)) {
             if (isSpinnerClicked) {
                 calldirectmeetingApi(nameStr, mobileStr, companystr, submitType);
             } else {
             }

         }


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

         try {
             Address obj = addresses.get(0);
             add = obj.getAddressLine(0);
             locationStr = obj.getLocality();
             areaStr = obj.getSubLocality();
             locationTVID.setText(add);
         } catch (Exception e) {
             e.printStackTrace();
         }
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
                 new android.app.AlertDialog.Builder(this)
                         .setTitle("Location Permission was denied")
                         .setMessage("Please allow location permission to continue")
                         .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 //Prompt the user once explanation has been shown
                                 ActivityCompat.requestPermissions(DirectMeeting.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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

     @Override
     public void onRequestPermissionsResult(int requestCode,
                                            String permissions[], int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
             }
             case PERMISSION_STORAGE: {
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
         MySharedPreferences.setPreference(DirectMeeting.this, "USER_ID", "");
         MySharedPreferences.setPreference(DirectMeeting.this, "USER_EMAIL", "");
         MySharedPreferences.setPreference(DirectMeeting.this, "USER_MOBILE", "");
         MySharedPreferences.setPreference(DirectMeeting.this, "USER_NAME", "");
         MySharedPreferences.setPreference(DirectMeeting.this, "USER_TYPE", "");
         MySharedPreferences.setPreference(DirectMeeting.this, "REG_COUNT", "");
         MySharedPreferences.setPreference(DirectMeeting.this, AppConstants.LOGOUT_STATUS, "");
         Intent intent = new Intent(DirectMeeting.this, LoginActivity.class);
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

                 } else {
                     areaStr = "Area";
                 }

                 if (locationStr != null && !locationStr.isEmpty() && !locationStr.equals("null")) {
                     locationStr = locationStr;
                 } else {
                     locationStr = "Location";
                 }


                 break;

             case R.id.textDisplayTVID:
                 startActivity(new Intent(this, AttendanceMainActivity.class));
                 break;
         }
     }

     @SuppressLint("SetTextI18n")


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
         okRLID.setVisibility(View.GONE);


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
                 refreshBtn.setVisibility(View.VISIBLE);
                 okRLID.setVisibility(View.VISIBLE);

                 location = newLocation;
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

     @SuppressLint("IntentReset")
     private void openImage() {
         Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         pickIntent.setType("image/*");
         startActivityForResult(pickIntent, PICK_IMAGE);
     }


     private void image() {

         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
         //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

         /*if (intent.resolveActivity(getPackageManager()) != null) {
             File photoFile = null;
             try {
                 photoFile = createImageFile();
             } catch (IOException ex) {
                 ex.printStackTrace();
             }

             if (photoFile != null) {
                 Uri photoURI = FileProvider.getUriForFile(this,
                         "crm.tranquil_sales_steer.fileprovider",
                         photoFile);
                 intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                 startActivityForResult(intent, 0);
             }
         }*/

         startActivityForResult(intent, 0);




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
                     Log.e("selectImagePath==>","selectImagePath "+selectImagePath);
                     decodeImage(selectImagePath);
                     //changeProfilePic(selectImagePath);
                 }
                 break;

             case 0:
                 if (resultCode == Activity.RESULT_OK) {

                     try {
                         Bundle extras = data.getExtras();
                         bitmap = (Bitmap) extras.get("data");

                         if (bitmap!=null){
                             imageID.setImageBitmap(bitmap);
                             Uri temp= ImageUtil.getImageUri(this, bitmap);
                             File path = new File(ImageUtil.getRealPathFromURI(temp, this));
                             selectImagePath = path.getPath();
                             Log.e("selectImagePath==>","selectImagePath "+selectImagePath);
                         }
                     } catch (Exception e) {
                         e.printStackTrace();
                         Log.e("selectImagePath==>","error "+e.getMessage());
                     }


                    /* try {

                         Bundle extras = data.getExtras();



                         Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                         //picTitleTVID.setText("1 Image Captured");
                         Uri tempUri = getImageUri(getApplicationContext(), bitmap);


                         bitmap = (Bitmap) extras.get("data");

                         imageID.setImageBitmap(bitmap);

                         selectImagePath = getRealPathFromURI2(tempUri);


                         Log.e("selectImagePath==>","selectImagePath "+selectImagePath);

                         System.out.println("image_path==>" +selectImagePath);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }*/
                 }

                 break;


         }
     }

     private File createImageFile() throws IOException {
         String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
         String imageFileName = "JPEG_" + timeStamp + "_";
         File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
         File imageFile = File.createTempFile(
                 imageFileName,
                 ".jpg",
                 storageDir
         );
         selectImagePath = imageFile.getAbsolutePath();
         return imageFile;
     }



     public Uri getImageUri(Context inContext, Bitmap inImage) {
        /* ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
         inImage.compress(Bitmap.CompressFormat.JPEG, 0, bmpStream);
         String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
         return Uri.parse(path);*/

         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
         String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

         Log.e("uri_path==>","" + path);

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


    /* public Uri getImageUri2(Context inContext, Bitmap inImage) {
         Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
         String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
         return Uri.parse(path);
     }*/


   /*  public String getRealPathFromURI2(Uri uri) {

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
     }*/

     @SuppressLint("SetTextI18n")
     private void loadProfile(String url) {



         Log.d("", "Image cache path: " + url);
         //picTitleTVID.setText("1 File Selected");

         Picasso.with(this).load(url).error(R.drawable.pic_d).rotate(0).into(imageID);

         /*Log.d("", "Image cache path: " + url);
         //picTitleTVID.setText("1 File Selected");
         Picasso.with(this).load(url).resize(700, 400).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).into(imageID);*/
     }

     private String getRealPathFromURI(Uri selectImageUri) {
         @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(selectImageUri, null, null, null, null);
         if (cursor == null) {
             return selectImageUri.getPath();
         } else {
             cursor.moveToFirst();
             int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
             return cursor.getString(idx);
         }
     }

     private void decodeImage(String selectImagePath) {
         Log.e("selectImagePath==>","decodeImage "+selectImagePath);
         int targetW = imageID.getWidth();
         int targetH = imageID.getHeight();

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
             imageID.setImageBitmap(bitmap);
         }
     }


     /*@Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == PERMISSION_STORAGE) {// If request is cancelled, the result arrays are empty.
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 openImage();
             }
         }

     }*/



     private void calldirectmeetingApi(String nameStr, String mobileStr, String companystr, String submitType) {

         RequestBody reqFile;
         final MultipartBody.Part imageBody;

         File file = new File(selectImagePath);

         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
         byte[] byteArray = byteArrayOutputStream.toByteArray();

// Create a request body for the image file
         RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/png"), byteArray);

// Create multipart body part for the image
         MultipartBody.Part imagePart = MultipartBody.Part.createFormData("upload_pic", "image.png", imageRequestBody);

// Create the request body containing the multipart data
         RequestBody requestBody = new MultipartBody.Builder()
                 .setType(MultipartBody.FORM)
                 .addPart(imagePart)
                 .build();


         if (selectImagePath.equalsIgnoreCase("null")) {
             reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
             imageBody = MultipartBody.Part.createFormData("upload_pic", "", reqFile);
             Log.e("api==>", String.valueOf(imageBody));
         } else {
             reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
             imageBody = MultipartBody.Part.createFormData("upload_pic", file.getName(), reqFile);
         }

            ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
         Call<DirectModelResponse> call = apiInterface.directMeeting(imageBody,nameStr, companystr, locationTVID.getText().toString(), mobileStr, userID, lead_source_type);
         Log.e("api==>", call.request().toString());
         call.enqueue(new Callback<DirectModelResponse>() {
             @Override
             public void onResponse(Call<DirectModelResponse> call, Response<DirectModelResponse> response) {

                 Log.e("direct_meet==>","" + "yes");

                 if (response.body() != null & response.code() == 200) {

                     directModelResponse = response.body();
                     if (directModelResponse.getStatus() == 1 && submitType.equalsIgnoreCase("1")) {
                         Log.e("direct_meet==>","" + "Created");
                         Toast.makeText(DirectMeeting.this, "Lead Created Successfully", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(DirectMeeting.this, DirectMeeting.class);
                         startActivity(intent);
                         finish();
                     }if (directModelResponse.getStatus() == 1 && submitType.equalsIgnoreCase("2")) {
                         Toast.makeText(DirectMeeting.this, "Successfully Submitted", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(DirectMeeting.this, DirectMeeting.class);
                         startActivity(intent);
                         finish();
                     } else if (directModelResponse.getStatus() == 0){
                         Log.e("direct_meet==>","" + "Not Created");
                         Toast.makeText(DirectMeeting.this, "Lead already exist", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(DirectMeeting.this, DirectMeeting.class);
                         startActivity(intent);
                         finish();
                     }

                 }else {
                     Log.e("direct_meet==>","" + "No Response");
                     Toast.makeText(DirectMeeting.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<DirectModelResponse> call, Throwable t) {

                 Log.e("direct_meet==>","" + "Error");

             }
         });
     }


 }
