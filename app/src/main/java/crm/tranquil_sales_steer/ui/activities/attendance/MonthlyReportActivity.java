package crm.tranquil_sales_steer.ui.activities.attendance;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;

public class MonthlyReportActivity extends AppCompatActivity {


    ListView employeeLVID;
    ArrayList<MonthsReportList> employeeLists = new ArrayList<>();
    MonthReportAdapter adapter;
    String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                titleStr = bundle.getString("NAME");
            }
        }

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText(titleStr);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });

        employeeLVID = findViewById(R.id.employeeLVID);
        employeeLists.clear();
        employeeLists.add(new MonthsReportList("1-02-2019", "9:30 Am", "7:00 Pm"));
        employeeLists.add(new MonthsReportList("2-02-2019", "9:30 Am", "7:00 Pm"));
        employeeLists.add(new MonthsReportList("3-02-2019", "9:30 Am", "7:00 Pm"));
        employeeLists.add(new MonthsReportList("4-02-2019", "9:30 Am", "7:00 Pm"));
        employeeLists.add(new MonthsReportList("5-02-2019", "9:30 Am", "7:00 Pm"));
        employeeLists.add(new MonthsReportList("6-02-2019", "9:30 Am", "7:00 Pm"));

        adapter = new MonthReportAdapter(this, employeeLists);
        employeeLVID.setAdapter(adapter);
    }

    public class MonthsReportList {

        private String date;
        private String inTime;
        private String outTime;

        public MonthsReportList(String date, String inTime, String outTime) {
            this.date = date;
            this.inTime = inTime;
            this.outTime = outTime;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }

        public String getOutTime() {
            return outTime;
        }

        public void setOutTime(String outTime) {
            this.outTime = outTime;
        }
    }

    public class MonthReportAdapter extends BaseAdapter {

        private ArrayList<MonthsReportList> employeeLists = new ArrayList<>();
        private Activity activity;
        private LayoutInflater inflater;

        public MonthReportAdapter(Activity activity, ArrayList<MonthsReportList> employeeLists) {
            this.activity = activity;
            this.employeeLists = employeeLists;
            inflater = LayoutInflater.from(activity);
        }

        @Override
        public int getCount() {
            return employeeLists.size();
        }

        @Override
        public Object getItem(int i) {
            return employeeLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            View view = convertView;
            if (convertView == null)
                view = inflater.inflate(R.layout.attendance_report_list, null);

            TextView dateTVID = view.findViewById(R.id.dateTVID);
            TextView inTimeTVID = view.findViewById(R.id.inTimeTVID);
            TextView outTimeTVID = view.findViewById(R.id.outTimeTVID);

            dateTVID.setText(employeeLists.get(i).getDate());
            inTimeTVID.setText(employeeLists.get(i).getInTime());
            outTimeTVID.setText(employeeLists.get(i).getOutTime());

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }
}
