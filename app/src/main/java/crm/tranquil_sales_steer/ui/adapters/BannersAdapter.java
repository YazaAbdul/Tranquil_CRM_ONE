package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.CardsResponse;

public class BannersAdapter extends RecyclerView.Adapter<BannersAdapter.ItemViewHolder>{

    ArrayList<CardsResponse> cardsResponse = new ArrayList<>();
    Activity activity;


    public BannersAdapter(ArrayList<CardsResponse> cardsResponse, Activity activity) {
        this.cardsResponse = cardsResponse;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_banners_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        itemViewHolder.cardNameTVID.setText(cardsResponse.get(i).getCard_name());
        itemViewHolder.dayTVID.setText(cardsResponse.get(i).getP2());
        itemViewHolder.dayLogicTVID.setText(cardsResponse.get(i).getP2logic());
        itemViewHolder.monthTVID.setText(cardsResponse.get(i).getP1());
        itemViewHolder.monthLogicTVID.setText(cardsResponse.get(i).getP1logic());
        itemViewHolder.activeTVID.setText(cardsResponse.get(i).getP3());
        itemViewHolder.activeLogicTVID.setText(cardsResponse.get(i).getP3logic());
        Picasso.with(activity).load(cardsResponse.get(i).getIcons()).into(itemViewHolder.bannerIVIID);
        Picasso.with(activity).load(cardsResponse.get(i).getCard_image()).into(itemViewHolder.imageViewbg);

      /*  Picasso.with(activity).load(item.getActivity_image()).into( itemVH.imageViewbg);*/
        //itemViewHolder.bannerCardsRLID.setBackgroundColor(Color.parseColor().get(i).getP3logic());

        /*int h = itemViewHolder.bannerCardsRLID.getHeight();
        ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
        //mDrawable.getPaint().setShader(new LinearGradient(270, 0, 0, h, Color.parseColor(cardsResponse.get(i).getColor_one()), Color.parseColor(cardsResponse.get(i).getColor_two()), Shader.TileMode.REPEAT));
        mDrawable.getPaint().setShader(new LinearGradient(270, 0, 0, h, Color.parseColor(cardsResponse.get(i).getColor_one()), Color.parseColor(cardsResponse.get(i).getColor_two()), Shader.TileMode.REPEAT));
        //itemViewHolder.bannerCardsRLID.setBackgroundDrawable(mDrawable);
        itemViewHolder.bannerCardsRLID.setBackgroundColor(Color.parseColor(cardsResponse.get(i).getColor_one()));*/

    /*   int[] colors = {Color.parseColor(cardsResponse.get(i).getColor_one()),Color.parseColor(cardsResponse.get(i).getColor_two())};
        int[] colors2 = {Color.parseColor(cardsResponse.get(i).getC_one()),Color.parseColor(cardsResponse.get(i).getC_two())};


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, colors);
        gd.setCornerRadius(0f);

        GradientDrawable gd2 = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT, colors2);
        gd.setCornerRadius(0f);*/

       /* itemViewHolder.bannerCardsRLID.setBackground(gd);*/

     /* itemViewHolder.overallLLID.setBackground(gd2);
        itemViewHolder.todayLLID.setBackground(gd2);
        itemViewHolder.monthLLID.setBackground(gd2);
        itemViewHolder.todayLLID.setAlpha(0.7f);
        itemViewHolder.monthLLID.setAlpha(0.6f);
        itemViewHolder.overallLLID.setAlpha(0.6f);*/




        //Picasso.with(activity).load(cardsResponse.get(i).getImage()).into((Target) itemViewHolder.bannerCardsRLID);

    }

    @Override
    public int getItemCount() {
        return cardsResponse.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout bannerCardsRLID;
        TextView cardNameTVID,dayTVID,dayLogicTVID,monthTVID,monthLogicTVID,activeTVID,activeLogicTVID;
        ImageView bannerIVIID;
        LinearLayout overallLLID,todayLLID;
       LinearLayout monthLLID;
        AppCompatImageView imageViewbg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerCardsRLID = itemView.findViewById(R.id.bannerCardsRLID);
            cardNameTVID = itemView.findViewById(R.id.cardNameTVID);
            dayTVID = itemView.findViewById(R.id.dayTVID);
            dayLogicTVID = itemView.findViewById(R.id.dayLogicTVID);
            monthTVID = itemView.findViewById(R.id.monthTVID);
            monthLogicTVID = itemView.findViewById(R.id.monthLogicTVID);
            activeTVID = itemView.findViewById(R.id.activeTVID);
            activeLogicTVID = itemView.findViewById(R.id.activeLogicTVID);
            bannerIVIID = itemView.findViewById(R.id.bannerIVIID);
            overallLLID = itemView.findViewById(R.id.overallLLID);
            todayLLID = itemView.findViewById(R.id.todayLLID);
            monthLLID = itemView.findViewById(R.id.monthLLID);
            imageViewbg=itemView.findViewById(R.id.imageViewbg);

        }
    }
}
