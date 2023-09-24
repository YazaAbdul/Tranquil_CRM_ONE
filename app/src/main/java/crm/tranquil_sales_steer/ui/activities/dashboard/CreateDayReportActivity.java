package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.CreateDayReportAdapter;
import crm.tranquil_sales_steer.ui.models.CreateDayreportResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDayReportActivity extends AppCompatActivity  {

    RelativeLayout backRLID;
    RecyclerView dayReportRVID;
    WebView webView;
    TextView headerTittleTVID;
    ProgressBar pb;
    AppCompatTextView noDataTVID;

    RecyclerView createDayReportRVID;
    CreateDayReportAdapter adapter;

    String userID;
    ArrayList<CreateDayreportResponse> createDayreportResponseArrayList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_create_day_report);
        webView=findViewById(R.id.webweview);
   /*     webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();*/




        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);

        backRLID = findViewById(R.id.backRLID);
        dayReportRVID = findViewById(R.id.dayReportRVID);

        pb = findViewById(R.id.pb);
        noDataTVID = findViewById(R.id.noDataTVID);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        createDayReportRVID = findViewById(R.id.createDayReportRVID);
        headerTittleTVID.setText("Create Day Report");

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });




        if (Utilities.isNetworkAvailable(this)){
            LoadCreateDayReport();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }











    }

    private void LoadCreateDayReport() {
        pb.setVisibility(View.VISIBLE);
        createDayReportRVID.setVisibility(View.GONE);
        noDataTVID.setVisibility(View.GONE);
        ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CreateDayreportResponse>> call = apiInterface.getCreateDayReport(userID);
        Log.e("api==>", call.request().toString());

        call.enqueue(new Callback<ArrayList<CreateDayreportResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CreateDayreportResponse>> call, Response<ArrayList<CreateDayreportResponse>> response) {
                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.GONE);
                createDayReportRVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200){
                    createDayreportResponseArrayList=response.body();
                    if (createDayreportResponseArrayList.size() > 0){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(CreateDayReportActivity.this,LinearLayoutManager.VERTICAL,false);
                        createDayReportRVID.setLayoutManager(layoutManager);
                        adapter = new CreateDayReportAdapter(CreateDayReportActivity.this,createDayreportResponseArrayList);
                        createDayReportRVID.setAdapter(adapter);


                    }else{
                        pb.setVisibility(View.GONE);
                        noDataTVID.setVisibility(View.VISIBLE);
                        createDayReportRVID.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CreateDayreportResponse>> call, Throwable t) {

            }
        });



    }


}