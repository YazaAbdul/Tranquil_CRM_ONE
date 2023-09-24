package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.Utilities;

public class PlotDetailsActivity extends AppCompatActivity {

    RelativeLayout backRLID;
    AppCompatButton sendRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_details);

        backRLID = findViewById(R.id.backRLID);
        sendRequestBtn = findViewById(R.id.sendRequestBtn);

        backRLID.setOnClickListener(v -> Utilities.finishAnimation(this));

        sendRequestBtn.setOnClickListener(v -> {

            Intent intent = new Intent(this,SendBlockRequestActivity.class);
            startActivity(intent);

        });
    }
}