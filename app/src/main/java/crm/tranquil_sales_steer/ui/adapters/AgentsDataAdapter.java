package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.AgentsMainResponse;
import crm.tranquil_sales_steer.ui.models.AgentsResponse;

public class AgentsDataAdapter extends RecyclerView.Adapter<AgentsDataAdapter.AgentsVH> {

    ArrayList<AgentsResponse> agentsMainResponses = new ArrayList<>();
    Activity activity;
    AgentCountClickListener agentCountClickListener;

    public AgentsDataAdapter(ArrayList<AgentsResponse> agentsMainResponses, Activity activity, AgentCountClickListener agentCountClickListener) {
        this.agentsMainResponses = agentsMainResponses;
        this.activity = activity;
        this.agentCountClickListener = agentCountClickListener;
    }

    @NonNull
    @Override
    public AgentsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AgentsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.agesnts_list_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AgentsVH holder, int position) {

        AgentsResponse item = agentsMainResponses.get(position);


        holder.agentNameTVID.setText(agentsMainResponses.get(position).getAgent_name());
        holder.agentMobileTVID.setText(agentsMainResponses.get(position).getAgent_mobile());


        holder.msgRLID.setOnClickListener(v -> agentCountClickListener.onAgentCountItemClick(item, v, position, holder));
        holder.whatsappRLID.setOnClickListener(v -> agentCountClickListener.onAgentCountItemClick(item, v, position, holder));
        holder.phnRLID.setOnClickListener(v -> agentCountClickListener.onAgentCountItemClick(item, v, position, holder));


    }

    @Override
    public int getItemCount() {
        return agentsMainResponses.size();
    }

    public class AgentsVH extends RecyclerView.ViewHolder{
        CardView agentCountCVID;
        TextView agentNameTVID,agentMobileTVID;
        RelativeLayout msgRLID,whatsappRLID,phnRLID;
        public AgentsVH(@NonNull View itemView) {
            super(itemView);

            agentCountCVID = itemView.findViewById(R.id.agentCountCVID);
            agentNameTVID = itemView.findViewById(R.id.agentNameTVID);
            agentMobileTVID = itemView.findViewById(R.id.agentMobileTVID);
            msgRLID = itemView.findViewById(R.id.msgRLID);
            whatsappRLID = itemView.findViewById(R.id.whatsappRLID);
            phnRLID = itemView.findViewById(R.id.phnRLID);
        }
    }

    public interface AgentCountClickListener {
        void onAgentCountItemClick(AgentsResponse response, View v, int pos, AgentsDataAdapter.AgentsVH holder);
    }
}
