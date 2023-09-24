package crm.tranquil_sales_steer.ui.adapters;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.DisableResponse;


/**
 * Created by venkei on 19-Mar-19.
 */
public class DeadReasonsAdapter extends ArrayAdapter<DisableResponse> {

    ArrayList<DisableResponse> sourceResponseList;

    public DeadReasonsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<DisableResponse> sourceResponseList) {
        super(context, resource, sourceResponseList);
        this.sourceResponseList = sourceResponseList;
    }

    @Override
    public int getCount() {

        try {
            if (sourceResponseList!=null){
                return sourceResponseList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }




    public class ViewHolder {
        public TextView spinnerText;

        public ViewHolder(View view) {
            spinnerText =  view.findViewById(R.id.spinnerText);
        }
    }

    public class ParentViewHolder {
        public TextView spinnerText;
        private ImageView dropdown;

        public ParentViewHolder(View view) {
            spinnerText = view.findViewById(R.id.spinnerItems);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        DisableResponse category = getItem(position);
        DeadReasonsAdapter.ParentViewHolder parentViewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_view, null);
            parentViewHolder = new DeadReasonsAdapter.ParentViewHolder(view);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (DeadReasonsAdapter.ParentViewHolder) view.getTag();
        }
        parentViewHolder.spinnerText.setText(TextUtils.isEmpty(category.getDead_reason()) ? "" : category.getDead_reason());
        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        DisableResponse category = getItem(position);
        DeadReasonsAdapter.ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_list_items, null);
            holder = new DeadReasonsAdapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (DeadReasonsAdapter.ViewHolder) view.getTag();
        }
        holder.spinnerText.setText(TextUtils.isEmpty(category.getDead_reason()) ? "" : category.getDead_reason());
        return view;
    }
}

