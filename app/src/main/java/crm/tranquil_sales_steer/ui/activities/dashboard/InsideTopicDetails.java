package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.ui.models.WebViewModel;

public class InsideTopicDetails extends AppCompatActivity {
    RelativeLayout backRLID;
    TextView tv;
    WebView webView;
    ProgressBar pb;
    String topicid;
    int mynum;
    ArrayList<WebViewModel> webViewModels=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_topic_details);
        backRLID = findViewById(R.id.backRLID);
        pb=findViewById(R.id.pb);
        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });
        webView=findViewById(R.id.webweview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                topicid=bundle.getString("TOPICID");
                webView.loadUrl("https://www.tranquilcrm.in/ss/mobileapp/getdescriptionbyid?topic_id="+topicid);


            }
        }
    }
}