package crm.tranquil_sales_steer.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by venkei on 29-Jun-19.
 */
public class LeadCommunicationFragment extends Fragment {

    ArrayList<CommunicationResponse> phoneResponse = new ArrayList<>();
    ArrayList<CommunicationResponse> smsResponse = new ArrayList<>();
    ArrayList<CommunicationResponse> whatsAppResponse = new ArrayList<>();
    ArrayList<CommunicationResponse> emailResponse = new ArrayList<>();
    CallsAdapter phoneAdapter;
    EmailCommunicationAdapter emailCommunicationAdapter;
    SmsCommunicationAdapter smsCommunicationAdapter;
    WhatsAppAdapter whatsAppAdapter;
    RecyclerView emailEXPGVID,whatsAppEXPGVID,callsEXPGVID, smsEXPGVID;
    ProgressBar callsPB, smsPB, whatsAppPB, emailPB;
    LinearLayout noCalls, noSms, noWhatsApp, noEmails;
    CardView whatsappCVID;

    private static final String ARG_KEY_LEAD_ID = "lead_id";
    String VALUE_LEAD_ID;
    public LeadCommunicationFragment newInstance(String customerId) {

        LeadCommunicationFragment subFrag = new LeadCommunicationFragment();
        Bundle b = new Bundle();
        b.putString(ARG_KEY_LEAD_ID, customerId);
        subFrag.setArguments(b);
        return subFrag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VALUE_LEAD_ID = getArguments().getString(ARG_KEY_LEAD_ID);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lead_communications, container, false);

        callsEXPGVID = view.findViewById(R.id.callsEXPGVID);
        smsEXPGVID = view.findViewById(R.id.smsEXPGVID);
        whatsAppEXPGVID = view.findViewById(R.id.whatsAppEXPGVID);
        emailEXPGVID = view.findViewById(R.id.emailEXPGVID);
        whatsappCVID = view.findViewById(R.id.whatsappCVID);

        callsPB = view.findViewById(R.id.callsPB);
        smsPB = view.findViewById(R.id.smsPB);
        whatsAppPB = view.findViewById(R.id.whatsAppPB);
        emailPB = view.findViewById(R.id.emailPB);

        callsPB.setVisibility(View.GONE);
        smsPB.setVisibility(View.GONE);
        whatsAppPB.setVisibility(View.GONE);
        emailPB.setVisibility(View.GONE);

        noCalls = view.findViewById(R.id.noCalls);
        noSms = view.findViewById(R.id.noSms);
        noWhatsApp = view.findViewById(R.id.noWhatsApp);
        noEmails = view.findViewById(R.id.noEmails);

        noCalls.setVisibility(View.GONE);
        noSms.setVisibility(View.GONE);
        noWhatsApp.setVisibility(View.GONE);
        noEmails.setVisibility(View.GONE);
        String planType = MySharedPreferences.getPreferences(getContext(),"PLAN_TYPE");

        if (Utilities.isNetworkAvailable(getActivity())) {
            loadCalls();
            loadEmail();
            loadWhatsApp();
            loadSms();
        } else {
            Utilities.AlertDaiolog(getActivity(), "No Internet...!", "Please check your internet connection", 1, null, "OK");
        }

        if (planType.equals("1")){
            whatsappCVID.setVisibility(View.VISIBLE);
        }else{
            whatsappCVID.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utilities.isNetworkAvailable(getActivity())) {
            loadCalls();
            loadEmail();
            loadWhatsApp();
            loadSms();
        } else {
            Utilities.AlertDaiolog(getActivity(), "No Internet...!", "Please check your internet connection", 1, null, "OK");
        }
    }

    private void loadCalls() {
        callsPB.setVisibility(View.VISIBLE);
        noCalls.setVisibility(View.GONE);
        callsEXPGVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<CommunicationResponse>> call = apiInterface.getAllCalls(VALUE_LEAD_ID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<CommunicationResponse>> call, Response<ArrayList<CommunicationResponse>> response) {
                callsPB.setVisibility(View.GONE);
                noCalls.setVisibility(View.GONE);
                callsEXPGVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    phoneResponse = response.body();
                    if (phoneResponse.size() > 0) {
                        for (int i = 0; i < phoneResponse.size(); i++) {
                            try {
                                phoneAdapter = new CallsAdapter(getActivity(), phoneResponse);
                                callsEXPGVID.setAdapter(phoneAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        noCalls.setVisibility(View.VISIBLE);
                    }
                } else {
                    noCalls.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<CommunicationResponse>> call, Throwable t) {
                callsPB.setVisibility(View.GONE);
                noCalls.setVisibility(View.VISIBLE);
                callsEXPGVID.setVisibility(View.GONE);
            }
        });
    }

    private void loadSms() {
        smsPB.setVisibility(View.VISIBLE);
        noSms.setVisibility(View.GONE);
        smsEXPGVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<CommunicationResponse>> call = apiInterface.getAllSms(VALUE_LEAD_ID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<CommunicationResponse>> call, Response<ArrayList<CommunicationResponse>> response) {
                smsPB.setVisibility(View.GONE);
                noSms.setVisibility(View.GONE);
                smsEXPGVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    smsResponse = response.body();
                    if (smsResponse.size() > 0) {
                        for (int i = 0; i < smsResponse.size(); i++) {
                            try {
                                smsCommunicationAdapter = new SmsCommunicationAdapter(getActivity(), smsResponse);
                                smsEXPGVID.setAdapter(smsCommunicationAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        noSms.setVisibility(View.VISIBLE);
                    }
                } else {
                    noSms.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<CommunicationResponse>> call, Throwable t) {
                smsPB.setVisibility(View.GONE);
                noSms.setVisibility(View.VISIBLE);
                smsEXPGVID.setVisibility(View.GONE);
            }
        });
    }

    private void loadWhatsApp() {
        whatsAppPB.setVisibility(View.VISIBLE);
        noWhatsApp.setVisibility(View.GONE);
        whatsAppEXPGVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<CommunicationResponse>> call = apiInterface.getAllWhatsApp(VALUE_LEAD_ID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<CommunicationResponse>> call, Response<ArrayList<CommunicationResponse>> response) {
                whatsAppPB.setVisibility(View.GONE);
                noWhatsApp.setVisibility(View.GONE);
                whatsAppEXPGVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    whatsAppResponse = response.body();
                    if (whatsAppResponse.size() > 0) {
                        for (int i = 0; i < whatsAppResponse.size(); i++) {
                            try {
                                whatsAppAdapter = new WhatsAppAdapter(getActivity(), whatsAppResponse);
                                whatsAppEXPGVID.setAdapter(whatsAppAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        noWhatsApp.setVisibility(View.VISIBLE);
                    }
                } else {
                    noWhatsApp.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<CommunicationResponse>> call, Throwable t) {
                whatsAppPB.setVisibility(View.GONE);
                noWhatsApp.setVisibility(View.VISIBLE);
                whatsAppEXPGVID.setVisibility(View.GONE);
            }
        });
    }

    private void loadEmail() {
        callsPB.setVisibility(View.VISIBLE);
        noEmails.setVisibility(View.GONE);
        emailEXPGVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<CommunicationResponse>> call = apiInterface.getAllEmail(VALUE_LEAD_ID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<CommunicationResponse>> call, Response<ArrayList<CommunicationResponse>> response) {
                emailPB.setVisibility(View.GONE);
                noEmails.setVisibility(View.GONE);
                emailEXPGVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    emailResponse = response.body();
                    if (emailResponse.size() > 0) {
                        for (int i = 0; i < emailResponse.size(); i++) {
                            try {
                                emailCommunicationAdapter = new EmailCommunicationAdapter(getActivity(), emailResponse);
                                emailEXPGVID.setAdapter(emailCommunicationAdapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        noEmails.setVisibility(View.VISIBLE);
                    }
                } else {
                    noEmails.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<CommunicationResponse>> call, Throwable t) {
                emailPB.setVisibility(View.GONE);
                noEmails.setVisibility(View.VISIBLE);
                emailEXPGVID.setVisibility(View.GONE);
            }
        });
    }

    public class CommunicationResponse {

        /*subject: "",
message: "",
created_date: "2019-07-25",
created_by: "20"*/

        private String subject;
        private String message;
        private String created_date;
        private String created_by;
        private String created_time;

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public CommunicationResponse(String subject, String message, String created_date, String created_by, String created_time) {
            this.subject = subject;
            this.message = message;
            this.created_date = created_date;
            this.created_by = created_by;
            this.created_time = created_time;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }
    }

    public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.CallVH> {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<CommunicationResponse> availabilityList = new ArrayList<>();

        public CallsAdapter(Activity activity, ArrayList<CommunicationResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }


        @NonNull
        @Override
        public CallVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CallVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.communications_calls_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull CallVH holder, int position) {

            holder.dateTVID.setText(availabilityList.get(position).getCreated_date());
            holder.timeTVID.setText(availabilityList.get(position).getCreated_time());
            holder.userNameTVID.setText(Utilities.CapitalText(availabilityList.get(position).getCreated_by()));

        }

        @Override
        public int getItemCount() {
            return availabilityList.size();
        }

        public class CallVH extends RecyclerView.ViewHolder{

            TextView dateTVID,timeTVID,userNameTVID;

            public CallVH(@NonNull View itemView) {
                super(itemView);

                dateTVID = itemView.findViewById(R.id.dateTVID);
                timeTVID = itemView.findViewById(R.id.timeTVID);
                userNameTVID = itemView.findViewById(R.id.userNameTVID);

            }
        }
    }

    public class WhatsAppAdapter extends RecyclerView.Adapter<WhatsAppAdapter.WhatsAppVH> {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<CommunicationResponse> availabilityList = new ArrayList<>();

        public WhatsAppAdapter(Activity activity, ArrayList<CommunicationResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }


        @NonNull
        @Override
        public WhatsAppVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WhatsAppVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.communication_message_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull WhatsAppVH holder, int position) {

            if (position % 2 == 1) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.edit_d));
            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }

            holder.dateTVID.setText(availabilityList.get(position).getCreated_date());
            holder.timeTVID.setText(availabilityList.get(position).getCreated_time());
            holder.userNameTVID.setText(Utilities.CapitalText(availabilityList.get(position).getCreated_by()));
            holder.messageTVID.setText(Utilities.CapitalText(availabilityList.get(position).getMessage()));

        }


        @Override
        public int getItemCount() {
            return availabilityList.size();
        }

        public class WhatsAppVH extends RecyclerView.ViewHolder{

            TextView dateTVID,timeTVID,userNameTVID,messageTVID,subjectTVID;

            public WhatsAppVH(@NonNull View itemView) {
                super(itemView);

                dateTVID = itemView.findViewById(R.id.dateTVID);
                timeTVID = itemView.findViewById(R.id.timeTVID);
                userNameTVID = itemView.findViewById(R.id.userNameTVID);
                messageTVID = itemView.findViewById(R.id.messageTVID);
                subjectTVID = itemView.findViewById(R.id.subjectTVID);

            }
        }
    }

    public class SmsCommunicationAdapter extends RecyclerView.Adapter<SmsCommunicationAdapter.SMSVH> {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<CommunicationResponse> availabilityList = new ArrayList<>();

        public SmsCommunicationAdapter(Activity activity, ArrayList<CommunicationResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }

        @NonNull
        @Override
        public SMSVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SMSVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.communication_message_list_item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull SMSVH holder, int position) {

            if (position % 2 == 1) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.edit_d));
            } else {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
            }

            holder.dateTVID.setText(availabilityList.get(position).getCreated_date());
            holder.timeTVID.setText(availabilityList.get(position).getCreated_time());
            holder.userNameTVID.setText(Utilities.CapitalText(availabilityList.get(position).getCreated_by()));
            holder.messageTVID.setText(Utilities.CapitalText(availabilityList.get(position).getMessage()));

        }


        @Override
        public int getItemCount() {
            return availabilityList.size();
        }

        public class SMSVH extends RecyclerView.ViewHolder{

            TextView dateTVID,timeTVID,userNameTVID,messageTVID,subjectTVID;

            public SMSVH(@NonNull View itemView) {
                super(itemView);

                dateTVID = itemView.findViewById(R.id.dateTVID);
                timeTVID = itemView.findViewById(R.id.timeTVID);
                userNameTVID = itemView.findViewById(R.id.userNameTVID);
                messageTVID = itemView.findViewById(R.id.messageTVID);
                subjectTVID = itemView.findViewById(R.id.subjectTVID);

            }
        }
    }

        public class EmailCommunicationAdapter extends RecyclerView.Adapter<EmailCommunicationAdapter.EmailVH> {
        private Activity activity;
        private LayoutInflater inflater;
        private ArrayList<CommunicationResponse> availabilityList = new ArrayList<>();

        public EmailCommunicationAdapter(Activity activity, ArrayList<CommunicationResponse> availabilityList) {
            this.activity = activity;
            this.availabilityList = availabilityList;
            inflater = LayoutInflater.from(activity);
        }


            @NonNull
            @Override
            public EmailVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new EmailVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.communication_message_list_item,parent,false));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(@NonNull EmailVH holder, int position) {


                if (position % 2 == 1) {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.edit_d));
                } else {
                    holder.itemView.setBackgroundColor(getResources().getColor(R.color.white));
                }

                holder.subjectTVID.setVisibility(View.VISIBLE);
                holder.dateTVID.setText(availabilityList.get(position).getCreated_date());
                holder.timeTVID.setText(availabilityList.get(position).getCreated_time());
                holder.userNameTVID.setText(Utilities.CapitalText(availabilityList.get(position).getCreated_by()));
                String message = Utilities.CapitalText(availabilityList.get(position).getMessage());
                holder.messageTVID.setText("Message : " + Html.fromHtml(Html.fromHtml(message).toString()));
                holder.subjectTVID.setText("Subject : " + Utilities.CapitalText(availabilityList.get(position).getSubject()));


            }


            @Override
            public int getItemCount() {
                return availabilityList.size();
            }



        public class EmailVH extends RecyclerView.ViewHolder{

            TextView dateTVID,timeTVID,userNameTVID,messageTVID,subjectTVID;

            public EmailVH(@NonNull View itemView) {
                super(itemView);

                dateTVID = itemView.findViewById(R.id.dateTVID);
                timeTVID = itemView.findViewById(R.id.timeTVID);
                userNameTVID = itemView.findViewById(R.id.userNameTVID);
                messageTVID = itemView.findViewById(R.id.messageTVID);
                subjectTVID = itemView.findViewById(R.id.subjectTVID);

            }
        }
    }
}
