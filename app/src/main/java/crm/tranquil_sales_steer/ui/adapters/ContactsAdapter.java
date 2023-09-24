package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.ContactsModel;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    ArrayList<ContactsModel> contactsModels = new ArrayList<>();
    Activity activity;

    public ContactsAdapter(ArrayList<ContactsModel> contactsModels, Activity activity) {
        this.contactsModels = contactsModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.contactNameTVID.setText(contactsModels.get(position).getName());
        holder.contactNumberTVID.setText(contactsModels.get(position).getNumber());

    }

    @Override
    public int getItemCount() {
        return contactsModels.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView contactNameTVID,contactNumberTVID;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            contactNameTVID = itemView.findViewById(R.id.contactNameTVID);
            contactNumberTVID = itemView.findViewById(R.id.contactNumberTVID);
        }
    }

}
