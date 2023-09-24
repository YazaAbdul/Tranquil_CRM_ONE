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
import crm.tranquil_sales_steer.ui.adapters.AgentsCountAdapter;
import crm.tranquil_sales_steer.ui.models.AgentsMainResponse;
import crm.tranquil_sales_steer.ui.models.AgentsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesignationActivity extends AppCompatActivity implements AgentsCountAdapter.AgentCountClickListener{

    //ActivityDesignationBinding binding;
    RelativeLayout designation1ID,designation2ID,designation3ID,backRLID;
    RecyclerView teleDesignationsRVID;
    ProgressBar pb;

    RecyclerView agentsCountRVID;
    LinearLayout noDataLLID;
    ArrayList<AgentsMainResponse> agentsMainResponses = new ArrayList<>();
    ArrayList<AgentsResponse> agentsResponses = new ArrayList<>();
    AgentsCountAdapter agentsCountAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = DataBindingUtil.setContentView(this,R.layout.activity_designation);
        setContentView(R.layout.activity_designation);

        /*designation1ID = findViewById(R.id.designation1ID);
        designation2ID = findViewById(R.id.designation2ID);
        designation3ID = findViewById(R.id.designation3ID);*/
        backRLID = findViewById(R.id.backRLID);
        backRLID = findViewById(R.id.backRLID);
        agentsCountRVID = findViewById(R.id.agentsCountRVID);
        noDataLLID = findViewById(R.id.noDataLLID);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadAgents();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAgents() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AgentsMainResponse>> call = apiInterface.getAgentsList();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AgentsMainResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AgentsMainResponse>> call, Response<ArrayList<AgentsMainResponse>> response) {

                if (response.body() != null && response.code() == 200){

                    agentsMainResponses = response.body();

                    if (agentsMainResponses.size() > 1){

                        LinearLayoutManager layoutManager = new LinearLayoutManager(DesignationActivity.this,RecyclerView.VERTICAL,false);
                        agentsCountAdapter = new AgentsCountAdapter(agentsMainResponses,DesignationActivity.this,DesignationActivity.this);

                        agentsCountRVID.setLayoutManager(layoutManager);
                        agentsCountRVID.setAdapter(agentsCountAdapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<AgentsMainResponse>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAgentCountItemClick(AgentsMainResponse response, View v, int pos, AgentsCountAdapter.AgentsVH holder) {

        switch (v.getId()) {

            case R.id.agentCountCVID:

                Intent intent = new Intent(this,AgentsDataActivity.class);
                intent.putExtra("agent_list",response.getAgents());
                intent.putExtra("designation_name",response.getDesignation_name());
                startActivity(intent);

                break;
        }

    }
}