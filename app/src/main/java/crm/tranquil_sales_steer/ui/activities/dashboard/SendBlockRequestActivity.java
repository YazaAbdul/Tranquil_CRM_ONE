package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;

public class SendBlockRequestActivity extends AppCompatActivity {

    RelativeLayout backRLID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_block_request);

        backRLID = findViewById(R.id.backRLID);

        backRLID.setOnClickListener(v -> Utilities.finishAnimation(this));
    }
}