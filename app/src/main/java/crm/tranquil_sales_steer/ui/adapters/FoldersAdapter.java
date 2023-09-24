package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.folders.SelectFolderActivity;
import crm.tranquil_sales_steer.ui.models.FoldersResponse;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FolderVH> {

    ArrayList<FoldersResponse> foldersResponses = new ArrayList<>();
    Activity activity;

    public FoldersAdapter(ArrayList<FoldersResponse> foldersResponses, Activity activity) {
        this.foldersResponses = foldersResponses;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FolderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FolderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.folders_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FolderVH holder, int position) {

        try {
            holder.folderTVID.setText(foldersResponses.get(position).getFolder_title());
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.folderRLID.setOnClickListener(v -> {
            Intent intent = new Intent(activity, SelectFolderActivity.class);
            intent.putExtra("FOLDER_ID",foldersResponses.get(position).getFolder_id());
            intent.putExtra("FOLDER_NAME",foldersResponses.get(position).getFolder_title());
            activity.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return foldersResponses.size();
    }

    public class FolderVH extends RecyclerView.ViewHolder{

        AppCompatTextView folderTVID;
        RelativeLayout folderRLID;

        public FolderVH(@NonNull View itemView) {
            super(itemView);

            folderTVID = itemView.findViewById(R.id.folderTVID);
            folderRLID = itemView.findViewById(R.id.folderRLID);
        }
    }
}
