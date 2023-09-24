package crm.tranquil_sales_steer.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.CommunicationsServices;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.CallCompleteActivity;
import crm.tranquil_sales_steer.ui.adapters.AutoFollowupAdapter;
import crm.tranquil_sales_steer.ui.models.AutoFollowupResponse;
import crm.tranquil_sales_steer.ui.models.CampaignCallResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AutoFollowUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutoFollowUpFragment extends Fragment implements AutoFollowupAdapter.KnowlarityCallClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AutoFollowUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CampaignCallsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AutoFollowUpFragment newInstance(String param1, String param2) {
        AutoFollowUpFragment fragment = new AutoFollowUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ProgressBar pb;
    AppCompatTextView noDataTVID;
    LinearLayoutManager linearLayoutManager;
    AutoFollowupAdapter adapter;
    ArrayList<AutoFollowupResponse> knowlarityCallsResponses = new ArrayList<>();
    RecyclerView knowlarityCallRVID;
    String userID;
    FloatingActionButton refreshFab;
    public static boolean isClickable = true;

    CampaignCallResponse campaignResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaign_calls, container, false);


        userID = MySharedPreferences.getPreferences(getActivity(), "" + AppConstants.SharedPreferenceValues.USER_ID);

        pb = view.findViewById(R.id.pb);
        noDataTVID = view.findViewById(R.id.noDataTVID);
        knowlarityCallRVID = view.findViewById(R.id.knowlarityCallRVID);
        refreshFab = view.findViewById(R.id.refreshFab);

        if (Utilities.isNetworkAvailable(getActivity())){
            loadKnowlarityCalls();
        }else {
            Toast.makeText(getActivity(), "Please check your network", Toast.LENGTH_SHORT).show();
        }


        refreshFab.setOnClickListener(v -> {


            loadKnowlarityCalls();
        });

        return view;
    }

    private void loadKnowlarityCalls() {
        pb.setVisibility(View.VISIBLE);
        knowlarityCallRVID.setVisibility(View.GONE);
        ApiInterface apiInterface=ApiClient.getClientNew((Activity) getContext().getApplicationContext()).create(ApiInterface.class);
        Call<ArrayList<AutoFollowupResponse>> call = apiInterface.getAutoFollowups(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AutoFollowupResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AutoFollowupResponse>> call, Response<ArrayList<AutoFollowupResponse>> response) {

                pb.setVisibility(View.GONE);
                knowlarityCallRVID.setVisibility(View.VISIBLE);
                refreshFab.setVisibility(View.VISIBLE);


                if (response.body() != null && response.code() == 200){

                    knowlarityCallsResponses = response.body();

                    if (knowlarityCallsResponses.size() > 0){

                        linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
                        knowlarityCallRVID.setLayoutManager(linearLayoutManager);

                        adapter = new AutoFollowupAdapter(getActivity(),knowlarityCallsResponses, AutoFollowUpFragment.this);
                        knowlarityCallRVID.setAdapter(adapter);
                    }else {
                        pb.setVisibility(View.GONE);
                        knowlarityCallRVID.setVisibility(View.GONE);
                        noDataTVID.setVisibility(View.VISIBLE);
                    }
                }else {
                    pb.setVisibility(View.GONE);
                    knowlarityCallRVID.setVisibility(View.GONE);
                    noDataTVID.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AutoFollowupResponse>> call, Throwable t) {
                pb.setVisibility(View.GONE);
                knowlarityCallRVID.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getCampaignCall() {

           ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CampaignCallResponse> call = apiInterface.getCampaignCall(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<CampaignCallResponse>() {
            @Override
            public void onResponse(Call<CampaignCallResponse> call, Response<CampaignCallResponse> response) {

                if (response.body() != null && response.code() == 200){

                    campaignResponse = response.body();

                    if (campaignResponse.getStatus().equals(true)) {

                        Toast.makeText(getActivity(), campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(getActivity(), campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }else {

                    Toast.makeText(getActivity(), campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CampaignCallResponse> call, Throwable t) {

                Log.e("Status==>","" + "Failed");

                Toast.makeText(getActivity(), campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        loadKnowlarityCalls();
    }

    @Override
    public void onKnowlarityCallItemClick(AutoFollowupResponse response, View v, int pos, AutoFollowupAdapter.KnowlarityCallVH holder) {

        switch (v.getId()) {

            case R.id.convertRLID:

                CommunicationsServices.InsertCommunication(getActivity(), "1", response.getLead_id(), userID, "", "");


                Intent intent = new Intent(getActivity(), CallCompleteActivity.class);
                intent.putExtra("ACTIVITY_NAME",response.getActivity_name());
                intent.putExtra("ACTIVITY_ID",response.getActivity_id());
                intent.putExtra("CUSTOMER_NAME",response.getCustomer_name());
                intent.putExtra("CUSTOMER_MOBILE", response.getMobile_number());
                intent.putExtra("SCREEN_FROM","AUTO_FOLLOWUP");
                intent.putExtra("LEAD_ID",response.getLead_id());
                startActivity(intent);

                break;
        }
    }
}