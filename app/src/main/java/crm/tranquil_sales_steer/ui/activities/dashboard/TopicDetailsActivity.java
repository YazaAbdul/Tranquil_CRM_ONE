package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.TopicDetailsAdapter;
import crm.tranquil_sales_steer.ui.models.GettopicnamesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicDetailsActivity extends AppCompatActivity {
    RelativeLayout backRLID;
    RecyclerView topicdetailsSourceTypeRVID;
    ProgressBar pb;
    LinearLayoutManager layoutManager;
    TopicDetailsAdapter adapter;

    int userid,status;
    LinearLayout noDataLLID;
    ArrayList<GettopicnamesResponse> gettopicnamesResponse = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);
        backRLID = findViewById(R.id.backRLID);
        pb=findViewById(R.id.pb);
        noDataLLID=findViewById(R.id.noDataLLID);
        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });
        topicdetailsSourceTypeRVID = findViewById(R.id.topicdetailsSourceTypeRVID);

        if (Utilities.isNetworkAvailable(this)){
            loadtopicdetailsTypes();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadtopicdetailsTypes() {
//        pb.setVisibility(View.VISIBLE);
//        topicdetailsSourceTypeRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<GettopicnamesResponse>> call = apiInterface.gettopicname(status);
        Log.e("api=00=>",call.request().toString());
        call.enqueue(new Callback<ArrayList<GettopicnamesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GettopicnamesResponse>> call, Response<ArrayList<GettopicnamesResponse>> response) {
                pb.setVisibility(View.GONE);
                topicdetailsSourceTypeRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);

                if (response.body() != null && response.code() == 200){
                    gettopicnamesResponse = response.body();

                    if (gettopicnamesResponse.size() > 0){
                        layoutManager = new LinearLayoutManager(TopicDetailsActivity.this,RecyclerView.VERTICAL,false);
                        adapter = new TopicDetailsAdapter(gettopicnamesResponse, TopicDetailsActivity.this);

                        topicdetailsSourceTypeRVID.setLayoutManager(layoutManager);
                        topicdetailsSourceTypeRVID.setAdapter(adapter);
                    }else {
                        pb.setVisibility(View.GONE);
                        topicdetailsSourceTypeRVID.setVisibility(View.GONE);
                        noDataLLID.setVisibility(View.VISIBLE);
                    }

                }else {
                    pb.setVisibility(View.GONE);
                    topicdetailsSourceTypeRVID.setVisibility(View.GONE);
                    noDataLLID.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GettopicnamesResponse>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                topicdetailsSourceTypeRVID.setVisibility(View.GONE);
                noDataLLID.setVisibility(View.VISIBLE);
            }
        });
    }
}