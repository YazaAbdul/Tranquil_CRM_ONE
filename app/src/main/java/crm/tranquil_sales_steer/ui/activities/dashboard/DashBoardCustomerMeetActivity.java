package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;

public class DashBoardCustomerMeetActivity extends AppCompatActivity implements View.OnClickListener {

    String customerID, nameStr, emailStr, mobileStr;
    LinearLayout startActiveBtn, startDisActiveBtn, meetingActiveBtn, meetingDisActiveBtn, endActiveBtn, endDisActiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_meet);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                customerID = bundle.getString("CUSTOMER_ID");
                nameStr = bundle.getString("CUSTOMER_NAME");
                mobileStr = bundle.getString("CUSTOMER_MOBILE");
            }
        }

        AlertUtilities.startAnimation(this);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText(nameStr);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertUtilities.finishAnimation(DashBoardCustomerMeetActivity.this);
            }
        });

        startActiveBtn = findViewById(R.id.startActiveBtn);
        startDisActiveBtn = findViewById(R.id.startDisActiveBtn);
        meetingActiveBtn = findViewById(R.id.meetingActiveBtn);
        meetingDisActiveBtn = findViewById(R.id.meetingDisActiveBtn);
        endActiveBtn = findViewById(R.id.endActiveBtn);
        endDisActiveBtn = findViewById(R.id.endDisActiveBtn);


        startActiveBtn.setOnClickListener(this);
        startDisActiveBtn.setOnClickListener(this);
        meetingActiveBtn.setOnClickListener(this);
        meetingDisActiveBtn.setOnClickListener(this);
        endActiveBtn.setOnClickListener(this);
        endDisActiveBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertUtilities.finishAnimation(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.startActiveBtn:
                startActiveBtn.setVisibility(View.GONE);
                startDisActiveBtn.setVisibility(View.VISIBLE);
                meetingActiveBtn.setVisibility(View.VISIBLE);
                meetingDisActiveBtn.setVisibility(View.GONE);
                endActiveBtn.setVisibility(View.GONE);
                endDisActiveBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.meetingActiveBtn:
                startActiveBtn.setVisibility(View.GONE);
                startDisActiveBtn.setVisibility(View.VISIBLE);
                meetingActiveBtn.setVisibility(View.GONE);
                meetingDisActiveBtn.setVisibility(View.VISIBLE);
                endActiveBtn.setVisibility(View.VISIBLE);
                endDisActiveBtn.setVisibility(View.GONE);
                break;

            case R.id.endActiveBtn:
                startActiveBtn.setVisibility(View.VISIBLE);
                startDisActiveBtn.setVisibility(View.GONE);
                meetingActiveBtn.setVisibility(View.GONE);
                meetingDisActiveBtn.setVisibility(View.VISIBLE);
                endActiveBtn.setVisibility(View.GONE);
                endDisActiveBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.startDisActiveBtn:
                Toast.makeText(this, "Please completed previous point", Toast.LENGTH_SHORT).show();
                break;

            case R.id.meetingDisActiveBtn:
                Toast.makeText(this, "Please completed previous point", Toast.LENGTH_SHORT).show();
                break;

            case R.id.endDisActiveBtn:
                Toast.makeText(this, "Please completed previous point", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
