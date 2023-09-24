package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.DueCustomersAdapter;
import crm.tranquil_sales_steer.ui.models.DueCustomersResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DueCustomersActivity extends AppCompatActivity {

    RelativeLayout backRLID;
    LinearLayout noDataLLID;
    ProgressBar pb;
    RecyclerView dueCustomersRVID;

    LinearLayoutManager layoutManager;

    DueCustomersAdapter dueCustomersAdapter;
    ArrayList<DueCustomersResponse> dueCustomersResponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_customers2);
        Utilities.setStatusBarGradiant(this);

        backRLID = findViewById(R.id.backRLID);
        noDataLLID = findViewById(R.id.noDataLLID);
        pb = findViewById(R.id.pb);
        dueCustomersRVID = findViewById(R.id.dueCustomersRVID);


        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){

            getDueCustomersData();
        }else {
            Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
        }


    }

    private void getDueCustomersData() {

        pb.setVisibility(View.VISIBLE);
        noDataLLID.setVisibility(View.GONE);
        dueCustomersRVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DueCustomersResponse>> call = apiInterface.getDueCustomers();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<DueCustomersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DueCustomersResponse>> call, Response<ArrayList<DueCustomersResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataLLID.setVisibility(View.GONE);
                dueCustomersRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    dueCustomersResponses = response.body();

                    if (dueCustomersResponses.size() > 0){

                        layoutManager = new LinearLayoutManager(DueCustomersActivity.this,LinearLayoutManager.VERTICAL,false);

                        dueCustomersAdapter = new DueCustomersAdapter(dueCustomersResponses,DueCustomersActivity.this);

                        dueCustomersRVID.setAdapter(dueCustomersAdapter);
                        dueCustomersRVID.setLayoutManager(layoutManager);

                    }else {

                        pb.setVisibility(View.GONE);
                        noDataLLID.setVisibility(View.VISIBLE);
                        dueCustomersRVID.setVisibility(View.GONE);
                    }


                }else {
                    pb.setVisibility(View.GONE);
                    noDataLLID.setVisibility(View.VISIBLE);
                    dueCustomersRVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DueCustomersResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                noDataLLID.setVisibility(View.VISIBLE);
                dueCustomersRVID.setVisibility(View.GONE);

            }
        });
    }
}