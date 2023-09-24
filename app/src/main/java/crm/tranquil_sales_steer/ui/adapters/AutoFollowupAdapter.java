package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.AutoFollowupResponse;
import crm.tranquil_sales_steer.ui.models.AutoFollowupResponse;

public class AutoFollowupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    ArrayList<AutoFollowupResponse> autoFollowupResponses = new ArrayList<>();
    KnowlarityCallClickListener knowlarityCallClickListener;

    public AutoFollowupAdapter(Activity activity, ArrayList<AutoFollowupResponse> autoFollowupResponses, KnowlarityCallClickListener knowlarityCallClickListener) {
        this.activity = activity;
        this.autoFollowupResponses = autoFollowupResponses;
        this.knowlarityCallClickListener = knowlarityCallClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new KnowlarityCallVH(inflater.inflate(R.layout.auto_followup_list_item, parent, false));
                break;

            case VIEW_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.progressbar1, parent, false));
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        switch (getItemViewType(i)) {
            case VIEW_ITEM:
                KnowlarityCallVH itemVH = (KnowlarityCallVH) holder;
                AutoFollowupResponse item = autoFollowupResponses.get(i);


                itemVH.customerNameTVID.setText(item.getCustomer_name());
                itemVH.durationTVID.setText(item.getActivity_time());
                itemVH.activityTVID.setText(item.getActivity_name());
                //itemVH.statusTVID.setText(item.getCall_end());


                //itemVH.playerRLID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                itemVH.convertRLID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                itemVH.updateBtn.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));


                break;

            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }

    }


    @Override
    public int getItemCount() {

        return autoFollowupResponses == null ? 0 : autoFollowupResponses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == autoFollowupResponses.size() - 1 && isLoadingAdded) ? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new AutoFollowupResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = autoFollowupResponses.size() - 1;
        AutoFollowupResponse result = getItem(position);
        if (result != null) {
            autoFollowupResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(AutoFollowupResponse movie) {
        autoFollowupResponses.add(movie);
        notifyItemInserted(autoFollowupResponses.size() - 1);
    }

    public void addAll(List<AutoFollowupResponse> moveResults) {
        for (AutoFollowupResponse result : moveResults) {
            add(result);
        }
    }


    public AutoFollowupResponse getItem(int position) {
        return autoFollowupResponses.get(position);
    }

    public class KnowlarityCallVH extends RecyclerView.ViewHolder{


        AppCompatTextView customerNameTVID,durationTVID,activityTVID,statusTVID;
        RelativeLayout convertRLID;
        AppCompatButton updateBtn;


        public KnowlarityCallVH(@NonNull View itemView) {
            super(itemView);

            customerNameTVID = itemView.findViewById(R.id.customerNameTVID);
            durationTVID = itemView.findViewById(R.id.durationTVID);
            activityTVID = itemView.findViewById(R.id.activityTVID);
            statusTVID = itemView.findViewById(R.id.statusTVID);
            updateBtn = itemView.findViewById(R.id.updateBtn);


            convertRLID = itemView.findViewById(R.id.convertRLID);

        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface KnowlarityCallClickListener {
        void onKnowlarityCallItemClick(AutoFollowupResponse response, View v, int pos, KnowlarityCallVH holder);
    }
}
