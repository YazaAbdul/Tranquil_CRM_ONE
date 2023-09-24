package crm.tranquil_sales_steer.ui.activities.folders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.SearchViewActivity;
import crm.tranquil_sales_steer.ui.adapters.AllUploadedFilesAdapter;
import crm.tranquil_sales_steer.ui.adapters.FoldersAdapter;
import crm.tranquil_sales_steer.ui.models.AllUploadFilesResponse;
import crm.tranquil_sales_steer.ui.models.FoldersResponse;
import crm.tranquil_sales_steer.ui.models.SendFileResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoldersActivity extends AppCompatActivity implements AllUploadedFilesAdapter.AllUploadedFilesClickListener {

    FoldersAdapter adapter;
    ArrayList<FoldersResponse> foldersResponses = new ArrayList<>();
    AllUploadedFilesAdapter allUploadedFilesAdapter;
    ArrayList<AllUploadFilesResponse> allUploadFilesResponses = new ArrayList<>();
    RecyclerView foldersRVID,allUploadedFilesRVID;
    ProgressBar pb;
    AppCompatTextView noDataTVID;
    TextView headerTittleTVID;
    RelativeLayout backRLID;
    String type,leadID,userID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        Utilities.setStatusBarGradiant(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                type = bundle.getString("TYPE");
                leadID = bundle.getString("LEAD_ID");
            }
        }

        userID = MySharedPreferences.getPreferences(FoldersActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);

        foldersRVID = findViewById(R.id.foldersRVID);
        allUploadedFilesRVID = findViewById(R.id.allUploadedFilesRVID);
        pb = findViewById(R.id.pb);
        noDataTVID = findViewById(R.id.noDataTVID);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        backRLID = findViewById(R.id.backRLID);

        headerTittleTVID.setText("File Manager");

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadFolders();
            loadAllUploadedFiles();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadAllUploadedFiles() {
        pb.setVisibility(View.VISIBLE);
        allUploadedFilesRVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllUploadFilesResponse>> call = apiInterface.getAllUploadedFiles();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllUploadFilesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllUploadFilesResponse>> call, Response<ArrayList<AllUploadFilesResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.GONE);
                allUploadedFilesRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    allUploadFilesResponses = response.body();

                    if (allUploadFilesResponses.size() > 0){

                     allUploadedFilesAdapter = new AllUploadedFilesAdapter(allUploadFilesResponses,FoldersActivity.this,FoldersActivity.this);
                       allUploadedFilesRVID.setAdapter(allUploadedFilesAdapter);
                    }else {
                        pb.setVisibility(View.GONE);
                        //noDataTVID.setVisibility(View.VISIBLE);
                        allUploadedFilesRVID.setVisibility(View.GONE);
                    }
                }else {
                    pb.setVisibility(View.GONE);
                    //noDataTVID.setVisibility(View.VISIBLE);
                    allUploadedFilesRVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllUploadFilesResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                //noDataTVID.setVisibility(View.VISIBLE);
                foldersRVID.setVisibility(View.GONE);

            }
        });
    }

    private void loadFolders() {

        pb.setVisibility(View.VISIBLE);
        noDataTVID.setVisibility(View.GONE);
        foldersRVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<FoldersResponse>> call = apiInterface.getFoldersTypes();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<FoldersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<FoldersResponse>> call, Response<ArrayList<FoldersResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.GONE);
                foldersRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    foldersResponses = response.body();

                    if (foldersResponses.size() > 0){

                        adapter = new FoldersAdapter(foldersResponses,FoldersActivity.this);
                        foldersRVID.setAdapter(adapter);
                    }else {
                        pb.setVisibility(View.GONE);
                        noDataTVID.setVisibility(View.VISIBLE);
                        foldersRVID.setVisibility(View.GONE);
                    }

                }else {
                    pb.setVisibility(View.GONE);
                    noDataTVID.setVisibility(View.VISIBLE);
                    foldersRVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FoldersResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.VISIBLE);
                foldersRVID.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onUploadedFilesClickItem(AllUploadFilesResponse response, View v, int pos, AllUploadedFilesAdapter.AllUploadedFilesVH holder) {

        switch (v.getId()) {
            case R.id.deleteRLID:
                getDeleteResponse(response.getId());
                break;

            case R.id.sendRLID:
                if (type != null && type.equalsIgnoreCase("FROM_LEADS")) {

                    getSendFileToLead(leadID,response.getId());

                } else {

                    Intent intent = new Intent(FoldersActivity.this, SearchViewActivity.class);
                    intent.putExtra("TYPE", "FOLDERS");
                    startActivity(intent);
                }
                break;
        }

    }

    private void getSendFileToLead(String lead_id, String c_id) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<SendFileResponse> call = apiInterface.getSendFile(userID,lead_id,c_id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<SendFileResponse>() {
            @Override
            public void onResponse(Call<SendFileResponse> call, Response<SendFileResponse> response) {

                if (response.body() != null && response.code() == 200){

                    SendFileResponse sendFileResponse = response.body();

                    if (sendFileResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(FoldersActivity.this, "File Sent Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }else {

                        Toast.makeText(FoldersActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();


                        /*if (searchResponses.get(0).getLead_email().isEmpty()){
                            Toast.makeText(SearchViewActivity.this, "There is no email", Toast.LENGTH_SHORT).show();
                        }else {


                        }*/

                    }
                }
            }

            @Override
            public void onFailure(Call<SendFileResponse> call, Throwable t) {

                Toast.makeText(FoldersActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void getDeleteResponse(String id) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.getDeleteFile(id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if (response.body() != null && response.code() == 200){

                    StatusResponse statusResponse = response.body();

                    if (statusResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(FoldersActivity.this, "File Deleted Successfully", Toast.LENGTH_SHORT).show();

                        loadAllUploadedFiles();

                    }else {
                        Toast.makeText(FoldersActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(FoldersActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Toast.makeText(FoldersActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}