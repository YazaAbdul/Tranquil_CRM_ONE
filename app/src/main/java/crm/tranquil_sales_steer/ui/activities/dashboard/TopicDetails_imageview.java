package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.ui.fragments.FragmentCreatives;
import crm.tranquil_sales_steer.ui.fragments.FragmentCreativesWithOut;

public class TopicDetails_imageview extends AppCompatActivity {
    RelativeLayout backRLID;
    int start = 0;
    boolean isLoading;
    TextView headerTittleTVID;
    RecyclerView creativesRVID;
    boolean isLastPage;
    Fragment fragment;
    ProgressBar pb;
    RelativeLayout createiveRVDetails;
    SwitchCompat switchUser;
    AppCompatTextView noDataTVID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details_imageview);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Creatives");
        creativesRVID = findViewById(R.id.creativesRVID);
        pb = findViewById(R.id.pb);
        switchUser = findViewById(R.id.switchUser);
        noDataTVID = findViewById(R.id.noDataTVID);
//        createiveRVDetails = findViewById(R.id.createiveRVDetails);
//        createiveRVDetails.setVisibility(View.GONE);
        fragment = new FragmentCreatives();
        loadFragment(fragment);
        backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);

        });



        switchUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    // createiveRVDetails.setVisibility(View.GONE);
                    fragment = new FragmentCreatives();
                    loadFragment(fragment);
                }else {
                    fragment = new FragmentCreativesWithOut();
                    loadFragment(fragment);
                }


               /* if (isChecked = true){

                    fragment = new FragmentCreatives();
                    loadFragment(fragment);

                }else if (isChecked = false){

                    fragment = new FragmentCreativesWithOut();
                    loadFragment(fragment);
                }*/


            }
        });



    }

    private void loadFragment(final Fragment fragment) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.CreativesViewPager, fragment, "")
                        .commit();
                //.commitAllowingStateLoss();
            }
        }, 30);
    }
}