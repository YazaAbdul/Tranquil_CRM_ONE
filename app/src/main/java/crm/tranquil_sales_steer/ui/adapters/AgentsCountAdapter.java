package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.AgentsMainResponse;
import crm.tranquil_sales_steer.ui.models.SourceTypesResponse;

public class AgentsCountAdapter extends RecyclerView.Adapter<AgentsCountAdapter.AgentsVH> {

    ArrayList<AgentsMainResponse> agentsMainResponses = new ArrayList<>();
    Activity activity;
    AgentCountClickListener agentCountClickListener;

    public AgentsCountAdapter(ArrayList<AgentsMainResponse> agentsMainResponses, Activity activity, AgentCountClickListener agentCountClickListener) {
        this.agentsMainResponses = agentsMainResponses;
        this.activity = activity;
        this.agentCountClickListener = agentCountClickListener;
    }

    @NonNull
    @Override
    public AgentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.agents_count_list_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AgentsVH holder, int position) {

        AgentsMainResponse item = agentsMainResponses.get(position);

        if (agentsMainResponses.get(position).getDesignation_name() != null && agentsMainResponses.get(position).getNoofagents() != null) {
            holder.agentCountTVID.setText(agentsMainResponses.get(position).getDesignation_name() + " (" + agentsMainResponses.get(position).getNoofagents() + ")");
        } else {
        }

        holder.agentCountCVID.setOnClickListener(v -> agentCountClickListener.onAgentCountItemClick(item, v, position, holder));


    }

    @Override
    public int getItemCount() {
        return agentsMainResponses.size();
    }

    public class AgentsVH extends RecyclerView.ViewHolder{
        CardView agentCountCVID;
        AppCompatTextView agentCountTVID;
        public AgentsVH(@NonNull View itemView) {
            super(itemView);

            agentCountCVID = itemView.findViewById(R.id.agentCountCVID);
            agentCountTVID = itemView.findViewById(R.id.agentCountTVID);
        }
    }

    public interface AgentCountClickListener {
        void onAgentCountItemClick(AgentsMainResponse response, View v, int pos, AgentsCountAdapter.AgentsVH holder);
    }
}
