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
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;


/**
 * Created by venkei on 04-Mar-19.
 */
public class ActivitiesAdapterNew extends ArrayAdapter<ActivityMainResponse> {

    List<ActivityMainResponse> activitiesResponse;

    public ActivitiesAdapterNew(@NonNull Context context, @LayoutRes int resource, @NonNull List<ActivityMainResponse> activitiesResponse) {
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
        ActivityMainResponse category = getItem(position);
        ParentViewHolder parentViewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_view, null);
            parentViewHolder = new ParentViewHolder(view);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) view.getTag();
        }
        parentViewHolder.spinnerItems.setText(TextUtils.isEmpty(category.getActivity_name()) ? "" : category.getActivity_name());
        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ActivityMainResponse category = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_list_items_new, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.spinnerText.setText(TextUtils.isEmpty(category.getActivity_name()) ? "" : category.getActivity_name());
        return view;
    }
}


