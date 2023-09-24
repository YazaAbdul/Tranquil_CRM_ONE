package crm.tranquil_sales_steer.ui.activities.templates;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

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
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;

public class EmailTemplatesActivity extends AppCompatActivity {
    ListView availableLVID;
    ProgressBar pb;
    TextView noData;
    EmailTemplatesAdapter adapter;
    ArrayList<EmailTemplatesResponse> performanceResponses = new ArrayList<>();
    String companyID, userID;
    private KProgressHUD hud;
    String selectImagePath = "null";
    Uri selectImageUri;
    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_STORAGE = 2;
    TextView attachmentTVID;
    LinearLayout uploadPicLLID;
    ImageView noDataImage;
    public static final int REQUEST_IMAGE_CAPTURE_USER = 0;
    public static String fileName;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_templates);


        Utilities.setStatusBarGradiant(this);

        userID = MySharedPreferences.getPreferences(EmailTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        companyID = MySharedPreferences.getPreferences(EmailTemplatesActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Email Templates");
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });
        FloatingActionButton fabAddTask = findViewById(R.id.fabAddTask);

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTemplate();
            }
        });

        availableLVID = findViewById(R.id.availableLVID);
        pb = findViewById(R.id.pb);
        noData = findViewById(R.id.noData);
        noDataImage = findViewById(R.id.noDataImage);

        if (Utilities.isNetworkAvailable(EmailTemplatesActivity.this)) {
            loaProjects();
        } else {
            Utilities.AlertDaiolog(EmailTemplatesActivity.this, "No Internet", "Please check your internet connection...", 1, null, "OK");
        }
    }

    private void loaProjects() {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<EmailTemplatesResponse>> call = apiInterface.getEmailTemplates(companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<EmailTemplatesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<EmailTemplatesResponse>> call, Response<ArrayList<EmailTemplatesResponse>> response) {
                pb.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    performanceResponses.clear();
                    performanceResponses = response.body();
                    if (performanceResponses.size() > 0) {
                        for (int i = 0; i < performanceResponses.size(); i++) {
                            adapter = new EmailTemplatesAdapter(EmailTemplatesActivity.this, performanceResponses);
                            availableLVID.setAdapter(adapter);
                        }
                    } else {
                        noDataImage.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmailTemplatesResponse>> call, Throwable t) {
                noData.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                noDataImage.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void addTemplate() {

        final Dialog dialog = new Dialog(EmailTemplatesActivity.this);
        dialog.setContentView(R.layout.add_templates);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

        Button submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setText("Save");

        LinearLayout headerID = dialog.findViewById(R.id.headerID);
        headerID.setVisibility(View.VISIBLE);

        attachmentTVID = dialog.findViewById(R.id.attachmentTVID);

        uploadPicLLID = dialog.findViewById(R.id.uploadPicLLID);
        uploadPicLLID.setVisibility(View.VISIBLE);
        uploadPicLLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDisplay();
            }
        });


        TextView titleID = dialog.findViewById(R.id.titleID);
        titleID.setText("Create Email Template");

        final TextInputEditText subjectETID = dialog.findViewById(R.id.subjectETID);
        final TextInputLayout attachment = dialog.findViewById(R.id.attachment);
        final TextInputEditText titleETID = dialog.findViewById(R.id.tittleETID);
        final TextInputEditText messageETID = dialog.findViewById(R.id.messageETID);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleETID.getText().toString();
                String message = messageETID.getText().toString();
                String subject = subjectETID.getText().toString();
                int count = 0;

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(EmailTemplatesActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(EmailTemplatesActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }

                if (TextUtils.isEmpty(subject)) {
                    Toast.makeText(EmailTemplatesActivity.this, "Please enter subject", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }

                if (count == 3) {
                    if (Utilities.isNetworkAvailable(EmailTemplatesActivity.this)) {
                        createEmail(dialog, title, message, subject, companyID, userID);
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(EmailTemplatesActivity.this, "No Internet..", "Make sure your device is connected to internet");
                    }
                }
            }
        });
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

                Dexter.withActivity(EmailTemplatesActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
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
                if (ContextCompat.checkSelfPermission(EmailTemplatesActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(EmailTemplatesActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EmailTemplatesActivity.this,
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
                    attachmentTVID.setText("1 File Selected");
                }

                break;

           /* case REQUEST_IMAGE_CAPTURE_USER:
                if (resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    loadProfile(tempUri.toString());
                    selectImagePath = getRealPathFromURI2(tempUri);
                } else {
                    setResultCancelled();
                }*/

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
        Log.d("", "Image cache path: " + url);
        attachmentTVID.setText("1 File Selected");
        //Picasso.get().load(url).into(leadPic);
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

    private void openImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, PICK_IMAGE);
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

    private void createEmail(final Dialog dialog, String title, String message, String subject, String companyID, String userID) {

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
        loaProjects();
        Call<ArrayList<StatusResponse>> call = apiInterface.getCreateEmail(imageBody, title, subject, message, companyID, userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {
                                dialog.dismiss();
                                //performanceResponses.clear();
                                loaProjects();
                                AlertUtilities.EmailSuccessAlertDialog(EmailTemplatesActivity.this, "Email Template Created Successfully");

                            } else {
                                Toast.makeText(EmailTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(EmailTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmailTemplatesActivity.this, "Error at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(EmailTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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

    public class EmailTemplatesResponse {

        private String emai_template_id;
        private String email_title;
        private String email_subject;
        private String email_message;
        private String email_attachement;

        public EmailTemplatesResponse(String emai_template_id, String email_title, String email_subject, String email_message, String email_attachement) {
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

    public class EmailTemplatesAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<EmailTemplatesResponse> availabilityList = new ArrayList<>();

        public EmailTemplatesAdapter(Activity activity, ArrayList<EmailTemplatesResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return availabilityList.size();
        }

        @Override
        public Object getItem(int i) {
            return availabilityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.new_availability_list_item, null);

            if (i % 2 == 1) {
                view.setBackgroundColor(getResources().getColor(R.color.edit_d));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }

            TextView projectNameTVID = view.findViewById(R.id.projectNameTVID);
            projectNameTVID.setText(Utilities.CapitalText(availabilityList.get(i).getEmail_title()));

            RelativeLayout mainDisplayRLID = view.findViewById(R.id.mainDisplayRLID);

            mainDisplayRLID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alertDisplayMessage(availabilityList.get(i).getEmail_message(), availabilityList.get(i).getEmail_title(), availabilityList.get(i).getEmail_subject(), availabilityList.get(i).getEmail_attachement(), availabilityList.get(i).getEmai_template_id());

                }
            });

            return view;
        }
    }

    @SuppressLint("SetTextI18n")
    private void alertDisplayMessage(String email_message, String email_title, String email_subject, String email_attachement, final String emai_template_id) {
        final Dialog dialog = new Dialog(EmailTemplatesActivity.this);
        dialog.setContentView(R.layout.add_templates);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

        Button submitBtn = dialog.findViewById(R.id.submitBtn);
        submitBtn.setText("Save");

        final TextInputEditText subjectETID = dialog.findViewById(R.id.subjectETID);
        final TextInputEditText titleETID = dialog.findViewById(R.id.tittleETID);
        final TextInputEditText messageETID = dialog.findViewById(R.id.messageETID);
        LinearLayout uploadPicLLID = dialog.findViewById(R.id.uploadPicLLID);
        uploadPicLLID.setVisibility(View.VISIBLE);
        uploadPicLLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDisplay();
            }
        });
        attachmentTVID = dialog.findViewById(R.id.attachmentTVID);
        LinearLayout headerID = dialog.findViewById(R.id.headerID);
        headerID.setVisibility(View.VISIBLE);

        TextView titleID = dialog.findViewById(R.id.titleID);
        titleID.setText("Edit Email Template");

        subjectETID.setText(email_subject);
        titleETID.setText(email_title);
        messageETID.setText(email_message);
        attachmentTVID.setText(email_attachement);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleETID.getText().toString();
                String message = messageETID.getText().toString();
                String subject = subjectETID.getText().toString();

                int count = 0;

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(EmailTemplatesActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(EmailTemplatesActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }

                if (TextUtils.isEmpty(subject)) {
                    Toast.makeText(EmailTemplatesActivity.this, "Please enter subject", Toast.LENGTH_SHORT).show();
                } else {
                    count++;
                }

                if (count == 3) {
                    if (Utilities.isNetworkAvailable(EmailTemplatesActivity.this)) {
                        editEmail(dialog, title, message, subject, companyID, userID, emai_template_id);
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(EmailTemplatesActivity.this, "No Internet..", "Make sure your device is connected to internet");
                    }
                }
            }
        });
    }

    private void editEmail(final Dialog dialog, String title, String message, String subject, String companyID, String userID, String emailID) {

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
        loaProjects();
        Call<ArrayList<StatusResponse>> call = apiInterface.getEditEmail(imageBody, title, subject, message, companyID, userID, emailID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {
                                dialog.dismiss();
                                performanceResponses.clear();
                                loaProjects();
                                AlertUtilities.EmailSuccessAlertDialog(EmailTemplatesActivity.this, "Email Template Edited Successfully");
                            } else {
                                Toast.makeText(EmailTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(EmailTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmailTemplatesActivity.this, "Error at server side", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(EmailTemplatesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(EmailTemplatesActivity.this)
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
