package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.ui.activities.dashboard.NewLeadsDataActivity;
import crm.tranquil_sales_steer.ui.activities.dashboard.TopicDetailsActivity;
import crm.tranquil_sales_steer.ui.models.GetModuleNameResponse;

public class GetModuleNameAdapter extends RecyclerView.Adapter<GetModuleNameAdapter.PreSalesVH> {
   private Activity activity;
   private LayoutInflater inflater;
   private ArrayList<GetModuleNameResponse> availabilityList = new ArrayList<>();
    String userID,userType;


    public GetModuleNameAdapter(Activity activity, ArrayList<GetModuleNameResponse> availabilityList, String userID, String userType) {
        this.activity = activity;
        this.inflater = inflater;
        this.availabilityList = availabilityList;
        this.userID = userID;
        this.userType = userType;
    }

    @NonNull
    @Override
    public GetModuleNameAdapter.PreSalesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetModuleNameAdapter.PreSalesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.z_menu_list_item_1, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetModuleNameAdapter.PreSalesVH holder, int position) {
        holder.textID.setText(Utilities.CapitalText(availabilityList.get(position).getName()));
        holder.textID.setSelected(true);


        holder.textID.setTextColor(R.drawable.bg_circle_black);

//        if (availabilityList.get(position).getColor_one() !=null && availabilityList.get(i).getColor_two()!= null){
//
//            try {
//                int[] colors = {Color.parseColor(availabilityList.get(i).getColor_one()),Color.parseColor(availabilityList.get(i).getColor_two())};
//
//                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
//                gd.setCornerRadius(0f);
//
//                holder.imageViewIcon.setBackground(gd);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }else {
//
//        }


        String title = availabilityList.get(position).getName();
        holder.iconTitleID.setText(Utilities.CapitalText(title));

        holder.imageViewIcon.setOnClickListener(v -> {

            Log.e("clicked==>","");
            String s = availabilityList.get(position).getName();
            String[] parts = s.split("\\("); // escape .
            String part1 = parts[0];
            Intent intent = new Intent(activity, TopicDetailsActivity.class);
            intent.putExtra("ACTIVITY_ID", availabilityList.get(position).getM_id());
            intent.putExtra("ACTIVITY_TITLE", "" + part1);
            intent.putExtra("USER_ID", userID);
            intent.putExtra("USER_TYPE", userType);
            intent.putExtra(AppConstants.VIEW_TYPE, "ACTIVITIES");
            intent.putExtra("TITLE", "" + part1);
            activity.startActivity(intent);
        });




    }

    @Override
    public int getItemCount() {
        return availabilityList.size();
    }

    public class PreSalesVH extends RecyclerView.ViewHolder {

        TextView textID;
        CardView clickID;
        LinearLayout activitiesLLID;
        TextView iconTitleID;
        LinearLayout imageViewIcon,viewRLID;
        AppCompatImageView planImageRLID;

        public PreSalesVH(@NonNull View itemView) {
            super(itemView);
            textID = (TextView) itemView.findViewById(R.id.textViewName);
            clickID = (CardView)itemView.findViewById(R.id.clickID);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            //viewRLID = itemView.findViewById(R.id.viewRLID);
            iconTitleID = (TextView)itemView.findViewById(R.id.iconTitleID);
            activitiesLLID = (LinearLayout)itemView.findViewById(R.id.activitiesLLID);
            //planImageRLID = itemView.findViewById(R.id.planImageRLID);
        }
    }
}
