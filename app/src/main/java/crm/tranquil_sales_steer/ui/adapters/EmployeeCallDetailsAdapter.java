package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.CallHistoryResponse;


public class EmployeeCallDetailsAdapter extends RecyclerView.Adapter<EmployeeCallDetailsAdapter.EmployeeViewHolder> {

    ArrayList<CallHistoryResponse> employeeCallDetailsResponses = new ArrayList<CallHistoryResponse>();

    public EmployeeCallDetailsAdapter(ArrayList<CallHistoryResponse> employeeCallDetailsResponses) {
        this.employeeCallDetailsResponses = employeeCallDetailsResponses;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new EmployeeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_call_details_list_item,parent,false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        holder.customerNameTVID.setText(employeeCallDetailsResponses.get(position).getCustomer_name());
        holder.mobileTVID.setText(employeeCallDetailsResponses.get(position).getMobile_number());
        holder.callStartTVID.setText(employeeCallDetailsResponses.get(position).getCal_start_time());
        holder.callEndTVID.setText(employeeCallDetailsResponses.get(position).getCal_end_time());
        holder.timeInSecTVID.setText(employeeCallDetailsResponses.get(position).getTime_in_sec());
        holder.nextCallGapTVID.setText(employeeCallDetailsResponses.get(position).getTime_gap());

        holder.nextCallRLID.setBackgroundColor(Color.parseColor(employeeCallDetailsResponses.get(position).getColor()));


        /*if (employeeCallDetailsResponses.get(position).getColor() == null){
            holder.nextCallRLID.setBackgroundColor((R.color.white));
        }else {
        }
*/
    }

    @Override
    public int getItemCount() {
        return employeeCallDetailsResponses.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTVID,mobileTVID,callStartTVID,callEndTVID,timeInSecTVID,nextCallGapTVID;
        RelativeLayout nextCallRLID;

        public EmployeeViewHolder(@NonNull View view) {
            super(view);
            customerNameTVID = view.findViewById(R.id.customerNameTVID);
            mobileTVID = view.findViewById(R.id.mobileTVID);
            callStartTVID = view.findViewById(R.id.callStartTVID);
            callEndTVID = view.findViewById(R.id.callEndTVID);
            timeInSecTVID = view.findViewById(R.id.timeInSecTVID);
            nextCallGapTVID = view.findViewById(R.id.nextCallGapTVID);
            nextCallGapTVID = view.findViewById(R.id.nextCallGapTVID);
            nextCallRLID = view.findViewById(R.id.nextCallRLID);


        }
    }
}
