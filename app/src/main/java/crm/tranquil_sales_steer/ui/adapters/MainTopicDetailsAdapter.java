package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.MainTopicDetailsResponse;

public class MainTopicDetailsAdapter extends RecyclerView.Adapter<MainTopicDetailsAdapter.MainTopicDetailsAdapterVH> {



    ArrayList<MainTopicDetailsResponse> mainTopicDetailsResponse = new ArrayList<>();
    Activity activity;
   TopicDetailsClickListener topicDetailsClickListener;


    public MainTopicDetailsAdapter(ArrayList<MainTopicDetailsResponse> mainTopicDetailsResponse, Activity activity, TopicDetailsClickListener topicDetailsClickListener) {
        this.mainTopicDetailsResponse = mainTopicDetailsResponse;
        this.activity = activity;
        this.topicDetailsClickListener = topicDetailsClickListener;
    }

    String[] startColors = {"#756FC0", "#006BFF", "#00C0CB", "#E23132", "#A25C97", "#DBA712", "#20AADD",
            "#D85741", "#756FC0","#006BFF"};

    String[] endColor = {"#8A66BA", "#0038EA", "#00D797", "#CB1314", "#A53A94", "#BA8D0B", "#00A2DD",
            "#CF462E", "#8A66BA","#0038EA"};

    @NonNull
    @Override
    public MainTopicDetailsAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainTopicDetailsAdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.shortcuts_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainTopicDetailsAdapterVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mainTopicDetailsResponse.size();
    }

    public class MainTopicDetailsAdapterVH extends RecyclerView.ViewHolder {
        AppCompatTextView shortcutsTVID;
        AppCompatImageView shortcutsIVID,shortcutsBackgroundIVID,planImageRLID;
        RelativeLayout shortcutsBackgroundRLID,shortcutsRLID;

        public MainTopicDetailsAdapterVH(@NonNull View itemView) {
            super(itemView);
            shortcutsTVID = itemView.findViewById(R.id.shortcutsTVID);
            shortcutsIVID = itemView.findViewById(R.id.shortcutsIVID);
            shortcutsBackgroundIVID = itemView.findViewById(R.id.shortcutsBackgroundIVID);
            shortcutsBackgroundRLID = itemView.findViewById(R.id.shortcutsBackgroundRLID);
            planImageRLID = itemView.findViewById(R.id.planImageRLID);
            shortcutsRLID = itemView.findViewById(R.id.shortcutsRLID);
        }
    }
    public interface TopicDetailsClickListener{
        void onTopicDetailsClicked(MainTopicDetailsResponse mainTopicDetailsResponse,int position, View v,MainTopicDetailsAdapterVH holder);
    }

}
