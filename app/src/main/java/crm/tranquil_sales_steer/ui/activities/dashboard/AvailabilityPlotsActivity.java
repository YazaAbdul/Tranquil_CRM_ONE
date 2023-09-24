package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;

public class AvailabilityPlotsActivity extends AppCompatActivity {

    RelativeLayout backRLID;
    LinearLayout plotDetailsLLID;
    WebView availablePlotsWVID;
    ProgressBar pb;
    AppCompatImageView oopsIVD;
    String available,sold,blocked,total,projectID,userID;
    AppCompatTextView totalTVID,soldTVID,availableTVID,blockedTVID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_plots);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                available = bundle.getString("AVAILABLE");
                sold = bundle.getString("SOLD");
                blocked = bundle.getString("BLOCKED");
                total = bundle.getString("TOTAL");
                projectID = bundle.getString("PROJECT_ID");
            }
        }

        userID = MySharedPreferences.getPreferences(AvailabilityPlotsActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);

        backRLID = findViewById(R.id.backRLID);
        plotDetailsLLID = findViewById(R.id.plotDetailsLLID);
        availablePlotsWVID = findViewById(R.id.availablePlotsWVID);
        pb = findViewById(R.id.pb);
        oopsIVD = findViewById(R.id.oopsIVD);
        totalTVID = findViewById(R.id.totalTVID);
        soldTVID = findViewById(R.id.soldTVID);
        availableTVID = findViewById(R.id.availableTVID);
        blockedTVID = findViewById(R.id.blockedTVID);

        totalTVID.setText(total);
        soldTVID.setText(sold);
        availableTVID.setText(available);
        blockedTVID.setText(blocked);

        plotDetailsLLID.setOnClickListener(v -> {
            Intent intent = new Intent(this,PlotDetailsActivity.class);
            startActivity(intent);
        });

        backRLID.setOnClickListener(v -> Utilities.finishAnimation(this));

        availablePlotsWVID.getSettings().setUseWideViewPort(true);
        availablePlotsWVID.getSettings().setLoadWithOverviewMode(true);

        availablePlotsWVID.getSettings().setBuiltInZoomControls(true);
        availablePlotsWVID.getSettings().setSupportZoom(true);

        if (Utilities.isNetworkAvailable(this)){
            renderWebPage("https://tranquilcrmone.com/mobileapp/getavailableunits?" + "project_id=" + projectID + "&user_id=" + userID);
            pb.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void renderWebPage(String urlToRender) {
        Log.e("url==>",urlToRender);
        availablePlotsWVID.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                pb.setVisibility(View.GONE);
            }
        });

        availablePlotsWVID.setWebChromeClient(new WebChromeClient() {

        });

        availablePlotsWVID.getSettings().setJavaScriptEnabled(true);
        availablePlotsWVID.loadUrl(urlToRender);
    }
}