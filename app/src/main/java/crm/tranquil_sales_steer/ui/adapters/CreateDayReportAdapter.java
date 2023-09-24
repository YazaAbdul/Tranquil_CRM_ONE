package crm.tranquil_sales_steer.ui.adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.models.CreateDayreportResponse;

public class CreateDayReportAdapter extends RecyclerView.Adapter<CreateDayReportAdapter.DayReportVH> {

    Activity activity;
    ArrayList<CreateDayreportResponse> createDayreportResponseArrayList = new ArrayList<>();

    String userID,melaID;

    ArrayList<String> ExaNameArray=new ArrayList<String>();
    ArrayList<String> ExaAmtArray=new ArrayList<String>();

    public CreateDayReportAdapter(Activity activity, ArrayList<CreateDayreportResponse> createDayreportResponseArrayList) {
        this.activity = activity;
        this.createDayreportResponseArrayList = createDayreportResponseArrayList;

    }


    @NonNull
    @Override
    public CreateDayReportAdapter.DayReportVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DayReportVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.createdate_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CreateDayReportAdapter.DayReportVH holder, int position) {
        holder.socialmediaTVID.setText(createDayreportResponseArrayList.get(position).getActivity_name());
        holder.exptedvalueTVID.setText(createDayreportResponseArrayList.get(position).getDaily_tgt());
        int id= Integer.parseInt(createDayreportResponseArrayList.get(position).getActivity_id());
            holder.realvalueTVID.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    try{

                        for(int i=0;i<=id;i++){
                            if (i!=id){
                                ExaNameArray.add("0");
                                ExaAmtArray.add("0");
                            }else{
                                ExaNameArray.add("0");
                                ExaAmtArray.add("0");
                                ExaNameArray.set(id,createDayreportResponseArrayList.get(id).getActivity_id());


                            }


                        }
                        Log.e("ExaName", ExaNameArray.toString());

                    }catch (Exception e){

                    }

                }
            });
                }

    @Override
    public int getItemCount() {
        return createDayreportResponseArrayList.size();
    }

    public class DayReportVH extends RecyclerView.ViewHolder {
        AppCompatTextView socialmediaTVID,exptedvalueTVID;
        AppCompatEditText realvalueTVID;

        public DayReportVH(@NonNull View itemView) {
            super(itemView);
            socialmediaTVID=itemView.findViewById(R.id.socialmediaTVID);
            exptedvalueTVID=itemView.findViewById(R.id.exptedvalueTVID);
            realvalueTVID=itemView.findViewById(R.id.realvalueTVID);

        }
    }
}
