package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kaopiz.kprogresshud.KProgressHUD;



import java.util.ArrayList;
import java.util.Collections;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.ActivitiesAdapterNew;
import crm.tranquil_sales_steer.ui.adapters.EmployeeAdapter;
import crm.tranquil_sales_steer.ui.adapters.EmployeeTrackAdapter;
import crm.tranquil_sales_steer.ui.adapters.TeleCallersDataAdapter;
import crm.tranquil_sales_steer.ui.fragments.LeadProfileFragment;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeTrackResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeTrackResponse;
import crm.tranquil_sales_steer.ui.models.TelecallersResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeTrackActivity extends AppCompatActivity {

    ArrayList<EmployeeTrackResponse> employeeTrackResponses = new ArrayList<>();
    EmployeeTrackAdapter adapter;
    RelativeLayout backRLID;
    AppCompatTextView headerTVID;
    RecyclerView employeeTrackRVID;
    ProgressBar pb;
    LinearLayoutManager layoutManager;
    LinearLayout noDataLLID;
    Spinner employeeSPID;
    String userID;
    KProgressHUD hud;
    ArrayList<EmployeeResponse> employeeResponses = new ArrayList<>();
    EmployeeAdapter employeeAdapter;
    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_track);
        Utilities.setStatusBarGradiant(this);

        /*mapView = findViewById(R.id.mapView);*/


        /*ItemizedIconOverlay<OverlayItem> itemizedIconOverlay = new ItemizedIconOverlay<>(
                new ArrayList<OverlayItem>(),
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(int i, OverlayItem overlayItem) {
                        // handle tap events
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                        // handle long press events
                        return true;
                    }
                },
                getApplicationContext());

        for (GeoPoint geoPoint : geoPoints) {
            OverlayItem overlayItem = new OverlayItem("Title", "Description", geoPoint);
            itemizedIconOverlay.addItem(overlayItem);
        }*/


        //userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);

        backRLID = findViewById(R.id.backRLID);
        headerTVID = findViewById(R.id.headerTVID);
        employeeTrackRVID = findViewById(R.id.employeeTrackRVID);
        pb = findViewById(R.id.pb);
        noDataLLID = findViewById(R.id.noDataLLID);
        employeeSPID = findViewById(R.id.employeeSPID);
        
        headerTVID.setText("Realtime");

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

       

        if (Utilities.isNetworkAvailable(this)){
            employee();
            //loadEmployeeTrack();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }


    }

    private void employee() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<EmployeeResponse>> call = apiInterface.getEmployees();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<EmployeeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeeResponse>> call, Response<ArrayList<EmployeeResponse>> response) {
                
                if (response.body() != null && response.code() == 200) {
                    employeeResponses = response.body();
                    if (employeeResponses != null && employeeResponses.size() > 0) {
                        for (int i = 0; i < employeeResponses.size(); i++) {
                            employeeAdapter = new EmployeeAdapter(EmployeeTrackActivity.this, R.layout.custom_spinner_view, employeeResponses);
                            employeeSPID.setAdapter(employeeAdapter);

                            employeeSPID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    userID = employeeResponses.get(position).getUser_id();
                                    loadEmployeeTrack(userID);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });                           
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmployeeResponse>> call, Throwable t) {

                Toast.makeText(EmployeeTrackActivity.this, "Employees not loading", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadEmployeeTrack(String userID) {

        start = 0;
        isLoading=false;
        isLastPage=false;

        layoutManager = new LinearLayoutManager(EmployeeTrackActivity.this,LinearLayoutManager.VERTICAL,false);
        adapter = new EmployeeTrackAdapter(employeeTrackResponses, EmployeeTrackActivity.this);
        employeeTrackRVID.setAdapter(adapter);
        employeeTrackRVID.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();
        employeeTrackResponses.clear();

        employeeTrackRVID.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("pagination","api called");
                isLastPage =false;
                isLoading = true;
                loadNextPage(userID);
            }

            @Override
            public boolean isLastPage() {
                Log.d("pagination","is last page");
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                Log.d("pagination","is loading");
                return isLoading;
            }
        });

        loadFirstPage(userID);

        pb.setVisibility(View.VISIBLE);
        employeeTrackRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);

    }

    private void loadFirstPage(String userID) {

        pb.setVisibility(View.VISIBLE);
        employeeTrackRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
        isClickable = false;

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<EmployeeTrackResponse>> call = apiInterface.getEmployeeTrack(userID,start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<EmployeeTrackResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeeTrackResponse>> call, Response<ArrayList<EmployeeTrackResponse>> response) {
                pb.setVisibility(View.GONE);
                employeeTrackRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    //employeeTrackResponses.clear();
                    employeeTrackResponses = response.body();
                    if (employeeTrackResponses.size() > 0) {
                        isClickable = true;
                        startedValues();
                        adapter.addAll(employeeTrackResponses);

                       /* for (int i = 0; i < employeeTrackResponses.size(); i++) {

                            latitude = Double.parseDouble(employeeTrackResponses.get(i).getLatitude());
                            longitude = Double.parseDouble(employeeTrackResponses.get(i).getLongitude());

                        }

                       geoPoint = new GeoPoint(latitude, longitude);

                        geoPoints = new ArrayList<GeoPoint>(Collections.singleton(geoPoint));*/

                        if (employeeTrackResponses.size() == 30) {
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        setErrorViews();
                    }
                } else {
                    setErrorViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmployeeTrackResponse>> call, Throwable t) {
                setErrorViews();
            }
        });

    }

    private void loadNextPage(String userID) {
        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            Call<ArrayList<EmployeeTrackResponse>> call = apiInterface.getEmployeeTrack(userID,start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<EmployeeTrackResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<EmployeeTrackResponse>> call, Response<ArrayList<EmployeeTrackResponse>> response) {
                isClickable = true;
                if (response.body() != null && response.code() == 200) {
                    employeeTrackResponses = response.body();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (employeeTrackResponses.size() > 0) {
                        adapter.addAll(employeeTrackResponses);

                        if (employeeTrackResponses.size() == 30) {
                            startedValues();
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            adapter.removeLoadingFooter();
                        }

                    } else {
                        adapter.removeLoadingFooter();
                    }
                } else {
                    adapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EmployeeTrackResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        employeeTrackRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 30;
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

    @Override
    protected void onResume() {
        super.onResume();
    }


}