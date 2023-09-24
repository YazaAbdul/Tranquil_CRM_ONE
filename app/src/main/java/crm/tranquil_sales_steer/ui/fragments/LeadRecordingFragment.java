package crm.tranquil_sales_steer.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.RecordingsList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadRecordingFragment extends Fragment {

    private static final String ARG_KEY_LEAD_ID = "lead_id";
    private static final String ARG_KEY_MOBILE = "lead_mobile";
    String VALUE_LEAD_ID,VALUE_LEAD_MOBILE,userID,userType;
    ArrayList<RecordingsList> recordingsLists = new ArrayList<>();

    RecyclerView historyRCID;
    TextView noData;
    ProgressBar pb;

    public LeadRecordingFragment newInstance(String mobileStr) {

        LeadRecordingFragment subFrag = new LeadRecordingFragment();
        Bundle b = new Bundle();
        //b.putString(ARG_KEY_LEAD_ID, customerId);
        b.putString(ARG_KEY_MOBILE, mobileStr);
        subFrag.setArguments(b);
        return subFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VALUE_LEAD_MOBILE = getArguments().getString(ARG_KEY_MOBILE);
        //recordingsLists = (ArrayList<RecordingsList>) getArguments().getSerializable("ACTIVITY_HISTORY");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lead_recordings, container, false);

        historyRCID = view.findViewById(R.id.historyRCID);
        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false);
        historyRCID.setLayoutManager(layoutManager);
        pb=view.findViewById(R.id.pb);
        noData = view.findViewById(R.id.noData);

        userID = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_TYPE);


       /* if (recordingsLists.size() > 0) {
            noData.setVisibility(View.GONE);
            historyRCID.setVisibility(View.VISIBLE);
            ActivityRecords adapter = new ActivityRecords(getActivity(), recordingsLists);

            historyRCID.setAdapter(adapter);
        } else {
            noData.setVisibility(View.VISIBLE);
            historyRCID.setVisibility(View.GONE);
        }*/

        loadRecordings();
        return view;
    }

    private void loadRecordings() {

        historyRCID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<RecordingsList>> call = apiInterface.getRecordingsList(userID,userType,VALUE_LEAD_MOBILE);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RecordingsList>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<RecordingsList>> call, Response<ArrayList<RecordingsList>> response) {

                historyRCID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    recordingsLists = response.body();
                    if (recordingsLists.size() > 0) {
                        for (int i = 0; i < recordingsLists.size(); i++) {
                            try {
                                ActivityRecords adapter = new ActivityRecords(getActivity(),recordingsLists);
                                historyRCID.setAdapter(adapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {

                    }

                } else {

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<RecordingsList>> call, Throwable t) {

            }
        });
    }




    public class ActivityRecords extends RecyclerView.Adapter<ActivityRecords.ActivitiesVH> {

        Activity activity;
        LayoutInflater inflater;
        private ArrayList<RecordingsList> recordingsLists = new ArrayList<>();

        String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


        public ActivityRecords(Activity activity, ArrayList<RecordingsList> recordingsLists) {
            this.activity = activity;
            this.recordingsLists = recordingsLists;
            inflater = LayoutInflater.from(activity);
        }

        @NonNull
        @Override
        public ActivitiesVH onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            return new ActivitiesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.recordings_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ActivitiesVH activitiesVH, final int position) {
            GradientDrawable bgShape = (GradientDrawable) activitiesVH.imageViewIcon.getBackground();
            bgShape.setColor(Color.parseColor(mColors[position % 40]));
            activitiesVH.iconTitleID.setText(recordingsLists.get(position).getUser_name());
            activitiesVH.customerNameTVID.setText(recordingsLists.get(position).getUser_name());
            activitiesVH.projectTVID.setText(recordingsLists.get(position).getUser_name());

            activitiesVH.callDateTVID.setText(recordingsLists.get(position).getCreated_date());
            activitiesVH.durationTVID.setText(recordingsLists.get(position).getDuration()+ " Sec");
            activitiesVH.callTimeTVID.setText(recordingsLists.get(position).getDuration());


            /*int minutes = Integer.parseInt(recordingsLists.get(position).getDuration());

            int min = (minutes/ 60) % 60;

            Log.e("call_min==>","" + min);*/

            //activitiesVH.callTimeTVID.setText(String.valueOf(min));

            activitiesVH.callByTVID.setText(recordingsLists.get(position).getUser_name());
            activitiesVH.audioLLID.setOnClickListener(v -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(recordingsLists.get(position).getSound_file()), "audio/*");
                startActivity(intent);
            });
            activitiesVH.callID.setOnClickListener(v -> {
                //dialedCalls(customerID, userID, numberStr);
            });
            activitiesVH.phoneLLID.setVisibility(View.GONE);
        }


        @Override
        public int getItemCount() {
            return recordingsLists.size();
        }


        public class ActivitiesVH extends RecyclerView.ViewHolder {

            TextView customerNameTVID;
            TextView callTimeTVID;
            TextView callDateTVID;
            LinearLayout audioLLID;
            TextView durationTVID;
            LinearLayout phoneLLID;
            TextView projectTVID;
            TextView iconTitleID;
            TextView callByTVID;
            RelativeLayout imageViewIcon, callID;

            public ActivitiesVH(@NonNull View view) {
                super(view);
                customerNameTVID = view.findViewById(R.id.customerNameTVID);
                callTimeTVID = view.findViewById(R.id.callTimeTVID);
                callDateTVID = view.findViewById(R.id.callDateTVID);
                audioLLID = view.findViewById(R.id.audioLLID);
                durationTVID = view.findViewById(R.id.durationTVID);
                phoneLLID = view.findViewById(R.id.phoneLLID);
                projectTVID = view.findViewById(R.id.projectTVID);
                iconTitleID = view.findViewById(R.id.iconTitleID);
                imageViewIcon = view.findViewById(R.id.imageViewIcon);
                callID = view.findViewById(R.id.callID);
                callByTVID=view.findViewById(R.id.callByTVID);
            }
        }
    }


}
