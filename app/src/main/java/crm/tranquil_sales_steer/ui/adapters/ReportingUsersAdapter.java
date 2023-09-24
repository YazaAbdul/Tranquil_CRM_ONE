package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.DashboardResponse;
import crm.tranquil_sales_steer.ui.models.ReportingUsersResponse;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReportingUsersAdapter extends RecyclerView.Adapter<ReportingUsersAdapter.ReportingUsersVH> {

    ArrayList<ReportingUsersResponse> reportingUsersResponses = new ArrayList<>();
    DashboardResponse dashboardResponses;
    Activity activity;
    ReportingUsersClickListener reportingUsersClickListener;

    public ReportingUsersAdapter(ArrayList<ReportingUsersResponse> reportingUsersResponses, Activity activity, ReportingUsersClickListener reportingUsersClickListener,DashboardResponse dashboardResponses) {
        this.reportingUsersResponses = reportingUsersResponses;
        this.activity = activity;
        this.reportingUsersClickListener = reportingUsersClickListener;
        this.dashboardResponses = dashboardResponses;
    }

    @NonNull
    @Override
    public ReportingUsersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportingUsersVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.reporting_users_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportingUsersVH holder, int position) {
        ReportingUsersVH itemVH=(ReportingUsersVH) holder;
        ReportingUsersResponse item = reportingUsersResponses.get(position);

        holder.userNameTVID.setText(item.getUser_name());
        holder.designationTVID.setText(item.getUser_role());
        Picasso.with(activity).load(item.getUser_profile_pic()).into(itemVH.profilePicID);

        if (dashboardResponses.getHref_status() != null && dashboardResponses.getHref_status().equalsIgnoreCase("true")){

            holder.viewRLID.setVisibility(View.VISIBLE);
        }else {
            holder.viewRLID.setVisibility(View.GONE);
        }

        holder.viewRLID.setOnClickListener(v -> reportingUsersClickListener.onReportingUsersItemClick(item,v,position,holder));

    }

    @Override
    public int getItemCount() {
        return reportingUsersResponses.size();
    }

    public class ReportingUsersVH extends RecyclerView.ViewHolder{

        CircleImageView profilePicID;
        AppCompatTextView userNameTVID,designationTVID;
        RelativeLayout viewRLID;

        public ReportingUsersVH(@NonNull View itemView) {
            super(itemView);

            profilePicID = itemView.findViewById(R.id.profilePicID);
            userNameTVID = itemView.findViewById(R.id.userNameTVID);
            designationTVID = itemView.findViewById(R.id.designationTVID);
            viewRLID = itemView.findViewById(R.id.viewRLID);
        }
    }

    public interface ReportingUsersClickListener{
        void onReportingUsersItemClick(ReportingUsersResponse response, View v, int pos, ReportingUsersAdapter.ReportingUsersVH holder);
    }
}
