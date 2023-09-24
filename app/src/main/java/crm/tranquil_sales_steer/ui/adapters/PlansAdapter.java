package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.PlansResponse;

public class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.ShortcutsVH> {

    ArrayList<PlansResponse> shortcutsResponses = new ArrayList<>();
    Activity activity;
    PlansClickListener shortcutsClickListener;

    String[] startColors = {"#756FC0", "#006BFF", "#00C0CB", "#E23132", "#A25C97", "#DBA712", "#20AADD",
            "#D85741", "#756FC0","#006BFF"};

    String[] endColor = {"#8A66BA", "#0038EA", "#00D797", "#CB1314", "#A53A94", "#BA8D0B", "#00A2DD",
            "#CF462E", "#8A66BA","#0038EA"};

    public int[] getGcolors() {
        String color = "";
        String color2 = "";

        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(startColors.length);

        color = startColors[randomNumber];
        color2 = endColor[randomNumber];
        int colorAsInt = Color.parseColor(color);
        int colorAsInt2 = Color.parseColor(color2);

        return new int[] {colorAsInt, colorAsInt2};
    }

    public PlansAdapter(ArrayList<PlansResponse> shortcutsResponses, Activity activity, PlansClickListener shortcutsClickListener) {
        this.shortcutsResponses = shortcutsResponses;
        this.activity = activity;
        this.shortcutsClickListener = shortcutsClickListener;
    }

    @NonNull
    @Override
    public ShortcutsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShortcutsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.plans_list_item,parent,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ShortcutsVH holder, int position) {

        PlansResponse item = shortcutsResponses.get(position);

        Picasso.with(activity).load(item.getImage()).into(holder.shortcutsIVID);


        holder.shortcutsTVID.setText(item.getName());

        /*int[] gColors = getGcolors();

        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {gColors[1], gColors[0]});
        gd2.setCornerRadius(0f);

        holder.shortcutsBackgroundRLID.setBackground(gd2);*/

      /*  if (shortcutsResponses.get(position).getColor_start() !=null && shortcutsResponses.get(position).getColor_end()!= null){

            try {
                int[] colors = {Color.parseColor(shortcutsResponses.get(position).getColor_start()),Color.parseColor(shortcutsResponses.get(position).getColor_end())};

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                gd.setCornerRadius(0f);

                holder.shortcutsBackgroundRLID.setBackground(gd);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

        }*/

       /* if (shortcutsResponses.get(position).getColor_light() !=null && shortcutsResponses.get(position).getColor_light2()!= null){

            try {
                int[] colors = {Color.parseColor(shortcutsResponses.get(position).getColor_light()),Color.parseColor(shortcutsResponses.get(position).getColor_light2())};

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                gd.setCornerRadius(0f);


                holder.planImageRLID.setBackground(gd);




            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

        }*/

        /*if (shortcutsResponses.get(position).getColor_light() !=null && shortcutsResponses.get(position).getColor_light2()!= null){

            try {
                int[] colors = {Color.parseColor(shortcutsResponses.get(position).getColor_light()),Color.parseColor(shortcutsResponses.get(position).getColor_light2())};

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
                gd.setCornerRadius(0f);



                holder.viewRLID.setBackground(gd);



            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

        }*/


        holder.shortcutsRLID.setOnClickListener(v -> shortcutsClickListener.onPlansItemClicked(item,position,v,holder));

    }

    @Override
    public int getItemCount() {
        return shortcutsResponses.size();
    }

    public static class ShortcutsVH extends RecyclerView.ViewHolder {

        AppCompatTextView shortcutsTVID;
        AppCompatImageView shortcutsIVID,shortcutsBackgroundIVID,planImageRLID;
        RelativeLayout shortcutsBackgroundRLID,shortcutsRLID,viewRLID;
        CardView planCVID;

        public ShortcutsVH(@NonNull View itemView) {
            super(itemView);

            shortcutsTVID = itemView.findViewById(R.id.shortcutsTVID);
            shortcutsIVID = itemView.findViewById(R.id.shortcutsIVID);
            shortcutsBackgroundIVID = itemView.findViewById(R.id.shortcutsBackgroundIVID);
            shortcutsBackgroundRLID = itemView.findViewById(R.id.shortcutsBackgroundRLID);
            viewRLID = itemView.findViewById(R.id.viewRLID);
            shortcutsRLID = itemView.findViewById(R.id.shortcutsRLID);
            planImageRLID = itemView.findViewById(R.id.planImageRLID);
            planCVID = itemView.findViewById(R.id.planCVID);

        }
    }

    public interface PlansClickListener {
        void onPlansItemClicked(PlansResponse shortcutsResponse, int position, View v, ShortcutsVH holder);
    }
}
