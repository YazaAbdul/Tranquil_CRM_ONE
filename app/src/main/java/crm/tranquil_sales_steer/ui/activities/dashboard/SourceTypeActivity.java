package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.SourceTypesAdapter;
import crm.tranquil_sales_steer.ui.models.SourceTypesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourceTypeActivity extends AppCompatActivity implements SourceTypesAdapter.SourceTypeClickListener {

    RecyclerView sourceRVID;
    ProgressBar loadingPb;
    FloatingActionButton refreshFab;
    LinearLayout noDataLLID;
    TextView headerTittleTVID;
    RelativeLayout backRLID;
    SourceTypesAdapter adapter;
    ArrayList<SourceTypesResponse> sourceTypesResponses = new ArrayList<>();
    String userID,userType,activityStr;
    public static boolean isClickable = true;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_type);
        Utilities.setStatusBarGradiant(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                userID = bundle.getString("USER_ID");
                userType = bundle.getString("USER_TYPE");
                activityStr = bundle.getString("ACTIVITY");
            }
        }

        /*userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);*/

        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        if (activityStr.equalsIgnoreCase("SOURCE")) {
            headerTittleTVID.setText("Source Types");
        } else {

            headerTittleTVID.setText("Project Types");
        }
        backRLID = findViewById(R.id.backRLID);
        noDataLLID = findViewById(R.id.noDataLLID);
        loadingPb = findViewById(R.id.loadingPb);
        sourceRVID = findViewById(R.id.sourceRVID);
        refreshFab = findViewById(R.id.refreshFab);
        backRLID.setOnClickListener(v -> Utilities.finishAnimation(this));

        refreshFab.setOnClickListener(v -> {
            if (isClickable) {
                isClickable = false;

                loadSourceTypes();

            }
        });

        if (Utilities.isNetworkAvailable(this)){

            loadSourceTypes();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSourceTypes() {

        noDataLLID.setVisibility(View.GONE);
        loadingPb.setVisibility(View.GONE);
        sourceRVID.setVisibility(View.VISIBLE);
        isClickable = false;

        Call<ArrayList<SourceTypesResponse>> call = null;

        if (activityStr.equalsIgnoreCase("SOURCE")) {
               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            call = apiInterface.getSourceType(userID, userType);
        } else {

               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            call = apiInterface.getProjectTypes(userID, userType);
        }

        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SourceTypesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SourceTypesResponse>> call, Response<ArrayList<SourceTypesResponse>> response) {

                if (response.body() != null && response.code() == 200) {

                    loadingPb.setVisibility(View.GONE);
                    sourceRVID.setVisibility(View.VISIBLE);
                    noDataLLID.setVisibility(View.GONE);
                    refreshFab.setVisibility(View.VISIBLE);

                    sourceTypesResponses.clear();
                    sourceTypesResponses = response.body();
                    if (sourceTypesResponses.size() > 0) {
                        isClickable = true;

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SourceTypeActivity.this, LinearLayoutManager.VERTICAL, false);
                        adapter = new SourceTypesAdapter(SourceTypeActivity.this,SourceTypeActivity.this,sourceTypesResponses);

                        sourceRVID.setAdapter(adapter);
                        sourceRVID.setLayoutManager(linearLayoutManager);
                    }else {
                        setErrorViews();
                    }
                }else {
                    setErrorViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SourceTypesResponse>> call, Throwable t) {

                setErrorViews();

            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        loadingPb.setVisibility(View.GONE);
        sourceRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSourceTypeItemClick(SourceTypesResponse response, View v, int pos, SourceTypesAdapter.ItemViewHolder holder) {

        switch (v.getId()) {
            case R.id.sourceTypeCVID:

                Intent btdelsale = new Intent(new Intent(this, SourceTypeLeadsActivity.class));
                btdelsale.putExtra("SOURCE_ID", response.getSource_id());
                btdelsale.putExtra("SOURCE_NAME", response.getSource_name() + " (" + response.getCount() +")");
                btdelsale.putExtra("USER_ID",userID);
                btdelsale.putExtra("USER_TYPE",userType);
                btdelsale.putExtra("ACTIVITY",activityStr);
                startActivity(btdelsale);

                break;
        }

    }
}