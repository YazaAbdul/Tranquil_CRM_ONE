package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.dashboard.InsideTopicDetails;
import crm.tranquil_sales_steer.ui.activities.dashboard.TopicDetailsActivity;
import crm.tranquil_sales_steer.ui.models.GettopicnamesResponse;

public class TopicDetailsAdapter extends RecyclerView.Adapter<TopicDetailsAdapter.ViewHolder>{
    ArrayList<GettopicnamesResponse> gettopicnamesResponse = new ArrayList<>();

    Activity activity;

    public TopicDetailsAdapter(ArrayList<GettopicnamesResponse> gettopicnamesResponse, Activity activity) {
        this.gettopicnamesResponse = gettopicnamesResponse;
        this.activity = activity;
    }
    @NonNull
    @Override
    public TopicDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopicDetailsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tele_source_type_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull TopicDetailsAdapter.ViewHolder holder, int position) {
        holder.teleSourceTVID.setText(gettopicnamesResponse.get(position).getTopic_name() + ")");
        //holder.teleSourceTVID.setText(gettopicnamesResponse.get(position).getTopic_id()+ ")");
        //  holder.topicid.setText(gettopicnamesResponse.get(position).getTopic_id()+ ")");

        holder.teleSourceCVID.setOnClickListener(v -> {


            Intent intent = new Intent(activity, InsideTopicDetails.class);
            intent.putExtra("TOPICID",gettopicnamesResponse.get(position).getTopic_id());

//            intent.putExtra("TOPICID",);
//            Toast.makeText(activity, ""+position, Toast.LENGTH_SHORT).show();
//            intent.putExtra("SOURCE_ID",gettopicnamesResponse.get(position).getTopic_id());
//            intent.putExtra("TITLE",gettopicnamesResponse.get(position).getTopic_name()+  ")");
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gettopicnamesResponse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView teleSourceCVID;
        AppCompatTextView teleSourceTVID;
        AppCompatTextView topicid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teleSourceCVID = itemView.findViewById(R.id.teleSourceCVID);
            teleSourceTVID = itemView.findViewById(R.id.teleSourceTVID);
        }
    }
}
