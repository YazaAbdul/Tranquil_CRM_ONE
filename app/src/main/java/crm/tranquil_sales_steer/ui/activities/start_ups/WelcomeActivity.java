package crm.tranquil_sales_steer.ui.activities.start_ups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AlertUtilities;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.UserSession;
import crm.tranquil_sales_steer.data.requirements.statusBarUtilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.DashBoardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private UserSession prefManager;

    public static String logincheck = "", splascheck = "6", regchec = "";

    Button loginBtn;
    TextView aboutTVID;
    String passwordStr, emailStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logincheck = MySharedPreferences.getPreferences(WelcomeActivity.this, "" + AppConstants.SharedPreferenceValues.USER_REG_COUNT);
        emailStr = MySharedPreferences.getPreferences(WelcomeActivity.this, "" + AppConstants.SharedPreferenceValues.USER_EMAIL_ID);
        passwordStr = MySharedPreferences.getPreferences(WelcomeActivity.this, "" + AppConstants.SharedPreferenceValues.USER_REG_PASSWORD);

        prefManager = new UserSession(this);

        if (!prefManager.isFirstTimeLaunch()) {
            if (logincheck.equals(splascheck)) {
                checkingLoginCredentials();
            } else {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
            finish();
        }


        setContentView(R.layout.activity_welcome);

        AlertUtilities.startAnimation(this);
        statusBarUtilities.statusBarSetup(this);


        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        btnSkip = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setVisibility(View.GONE);
        aboutTVID = findViewById(R.id.aboutTVID);
        aboutTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.tranquilcrm.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3};


        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });


        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void checkingLoginCredentials() {

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<LoginActivity.LoginResponse>> call = apiInterface.getLoginResponse(emailStr, passwordStr,"4");
        Log.d("LOGIN_URL", "" + AppConstants.GLOBAL_MAIN_URL + "login?email_id=" + emailStr + "&password=" + passwordStr);
        call.enqueue(new Callback<ArrayList<LoginActivity.LoginResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LoginActivity.LoginResponse>> call, Response<ArrayList<LoginActivity.LoginResponse>> response) {

                if (response.body() != null && response.code() == 200) {
                    ArrayList<LoginActivity.LoginResponse> loginResponses = response.body();
                    if (loginResponses != null && loginResponses.size() > 0) {
                        for (int i = 0; i < loginResponses.size(); i++) {

                            if (loginResponses.get(i).getStatus().equals("1")) {
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_EXPIRY_DAYS, "" + loginResponses.get(i).getExpiry_days());

                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_PROFILE_PIC, "" + loginResponses.get(i).getUser_pic());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_COMPANY_NAME, "" + loginResponses.get(i).getCompany_name());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_ID, "" + loginResponses.get(i).getUser_id());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_TYPE, "" + loginResponses.get(i).getUser_type());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_NAME, "" + loginResponses.get(i).getUser_name());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_MOBILE, "" + loginResponses.get(i).getMobile_number());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_EMAIL_ID, "" + loginResponses.get(i).getEmail_id());
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_COMPANY_ID, "" + loginResponses.get(i).getCompany_id());

                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_EXPIRY_DAYS, "" + loginResponses.get(i).getExpiry_days());


                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_REG_COUNT, "6");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_REG_PASSWORD, "" + passwordStr);


                                startActivity(new Intent(WelcomeActivity.this, DashBoardActivity.class));
                            } else {
                                Toast.makeText(WelcomeActivity.this, "Session expired \nLogin with new credentials", Toast.LENGTH_SHORT).show();
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_ID, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_TYPE, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_NAME, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_MOBILE, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_EMAIL_ID, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_COMPANY_ID, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_REG_COUNT, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_REG_PASSWORD, "");
                                MySharedPreferences.setPreference(WelcomeActivity.this, AppConstants.SharedPreferenceValues.USER_EXPIRY_DAYS, "" );
                                //startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));

                                Intent newIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(newIntent);
                                finish();
                            }
                        }
                    } else {
                        Intent newIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(newIntent);
                        finish();
                    }
                } else {
                    Intent newIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LoginActivity.LoginResponse>> call, Throwable t) {

                Intent newIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                finish();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);


        if (logincheck.equals(splascheck)) {
            startActivity(new Intent(WelcomeActivity.this, DashBoardActivity.class));
        } else {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        }
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);

                loginBtn.setVisibility(View.VISIBLE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
