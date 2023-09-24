package crm.tranquil_sales_steer.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.AppSingleton;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.ui.activities.dashboard.AvailabilityProjectsActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.CollectionsActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.DesignationActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.DueCustomersActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.NewLeadsDataActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.SaleDoneActivity;

/**
 * Created by venkei on 22-Jul-19.
 */

public class ActivitiesFragment extends Fragment implements View.OnClickListener {

    RecyclerView availableLVID;
    ProgressBar pb;
    TextView noData;
    ImageView noDataImage;
    MainDisplayAdapter adapter;
    ArrayList<MainDisplayResponse> performanceResponses = new ArrayList<>();
    String companyID,userID,userType;

    private static final String ARG_KEY_MENU_ID = "arg_menu_id";
    private static final String ARG_KEY_MENU_URL = "arg_menu_url";
    private static final String ARG_KEY_ACTIVITY_TITLE = "arg_activity_title";
    String VALUE_MENU_ID, VALUE_MENU_URL, VALUE_ACTIVITY_TITLE;
    public static int pagePosition = 0;
    SwipeRefreshLayout swipe;
    Button buttonID;
    CardView testCVID,overallCVID,todayCVID,pendingCVID,featureCVID,agentsCVID,availabilityCVID,dueCustomersCVID,collectionsCVID,saleDoneCVID;

    public static ActivitiesFragment newInstance(int position, String id, String url, String activityTittle) {
        pagePosition = position;
        ActivitiesFragment subFrag = new ActivitiesFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY_MENU_ID, id);
        b.putString(ARG_KEY_MENU_URL, url);
        b.putString(ARG_KEY_ACTIVITY_TITLE, activityTittle);
        subFrag.setArguments(b);
        return subFrag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VALUE_MENU_ID = getArguments().getString(ARG_KEY_MENU_ID);
        VALUE_MENU_URL = getArguments().getString(ARG_KEY_MENU_URL);
        VALUE_ACTIVITY_TITLE = getArguments().getString(ARG_KEY_ACTIVITY_TITLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities, container, false);

        companyID = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        userID = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_TYPE);
        availableLVID = view.findViewById(R.id.availableLVID);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 2);

        availableLVID.setLayoutManager(layoutManager1);
        pb = view.findViewById(R.id.pb);
        noData = view.findViewById(R.id.noData);
        noDataImage = view.findViewById(R.id.noDataImage);
        overallCVID = view.findViewById(R.id.overallCVID);
        todayCVID = view.findViewById(R.id.todayCVID);
        pendingCVID = view.findViewById(R.id.pendingCVID);
        featureCVID = view.findViewById(R.id.featureCVID);
        agentsCVID = view.findViewById(R.id.agentsCVID);
        availabilityCVID = view.findViewById(R.id.availabilityCVID);
        dueCustomersCVID = view.findViewById(R.id.dueCustomersCVID);
        collectionsCVID = view.findViewById(R.id.collectionsCVID);
        saleDoneCVID = view.findViewById(R.id.saleDoneCVID);

        todayCVID.setOnClickListener(this);
        pendingCVID.setOnClickListener(this);
        featureCVID.setOnClickListener(this);
        agentsCVID.setOnClickListener(this);
        availabilityCVID.setOnClickListener(this);
        dueCustomersCVID.setOnClickListener(this);
        collectionsCVID.setOnClickListener(this);
        saleDoneCVID.setOnClickListener(this);

        overallCVID.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewLeadsDataActivity.class);
            intent.putExtra("PLAN_ID","1");
            intent.putExtra("TITLE","Overall");
            intent.putExtra(AppConstants.VIEW_TYPE, "Plans");
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utilities.isNetworkAvailable(getActivity())) {
            loaProjects(AppConstants.GLOBAL_MAIN_URL + VALUE_MENU_URL + "?company_id=" + companyID + "&menu_id=" + VALUE_MENU_ID+"&user_type="+userType+"&user_id="+userID);
        } else {
            AlertUtilities.bottomDisplayStaticAlert(getActivity(), getString(R.string.no_internet_title), getString(R.string.no_internet_desc));
        }
    }

    private void loaProjects(String url) {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);
        availableLVID.setVisibility(View.GONE);
        Log.e("api==>",url);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONArray response) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                noDataImage.setVisibility(View.GONE);

                if (response == null) {
                    availableLVID.setVisibility(View.VISIBLE);
                } else if (response.equals("null")) {
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("No Data Available");
                    noDataImage.setVisibility(View.VISIBLE);
                } else {
                    try {
                        int count = 0;
                        String activity_id, activity_name,activity_count,color_one,color_two;
                        //leadsResponses.clear();
                        performanceResponses.clear();
                        while (count < response.length()) {
                            JSONObject jo = response.getJSONObject(count);
                            activity_id = jo.getString("activity_id");
                            activity_name = jo.getString("activity_name");
                            activity_count = jo.getString("activity_count");
                            color_one = jo.getString("color_one");
                            color_two = jo.getString("color_two");


                            performanceResponses.add(new MainDisplayResponse(activity_id, activity_name,activity_count,color_one,color_two));
                            count++;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        adapter = new MainDisplayAdapter(getActivity(), performanceResponses);
                        availableLVID.setAdapter(adapter);
                        availableLVID.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noDataImage.setVisibility(View.VISIBLE);
                availableLVID.setVisibility(View.GONE);
                noData.setText("No Data Available");
            }
        });

        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getActivity()).addToRequestQueue(arrayRequest, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.todayCVID:
                Intent intent2 = new Intent(getActivity(), NewLeadsDataActivity.class);
                intent2.putExtra("PLAN_ID","2");
                intent2.putExtra("TITLE","Today");
                intent2.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent2);
                break;
            case R.id.pendingCVID:
                Intent intent3 = new Intent(getActivity(), NewLeadsDataActivity.class);
                intent3.putExtra("PLAN_ID","3");
                intent3.putExtra("TITLE","Pending");
                intent3.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent3);
                break;
            case R.id.featureCVID:
                Intent intent4 = new Intent(getActivity(), NewLeadsDataActivity.class);
                intent4.putExtra("PLAN_ID","4");
                intent4.putExtra("TITLE","Future");
                intent4.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent4);
                break;

            case R.id.agentsCVID:
                Intent intent5 = new Intent(getActivity(), DesignationActivity.class);
                startActivity(intent5);
                break;
            case R.id.availabilityCVID:
                Intent intent6 = new Intent(getActivity(), AvailabilityProjectsActivity.class);
                startActivity(intent6);
                break;

            case R.id.dueCustomersCVID:
                Intent intent7 = new Intent(getActivity(), DueCustomersActivity.class);
                startActivity(intent7);
                break;

            case R.id.collectionsCVID:
                Intent intent8 = new Intent(getActivity(), CollectionsActivity.class);
                startActivity(intent8);
                break;
            case R.id.saleDoneCVID:
                Intent intent9 = new Intent(getActivity(), SaleDoneActivity.class);
                startActivity(intent9);
                break;
        }

    }

    public class MainDisplayAdapter extends RecyclerView.Adapter<MainDisplayAdapter.PreSalesVH> {

        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<MainDisplayResponse> availabilityList = new ArrayList<>();

        public MainDisplayAdapter(Activity activity, ArrayList<MainDisplayResponse> availabilityList) {
            try {
                this.activity = activity;
                this.availabilityList = availabilityList;
                inflater = LayoutInflater.from(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public MainDisplayAdapter.PreSalesVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MainDisplayAdapter.PreSalesVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.z_menu_list_item_1, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainDisplayAdapter.PreSalesVH holder, @SuppressLint("RecyclerView") final int i) {

            holder.textID.setText(Utilities.CapitalText(availabilityList.get(i).getActivity_name()));


            if (availabilityList.get(i).getColor_one() !=null && availabilityList.get(i).getColor_two()!= null){

                try {
                    int[] colors = {Color.parseColor(availabilityList.get(i).getColor_one()),Color.parseColor(availabilityList.get(i).getColor_two())};

                    Log.e("colors1==>", String.valueOf(Color.parseColor(availabilityList.get(i).getColor_one())));
                    Log.e("colors2==>", String.valueOf(Color.parseColor(availabilityList.get(i).getColor_two())));

                    GradientDrawable gd = new GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT, colors);
                    gd.setCornerRadius(0f);

                    holder.imageViewIcon.setBackground(gd);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {



            }

            String title = availabilityList.get(i).getActivity_count();
            holder.iconTitleID.setText(Utilities.CapitalText(title));

            holder.imageViewIcon.setOnClickListener(v -> {

                Log.e("clicked==>","");
                String s = availabilityList.get(i).getActivity_name();
                String[] parts = s.split("\\("); // escape .
                String part1 = parts[0];
                Intent intent = new Intent(getContext(), NewLeadsDataActivity.class);
                //intent.putExtra("URL", "getactivitydata");
                intent.putExtra("ACTIVITY_ID", availabilityList.get(i).getActivity_id());
                intent.putExtra("ACTIVITY_TITLE", "" + part1);
                intent.putExtra(AppConstants.VIEW_TYPE, "ACTIVITIES");
                intent.putExtra("TITLE", "" + part1);
                intent.putExtra("MENU_ID", "" + VALUE_MENU_ID);
                intent.putExtra("MENU_TITLE", "" + VALUE_ACTIVITY_TITLE);
                getContext().startActivity(intent);
            });

           /* holder.imageViewIcon.setOnClickListener(v -> {

                Log.e("clicked==>","");
                String s = availabilityList.get(i).getActivity_name();
                String[] parts = s.split("\\("); // escape .
                String part1 = parts[0];
                Intent intent = new Intent(getContext(), LeadsViewActivity.class);
                intent.putExtra("URL", "getactivitydata");
                intent.putExtra("ACTIVITY_ID", "" + availabilityList.get(i).getActivity_id());
                intent.putExtra("ACTIVITY_TITLE", "" + part1);
                intent.putExtra("MENU_ID", "" + VALUE_MENU_ID);
                intent.putExtra("MENU_TITLE", "" + VALUE_ACTIVITY_TITLE);
                getContext().startActivity(intent);

            });*/

        }

        @Override
        public int getItemCount() {
            return availabilityList.size();
        }

        public class PreSalesVH extends RecyclerView.ViewHolder {

            TextView textID;
            CardView clickID;
            LinearLayout imageViewIcon;
            TextView iconTitleID;

            public PreSalesVH(@NonNull View itemView) {
                super(itemView);
                textID = (TextView) itemView.findViewById(R.id.textViewName);
                clickID = (CardView)itemView.findViewById(R.id.clickID);
                imageViewIcon = (LinearLayout)itemView.findViewById(R.id.imageViewIcon);
                iconTitleID = (TextView)itemView.findViewById(R.id.iconTitleID);
            }
        }
    }


    public class MainDisplayResponse {

        private String activity_id;
        private String activity_name;
        private String color_one;
        private String color_two;
        private String activity_count;


        public MainDisplayResponse(String activity_id, String activity_name,String activity_count, String color_one, String color_two) {
            this.activity_id = activity_id;
            this.activity_name = activity_name;

            this.activity_count = activity_count;
            this.color_one = color_one;
            this.color_two = color_two;
        }

        public String getColor_one() {
            return color_one;
        }

        public void setColor_one(String color_one) {
            this.color_one = color_one;
        }

        public String getColor_two() {
            return color_two;
        }

        public void setColor_two(String color_two) {
            this.color_two = color_two;
        }

        public String getActivity_count() {
            return activity_count;
        }

        public void setActivity_count(String activity_count) {
            this.activity_count = activity_count;
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
    }
}
