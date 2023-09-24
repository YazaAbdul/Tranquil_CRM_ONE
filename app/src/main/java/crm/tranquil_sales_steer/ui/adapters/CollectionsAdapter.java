package crm.tranquil_sales_steer.ui.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.models.AllDataResponse;
import crm.tranquil_sales_steer.ui.models.CollectionsResponse;
import de.hdodenhof.circleimageview.CircleImageView;


public class CollectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    ArrayList<CollectionsResponse> leadsList = new ArrayList<>();
    String userID;


    public CollectionsAdapter( Activity activity, ArrayList<CollectionsResponse> leadsList) {
        this.leadsList = leadsList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new ItemViewHolder(inflater.inflate(R.layout.collections_list_item, parent, false));
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
                CollectionsResponse item = leadsList.get(i);
                itemVH.customerNameTVID.setText(item.getCustomer_name());
                itemVH.projectNameTVID.setText(item.getProject_name());
                itemVH.amountTVID.setText("₹ "+item.getAmount());
                itemVH.paidDateTVID.setText(item.getPaid_date());

                break;

            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void numberCalling(String number) {

        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                activity.startActivity(callIntent);
                // Log.d("CONSTRUCTOR_ID_EXE_1->", "" + callID);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                activity.startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        add(new CollectionsResponse());

    }

    public void removeLoadingFooter() {

        isLoadingAdded = false;
        int position = leadsList.size() - 1;
        CollectionsResponse result = getItem(position);
        if (result != null) {
            leadsList.remove(position);
            notifyItemRemoved(position);
        }

    }

    public void add(CollectionsResponse movie) {

        leadsList.add(movie);
        notifyItemInserted(leadsList.size() - 1);

    }

    public void addAll(List<CollectionsResponse> moveResults) {

        for(CollectionsResponse result : moveResults) {
            add(result);
        }

    }

    public CollectionsResponse getItem(int position) {
        return leadsList.get(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newLeadNameTVID,leadMobileTVID,dateTVID,timeTVID;
        RelativeLayout newPhoneRL, newMainLeadView,cubeCallRLID;
        private SwipeRevealLayout swipelayout;
        RelativeLayout messageBtnRLID,mailBtnRLID,whatsAppCallRLID;
        CircleImageView leadPic;

        AppCompatTextView customerNameTVID,projectNameTVID,amountTVID,paidDateTVID;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            customerNameTVID = itemView.findViewById(R.id.customerNameTVID);
            projectNameTVID = itemView.findViewById(R.id.projectNameTVID);
            amountTVID = itemView.findViewById(R.id.amountTVID);
            paidDateTVID = itemView.findViewById(R.id.paidDateTVID);

        }
    }


    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

}
