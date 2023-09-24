package crm.tranquil_sales_steer.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by venkei on 22-Jun-19.
 */
public class LeadJourneyFragment extends Fragment {


    ExpandableHeightGridView availableLVID;
    ProgressBar pb;
    TextView noData;
    ImageView noDataImage;

    ArrayList<LeadHistoryResponse> performanceResponses = new ArrayList<>();
    LeadHistoryAdapter adapter;

    private static final String ARG_KEY_LEAD_ID = "lead_id";
    String VALUE_LEAD_ID;

    public LeadJourneyFragment newInstance(String customerId) {

        LeadJourneyFragment subFrag = new LeadJourneyFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY_LEAD_ID, customerId);
        subFrag.setArguments(b);
        return subFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VALUE_LEAD_ID = getArguments().getString(ARG_KEY_LEAD_ID);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lead_history, container, false);

        availableLVID = view.findViewById(R.id.availableLVID);
        pb = view.findViewById(R.id.pb);
        noData = view.findViewById(R.id.noData);
        noDataImage = view.findViewById(R.id.noDataImage);

        availableLVID.setFocusable(false);
        if (Utilities.isNetworkAvailable(getActivity())) {
            loaProjects(VALUE_LEAD_ID);
        } else {
            Utilities.AlertDaiolog(getActivity(), "No Internet...!", "Please check your internet connection", 1, null, "OK");
        }

        Log.d("CUSTOMER_ID", "" + VALUE_LEAD_ID);

        return view;
    }

    private void loaProjects(String leadID) {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<LeadHistoryResponse>> call = apiInterface.getLeadJourney(leadID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<LeadHistoryResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<LeadHistoryResponse>> call, Response<ArrayList<LeadHistoryResponse>> response) {
                pb.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    performanceResponses = response.body();
                    if (performanceResponses.size() > 0) {
                        for (int i = 0; i < performanceResponses.size(); i++) {
                            try {
                                adapter = new LeadHistoryAdapter(getActivity(), performanceResponses);
                                availableLVID.setAdapter(adapter);
                                availableLVID.setExpanded(true);
                                availableLVID.setFocusable(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("Activities Not Available");
                        noDataImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                    noData.setText("Activities Not Available");
                }

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<LeadHistoryResponse>> call, Throwable t) {
                noData.setVisibility(View.VISIBLE);
                noData.setText("Activities Not Available");
                noDataImage.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CUSTOMER_ID", "" + VALUE_LEAD_ID);
        if (Utilities.isNetworkAvailable(getActivity())) {
            performanceResponses.clear();
            loaProjects(VALUE_LEAD_ID);
        } else {
            Utilities.AlertDaiolog(getActivity(), "No Internet...!", "Please check your internet connection", 1, null, "OK");
        }
    }

    public class LeadHistoryAdapter extends BaseAdapter {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<LeadHistoryResponse> availabilityList = new ArrayList<>();

        public LeadHistoryAdapter(Activity activity, ArrayList<LeadHistoryResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return availabilityList.size();
        }

        @Override
        public Object getItem(int i) {
            return availabilityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.lead_history_list_item, null);

            if (i % 2 == 1) {

                view.setBackgroundColor(getResources().getColor(R.color.edit_d));

            } else {

                view.setBackgroundColor(getResources().getColor(R.color.white));

            }

            TextView projectNameTVID = view.findViewById(R.id.activityNameID);
            TextView activityDateID = view.findViewById(R.id.activityDateID);
            TextView activityNotesID = view.findViewById(R.id.activityNotesID);
            ImageView completeImage = view.findViewById(R.id.completeImage);
            ImageView cancelImage = view.findViewById(R.id.cancelImage);


            projectNameTVID.setText(Utilities.CapitalText(availabilityList.get(i).getFrm_activity() + " ---> " + availabilityList.get(i).getActivity_name()));
            activityDateID.setText(availabilityList.get(i).getActivity_date());


            String notes=availabilityList.get(i).getNotes();
            activityNotesID.setText("Updated By : " + Utilities.CapitalText(availabilityList.get(i).getUpdated_by()));

            /*if (notes.equals("")){
                activityNotesID.setText("Notes not given");
            }else{
            }*/


            String status = availabilityList.get(i).getActivity_status();

            if (status.equalsIgnoreCase("1")) {
                cancelImage.setVisibility(View.GONE);
                completeImage.setVisibility(View.VISIBLE);
            } else if (status.equalsIgnoreCase("2")) {
                cancelImage.setVisibility(View.VISIBLE);
                completeImage.setVisibility(View.GONE);
            }

            return view;
        }
    }

    public class LeadHistoryResponse {
        /*act_no: "17",
activity_name: "Followup",
activity_date: "2019-07-16",
notes: ""*/

        private String act_no;
        private String activity_name;
        private String activity_date;
        private String frm_activity;
        private String notes;
        private String activity_status;
        private String updated_by;

        public LeadHistoryResponse(String act_no, String activity_name, String activity_date, String notes, String activity_status) {
            this.act_no = act_no;
            this.activity_name = activity_name;
            this.activity_date = activity_date;
            this.notes = notes;
            this.activity_status = activity_status;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public String getFrm_activity() {
            return frm_activity;
        }

        public void setFrm_activity(String frm_activity) {
            this.frm_activity = frm_activity;
        }

        public String getActivity_status() {
            return activity_status;
        }

        public void setActivity_status(String activity_status) {
            this.activity_status = activity_status;
        }

        public String getAct_no() {
            return act_no;
        }

        public void setAct_no(String act_no) {
            this.act_no = act_no;
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

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }
}
