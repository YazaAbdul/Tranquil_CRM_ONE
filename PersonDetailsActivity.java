package hostelguru.android.module_step_verification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import hostelguru.android.Models.BedsResponse;
import hostelguru.android.Models.FloorsResponse;
import hostelguru.android.Models.HostelMainModel;
import hostelguru.android.Models.PersonDetailsResponse;
import hostelguru.android.Models.RoomsResponse;
import hostelguru.android.R;
import hostelguru.android.Requriements.AppConstants;
import hostelguru.android.Requriements.MySharedPreferences;
import hostelguru.android.Requriements.Utilities;
import hostelguru.android.module_dash_board.DashBoardActivity;

public class PersonDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db;
    String city, state, address, postalCode, latitude, longitude, floorCount, floorTitle, roomCount, roomTitle, hostelTypeStr, hostelNameStr, hostelAddressStr, hostelWardenNameStr, hostelWardenNumberStr;
    String bedTitle, bedCount;

    private ArrayList<FloorsResponse> floors = new ArrayList<>();
    private ArrayList<RoomsResponse> rooms = new ArrayList<>();
    private ArrayList<BedsResponse> beds = new ArrayList<>();
    private ArrayList<PersonDetailsResponse> personDetails = new ArrayList<>();

    RelativeLayout submitOkRLID;
    EditText nameETID, mobileNumberETID, advanceETID, addressETID, ageETID, aadharNumberETID;
    String nameStr, mobileNumberStr, advanceStr, addressStr, ageStr, aadharNumberStr;


    String document_ID, userID;
    Switch switchID;
    boolean isChecked = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                address = bundle.getString("HOSTEL_ADDRESS");
                city = bundle.getString("HOSTEL_CITY");
                latitude = bundle.getString("HOSTEL_LATITUDE");
                longitude = bundle.getString("HOSTEL_LONGITUDE");
                postalCode = bundle.getString("HOSTEL_PIN_CODE");
                state = bundle.getString("HOSTEL_STATE");
                floorCount = bundle.getString("HOSTEL_FLOOR_COUNT");
                floorTitle = bundle.getString("HOSTEL_FLOOR_TITLE");
                roomCount = bundle.getString("HOSTEL_ROOM_COUNT");
                roomTitle = bundle.getString("HOSTEL_ROOM_TITLE");
                bedCount = bundle.getString("HOSTEL_BED_COUNT");
                bedTitle = bundle.getString("HOSTEL_BED_TITLE");
                hostelTypeStr = bundle.getString("HOSTEL_TYPE");
                hostelNameStr = bundle.getString("HOSTEL_NAME");
                hostelAddressStr = bundle.getString("HOSTEL_ADDRESS");
                hostelWardenNameStr = bundle.getString("HOSTEL_WARDEN");
                hostelWardenNumberStr = bundle.getString("HOSTEL_WARDEN_NUMBER");
            }
        }

        Utilities.startAnimation(this);
        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.REGISTER_USER_ID);

        db = FirebaseFirestore.getInstance();
        RelativeLayout backBtnRLID = findViewById(R.id.backBtnRLID);
        backBtnRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.finishAnimation(PersonDetailsActivity.this);
            }
        });

        TextView headerTittle = findViewById(R.id.headerTittle);
        headerTittle.setText(bedTitle + "  Details");

        nameETID = findViewById(R.id.nameETID);
        mobileNumberETID = findViewById(R.id.mobileNumberETID);
        advanceETID = findViewById(R.id.advanceETID);
        addressETID = findViewById(R.id.addressETID);
        ageETID = findViewById(R.id.ageETID);
        aadharNumberETID = findViewById(R.id.aadharNumberETID);

        submitOkRLID = findViewById(R.id.submitOkRLID);
        submitOkRLID.setVisibility(View.VISIBLE);
        submitOkRLID.setOnClickListener(this);
        switchID = findViewById(R.id.switchID);
        switchID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isChecked = true;
                } else {
                    isChecked = false;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.finishAnimation(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitOkRLID:
                if (Utilities.isNetworkAvailable(this)) {
                    validations();
                } else {
                    Utilities.alertDisplay(this, getResources().getString(R.string.no_internet_title), getResources().getString(R.string.no_internet_desc));
                }
                break;
        }
    }

    private void validations() {
        int count = 0;
        nameStr = nameETID.getText().toString();
        mobileNumberStr = mobileNumberETID.getText().toString();
        advanceStr = advanceETID.getText().toString();
        addressStr = addressETID.getText().toString();
        ageStr = ageETID.getText().toString();
        aadharNumberStr = aadharNumberETID.getText().toString();

        if (TextUtils.isEmpty(nameStr)) {
            Utilities.alertDisplay(this, "Oops..!", "Enter Hosteler Name");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(mobileNumberStr)) {
            Utilities.alertDisplay(this, "Oops..!", "Enter Hosteler Number");
            return;
        } else {
            count++;
        }

        if (mobileNumberStr.length() > 9) {
            count++;
        } else {
            Utilities.alertDisplay(this, "Oops..!", "Please enter valid Mobile number");
            return;
        }

        if (TextUtils.isEmpty(advanceStr)) {
            Utilities.alertDisplay(this, "Oops..!", "Enter Advance");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(addressStr)) {
            Utilities.alertDisplay(this, "Oops..!", "Enter Hosteler Address");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(ageStr)) {
            Utilities.alertDisplay(this, "Oops..!", "Enter Hosteler Age");
            return;
        } else {
            count++;
        }

        if (TextUtils.isEmpty(aadharNumberStr)) {
            Utilities.alertDisplay(this, "Oops..!", "Enter Hosteler Aadhar Number");
            return;
        } else {
            count++;
        }

        if (count == 7) {
            if (Utilities.isNetworkAvailable(this)) {
                addHostelDetailsToDataBase();
            } else {
                Utilities.alertDisplay(this, getResources().getString(R.string.no_internet_title), getResources().getString(R.string.no_internet_desc));
            }
        }
    }

    private void addHostelDetailsToDataBase() {

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, null, "Adding Hostel please wait...");
        dialog.show();

        personDetails.add(new PersonDetailsResponse(nameStr, mobileNumberStr, advanceStr, addressStr, ageStr, aadharNumberStr));
        beds.add(new BedsResponse(bedCount, bedTitle, personDetails));
        rooms.add(new RoomsResponse(roomCount, roomTitle, beds));
        floors.add(new FloorsResponse(floorCount, floorTitle, rooms));
        HostelMainModel hostelMainModel = new HostelMainModel(userID, address, city, latitude, longitude, postalCode, state, hostelTypeStr, hostelNameStr, hostelAddressStr, hostelWardenNameStr, hostelWardenNumberStr, floors);

        db.collection(city + "_" + AppConstants.GET_HOSTEL_MAIN_INFO).add(hostelMainModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {


                        if (isChecked) {
                            sentNotification("Thanks for adding hostel with Hostel Guru \n We will notify fee due date of " + hostelNameStr + " before 1 week");
                        } else {
                            sentNotification("Thanks for adding hostel with Hostel Guru");
                        }

                        dialog.dismiss();
                        document_ID = documentReference.getId();
                        MySharedPreferences.setPreference(PersonDetailsActivity.this, "" + AppConstants.REGISTERED_HOSTEL_CITY, "" + city);
                        MySharedPreferences.setPreference(PersonDetailsActivity.this, "" + AppConstants.HOSTEL_ID, "" + document_ID);
                        Intent intent = new Intent(PersonDetailsActivity.this, DashBoardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(PersonDetailsActivity.this, "Hostel not created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sentNotification(String msg) {
        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(getApplicationContext())
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon))
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentTitle(msg)
                .build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.app_icon : R.mipmap.ic_launcher;
    }
}
