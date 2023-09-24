package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.net.URLEncoder;
import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.AgentsCountAdapter;
import crm.tranquil_sales_steer.ui.adapters.AgentsDataAdapter;
import crm.tranquil_sales_steer.ui.models.AgentsMainResponse;
import crm.tranquil_sales_steer.ui.models.AgentsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentsDataActivity extends AppCompatActivity implements AgentsDataAdapter.AgentCountClickListener {

    //ActivityAgentsDataBinding binding;

    RelativeLayout backRLID,agentRLID,agentRLID2,agentRLID3;
    RecyclerView agentsCountRVID;
    ProgressBar pb;
    LinearLayout noDataLLID;
    ArrayList<AgentsMainResponse> agentsMainResponses = new ArrayList<>();
    ArrayList<AgentsResponse> agentsResponses = new ArrayList<>();
    AgentsDataAdapter agentsCountAdapter;
    AppCompatTextView headerTitleTVID;

    String headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = DataBindingUtil.setContentView(this,R.layout.activity_agents_data);
        setContentView(R.layout.activity_agents_data);

        if (getIntent()!=null){
            Bundle bundle=getIntent().getExtras();
            if (bundle!=null){

                agentsResponses= (ArrayList<AgentsResponse>) bundle.getSerializable("agent_list");
                headerTitle=  bundle.getString("designation_name");

            }
        }

        backRLID = findViewById(R.id.backRLID);
        agentsCountRVID = findViewById(R.id.agentsRVID);
        noDataLLID = findViewById(R.id.noDataLLID);
        headerTitleTVID = findViewById(R.id.headerTitleTVID);
        pb = findViewById(R.id.pb);

        headerTitleTVID.setText(headerTitle);

        /*agentRLID.setOnClickListener(v -> {

            Intent intent = new Intent(this,AgentHistoryActivity.class);
            startActivity(intent);

        });*/

        backRLID.setOnClickListener(v -> Utilities.finishAnimation(this));

        if (Utilities.isNetworkAvailable(this)){
            loadAgents();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAgents() {

        if (agentsResponses.size() > 0){

            LinearLayoutManager layoutManager = new LinearLayoutManager(AgentsDataActivity.this,RecyclerView.VERTICAL,false);
            agentsCountAdapter = new AgentsDataAdapter(agentsResponses,AgentsDataActivity.this,AgentsDataActivity.this);

            agentsCountRVID.setLayoutManager(layoutManager);
            agentsCountRVID.setAdapter(agentsCountAdapter);
        }

    }

    @Override
    public void onAgentCountItemClick(AgentsResponse response, View v, int pos, AgentsDataAdapter.AgentsVH holder) {

        switch (v.getId()) {

            case R.id.msgRLID:

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", response.getAgent_mobile(), null));
                intent.putExtra("sms_body",  "" );
                startActivity(intent);
                //finish();

                break;
            case R.id.whatsappRLID:

                final BottomSheetDialog dialog1 = new BottomSheetDialog(AgentsDataActivity.this);
                dialog1.setContentView(R.layout.alert_business_whatsapp);

                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                int width1 = ViewGroup.LayoutParams.MATCH_PARENT;
                int height1 = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog1.getWindow().setLayout(width1, height1);
                dialog1.show();

                AppCompatImageView whatsappIVID = dialog1.findViewById(R.id.whatsappIVID);
                AppCompatImageView businessWhatsappIVID = dialog1.findViewById(R.id.businessWhatsappIVID);

                whatsappIVID.setOnClickListener(view1 -> {

                    try {
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getAgent_mobile() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showToast(this,e.getMessage());
                        Toast.makeText(AgentsDataActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                });

                businessWhatsappIVID.setOnClickListener(view2 -> {

                    try {
                        Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=" + "+91" + response.getAgent_mobile() + "&text=" + URLEncoder.encode("", "UTF-8");
                        sendMsg.setPackage("com.whatsapp.w4b");
                        sendMsg.setData(Uri.parse(url));
                        startActivity(sendMsg);
                        dialog1.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilities.showToast(this,e.getMessage());
                        Toast.makeText(AgentsDataActivity.this, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                    }

                    dialog1.dismiss();

                });

                break;

            case R.id.phnRLID:

               numberCalling(response.getAgent_mobile());

                break;
        }

    }

    private void numberCalling(String number) {

        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
                // Log.d("CONSTRUCTOR_ID_EXE_1->", "" + callID);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}