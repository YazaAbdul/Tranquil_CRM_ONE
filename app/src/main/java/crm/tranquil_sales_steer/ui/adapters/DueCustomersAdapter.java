package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.dashboard.DueCustomersListActivity;
import crm.tranquil_sales_steer.ui.models.DueCustomersResponse;

public class DueCustomersAdapter extends RecyclerView.Adapter<DueCustomersAdapter.DueCustomersVH> {

    ArrayList<DueCustomersResponse> dueCustomersResponses = new ArrayList<>();
    Activity activity;

    public DueCustomersAdapter(ArrayList<DueCustomersResponse> dueCustomersResponses, Activity activity) {
        this.dueCustomersResponses = dueCustomersResponses;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DueCustomersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DueCustomersVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.due_customers_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DueCustomersVH holder, int position) {

        holder.soldTVID.setText("₹ "+Integer.toString(dueCustomersResponses.get(position).getSold()));
        holder.totalSaleTVID.setText("₹ "+Integer.toString(dueCustomersResponses.get(position).getTotal_sale_amt()));
        holder.receivedTVID.setText("₹ "+Integer.toString(dueCustomersResponses.get(position).getReceived()));
        holder.dueAmountTVID.setText("₹ "+Integer.toString(dueCustomersResponses.get(position).getDue_amount()));
        holder.projectNameTVID.setText(dueCustomersResponses.get(position).getProject_name());

        holder.dueCustomerLLID.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DueCustomersListActivity.class);
            intent.putExtra("PROJECT_ID",dueCustomersResponses.get(position).getProject_id());
            intent.putExtra("PROJECT_NAME",dueCustomersResponses.get(position).getProject_name());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dueCustomersResponses.size();
    }

    public class DueCustomersVH extends RecyclerView.ViewHolder{

        AppCompatTextView projectNameTVID,soldTVID,totalSaleTVID,receivedTVID,dueAmountTVID;
        LinearLayout dueCustomerLLID;
        public DueCustomersVH(@NonNull View itemView) {
            super(itemView);

            projectNameTVID = itemView.findViewById(R.id.projectNameTVID);
            soldTVID = itemView.findViewById(R.id.soldTVID);
            totalSaleTVID = itemView.findViewById(R.id.totalSaleTVID);
            receivedTVID = itemView.findViewById(R.id.receivedTVID);
            dueAmountTVID = itemView.findViewById(R.id.dueAmountTVID);
            dueCustomerLLID = itemView.findViewById(R.id.dueCustomerLLID);
        }
    }
}
