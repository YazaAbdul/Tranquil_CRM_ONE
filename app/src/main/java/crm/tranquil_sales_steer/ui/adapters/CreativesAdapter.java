package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.activities.dashboard.DashBoardActivity;
import crm.tranquil_sales_steer.ui.models.CreativesResponse;

public class CreativesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    ArrayList<CreativesResponse> creativesResponses = new ArrayList<>();
    CreativeClickListener creativeClickListener;
    String cpPicStr,cpUserNameStr,cpMobileStr,cpEmailStr;

    public CreativesAdapter(Activity activity, ArrayList<CreativesResponse> creativesResponses, CreativeClickListener creativeClickListener) {
        this.activity = activity;
        this.creativesResponses = creativesResponses;
        this.creativeClickListener = creativeClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new CreativesVH(inflater.inflate(R.layout.creatives_recycler_item, parent, false));
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
                CreativesVH itemVH = (CreativesVH) holder;
                CreativesResponse item = creativesResponses.get(i);

                Picasso.with(activity).load(item.getImage()).into(itemVH.creativeIVID);

                cpUserNameStr = MySharedPreferences.getPreferences(activity ,"" + AppConstants.SharedPreferenceValues.USER_NAME);
                cpMobileStr = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
                cpPicStr = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_PROFILE_PIC);
                cpEmailStr = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_EMAIL_ID);

                Picasso.with(activity).load(cpPicStr).placeholder(R.drawable.userpic).into(itemVH.agentIVID);
                Picasso.with(activity).load(R.drawable.crmappicon).into(itemVH.companyLogoIVID);
                //Picasso.get().load(R.drawable.crmappicon).into(itemVH.companyLogoIVID);
                itemVH.agentNameTVID.setText(cpUserNameStr);
                itemVH.agentMobileTVID.setText(cpMobileStr);
                itemVH.agentEmailTVID.setText(cpEmailStr);

                itemVH.creativeShareBtn.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item,v,i,itemVH));
                itemVH.whatsAppShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item,v,i,itemVH));
                itemVH.facebookShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item,v,i,itemVH));
                itemVH.instagramShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item,v,i,itemVH));
                itemVH.twitterShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item,v,i,itemVH));

                break;

            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }

    }



    @Override
    public int getItemCount() {

        return creativesResponses == null ? 0 : creativesResponses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == creativesResponses.size() - 1 && isLoadingAdded) ? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new CreativesResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = creativesResponses.size() - 1;
        CreativesResponse result = getItem(position);
        if (result != null) {
            creativesResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(CreativesResponse movie) {
        creativesResponses.add(movie);
        notifyItemInserted(creativesResponses.size() - 1);
    }

    public void addAll(List<CreativesResponse> moveResults) {
        for (CreativesResponse result : moveResults) {
            add(result);
        }
    }


    public CreativesResponse getItem(int position) {
        return creativesResponses.get(position);
    }

    public class CreativesVH extends RecyclerView.ViewHolder{

        AppCompatImageView creativeIVID,companyLogoIVID,agentIVID;
        AppCompatTextView agentNameTVID,agentMobileTVID,agentEmailTVID;
        public AppCompatButton creativeShareBtn;
        public RelativeLayout creativeMainRLID,whatsAppShareIVID,facebookShareIVID,instagramShareIVID,
                twitterShareIVID;


        public CreativesVH(@NonNull View itemView) {
            super(itemView);

            creativeIVID = itemView.findViewById(R.id.creativeIVID);
            agentIVID = itemView.findViewById(R.id.agentIVID);
            companyLogoIVID = itemView.findViewById(R.id.companyLogoIVID);
            agentNameTVID = itemView.findViewById(R.id.agentNameTVID);
            creativeShareBtn = itemView.findViewById(R.id.creativeShareBtn);
            creativeMainRLID = itemView.findViewById(R.id.creativeMainRLID);
            agentMobileTVID = itemView.findViewById(R.id.agentMobileTVID);
            agentEmailTVID = itemView.findViewById(R.id.agentEmailTVID);
            whatsAppShareIVID = itemView.findViewById(R.id.whatsAppShareIVID);
            facebookShareIVID = itemView.findViewById(R.id.facebookShareIVID);
            instagramShareIVID = itemView.findViewById(R.id.instagramShareIVID);
            twitterShareIVID = itemView.findViewById(R.id.twitterShareIVID);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface CreativeClickListener {
        void onCreativeItemClick(CreativesResponse response, View v, int pos, CreativesVH holder);
    }
}
