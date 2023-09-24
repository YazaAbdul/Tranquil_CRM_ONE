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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.models.SourceTypeLeadsResponse;
import de.hdodenhof.circleimageview.CircleImageView;


public class SourceTypeLeadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    LeadClickListener clickListener;
    ArrayList<SourceTypeLeadsResponse> leadsList = new ArrayList<>();
    String userID;


    public SourceTypeLeadsAdapter(LeadClickListener clickListener, Activity activity, ArrayList<SourceTypeLeadsResponse> leadsList) {
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
                viewHolder = new ItemViewHolder(inflater.inflate(R.layout.source_view_list_item, parent, false));
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

                SourceTypeLeadsResponse item = leadsList.get(i);
                itemVH.leadMobileTVID.setText(item.getActivity_name());
                itemVH.newLeadNameTVID.setText(item.getLead_name());
                itemVH.dateTVID.setText("Date : " + item.getActivity_date());
                itemVH.timeTVID.setText("Time : " +item.getActivity_time());

                userID = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_ID);

                try {
                    Picasso.with(activity).load(R.drawable.user).into(itemVH.leadPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                itemVH.newMainLeadView.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.newPhoneRL.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                //itemVH.cubeCallRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.whatsAppCallRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                //itemVH.mailBtnRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                //itemVH.messageBtnRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));

               /* itemVH.newPhoneRL.setOnClickListener(v -> {
                    CommunicationsServices.InsertCommunication(activity, "1", item.getS_no(), userID, "", "");
                    numberCalling(item.getMobile_number());
                    Intent intent = new Intent(activity, CallCompleteActivity.class);
                    intent.putExtra("ACTIVITY_NAME",item.getActivity_name());
                    intent.putExtra("ACTIVITY_ID",item.getActivity_id());
                    intent.putExtra("CUSTOMER_NAME",item.getFirst_name());
                    intent.putExtra("LEAD_ID",item.getS_no());
                });*/

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
        add(new SourceTypeLeadsResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = leadsList.size() - 1;
        SourceTypeLeadsResponse result = getItem(position);
        if (result != null) {
            leadsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(SourceTypeLeadsResponse movie) {
        leadsList.add(movie);
        notifyItemInserted(leadsList.size() - 1);
    }

    public void addAll(List<SourceTypeLeadsResponse> moveResults) {
        for (SourceTypeLeadsResponse result : moveResults) {
            add(result);
        }
    }

    public SourceTypeLeadsResponse getItem(int position) {
        return leadsList.get(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView newLeadNameTVID,leadMobileTVID,dateTVID,timeTVID;
        RelativeLayout newPhoneRL, newMainLeadView,cubeCallRLID;
        private SwipeRevealLayout swipelayout;
        RelativeLayout messageBtnRLID,mailBtnRLID,whatsAppCallRLID;
        CircleImageView leadPic;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            newMainLeadView = itemView.findViewById(R.id.newMainLeadView);
            newLeadNameTVID = itemView.findViewById(R.id.newLeadNameTVID);
            leadMobileTVID = itemView.findViewById(R.id.leadMobileTVID);
            messageBtnRLID = itemView.findViewById(R.id.messageBtnRLID);
            mailBtnRLID = itemView.findViewById(R.id.mailBtnRLID);
            newPhoneRL = itemView.findViewById(R.id.newPhoneRL);
            cubeCallRLID = itemView.findViewById(R.id.cubeCallRLID);
            dateTVID = itemView.findViewById(R.id.dateTVID);
            timeTVID = itemView.findViewById(R.id.timeTVID);

            whatsAppCallRLID = itemView.findViewById(R.id.whatsAppCallRLID);
            leadPic = itemView.findViewById(R.id.leadPic);

        }
    }


    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface LeadClickListener {
        void onLeadItemClick(SourceTypeLeadsResponse response, View v, int pos, ItemViewHolder holder);
    }
}
