package hostelguru.android.module_dash_board;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import hostelguru.android.Models.BedsResponse;
import hostelguru.android.Models.FloorsResponse;
import hostelguru.android.Models.HostelMainModel;
import hostelguru.android.Models.PersonDetailsResponse;
import hostelguru.android.Models.RoomsResponse;
import hostelguru.android.R;
import hostelguru.android.Requriements.AppConstants;
import hostelguru.android.Requriements.GPSTracker;
import hostelguru.android.Requriements.MySharedPreferences;
import hostelguru.android.Requriements.Utilities;
import hostelguru.android.module_start_ups.SelectLocationActivity;

public class DashBoardActivity extends AppCompatActivity {


    Location location = null;
    GPSTracker mGPS = null;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String city, state, country, postalCode, knownName, latitude, longitude, hostelID;



    RecyclerView dashBoardRCID;
    DashBoardAdapter adapter;
    String userID;

    private FirebaseFirestore db;
    FloatingActionButton addHostel;
    List<HostelMainModel> hostelrs_disp = new ArrayList<>();

    List<FloorsResponse> floorsResponses = new ArrayList<>();
    List<RoomsResponse> roomsResponses = new ArrayList<>();
    List<BedsResponse> bedsResponses = new ArrayList<>();
    List<PersonDetailsResponse> detailsResponses = new ArrayList<>();

    LinearLayout emptyDataRLID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Utilities.startAnimation(this);

        getLocation();
        db = FirebaseFirestore.getInstance();
        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.REGISTER_USER_ID);
        addHostel = findViewById(R.id.addHostel);
        addHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardActivity.this, SelectLocationActivity.class));
            }
        });


        dashBoardRCID = findViewById(R.id.dashBoardRCID);
        dashBoardRCID.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new DashBoardAdapter(this, hostelrs_disp);
        dashBoardRCID.setAdapter(adapter);
        emptyDataRLID = findViewById(R.id.emptyDataRLID);
        emptyDataRLID.setVisibility(View.GONE);

    }

    private void getLocation() {
        if (mGPS == null) {
            mGPS = new GPSTracker(this);
        }

        // check if mGPS object is created or not
        if (mGPS != null && location == null) {
            location = mGPS.getLocation();
        }

        if (location != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if (addresses != null && addresses.size() > 0) {
                    city = addresses.get(0).getLocality();

                    MySharedPreferences.setPreference(this, "" + AppConstants.REGISTERED_HOSTEL_CITY, "" + city);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHostels();

    }

    public void loadHostels() {

        final ProgressDialog progressDialog;
        progressDialog=ProgressDialog.show(this,null,"Loading hostels....");
        progressDialog.show();

        db.collection(MySharedPreferences.getPreferences(this, "" + AppConstants.REGISTERED_HOSTEL_CITY) + "_" + AppConstants.GET_HOSTEL_MAIN_INFO).whereEqualTo("userID", userID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                try {
                    if (documentSnapshot != null) {
                        progressDialog.dismiss();
                        hostelrs_disp.clear();
                        hostelrs_disp = documentSnapshot.toObjects(HostelMainModel.class);

                        for (int i = 0; i < hostelrs_disp.size(); i++) {

                            HostelMainModel dataModel = new HostelMainModel();
                            dataModel.setHostelName(hostelrs_disp.get(i).getHostelName());
                            dataModel.setHostelWardenName(hostelrs_disp.get(i).getHostelWardenName());
                            dataModel.setHostelWardenNumber(hostelrs_disp.get(i).getHostelWardenNumber());
                            dataModel.setAddress(hostelrs_disp.get(i).getAddress());
                            dataModel.setCity(hostelrs_disp.get(i).getCity());
                            dataModel.setState(hostelrs_disp.get(i).getState());
                            dataModel.setPincode(hostelrs_disp.get(i).getPincode());
                            dataModel.setHostelType(hostelrs_disp.get(i).getHostelType());

                            hostelrs_disp.set(i, dataModel);
                        }

                        if (hostelrs_disp.size() > 0) {
                            emptyDataRLID.setVisibility(View.GONE);
                            adapter = new DashBoardAdapter(DashBoardActivity.this, hostelrs_disp);
                            dashBoardRCID.setAdapter(adapter);
                        } else {
                            emptyDataRLID.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    emptyDataRLID.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.DashBoardViewHolder> {

        private Activity activity;
        private List<HostelMainModel> mainModels = new ArrayList<>();

        private int[] images = {R.drawable.hostel1, R.drawable.hostel2, R.drawable.hostel3, R.drawable.hostel4};


        public DashBoardAdapter(Activity activity, List<HostelMainModel> mainModels) {
            this.activity = activity;
            this.mainModels = mainModels;
        }

        @NonNull
        @Override
        public DashBoardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_list_item, viewGroup, false);
            return new DashBoardViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DashBoardViewHolder dashBoardViewHolder, int i) {


            dashBoardViewHolder.hostelName.setText(mainModels.get(i).getHostelName());
            dashBoardViewHolder.wardenNameTVID.setText(mainModels.get(i).getHostelWardenName());
            dashBoardViewHolder.wardenNumberTVID.setText(mainModels.get(i).getHostelWardenNumber());
            dashBoardViewHolder.addressTVID.setText(mainModels.get(i).getAddress() + "," + mainModels.get(i).getCity() + "," + mainModels.get(i).getState() + "," + mainModels.get(i).getPincode());
            String type = mainModels.get(i).getHostelType();

            if (type.equalsIgnoreCase("MEN")) {
                dashBoardViewHolder.hostelTypeTVID.setText("Boy's Hostel");
            } else if (type.equalsIgnoreCase("WOMEN")) {
                dashBoardViewHolder.hostelTypeTVID.setText("Women's Hostel");
            }

            Picasso.with(activity).load(images[i % 4]).into(dashBoardViewHolder.hostelImage);

            /*floorsResponses = mainModels.get(i).getFloors();
            for (int k = 0; k < floorsResponses.size(); k++) {
                roomsResponses = floorsResponses.get(k).getRooms();
                for (int l = 0; l < roomsResponses.size(); l++) {
                    bedsResponses = roomsResponses.get(l).getBeds();
                    for (int m = 0; m < bedsResponses.size(); m++) {
                        detailsResponses = bedsResponses.get(m).getPersonDetails();

                    }
                }

            }*/


        }

        @Override
        public int getItemCount() {
            return mainModels.size();
        }

        class DashBoardViewHolder extends RecyclerView.ViewHolder {

            TextView hostelName, wardenNameTVID, wardenNumberTVID, addressTVID, hostelTypeTVID;
            ImageView hostelImage;

            DashBoardViewHolder(@NonNull View itemView) {
                super(itemView);
                hostelName = itemView.findViewById(R.id.hostelName);
                wardenNameTVID = itemView.findViewById(R.id.wardenNameTVID);
                wardenNumberTVID = itemView.findViewById(R.id.wardenNumberTVID);
                addressTVID = itemView.findViewById(R.id.addressTVID);
                hostelImage = itemView.findViewById(R.id.hostelImage);
                hostelTypeTVID = itemView.findViewById(R.id.hostelTypeTVID);
            }
        }
    }
}
