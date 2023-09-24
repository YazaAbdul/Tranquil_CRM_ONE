package crm.tranquil_sales_steer.ui.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.models.RecordingsList;


public class RecordingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    LeadClickListener clickListener;
    ArrayList<RecordingsList> leadsList = new ArrayList<>();
    String userID;


    public RecordingsAdapter(LeadClickListener clickListener, Activity activity, ArrayList<RecordingsList> leadsList) {
        this.leadsList = leadsList;
        this.clickListener = clickListener;
        this.activity = activity;
    }

    String[] mColors = {"#3F51B5", "#FF9800", "#009688", "#673AB7", "#999999", "#454545", "#00FF00",
            "#FF0000", "#0000FF", "#800000", "#808000", "#00FF00", "#008000", "#00FFFF",
            "#000080", "#800080", "#f40059", "#0080b8", "#350040", "#650040", "#750040",
            "#45ddc0", "#dea42d", "#b83800", "#dd0244", "#c90000", "#465400",
            "#ff004d", "#ff6700", "#5d6eff", "#3955ff", "#0a24ff", "#004380", "#6b2e53",
            "#a5c996", "#f94fad", "#ff85bc", "#ff906b", "#b6bc68", "#296139"};


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new ItemViewHolder(inflater.inflate(R.layout.recordings_list_item, parent, false));
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

                RecordingsList item = leadsList.get(i);

                userID = MySharedPreferences.getPreferences(activity, "" + AppConstants.SharedPreferenceValues.USER_ID);

                GradientDrawable bgShape = (GradientDrawable) itemVH.imageViewIcon.getBackground();
                bgShape.setColor(Color.parseColor(mColors[i % 40]));

                itemVH.iconTitleID.setText(item.getUser_name());
                itemVH.customerNameTVID.setText(item.getUser_name());
                //itemVH.projectTVID.setText(item.getUser_name());
                itemVH.callDateTVID.setText(item.getCreated_date());
                itemVH.durationTVID.setText(item.getDuration() + " Sec");
                itemVH.callTimeTVID.setText(item.getDuration());


                int minutes = Integer.parseInt(item.getDuration());

                int min = (minutes/ 60) % 60;

                Log.e("call_min==>","" + min + " SEC");

                //itemVH.durationTVID.setText(String.valueOf(min));

                itemVH.callByTVID.setText(item.getUser_name());
                itemVH.audioLLID.setOnClickListener(v -> {
                    clickListener.onLeadItemClick(item,v,i,itemVH);


                });
                itemVH.callID.setOnClickListener(v -> {
                    //dialedCalls(customerID, userID, numberStr);
                });
                itemVH.phoneLLID.setVisibility(View.GONE);

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
        add(new RecordingsList());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = leadsList.size() - 1;
        RecordingsList result = getItem(position);
        if (result != null) {
            leadsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(RecordingsList movie) {
        leadsList.add(movie);
        notifyItemInserted(leadsList.size() - 1);
    }

    public void addAll(List<RecordingsList> moveResults) {
        for (RecordingsList result : moveResults) {
            add(result);
        }
    }

    public RecordingsList getItem(int position) {
        return leadsList.get(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView customerNameTVID;
        TextView callTimeTVID;
        TextView callDateTVID;
        LinearLayout audioLLID;
        TextView durationTVID;
        LinearLayout phoneLLID;
        TextView projectTVID;
        TextView iconTitleID;
        TextView callByTVID;
        RelativeLayout imageViewIcon, callID;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            customerNameTVID = view.findViewById(R.id.customerNameTVID);
            callTimeTVID = view.findViewById(R.id.callTimeTVID);
            callDateTVID = view.findViewById(R.id.callDateTVID);
            audioLLID = view.findViewById(R.id.audioLLID);
            durationTVID = view.findViewById(R.id.durationTVID);
            phoneLLID = view.findViewById(R.id.phoneLLID);
            projectTVID = view.findViewById(R.id.projectTVID);
            iconTitleID = view.findViewById(R.id.iconTitleID);
            imageViewIcon = view.findViewById(R.id.imageViewIcon);
            callID = view.findViewById(R.id.callID);
            callByTVID=view.findViewById(R.id.callByTVID);

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
        void onLeadItemClick(RecordingsList response, View v, int pos, ItemViewHolder holder);
    }
}
