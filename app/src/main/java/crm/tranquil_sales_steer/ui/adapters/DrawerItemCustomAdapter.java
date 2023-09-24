package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.DataModel;

/**
 * Created by venkei on 15-May-18.
 */

public class DrawerItemCustomAdapter extends ArrayAdapter<DataModel> {

    private Context mContext;
    private int layoutResourceId;
    private DataModel data[] = null;

    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, List<DataModel> data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data.toArray(new DataModel[0]);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);
        ImageView imageViewIcon = listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = listItem.findViewById(R.id.textViewName);
        DataModel folder = data[position];
        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);
        return listItem;
    }
}
