package crm.tranquil_sales_steer.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.ActivityMainResponse;


/**
 * Created by venkei on 04-Mar-19.
 */
public class ActivitiesAdapter extends ArrayAdapter<ActivityMainResponse> {

    List<ActivityMainResponse> doubtsClassesResponces;


    String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
            "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
            "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
            "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
            "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
            "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


    public ActivitiesAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ActivityMainResponse> doubtsClassesResponces) {
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
        public RelativeLayout spinnerBgColor;

        public ViewHolder(View view) {
            spinnerText =  view.findViewById(R.id.spinnerText);
            spinnerBgColor=view.findViewById(R.id.spinnerBgColor);
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
        parentViewHolder.spinnerText.setText(TextUtils.isEmpty(category.getActivity_name()) ? "" : category.getActivity_name());
        return view;
    }


    @Override
    public View getDropDownView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ActivityMainResponse category = getItem(position);
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_spinner_list_items, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GradientDrawable bgShape = (GradientDrawable) holder.spinnerBgColor.getBackground();
        bgShape.setColor(Color.parseColor(mColors[position % 40]));
        //holder.spinnerBgColor.setBackgroundColor(Color.parseColor(mColors[position % 40]));
        holder.spinnerText.setText(TextUtils.isEmpty(category.getActivity_name()) ? "" : category.getActivity_name());
        return view;
    }
}


