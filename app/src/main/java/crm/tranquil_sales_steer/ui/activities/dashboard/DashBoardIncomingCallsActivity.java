package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.adapters.AllLeadPagesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardIncomingCallsActivity extends AppCompatActivity {

    RecyclerView incomingLVID;
    ProgressBar pb;
    TextView noDataID;
    String user_id;
    IncomingAdapter adapter;
    ArrayList<IncomingResponse> incomingResponses = new ArrayList<>();

    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    boolean isCallClick=true;
    LinearLayoutManager linearLayoutManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_calls);

        AlertUtilities.startAnimation(this);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertUtilities.finishAnimation(DashBoardIncomingCallsActivity.this);
            }
        });
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Incoming Calls");
        user_id = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        incomingLVID = findViewById(R.id.incomingLVID);
        pb = findViewById(R.id.pb);
        noDataID = findViewById(R.id.noDataID);

        if (Utilities.isNetworkAvailable(this)) {
            callIncomingCalls(user_id);
        }else{
            AlertUtilities.bottomDisplayStaticAlert(this,getString(R.string.no_internet_title),getString(R.string.no_internet_des));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.isNetworkAvailable(this)) {
            callIncomingCalls(user_id);
        }else{
            AlertUtilities.bottomDisplayStaticAlert(this,getString(R.string.no_internet_title),getString(R.string.no_internet_des));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callIncomingCalls(String user_id) {

        //incomingResponses.clear();
        start = 0;
        isLoading=false;
        isLastPage=false;
        
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new IncomingAdapter(DashBoardIncomingCallsActivity.this, incomingResponses);
        adapter.notifyDataSetChanged();

        incomingLVID.setAdapter(adapter);
        incomingLVID.setLayoutManager(linearLayoutManager);
        incomingLVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

        incomingLVID.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        noDataID.setVisibility(View.GONE);
        isClickable = false;

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<IncomingResponse>> call = apiInterface.getIncomingDisplayResponse(user_id,start);
        Log.e("api==>",call.request().toString());

        call.enqueue(new Callback<ArrayList<IncomingResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<IncomingResponse>> call, Response<ArrayList<IncomingResponse>> response) {

                incomingLVID.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                noDataID.setVisibility(View.GONE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {

                    incomingResponses = response.body();

                    if (incomingResponses.size() > 0) {

                        isClickable = true;
                        startedValues();
                        adapter.addAll(incomingResponses);

                        if (incomingResponses.size() == 30) {
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
            public void onFailure(Call<ArrayList<IncomingResponse>> call, Throwable t) {
                setErrorViews();
            }
        });
        
    }

    private void loadNextPage() {

        Log.d("START_VALUE",""+start);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<IncomingResponse>> call = apiInterface.getIncomingDisplayResponse(user_id,start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<IncomingResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<IncomingResponse>> call, Response<ArrayList<IncomingResponse>> response) {

                isClickable = true;

                if (response.body() != null && response.code() == 200) {

                    incomingResponses = response.body();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (incomingResponses.size() > 0) {

                        adapter.addAll(incomingResponses);

                        if (incomingResponses.size() == 30) {
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
            public void onFailure(Call<ArrayList<IncomingResponse>> call, Throwable t) {

                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        incomingLVID.setVisibility(View.GONE);
        noDataID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 30;
    }

    @Override
    public void onBackPressed() {
        AlertUtilities.finishAnimation(this);
    }

    public class IncomingResponse implements Serializable {

        private String icm_id;
        private String mobile_no;
        private String user_id;
        private String created_date;
        private String created_time;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcm_id() {
            return icm_id;
        }

        public void setIcm_id(String icm_id) {
            this.icm_id = icm_id;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }
    }

    public class IncomingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_ITEM = 1;
        private static final int VIEW_LOADING = 0;
        private boolean isLoadingAdded = false;

        private ArrayList<IncomingResponse> incomingResponses = new ArrayList<>();
        private Activity activity;


        public IncomingAdapter(Activity activity, ArrayList<IncomingResponse> incomingResponses) {
            this.activity = activity;
            this.incomingResponses = incomingResponses;
        }
        

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case VIEW_ITEM:
                    viewHolder = new IncomingVH (inflater.inflate(R.layout.incoming_list_item, parent, false));
                    break;

                case VIEW_LOADING:
                    viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.progressbar1, parent, false));
                    break;
            }

            return viewHolder;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            switch (getItemViewType(position)) {
                case VIEW_ITEM:
                    IncomingVH itemVH = (IncomingVH) holder;

                    IncomingResponse item = incomingResponses.get(position);

                    itemVH.numberTVID.setText("+91 " + item.getMobile_no());
                    itemVH.nameTVID.setText(item.getName());
                    itemVH.timeID.setText(item.getCreated_date() + ","+item.getCreated_time());

                    if (item.getName() != null && item.getName().equalsIgnoreCase("Not in crm")){
                        
                        itemVH.newLeadTVID.setVisibility(View.VISIBLE);
                        itemVH.existingLeadTVID.setVisibility(View.GONE);
                    }else {
                        itemVH.newLeadTVID.setVisibility(View.GONE);
                        itemVH.existingLeadTVID.setVisibility(View.VISIBLE);
                    }

                    itemVH.itemsViewCVID.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(activity, LeadCreateActivity.class);
                            intent.putExtra("NUMBER",""+item.getMobile_no());
                            intent.putExtra("INCOMING","YES");
                            startActivity(intent);
                        }
                    });

                    break;
            }

        }

        @Override
        public int getItemCount() {

            return incomingResponses == null ? 0 : incomingResponses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == incomingResponses.size() - 1 && isLoadingAdded) ? VIEW_LOADING : VIEW_ITEM;
        }

        public void addLoadingFooter() {
            isLoadingAdded = true;
            add(new IncomingResponse());
        }

        public void removeLoadingFooter() {
            isLoadingAdded = false;
            int position = incomingResponses.size() - 1;
            IncomingResponse result = getItem(position);
            if (result != null) {
                incomingResponses.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void add(IncomingResponse movie) {
            incomingResponses.add(movie);
            notifyItemInserted(incomingResponses.size() - 1);
        }

        public void addAll(List<IncomingResponse> moveResults) {
            for (IncomingResponse result : moveResults) {
                add(result);
            }
        }

        public IncomingResponse getItem(int position) {
            return incomingResponses.get(position);
        }

        

        public class LoadingViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar1;

            public LoadingViewHolder(@NonNull View itemView) {
                super(itemView);
                progressBar1 = itemView.findViewById(R.id.progressBar1);
            }
        }

        public class IncomingVH extends RecyclerView.ViewHolder{

            TextView numberTVID,nameTVID,timeID,newLeadTVID,existingLeadTVID;
            CardView itemsViewCVID;

            public IncomingVH(@NonNull View itemView) {
                super(itemView);

                numberTVID = itemView.findViewById(R.id.numberTVID);
                nameTVID = itemView.findViewById(R.id.nameTVID);
                timeID = itemView.findViewById(R.id.timeID);
                newLeadTVID = itemView.findViewById(R.id.newLeadTVID);
                existingLeadTVID = itemView.findViewById(R.id.existingLeadTVID);
                itemsViewCVID = itemView.findViewById(R.id.itemsViewCVID);
            }
        }
    }
}
