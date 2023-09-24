package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.ui.activities.dashboard.NewLeadsDataActivity;
import crm.tranquil_sales_steer.ui.models.DashBoardResponseNew;

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.PreSalesVH>{

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<DashBoardResponseNew> availabilityList = new ArrayList<>();
    String userID,userType;


    public DashBoardAdapter(Activity activity, ArrayList<DashBoardResponseNew> availabilityList,String userID,String userType) {
        try {
            this.activity = activity;
            this.availabilityList = availabilityList;
            this.userID = userID;
            this.userType = userType;
            inflater = LayoutInflater.from(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public PreSalesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashBoardAdapter.PreSalesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.z_menu_list_item_2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PreSalesVH holder, int i) {

        PreSalesVH itemVH= (PreSalesVH)  holder;

        holder.textID.setText(Utilities.CapitalText(availabilityList.get(i).getActivity_name()));
        holder.textID.setSelected(true);
       /* holder.imageViewIcon.setBackgroundTintMode(availabilityList.get(i).getActivity_image()));*/
        DashBoardResponseNew item=availabilityList.get(i);
    Picasso.with(activity).load(item.getActivity_image()).into( itemVH.imageViewbg);



     if (availabilityList.get(i).getColor_one() !=null && availabilityList.get(i).getColor_two()!= null){

            try {
                int[] colors = {Color.parseColor(availabilityList.get(i).getColor_one()),Color.parseColor(availabilityList.get(i).getColor_two())};

               // GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                gd.setCornerRadius(14f);

               // holder.imageViewIcon.setBackground(gd);
           //     holder.clickID.setBackgroundColor(Color.parseColor(availabilityList.get(i).getColor_one()));
                holder.clickID.setBackground(gd);
              //  holder.imageViewIcon.setBackgroundTintMode(availabilityList.get(i).getActivity_image());
              //  holder.imageViewIcon.setBackground(gd);
               // holder.clickID.setAlpha(0.6f);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

        }







        /*if (availabilityList.get(i).getColor_light() !=null && availabilityList.get(i).getColor_light_two()!= null){

            try {
                int[] colors = {Color.parseColor(availabilityList.get(i).getColor_light()),Color.parseColor(availabilityList.get(i).getColor_light_two())};

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                gd.setCornerRadius(0f);

                holder.planImageRLID.setBackground(gd);
                holder.viewRLID.setBackground(gd);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

        }*/

        String title = availabilityList.get(i).getActivity_count();
        holder.iconTitleID.setText(Utilities.CapitalText(title));



        // Create an animation instance
       /* Animation an = new RotateAnimation(0.0f, 360.0f);

        // Set the animation's parameters
        an.setDuration(4000);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.RELATIVE_TO_PARENT); // reverses each repeat
        an.setFillAfter(true);               // keep rotation after animation

        // Aply animation to image view
        holder.iconTitleID.setAnimation(an);*/

       /* holder.clickID.setOnClickListener(v -> {

            Log.e("clicked==>","");
            String s = availabilityList.get(i).getActivity_name();
            String[] parts = s.split("\\("); // escape .
            String part1 = parts[0];
            Intent intent = new Intent(activity, NewLeadsDataActivity.class);
            intent.putExtra("ACTIVITY_ID", availabilityList.get(i).getActivity_id());
            intent.putExtra("ACTIVITY_TITLE", "" + part1);
            intent.putExtra(AppConstants.VIEW_TYPE, "ACTIVITIES");
            intent.putExtra("TITLE", "" + part1);
            activity.startActivity(intent);
        });*/

        holder.imageViewIcon.setOnClickListener(v -> {

            Log.e("clicked==>","");
            String s = availabilityList.get(i).getActivity_name();
            String[] parts = s.split("\\("); // escape .
            String part1 = parts[0];
            Intent intent = new Intent(activity, NewLeadsDataActivity.class);
            intent.putExtra("ACTIVITY_ID", availabilityList.get(i).getActivity_id());
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
        AppCompatImageView imageViewbg;
        ImageView activityIconID1;

        public PreSalesVH(@NonNull View itemView) {
            super(itemView);
            textID = (TextView) itemView.findViewById(R.id.textViewName);
            clickID = (CardView)itemView.findViewById(R.id.clickID);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            //viewRLID = itemView.findViewById(R.id.viewRLID);
            iconTitleID = (TextView)itemView.findViewById(R.id.iconTitleID);
            activitiesLLID = (LinearLayout)itemView.findViewById(R.id.activitiesLLID);
            imageViewbg=itemView.findViewById(R.id.imageViewbg);
            //planImageRLID = itemView.findViewById(R.id.planImageRLID);
        }
    }
}
