package crm.tranquil_sales_steer.domain.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.LeadHistoryActivity;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallersDialogActivity extends AppCompatActivity implements View.OnClickListener {
    Dialog dialog;
    String comapanyID, leadID;
    ProgressBar detailsPB, tagsPB;
    RecyclerView tagsRCID;
    RelativeLayout tagsRLID;
    ArrayList<TagsDisplayResponse> tagsResponse = new ArrayList<>();
    TagsDisplayAdapter tagsAdapter;
    RelativeLayout noTagsTVID;
    LinearLayoutManager linearLayoutManager;
    TextView leadNameTVID, leadDetails1TVID, leadDetails2TVID, leadDetails3TVID;
    RelativeLayout canelRLID;
    LinearLayout callLLID, EditLLID, messageLLID, emailLLID;
    String numberStr, emailStr, nameStr;
    ImageView imageDrawable;
    Button viewLeadDetailsBtn;

    ArrayList<LeadRequirementInfo> userRequirements = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_callers_dialog);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                comapanyID = bundle.getString("COMPANY_ID");
                leadID = bundle.getString("LEAD_ID");
            }
        }

        Log.d("INCOMING_DETAILS : ", "" + leadID + " , " + comapanyID);

        showAlert();

        loadTags(leadID);
        loadDetails(leadID);
    }

    private void showAlert() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_crm_number_details);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        tagsPB = dialog.findViewById(R.id.tagsPB);
        noTagsTVID = dialog.findViewById(R.id.noTagsTVID);
        tagsRCID = dialog.findViewById(R.id.tagsRCID);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagsRCID.setLayoutManager(linearLayoutManager);
        tagsRCID.setItemAnimator(new DefaultItemAnimator());
        tagsAdapter = new TagsDisplayAdapter(this, tagsResponse);
        tagsRCID.setAdapter(tagsAdapter);
        imageDrawable = dialog.findViewById(R.id.imageDrawable);

        leadNameTVID = dialog.findViewById(R.id.leadNameTVID);
        leadDetails1TVID = dialog.findViewById(R.id.leadDetails1TVID);
        leadDetails2TVID = dialog.findViewById(R.id.leadDetails2TVID);
        leadDetails3TVID = dialog.findViewById(R.id.leadDetails3TVID);

        callLLID = dialog.findViewById(R.id.callLLID);
        EditLLID = dialog.findViewById(R.id.EditLLID);
        messageLLID = dialog.findViewById(R.id.messageLLID);
        emailLLID = dialog.findViewById(R.id.emailLLID);
        canelRLID = dialog.findViewById(R.id.canelRLID);
        viewLeadDetailsBtn = dialog.findViewById(R.id.viewLeadDetailsBtn);

        callLLID.setOnClickListener(this);
        EditLLID.setOnClickListener(this);
        messageLLID.setOnClickListener(this);
        emailLLID.setOnClickListener(this);
        canelRLID.setOnClickListener(this);
        viewLeadDetailsBtn.setOnClickListener(this);
    }


    private void loadDetails(String leadID) {
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<LeadInformationResponse>> call = apiInterface.getCrmNumberDetails(leadID);
        call.enqueue(new Callback<ArrayList<LeadInformationResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<LeadInformationResponse>> call, Response<ArrayList<LeadInformationResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    ArrayList<LeadInformationResponse> informationResponses = response.body();
                    if (informationResponses.size() > 0) {
                        Log.d("USER_DETAILS_GETTING", "COMING");

                        userRequirements = informationResponses.get(0).getLead_requirement();

                        for (int j = 0; j < informationResponses.size(); j++) {

                        }

                        for (int i = 0; i < informationResponses.size(); i++) {
                            leadNameTVID.setText(informationResponses.get(i).getLead_name());
                            leadDetails1TVID.setText(informationResponses.get(i).getLead_mobile() + " , " + informationResponses.get(i).getLead_email());
                            leadDetails2TVID.setText(informationResponses.get(i).getLead_location() + " , ");
                            leadDetails3TVID.setText(informationResponses.get(i).getLead_company_name() + ".");

                            numberStr = informationResponses.get(i).getLead_mobile();
                            emailStr = informationResponses.get(i).getLead_email();
                            nameStr = informationResponses.get(i).getLead_name();

                            String pic = informationResponses.get(i).getLead_pic();
                            if (pic.equalsIgnoreCase(AppConstants.GLOBAL_MAIN_URL_FOR_ICONS + "lead_pics/")) {
                                Picasso.with(CallersDialogActivity.this).load(R.drawable.userpic).into(imageDrawable);
                            } else {
                                Picasso.with(CallersDialogActivity.this).load(pic).into(imageDrawable);
                            }
                        }
                    } else {
                        Log.d("USER_DETAILS_GETTING", "NOT_COMING");
                    }
                } else {
                    Log.d("USER_DETAILS_GETTING", "NOT_COMING");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LeadInformationResponse>> call, Throwable t) {
                Log.d("USER_DETAILS_GETTING", "NOT_COMING");

            }
        });

    }

    private void loadTags(String leadID) {
        tagsRCID.setVisibility(View.GONE);
        tagsPB.setVisibility(View.VISIBLE);
        noTagsTVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);

        Call<ArrayList<TagsDisplayResponse>> call = apiInterface.getCrmNumberTags(leadID);
        call.enqueue(new Callback<ArrayList<TagsDisplayResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<TagsDisplayResponse>> call, Response<ArrayList<TagsDisplayResponse>> response) {
                tagsPB.setVisibility(View.GONE);
                tagsRCID.setVisibility(View.VISIBLE);
                Log.d("USER_TAG_GETTING", "COMING");
                if (response.body() != null && response.code() == 200) {
                    tagsResponse = response.body();
                    if (tagsResponse.size() > 0) {
                        tagsAdapter = new TagsDisplayAdapter(CallersDialogActivity.this, tagsResponse);
                        tagsRCID.setAdapter(tagsAdapter);
                        tagsAdapter.setTagsList(tagsResponse);
                    } else {
                        tagsRCID.setVisibility(View.GONE);
                        noTagsTVID.setVisibility(View.VISIBLE);

                    }
                } else {
                    tagsRCID.setVisibility(View.GONE);
                    noTagsTVID.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<TagsDisplayResponse>> call, Throwable t) {
                tagsPB.setVisibility(View.GONE);
                tagsRCID.setVisibility(View.GONE);
                noTagsTVID.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.callLLID:
                numberCalling(numberStr);
                dialog.dismiss();
                break;

            case R.id.EditLLID:
                Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                i.putExtra(ContactsContract.Intents.Insert.NAME, nameStr);
                i.putExtra(ContactsContract.Intents.Insert.PHONE, numberStr);
                startActivity(i);
                break;

            case R.id.messageLLID:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", numberStr, null));
                intent.putExtra("sms_body", "");
                startActivity(intent);
                //dialog.dismiss();
                break;

            case R.id.emailLLID:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailStr});
                emailIntent.putExtra(Intent.EXTRA_CHOSEN_COMPONENT_INTENT_SENDER, new String[]{emailStr});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches)
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                        best = info;
                if (best != null)
                    emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                startActivity(emailIntent);
                //dialog.dismiss();
                break;

            case R.id.canelRLID:
                dialog.dismiss();
                finish();
                break;

            case R.id.viewLeadDetailsBtn:
                Intent intent1 = new Intent(this, LeadHistoryActivity.class);
                intent1.putExtra("CUSTOMER_ID", "" + leadID);
                intent1.putExtra("CUSTOMER_NAME", "" + nameStr);
                intent1.putExtra("CUSTOMER_MOBILE", "" + numberStr);
                intent1.putExtra("pageFrom", false);
                startActivity(intent1);
                dialog.dismiss();
                finish();
                break;
        }
    }

    private void numberCalling(String number) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);

        }
    }

    public class LeadInformationResponse {
        private String lead_id;
        private String lead_name;
        private String lead_last_name;
        private String lead_email;
        private String lead_mobile;
        private ArrayList<LeadRequirementInfo> lead_requirement = new ArrayList<>();

        public ArrayList<LeadRequirementInfo> getLead_requirement() {
            return lead_requirement;
        }

        public void setLead_requirement(ArrayList<LeadRequirementInfo> lead_requirement) {
            this.lead_requirement = lead_requirement;
        }

        private String lead_source;
        private String lead_website;
        private String lead_location;
        private String lead_company_name;
        private String lead_pic;

        public String getLead_pic() {
            return lead_pic;
        }

        public void setLead_pic(String lead_pic) {
            this.lead_pic = lead_pic;
        }

        public String getLead_id() {
            return lead_id;
        }

        public void setLead_id(String lead_id) {
            this.lead_id = lead_id;
        }

        public String getLead_name() {
            return lead_name;
        }

        public void setLead_name(String lead_name) {
            this.lead_name = lead_name;
        }

        public String getLead_last_name() {
            return lead_last_name;
        }

        public void setLead_last_name(String lead_last_name) {
            this.lead_last_name = lead_last_name;
        }

        public String getLead_email() {
            return lead_email;
        }

        public void setLead_email(String lead_email) {
            this.lead_email = lead_email;
        }

        public String getLead_mobile() {
            return lead_mobile;
        }

        public void setLead_mobile(String lead_mobile) {
            this.lead_mobile = lead_mobile;
        }


        public String getLead_source() {
            return lead_source;
        }

        public void setLead_source(String lead_source) {
            this.lead_source = lead_source;
        }

        public String getLead_website() {
            return lead_website;
        }

        public void setLead_website(String lead_website) {
            this.lead_website = lead_website;
        }

        public String getLead_location() {
            return lead_location;
        }

        public void setLead_location(String lead_location) {
            this.lead_location = lead_location;
        }

        public String getLead_company_name() {
            return lead_company_name;
        }

        public void setLead_company_name(String lead_company_name) {
            this.lead_company_name = lead_company_name;
        }
    }

    public class LeadRequirementInfo {
        /*req_name: "Mobile App",
req_id: "3821"*/

        private String req_name;
        private String req_id;

        public String getReq_name() {
            return req_name;
        }

        public void setReq_name(String req_name) {
            this.req_name = req_name;
        }

        public String getReq_id() {
            return req_id;
        }

        public void setReq_id(String req_id) {
            this.req_id = req_id;
        }
    }

    public class TagsDisplayResponse implements Serializable {
        private String lead_tag_id;
        private String tag_title;
        private String tag_color;

        public TagsDisplayResponse(String lead_tag_id, String tag_title, String tag_color) {
            this.lead_tag_id = lead_tag_id;
            this.tag_title = tag_title;
            this.tag_color = tag_color;
        }

        public String getLead_tag_id() {
            return lead_tag_id;
        }

        public void setLead_tag_id(String lead_tag_id) {
            this.lead_tag_id = lead_tag_id;
        }

        public String getTag_title() {
            return tag_title;
        }

        public void setTag_title(String tag_title) {
            this.tag_title = tag_title;
        }

        public String getTag_color() {
            return tag_color;
        }

        public void setTag_color(String tag_color) {
            this.tag_color = tag_color;
        }

    }

    public class TagsDisplayAdapter extends RecyclerView.Adapter<TagsDisplayAdapter.SearchTagsViewHolder> {
        private Context context;
        private ArrayList<TagsDisplayResponse> tagsMainList = new ArrayList<>();
        int row_index;
        int num = 1;

        public TagsDisplayAdapter(Context context, ArrayList<TagsDisplayResponse> tagsMainList) {
            this.context = context;
            this.tagsMainList = tagsMainList;
        }

        @NonNull
        @Override
        public TagsDisplayAdapter.SearchTagsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams")
            View cardView = inflater.inflate(R.layout.tags_main_list_item, null, false);
            TagsDisplayAdapter.SearchTagsViewHolder viewHolder = new TagsDisplayAdapter.SearchTagsViewHolder(cardView);

            return viewHolder;
        }

        public void setTagsList(ArrayList<TagsDisplayResponse> tagsList) {
            this.tagsMainList = tagsList;
            notifyItemRangeChanged(0, tagsList.size());
        }

        @Override
        public void onBindViewHolder(@NonNull TagsDisplayAdapter.SearchTagsViewHolder searchTagsViewHolder, final int i) {

            TextView activeTagTVID = searchTagsViewHolder.activeTagTVID;
            final RelativeLayout activeTagRLID = searchTagsViewHolder.activeTagRLID;
            activeTagTVID.setText(tagsMainList.get(i).getTag_title());

            String colorCode = tagsMainList.get(i).getTag_color();
            if (colorCode.startsWith("#")) {
                GradientDrawable bgShape = (GradientDrawable) searchTagsViewHolder.activeTagRLID.getBackground();
                bgShape.setColor(Color.parseColor(tagsMainList.get(i).getTag_color()));
            } else {
                searchTagsViewHolder.activeTagRLID.setBackgroundResource(R.drawable.bg_active_label1);
            }
        }

        @Override
        public int getItemCount() {
            return tagsMainList.size();

        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class SearchTagsViewHolder extends RecyclerView.ViewHolder {

            TextView activeTagTVID;

            RelativeLayout activeTagRLID;

            public SearchTagsViewHolder(@NonNull View itemView) {
                super(itemView);
                activeTagTVID = itemView.findViewById(R.id.activeTagTVID);
                activeTagRLID = itemView.findViewById(R.id.activeTagRLID);
            }
        }
    }

}
