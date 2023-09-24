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
import crm.tranquil_sales_steer.ui.models.ImagescreateiveResponse;

public class CreateivesFragmenttwoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    ArrayList<ImagescreateiveResponse> imagescreateiveResponse = new ArrayList<>();
    String cpPicStr,cpUserNameStr,cpMobileStr,cpEmailStr;
   CreativeClickListener creativeClickListener;
    public CreateivesFragmenttwoAdapter(Activity activity,ArrayList<ImagescreateiveResponse> imagescreateiveResponse,CreateivesFragmenttwoAdapter.CreativeClickListener creativeClickListener) {
        this.activity = activity;
        this.creativeClickListener=creativeClickListener;
        this.imagescreateiveResponse = imagescreateiveResponse;


    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder=null;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case VIEW_ITEM:
                viewHolder = new CreateivesFragmenttwoAdapter.CreativesVH(inflater.inflate(R.layout.creatives_recycler_item2, parent, false));
                break;
//creatives_recycler_item2
            case VIEW_LOADING:
                viewHolder = new CreateivesFragmenttwoAdapter.CreativesVH(inflater.inflate(R.layout.progressbar1, parent, false));

                break;

        }




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_ITEM:
//                CreativesVH itemVH = (CreativesVH) holder;
                CreativesVH itemVH = (CreativesVH) holder;
                ImagescreateiveResponse item = imagescreateiveResponse.get(position);

                Picasso.with(activity).load(item.getFile_name()).into(itemVH.creativeIVID);
              //  cpUserNameStr = MySharedPreferences.getPreferences(activity ,"" + AppConstants.SharedPreferenceValues.USER_NAME);
//                cpMobileStr = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
//                cpPicStr = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_PROFILE_PIC);
//                cpEmailStr = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_EMAIL_ID);

//                Picasso.with(activity).load(cpPicStr).placeholder(R.drawable.userpic).into(itemVH.agentIVID);
//                Picasso.with(activity).load(R.drawable.crmappicon).into(itemVH.companyLogoIVID);
//                itemVH.agentNameTVID.setText(cpUserNameStr);
//                itemVH.agentMobileTVID.setText(cpMobileStr);
//                itemVH.agentEmailTVID.setText(cpEmailStr);



//             itemVH.creativeShareBtn.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
//                itemVH.whatsAppShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
//                itemVH.facebookShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
//                itemVH.instagramShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
//                itemVH.twitterShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));

                itemVH.creativeShareBtn.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
                itemVH.whatsAppShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
                itemVH.facebookShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
                itemVH.instagramShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
                itemVH.twitterShareIVID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item, v, position, itemVH));
                itemVH.businessWhatsAppRLID.setOnClickListener(v -> creativeClickListener.onCreativeItemClick(item,v,position,itemVH));






                break;

            case VIEW_LOADING:
              LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return imagescreateiveResponse == null ? 0 : imagescreateiveResponse.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == imagescreateiveResponse.size() - 1 && isLoadingAdded) ? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ImagescreateiveResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = imagescreateiveResponse.size() - 1;
        ImagescreateiveResponse result = getItem(position);
        if (result != null) {
            imagescreateiveResponse.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(ImagescreateiveResponse movie) {
        imagescreateiveResponse.add(movie);
        notifyItemInserted(imagescreateiveResponse.size() - 1);
    }

    public void addAll(List<ImagescreateiveResponse> moveResults) {
        for (ImagescreateiveResponse result : moveResults) {
            add(result);
        }
    }


    public ImagescreateiveResponse getItem(int position) {
        return imagescreateiveResponse.get(position);
    }






    public class CreativesVH extends RecyclerView.ViewHolder{



        AppCompatImageView creativeIVID,companyLogoIVID,agentIVID;
        AppCompatTextView agentNameTVID,agentMobileTVID,agentEmailTVID;
        public AppCompatButton creativeShareBtn;
        public RelativeLayout creativeMainRLID,whatsAppShareIVID,facebookShareIVID,instagramShareIVID,
                twitterShareIVID,businessWhatsAppRLID;
        public CreativesVH(@NonNull View itemView) {
            super(itemView);



            creativeIVID = itemView.findViewById(R.id.creativeIVID);
//            creativeIVID = itemView.findViewById(R.id.creativeIVID);
//            agentIVID = itemView.findViewById(R.id.agentIVID);
//            companyLogoIVID = itemView.findViewById(R.id.companyLogoIVID);
  //         agentNameTVID = itemView.findViewById(R.id.agentNameTVID);
           creativeShareBtn = itemView.findViewById(R.id.creativeShareBtn);
            creativeMainRLID = itemView.findViewById(R.id.creativeMainRLID);
//            agentMobileTVID = itemView.findViewById(R.id.agentMobileTVID);
            agentEmailTVID = itemView.findViewById(R.id.agentEmailTVID);
            whatsAppShareIVID = itemView.findViewById(R.id.whatsAppShareIVID);
            facebookShareIVID = itemView.findViewById(R.id.facebookShareIVID);
            businessWhatsAppRLID = itemView.findViewById(R.id.businessWhatsAppRLID);
            instagramShareIVID = itemView.findViewById(R.id.instagramShareIVID);
            twitterShareIVID = itemView.findViewById(R.id.twitterShareIVID);
        }
    }
    public interface CreativeClickListener {
        void onCreativeItemClick(ImagescreateiveResponse response, View v, int pos,CreativesVH holder);
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }




}
