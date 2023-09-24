package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.AllUploadFilesResponse;

public class AllUploadedFilesAdapter extends RecyclerView.Adapter<AllUploadedFilesAdapter.AllUploadedFilesVH> {

    ArrayList<AllUploadFilesResponse> allUploadFilesResponses = new ArrayList<>();
    Activity activity;
    AllUploadedFilesClickListener allUploadedFilesClickListener;

    public AllUploadedFilesAdapter(ArrayList<AllUploadFilesResponse> allUploadFilesResponses, Activity activity, AllUploadedFilesClickListener allUploadedFilesClickListener) {
        this.allUploadFilesResponses = allUploadFilesResponses;
        this.activity = activity;
        this.allUploadedFilesClickListener = allUploadedFilesClickListener;
    }

  /*  public AllUploadedFilesAdapter(ArrayList<AllUploadFilesResponse> allUploadFilesResponses, FragmentActivity activity, FolderFragment folderFragment) {
        this.allUploadFilesResponses = allUploadFilesResponses;
        this.activity = activity;
        this.allUploadedFilesClickListener = allUploadedFilesClickListener;
    }*/

  /*  public AllUploadedFilesAdapter(ArrayList<AllUploadFilesResponse> allUploadFilesResponses, SelectFolderActivity activity, SelectFolderActivity selectFolderActivity) {
        this.allUploadFilesResponses = allUploadFilesResponses;
        this.activity = activity;
        this.allUploadedFilesClickListener = allUploadedFilesClickListener;
    }*/

  /*  public AllUploadedFilesAdapter(ArrayList<AllUploadFilesResponse> allUploadFilesResponses, Activity activity) {
        this.allUploadFilesResponses = allUploadFilesResponses;
        this.activity = activity;

    }*/


    @NonNull
    @Override
    public AllUploadedFilesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllUploadedFilesVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_uploaded_files_item,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AllUploadedFilesVH holder, int position) {

        AllUploadFilesResponse item = allUploadFilesResponses.get(position);

        try {
            Picasso.with(activity).load(allUploadFilesResponses.get(position).getUpload_doc()).placeholder(R.drawable.user).into(holder.fileIVID);
            holder.fileNameTVID.setText(allUploadFilesResponses.get(position).getFile_name());
            holder.dateTVID.setText("Added : "+allUploadFilesResponses.get(position).getCreated_date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.shareRLID.setOnClickListener(v -> allUploadedFilesClickListener.onUploadedFilesClickItem(item,v,position,holder));
        holder.sendRLID.setOnClickListener(v -> allUploadedFilesClickListener.onUploadedFilesClickItem(item,v,position,holder));
        holder.deleteRLID.setOnClickListener(v -> allUploadedFilesClickListener.onUploadedFilesClickItem(item,v,position,holder));

    }

    @Override
    public int getItemCount() {
        return allUploadFilesResponses.size();
    }

    public class AllUploadedFilesVH extends RecyclerView.ViewHolder{

        AppCompatImageView fileIVID;
        AppCompatTextView fileNameTVID,dateTVID;
        RelativeLayout shareRLID,sendRLID,deleteRLID;

        public AllUploadedFilesVH(@NonNull View itemView) {
            super(itemView);

            fileIVID = itemView.findViewById(R.id.fileIVID);
            fileNameTVID = itemView.findViewById(R.id.fileNameTVID);
            shareRLID = itemView.findViewById(R.id.shareRLID);
            sendRLID = itemView.findViewById(R.id.sendRLID);
            deleteRLID = itemView.findViewById(R.id.deleteRLID);
            dateTVID = itemView.findViewById(R.id.dateTVID);
        }
    }

    public interface AllUploadedFilesClickListener{
        void onUploadedFilesClickItem(AllUploadFilesResponse response,  View v, int pos,AllUploadedFilesVH holder);
    }
}
