package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.SourceResponse;

/**
 * Created by venkei on 14-Aug-19.
 */
public class SourceNewAdapter extends BaseAdapter implements Filterable {

    private ArrayList<SourceResponse> _Contacts;
    private Activity context;
    private LayoutInflater inflater;
    private ValueFilter valueFilter;
    private ArrayList<SourceResponse> mStringFilterList;

    public SourceNewAdapter(Activity context, ArrayList<SourceResponse> _Contacts) {
        super();
        this.context = context;
        this._Contacts = _Contacts;
        mStringFilterList = _Contacts;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    @Override
    public int getCount() {
        return _Contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return _Contacts.get(position).getSource_name();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        TextView tname;
        RelativeLayout spinnerBgColor;
        LinearLayout dropDownListItem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
                "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
                "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
                "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
                "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
                "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_spinner_list_items, null);

            holder.tname = convertView.findViewById(R.id.spinnerText);
            holder.dropDownListItem = convertView.findViewById(R.id.dropDownListItem);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.tname.setText(_Contacts.get(position).getSource_name());
        holder.spinnerBgColor = convertView.findViewById(R.id.spinnerBgColor);
        GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
        bgShape.setColor(Color.parseColor(mColors[position % 40]));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }

    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<SourceResponse> filterList = new ArrayList<SourceResponse>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getSource_name().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        SourceResponse contacts = new SourceResponse();
                        contacts.setSource_name(mStringFilterList.get(i).getSource_name());
                        contacts.setSource_id(mStringFilterList.get(i).getSource_id());
                        filterList.add(contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            _Contacts = (ArrayList<SourceResponse>) results.values;
            notifyDataSetChanged();
        }
    }
}