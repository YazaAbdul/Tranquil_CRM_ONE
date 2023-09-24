package crm.tranquil_sales_steer.ui.activities.attendance;

import android.app.Activity;
import android.content.Intent;
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

public class MonthsActivity extends AppCompatActivity {

    ListView employeeLVID;
    ArrayList<MonthsList> employeeLists = new ArrayList<>();
    MonthsAdapter adapter;
    String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        if (getIntent()!=null){
            Bundle bundle=getIntent().getExtras();
            if (bundle!=null){
                titleStr=bundle.getString("NAME");
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
        employeeLists.add(new MonthsList("August"));
        employeeLists.add(new MonthsList("July"));
        employeeLists.add(new MonthsList("June"));
        employeeLists.add(new MonthsList("May"));
        employeeLists.add(new MonthsList("April"));
        employeeLists.add(new MonthsList("MArch"));

        adapter = new MonthsAdapter(MonthsActivity.this, employeeLists);
        employeeLVID.setAdapter(adapter);
    }

    public class MonthsList {

        private String name;

        public MonthsList(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class MonthsAdapter extends BaseAdapter {

        private ArrayList<MonthsList> employeeLists = new ArrayList<>();
        private Activity activity;
        private LayoutInflater inflater;

        public MonthsAdapter(Activity activity, ArrayList<MonthsList> employeeLists) {
            this.activity = activity;
            this.employeeLists = employeeLists;
            inflater=LayoutInflater.from(activity);
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
                view = inflater.inflate(R.layout.search_suggestions_list_item, null);

            TextView searchLeadNameTVID = view.findViewById(R.id.searchLeadNameTVID);
            searchLeadNameTVID.setText(employeeLists.get(i).getName());

            RelativeLayout newMainLeadView=view.findViewById(R.id.newMainLeadView);
            newMainLeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(activity, MonthlyReportActivity.class);
                    intent.putExtra("NAME",""+employeeLists.get(i).getName());
                    startActivity(intent);
                }
            });

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
