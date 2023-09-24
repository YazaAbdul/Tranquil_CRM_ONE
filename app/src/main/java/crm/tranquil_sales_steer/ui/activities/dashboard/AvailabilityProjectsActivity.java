package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.AvailabilityProjectsAdapter;
import crm.tranquil_sales_steer.ui.models.AvailableProjectsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvailabilityProjectsActivity extends AppCompatActivity {

    RelativeLayout designation1ID,designation2ID,designation3ID,backRLID;
    RecyclerView availableProjectsRVID;
    AvailabilityProjectsAdapter adapter;
    ArrayList<AvailableProjectsResponse> availableProjectsResponse = new ArrayList<>();
    ProgressBar pb;
    AppCompatImageView oopsIVD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_projects);

        backRLID = findViewById(R.id.backRLID);
        /*designation1ID = findViewById(R.id.designation1ID);
        designation2ID = findViewById(R.id.designation2ID);
        designation3ID = findViewById(R.id.designation3ID);*/
        availableProjectsRVID = findViewById(R.id.availableProjectsRVID);
        pb = findViewById(R.id.pb);
        oopsIVD = findViewById(R.id.oopsIVD);


        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadAvailableProjects();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAvailableProjects() {

        availableProjectsRVID.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        oopsIVD.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AvailableProjectsResponse>> call = apiInterface.getAvailableProjects();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AvailableProjectsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AvailableProjectsResponse>> call, Response<ArrayList<AvailableProjectsResponse>> response) {

                availableProjectsRVID.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                oopsIVD.setVisibility(View.GONE);

                if (response.body() != null && response.code() == 200){

                    availableProjectsResponse = response.body();

                    if (availableProjectsResponse.size() > 0) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(AvailabilityProjectsActivity.this,LinearLayoutManager.VERTICAL,false);
                        availableProjectsRVID.setLayoutManager(layoutManager);
                        adapter = new AvailabilityProjectsAdapter(availableProjectsResponse,AvailabilityProjectsActivity.this);
                        availableProjectsRVID.setAdapter(adapter);
                    } else {
                        availableProjectsRVID.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        oopsIVD.setVisibility(View.VISIBLE);
                    }
                }else {
                    availableProjectsRVID.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    oopsIVD.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AvailableProjectsResponse>> call, Throwable t) {

                availableProjectsRVID.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                oopsIVD.setVisibility(View.VISIBLE);

            }
        });
    }
}