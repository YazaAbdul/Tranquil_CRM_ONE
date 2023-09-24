package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.NotesResponse;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesVH> {

    ArrayList<NotesResponse> notesResponses = new ArrayList<>();
    Activity activity;

    public NotesAdapter(ArrayList<NotesResponse> notesResponses, Activity activity) {
        this.notesResponses = notesResponses;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NotesVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotesVH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_recycler_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesVH notesVH, int i) {

        Picasso.with(activity).load(notesResponses.get(i).getProfilepic()).placeholder(R.drawable.profpic).into(notesVH.profileIVID);
        notesVH.userNameTVID.setText(notesResponses.get(i).getCreated_name());
        notesVH.dateTVID.setText(notesResponses.get(i).getCreated_date());
        notesVH.notesTVID.setText(notesResponses.get(i).getPost());



    }

    @Override
    public int getItemCount() {
        return notesResponses.size();
    }

    public class NotesVH extends RecyclerView.ViewHolder{
        CircleImageView profileIVID;
        TextView notesTVID,userNameTVID,dateTVID;
        public NotesVH(@NonNull View itemView) {
            super(itemView);

            notesTVID = itemView.findViewById(R.id.notesTVID);
            profileIVID = itemView.findViewById(R.id.profileIVID);
            dateTVID = itemView.findViewById(R.id.dateTVID);
            userNameTVID = itemView.findViewById(R.id.userNameTVID);

        }
    }
}
