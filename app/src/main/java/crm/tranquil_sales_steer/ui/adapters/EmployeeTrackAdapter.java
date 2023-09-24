package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.EmployeeTrackResponse;
import crm.tranquil_sales_steer.ui.models.EmployeeTrackResponse;

public class EmployeeTrackAdapter extends RecyclerView.Adapter<ViewHolder> {

    ArrayList<EmployeeTrackResponse> teleCallersDataResponses = new ArrayList<>();
    Activity activity;

    private boolean isLoadingAdded = false;
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;

    public EmployeeTrackAdapter(ArrayList<EmployeeTrackResponse> teleCallersDataResponses, Activity activity) {
        this.teleCallersDataResponses = teleCallersDataResponses;
        this.activity = activity;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new TeleCallersVH(inflater.inflate(R.layout.employee_track_item, parent, false));
                break;

            case VIEW_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.progressbar1, parent, false));
                break;
        }

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case VIEW_ITEM:

                EmployeeTrackResponse item = teleCallersDataResponses.get(position);
                TeleCallersVH itemVH = (TeleCallersVH) holder;

                itemVH.locationTVID.setText(teleCallersDataResponses.get(position).getArea());
                itemVH.dateTVID.setText(teleCallersDataResponses.get(position).getCreated_date());

                break;
            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return teleCallersDataResponses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == teleCallersDataResponses.size() - 1 && isLoadingAdded)? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new EmployeeTrackResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = teleCallersDataResponses.size() - 1;
        EmployeeTrackResponse result = getItem(position);
        if (result != null) {
            teleCallersDataResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(EmployeeTrackResponse movie) {
        teleCallersDataResponses.add(movie);
        notifyItemInserted(teleCallersDataResponses.size() - 1);
    }

    public void addAll(List<EmployeeTrackResponse> moveResults) {
        for (EmployeeTrackResponse result : moveResults) {
            add(result);
        }
    }

    public EmployeeTrackResponse getItem(int position) {
        return teleCallersDataResponses.get(position);
    }

    public class TeleCallersVH extends ViewHolder{

        AppCompatTextView locationTVID,dateTVID;
        
        public TeleCallersVH(@NonNull View itemView) {
            super(itemView);

            locationTVID = itemView.findViewById(R.id.locationTVID);
            dateTVID = itemView.findViewById(R.id.dateTVID);
            
        }
    }

    public static class LoadingViewHolder extends ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }


}
