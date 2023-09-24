package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.TeleCallersDataResponse;

public class TeleCallersDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<TeleCallersDataResponse> teleCallersDataResponses = new ArrayList<>();
    Activity activity;
    TeleLeadClickListener teleLeadClickListener;
    private boolean isLoadingAdded = false;
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;

    public TeleCallersDataAdapter(ArrayList<TeleCallersDataResponse> teleCallersDataResponses, Activity activity,TeleLeadClickListener teleLeadClickListener) {
        this.teleCallersDataResponses = teleCallersDataResponses;
        this.activity = activity;
        this.teleLeadClickListener = teleLeadClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new TeleCallersVH(inflater.inflate(R.layout.tele_callers_data_item, parent, false));
                break;

            case VIEW_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.progressbar1, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case VIEW_ITEM:

                TeleCallersDataResponse item = teleCallersDataResponses.get(position);
                TeleCallersVH itemVH = (TeleCallersVH) holder;

                itemVH.newLeadNameTVID.setText(teleCallersDataResponses.get(position).getLead_name());
                itemVH.leadMobileTVID.setText(teleCallersDataResponses.get(position).getLead_mobile());

                itemVH.newPhoneRL.setOnClickListener(v -> teleLeadClickListener.onTeleLeadItemClick(item, v, position, itemVH));
                break;
            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return teleCallersDataResponses == null ? 0 : teleCallersDataResponses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == teleCallersDataResponses.size() - 1 && isLoadingAdded)? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new TeleCallersDataResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = teleCallersDataResponses.size() - 1;
        TeleCallersDataResponse result = getItem(position);
        if (result != null) {
            teleCallersDataResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(TeleCallersDataResponse movie) {
        teleCallersDataResponses.add(movie);
        notifyItemInserted(teleCallersDataResponses.size() - 1);
    }

    public void addAll(List<TeleCallersDataResponse> moveResults) {
        for (TeleCallersDataResponse result : moveResults) {
            add(result);
        }
    }

    public TeleCallersDataResponse getItem(int position) {
        return teleCallersDataResponses.get(position);
    }

    public class TeleCallersVH extends ViewHolder{

        TextView newLeadNameTVID,leadMobileTVID;
        RelativeLayout newPhoneRL;

        public TeleCallersVH(@NonNull View itemView) {
            super(itemView);

            newLeadNameTVID = itemView.findViewById(R.id.newLeadNameTVID);
            leadMobileTVID = itemView.findViewById(R.id.leadMobileTVID);
            newPhoneRL = itemView.findViewById(R.id.newPhoneRL);
        }
    }

    public static class LoadingViewHolder extends ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface TeleLeadClickListener {
        void onTeleLeadItemClick(TeleCallersDataResponse response, View v, int pos, TeleCallersDataAdapter.TeleCallersVH holder);
    }
}
