package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.dashboard.AvailabilityPlotsActivity;
import crm.tranquil_sales_steer.ui.models.AvailableProjectsResponse;

public class AvailabilityProjectsAdapter extends RecyclerView.Adapter<AvailabilityProjectsAdapter.AvailabilityProjectsVH> {

    ArrayList<AvailableProjectsResponse> availableProjectsResponses = new ArrayList<>();
    Activity activity;
    String available,sold,blocked,total,projectID;

    public AvailabilityProjectsAdapter(ArrayList<AvailableProjectsResponse> availableProjectsResponses, Activity activity) {
        this.availableProjectsResponses = availableProjectsResponses;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AvailabilityProjectsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AvailabilityProjectsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.availability_projects_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AvailabilityProjectsVH holder, int position) {

        holder.availabilityProjectsTVID.setText(availableProjectsResponses.get(position).getRequirement_name());

        projectID = availableProjectsResponses.get(position).getRequirement_id();
        available = availableProjectsResponses.get(position).getAvailable();
        sold = availableProjectsResponses.get(position).getSold();
        blocked = availableProjectsResponses.get(position).getBlocked();
        total = availableProjectsResponses.get(position).getTotal();

        holder.availableProjectsRLID.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AvailabilityPlotsActivity.class);
            intent.putExtra("PROJECT_ID",projectID);
            intent.putExtra("AVAILABLE",projectID);
            intent.putExtra("SOLD",projectID);
            intent.putExtra("BLOCKED",projectID);
            intent.putExtra("TOTAL",projectID);
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return availableProjectsResponses.size();
    }

    public class AvailabilityProjectsVH extends RecyclerView.ViewHolder{

        RelativeLayout availableProjectsRLID;
        AppCompatTextView availabilityProjectsTVID;

        public AvailabilityProjectsVH(@NonNull View itemView) {
            super(itemView);

            availableProjectsRLID = itemView.findViewById(R.id.availableProjectsRLID);
            availabilityProjectsTVID = itemView.findViewById(R.id.availabilityProjectsTVID);
        }
    }
}
