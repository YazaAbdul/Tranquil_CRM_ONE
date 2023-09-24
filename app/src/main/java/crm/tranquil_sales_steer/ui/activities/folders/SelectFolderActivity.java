package crm.tranquil_sales_steer.ui.activities.folders;

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
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.SearchViewActivity;
import crm.tranquil_sales_steer.ui.adapters.AllUploadedFilesAdapter;
import crm.tranquil_sales_steer.ui.models.AllUploadFilesResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFolderActivity extends AppCompatActivity implements AllUploadedFilesAdapter.AllUploadedFilesClickListener {


    AllUploadedFilesAdapter allUploadedFilesAdapter;
    ArrayList<AllUploadFilesResponse> allUploadFilesResponses = new ArrayList<>();

    RecyclerView selectedFolderRVID;
    ProgressBar pb;
    AppCompatTextView noDataTVID;
    RelativeLayout backRLID;
    TextView headerTittleTVID;
    String title,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_folder);
        Utilities.setStatusBarGradiant(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                title = bundle.getString("FOLDER_NAME");
                id = bundle.getString("FOLDER_ID");
            }
        }

        selectedFolderRVID = findViewById(R.id.selectedFolderRVID);
        pb = findViewById(R.id.pb);
        noDataTVID = findViewById(R.id.noDataTVID);
        backRLID = findViewById(R.id.backRLID);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);

        headerTittleTVID.setText(title);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadAllUploadedFiles();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadAllUploadedFiles() {
        pb.setVisibility(View.VISIBLE);
        selectedFolderRVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<AllUploadFilesResponse>> call = apiInterface.getSelectFolderType(id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllUploadFilesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllUploadFilesResponse>> call, Response<ArrayList<AllUploadFilesResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.GONE);
                selectedFolderRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    allUploadFilesResponses = response.body();

                    if (allUploadFilesResponses.size() > 0){

                        allUploadedFilesAdapter = new AllUploadedFilesAdapter(allUploadFilesResponses,SelectFolderActivity.this,SelectFolderActivity.this);
                        selectedFolderRVID.setAdapter(allUploadedFilesAdapter);
                    }else {
                        pb.setVisibility(View.GONE);
                        noDataTVID.setVisibility(View.VISIBLE);
                        selectedFolderRVID.setVisibility(View.GONE);
                    }
                }else {
                    pb.setVisibility(View.GONE);
                    noDataTVID.setVisibility(View.VISIBLE);
                    selectedFolderRVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllUploadFilesResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.VISIBLE);
                selectedFolderRVID.setVisibility(View.GONE);

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
                Intent intent = new Intent(SelectFolderActivity.this, SearchViewActivity.class);
                intent.putExtra("TYPE", "FOLDERS");
                startActivity(intent);
                break;

        }
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
                        Toast.makeText(SelectFolderActivity.this, "File Deleted Successfully", Toast.LENGTH_SHORT).show();

                        loadAllUploadedFiles();

                    }else {
                        Toast.makeText(SelectFolderActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SelectFolderActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Toast.makeText(SelectFolderActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}