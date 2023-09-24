package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.KnowlarityCallsAdapter;
import crm.tranquil_sales_steer.ui.fragments.AutoFollowUpFragment;
import crm.tranquil_sales_steer.ui.fragments.CampaignCallsFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadHistoryFragment;
import crm.tranquil_sales_steer.ui.fragments.LeadProfileFragment;
import crm.tranquil_sales_steer.ui.models.CampaignCallResponse;
import crm.tranquil_sales_steer.ui.models.KnowlarityCallsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowlarityCalls extends AppCompatActivity {

    RelativeLayout backRLID;


    TextView headerTittleTVID;

    KnowlarityCallsAdapter adapter;
    ArrayList<KnowlarityCallsResponse> knowlarityCallsResponses = new ArrayList<>();

    String userID,callTypeStr;
    RelativeLayout callRLID;

    public static boolean isClickable = true;

    CampaignCallResponse campaignResponse;
    TabLayout tabs;
    FrameLayout campaignViewPager;

    RelativeLayout campaignsVisibleRLID,campaignsGoneRLID,followUpVisibleRLID,followUpGoneRLID;
    Fragment fragment;

    @SuppressLint({"SetTextI18n", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowlarity_calls);
        Utilities.setStatusBarGradiant(this);

        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        backRLID = findViewById(R.id.backRLID);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                callTypeStr = bundle.getString("CALL_TYPE");
            }
        }




        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        callRLID = findViewById(R.id.callRLID);
        tabs = findViewById(R.id.tabs);
        campaignViewPager = findViewById(R.id.campaignViewPager);

        campaignsVisibleRLID = findViewById(R.id.campaignsVisibleRLID);
        campaignsGoneRLID = findViewById(R.id.campaignsGoneRLID);
        followUpVisibleRLID = findViewById(R.id.followUpVisibleRLID);
        followUpGoneRLID = findViewById(R.id.followUpGoneRLID);

        callRLID.setVisibility(View.VISIBLE);




        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);

        });

        headerTittleTVID.setText("Campaign Calls");

        fragment = new CampaignCallsFragment();
        loadFragment(fragment);

        if (callTypeStr != null && callTypeStr.equalsIgnoreCase("CAMPAIGN_CALL")){

            fragment = new CampaignCallsFragment();
            loadFragment(fragment);

        }else if (callTypeStr != null && callTypeStr.equalsIgnoreCase("AUTO_FOLLOWUP")){

            campaignsGoneRLID.setVisibility(View.VISIBLE);
            followUpGoneRLID.setVisibility(View.GONE);
            campaignsVisibleRLID.setVisibility(View.GONE);
            followUpVisibleRLID.setVisibility(View.VISIBLE);

            fragment = new AutoFollowUpFragment();
            loadFragment(fragment);

        }


        campaignsVisibleRLID.setOnClickListener(v -> {
            campaignsGoneRLID.setVisibility(View.GONE);
            followUpGoneRLID.setVisibility(View.VISIBLE);
            campaignsVisibleRLID.setVisibility(View.VISIBLE);
            followUpVisibleRLID.setVisibility(View.GONE);
            fragment = new CampaignCallsFragment();
            loadFragment(fragment);


        });

        campaignsGoneRLID.setOnClickListener(v -> {
            campaignsGoneRLID.setVisibility(View.GONE);
            followUpGoneRLID.setVisibility(View.VISIBLE);
            campaignsVisibleRLID.setVisibility(View.VISIBLE);
            followUpVisibleRLID.setVisibility(View.GONE);

            fragment = new CampaignCallsFragment();
            loadFragment(fragment);


        });

        followUpVisibleRLID.setOnClickListener(v -> {
            campaignsGoneRLID.setVisibility(View.VISIBLE);
            followUpGoneRLID.setVisibility(View.GONE);
            campaignsVisibleRLID.setVisibility(View.GONE);
            followUpVisibleRLID.setVisibility(View.VISIBLE);

            fragment = new AutoFollowUpFragment();
            loadFragment(fragment);


        });

        followUpGoneRLID.setOnClickListener(v -> {
            campaignsGoneRLID.setVisibility(View.VISIBLE);
            followUpGoneRLID.setVisibility(View.GONE);
            campaignsVisibleRLID.setVisibility(View.GONE);
            followUpVisibleRLID.setVisibility(View.VISIBLE);

            fragment = new AutoFollowUpFragment();
            loadFragment(fragment);


        });

        callRLID.setOnClickListener(v -> {

            getCampaignCall();

            Log.e("Clicked==>","" + "Clicked");
        });

    }

    private void loadFragment(final Fragment fragment) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.campaignViewPager, fragment, "")
                        .commit();
                //.commitAllowingStateLoss();
            }
        }, 30);
    }

    private void getCampaignCall() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<CampaignCallResponse> call = apiInterface.getCampaignCall(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<CampaignCallResponse>() {
            @Override
            public void onResponse(Call<CampaignCallResponse> call, Response<CampaignCallResponse> response) {

                if (response.body() != null && response.code() == 200){

                    campaignResponse = response.body();

                    if (campaignResponse.getStatus().equals(true)) {

                        Toast.makeText(KnowlarityCalls.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(KnowlarityCalls.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                    }

                }else {

                    Toast.makeText(KnowlarityCalls.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CampaignCallResponse> call, Throwable t) {

                Log.e("Status==>","" + "Failed");

                Toast.makeText(KnowlarityCalls.this, campaignResponse.getMsg(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}