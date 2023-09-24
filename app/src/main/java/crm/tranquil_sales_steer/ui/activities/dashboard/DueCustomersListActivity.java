package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.DueCustomersAdapter;
import crm.tranquil_sales_steer.ui.adapters.DueCustomersListAdapter;
import crm.tranquil_sales_steer.ui.models.DueCustomersListResponse;
import crm.tranquil_sales_steer.ui.models.DueCustomersResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DueCustomersListActivity extends AppCompatActivity {

    RelativeLayout backRLID;

    LinearLayout noDataLLID;
    ProgressBar pb;
    RecyclerView dueCustomersRVID;

    LinearLayoutManager layoutManager;

    DueCustomersListAdapter dueCustomersAdapter;
    ArrayList<DueCustomersListResponse> dueCustomersResponses = new ArrayList<>();

    String userID,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_customers_list_acivity);

        backRLID = findViewById(R.id.backRLID);
        noDataLLID = findViewById(R.id.noDataLLID);
        pb = findViewById(R.id.pb);
        dueCustomersRVID = findViewById(R.id.dueCustomersRVID);

        userID = MySharedPreferences.getPreferences(DueCustomersListActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(DueCustomersListActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);


        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){

            getDueCustomersListData();
        }else {
            Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDueCustomersListData() {

        pb.setVisibility(View.VISIBLE);
        noDataLLID.setVisibility(View.GONE);
        dueCustomersRVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DueCustomersListResponse>> call = apiInterface.getDueCustomersList(userID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<DueCustomersListResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<DueCustomersListResponse>> call, Response<ArrayList<DueCustomersListResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataLLID.setVisibility(View.GONE);
                dueCustomersRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    dueCustomersResponses = response.body();

                    if (dueCustomersResponses.size() > 0){

                        layoutManager = new LinearLayoutManager(DueCustomersListActivity.this,LinearLayoutManager.VERTICAL,false);

                        dueCustomersAdapter = new DueCustomersListAdapter(dueCustomersResponses,DueCustomersListActivity.this);

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
            public void onFailure(Call<ArrayList<DueCustomersListResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                noDataLLID.setVisibility(View.VISIBLE);
                dueCustomersRVID.setVisibility(View.GONE);

            }
        });

    }
}