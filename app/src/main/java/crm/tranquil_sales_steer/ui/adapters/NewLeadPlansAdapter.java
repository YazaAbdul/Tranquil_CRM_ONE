package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.dashboard.NewLeadsDataActivity;
import crm.tranquil_sales_steer.ui.models.NewLeadPlansResponse;


public class NewLeadPlansAdapter extends RecyclerView.Adapter<NewLeadPlansAdapter.PlanesVH> {

    private ArrayList<NewLeadPlansResponse> activitiesArrayList = new ArrayList<>();
    Activity activity;
    private boolean selectedPosition = true;
    int row_index = -1;

    PlaneClickListener clickListener;


    public NewLeadPlansAdapter(Activity activity, ArrayList<NewLeadPlansResponse> activitiesArrayList, PlaneClickListener clickListener) {
        this.activity = activity;
        this.activitiesArrayList = activitiesArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NewLeadPlansAdapter.PlanesVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NewLeadPlansAdapter.PlanesVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activities_list_item, viewGroup, false));
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull NewLeadPlansAdapter.PlanesVH holder, @SuppressLint("RecyclerView") int i) {
        holder.activityNameTVID.setText(activitiesArrayList.get(i).getPlan_name() + " (" + activitiesArrayList.get(i).getCount() + ")");
        holder.activityRLID.setOnClickListener(v -> {

            if (NewLeadsDataActivity.isClickable) {
                row_index = i;
                notifyDataSetChanged();
                selectedPosition = false;
                clickListener.onPlanItemClicked(activitiesArrayList.get(i), 1);
            } else {
                notifyDataSetChanged();
            }

        });

        if (row_index == i) {
            holder.activityRLID.setBackground(activity.getResources().getDrawable(R.drawable.bg_sale_activity));
            holder.activityNameTVID.setTextColor(activity.getResources().getColor(R.color.logoColor));
        } else {
            holder.activityRLID.setBackground(activity.getResources().getDrawable(R.drawable.bg_sale_activity2));
            holder.activityNameTVID.setTextColor(activity.getResources().getColor(R.color.white));
        }

        if (i == 0) {
            if (selectedPosition) {
                clickListener.onPlanItemClicked(activitiesArrayList.get(0), 0);
                holder.activityRLID.setBackground(activity.getResources().getDrawable(R.drawable.bg_sale_activity));
                holder.activityNameTVID.setTextColor(activity.getResources().getColor(R.color.logoColor));
            }
        }
    }

    @Override
    public int getItemCount() {
        return activitiesArrayList.size();
    }

    public static class PlanesVH extends RecyclerView.ViewHolder {
        TextView activityNameTVID;
        RelativeLayout activityRLID;

        public PlanesVH(@NonNull View itemView) {
            super(itemView);
            activityNameTVID = itemView.findViewById(R.id.activityNameTVID);
            activityRLID = itemView.findViewById(R.id.activityRLID);
        }
    }


    public interface PlaneClickListener {
        void onPlanItemClicked(NewLeadPlansResponse plansResponse, int typeOfClick);
    }
}