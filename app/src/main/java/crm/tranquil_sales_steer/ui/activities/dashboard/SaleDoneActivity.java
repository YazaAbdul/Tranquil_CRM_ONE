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
import crm.tranquil_sales_steer.ui.adapters.SaleDoneAdapter;
import crm.tranquil_sales_steer.ui.adapters.SaleDoneAdapter;
import crm.tranquil_sales_steer.ui.models.SaleDoneResponse;
import crm.tranquil_sales_steer.ui.models.SaleDoneResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleDoneActivity extends AppCompatActivity {

    RelativeLayout backRLID;

    LinearLayout noDataLLID;
    ProgressBar pb;
    RecyclerView saleDoneRVID;

    LinearLayoutManager linearLayoutManager;

    SaleDoneAdapter adapter;

    ArrayList<SaleDoneResponse> saleDoneResponses = new ArrayList<>();

    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    boolean isCallClick=true;

    String userID,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_done);

        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        backRLID = findViewById(R.id.backRLID);
        noDataLLID = findViewById(R.id.noDataLLID);
        pb = findViewById(R.id.pb);
        saleDoneRVID = findViewById(R.id.saleDoneRVID);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        if (Utilities.isNetworkAvailable(this)){
            loadDailySaleDone();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDailySaleDone() {

        saleDoneResponses.clear();
        start = 0;
        isLoading=false;
        isLastPage=false;
        saleDoneRVID = findViewById(R.id.saleDoneRVID);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new SaleDoneAdapter(this,saleDoneResponses);
        adapter.notifyDataSetChanged();

        saleDoneRVID.setAdapter(adapter);
        saleDoneRVID.setLayoutManager(linearLayoutManager);
        saleDoneRVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
        saleDoneRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.GONE);
        isClickable = false;


           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SaleDoneResponse>> call = apiInterface.getSaleDone(userID, userType, start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SaleDoneResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SaleDoneResponse>> call, Response<ArrayList<SaleDoneResponse>> response) {

                pb.setVisibility(View.GONE);
                saleDoneRVID.setVisibility(View.VISIBLE);
                noDataLLID.setVisibility(View.GONE);
                //refreshFab.setVisibility(View.VISIBLE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    saleDoneResponses.clear();
                    saleDoneResponses = response.body();
                    if (saleDoneResponses.size() > 0) {
                        isClickable = true;
                        startedValues();
                        adapter.addAll(saleDoneResponses);

                        if (saleDoneResponses.size() == 30) {
                            adapter.addLoadingFooter();
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
            public void onFailure(Call<ArrayList<SaleDoneResponse>> call, Throwable t) {
                setErrorViews();
            }
        });
    }

    private void loadNextPage() {

        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SaleDoneResponse>> call = apiInterface.getSaleDone(userID, userType, start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SaleDoneResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SaleDoneResponse>> call, Response<ArrayList<SaleDoneResponse>> response) {

                isClickable = true;

                if (response.body() != null && response.code() == 200) {

                    saleDoneResponses = response.body();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (saleDoneResponses.size() > 0) {
                        adapter.addAll(saleDoneResponses);

                        if (saleDoneResponses.size() == 30) {
                            startedValues();
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            adapter.removeLoadingFooter();
                        }
                    } else {
                        adapter.removeLoadingFooter();
                    }
                } else {
                    adapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SaleDoneResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        saleDoneRVID.setVisibility(View.GONE);
        noDataLLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {

        start = start + 30;
        //end = end + 30;
    }
}