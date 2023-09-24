package crm.tranquil_sales_steer.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.EmployeeResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeResponse;


/**
 * Created by venkei on 04-Mar-19.
 */
public class EmployeeAdapter extends ArrayAdapter<EmployeeResponse> {

    List<EmployeeResponse> activitiesResponse;

    public EmployeeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<EmployeeResponse> activitiesResponse) {
        super(context, resource, activitiesResponse);
        this.activitiesResponse = activitiesResponse;
    }

    @Override
    public int getCount() {

        try {
            if (activitiesResponse != null) {
                return activitiesResponse.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public class ViewHolder {
        public TextView spinnerText;

        public ViewHolder(View view) {
            spinnerText = view.findViewById(R.id.spinnerText);
        }
    }

    public class ParentViewHolder {
        public TextView spinnerItems;
        private ImageView dropdown;

        public ParentViewHolder(View view) {
            spinnerItems = view.findViewById(R.id.spinnerItems);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        EmployeeResponse category = getItem(position);
        ParentViewHolder parentViewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_view, null);
            parentViewHolder = new ParentViewHolder(view);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) view.getTag();
        }
        parentViewHolder.spinnerItems.setText(TextUtils.isEmpty(category.getUser_name()) ? "" : category.getUser_name());
        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        EmployeeResponse category = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_list_items_new, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.spinnerText.setText(TextUtils.isEmpty(category.getUser_name()) ? "" : category.getUser_name());
        return view;
    }
}


