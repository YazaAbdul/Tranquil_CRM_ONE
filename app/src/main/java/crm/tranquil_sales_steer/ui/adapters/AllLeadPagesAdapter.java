package crm.tranquil_sales_steer.ui.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.models.AllDataResponse;
import de.hdodenhof.circleimageview.CircleImageView;


public class AllLeadPagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   // String planType = MySharedPreferences.getPreferences("","PLAN_TYPE");
   private Context context;
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    LeadClickListener clickListener;
    ArrayList<AllDataResponse> leadsList = new ArrayList<>();
    String userID;
    String plantype;


    public AllLeadPagesAdapter(LeadClickListener clickListener, Activity activity, ArrayList<AllDataResponse> leadsList,Context context) {

        this.leadsList = leadsList;
        this.clickListener = clickListener;
        this.activity = activity;
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("PLAN_TYPE", Context.MODE_PRIVATE);
        plantype = MySharedPreferences.getPreferences(context,"PLAN_TYPE");
        Log.e("playtype", ""+plantype);

    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new ItemViewHolder(inflater.inflate(R.layout.leads_data_item_new, parent, false));

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

                AllDataResponse item = leadsList.get(i);
                itemVH.projectTVID.setText(item.getRequirement_name());

                itemVH.leadNameTVID.setText(capitalize(item.getFirst_name()));
                itemVH.createdDateTVID.setText(item.getCreated_date());
                itemVH.activityDateTVID.setText(item.getActivity_date());
                itemVH.activityDateTitleTVID.setText(item.getAlldata());
                itemVH.leadSourceTVID.setText(item.getLead_source());
                itemVH.leadIDTVID.setText(item.getS_no());
                //itemVH.timeTVID.setText("Time : " +item.getActivity_time());

                userID = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_ID);

                try {
                    Picasso.with(activity).load(item.getLead_pic()).error(R.drawable.user).error(R.drawable.user).placeholder(R.drawable.user).rotate(0).into(itemVH.leadPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                itemVH.newMainLeadView.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.newPhoneRL.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.cubeCallRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.whatsAppCallRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.businessWhatsAppCallRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.foldersRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.mailBtnRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));
                itemVH.messageBtnRLID.setOnClickListener(v -> clickListener.onLeadItemClick(item, v, i, itemVH));

                if (item.getLast_remark().isEmpty()){
                    itemVH.lastRemarkTVID.setVisibility(View.VISIBLE);
                    //itemVH.lastRemarkTVID.setText("Last Remark" +item.getLast_remark());
                }else {
                    itemVH.lastRemarkTVID.setVisibility(View.VISIBLE);
                    itemVH.lastRemarkTVID.setText(capitalize(item.getLast_remark()));

                }

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

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
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
        add(new AllDataResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = leadsList.size() - 1;
        AllDataResponse result = getItem(position);
        if (result != null) {
            leadsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(AllDataResponse movie) {
        leadsList.add(movie);
        notifyItemInserted(leadsList.size() - 1);
    }

    public void addAll(List<AllDataResponse> moveResults) {
        for (AllDataResponse result : moveResults) {
            add(result);
        }
    }

    public AllDataResponse getItem(int position) {
        return leadsList.get(position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView newLeadNameTVID,leadMobileTVID,dateTVID,timeTVID;
        AppCompatTextView lastRemarkTVID;
        AppCompatTextView leadNameTVID,leadIDTVID,leadSourceTVID,projectTVID,createdDateTVID,activityDateTVID,activityDateTitleTVID;
        RelativeLayout newPhoneRL, newMainLeadView,cubeCallRLID;
        private SwipeRevealLayout swipelayout;
        RelativeLayout messageBtnRLID,mailBtnRLID,whatsAppCallRLID,businessWhatsAppCallRLID,foldersRLID;
        CircleImageView leadPic;

      /*  private PreferenceManager context;
        SharedPreferences preferences = context.getSharedPreferences();
        String planType = preferences.getString("PLAN_TYPE", "");*/




        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            newMainLeadView = itemView.findViewById(R.id.newMainLeadView);
            newLeadNameTVID = itemView.findViewById(R.id.newLeadNameTVID);
            leadMobileTVID = itemView.findViewById(R.id.leadMobileTVID);
            messageBtnRLID = itemView.findViewById(R.id.messageBtnRLID);
            mailBtnRLID = itemView.findViewById(R.id.mailBtnRLID);
            newPhoneRL = itemView.findViewById(R.id.newPhoneRL);







            dateTVID = itemView.findViewById(R.id.dateTVID);
            timeTVID = itemView.findViewById(R.id.timeTVID);
            lastRemarkTVID = itemView.findViewById(R.id.lastRemarkTVID);

            leadNameTVID = itemView.findViewById(R.id.leadNameTVID);
            leadIDTVID = itemView.findViewById(R.id.leadIDTVID);
            leadSourceTVID = itemView.findViewById(R.id.leadSourceTVID);
            projectTVID = itemView.findViewById(R.id.projectTVID);
            createdDateTVID = itemView.findViewById(R.id.createdDateTVID);
            activityDateTVID = itemView.findViewById(R.id.activityDateTVID);
            activityDateTitleTVID = itemView.findViewById(R.id.activityDateTitleTVID);

            whatsAppCallRLID = itemView.findViewById(R.id.whatsAppCallRLID);
            businessWhatsAppCallRLID = itemView.findViewById(R.id.businessWhatsAppCallRLID);
            foldersRLID = itemView.findViewById(R.id.foldersRLID);
            leadPic = itemView.findViewById(R.id.leadPic);

            cubeCallRLID = itemView.findViewById(R.id.cubeCallRLID);

            if (plantype.equals("1")){
                  cubeCallRLID.setVisibility(View.VISIBLE);
                whatsAppCallRLID.setVisibility(View.VISIBLE);
                foldersRLID.setVisibility(View.VISIBLE);
                /*     Toast.makeText(context, "plantype adapter   "+plantype, Toast.LENGTH_SHORT).show();*/
                Log.e("playtype", ""+plantype);
            }else{
                cubeCallRLID.setVisibility(View.GONE);
                whatsAppCallRLID.setVisibility(View.GONE);
                foldersRLID.setVisibility(View.GONE);
            }



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
        void onLeadItemClick(AllDataResponse response, View v, int pos, ItemViewHolder holder);
    }
}
