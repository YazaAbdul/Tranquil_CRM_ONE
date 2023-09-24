package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.github.axet.androidlibrary.app.SuperUser;
import com.github.axet.androidlibrary.widgets.ErrorDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.ExpandableHeightGridView;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.UserInfoSession;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.recordings.app.CallApplication;
import crm.tranquil_sales_steer.recordings.app.MixerPaths;
import crm.tranquil_sales_steer.recordings.app.Storage;
import crm.tranquil_sales_steer.recordings.services.RecordingService;
import crm.tranquil_sales_steer.recordings.widgets.MixerPathsPreferenceCompat;
import crm.tranquil_sales_steer.ui.activities.attendance.AttendanceMainActivity;
import crm.tranquil_sales_steer.ui.activities.folders.FoldersActivity;
import crm.tranquil_sales_steer.ui.activities.realTime.ProcessMainClass;
import crm.tranquil_sales_steer.ui.activities.realTime.restarter.RestartServiceBroadcastReceiver;
import crm.tranquil_sales_steer.ui.activities.site_visits.SiteVisitSearchActivity;
import crm.tranquil_sales_steer.ui.activities.start_ups.LoginActivity;
import crm.tranquil_sales_steer.ui.activities.user_profile.EditProfileActivity;
import crm.tranquil_sales_steer.ui.adapters.BannersAdapter;
import crm.tranquil_sales_steer.ui.adapters.ContactsAdapter;
import crm.tranquil_sales_steer.ui.adapters.DrawerItemCustomAdapter;
import crm.tranquil_sales_steer.ui.adapters.GetModuleNameAdapter;
import crm.tranquil_sales_steer.ui.adapters.PlansAdapter;
import crm.tranquil_sales_steer.ui.adapters.PostSalesAdapter;
import crm.tranquil_sales_steer.ui.adapters.ShortcutsAdapter;
import crm.tranquil_sales_steer.ui.fragments.ActivitiesFragment;
import crm.tranquil_sales_steer.ui.models.ActivestatusResponse;
import crm.tranquil_sales_steer.ui.models.CardsResponse;
import crm.tranquil_sales_steer.ui.models.ContactsModel;
import crm.tranquil_sales_steer.ui.models.DashBoardResponseNew;
import crm.tranquil_sales_steer.ui.models.DataModel;
import crm.tranquil_sales_steer.ui.models.GetCompanyUserdlt;
import crm.tranquil_sales_steer.ui.models.GetModuleNameResponse;
import crm.tranquil_sales_steer.ui.models.GetlogoResponse;
import crm.tranquil_sales_steer.ui.models.InsertContactsResponse;
import crm.tranquil_sales_steer.ui.models.MainTopicDetailsResponse;
import crm.tranquil_sales_steer.ui.models.NextReceivingCallResponse;
import crm.tranquil_sales_steer.ui.models.PlansResponse;
import crm.tranquil_sales_steer.ui.models.PostSalesResponse;
import crm.tranquil_sales_steer.ui.models.ShortcutsResponse;
import crm.tranquil_sales_steer.ui.models.UserStatusResponse;
import crm.tranquil_sales_steer.ui.models.Versioncontrol;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener, ShortcutsAdapter.ShortcutsClickListener, PlansAdapter.PlansClickListener, PostSalesAdapter.PostSalesClickListener {

    String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    ExpandableHeightGridView mDrawerList;
    Toolbar toolbar;
    CharSequence mDrawerTitle;
    private CharSequence mTitle;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;
    LinearLayout nav_menu_layout,incomingLLID;
    ExpandableHeightGridView dashboardGVID;
    RecyclerView cardsRVID;
    TextView noData;
    ProgressBar progress;
    String getcompanydlts;
    ImageView imagelogo;


    List<DataModel>drawerItem=new ArrayList<DataModel>();


    ArrayList<CardsResponse> cardsResponses = new ArrayList<>();
    ArrayList<GetCompanyUserdlt> getCompanyUserdlts = new ArrayList<>();
    BannersAdapter bannersAdapter;
    String companyID, nameStr, mobileStr, pic, userID, userType,user_designation;
    TextView sideMenuUsernameTVID, mobileNumberTVID, userNameTVID, userMobileTVID,designationTVID;
    RelativeLayout incomingRLID;
    SwipeRefreshLayout swipeID;
    EditText searchHintETID;
    boolean doubleBackToExitPressedOnce = false;
    UserInfoSession prefManager;
    CircleImageView img_icon, userPicID;
    ImageView menuID;
    //update App

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    String version;

    String expiryDaysStr;
    RelativeLayout navHeaderInfoRLID;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    Location location = null;
    String langitudeStr, latitudeStr, dateS, timeS, locationS, areaS;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    TabLayout tabs;
    ViewPager viewPager;
    FloatingActionButton refresh_fab;

    public static String url, title;
    RecyclerView availableLVID,topicDetailsLVID;
    ArrayList<DashBoardResponseNew> dashBoardResponseNew = new ArrayList<>();
    ArrayList<GetModuleNameResponse> getModuleNameResponse = new ArrayList<>();
    crm.tranquil_sales_steer.ui.adapters.DashBoardAdapter dashBoardAdapter;
    GetModuleNameAdapter getModuleNameAdapter;
    CardView overallCVID, todayCVID, pendingCVID, featureCVID, agentsCVID, availabilityCVID, dueCustomersCVID, collectionsCVID, saleDoneCVID,sourceCVID,
             searchCVID,incomingCVID,refreshCVID,createLeadCVID;
//LinearLayout topicdetailstxtLLID,createivesLLID;

LinearLayout topicdetailstxtCVID,createivesCVID;
    RecyclerView shortcutsRVID,plansRVID,postSalesRVID;
    ShortcutsAdapter shortcutsAdapter;
    ArrayList<ShortcutsResponse> shortcutsResponses = new ArrayList<>();
    ArrayList<MainTopicDetailsResponse> topicdetailsResponses = new ArrayList<>();
    ArrayList<PlansResponse> plansResponses = new ArrayList<>();
    ArrayList<PostSalesResponse> postSalesResponses = new ArrayList<>();
    PlansAdapter plansAdapter;
    PostSalesAdapter postSalesAdapter;

    //SwitchCompat switchUser;
    LabeledSwitch switchUser;
    static Boolean isTouched = false;

    //Next Receiving Call

    AppCompatTextView logoTextTVID,nextReceivingNameTVID,nextReceivingMobileTVID;
    RelativeLayout nextReceivingView,nextCallRLID;
    LinearLayout nextReceivingWhatsAppLLID,nextReceivingMessageLLID,nextReceivingCallLLID,camp_autoLLID;
    String nextReceivingNameStr,nextReceivingMobileStr,nextReceivingLeadID;

    RelativeLayout campaignsRLID,followUpRLID,topicdetailsRLID;

    //Google Analytics

    private  FirebaseAnalytics analytics;

    // recordings


    //recorder

    public static final String[] MUST = new String[]{
            Manifest.permission.RECORD_AUDIO,
    };

    public static final String[] PERMISSIONS = SuperUser.concat(MUST, new String[]{
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS, // get contact name by phone number
            Manifest.permission.READ_PHONE_STATE, // read outgoing going calls information
    });
    public static final int RESULT_CALL = 1;
    Storage storage;
    boolean show;
    Boolean recording;
    int encoding;
    String phone;
    long sec;
    public static String SHOW_PROGRESS = DashBoardActivity.class.getCanonicalName() + ".SHOW_PROGRESS";
    public static String SET_PROGRESS = DashBoardActivity.class.getCanonicalName() + ".SET_PROGRESS";
    public static String SHOW_LAST = DashBoardActivity.class.getCanonicalName() + ".SHOW_LAST";
    //Recordings recordings;

    AppCompatImageView askMeIVID;

    CardView shortCutsCVID,plansCVID;
    RelativeLayout postSalesRLID;
    AppCompatTextView versionTVID;

    ArrayList<ContactsModel> contactsModels = new ArrayList<>();

    String android_id;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String a = intent.getAction();
            if (a.equals(SHOW_PROGRESS)) {
                encoding = -1;
                show = intent.getBooleanExtra("show", false);
                recording = (Boolean) intent.getExtras().get("recording");
                sec = intent.getLongExtra("sec", 0);
                phone = intent.getStringExtra("phone");
                //updatePanel();
            }
            if (a.equals(SET_PROGRESS)) {
                encoding = intent.getIntExtra("set", 0);
                //updatePanel();
            }
            if (a.equals(SHOW_LAST)) {
                //last();
            }
        }
    };






    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RESULT_CALL:
                if (Storage.permitted(this, MUST)) {
                    try {
                        storage.migrateLocalStorage();
                    } catch (RuntimeException e) {
                        ErrorDialog.Error(this, e);
                    }
                    /*recordings.load(false, null);
                    if (resumeCall != null) {
                        RecordingService.setEnabled(this, resumeCall.isChecked());
                        resumeCall = null;
                    }*/
                } else {
                   // Toast.makeText(this, R.string.not_permitted, Toast.LENGTH_SHORT).show();
                    if (!Storage.permitted(this, MUST)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Permissions");
                        builder.setMessage("Call permissions must be enabled manually");

                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Storage.showPermissions(DashBoardActivity.this);
                            }
                        });

                        builder.show();
                        //resumeCall = null;
                    }
                }
        }
    }


    @SuppressLint({"SetTextI18n", "NewApi", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.z_new_home_screen);
        version="2";

        imagelogo=findViewById(R.id.imagelogo);
        CheckVerionControl();
        getcompanyuserdlt();
        getCompanylogo();



        Utilities.startAnimation(this);
        Utilities.setStatusBarGradiant(this);
        analytics = FirebaseAnalytics.getInstance(this);

        MySharedPreferences.setPreference(this,AppConstants.PAGE_REFRESH,"NO");








        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        prefManager = new UserInfoSession(this);
        if (prefManager.isFirstTimeInfoLaunch()) {
            prefManager.setInfoFirstTimeLaunch(false);
            Handler mHandler = new Handler();
            Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //displayAboutInfo();
                    //permissioncheck();
                }
            };
            mHandler.postDelayed(mRunnable, 2 * 1000);
        }

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.e("device_id==>", "" + android_id);


        if (android_id.equalsIgnoreCase("2da05d4412adcefb")) {

            //a2553870a151a2d0,2da05d4412adcefb

            Log.e("chandu_device==>","" + "YES");

        } else {

            Log.e("chandu_device==>","" + "NO");

            try {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted

                    Thread t = new Thread(){
                        public void run(){
                            //getContacts();

                            getContactsAsJsonArray();

                            pushContactsToServer();
                        }
                    };
                    t.start();


                } else {
                    // permission is not granted, request it
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        storage = new Storage(this);
        IntentFilter ff = new IntentFilter();
        ff.addAction(SHOW_PROGRESS);
        ff.addAction(SET_PROGRESS);
        ff.addAction(SHOW_LAST);
        registerReceiver(receiver, ff);
        Storage.permitted(this, PERMISSIONS, RESULT_CALL);
        RecordingService.setEnabled(this, true);
        warning();


        expiryDaysStr = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_EXPIRY_DAYS);

        nameStr = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_NAME);
        user_designation = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_DESIGNATION);
        mobileStr = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
        pic = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_PROFILE_PIC);
        userID = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);


        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        nav_menu_layout = findViewById(R.id.nav_menu_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        mDrawerList.setFocusable(false);
        toolbar = findViewById(R.id.toolbar2);
        menuID = findViewById(R.id.menuID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sideMenuUsernameTVID = findViewById(R.id.sideMenuUsernameTVID);
        mobileNumberTVID = findViewById(R.id.mobileNumberTVID);
        userNameTVID = findViewById(R.id.userNameTVID);
        img_icon = findViewById(R.id.img_icon);
        userPicID = findViewById(R.id.userPicID);
        askMeIVID = findViewById(R.id.askMeIVID);
        versionTVID = findViewById(R.id.versionTVID);
        designationTVID=findViewById(R.id.designationTVID);

        overallCVID = findViewById(R.id.overallCVID);
        todayCVID = findViewById(R.id.todayCVID);
        pendingCVID = findViewById(R.id.pendingCVID);
        featureCVID = findViewById(R.id.featureCVID);
        agentsCVID = findViewById(R.id.agentsCVID);
        availabilityCVID = findViewById(R.id.availabilityCVID);
        dueCustomersCVID = findViewById(R.id.dueCustomersCVID);
        collectionsCVID = findViewById(R.id.collectionsCVID);
        saleDoneCVID = findViewById(R.id.saleDoneCVID);
        sourceCVID = findViewById(R.id.sourceCVID);
        searchCVID = findViewById(R.id.searchCVID);
        incomingCVID = findViewById(R.id.incomingCVID);
        refreshCVID = findViewById(R.id.refreshCVID);
        createLeadCVID = findViewById(R.id.createLeadCVID);
        createivesCVID = findViewById(R.id.createivesCVID);
        topicdetailstxtCVID = findViewById(R.id.topicdetailstxtCVID);
        switchUser = findViewById(R.id.switchUser);

        shortCutsCVID = findViewById(R.id.shortCutsCVID);
        plansCVID = findViewById(R.id.plansCVID);
        postSalesRLID = findViewById(R.id.postSalesRLID);
        camp_autoLLID = findViewById(R.id.camp_autoLLID);
        topicdetailsRLID = findViewById(R.id.topicdetailsRLID);
        incomingLLID = findViewById(R.id.incomingLLID);

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;


        CheckVerionControl();

        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String version = info.versionName;
        versionTVID.setText("Version : " + version);



        //Next Call Alert


        nextCallRLID = findViewById(R.id.nextCallRLID);

        startWorker();


        try {
            Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(0).into(img_icon);
            Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(0).into(userPicID);
            Log.e("upserpicimage", ""+pic);
        } catch (Exception e) {
            e.printStackTrace();
        }

        userPicID.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashBoardProfileActivity.class);
            intent.putExtra("USER_ID",userID);
            intent.putExtra("USER_NAME",nameStr);
            intent.putExtra("DESIGNATION",user_designation);
            Log.e("DESIGNATION", ""+user_designation);
            intent.putExtra("IMAGEURL",pic);
            startActivity(intent);
        });


        swipeID = findViewById(R.id.swipeID);
        //mainSubscribeBtn = findViewById(R.id.mainSubscribeBtn);
        //subScribeViewLLID = findViewById(R.id.subScribeViewLLID);
        // subScribeViewLLID.setVisibility(View.GONE);

       /* DataModel[] drawerItem = new DataModel[6];
        drawerItem[0] = new DataModel(R.drawable.home2, "Home");
        drawerItem[1] = new DataModel(R.drawable._phone, "Tele Callers");
        drawerItem[2] = new DataModel(R.drawable.lock, "Change Password");
        drawerItem[3] = new DataModel(R.drawable.attendance, "Attendance");
        drawerItem[4] = new DataModel(R.drawable.attendance, "Direct Meeting");
        drawerItem[5] = new DataModel(R.drawable.logout, "Logout");*/
        String planType = MySharedPreferences.getPreferences(this,"PLAN_TYPE");
        Log.e("Plantype", ""+planType);

      //  DataModel[] drawerItem = new DataModel[11];


     /*List<DataModel>drawerItem=new ArrayList<DataModel>();*/

        drawerItem.add(new DataModel(R.drawable.home2, "Home",0));
        drawerItem.add(new DataModel(R.drawable._phone, "Tele Caller ",1));

        if (planType.equals("1")) {
        drawerItem.add(new DataModel(R.drawable.attendancebold, "Attendance",2));
        drawerItem.add(new DataModel(R.drawable.partnership_128, "Direct Meeting",3));
        drawerItem.add(new DataModel(R.drawable.meet, "Customer Meet",4));
        drawerItem.add(new DataModel(R.drawable.ic_punch_report, "Create Day Report",5));
        drawerItem.add(new DataModel(R.drawable.ic_file, "File Manager",6));
        }else if(planType.equals("2")){
            drawerItem.add(new DataModel(R.drawable.attendancebold, "Attendance",2));
            drawerItem.add(new DataModel(R.drawable.partnership_128, "Direct Meeting",3));
            drawerItem.add(new DataModel(R.drawable.meet, "5",4));
            drawerItem.add(new DataModel(R.drawable.ic_punch_report, "Create Day Report",5));
            drawerItem.add(new DataModel(R.drawable.ic_file, "File Manager",6));
        }else{

        }
        drawerItem.add(new DataModel(R.drawable.creativity32, "Creatives",7));
        drawerItem.add(new DataModel(R.drawable.ai, "Tranquil AI",8));
        drawerItem.add(new DataModel(R.drawable.lock, "Change Password",9));
        drawerItem.add(new DataModel(R.drawable.logout, "Logout",10));



      /*  drawerItem[0] =  new DataModel(R.drawable.home2, "Home");
        drawerItem[1] =  new DataModel(R.drawable._phone, "Tele Callers");
        drawerItem[2] =  new DataModel(R.drawable.attendancebold, "Attendance");
        drawerItem[3] =  new DataModel(R.drawable.partnership_128, "Direct Meeting");
        drawerItem[4] =  new DataModel(R.drawable.meet, "Customer Meet");
        if (planType.equals("1")) {
            drawerItem[2] =  new DataModel(R.drawable.attendancebold, "Attendance");
            drawerItem[3] =  new DataModel(R.drawable.partnership_128, "Direct Meeting");
            drawerItem[4] =  new DataModel(R.drawable.meet, "Customer Meet");
            drawerItem[5] =  new DataModel(R.drawable.ic_punch_report, "Create Day Report");
            drawerItem[6] =  new DataModel(R.drawable.ic_file, "File Manager");
        }else if(planType.equals("2")){
            drawerItem[2] =  new DataModel(R.drawable.attendancebold, "Attendance");
            drawerItem[3] =  new DataModel(R.drawable.partnership_128, "Direct Meeting");
            drawerItem[4] =  new DataModel(R.drawable.meet, "Customer Meet");
            drawerItem[5] =  new DataModel(R.drawable.ic_punch_report, "Create Day Report");
            drawerItem[6] =  new DataModel(R.drawable.ic_file, "File Manager");
        }else{


        }
        drawerItem[7] = new DataModel(R.drawable.creativity32, "Creatives");
        drawerItem[8] = new DataModel(R.drawable.ai, "Tranquil AI");
        drawerItem[9] =  new DataModel(R.drawable.lock, "Change Password");
        drawerItem[10] = new DataModel(R.drawable.logout, "Logout");*/



        final DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.side_menu_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setExpanded(true);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setupDrawerToggle();

        navHeaderInfoRLID = findViewById(R.id.navHeaderInfoRLID);
        navHeaderInfoRLID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(DashBoardActivity.this, EditProfileActivity.class));
            }
        });

        askMeIVID.setOnClickListener(v -> {
            startActivity(new Intent(DashBoardActivity.this, AskMeActivity.class));

        });

        cardsRVID = findViewById(R.id.cardsRVID);
        noData = findViewById(R.id.noData);
        progress = findViewById(R.id.progress);
        availableLVID = findViewById(R.id.availableLVID);
        topicDetailsLVID = findViewById(R.id.topicDetailsLVID);
        /*GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        availableLVID.setLayoutManager(layoutManager);*/

        //shortcuts

        shortcutsRVID = findViewById(R.id.shortcutsRVID);
        plansRVID = findViewById(R.id.plansRVID);
        postSalesRVID = findViewById(R.id.postSalesRVID);

        campaignsRLID = findViewById(R.id.campaignsRLID);
        followUpRLID = findViewById(R.id.followUpRLID);

        campaignsRLID.setOnClickListener(v -> {
            Intent intent = new Intent(this,KnowlarityCalls.class);
            intent.putExtra("CALL_TYPE","CAMPAIGN_CALL");
            startActivity(intent);
        });

        followUpRLID.setOnClickListener(v -> {
            Intent intent = new Intent(this,KnowlarityCalls.class);
            intent.putExtra("CALL_TYPE","AUTO_FOLLOWUP");
            startActivity(intent);
        });

        loadShortcuts();
        loadPlans();
        loadPostSales();


        companyID = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
        Log.d("LOGIN_COMPANY_ID : ", "" + companyID);

        if (Utilities.isNetworkAvailable(this)) {

            CheckVerionControl();

         /*   loadSkyCards();
            loadDashBoardNew();
            loadUserStatus();
            loadTopicDetials();*/
        } else {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show();
        }


        if (Utilities.isNetworkAvailable(DashBoardActivity.this)) {
            Gson gson = new Gson();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DashBoardActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            String json1 = gson.toJson("");
            editor.putString("" + AppConstants.SharedPreferenceValues.OFFLINE_HOME_URL, "");
            editor.apply();
            //loadDashBoard();
            //loadSkyCards();
        } else {
            //offlineResponse = getArrayList(AppConstants.SharedPreferenceValues.OFFLINE_HOME_URL);
            //getOfflineData(offlineResponse);
        }

        checkForAppUpdate();

        overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);


        FloatingActionButton fabAddBtn = findViewById(R.id.fabAddBtn);
        fabAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, LeadCreateActivity.class);
                intent.putExtra("NUMBER", "");
                startActivity(intent);

            }
        });

        FloatingActionButton refreshBtn = findViewById(R.id.refreshBtn);
        FloatingActionButton searchBtn = findViewById(R.id.searchBtn);
        FloatingActionButton incoming = findViewById(R.id.incoming);

        incoming.setOnClickListener(v -> {
            startActivity(new Intent(DashBoardActivity.this, DashBoardIncomingCallsActivity.class));
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivesSatus();
               /* loadDashBoardNew();
                loadSkyCards();
                loadTopicDetials();*/





            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, SearchViewActivity.class);
                intent.putExtra("TYPE", "SEARCH");
                startActivity(intent);

            }
        });



/*if(getcompanydlts=="1") {
    switchUser.setVisibility(View.GONE);

}*/


    switchUser.setOnToggledListener(new OnToggledListener() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onSwitched(ToggleableView toggleableView, boolean isOn) {
            if (isOn) {

                loadUpdateUser("1");
                    /*switchUser.setColorOn(R.color.colorCallButton);
                    switchUser.setColorDisabled(R.color.red_main);*/
                //switchUser.setColorOn(R.color.green_main);
                //switchUser.setBackgroundColor(R.color.green_main);


            } else {
                loadUpdateUser("0");
                    /*switchUser.setColorOff(R.color.red_main);
                    switchUser.setColorDisabled(R.color.colorCallButton);*/
                //switchUser.setColorOn(R.color.red_main);

                // switchUser.setBackgroundColor(R.color.red_main);


            }
        }

    });



       /* switchUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    loadUpdateUser("1");
                }else {
                    loadUpdateUser("0");
                }
            }
        });*/

        overallCVID.setOnClickListener(this);
        todayCVID.setOnClickListener(this);
        pendingCVID.setOnClickListener(this);
        featureCVID.setOnClickListener(this);
        agentsCVID.setOnClickListener(this);
        availabilityCVID.setOnClickListener(this);
        dueCustomersCVID.setOnClickListener(this);
        collectionsCVID.setOnClickListener(this);
        saleDoneCVID.setOnClickListener(this);
        sourceCVID.setOnClickListener(this);
        searchCVID.setOnClickListener(this);
        incomingCVID.setOnClickListener(this);
        refreshCVID.setOnClickListener(this);
        createLeadCVID.setOnClickListener(this);
        createivesCVID.setOnClickListener(this);
        topicdetailstxtCVID.setOnClickListener(this);
        nextCallRLID.setOnClickListener(this);

    }

    private void getContacts() {

        try {
            Set<String> phoneNumbers = new HashSet<>();
            Set<String> names = new HashSet<>();

            String[] stringArray = new String[0];
            String[] nameArray = new String[contactsModels.size()];
            String[] numbersArray = new String[contactsModels.size()];


            Map<String, String> phoneBook = new HashMap<>();


            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        /*if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // Do something with the name and phone number
                Log.d(TAG, "Name: " + name + ", Phone Number: " + phoneNumber);

                contactsList(name,phoneNumber);
            }
            cursor.close();
        }*/

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    ContactsModel contact = new ContactsModel(name, phoneNumber);
                    contactsModels.add(contact);


                    ContactsModel[] contactsArray = new ContactsModel[contactsModels.size()];
                    contactsArray = contactsModels.toArray(contactsArray);

                    for (int i = 0; i < contactsModels.size(); i++) {

                        List<String> stringList = new ArrayList<>();
                        List<String> namesList = new ArrayList<>();
                        List<String> numbersList = new ArrayList<>();



                        stringList.add(contactsModels.get(i).getName());
                        namesList.add(contactsModels.get(i).getName());

                        stringList.add(contactsModels.get(i).getNumber());
                        numbersList.add(contactsModels.get(i).getNumber());

                        /*for (int j = 0; j < contactsModels.size(); j++) {
                            nameArray[j] = String.valueOf(contactsModels.get(j).getName());
                        }

                        for (int k = 0; k < contactsModels.size(); k++) {
                            nameArray[k] = String.valueOf(contactsModels.get(k).getNumber());
                        }*/


                        /*for (int j = 0; j < contactsModels.size(); j++) {

                            String personString = contactsModels.get(j).getName();
                            nameArray[j] = personString;
                        }

                        for (int k = 0; k < contactsModels.size(); k++) {

                            String personString = contactsModels.get(k).getNumber();
                            numbersArray[k] = personString;
                        }*/

                        stringArray  = stringList.toArray(new String[0]);
                        nameArray  = namesList.toArray(new String[namesList.size()]);
                        numbersArray  = numbersList.toArray(new String[numbersList.size()]);


                        /*try {
                            Log.e("contacts_array==>","" + Arrays.toString(stringArray));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/


                    }

                    // Check if the phone number is already in the HashSet
                    /*if (!phoneBook.containsKey(phoneNumber)) {
                        phoneBook.put(phoneNumber, name);
                        // Do something with the name and phone number
                        Log.d(TAG, "Name: " + name + ", Phone Number: " + phoneNumber);
                    }*/
                }
                cursor.close();
            }

        /*for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
            String phoneNumber = entry.getKey();
            String name = entry.getValue();
            // Do something with the unique phone number and name
            Log.d(TAG, "Name: " + name + ", Phone Number: " + phoneNumber);
            Log.e("Contacts==>", "Name: " + name + ", Phone Number: " + phoneNumber);
            Toast.makeText(this, "getting contacts" + "  , Name: " + name + ", Phone Number: " + phoneNumber, Toast.LENGTH_SHORT).show();
        }*/


            //contactsList();

            String[] finalStringArray = stringArray;
            String[] finalNameArray = nameArray;
            String[] finalNumbersArray = numbersArray;



            Thread t = new Thread(){
                public void run(){
                   // pushContactsToServer(finalStringArray, finalNameArray, finalNumbersArray);
                }
            };
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private JsonArray getContactsAsJsonArray() {

        JsonArray contactsArray = new JsonArray();

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            JsonObject contactObject = new JsonObject();
            contactObject.addProperty("name", name);
            contactObject.addProperty("number", phoneNumber);

            contactsArray.add(contactObject);
        }

        cursor.close();

        System.out.println(contactsArray);

        return contactsArray;
    }

    private void pushContactsToServer() {

        try {

            /*JSONArray arr = new JSONArray();
            JSONObject obj;
            for( int i = 0; i < numbersArray.length; i++ ){
                obj = new JSONObject();
                obj.put("name", nameArray[i]);
                obj.put("number", numbersArray[i]);
                arr.put( obj );

                System.out.println(obj);
            }

            String data = arr.toString();*/

            JsonArray contactArray = getContactsAsJsonArray();

            String user = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_ID);

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("contactlist", String.valueOf(contactArray))
                    .addFormDataPart("user_id", user)
                    .addFormDataPart("cp_id", "0");

            RequestBody requestBody = builder.build();

            Log.e("POST_Response==>", "" + contactArray);

            //progress.setVisibility(View.VISIBLE);

            Log.e("Insert==>","" + "INSERTING");

               ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
            Call<InsertContactsResponse> call = apiInterface.pushUserContactsToServer(requestBody);
            Log.e("Call_Api==>",call.request().toString());
            call.enqueue(new Callback<InsertContactsResponse>() {
                @Override
                public void onResponse(Call<InsertContactsResponse> call, Response<InsertContactsResponse> response) {

                    //progress.setVisibility(View.GONE);

                    if (response.body() != null && response.code() == 200){

                        InsertContactsResponse insertContactsResponse = response.body();

                        if (insertContactsResponse.getStatus() == 1){

                           // Toast.makeText(DashBoardActivity.this, "Calls Pushed To Server", Toast.LENGTH_SHORT).show();

                            Log.e("Insert==>","" + "SUCCESS");

                        }else {
                            Log.e("Insert==>","" + "FAILED");

                        }
                    }
                }

                @Override
                public void onFailure(Call<InsertContactsResponse> call, Throwable t) {

                    Log.e("Insert==>","" + "FAILED");

                   // progress.setVisibility(View.GONE);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void contactsList() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.contact_list_alert);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.show();

        RecyclerView contactsListRVID = dialog.findViewById(R.id.contactsRVID);
        ContactsAdapter adapter = new ContactsAdapter(contactsModels,DashBoardActivity.this);
        contactsListRVID.setLayoutManager(new LinearLayoutManager(this));
        contactsListRVID.setAdapter(adapter);


    }

    private void loadUpdateUser(String status) {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<UserStatusResponse> call = apiInterface.getUpdateUserStatus(userID,status);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<UserStatusResponse>() {
            @Override
            public void onResponse(Call<UserStatusResponse> call, Response<UserStatusResponse> response) {

                if (response.body() != null && response.code() == 200){

                    UserStatusResponse userStatusResponse = response.body();

                }else {
                    Toast.makeText(DashBoardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserStatusResponse> call, Throwable t) {
                Toast.makeText(DashBoardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void getcompanyuserdlt(){
        ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<GetCompanyUserdlt>> call=apiInterface.getCompanyUserdlt();
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetCompanyUserdlt>>() {
            @Override
            public void onResponse(Call<ArrayList<GetCompanyUserdlt>> call, Response<ArrayList<GetCompanyUserdlt>> response) {
                if(response.body()!=null&& response.code()==200){
                    getCompanyUserdlts=response.body();
                    if(getCompanyUserdlts.size()>0){
                       for(int i=0;i<getCompanyUserdlts.size();i++){
                           if(getCompanyUserdlts.get(i).getProduct_id().equals("1")){
                               MySharedPreferences.setPreference(DashBoardActivity.this,"PLAN_TYPE",getCompanyUserdlts.get(i).getProduct_id());
                               getcompanydlts=getCompanyUserdlts.get(i).getProduct_id();
                            //   Toast.makeText(DashBoardActivity.this, "1  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                               //Toast.makeText(DashBoardActivity.this, "  "+getcompanydlts, Toast.LENGTH_SHORT).show();
                               switchUser.setVisibility(View.VISIBLE);
                               camp_autoLLID.setVisibility(View.VISIBLE);
                               askMeIVID .setVisibility(View.VISIBLE);
                               topicdetailsRLID.setVisibility(View.VISIBLE);
                               incomingLLID.setVisibility(View.VISIBLE);
                               Log.e("getcompanydlts", ""+getcompanydlts);



                           }else if(getCompanyUserdlts.get(i).getProduct_id().equals("2")){
                              // Toast.makeText(DashBoardActivity.this, "2  "+getCompanyUserdlts.get(i).getProduct_id(), Toast.LENGTH_SHORT).show();
                               switchUser.setVisibility(View.GONE);
                               camp_autoLLID.setVisibility(View.GONE);
                               askMeIVID .setVisibility(View.VISIBLE);
                               topicdetailsRLID.setVisibility(View.GONE);
                               incomingLLID.setVisibility(View.GONE);
                               Log.e("getcompanydlts", ""+getcompanydlts);
                           }else{
                             //  Toast.makeText(DashBoardActivity.this, "3", Toast.LENGTH_SHORT).show();
                               askMeIVID .setVisibility(View.GONE);
                               switchUser.setVisibility(View.GONE);
                               camp_autoLLID.setVisibility(View.GONE);
                               topicdetailsRLID.setVisibility(View.GONE);
                               incomingLLID.setVisibility(View.GONE);
                               Log.e("getcompanydlts", ""+getcompanydlts);

                           }

                       }


                    }
                }



            //    Toast.makeText(DashBoardActivity.this, "onsucccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<GetCompanyUserdlt>> call, Throwable t) {
                Toast.makeText(DashBoardActivity.this, "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void loadUserStatus() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<UserStatusResponse> call = apiInterface.getUserStatus(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<UserStatusResponse>() {
            @Override
            public void onResponse(Call<UserStatusResponse> call, Response<UserStatusResponse> response) {

                if (response.body() != null && response.code() == 200){

                    UserStatusResponse userStatusResponse = response.body();

                    if (userStatusResponse.getStatus().equals(true)){
                        //switchUser.setChecked(true);

                        switchUser.setOn(true);
                        /*switchUser.setColorOn(R.color.colorCallButton);
                        switchUser.setColorBorder(R.color.colorCallButton);
                        switchUser.setColorDisabled(R.color.red_main);*/

                    }else if (userStatusResponse.getStatus().equals(false)){
                        //switchUser.setChecked(false);
                        switchUser.setOn(false);
                        /*switchUser.setColorOff(R.color.red_main);
                        switchUser.setColorBorder(R.color.red_main);
                        switchUser.setColorDisabled(R.color.colorCallButton);*/

                    }

                }else {
                    Toast.makeText(DashBoardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserStatusResponse> call, Throwable t) {
                Toast.makeText(DashBoardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadPostSales() {

        postSalesResponses.add(new PostSalesResponse("1","Due Customers","#1878e7","#3f7fc0","#6ca9f0","#7fb4f2",R.drawable.people));
        postSalesResponses.add(new PostSalesResponse("2","Collections","#bb24db","#aa45ba","#d475e8","#dc8dec",R.drawable.datacollection512));
        postSalesResponses.add(new PostSalesResponse("3","Sale Done","#10efc6","#43bcb6","#84f7e2","#90f8e5",R.drawable.sales512));
        postSalesResponses.add(new PostSalesResponse("4","Agents","#8813ec","#7e42bd","#c184f5","#c995f6",R.drawable.agent512));
        postSalesResponses.add(new PostSalesResponse("5","Availability","#ff0088","#bc4395","#ff6fbc","#ff80c4",R.drawable.quality));


        postSalesAdapter = new PostSalesAdapter(postSalesResponses,DashBoardActivity.this,DashBoardActivity.this);
        postSalesRVID.setAdapter(postSalesAdapter);


    }

    private void loadPlans() {
        String planType = MySharedPreferences.getPreferences(this,"PLAN_TYPE");
        Log.e("Plantype", ""+planType);


        plansResponses.add(new PlansResponse("1","Overall \n Activity ","#38068A","#2C066A","#5a0ade","#610aef",R.drawable.humanrights));
        plansResponses.add(new PlansResponse("2","Today\nActivity","#E22C13","#CD230C","#ef5b46","#f2715f",R.drawable.date512));
        plansResponses.add(new PlansResponse("5","Tomorrow\nActivity","#ff0088","#bc4395","#ff6fbc","#ff80c4",R.drawable.tomorrow_white));
        plansResponses.add(new PlansResponse("3","Pending\nActivity","#A3034C","#B6115D","#8EA3034C","#8EA3034C",R.drawable.pending_white));
        plansResponses.add(new PlansResponse("4","Future\nActivity","#8813ec","#7e42bd","#c184f5","#c995f6",R.drawable.diagram));
       // plansResponses.add(new PlansResponse("5","Tomorrow\nActivity","#ff0088","#bc4395","#ff6fbc","#ff80c4",R.drawable.tomorrow_white));


        if (planType.equals("1")) {
         //   Toast.makeText(this, "plantype"+planType, Toast.LENGTH_SHORT).show();
            plansResponses.add(new PlansResponse("6","Source Wise\nActivity","#3607f8","#5939c6","#704efa","#8366fb",R.drawable.source_white));
            plansResponses.add(new PlansResponse("7","Project Wise\nActivity","#38068A","#2C066A","#5a0ade","#610aef",R.drawable.source_white));

        }


        plansAdapter = new PlansAdapter(plansResponses,DashBoardActivity.this,DashBoardActivity.this);
        plansRVID.setAdapter(plansAdapter);

    }

    private void loadShortcuts() {

        String planType = MySharedPreferences.getPreferences(this,"PLAN_TYPE");
        Log.e("Plantype", ""+planType);


            shortcutsResponses.add(new ShortcutsResponse("1", "Tele \n Callers", "#1878e7", "#3f7fc0", "#6ca9f0", "#7fb4f2", R.drawable.humanrights));
            //  shortcutsResponses.add(new ShortcutsResponse("2","Change Password","#bb24db","#aa45ba","#d475e8","#dc8dec",R.drawable.password_white));phone_call_white
        //  shortcutsResponses.add(new ShortcutsResponse("8","call \n Recordings","#f51e0a","#ca4435","#f97b70","#fa897f",R.drawable.recordings_white));



        if (planType.equals("1")) {
        //    Toast.makeText(this, "plantype"+planType, Toast.LENGTH_SHORT).show();
            shortcutsResponses.add(new ShortcutsResponse("3", "Attendance Punch In/Out", "#10efc6", "#43bcb6", "#84f7e2", "#90f8e5", R.drawable.attendancebold));

            shortcutsResponses.add(new ShortcutsResponse("7", "File \n Manger", "#ebb014", "#d0b62f", "#f2cc68", "#f4d37c", R.drawable.file));

            shortcutsResponses.add(new ShortcutsResponse("9","Creatives Marketing","#082ff7","#3059cf","#546ff9","#6b83fa",R.drawable.creativity32));
            shortcutsResponses.add(new ShortcutsResponse("4", "Direct Meeting", "#8813ec", "#7e42bd", "#c184f5", "#c995f6", R.drawable.direct_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("5", "Customer Meet", "#ff0088", "#bc4395", "#ff6fbc", "#ff80c4", R.drawable.customer_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("6", "Create Day Report", "#3607f8", "#5939c6", "#704efa", "#8366fb", R.drawable.day_report_white));

        } else if(planType.equals("2")){
            shortcutsResponses.add(new ShortcutsResponse("3", "Attendance Punch In/Out", "#10efc6", "#43bcb6", "#84f7e2", "#90f8e5", R.drawable.attendancebold));

            shortcutsResponses.add(new ShortcutsResponse("7", "File \n Manger", "#ebb014", "#d0b62f", "#f2cc68", "#f4d37c", R.drawable.file));

            shortcutsResponses.add(new ShortcutsResponse("9","Creatives Marketing","#082ff7","#3059cf","#546ff9","#6b83fa",R.drawable.creativity32));
            shortcutsResponses.add(new ShortcutsResponse("4", "Direct Meeting", "#8813ec", "#7e42bd", "#c184f5", "#c995f6", R.drawable.direct_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("5", "Customer Meet", "#ff0088", "#bc4395", "#ff6fbc", "#ff80c4", R.drawable.customer_meet_white));
            shortcutsResponses.add(new ShortcutsResponse("6", "Create Day Report", "#3607f8", "#5939c6", "#704efa", "#8366fb", R.drawable.day_report_white));

        }




        //shortcutsResponses.add(new ShortcutsResponse("10","Tele Callers",R.drawable._phone));

        /*LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this,RecyclerView.VERTICAL,false);
        shortcutsRVID.setLayoutManager(layoutManager);*/

        shortcutsAdapter = new ShortcutsAdapter(shortcutsResponses,DashBoardActivity.this,DashBoardActivity.this);
        shortcutsRVID.setAdapter(shortcutsAdapter);



    }



    private void loadTopicdetails(){
     //   shortcutsResponses.add(new ShortcutsResponse("1","Tele Callers","#1878e7","#3f7fc0","#6ca9f0","#7fb4f2",R.drawable.phone_call_white));
        topicdetailsResponses.add(new MainTopicDetailsResponse("1", "Topic Details", "#1878e7","#3f7fc0","#6ca9f0","#7fb4f2",R.drawable.collection) );

    }

    private void warning() {
        final SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        if (shared.getBoolean("warning", true)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.call_warning);
            builder.setCancelable(false);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putBoolean("warning", false);
                    edit.commit();
                }
            });
            final AlertDialog d = builder.create();
            d.setOnShowListener(new DialogInterface.OnShowListener() {
                Button b;
                SwitchCompat sw1, sw2, sw3, sw4;

                @Override
                public void onShow(DialogInterface dialog) {
                    b = d.getButton(DialogInterface.BUTTON_POSITIVE);
                    b.setEnabled(false);
                    Window w = d.getWindow();
                    sw1 = (SwitchCompat) w.findViewById(R.id.recording);
                    sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked)
                                sw1.setClickable(true);
                            update();
                        }
                    });
                    sw2 = (SwitchCompat) w.findViewById(R.id.quality);
                    sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked)
                                sw2.setClickable(true);
                            update();
                        }
                    });
                    sw3 = (SwitchCompat) w.findViewById(R.id.taskmanagers);
                    sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                sw3.setClickable(false);
                            }
                            update();
                        }
                    });
                    sw4 = (SwitchCompat) w.findViewById(R.id.mixedpaths_switch);
                    final MixerPaths m = new MixerPaths();
                    if (!m.isCompatible() || m.isEnabled()) {
                        View v = w.findViewById(R.id.mixedpaths);
                        v.setVisibility(View.GONE);
                        sw4.setChecked(true);
                    } else {
                        sw4.setChecked(m.isEnabled());
                        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked)
                                    sw4.setClickable(false);
                                m.load();
                                if (isChecked && !m.isEnabled())
                                    MixerPathsPreferenceCompat.show(DashBoardActivity.this);
                                update();
                            }
                        });
                    }
                }

                void update() {
                    b.setEnabled(sw3.isChecked() && sw4.isChecked());
                }
            });
            d.show();
        }

        /*if (OptimizationPreferenceCompat.needKillWarning(this, CallApplication.PREFERENCE_NEXT))
            OptimizationPreferenceCompat.buildKilledWarning(new ContextThemeWrapper(this, getAppTheme()), true, CallApplication.PREFERENCE_OPTIMIZATION, RecordingService.class).show();
        else if (OptimizationPreferenceCompat.needBootWarning(this, CallApplication.PREFERENCE_BOOT))
            OptimizationPreferenceCompat.buildBootWarning(this, CallApplication.PREFERENCE_BOOT).show();*/

        RecordingService.startIfEnabled(this);

        Intent intent = getIntent();
        openIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        openIntent(intent);
    }


    public int getAppTheme() {
        return CallApplication.getTheme(this, R.style.RecThemeLight_NoActionBar, R.style.RecThemeDark_NoActionBar);
    }

    public static String ENABLE = DashBoardActivity.class.getCanonicalName() + ".ENABLE";

    @SuppressLint("RestrictedApi")
    void openIntent(Intent intent) {
        String a = intent.getAction();
        if (a != null && a.equals(ENABLE)) {
            MenuBuilder m = new MenuBuilder(this);
            MenuItem item = m.add(Menu.NONE, R.id.action_call, Menu.NONE, "");
            item.setEnabled(RecordingService.isEnabled(this));
            onOptionsItemSelected(item);
        }
    }


    private void loadDashBoardNew() {

        progress.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        availableLVID.setVisibility(View.GONE);

        shortCutsCVID.setVisibility(View.GONE);
        cardsRVID.setVisibility(View.GONE);
        plansCVID.setVisibility(View.GONE);
        postSalesRLID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<DashBoardResponseNew>> call = apiInterface.getOverallActivities(companyID, "1", userType, userID);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<DashBoardResponseNew>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<DashBoardResponseNew>> call, Response<ArrayList<DashBoardResponseNew>> response) {
                progress.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                availableLVID.setVisibility(View.VISIBLE);
                cardsRVID.setVisibility(View.VISIBLE);

                shortCutsCVID.setVisibility(View.VISIBLE);
                plansCVID.setVisibility(View.VISIBLE);
                postSalesRLID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {


                    dashBoardResponseNew = response.body();

                    if (dashBoardResponseNew.size() > 0) {
                       /* LinearLayoutManager layoutManager = new LinearLayoutManager(DashBoardActivity.this,LinearLayoutManager.VERTICAL,false);
                        availableLVID.setLayoutManager(layoutManager);*/
                        dashBoardAdapter = new crm.tranquil_sales_steer.ui.adapters.DashBoardAdapter(DashBoardActivity.this, dashBoardResponseNew,userID,userType);
                        availableLVID.setAdapter(dashBoardAdapter);

                    } else {

                        progress.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("No Data Available");
                        availableLVID.setVisibility(View.GONE);

                    }

                } else {
                    progress.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("No Data Available");
                    availableLVID.setVisibility(View.GONE);
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<DashBoardResponseNew>> call, Throwable t) {

                progress.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                noData.setText("No Data Available");
                availableLVID.setVisibility(View.GONE);
                shortCutsCVID.setVisibility(View.GONE);
                plansCVID.setVisibility(View.GONE);
                postSalesRLID.setVisibility(View.GONE);

            }
        });

    }
    private void CheckVerionControl(){

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<Versioncontrol> call=apiInterface.versionControl(version);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<Versioncontrol>() {
            @Override
            public void onResponse(Call<Versioncontrol> call, Response<Versioncontrol> response) {
                Log.e("Response==>","" + response);

                if(response.body()!=null&& response.code()==200){
                    Versioncontrol versioncontrol=response.body();
                    if(versioncontrol.getStatus().equals("1")){
                        getActivesSatus();
                        //   getLoginResponse();
                    }else {

                        AlertUtilities.bottomDisplayStaticAlert(DashBoardActivity.this, "Alert...!", "Update the app to latest version");
                        Intent intent=new Intent(DashBoardActivity.this,LoginActivity.class);
                     /*   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
                        startActivity(intent);
                        finish();
                    }

                }
            }
            @Override
            public void onFailure(Call<Versioncontrol> call, Throwable t) {

                AlertUtilities.bottomDisplayStaticAlert(DashBoardActivity.this, "Alert...!", "Something went wrong at server side");

            }
        });

    }

    private void getActivesSatus() {
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<ActivestatusResponse>> call = apiInterface.getactivestatus(userID);
        Log.e("api=load=>", call.request().toString());
        call.enqueue(new Callback<ArrayList<ActivestatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ActivestatusResponse>> call, Response<ArrayList<ActivestatusResponse>> response) {
                Log.e("Response==>","" + response);
                if (response.body() != null && response.code() == 200){
                    // Toast.makeText(DashBoardActivity.this, "inside respbse body", Toast.LENGTH_SHORT).show();
                    ArrayList<ActivestatusResponse> activestatusResponses = response.body();
                    if(activestatusResponses!=null&& activestatusResponses.size()>0){
                        for(int i=0;i<activestatusResponses.size();i++){
                            if(activestatusResponses.get(i).getStatus().equalsIgnoreCase("0")){
                                //  Toast.makeText(DashBoardActivity.this, "activtiy", Toast.LENGTH_SHORT).show();
                                getcompanyuserdlt();
                                loadDashBoardNew();
                                loadSkyCards();

                                //  loadSkyCards();
                                //  loadDashBoardNew();
                                loadUserStatus();
                                loadTopicDetials();
                                getCompanylogo();





                            }else{
                                //      Toast.makeText( DashBoardActivity.this, "deactiti", Toast.LENGTH_SHORT).show();
                                //  AlertUtilities.bottomDisplayStaticAlert(DashBoardActivity.this, "user doesnt exites!", "Please enter valid email and password");
                                Intent intent=new Intent(DashBoardActivity.this,LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }


                    }
                }
                else {
                    AlertUtilities.bottomDisplayStaticAlert(DashBoardActivity.this, "Alert...!", "response body Something went wrong at server side");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ActivestatusResponse>> call, Throwable t) {
                // dismissProgressDialog();
                AlertUtilities.bottomDisplayStaticAlert(DashBoardActivity.this, "Alert...!", "onfail Something went wrong at server side");
            }
        });




    }



public void loadTopicDetials(){
   // Toast.makeText(this, "loadtopicdetails", Toast.LENGTH_SHORT).show();

        ApiInterface apiInterface= ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<GetModuleNameResponse>> call=apiInterface.getModuleName(0);
        Log.e("api==>>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetModuleNameResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetModuleNameResponse>> call, Response<ArrayList<GetModuleNameResponse>> response) {
                if(response.body()!=null&& response.code()==200){
                    getModuleNameResponse=response.body();
                    if(getModuleNameResponse.size()>0){
                        getModuleNameAdapter=new GetModuleNameAdapter(DashBoardActivity.this, getModuleNameResponse,userID,userType);
                        topicDetailsLVID.setAdapter(getModuleNameAdapter);

                    }else {
                        noData.setText("No Data Available");
                    }
                }else{
                    noData.setText("No Data Available");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetModuleNameResponse>> call, Throwable t) {
                noData.setText("No Data AvailableF");
            }
        });


}
    private void getCompanylogo()   {
        ApiInterface apiInterface=ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<GetlogoResponse>> call=apiInterface.getlogo();
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<GetlogoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<GetlogoResponse>> call, Response<ArrayList<GetlogoResponse>> response) {
                if(response.body()!=null&& response.code()==200){

                 //   Toast.makeText(getApplicationContext(), "onresponse logo"+response.body().get(0).getLogo(), Toast.LENGTH_LONG).show();
                    String imageurl=response.body().get(0).getLogo();
                    MySharedPreferences.setPreference(DashBoardActivity.this,AppConstants.SharedPreferenceValues.LOGO_URL,imageurl);
                    Log.e("imageurl", imageurl);

                    Picasso.with(DashBoardActivity.this).load(imageurl).into(imagelogo);


                /*    String  crmappiconurl = MySharedPreferences.getPreferences(DashBoardActivity.this, AppConstants.SharedPreferenceValues.LOGO_URL);
                    Log.e("crmappiconurl",crmappiconurl);*/

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetlogoResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "onfail", Toast.LENGTH_SHORT).show();
            }
        });

    }





    private void loadSkyCards() {
        progress.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
        cardsRVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CardsResponse>> call = apiInterface.getSkycards(userID, userType);
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<ArrayList<CardsResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<CardsResponse>> call, Response<ArrayList<CardsResponse>> response) {
                progress.setVisibility(View.GONE);
                cardsRVID.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
                if (response.body() != null && response.code() == 200) {
                    cardsResponses = response.body();
                    if (cardsResponses.size() > 0) {

                        bannersAdapter = new BannersAdapter(cardsResponses, DashBoardActivity.this);
                        cardsRVID.setAdapter(bannersAdapter);

                        for (int i = 0; i < cardsResponses.size(); i++) {
                            //cardsRVID.setFocusable(false);
                            //MySharedPreferences.setArrayListData(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.OFFLINE_HOME_URL, dashboardResponses);
                            //saveCardsActivitiesArrayList(cardsResponses, "" + AppConstants.SharedPreferenceValues.OFFLINE_HOME_URL);
                        }
                    } else {
                        noData.setVisibility(View.VISIBLE);
                        noData.setText("No Data Available");
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
                    noData.setText("No Data Available");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<CardsResponse>> call, Throwable t) {
                noData.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                noData.setText("No Data Available");
            }
        });
    }


    @SuppressLint("NewApi")
    private void backPressedAnimation() {
        finish();
        overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

    private void nextCallAlert() {

        Dialog dialog = new Dialog(DashBoardActivity.this);
        dialog.setContentView(R.layout.next_receiving_call_alert);
        int height2 = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width2 = ViewGroup.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setLayout(width2, height2);
        dialog.getWindow().setGravity(Gravity.CENTER);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        logoTextTVID = dialog.findViewById(R.id.logoTextTVID);
        nextReceivingNameTVID = dialog.findViewById(R.id.nextReceivingNameTVID);
        nextReceivingMobileTVID = dialog.findViewById(R.id.nextReceivingMobileTVID);
        nextReceivingView = dialog.findViewById(R.id.nextReceivingView);
        nextReceivingWhatsAppLLID = dialog.findViewById(R.id.nextReceivingWhatsAppLLID);
        nextReceivingMessageLLID = dialog.findViewById(R.id.nextReceivingMessageLLID);
        nextReceivingCallLLID = dialog.findViewById(R.id.nextReceivingCallLLID);

        //nextReceivingCallLLID.setOnClickListener(this);

        loadNextReceivingCall();
    }

    private void loadNextReceivingCall() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<NextReceivingCallResponse> call = apiInterface.getNextReceivingCall(userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<NextReceivingCallResponse>() {
            @Override
            public void onResponse(Call<NextReceivingCallResponse> call, Response<NextReceivingCallResponse> response) {

                if (response.body() != null && response.code() == 200){

                    NextReceivingCallResponse nextReceivingCallResponse = response.body();

                    nextReceivingNameTVID.setText(nextReceivingCallResponse.getCustomer_name());
                    logoTextTVID.setText(nextReceivingCallResponse.getCustomer_name());
                    nextReceivingMobileTVID.setText(nextReceivingCallResponse.getMobile_number());

                    nextReceivingMobileStr = nextReceivingCallResponse.getMobile_number();
                    nextReceivingNameStr = nextReceivingCallResponse.getCustomer_name();
                    nextReceivingLeadID = nextReceivingCallResponse.getLead_id();

                }
            }

            @Override
            public void onFailure(Call<NextReceivingCallResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.overallCVID:
                Intent intent = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent.putExtra("PLAN_ID", "1");
                intent.putExtra("TITLE", "Overall");
                intent.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent);
                break;
            case R.id.todayCVID:
                Intent intent2 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent2.putExtra("PLAN_ID", "2");
                intent2.putExtra("TITLE", "Today");
                intent2.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent2);
                break;
            case R.id.pendingCVID:
                Intent intent3 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent3.putExtra("PLAN_ID", "3");
                intent3.putExtra("TITLE", "Pending");
                intent3.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent3);
                break;

            case R.id.featureCVID:
                Intent intent4 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent4.putExtra("PLAN_ID", "4");
                intent4.putExtra("TITLE", "Future");
                intent4.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent4);
                break;

            case R.id.agentsCVID:
                Intent intent5 = new Intent(DashBoardActivity.this, DesignationActivity.class);
                startActivity(intent5);
                break;

            case R.id.availabilityCVID:
                Intent intent6 = new Intent(DashBoardActivity.this, AvailabilityProjectsActivity.class);
                startActivity(intent6);
                break;

            case R.id.dueCustomersCVID:
                Intent intent7 = new Intent(DashBoardActivity.this, DueCustomersActivity.class);
                startActivity(intent7);
                break;

            case R.id.collectionsCVID:
                Intent intent8 = new Intent(DashBoardActivity.this, CollectionsActivity.class);
                startActivity(intent8);
                break;
            /*case R.id.saleDoneCVID:
                Intent intent9 = new Intent(DashBoardActivity.this, SaleDoneActivity.class);
                startActivity(intent9);
                break;*/
            case R.id.sourceCVID:
                Intent intent10 = new Intent(DashBoardActivity.this, SourceTypeActivity.class);
                startActivity(intent10);
                break;
            case R.id.searchCVID:
                Intent intent11 = new Intent(DashBoardActivity.this, SearchViewActivity.class);
                intent11.putExtra("TYPE", "SEARCH");
                startActivity(intent11);
                break;
            case R.id.incomingCVID:
                Intent intent12 = new Intent(DashBoardActivity.this, DashBoardIncomingCallsActivity.class);
                startActivity(intent12);
                break;
            case R.id.refreshCVID:

                CheckVerionControl();
                //getActivesSatus();
             /*   loadDashBoardNew();
                loadSkyCards();
                loadTopicDetials();*/
                break;
            case R.id.createLeadCVID:
                Intent intent14 = new Intent(DashBoardActivity.this, LeadCreateActivity.class);
                intent14.putExtra("NUMBER", "");
                startActivity(intent14);
                break;

            case R.id.topicdetailstxtCVID:
                Intent intent15 = new Intent(DashBoardActivity.this, TopicDetailsActivity.class);
//                intent14.putExtra("NUMBER", "");
                startActivity(intent15);
                break;

            case R.id.nextCallRLID:

                Intent intent16 = new Intent(DashBoardActivity.this, KnowlarityCalls.class);
                startActivity(intent16);

                //nextCallAlert();

                break;

            case  R.id.createivesCVID:
                Intent intent17=new Intent(DashBoardActivity.this,TopicDetails_imageview.class);
                startActivity(intent17);
                break;
        }
    }



    @Override
    public void onShortcutsItemClicked(ShortcutsResponse shortcutsResponse, int position, View v, ShortcutsAdapter.ShortcutsVH holder) {
        switch (shortcutsResponse.getId()) {
            case "1":
                startActivity(new Intent(DashBoardActivity.this, TeleSourceActivity.class));
                break;
            case "2":
                startActivity(new Intent(DashBoardActivity.this, DashBoardChangePasswordActivity.class));                break;
            case "3":
                startActivity(new Intent(DashBoardActivity.this, AttendanceMainActivity.class));
                break;
            case "4":
                startActivity(new Intent(DashBoardActivity.this, DirectMeeting.class));
                break;
            case "5":
                startActivity(new Intent(DashBoardActivity.this, SiteVisitSearchActivity.class));
                break;
            case "6":
                startActivity(new Intent(DashBoardActivity.this, CreateDayReportActivity.class));
                break;
            case "7":
                startActivity(new Intent(DashBoardActivity.this, FoldersActivity.class));
                break;
            case "8":
                startActivity(new Intent(DashBoardActivity.this, RecordingsActivity.class));
                break;
            case "9":
                startActivity(new Intent(DashBoardActivity.this, CreativesActivity.class));
                break;
        }

    }

    @Override
    public void onPlansItemClicked(PlansResponse shortcutsResponse, int position, View v, PlansAdapter.ShortcutsVH holder) {

        switch (shortcutsResponse.getId()) {
            case "1":
                Intent intent = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent.putExtra("PLAN_ID", "1");
                intent.putExtra("TITLE", "Overall");
                intent.putExtra("USER_ID", userID);
                intent.putExtra("USER_TYPE", userType);
                intent.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent);
                break;
            case "2":
                Intent intent2 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent2.putExtra("PLAN_ID", "2");
                intent2.putExtra("TITLE", "Today");
                intent2.putExtra("USER_ID", userID);
                intent2.putExtra("USER_TYPE", userType);
                intent2.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent2);
                break;
            case "3":
                Intent intent3 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent3.putExtra("PLAN_ID", "3");
                intent3.putExtra("TITLE", "Pending");
                intent3.putExtra("USER_ID", userID);
                intent3.putExtra("USER_TYPE", userType);
                intent3.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent3);
                break;

            case "4":
                Intent intent4 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent4.putExtra("PLAN_ID", "4");
                intent4.putExtra("TITLE", "Future");
                intent4.putExtra("USER_ID", userID);
                intent4.putExtra("USER_TYPE", userType);
                intent4.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent4);
                break;
            case "5":
                //startActivity(new Intent(DashBoardActivity.this, SiteVisitSearchActivity.class));
                Intent intent5 = new Intent(DashBoardActivity.this, NewLeadsDataActivity.class);
                intent5.putExtra("PLAN_ID", "5");
                intent5.putExtra("TITLE", "Tomorrow");
                intent5.putExtra("USER_ID", userID);
                intent5.putExtra("USER_TYPE", userType);
                intent5.putExtra(AppConstants.VIEW_TYPE, "Plans");
                startActivity(intent5);
                break;
            case "6":
                Intent intent6 = new Intent(DashBoardActivity.this, SourceTypeActivity.class);
                intent6.putExtra("USER_ID", userID);
                intent6.putExtra("USER_TYPE", userType);
                intent6.putExtra("ACTIVITY", "SOURCE");
                startActivity(intent6);
                break;
            case "7":
                Intent intent7 = new Intent(DashBoardActivity.this, SourceTypeActivity.class);
                intent7.putExtra("USER_ID", userID);
                intent7.putExtra("USER_TYPE", userType);
                intent7.putExtra("ACTIVITY", "PROJECT");
                startActivity(intent7);
                break;
        }

    }

    @Override
    public void onPostSalesItemClicked(PostSalesResponse shortcutsResponse, int position, View v, PostSalesAdapter.ShortcutsVH holder) {
        switch (shortcutsResponse.getId()) {
            case "1":

                Intent intent = new Intent(DashBoardActivity.this, DueCustomersActivity.class);
                startActivity(intent);

                break;
            case "2":

                Intent intent2 = new Intent(DashBoardActivity.this, CollectionsActivity.class);
                startActivity(intent2);

                break;
            case "3":

                Intent intent3 = new Intent(DashBoardActivity.this, SaleDoneActivity.class);
                startActivity(intent3);

                break;

            case "4":

                Intent intent4 = new Intent(DashBoardActivity.this, DesignationActivity.class);
                startActivity(intent4);

                break;
            case "5":

                Intent intent6 = new Intent(DashBoardActivity.this, AvailabilityProjectsActivity.class);
                startActivity(intent6);

                break;

        }
    }

    public class TabTittlesResponse {
        private String menu_id;
        private String menu_title;
        private String menu_url;
        private String user_id;
        private String user_type;

        public TabTittlesResponse(String menu_id, String menu_title, String menu_url, String user_id, String user_type) {
            this.menu_id = menu_id;
            this.menu_title = menu_title;
            this.menu_url = menu_url;
            this.user_id = user_id;
            this.user_type = user_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getMenu_id() {
            return menu_id;
        }

        public void setMenu_id(String menu_id) {
            this.menu_id = menu_id;
        }

        public String getMenu_title() {
            return menu_title;
        }

        public void setMenu_title(String menu_title) {
            this.menu_title = menu_title;
        }

        public String getMenu_url() {
            return menu_url;
        }

        public void setMenu_url(String menu_url) {
            this.menu_url = menu_url;
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<TabTittlesResponse> mCategoriesList;

        public ViewPagerAdapter(FragmentManager manager, ArrayList<TabTittlesResponse> _subCategoriesListItemDatas) {
            super(manager);
            this.mCategoriesList = _subCategoriesListItemDatas;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            if (mCategoriesList.size() > 0) {
                return mCategoriesList.get(position).getMenu_title();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mCategoriesList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ActivitiesFragment.newInstance(position, mCategoriesList.get(position).getMenu_id(), mCategoriesList.get(position).getMenu_url(), mCategoriesList.get(position).getMenu_title());
        }
    }

    private void permissioncheck() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE");

        /*if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("WRITE");*/


        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("READ");


        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("READ");

        /*if (!addPermission(permissionsList, Manifest.permission.SEND_SMS))
            permissionsNeeded.add("READ");*/

        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.CAPTURE_AUDIO_OUTPUT))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.CAPTURE_AUDIO_OUTPUT))
            permissionsNeeded.add("WRITE");

        if (!addPermission(permissionsList, Manifest.permission.PROCESS_OUTGOING_CALLS))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.READ_CALL_LOG))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("READ");

        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("READ");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    // Marshmallow+
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                } else {
                                    // Pre-Marshmallow
                                }

                            }
                        });
                return;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                // Pre-Marshmallow
            }

            return;
        } else {
            // Toast.makeText(this,"Permission",Toast.LENGTH_LONG).show();
            //LaunchApp();
        }

        //insertDummyContact();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        Boolean cond;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    //  return false;

                    cond = false;
            }
            //  return true;
            cond = true;

        } else {
            // Pre-Marshmallow
            cond = true;
        }

        return cond;

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashBoardActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @SuppressLint("NewApi")
    private void getChangeLocation() {
        String add = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address obj = addresses.get(0);
        langitudeStr = String.valueOf(obj.getLongitude());
        latitudeStr = String.valueOf(obj.getLatitude());
        add = obj.getAddressLine(0);
        locationS = obj.getLocality();
        areaS = obj.getSubLocality();
        //getGpsTrackLocationResponse();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (drawerItem.get(position).id) {
                case 0:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    break;
                case 1:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, TeleSourceActivity.class));
                    break;

                case 2:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, AttendanceMainActivity.class));
                    break;
                case 3:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, DirectMeeting.class));
                    break;

               /* case 5:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, EmployeeTrackActivity.class));
                    break;*/
                case 4:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, SiteVisitSearchActivity.class));
                    break;

                case 5:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    //startActivity(new Intent(DashBoardActivity.this, MainActivity.class));

                    Intent intent = new Intent(DashBoardActivity.this, CreateDayReportActivity.class);
                    /*Intent intent = new Intent(DashBoardActivity.this, ViewPdfActivity.class);
                    intent.putExtra("PRODUCT_ID", "" + userID);
                    intent.putExtra("PRODUCT_URL", AppConstants.GLOBAL_MAIN_URL+"createdayreport?");
                    intent.putExtra("PRODUCT_TYPE", "Create Day Report");*/
                    startActivity(intent);

                    break;

                case 6:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, FoldersActivity.class));
                    break;

              /*  case 9:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, RecordingsActivity.class));
                    break;*/

                case 7:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, CreativesActivity.class));
                    break;
                case 8:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(DashBoardActivity.this, AskMeActivity.class));
                    break;

                case 9:
                    startActivity(new Intent(DashBoardActivity.this, DashBoardChangePasswordActivity.class));
                    break;
                case 10:
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    logoutAlert();
                    break;

            }
        }
    }

    private void logoutAlert() {
        final Dialog dialog;
        dialog = new Dialog(DashBoardActivity.this);
        dialog.setContentView(R.layout.alert_logout);


        Button alertNoBtn = dialog.findViewById(R.id.noBtn);
        Button alertYesBtn = dialog.findViewById(R.id.yesBtn);
        alertNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        alertYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MySharedPreferences.setPreference(DashBoardActivity.this, "USER_ID", "");
                MySharedPreferences.setPreference(DashBoardActivity.this, "USER_EMAIL", "");
                MySharedPreferences.setPreference(DashBoardActivity.this, "USER_MOBILE", "");
                MySharedPreferences.setPreference(DashBoardActivity.this, "USER_NAME", "");
                MySharedPreferences.setPreference(DashBoardActivity.this, "USER_TYPE", "");
                MySharedPreferences.setPreference(DashBoardActivity.this, "REG_COUNT", "");
                MySharedPreferences.setPreference(DashBoardActivity.this, AppConstants.LOGOUT_STATUS, "");
                MySharedPreferences.clearPreferences(DashBoardActivity.this);
                Intent newIntent = new Intent(DashBoardActivity.this, LoginActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                finish();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       /* switch (item.getItemId()) {
            case R.id.dashboardseaerch:
                Intent intent = new Intent(DashBoardActivity.this, SearchViewActivity.class);
                intent.putExtra("TYPE", "SEARCH");
                startActivity(intent);
                break;

            case R.id.info:
                Intent intent2 = new Intent(DashBoardActivity.this, DashBoardInfoActivity.class);
                startActivity(intent2);
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupDrawerToggle() {
        mDrawerToggle = new androidx.appcompat.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.menu);
        mDrawerToggle.syncState();
    }

   /* @SuppressLint("NewApi")
    public void saveActivitiesArrayList(ArrayList<DashboardResponse> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }*/

    @SuppressLint("NewApi")
    public void saveCardsActivitiesArrayList(ArrayList<CardsResponse> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }



    //update App Info
    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    Log.d("VERSION_CODE", "" + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }

        checkNewAppVersionState();

        nameStr = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_NAME);
        user_designation = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_DESIGNATION);
        mobileStr = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_MOBILE);
        pic = MySharedPreferences.getPreferences(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.USER_PROFILE_PIC);

        String pageRefresh = MySharedPreferences.getPreferences(this, "" + AppConstants.PAGE_REFRESH);

        if (pageRefresh.equalsIgnoreCase("YES")){

            MySharedPreferences.setPreference(this,AppConstants.PAGE_REFRESH,"NO");
            CheckVerionControl();
           // getActivesSatus();
           /* loadDashBoardNew();
            loadSkyCards();
            loadTopicDetials();*/

        }


        sideMenuUsernameTVID.setText(nameStr);
        mobileNumberTVID.setText(mobileStr);

        if (!user_designation.equalsIgnoreCase("null")){
            designationTVID.setText(user_designation);
        }else{
            designationTVID.setText("User Designation");
        }


        /*Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).into(img_icon);
        Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).into(userPicID);*/



        try {
            Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(0).into(img_icon);
            Picasso.with(this).load(pic).error(R.drawable.pic_d).error(R.drawable.pic_d).placeholder(R.drawable.pic_d).rotate(0).into(userPicID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            storage.migrateLocalStorage();
        } catch (RuntimeException e) {
            ErrorDialog.Error(this, e);
        }
    }

    @Override
    protected void onPause() {
        stopMyService();
        super.onPause();
    }

    @Override
    protected void onStop() {
        stopMyService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopMyService();
        super.onDestroy();
        unregisterInstallStateUpdListener();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED) {

                }
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                //popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                        // Before starting an update, register a listener for updates.
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        // Start an update.
                        DashBoardActivity.this.startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        DashBoardActivity.this.startAppUpdateImmediate(appUpdateInfo);
                    }
                }
            }
        });
    }
    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    DashBoardActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    DashBoardActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(mDrawerLayout, getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.black_n));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */

    private void checkNewAppVersionState() {
        appUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener(
                        new OnSuccessListener<AppUpdateInfo>() {
                            @Override
                            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                                //FLEXIBLE:
                                // If the update is downloaded but not installed,
                                // notify the user to complete the update.
                                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                    DashBoardActivity.this.popupSnackbarForCompleteUpdateAndUnregister();
                                }

                                //IMMEDIATE:
                                if (appUpdateInfo.updateAvailability()
                                        == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                    // If an in-app update is already running, resume the update.
                                    DashBoardActivity.this.startAppUpdateImmediate(appUpdateInfo);
                                }
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    /*private fun startServiceViaWorker() {
        Log.e(TAG, "startServiceViaWorker called")
        val UNIQUE_WORK_NAME = "StartMyServiceViaWorker"
        val workManager = WorkManager.getInstance(this)

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        val request = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            16,
            TimeUnit.MINUTES
        )
            .build()

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        startService()
    }*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startWorker(){

       /* String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance(this);
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(MyWorker.class, 16, TimeUnit.MINUTES).build();
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,request);
        startMyService();*/
    }

    private void startMyService(){
        /*Log.e("TAG", "startService called");
        if (!MyService.isServiceRunning) {
            Intent serviceIntent =new Intent(this, MyService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }*/
    }
    private void stopMyService(){
        /*Log.e("TAG", "stopService called");
        if (MyService.isServiceRunning) {
            Intent serviceIntent =new Intent(this, MyService.class);
            stopService(serviceIntent);
        }*/
    }
}
