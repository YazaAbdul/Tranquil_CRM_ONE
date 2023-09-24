package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.models.SendFileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewActivity extends AppCompatActivity {

    ProgressBar pb;
    String companyID, type,userID,userType;
    ListView searchLVID;
    ImageView noDataImage;
    TextView noData;
    EditText keyETID;

    ArrayList<SearchResponse> searchResponses = new ArrayList<>();
    SearchAdapter adapter;
    SearchFileAdapter searchFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search_view);
        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedAnimation();
            }
        });

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                type = bundle.getString("TYPE");
            }
        }

        companyID = MySharedPreferences.getPreferences(SearchViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        userID = MySharedPreferences.getPreferences(SearchViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(SearchViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        pb = findViewById(R.id.pb);
        searchLVID = findViewById(R.id.searchLVID);
        noDataImage = findViewById(R.id.noDataImage);
        noData = findViewById(R.id.noData);
        keyETID = findViewById(R.id.keyETID);
        keyETID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String key = keyETID.getText().toString();

                if (type.equalsIgnoreCase("FOLDERS")){
                    searchForSendFolder(key, companyID);
                }else {
                    searchSuggestions(key, companyID);
                }

            }
        });
    }

    private void searchForSendFolder(String key, String companyID) {

        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
        searchLVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SearchResponse>> call = apiInterface.getSearchForFileSend(key, companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SearchResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchResponse>> call, Response<ArrayList<SearchResponse>> response) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                noDataImage.setVisibility(View.GONE);
                searchLVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200) {
                    searchResponses = response.body();
                    if (searchResponses != null && searchResponses.size() > 0) {
                        for (int i = 0; i < searchResponses.size(); i++) {
                            searchFileAdapter = new SearchFileAdapter(SearchViewActivity.this, searchResponses);
                            searchLVID.setAdapter(searchFileAdapter);
                        }
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        noDataImage.setVisibility(View.VISIBLE);
                        searchLVID.setVisibility(View.GONE);
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                    searchLVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SearchResponse>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noDataImage.setVisibility(View.VISIBLE);
                searchLVID.setVisibility(View.GONE);
            }
        });


    }

    private void searchSuggestions(String key, String companyID) {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
        searchLVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SearchResponse>> call = apiInterface.getSearchResponse(key, userID,userType);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SearchResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchResponse>> call, Response<ArrayList<SearchResponse>> response) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                noDataImage.setVisibility(View.GONE);
                searchLVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200) {
                    searchResponses = response.body();
                    if (searchResponses != null && searchResponses.size() > 0) {
                        for (int i = 0; i < searchResponses.size(); i++) {
                            adapter = new SearchAdapter(SearchViewActivity.this, searchResponses);
                            searchLVID.setAdapter(adapter);
                        }
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        noDataImage.setVisibility(View.VISIBLE);
                        searchLVID.setVisibility(View.GONE);
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                    searchLVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SearchResponse>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noDataImage.setVisibility(View.VISIBLE);
                searchLVID.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        onBackPressedAnimation();
    }

    private void onBackPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }


    public class SearchResponse {

        /*"activity_id": "2",
    "activity_name": "Phone Call",
    "activity_date": "2022-09-09",
    "activity_time": "18:22:00"*/

        private String lead_id;
        private String lead_name;
        private String lead_email;
        private String lead_mobile;
        private String c_id;
        private String activity_id;
        private String activity_name;
        private String activity_date;
        private String activity_time;

        public SearchResponse(String lead_id, String lead_name, String lead_email, String lead_mobile, String c_id, String activity_id, String activity_name, String activity_date, String activity_time) {
            this.lead_id = lead_id;
            this.lead_name = lead_name;
            this.lead_email = lead_email;
            this.lead_mobile = lead_mobile;
            this.c_id = c_id;
            this.activity_id = activity_id;
            this.activity_name = activity_name;
            this.activity_date = activity_date;
            this.activity_time = activity_time;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getActivity_date() {
            return activity_date;
        }

        public void setActivity_date(String activity_date) {
            this.activity_date = activity_date;
        }

        public String getActivity_time() {
            return activity_time;
        }

        public void setActivity_time(String activity_time) {
            this.activity_time = activity_time;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getLead_id() {
            return lead_id;
        }

        public void setLead_id(String lead_id) {
            this.lead_id = lead_id;
        }

        public String getLead_name() {
            return lead_name;
        }

        public void setLead_name(String lead_name) {
            this.lead_name = lead_name;
        }

        public String getLead_email() {
            return lead_email;
        }

        public void setLead_email(String lead_email) {
            this.lead_email = lead_email;
        }

        public String getLead_mobile() {
            return lead_mobile;
        }

        public void setLead_mobile(String lead_mobile) {
            this.lead_mobile = lead_mobile;
        }
    }

    public class SearchAdapter extends BaseAdapter {

        private ArrayList<SearchResponse> searchResponses = new ArrayList<>();
        private Activity activity;
        private LayoutInflater inflater;

        public SearchAdapter(Activity activity, ArrayList<SearchResponse> searchResponses) {
            this.activity = activity;
            this.searchResponses = searchResponses;
            inflater = LayoutInflater.from(activity);
        }


        @Override
        public int getCount() {
            return searchResponses.size();
        }

        @Override
        public Object getItem(int i) {
            return searchResponses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {


            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.search_suggestions_list_item, null);
            if (i % 2 == 1) {
                view.setBackgroundColor(getResources().getColor(R.color.edit_d));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }
            TextView searchLeadNameTVID = view.findViewById(R.id.searchLeadNameTVID);
            TextView searchLeadMobileTVID = view.findViewById(R.id.searchLeadMobileTVID);
            searchLeadNameTVID.setText(Utilities.CapitalText(searchResponses.get(i).getLead_name()));
            searchLeadMobileTVID.setText(searchResponses.get(i).getLead_mobile());

            RelativeLayout newMainLeadView = view.findViewById(R.id.newMainLeadView);
            newMainLeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (type.equalsIgnoreCase("SEARCH")) {
                        Intent intent = new Intent(activity, LeadHistoryActivity.class);
                        intent.putExtra("CUSTOMER_ID", "" + searchResponses.get(i).getLead_id());
                        intent.putExtra("CUSTOMER_NAME", "" + searchResponses.get(i).getLead_name());
                        intent.putExtra("ACTIVITY_NAME","" + searchResponses.get(i).getActivity_name());
                        intent.putExtra("CUSTOMER_MOBILE", searchResponses.get(i).getLead_mobile());
                        intent.putExtra("ACTIVITY_ID","" + searchResponses.get(i).getActivity_id());
                        intent.putExtra("pageFrom", false);
                        activity.startActivity(intent);
                    } else if (type.equalsIgnoreCase("CUSTOMER_MEET")) {
                        Intent intent = new Intent(activity, DashBoardCustomerMeetActivity.class);
                        intent.putExtra("CUSTOMER_ID", "" + searchResponses.get(i).getLead_id());
                        intent.putExtra("CUSTOMER_NAME", "" + searchResponses.get(i).getLead_name());
                        intent.putExtra("CUSTOMER_MOBILE", "" + searchResponses.get(i).getLead_mobile());
                        activity.startActivity(intent);
                    }
                }
            });

            return view;
        }
    }

    public class SearchFileAdapter extends BaseAdapter {

        private ArrayList<SearchResponse> searchResponses = new ArrayList<>();
        private Activity activity;
        private LayoutInflater inflater;

        public SearchFileAdapter(Activity activity, ArrayList<SearchResponse> searchResponses) {
            this.activity = activity;
            this.searchResponses = searchResponses;
            inflater = LayoutInflater.from(activity);
        }


        @Override
        public int getCount() {
            return searchResponses.size();
        }

        @Override
        public Object getItem(int i) {
            return searchResponses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {


            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.search_file_item, null);
            if (i % 2 == 1) {
                view.setBackgroundColor(getResources().getColor(R.color.edit_d));
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.white));
            }
            AppCompatTextView nameTVID = view.findViewById(R.id.nameTVID);
            AppCompatTextView numberTVID = view.findViewById(R.id.numberTVID);
            AppCompatButton sendBtn = view.findViewById(R.id.sendBtn);
            nameTVID.setText(Utilities.CapitalText(searchResponses.get(i).getLead_name()));
            numberTVID.setText(searchResponses.get(i).getLead_mobile());

            sendBtn.setOnClickListener(v -> {

                getSendFileResponse(searchResponses.get(i).lead_id,searchResponses.get(i).getC_id(),activity);

            });

            /*RelativeLayout newMainLeadView = view.findViewById(R.id.newMainLeadView);
            newMainLeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (type.equalsIgnoreCase("SEARCH")) {
                        Intent intent = new Intent(activity, LeadHistoryActivity.class);
                        intent.putExtra("CUSTOMER_ID", "" + searchResponses.get(i).getLead_id());
                        intent.putExtra("CUSTOMER_NAME", "" + searchResponses.get(i).getLead_name());
                        intent.putExtra("CUSTOMER_MOBILE", "" + searchResponses.get(i).getLead_mobile());
                        intent.putExtra("pageFrom", false);
                        activity.startActivity(intent);
                    } else if (type.equalsIgnoreCase("CUSTOMER_MEET")) {
                        Intent intent = new Intent(activity, DashBoardCustomerMeetActivity.class);
                        intent.putExtra("CUSTOMER_ID", "" + searchResponses.get(i).getLead_id());
                        intent.putExtra("CUSTOMER_NAME", "" + searchResponses.get(i).getLead_name());
                        intent.putExtra("CUSTOMER_MOBILE", "" + searchResponses.get(i).getLead_mobile());
                        activity.startActivity(intent);
                    }
                }
            });*/

            return view;
        }
    }

    private void getSendFileResponse(String lead_id, String c_id, Activity activity) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<SendFileResponse> call = apiInterface.getSendFile(userID,lead_id,c_id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<SendFileResponse>() {
            @Override
            public void onResponse(Call<SendFileResponse> call, Response<SendFileResponse> response) {

                if (response.body() != null && response.code() == 200){

                    SendFileResponse sendFileResponse = response.body();

                    if (sendFileResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(SearchViewActivity.this, "File Send Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    }else {

                        if (searchResponses.get(0).getLead_email().isEmpty()){
                            Toast.makeText(SearchViewActivity.this, "There is no email", Toast.LENGTH_SHORT).show();
                        }else {

                            Toast.makeText(SearchViewActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<SendFileResponse> call, Throwable t) {

                Toast.makeText(SearchViewActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

}
