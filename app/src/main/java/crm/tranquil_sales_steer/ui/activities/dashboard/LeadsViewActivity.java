package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.AppSingleton;
import crm.tranquil_sales_steer.data.requirements.CommunicationsServices;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.OnLoadMoreListener;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.ui.activities.templates.SendTemplatesActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeadsViewActivity extends AppCompatActivity {

    public static String tittleStr, urlStr, menuID, menuTitle;

    RelativeLayout backRLID, searchRLID;
    TextView headerTittleTVID;
    String companyID, activityID, activityTitle,mobileStr,leadID,emailStr,leadMobileStr, leadEmailStr;
    MainLeadsAdapter adapter;
    ArrayList<MainLeadsResponse> performanceResponses = new ArrayList<>();
    RecyclerView availableLVID;
    ProgressBar pb;
    TextView noData;
    ImageView noDataImage;
    String userType, userID;
    Integer start = 0, pagiantion = 1;
    LinearLayoutManager linearLayoutManager;
    private boolean isFirstListLoaded = true;
    SwipeRefreshLayout swipeID;

    ArrayList<MainLeadsResponse> offlineResponse;

    String mimeString = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
    KProgressHUD kProgressHUD;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_leads_view);

        mobileStr = MySharedPreferences.getPreferences(LeadsViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
        leadID = MySharedPreferences.getPreferences(LeadsViewActivity.this, "" + AppConstants.LEAD_ID);
        emailStr = MySharedPreferences.getPreferences(LeadsViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_EMAIL_ID);


        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                urlStr = bundle.getString("URL");
                activityID = bundle.getString("ACTIVITY_ID");
                activityTitle = bundle.getString("ACTIVITY_TITLE");
                menuID = bundle.getString("MENU_ID");
                menuTitle = bundle.getString("MENU_TITLE");

            }
        }

        companyID = MySharedPreferences.getPreferences(LeadsViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        userID = MySharedPreferences.getPreferences(LeadsViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(LeadsViewActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        backRLID = findViewById(R.id.backRLID);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        RelativeLayout searchRLID = findViewById(R.id.searchRLID);

        searchRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadsViewActivity.this, SearchViewActivity.class);
                intent.putExtra("TYPE", "SEARCH");
                startActivity(intent);
            }
        });

        headerTittleTVID.setText(menuTitle + " " + activityTitle);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedAnimation();
            }
        });
        searchRLID = findViewById(R.id.searchRLID);
        searchRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadsViewActivity.this, SearchViewActivity.class);
                intent.putExtra("TYPE", "SEARCH");
                startActivity(intent);
            }
        });

        availableLVID = findViewById(R.id.availableLVID);
        pb = findViewById(R.id.pb);
        noData = findViewById(R.id.noData);
        noDataImage = findViewById(R.id.noDataImage);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        availableLVID.setLayoutManager(linearLayoutManager);
        availableLVID.setItemAnimator(new DefaultItemAnimator());
        adapter = new MainLeadsAdapter(LeadsViewActivity.this, availableLVID, performanceResponses);
        availableLVID.setAdapter(adapter);
        swipeID = findViewById(R.id.swipeID);


        swipeID.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(LeadsViewActivity.this)) {
                    performanceResponses.clear();
                    swipeID.setRefreshing(false);
                    leadActivitiesDisplayListList(AppConstants.GLOBAL_MAIN_URL + urlStr + "?company_id=" + companyID + "&user_type=" + userType + "&user_id=" + userID + "&act_id=" + activityID + "&menu_id=" + menuID+ "&start=" + 0);
                } else {
                    swipeID.setRefreshing(false);
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                    noData.setText("NO Internet");
                }
            }
        });


        FloatingActionButton fabAddBtn = findViewById(R.id.fabAddBtn);
        fabAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadsViewActivity.this, LeadCreateActivity.class);
                intent.putExtra("NUMBER", "");
                startActivity(intent);
            }
        });

        FloatingActionButton refreshBtn = findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utilities.isNetworkAvailable(LeadsViewActivity.this)) {
                    performanceResponses.clear();
                    swipeID.setRefreshing(false);
                    leadActivitiesDisplayListList(AppConstants.GLOBAL_MAIN_URL + urlStr + "?company_id=" + companyID + "&user_type=" + userType + "&user_id=" + userID + "&act_id=" + activityID + "&menu_id=" + menuID+ "&start=" + 0);
                } else {
                    swipeID.setRefreshing(false);
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                    noData.setText("NO Internet");
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (Utilities.isNetworkAvailable(LeadsViewActivity.this)) {
            leadActivitiesDisplayListList(AppConstants.GLOBAL_MAIN_URL + urlStr + "?company_id=" + companyID + "&user_type=" + userType + "&user_id=" + userID + "&act_id=" + activityID + "&menu_id=" + menuID+ "&start=" + 0);
        } else {
            noData.setVisibility(View.VISIBLE);
            noDataImage.setVisibility(View.VISIBLE);
            noData.setText("NO Internet");
        }
    }

    @Override
    public void onBackPressed() {
        onBackPressedAnimation();
    }

    private void onBackPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

    private void leadActivitiesDisplayListList(String url) {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        noDataImage.setVisibility(View.GONE);

        Log.e("api==>", url);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                noDataImage.setVisibility(View.GONE);
                adapter.setLoaded();
                adapter.notifyDataSetChanged();
                performanceResponses.clear();
                //adapter.clear();

                if (response == null) {
                    availableLVID.setVisibility(View.VISIBLE);
                    //noDataFoundTVID.setVisibility(View.VISIBLE);
                } else if (response.equals("null")) {
                    pagiantion = 0;
                    noData.setVisibility(View.VISIBLE);
                    noDataImage.setVisibility(View.VISIBLE);
                } else {
                    try {
                        int count = 0;


                        while (count < response.length()) {
                            isFirstListLoaded = false;
                            JSONObject jo = response.getJSONObject(count);
                            String lead_id = jo.optString("lead_id");
                            String lead_name = jo.optString("lead_name");
                            String lead_mobile = jo.optString("lead_mobile");
                            boolean show_menu = Boolean.parseBoolean(jo.optString("show_menu"));
                            String email_id = jo.optString("email_id");
                            String requirement_name = jo.optString("requirement_name");
                            String lead_pic = jo.optString("lead_pic");

                            performanceResponses.add(new MainLeadsResponse(lead_id, lead_name, lead_mobile, show_menu,email_id,requirement_name,lead_pic));
                            count++;
                        }
                       /* if (count < 50) {
                            pagiantion = 0;
                        }
*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter.setSubCategoriesHolderslist(performanceResponses);
                    // availableLVID.setAdapter(adapter);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noDataImage.setVisibility(View.VISIBLE);
            }
        });

        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(LeadsViewActivity.this).addToRequestQueue(arrayRequest, "");
    }

    public class MainLeadsResponse {

        /*lead_id: "1",
    lead_name: "sddsf",
    lead_mobile: "2147483647"*/
        private String lead_id;
        private String lead_name;
        private String lead_mobile;
        private boolean show_menu;
        private String email_id;
        private String requirement_name;
        private String lead_pic;

        public MainLeadsResponse(String lead_id, String lead_name, String lead_mobile, boolean show_menu, String email_id, String requirement_name, String lead_pic) {
            this.lead_id = lead_id;
            this.lead_name = lead_name;
            this.lead_mobile = lead_mobile;
            this.show_menu = show_menu;
            this.email_id = email_id;
            this.requirement_name = requirement_name;
            this.lead_pic = lead_pic;
        }

        public String getRequirement_name() {
            return requirement_name;
        }

        public void setRequirement_name(String requirement_name) {
            this.requirement_name = requirement_name;
        }

        public String getEmail_id() {
            return email_id;
        }

        public void setEmail_id(String email_id) {
            this.email_id = email_id;
        }

        public String getLead_pic() {
            return lead_pic;
        }

        public void setLead_pic(String lead_pic) {
            this.lead_pic = lead_pic;
        }

        public boolean isShow_menu() {
            return show_menu;
        }

        public void setShow_menu(boolean show_menu) {
            this.show_menu = show_menu;
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

        public String getLead_mobile() {
            return lead_mobile;
        }

        public void setLead_mobile(String lead_mobile) {
            this.lead_mobile = lead_mobile;
        }
    }

    public class MainLeadsAdapter extends RecyclerView.Adapter {
        private LayoutInflater layoutInflater;
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private OnLoadMoreListener mOnLoadMoreListener;
        private boolean isLoading;
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private ArrayList<MainLeadsResponse> mainLeadsResponse = new ArrayList<MainLeadsResponse>();
        public String id;
        private Activity activity;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


        public void setSubCategoriesHolderslist(ArrayList<MainLeadsResponse> mainLeadsResponse) {
            this.mainLeadsResponse = mainLeadsResponse;
            notifyItemRangeChanged(0, mainLeadsResponse.size());
        }


        public MainLeadsAdapter(Activity activity, RecyclerView mRecyclerView, ArrayList<MainLeadsResponse> _subcategoriesHolderslist) {
            this.mainLeadsResponse = _subcategoriesHolderslist;
            layoutInflater = LayoutInflater.from(activity);
            this.activity = activity;
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });

        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        @Override
        public int getItemViewType(int position) {
            return mainLeadsResponse.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(activity).inflate(R.layout.lead_view_list_item, parent, false);
                return new UserViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(activity).inflate(R.layout.progressbar1, parent, false);
                return new LoadingViewHolder(view);
            }

            return null;
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            if (holder instanceof UserViewHolder) {

                String title = mainLeadsResponse.get(position).getLead_name();
                ((UserViewHolder) holder).newLeadNameTVID.setText(Utilities.CapitalText(title));
                //((UserViewHolder) holder).leadMobileTVID.setText(mainLeadsResponse.get(position).lead_mobile);
                ((UserViewHolder) holder).leadMobileTVID.setText(mainLeadsResponse.get(position).getRequirement_name());

                viewBinderHelper.setOpenOnlyOne(true);
                viewBinderHelper.bind(((UserViewHolder) holder).swipelayout, String.valueOf(mainLeadsResponse.get(position).getLead_name()));
                viewBinderHelper.closeLayout(String.valueOf(mainLeadsResponse.get(position).getLead_name()));
                ((UserViewHolder) holder).bindData(mainLeadsResponse.get(position));

                MySharedPreferences.setPreference(LeadsViewActivity.this, "" + AppConstants.LEAD_MOBILE, "" + mainLeadsResponse.get(position).getLead_mobile());
                MySharedPreferences.setPreference(LeadsViewActivity.this, "" + AppConstants.LEAD_EMAIL, "" + mainLeadsResponse.get(position).getLead_name());

                try {
                    Picasso.with(activity).load(mainLeadsResponse.get(position).getLead_pic()).error(R.drawable.user).error(R.drawable.user).placeholder(R.drawable.user).rotate(0).into(((UserViewHolder) holder).leadPic);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ((UserViewHolder) holder).newPhoneRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CommunicationsServices.InsertCommunication(activity, "1", mainLeadsResponse.get(position).getLead_id(), userID, "", "");
                        numberCalling(mainLeadsResponse.get(position).getLead_mobile());
                    }
                });

                ((UserViewHolder) holder).whatsAppCallRLID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final BottomSheetDialog dialog1 = new BottomSheetDialog(activity);
                        dialog1.setContentView(R.layout.alert_business_whatsapp);

                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                        int width1 = ViewGroup.LayoutParams.MATCH_PARENT;
                        int height1 = ViewGroup.LayoutParams.WRAP_CONTENT;
                        dialog1.getWindow().setLayout(width1, height1);
                        dialog1.show();

                        AppCompatImageView whatsappIVID = dialog1.findViewById(R.id.whatsappIVID);
                        AppCompatImageView businessWhatsappIVID = dialog1.findViewById(R.id.businessWhatsappIVID);

                        whatsappIVID.setOnClickListener(view1 -> {

                            try {
                                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                                String url = "https://api.whatsapp.com/send?phone=" + "+91" + mainLeadsResponse.get(position).getLead_mobile() + "&text=" + URLEncoder.encode("", "UTF-8");
                                sendMsg.setPackage("com.whatsapp");
                                sendMsg.setData(Uri.parse(url));
                                startActivity(sendMsg);
                                dialog1.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                /*Utilities.showToast(this,e.getMessage());*/
                                Toast.makeText(activity, "You don't have WhatsApp in your device", Toast.LENGTH_SHORT).show();
                            }

                        });

                        businessWhatsappIVID.setOnClickListener(view2 -> {

                            try {

                                Intent sendMsg = new Intent(Intent.ACTION_VIEW);
                                String url = "https://api.whatsapp.com/send?phone=" + "+91" + mainLeadsResponse.get(position).getLead_mobile() + "&text=" + URLEncoder.encode("", "UTF-8");
                                sendMsg.setPackage("com.whatsapp.w4b");
                                sendMsg.setData(Uri.parse(url));

                                startActivity(sendMsg);
                                dialog1.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                                /*Utilities.showToast(this,e.getMessage());*/
                                Toast.makeText(activity, "You don't have business WhatsApp in your device", Toast.LENGTH_SHORT).show();
                            }

                            dialog1.dismiss();

                        });

                    }
                });

                ((UserViewHolder) holder).newMainLeadView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent btdelsale = new Intent(new Intent(activity, LeadHistoryActivity.class));
                        btdelsale.putExtra("CUSTOMER_ID", mainLeadsResponse.get(position).getLead_id());
                        btdelsale.putExtra("CUSTOMER_NAME", mainLeadsResponse.get(position).getLead_name());
                        btdelsale.putExtra("CUSTOMER_MOBILE", mainLeadsResponse.get(position).getLead_mobile());
                        activity.startActivity(btdelsale);
                    }
                });

                ((UserViewHolder) holder).messageBtnRLID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(LeadsViewActivity.this, SendTemplatesActivity.class);
                        intent.putExtra("TYPE", "Send Sms");
                        intent.putExtra("LEAD_MOBILE_NUMBER", "" + mainLeadsResponse.get(position).getLead_mobile());
                        intent.putExtra("LEAD_EMAIL", "" + leadEmailStr);
                        intent.putExtra("LEAD_ID", "" + leadID);
                        startActivity(intent);
                    }
                });

                ((UserViewHolder) holder).mailBtnRLID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(LeadsViewActivity.this, SendTemplatesActivity.class);
                        intent.putExtra("TYPE", "Send Email");
                        intent.putExtra("LEAD_MOBILE_NUMBER", "" + mainLeadsResponse.get(position).getLead_mobile());
                        intent.putExtra("LEAD_EMAIL", "" + mainLeadsResponse.get(position).getEmail_id());
                        intent.putExtra("LEAD_ID", "" + leadID);
                        startActivity(intent);
                    }
                });


            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }


        @Override
        public int getItemCount() {
            return mainLeadsResponse == null ? 0 : mainLeadsResponse.size();
        }

        public void setLoaded() {
            isLoading = false;
        }


        class LoadingViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public LoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = itemView.findViewById(R.id.progressBar1);
            }
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView newLeadNameTVID,leadMobileTVID;
            RelativeLayout newPhoneRL, newMainLeadView;
            private SwipeRevealLayout swipelayout;
            RelativeLayout messageBtnRLID,mailBtnRLID,whatsAppCallRLID;
            CircleImageView leadPic;

            public UserViewHolder(View itemView) {
                super(itemView);
                newLeadNameTVID = itemView.findViewById(R.id.newLeadNameTVID);
                leadMobileTVID = itemView.findViewById(R.id.leadMobileTVID);
                newPhoneRL = itemView.findViewById(R.id.newPhoneRL);
                newMainLeadView = itemView.findViewById(R.id.newMainLeadView);
                swipelayout = itemView.findViewById(R.id.swipelayout);
                messageBtnRLID = itemView.findViewById(R.id.messageBtnRLID);
                mailBtnRLID = itemView.findViewById(R.id.mailBtnRLID);
                whatsAppCallRLID = itemView.findViewById(R.id.whatsAppCallRLID);
                leadPic = itemView.findViewById(R.id.leadPic);

            }

            public void bindData(MainLeadsResponse mainLeadsResponse) {
                newLeadNameTVID.setText(mainLeadsResponse.getLead_name());
            }
        }

        private void numberCalling(String number) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);

               /* if (callIntent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(activity, OutGoingCalls.class);
                            i.putExtras(callIntent);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            activity.startActivity(i);
                        }
                    }, 1000);

            }*/
        }

    }
}

}
