package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.AllLeadPagesAdapter;
import crm.tranquil_sales_steer.ui.adapters.CollectionsAdapter;
import crm.tranquil_sales_steer.ui.models.CollectionsResponse;
import crm.tranquil_sales_steer.ui.models.CollectionsResponse;
import crm.tranquil_sales_steer.ui.models.CollectionsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsActivity extends AppCompatActivity {

    RelativeLayout backRLID;
    LinearLayout noDataLLID;
    ProgressBar pb;
    RecyclerView collectionsRVID;

    LinearLayoutManager linearLayoutManager;

    CollectionsAdapter collectionsAdapter;

    ArrayList<CollectionsResponse> collectionsResponses = new ArrayList<>();

    int start = 0;
    int end = 30;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    boolean isCallClick=true;
    
    String userID,userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        userID = MySharedPreferences.getPreferences(CollectionsActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(CollectionsActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        backRLID = findViewById(R.id.backRLID);
        noDataLLID = findViewById(R.id.noDataLLID);
        pb = findViewById(R.id.pb);
        collectionsRVID = findViewById(R.id.collectionsRVID);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadDailyCollections();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDailyCollections() {
        //MySharedPreferences.setPreference(this, AppConstants.CLEAR, AppConstants.NO);
        collectionsResponses.clear();
        start = 0;
        end = 30;
        isLoading=false;
        isLastPage=false;
        collectionsRVID = findViewById(R.id.collectionsRVID);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        collectionsAdapter = new CollectionsAdapter(this,collectionsResponses);
        collectionsAdapter.notifyDataSetChanged();

        collectionsRVID.setAdapter(collectionsAdapter);
        collectionsRVID.setLayoutManager(linearLayoutManager);
        collectionsRVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("pagination","api called");
                isLastPage =false;
                isLoading = true;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                Log.d("pagination","is last page");
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                Log.d("pagination","is loading");
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage() {

        pb.setVisibility(View.VISIBLE);
        collectionsRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
        isClickable = false;


           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CollectionsResponse>> call = apiInterface.getDailyCollections(userID, userType, start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CollectionsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CollectionsResponse>> call, Response<ArrayList<CollectionsResponse>> response) {

                pb.setVisibility(View.GONE);
                collectionsRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);
                //refreshFab.setVisibility(View.VISIBLE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    collectionsResponses.clear();
                    collectionsResponses = response.body();
                    if (collectionsResponses.size() > 0) {
                        isClickable = true;
                        startedValues();
                        collectionsAdapter.addAll(collectionsResponses);

                        if (collectionsResponses.size() == 30) {
                            collectionsAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        setErrorViews();
                    }
                } else {
                    setErrorViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CollectionsResponse>> call, Throwable t) {
                setErrorViews();
            }
        });
    }

    private void loadNextPage() {

        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CollectionsResponse>> call = apiInterface.getDailyCollections(userID, userType, start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CollectionsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CollectionsResponse>> call, Response<ArrayList<CollectionsResponse>> response) {

                isClickable = true;

                if (response.body() != null && response.code() == 200) {

                    collectionsResponses = response.body();
                    collectionsAdapter.removeLoadingFooter();
                    isLoading = false;

                    if (collectionsResponses.size() > 0) {
                        collectionsAdapter.addAll(collectionsResponses);

                        if (collectionsResponses.size() == 30) {
                            startedValues();
                            collectionsAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            collectionsAdapter.removeLoadingFooter();
                        }
                    } else {
                        collectionsAdapter.removeLoadingFooter();
                    }
                } else {
                    collectionsAdapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CollectionsResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                collectionsAdapter.removeLoadingFooter();
            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        collectionsRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {

        start = start + 30;
        //end = end + 30;
    }
}