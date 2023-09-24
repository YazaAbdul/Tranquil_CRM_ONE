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

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.TeleSourceTypeAdapter;
import crm.tranquil_sales_steer.ui.models.TeleSourceTypeResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeleSourceActivity extends AppCompatActivity {

    ArrayList<TeleSourceTypeResponse> teleSourceTypeResponses = new ArrayList<>();
    TeleSourceTypeAdapter adapter;

    RelativeLayout backRLID;
    RecyclerView teleSourceTypeRVID;
    ProgressBar pb;
    LinearLayoutManager layoutManager;
    LinearLayout noDataLLID;
    String userID,userType;

    //Google Analytics
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_source);
        Utilities.setStatusBarGradiant(this);
        analytics = FirebaseAnalytics.getInstance(this);

        backRLID = findViewById(R.id.backRLID);
        teleSourceTypeRVID = findViewById(R.id.teleSourceTypeRVID);
        pb = findViewById(R.id.pb);
        noDataLLID = findViewById(R.id.noDataLLID);

        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadTeleSourceTypes();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTeleSourceTypes() {

        pb.setVisibility(View.VISIBLE);
        teleSourceTypeRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<TeleSourceTypeResponse>> call = apiInterface.getTeleSourceType(userID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<TeleSourceTypeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<TeleSourceTypeResponse>> call, Response<ArrayList<TeleSourceTypeResponse>> response) {
                pb.setVisibility(View.GONE);
                teleSourceTypeRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);

                if (response.body() != null && response.code() == 200){
                    teleSourceTypeResponses = response.body();

                    if (teleSourceTypeResponses.size() > 0){
                        layoutManager = new LinearLayoutManager(TeleSourceActivity.this,RecyclerView.VERTICAL,false);
                        adapter = new TeleSourceTypeAdapter(TeleSourceActivity.this,teleSourceTypeResponses);

                        teleSourceTypeRVID.setLayoutManager(layoutManager);
                        teleSourceTypeRVID.setAdapter(adapter);
                    }else {
                        pb.setVisibility(View.GONE);
                        teleSourceTypeRVID.setVisibility(View.GONE);
                        noDataLLID.setVisibility(View.VISIBLE);
                    }

                }else {
                    pb.setVisibility(View.GONE);
                    teleSourceTypeRVID.setVisibility(View.GONE);
                    noDataLLID.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TeleSourceTypeResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                teleSourceTypeRVID.setVisibility(View.GONE);
                noDataLLID.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTeleSourceTypes();
    }
}