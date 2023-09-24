package crm.tranquil_sales_steer.ui.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.SourceTypesResponse;

public class SourceTypesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    SourceTypeClickListener clickListener;
    ArrayList<SourceTypesResponse> leadsList = new ArrayList<>();
    String userID;


    public SourceTypesAdapter(SourceTypeClickListener clickListener, Activity activity, ArrayList<SourceTypesResponse> leadsList) {
        this.leadsList = leadsList;
        this.clickListener = clickListener;
        this.activity = activity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new ItemViewHolder(inflater.inflate(R.layout.source_type_item, parent, false));
                break;

            case VIEW_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.progressbar1, parent, false));
                break;
        }

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int i) {

        switch (getItemViewType(i)) {
            case VIEW_ITEM:
                ItemViewHolder itemVH = (ItemViewHolder) holder;

                SourceTypesResponse item = leadsList.get(i);
                itemVH.sourceTypeTVID.setText(item.getSource_name() + " (" + item.getCount() +")" );
                itemVH.sourceTypeCVID.setOnClickListener(v -> clickListener.onSourceTypeItemClick(item, v, i, itemVH));

                break;

            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }
    }



    @Override
    public int getItemCount() {

        return leadsList == null ? 0 : leadsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == leadsList.size() - 1 && isLoadingAdded) ? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new SourceTypesResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = leadsList.size() - 1;
        SourceTypesResponse result = getItem(position);
        if (result != null) {
            leadsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(SourceTypesResponse movie) {
        leadsList.add(movie);
        notifyItemInserted(leadsList.size() - 1);
    }

    public void addAll(List<SourceTypesResponse> moveResults) {
        for (SourceTypesResponse result : moveResults) {
            add(result);
        }
    }

    public SourceTypesResponse getItem(int position) {
        return leadsList.get(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView sourceTypeTVID;
        CardView sourceTypeCVID;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            sourceTypeTVID = itemView.findViewById(R.id.sourceTypeTVID);
            sourceTypeCVID = itemView.findViewById(R.id.sourceTypeCVID);


        }
    }


    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface SourceTypeClickListener {
        void onSourceTypeItemClick(SourceTypesResponse response, View v, int pos, ItemViewHolder holder);
    }
}
