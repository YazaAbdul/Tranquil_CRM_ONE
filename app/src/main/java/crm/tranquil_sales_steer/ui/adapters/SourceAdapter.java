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

import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.SourceResponse;

/**
 * Created by venkei on 06-Jul-19.
 */
public class SourceAdapter  extends ArrayAdapter<SourceResponse> {

    List<SourceResponse> doubtsClassesResponces;

    public SourceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<SourceResponse> doubtsClassesResponces) {
        super(context, resource, doubtsClassesResponces);
        this.doubtsClassesResponces = doubtsClassesResponces;
    }

    @Override
    public int getCount() {

        try {
            if (doubtsClassesResponces!=null){
                return doubtsClassesResponces.size();
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
        SourceResponse category = getItem(position);
        ParentViewHolder parentViewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_view, null);
            parentViewHolder = new ParentViewHolder(view);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) view.getTag();
        }
        parentViewHolder.spinnerText.setText(TextUtils.isEmpty(category.getSource_name()) ? "" : category.getSource_name());
        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        SourceResponse category = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_list_items, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.spinnerText.setText(TextUtils.isEmpty(category.getSource_name()) ? "" : category.getSource_name());
        return view;
    }
}

