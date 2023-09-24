package crm.tranquil_sales_steer.ui.activities.tags;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView tagsRVID, addTagsRCVID;
    LinearLayout addNewTagsLLID;
    TagsMainAdapter adapter;
    ArrayList<TagsMainList> mainLists = new ArrayList<>();
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    String name, leadID;
    LinearLayout newTagLLID;
    ArrayList<AddTagsList> addTagsLists = new ArrayList<>();
    AddTagsAdapter addTagsAdapter;
    TextView okTVIDBtn, cancelTVIDBtn;
    String GLOBAL_TAG_COLOR, user_id, company_id;
    private KProgressHUD hud;
    ProgressBar pb;
    TextView noData;
    String arr;
    RelativeLayout okRLID, okDisRLID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags_main);
        //statusBarUtilities.statusBarSetup(this);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                name = bundle.getString("CUSTOMER_NAME");
                leadID = bundle.getString("LEAD_ID");
            }
        }

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        RelativeLayout backRLID = findViewById(R.id.backRLID);
        backRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressedAnimation();
            }
        });
        TextView headerTittleTVID = findViewById(R.id.headerTittleTVID);
        headerTittleTVID.setText("Tags - " + name);

        okRLID = findViewById(R.id.okRLID);
        okRLID.setVisibility(View.VISIBLE);
        okDisRLID = findViewById(R.id.okDisRLID);
        okDisRLID.setVisibility(View.GONE);
        okRLID.setOnClickListener(this);

        user_id = MySharedPreferences.getPreferences(TagDetailsActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        company_id = MySharedPreferences.getPreferences(TagDetailsActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);

        addNewTagsLLID = findViewById(R.id.addNewTagsLLID);
        newTagLLID = findViewById(R.id.newTagLLID);

        newTagLLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTagsLists.clear();
                addTagAlert();
            }
        });

        okTVIDBtn = findViewById(R.id.okTVIDBtn);
        cancelTVIDBtn = findViewById(R.id.cancelTVIDBtn);
        okTVIDBtn.setOnClickListener(this);
        cancelTVIDBtn.setOnClickListener(this);
        pb = findViewById(R.id.pb);
        noData = findViewById(R.id.noData);

        tagsRVID = findViewById(R.id.tagsRVID);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        tagsRVID.setLayoutManager(linearLayoutManager);
        tagsRVID.setItemAnimator(new DefaultItemAnimator());
        adapter = new TagsMainAdapter(TagDetailsActivity.this, mainLists);
        tagsRVID.setAdapter(adapter);

        getTagsData();
    }

    private void getTagsData() {
        pb.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        tagsRVID.setVisibility(View.GONE);
        newTagLLID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);

        Call<ArrayList<TagsMainList>> call = apiInterface.getMainTags(company_id, leadID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<TagsMainList>>() {
            @Override
            public void onResponse(Call<ArrayList<TagsMainList>> call, Response<ArrayList<TagsMainList>> response) {

                pb.setVisibility(View.GONE);
                //noData.setVisibility(View.GONE);
                tagsRVID.setVisibility(View.VISIBLE);
                newTagLLID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    mainLists = response.body();
                    if (mainLists.size() > 0) {
                        adapter = new TagsMainAdapter(TagDetailsActivity.this, mainLists);
                        tagsRVID.setAdapter(adapter);
                        adapter.setTagsList(mainLists);
                    } else {
                        tagsRVID.setVisibility(View.GONE);
                        //noData.setVisibility(View.VISIBLE);
                        newTagLLID.setVisibility(View.VISIBLE);
                    }
                } else {
                    tagsRVID.setVisibility(View.GONE);
                    //noData.setVisibility(View.VISIBLE);
                    newTagLLID.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TagsMainList>> call, Throwable t) {
                tagsRVID.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                newTagLLID.setVisibility(View.VISIBLE);
                //noData.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addTagAlert() {

        final Dialog dialog = new Dialog(TagDetailsActivity.this);
        dialog.setContentView(R.layout.alert_add_tags);
        dialog.show();

        final EditText tagNameETID = dialog.findViewById(R.id.tagNameETID);
        addTagsRCVID = dialog.findViewById(R.id.addTagsRCVID);

        addTagsLists.add(new AddTagsList("#003366"));
        addTagsLists.add(new AddTagsList("#00ccff"));
        addTagsLists.add(new AddTagsList("#cc00ff"));
        addTagsLists.add(new AddTagsList("#ff0000"));
        addTagsLists.add(new AddTagsList("#996633"));
        addTagsLists.add(new AddTagsList("#0000ff"));
        addTagsLists.add(new AddTagsList("#660033"));
        addTagsLists.add(new AddTagsList("#669900"));
        addTagsLists.add(new AddTagsList("#993333"));
        addTagsLists.add(new AddTagsList("#ff9900"));
        addTagsLists.add(new AddTagsList("#669999"));
        addTagsLists.add(new AddTagsList("#6600cc"));
        addTagsLists.add(new AddTagsList("#802b00"));
        addTagsLists.add(new AddTagsList("#b366ff"));
        addTagsLists.add(new AddTagsList("#00ff00"));
        addTagsLists.add(new AddTagsList("#3366ff"));
        addTagsLists.add(new AddTagsList("#333300"));
        addTagsLists.add(new AddTagsList("#001a33"));

        linearLayoutManager1 = new GridLayoutManager(this, 5);
        addTagsRCVID.setLayoutManager(linearLayoutManager1);
        addTagsRCVID.setItemAnimator(new DefaultItemAnimator());


        addTagsAdapter = new AddTagsAdapter(TagDetailsActivity.this, addTagsLists);
        addTagsRCVID.setAdapter(addTagsAdapter);

        TextView cancelID = dialog.findViewById(R.id.cancelID);
        RelativeLayout okID = dialog.findViewById(R.id.okID);

        cancelID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        okID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagNameStr = tagNameETID.getText().toString();
                if (TextUtils.isEmpty(tagNameStr)) {
                    dialog.dismiss();
                } else {
                    if (Utilities.isNetworkAvailable(TagDetailsActivity.this)) {
                        addTagToDataBase(dialog, tagNameStr, leadID, user_id, company_id, GLOBAL_TAG_COLOR);
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(TagDetailsActivity.this, "No Internet...!", "Make sure your device is connected to internet");
                    }
                }
            }
        });
    }

    private void addTagToDataBase(final Dialog d, String tagNameStr, String customerID, String user_id, String company_id, String global_tag_color) {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.addTagResponse(customerID, user_id, company_id, tagNameStr, global_tag_color);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {
                                mainLists.clear();
                                getTagsData();
                                d.dismiss();
                            } else {
                                AlertUtilities.bottomDisplayStaticAlert(TagDetailsActivity.this, "Oops..!", "Tag not added");
                            }
                        }
                    } else {
                        AlertUtilities.bottomDisplayStaticAlert(TagDetailsActivity.this, "Oops..!", "Tag not added");
                    }
                } else {
                    AlertUtilities.bottomDisplayStaticAlert(TagDetailsActivity.this, "Oops..!", "Tag not added");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                dismissProgressDialog();
                AlertUtilities.bottomDisplayStaticAlert(TagDetailsActivity.this, "Oops..!", "Tag not added");
            }
        });
    }

    private void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(TagDetailsActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading..")
                    .setDetailsLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }
    }

    private void dismissProgressDialog() {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.okRLID:
                sendSelectedTagsToDataBase();
                break;
            case R.id.cancelTVIDBtn:
                backPressedAnimation();
                break;
        }
    }

    private void sendSelectedTagsToDataBase() {

        try {
            ArrayList<String> mCheckedItemIDs = adapter.getCheckedItems();
            Log.w("", "" + mCheckedItemIDs.size());
            /// add elements to al, including duplicates
            Set<String> hs = new HashSet<>();
            hs.addAll(mCheckedItemIDs);
            mCheckedItemIDs.clear();
            mCheckedItemIDs.addAll(hs);
            Log.w("", "" + mCheckedItemIDs.size());

            StringBuilder sb = new StringBuilder();
            if (mCheckedItemIDs.size() == 1) {
                sb.append(mCheckedItemIDs.get(0));
            } else {
                for (int j = 0; j < mCheckedItemIDs.size(); j++) {
                    if (j == 0) {
                        sb.append(mCheckedItemIDs.get(j)).append(",");
                    } else {
                        int a = mCheckedItemIDs.size() - 1;
                        if (j == a) {
                            sb.append("").append(mCheckedItemIDs.get(j));
                        } else {
                            sb.append("").append(mCheckedItemIDs.get(j)).append(",");
                        }
                    }
                }
            }

            arr = sb.toString();

            if (!arr.equals("")) {
                okDisRLID.setVisibility(View.VISIBLE);
                okRLID.setVisibility(View.GONE);
                sendSelectedTagsToDataBaseResponse(arr, leadID, company_id, user_id);
            } else {
                sendSelectedTagsToDataBaseResponse(arr, leadID, company_id, user_id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendSelectedTagsToDataBaseResponse(String arr, String customerID, String company_id, String user_id) {
        showProgressDialog();
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.InsertTagsToLead(customerID, arr, company_id, user_id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                dismissProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {


                            if (statusResponses.get(i).getStatus().equalsIgnoreCase("1")) {

                                okDisRLID.setVisibility(View.VISIBLE);
                                okRLID.setVisibility(View.GONE);
                                //Toast.makeText(TagDetailsActivity.this, "Tags Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                // AlertUtilities.SuccessAlertDialog(TagDetailsActivity.this,"Tags Added Successfully","");
                            } else {
                                okDisRLID.setVisibility(View.GONE);
                                okRLID.setVisibility(View.VISIBLE);
                                Toast.makeText(TagDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        okDisRLID.setVisibility(View.GONE);
                        okRLID.setVisibility(View.VISIBLE);
                        Toast.makeText(TagDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TagDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {

                okDisRLID.setVisibility(View.GONE);
                okRLID.setVisibility(View.VISIBLE);
                dismissProgressDialog();
                Toast.makeText(TagDetailsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class AddTagsList {

        private String color;

        public AddTagsList(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public class TagsMainList {

        private String tag_id;
        private String tag_title;
        private String tag_color;
        private String tag_status;

        public String getTag_status() {
            return tag_status;
        }

        public void setTag_status(String tag_status) {
            this.tag_status = tag_status;
        }

        public TagsMainList(String tag_id, String tag_title, String tag_color, String tag_status) {
            this.tag_id = tag_id;
            this.tag_title = tag_title;
            this.tag_color = tag_color;
            this.tag_status = tag_status;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
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

    public class TagsMainAdapter extends RecyclerView.Adapter<TagsMainAdapter.TagsViewHolder> {
        private Context context;
        private ArrayList<TagsMainList> tagsMainList = new ArrayList<>();
        int row_index;
        private SparseBooleanArray itemStateArray = new SparseBooleanArray();
        private ArrayList<String> mCheckedItemIDs;
        boolean state = true;
        boolean isInitialChecked = false;

        public TagsMainAdapter(Context context, ArrayList<TagsMainList> tagsMainList) {
            this.context = context;
            this.tagsMainList = tagsMainList;
            mCheckedItemIDs = new ArrayList<>();
            mCheckedItemIDs.clear();
        }

        public ArrayList<String> getCheckedItems() {
            return mCheckedItemIDs;
        }

        @NonNull
        @Override
        public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View cardView = inflater.inflate(R.layout.tags_list_item, null, false);
            TagsViewHolder viewHolder = new TagsViewHolder(cardView);


            return viewHolder;
        }

        public void setTagsList(ArrayList<TagsMainList> tagsList) {
            this.tagsMainList = tagsList;
            notifyItemRangeChanged(0, tagsList.size());
        }

        @Override
        public void onBindViewHolder(@NonNull final TagsViewHolder searchTagsViewHolder, final int i) {
            try {
                searchTagsViewHolder.activeTagTVID.setText(Utilities.CapitalText(tagsMainList.get(i).getTag_title()));
                GradientDrawable bgShape = (GradientDrawable) searchTagsViewHolder.activeTagRLID.getBackground();
                bgShape.setColor(Color.parseColor(tagsMainList.get(i).getTag_color()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String tagStatus = tagsMainList.get(i).getTag_status();


            if (tagStatus.equalsIgnoreCase("1")) {
                isInitialChecked = true;
                mCheckedItemIDs.add(tagsMainList.get(i).getTag_id());
                searchTagsViewHolder.tagsLLID.setBackgroundColor(getResources().getColor(R.color.tag_bg));
                searchTagsViewHolder.tagCheckBox.setChecked(true);
            } else {
                isInitialChecked = false;
                mCheckedItemIDs.remove(tagsMainList.get(i).getTag_id());
                searchTagsViewHolder.tagsLLID.setBackgroundColor(getResources().getColor(R.color.white));
                searchTagsViewHolder.tagCheckBox.setChecked(false);
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

        public class TagsViewHolder extends RecyclerView.ViewHolder {

            TextView activeTagTVID;
            RelativeLayout activeTagRLID;
            CheckBox tagCheckBox;
            LinearLayout tagsLLID;

            public TagsViewHolder(@NonNull View itemView) {
                super(itemView);
                activeTagTVID = itemView.findViewById(R.id.tagTitleTVID);
                activeTagRLID = itemView.findViewById(R.id.activeTagRLID);
                tagCheckBox = itemView.findViewById(R.id.tagCheckBox);
                tagsLLID = itemView.findViewById(R.id.tagsLLID);

                tagCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton arg0, boolean value) {
                        if (tagCheckBox.isPressed()) {

                            if (value) {
                                state = false;
                                tagsLLID.setBackgroundColor(getResources().getColor(R.color.tag_bg));
                                mCheckedItemIDs.add(tagsMainList.get(getAdapterPosition()).getTag_id());
                            } else {
                                state = true;
                                tagsLLID.setBackgroundColor(getResources().getColor(R.color.white));
                                mCheckedItemIDs.remove(tagsMainList.get(getAdapterPosition()).getTag_id());
                            }
                        }
                    }
                });

                tagsLLID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (state) {
                            state = false;
                            tagCheckBox.setChecked(true);
                            tagsLLID.setBackgroundColor(getResources().getColor(R.color.tag_bg));
                            mCheckedItemIDs.add(tagsMainList.get(getAdapterPosition()).getTag_id());
                        } else {
                            state = true;
                            tagsLLID.setBackgroundColor(getResources().getColor(R.color.white));
                            tagCheckBox.setChecked(false);
                            mCheckedItemIDs.remove(tagsMainList.get(getAdapterPosition()).getTag_id());
                        }
                    }
                });
            }
        }
    }

    public class AddTagsAdapter extends RecyclerView.Adapter<AddTagsAdapter.ViewHolder> {

        private Context context;
        private ArrayList<AddTagsList> addTagsList = new ArrayList<>();
        int row_index;

        public AddTagsAdapter(Context context, ArrayList<AddTagsList> tagsMainList) {
            this.context = context;
            this.addTagsList = tagsMainList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View cardView = inflater.inflate(R.layout.add_tags_list_item, null, false);
            ViewHolder viewHolder = new ViewHolder(cardView);
            viewHolder.tagBgID = cardView.findViewById(R.id.tagBgID);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            RelativeLayout tagTitleTVID = holder.tagBgID;

            GradientDrawable bgShape = (GradientDrawable) holder.tagBgID.getBackground();
            bgShape.setColor(Color.parseColor(addTagsList.get(position).getColor()));

            //holder.tagBgID.setColorFilter(Color.parseColor(addTagsList.get(position).getColor()), PorterDuff.Mode.SRC_IN);

            //holder.tagBgID.setBackgroundColor(Color.parseColor(addTagsList.get(position).getColor()));

            holder.tagBgID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position;
                    notifyDataSetChanged();


                }
            });
            holder.selectedMarkID.setVisibility(View.GONE);

            if (row_index == position) {
                GLOBAL_TAG_COLOR = addTagsList.get(position).getColor();
                holder.selectedMarkID.setVisibility(View.VISIBLE);
            } else {
                holder.selectedMarkID.setVisibility(View.GONE);
            }

        }

        public void setTagsList(ArrayList<AddTagsList> tagsList) {
            this.addTagsList = tagsList;
            notifyItemRangeChanged(0, tagsList.size());
        }

        @Override
        public int getItemCount() {
            return addTagsList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RelativeLayout tagBgID;
            ImageView selectedMarkID;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tagBgID = itemView.findViewById(R.id.tagBgID);
                selectedMarkID = itemView.findViewById(R.id.selectedMarkID);
            }
        }
    }

    @Override
    public void onBackPressed() {
        backPressedAnimation();
    }

    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

}
