package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.dashboard.TeleCallersDataActivity;
import crm.tranquil_sales_steer.ui.models.TeleSourceTypeResponse;

public class TeleSourceTypeAdapter extends RecyclerView.Adapter<TeleSourceTypeAdapter.TeleSourceVH> {

    ArrayList<TeleSourceTypeResponse> teleSourceTypeResponses = new ArrayList<>();
    Activity activity;

    public TeleSourceTypeAdapter(Activity activity,ArrayList<TeleSourceTypeResponse> teleSourceTypeResponses) {
        this.activity = activity;
        this.teleSourceTypeResponses = teleSourceTypeResponses;

    }

    @NonNull
    @Override
    public TeleSourceVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TeleSourceVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.tele_source_type_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TeleSourceVH holder, int position) {

        holder.teleSourceTVID.setText(teleSourceTypeResponses.get(position).getSource_name() + " (" +teleSourceTypeResponses.get(position).getCount() + ")");

        holder.teleSourceCVID.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TeleCallersDataActivity.class);
            intent.putExtra("SOURCE_ID",teleSourceTypeResponses.get(position).getTelecaller_source_id());
            intent.putExtra("TITLE",teleSourceTypeResponses.get(position).getSource_name()+ " (" +teleSourceTypeResponses.get(position).getCount() + ")");
            activity.startActivity(intent);
        });

        if (teleSourceTypeResponses.get(position).getCount() != null && teleSourceTypeResponses.get(position).getCount().equalsIgnoreCase("0")){

            holder.teleSourceCVID.setVisibility(View.GONE);

        }else {
            holder.teleSourceCVID.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return teleSourceTypeResponses.size();
    }

    public class TeleSourceVH extends RecyclerView.ViewHolder{

        CardView teleSourceCVID;
        AppCompatTextView teleSourceTVID;

        public TeleSourceVH(@NonNull View itemView) {
            super(itemView);

            teleSourceCVID = itemView.findViewById(R.id.teleSourceCVID);
            teleSourceTVID = itemView.findViewById(R.id.teleSourceTVID);
        }
    }
}
