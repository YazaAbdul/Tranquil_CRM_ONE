package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.activities.site_visits.SiteVisitFinalActivity;
import crm.tranquil_sales_steer.ui.activities.site_visits.SiteVisitUpdateActivity;
import crm.tranquil_sales_steer.ui.models.SearchResponse;

/**
 * Created by venkei on 05-Jun-18.
 */

public class SearchAdapter extends BaseAdapter {

    private ArrayList<SearchResponse> searchResponses = new ArrayList<>();
    private Activity activity;
    private LayoutInflater inflater;

    public SearchAdapter(Activity activity, ArrayList<SearchResponse> searchResponses) {
        this.activity = activity;
        this.searchResponses = searchResponses;
        inflater = LayoutInflater.from(activity);
    }


    @Override
    public int getCount() {
        return searchResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.site_visit_serach_list_item, null);

        TextView name = (TextView) view.findViewById(R.id.nameTVID);
        TextView number = (TextView) view.findViewById(R.id.numberTVID);
        Button updateBtn = (Button) view.findViewById(R.id.updateBtn);

        name.setText(searchResponses.get(position).getCustomer_name());
        number.setText(searchResponses.get(position).getMobile());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String statusActivity = MySharedPreferences.getPreferences(activity, "BUTTON_STATUS");
                String siteID= MySharedPreferences.getPreferences(activity,"SITE_ID");
                String statusActivity1= MySharedPreferences.getPreferences(activity,"BUTTON_STATUS_1");
/*
                Intent intent12 = new Intent(activity, SiteVisitUpdateActivity.class);
                intent12.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                intent12.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                intent12.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                activity.startActivity(intent12);
                activity.finish();*/

                MySharedPreferences.setPreference(activity,"SITE_ID",""+siteID);
                if (statusActivity.equals("2")) {
                    Intent intent1 = new Intent(activity, SiteVisitUpdateActivity.class);
                    intent1.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                    intent1.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                    intent1.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                    intent1.putExtra("SITE_ID_1",siteID);
                    activity.startActivity(intent1);
                    activity.finish();
                } else if (statusActivity.equals("0")){
                    Intent intent = new Intent(activity, SiteVisitFinalActivity.class);
                    intent.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                    intent.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                    intent.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                    activity.startActivity(intent);
                    activity.finish();
                } else if (statusActivity.equals("1")){
                    Intent intent = new Intent(activity, SiteVisitFinalActivity.class);
                    intent.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                    intent.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                    intent.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                    activity.startActivity(intent);
                    activity.finish();
                } else if (statusActivity.equals("3")){
                    Intent intent = new Intent(activity, SiteVisitFinalActivity.class);
                    intent.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                    intent.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                    intent.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                    intent.putExtra("SITE_ID_1",siteID);
                    activity.startActivity(intent);
                    activity.finish();
                }else if (statusActivity.equals("")){
                    Intent intent = new Intent(activity, SiteVisitFinalActivity.class);
                    intent.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                    intent.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                    intent.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                    activity.startActivity(intent);
                    activity.finish();
                }
             /*   if (statusActivity1.equals("3")){
                    Intent intent1 = new Intent(activity, SiteVisitUpdateActivity.class);
                    intent1.putExtra("CUSTOMER_NAME", searchResponses.get(position).getCustomer_name());
                    intent1.putExtra("CUSTOMER_NUMBER", searchResponses.get(position).getMobile());
                    intent1.putExtra("CUSTOMER_ID", searchResponses.get(position).getCustomer_id());
                    intent1.putExtra("SITE_ID_1",siteID);
                    activity.startActivity(intent1);
                    activity.finish();
                }*/

                //activity.finish();
            }
        });

        return view;
    }
}
