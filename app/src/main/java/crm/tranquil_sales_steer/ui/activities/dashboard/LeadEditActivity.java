package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.data.requirements.statusBarUtilities;
import crm.tranquil_sales_steer.ui.fragments.LeadProfileFragment;
import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.AllStatusResponse;
import crm.tranquil_sales_steer.ui.models.RequirementResponse;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;

public class LeadEditActivity extends AppCompatActivity {

    TextInputEditText requirementOtherETID, firstNameETID, lastNameETID, emailETID, mobileETID, alternateETID, websiteETID, locationETID, cityETID,companyETID, gstETID;
    LinearLayout mainView;

    private KProgressHUD hud;
    String customerID, requirementOtherStr, userID, firstNameStr, lastNameStr, emailStr, mobileStr, websiteStr, locationStr,cityStr, companyStr, gstStr;
    TextView noDataID;

    ImageView header_image;
    RelativeLayout changeLeadPic;

    String selectImagePath = "null";
    Uri selectImageUri;

    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_STORAGE = 2;

    LinearLayout actRLID, souRLID, reqRLID, requirementOtherLLID;

    Dialog dialog;
    ExpandableHeightGridView dropDownLVID;
    TextView requirementTVID;
    String companyID, reqStr, selectedRequirementArrays, selectedRequirementNameArrays;

    ArrayList<RequirementResponse> requirementResponses = new ArrayList<>();
    //RequirementNewAdapter requirementAdapter;
    RequirementAdapter requirementAdapter;

    ArrayList<LeadProfileFragment.UserRequirements> userRequirements = new ArrayList<>();
    ProgressBar rePB;

    RelativeLayout otherReqID;
    boolean otherReqIsVisible = false;
    EditText otherReqETID;

    public static final int REQUEST_IMAGE_CAPTURE_USER = 0;
    public static String fileName;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_edit_new);
        //statusBarUtilities.statusBarSetup(this);
        Utilities.setStatusBarGradiant(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                customerID = bundle.getString("CUSTOMER_ID");
            }
        }

        companyID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Edit Lead");
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitleTextColor(Color.WHITE);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.colorHomeScreen2));

        noDataID = findViewById(R.id.noData);
        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Edit Lead");
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });
        RelativeLayout okRLID = findViewById(R.id.okRLID);
        okRLID.setVisibility(View.VISIBLE);

        userID = MySharedPreferences.getPreferences(LeadEditActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);


        mainView = findViewById(R.id.mainView);
        firstNameETID = findViewById(R.id.firstNameETID);
        lastNameETID = findViewById(R.id.lastNameETID);
        emailETID = findViewById(R.id.emailETID);
        mobileETID = findViewById(R.id.mobileETID);
        alternateETID = findViewById(R.id.alternateETID);
        websiteETID = findViewById(R.id.websiteETID);
        locationETID = findViewById(R.id.locationETID);
        cityETID = findViewById(R.id.cityETID);
        companyETID = findViewById(R.id.companyETID);
        gstETID = findViewById(R.id.gstETID);
        header_image = findViewById(R.id.header_image);
        changeLeadPic = findViewById(R.id.changeLeadPic);
        requirementOtherETID = findViewById(R.id.requirementOtherETID);

        changeLeadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(LeadEditActivity.this)) {
                    alertDisplay();
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "No Internet Connection..", "Make sure your device is connected to internet");
                }
            }
        });

        requirementOtherLLID = findViewById(R.id.requirementOtherLLID);
        requirementTVID = findViewById(R.id.requirementTVID);
        reqRLID = findViewById(R.id.reqRLID);
        reqRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivitiesAlert("REQUIREMENT");
            }
        });

        loadLeadProfileDetails(customerID);

    }

    /*@SuppressLint("ClickableViewAccessibility")
    private void showActivitiesAlert(final String type) {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.search_drop_down);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();
        Button saveRequirementBtn = dialog.findViewById(R.id.saveRequirementBtn);
        otherReqID = dialog.findViewById(R.id.otherReqID);
        otherReqETID = dialog.findViewById(R.id.otherReqETID);


        dropDownLVID = dialog.findViewById(R.id.dropDownLVID);
        dropDownLVID.setFocusable(false);
        rePB = dialog.findViewById(R.id.rePB);
        rePB.setVisibility(View.GONE);
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
                if (Utilities.isNetworkAvailable(LeadEditActivity.this)) {

                    if (otherReqIsVisible) {
                        requirementOtherStr = otherReqETID.getText().toString();
                        if (TextUtils.isEmpty(requirementOtherStr)) {
                            Toast.makeText(LeadEditActivity.this, "Please enter New Requirement Name", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            selectedRequirementArrays = "";
                            requirementTVID.setText(requirementOtherStr);
                        }

                    } else {
                        saveSelectedRequirements();
                    }

                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, getString(R.string.no_internet_title), getString(R.string.no_internet_desc));
                }
            }
        });
    }

    private void requirements() {
        rePB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<RequirementResponse>> call = apiInterface.getRequirements(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RequirementResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RequirementResponse>> call, Response<ArrayList<RequirementResponse>> response) {
                rePB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    requirementResponses = response.body();
                    if (requirementResponses != null && requirementResponses.size() > 0) {
                        for (int i = 0; i < requirementResponses.size(); i++) {
                            requirementAdapter = new RequirementNewAdapter(LeadEditActivity.this, requirementResponses);
                            dropDownLVID.setAdapter(requirementAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    } else {
                        Toast.makeText(LeadEditActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeadEditActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequirementResponse>> call, Throwable t) {
                rePB.setVisibility(View.GONE);
                Toast.makeText(LeadEditActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

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

        /*otherFieldLLID = dialog.findViewById(R.id.otherFieldLLID);
        otherFieldETID = dialog.findViewById(R.id.otherFieldETID);*/

        rePB = dialog.findViewById(R.id.drPB);
        rePB.setVisibility(View.GONE);
        final EditText searchHintETID = dialog.findViewById(R.id.searchHintETID);
        final LinearLayout searchLLID = dialog.findViewById(R.id.searchLLID);
        searchLLID.setFocusable(false);

        Button otherFieldSaveBtn = dialog.findViewById(R.id.otherFieldSaveBtn);
        otherFieldSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (type.equalsIgnoreCase("ACTIVITIES")) {

                    activityOtherStr = otherFieldETID.getText().toString();

                    if (otherActivityIsVisible) {
                        if (TextUtils.isEmpty(activityOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter Other Activity Name", Toast.LENGTH_SHORT).show();
                        } else {
                            activityTVID.setText(activityOtherStr);
                            dialog.dismiss();
                        }
                    }
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    sourceOtherStr = otherFieldETID.getText().toString();
                    if (otherSourceIsVisible) {
                        if (TextUtils.isEmpty(sourceOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter Other Source", Toast.LENGTH_SHORT).show();
                        } else {
                            customerSourceID.setText(sourceOtherStr);
                            dialog.dismiss();
                        }
                    }
                } else*/ if (type.equalsIgnoreCase("REQUIREMENT")) {

                    if (otherReqIsVisible) {
                        requirementOtherStr = otherReqETID.getText().toString();

                        if (TextUtils.isEmpty(requirementOtherStr)) {
                            Toast.makeText(LeadEditActivity.this, "Please enter New Requirement Name", Toast.LENGTH_SHORT).show();
                        } else {

                            selectedRequirementArrays = "";
                            requirementTVID.setText(requirementOtherStr);
                            dialog.dismiss();
                        }

                    }
                }

                /*}else if (type.equalsIgnoreCase("EMPLOYEE")) {
                    //sourceOtherStr = otherFieldETID.getText().toString();
                    //employeeTVID.setText(sourceOtherStr);
                    dialog.dismiss();
                    *//*if (otherSourceIsVisible) {
                        if (TextUtils.isEmpty(sourceOtherStr)) {
                            Toast.makeText(getActivity(), "Please enter Other Source", Toast.LENGTH_SHORT).show();
                        } else {
                            customerSourceID.setText(sourceOtherStr);
                            dialog.dismiss();
                        }
                    }*//*
                }*/
            }
        });


        if (Utilities.isNetworkAvailable(this)) {

            /*if (type.equalsIgnoreCase("ACTIVITIES")) {
                activities();
            } else if (type.equalsIgnoreCase("SOURCES")) {
                sources();
            } else*/ if (type.equalsIgnoreCase("REQUIREMENT")) {
                requirements();
            }/*else if (type.equalsIgnoreCase("EMPLOYEE")) {
                employee();*/

        } else {
            AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "No Internet connection", "MAke sure your device is connected to internet");
        }

        dropDownLVID.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> requirementAdapterView, View view, int i, long l) {
                dialog.dismiss();

                /*if (type.equalsIgnoreCase("ACTIVITIES")) {


                    activityTVID.setText(activityMainResponses1.get(i).getActivity_name());
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    customerSourceID.setText(sourceResponses.get(i).getSource_name());
                } else */if (type.equalsIgnoreCase("REQUIREMENT")) {
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

               /* if (type.equalsIgnoreCase("ACTIVITIES")) {
                    String te = searchHintETID.getText().toString();
                    activitiesAdapter.getFilter().filter(te);
                } else if (type.equalsIgnoreCase("SOURCES")) {
                    String te = searchHintETID.getText().toString();
                    sAdapter.getFilter().filter(te);
                } else */if (type.equalsIgnoreCase("REQUIREMENT")) {
                    String te = searchHintETID.getText().toString();
                    requirementAdapter.getFilter().filter(te);
                } /*else if (type.equalsIgnoreCase("EMPLOYEE")) {
                    String te = searchHintETID.getText().toString();
                    employeeAdapter.getFilter().filter(te);
                }*/


            }
        });

    }

    private void requirements() {
        rePB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<RequirementResponse>> call = apiInterface.getRequirements(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RequirementResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<RequirementResponse>> call, Response<ArrayList<RequirementResponse>> response) {
                rePB.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    requirementResponses = response.body();
                    if (requirementResponses != null && requirementResponses.size() > 0) {
                        for (int i = 0; i < requirementResponses.size(); i++) {
                            requirementAdapter = new RequirementAdapter(LeadEditActivity.this, requirementResponses);
                            dropDownLVID.setAdapter(requirementAdapter);
                            dropDownLVID.setExpanded(true);
                            dropDownLVID.setFocusable(false);
                        }
                    } else {
                        Toast.makeText(LeadEditActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LeadEditActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RequirementResponse>> call, Throwable t) {
                rePB.setVisibility(View.GONE);
                Toast.makeText(LeadEditActivity.this, "Requirement not loading", Toast.LENGTH_SHORT).show();

            }
        });
    }

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
                Dexter.withActivity(LeadEditActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
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
                if (ContextCompat.checkSelfPermission(LeadEditActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(LeadEditActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LeadEditActivity.this,
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

          /*  case REQUEST_IMAGE_CAPTURE_USER:
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
        Picasso.with(this).load(url).into(header_image);
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
                // If request is cancelled, the result selectedRequirementArraysays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }

                return;
            }
        }
    }

    private String getRealPathFromURI(Uri selectImageUri) {
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(selectImageUri, null, null, null, null);
        if (cursor == null) {
            return selectImageUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = 0;
            try {
                idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor.getString(idx);
        }
    }

    private void decodeImage(String selectImagePath) {
        int targetW = header_image.getWidth();
        int targetH = header_image.getHeight();

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
            header_image.setImageBitmap(bitmap);
        }
    }

    private void validations() {
        firstNameStr = firstNameETID.getText().toString();
        lastNameStr = lastNameETID.getText().toString();
        emailStr = emailETID.getText().toString();
        mobileStr = mobileETID.getText().toString();
        websiteStr = websiteETID.getText().toString();
        locationStr = locationETID.getText().toString();
        cityStr = cityETID.getText().toString();
        companyStr = companyETID.getText().toString();
        gstStr = gstETID.getText().toString();

        int count = 0;

        if (TextUtils.isEmpty(firstNameStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "First name can't empty", "Please enter first name");
            return;
        } else {
            count++;
        }

       /* if (TextUtils.isEmpty(emailStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Email can't empty", "Please enter email");
            return;
        } else {
            count++;
        }*/

        if (TextUtils.isEmpty(mobileStr)) {
            AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Mobile can't empty", "Please enter mobile");
            return;
        } else {
            count++;
        }

        if (!otherReqIsVisible) {
            if (TextUtils.isEmpty(selectedRequirementArrays)) {
                AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Requirement can't empty", "Please select Requirement");
                return;
            } else {
                count++;
            }
        } else {
            count++;
        }

        if (count == 3) {
            if (Utilities.isNetworkAvailable(LeadEditActivity.this)) {
                editLeadResponse();
            } else {
                AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Alert...!", "No Internet connection");
            }
        }
    }

    private void loadLeadProfileDetails(String customerID) {
        mainView.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<LeadProfileResponse>> call = apiInterface.getEditLeadProfileDetails(customerID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<LeadProfileResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LeadProfileResponse>> call, Response<ArrayList<LeadProfileResponse>> response) {
                mainView.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200) {
                    ArrayList<LeadProfileResponse> profileResponses = response.body();
                    if (profileResponses != null && profileResponses.size() > 0) {

                        userRequirements = profileResponses.get(0).getLead_requirement();

                        try {
                            for (int j = 0; j < userRequirements.size(); j++) {
                                callAllRequirements(userRequirements);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < profileResponses.size(); i++) {
                            mobileETID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_mobile()));
                            emailETID.setText(profileResponses.get(i).getLead_email());
                            websiteETID.setText(profileResponses.get(i).getLead_website());
                            locationETID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_location()));
                            cityETID.setText(Utilities.CapitalText(profileResponses.get(i).getCity()));
                            companyETID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_company_name()));
                            gstETID.setText(profileResponses.get(i).getLead_gstnumber());
                            firstNameETID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_name()));
                            lastNameETID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_last_name()));
                            lastNameETID.setText(Utilities.CapitalText(profileResponses.get(i).getLead_last_name()));
                            requirementTVID.setText(Utilities.CapitalText(profileResponses.get(i).getRequirement_name()));
                            selectedRequirementArrays = profileResponses.get(i).getRequirement_id();

                            String pic = profileResponses.get(i).getLead_pic();
                            if (pic.equalsIgnoreCase(AppConstants.GLOBAL_MAIN_URL_FOR_ICONS + "lead_pics/")) {
                                Picasso.with(LeadEditActivity.this).load(R.drawable.no_image).into(header_image);
                            } else {
                                Picasso.with(LeadEditActivity.this).load(pic).into(header_image);
                            }
                        }

                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Oops....!", "Details not loading");
                    }

                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Oops....!", "Details not loading");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<LeadProfileResponse>> call, Throwable t) {
                mainView.setVisibility(View.GONE);
                noDataID.setVisibility(View.VISIBLE);
                noDataID.setText("No Data Available");
                AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "Error at server....!", "Details not loading");
            }
        });
    }

    private void callAllRequirements(ArrayList<LeadProfileFragment.UserRequirements> userRequirements) {
        String listString = "";
        String listIdString = "";
        for (int i = 0; i < userRequirements.size(); i++) {
            String val = userRequirements.get(i).getReq_name();

            String ids = userRequirements.get(i).getReq_id();

            listString += val + " ,";
            listIdString += ids + ",";
            selectedRequirementArrays = listIdString;

            if (listIdString.endsWith(",")) {
                if (listIdString.length() == 3) {
                    selectedRequirementArrays = selectedRequirementArrays.replace(",", "");
                } else {
                    selectedRequirementArrays = listIdString;
                }
            } else {
                selectedRequirementArrays = listIdString;
            }

            requirementOtherStr = "";
            requirementTVID.setText(listString);
            String te = requirementTVID.getText().toString();
            te = te.substring(0, te.length() - 1) + "";
            requirementTVID.setText(Utilities.CapitalText(te));

            String re = requirementTVID.getText().toString();

            if (re.contains("Null ,")) {
                re = re.replace("Null ,", "");
            }

            if (re.contains(",null")) {
                re = re.replace(",null", "");
            }

            requirementTVID.setText(Utilities.CapitalText(re));
        }
    }

    public class LeadProfileResponse implements Serializable {
        private String lead_id;
        private String lead_name;
        private String lead_last_name;
        private String lead_email;
        private String lead_mobile;
        private ArrayList<LeadProfileFragment.UserRequirements> lead_requirement = new ArrayList<>();
        private String lead_source;
        private String lead_website;
        private String lead_location;
        private String city;
        private String lead_gstnumber;
        private String lead_company_name;
        private String lead_pic;
        private String requirement_id;
        private String requirement_name;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRequirement_id() {
            return requirement_id;
        }

        public void setRequirement_id(String requirement_id) {
            this.requirement_id = requirement_id;
        }

        public String getRequirement_name() {
            return requirement_name;
        }

        public void setRequirement_name(String requirement_name) {
            this.requirement_name = requirement_name;
        }

        public String getLead_id() {
            return lead_id;
        }

        public void setLead_id(String lead_id) {
            this.lead_id = lead_id;
        }

        public String getLead_name() {
            return lead_name;
        }

        public void setLead_name(String lead_name) {
            this.lead_name = lead_name;
        }

        public String getLead_last_name() {
            return lead_last_name;
        }

        public void setLead_last_name(String lead_last_name) {
            this.lead_last_name = lead_last_name;
        }

        public String getLead_email() {
            return lead_email;
        }

        public void setLead_email(String lead_email) {
            this.lead_email = lead_email;
        }

        public String getLead_mobile() {
            return lead_mobile;
        }

        public void setLead_mobile(String lead_mobile) {
            this.lead_mobile = lead_mobile;
        }

        public ArrayList<LeadProfileFragment.UserRequirements> getLead_requirement() {
            return lead_requirement;
        }

        public void setLead_requirement(ArrayList<LeadProfileFragment.UserRequirements> lead_requirement) {
            this.lead_requirement = lead_requirement;
        }

        public String getLead_source() {
            return lead_source;
        }

        public void setLead_source(String lead_source) {
            this.lead_source = lead_source;
        }

        public String getLead_website() {
            return lead_website;
        }

        public void setLead_website(String lead_website) {
            this.lead_website = lead_website;
        }

        public String getLead_location() {
            return lead_location;
        }

        public void setLead_location(String lead_location) {
            this.lead_location = lead_location;
        }

        public String getLead_gstnumber() {
            return lead_gstnumber;
        }

        public void setLead_gstnumber(String lead_gstnumber) {
            this.lead_gstnumber = lead_gstnumber;
        }

        public String getLead_company_name() {
            return lead_company_name;
        }

        public void setLead_company_name(String lead_company_name) {
            this.lead_company_name = lead_company_name;
        }

        public String getLead_pic() {
            return lead_pic;
        }

        public void setLead_pic(String lead_pic) {
            this.lead_pic = lead_pic;
        }
    }

    private void editLeadResponse() {

        try {
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

            Call<ArrayList<AllStatusResponse>> call = apiInterface.getEditLead(imageBody, customerID, firstNameStr, lastNameStr, mobileStr, emailStr, websiteStr, locationStr,cityStr, companyStr, gstStr, selectedRequirementArrays, "", companyID);
            //Call<ArrayList<AllStatusResponse>> call = apiInterface.getEditLead(imageBody, customerID, firstNameStr, lastNameStr, mobileStr, emailStr, websiteStr, locationStr, companyStr, gstStr, selectedRequirementArrays, requirementOtherStr, companyID);
           // Log.d("Edit_Lead : ", "" + AppConstants.GLOBAL_MAIN_URL + "editlead?upload_pic=" + imageBody + "&lead_id=" + customerID + "&first_name=" + firstNameStr + "&last_name=" + lastNameStr + "&mobile_number=" + mobileStr + "&email_id=" + emailStr + "&website=" + websiteStr + "&location=" + locationStr + "&company_name=" + companyStr + "&gst_number=" + gstStr + "&requirement_id=" + selectedRequirementArrays + "&requirement_other=" + requirementOtherStr + "&user_company_id=" + companyID);
            Log.e("api==>",call.request().toString());

            call.enqueue(new Callback<ArrayList<AllStatusResponse>>() {
                @Override
                public void onResponse(Call<ArrayList<AllStatusResponse>> call, Response<ArrayList<AllStatusResponse>> response) {
                    dismissProgressDialog();
                    if (response.body() != null && response.code() == 200) {
                        ArrayList<AllStatusResponse> leadResponse = response.body();
                        if (leadResponse != null && leadResponse.size() > 0) {
                            for (int i = 0; i < leadResponse.size(); i++) {
                                if (leadResponse.get(i).getStatus() == 1) {
                                    Toast.makeText(LeadEditActivity.this, "Lead edited successfully", Toast.LENGTH_SHORT).show();
                                    AlertUtilities.finishAnimation(LeadEditActivity.this);
                                    //AlertUtilities.SuccessAlertDialog(LeadEditActivity.this, "Lead Edited Successfully....!", "");
                                } else {
                                    Utilities.AlertDaiolog(LeadEditActivity.this, getString(R.string.opps), "Editing failed", 1, null, "OK");
                                }
                            }
                        }
                    } else {
                        Utilities.AlertDaiolog(LeadEditActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AllStatusResponse>> call, Throwable t) {
                    dismissProgressDialog();
                    Utilities.AlertDaiolog(LeadEditActivity.this, getString(R.string.opps), getString(R.string.something), 1, null, "OK");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        showDoChangesAlert();
        //finish();
        //overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

    private void showDoChangesAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_save_changes);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView exitTVID = dialog.findViewById(R.id.exitTVID);
        TextView saveTVID = dialog.findViewById(R.id.saveTVID);

        exitTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertUtilities.finishAnimation(LeadEditActivity.this);
            }
        });

        saveTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
                dialog.dismiss();
            }
        });

    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(LeadEditActivity.this)
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

    // Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_lead, menu);
        return true;
    }

    // Setup menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                backPressedAnimation();
                return true;

            case R.id.edit_ok:
                if (Utilities.isNetworkAvailable(LeadEditActivity.this)) {
                    validations();
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "No Internet Connection...", "Make sure your device is connected to internet");
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
            requirementOtherStr = "";

            if (!selectedRequirementArrays.equals("")) {
                if (selectedRequirementArrays.contains("1000")) {
                    selectedRequirementArrays = "1000";
                    requirementOtherStr = "";
                    requirementTVID.setText(selectedRequirementNameArrays);
                } else {
                    requirementOtherStr = "";
                    requirementOtherLLID.setVisibility(View.GONE);
                    requirementTVID.setText(selectedRequirementNameArrays);
                }
            } else {
                AlertUtilities.bottomDisplayStaticAlert(LeadEditActivity.this, "No Tags selected", "Please select requirements...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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
                       /* otherFieldLLID.setVisibility(View.VISIBLE);
                        otherFieldETID.setHint("Other Requirement");*/
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

    public class RequirementNewAdapter extends BaseAdapter implements Filterable {
        private ArrayList<RequirementResponse> _Contacts;
        private Activity context;
        private LayoutInflater inflater;
        private RequirementNewAdapter.ValueFilter valueFilter;
        private ArrayList<RequirementResponse> mStringFilterList;

        private ArrayList<String> mCheckedItemIDs;
        private ArrayList<String> mCheckedItemNames;
        public boolean state = true;

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


            final RequirementNewAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new RequirementNewAdapter.ViewHolder();
                convertView = inflater.inflate(R.layout.new_custom_spinner_list_items, null);
                holder.checkBoxID = convertView.findViewById(R.id.checkBoxID);
                holder.tname = convertView.findViewById(R.id.spinnerText);
                holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
                convertView.setTag(holder);
            } else
                holder = (RequirementNewAdapter.ViewHolder) convertView.getTag();
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
                            otherReqIsVisible = true;
                            otherReqID.setVisibility(View.VISIBLE);
                        }
                    } else {

                        String id = _Contacts.get(position).getRequirement_id();
                        if (id.equals("1000")) {
                            otherReqIsVisible = false;
                            otherReqID.setVisibility(View.GONE);
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
                                otherReqIsVisible = true;
                                otherReqID.setVisibility(View.VISIBLE);
                            }

                        } else {
                            String id = _Contacts.get(position).getRequirement_id();
                            if (id.equals("1000")) {
                                otherReqIsVisible = false;
                                otherReqID.setVisibility(View.GONE);
                            }
                            mCheckedItemIDs.remove(_Contacts.get(position).getRequirement_id());
                            mCheckedItemNames.remove(_Contacts.get(position).getRequirement_name());
                        }
                    }
                }
            });


            for (int i = 0; i < userRequirements.size(); i++) {
                String id = userRequirements.get(i).getReq_id();
                if (id.equals(_Contacts.get(position).getRequirement_id())) {

                    holder.checkBoxID.setChecked(true);
                    state = false;
                    mCheckedItemIDs.add(_Contacts.get(position).getRequirement_id());
                    mCheckedItemNames.add(_Contacts.get(position).getRequirement_name());
                }
            }


            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {

                valueFilter = new RequirementNewAdapter.ValueFilter();
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


}
