package crm.tranquil_sales_steer.ui.activities.user_profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.AllStatusResponse;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout backRLID;
    String nameStr, companyStr, mobileStr, userID, companyID,pic;
    //image upload
    private String selectImagePath = "null";
    Uri selectImageUri;
    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_STORAGE = 2;
    public static final int REQUEST_IMAGE_CAPTURE_USER = 0;
    public static String fileName;
    CircleImageView userPic;
    RelativeLayout picImageRLID;

    EditText nameETID, emailETID, mobileETID;
    KProgressHUD hud;
    Button updateBtn;
    String picUrl;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        AlertUtilities.startAnimation(this);
        backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtilities.finishAnimation(EditProfileActivity.this);
            }
        });

        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Edit Profile");

        nameStr = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_NAME);
        companyStr = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_NAME);
        mobileStr = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        pic = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_PROFILE_PIC);



        userPic = findViewById(R.id.userPic);
        Picasso.with(this).load(pic).error(R.drawable.pic_d).rotate(0).into(userPic);
        picImageRLID = findViewById(R.id.picImageRLID);
        picImageRLID.setOnClickListener(this);

        nameETID = findViewById(R.id.nameETID);
        emailETID = findViewById(R.id.emailETID);
        mobileETID = findViewById(R.id.mobileETID);

        nameETID.setText(nameStr);
        emailETID.setText(companyStr);
        mobileETID.setText(mobileStr);

        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);
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
                Dexter.withActivity(EditProfileActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
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
                if (ContextCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EditProfileActivity.this,
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

    @SuppressLint("IntentReset")
    private void openImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PICK_IMAGE);
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
                    loadProfile(selectImageUri.toString());
                    //picTitleTVID.setText("1 Image selected");
                    //decodeImage(selectImagePath);
                }
                break;

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

                break;

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

         picUrl=url;

        Log.d("loadProfile", "Image cache path: " + url);
        //picTitleTVID.setText("1 File Selected");

        Picasso.with(this).load(url).rotate(0).into(userPic);


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertUtilities.finishAnimation(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picImageRLID:
                alertDisplay();
                break;
            case R.id.updateBtn:

                nameStr=nameETID.getText().toString();
                companyStr=emailETID.getText().toString();


                if (Utilities.isNetworkAvailable(this)){

                    editCompanyResponse(nameStr,companyStr);
                }else{
                    AlertUtilities.bottomDisplayStaticAlert(this,"No Internet connection","Make sure your device is connected to internet");
                }

                break;
        }
    }


    private void editCompanyResponse(String name,String company) {

        RequestBody reqFile;
        final MultipartBody.Part imageBody;
        File file = new File(selectImagePath);

        if (selectImagePath.equalsIgnoreCase("null")) {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            imageBody = MultipartBody.Part.createFormData("upload_logo", "", reqFile);
        } else {
            reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            imageBody = MultipartBody.Part.createFormData("upload_logo", file.getName(), reqFile);
        }

        showProgressDialog();

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllStatusResponse>> call = apiInterface.updateUserProfile(imageBody, userID,companyID,companyStr,nameStr);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllStatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllStatusResponse>> call, Response<ArrayList<AllStatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<AllStatusResponse> leadResponse = response.body();
                    if (leadResponse.size() > 0) {
                        for (int i = 0; i < leadResponse.size(); i++) {
                            if (leadResponse.get(i).getStatus() == 1) {
                                MySharedPreferences.setPreference(EditProfileActivity.this,""+AppConstants.SharedPreferenceValues.USER_NAME,""+nameStr);
                                MySharedPreferences.setPreference(EditProfileActivity.this,""+AppConstants.SharedPreferenceValues.USER_COMPANY_NAME,""+companyStr);
                                MySharedPreferences.setPreference(EditProfileActivity.this,""+AppConstants.SharedPreferenceValues.USER_PROFILE_PIC,""+picUrl);
                                AlertUtilities.SuccessAlertDialog(EditProfileActivity.this, "Details updated", "done.json");
                            } else {
                                Utilities.AlertDaiolog(EditProfileActivity.this, getString(R.string.opps), "failed", 1, null, "OK");
                            }
                        }
                    } else {

                        Utilities.AlertDaiolog(EditProfileActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                    }
                } else {
                    Utilities.AlertDaiolog(EditProfileActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllStatusResponse>> call, Throwable t) {
                dismissProgressDialog();
             //   Utilities.AlertDaiolog(EditProfileActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
            }
        });
    }


    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(this)
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

}
