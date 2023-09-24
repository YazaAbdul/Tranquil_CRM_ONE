package crm.tranquil_sales_steer.data.requirements;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by venkei on 06-Jul-19.
 */
public class AppConstants {


    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.GLOBAL_MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return retrofit;
    }


  public static  String GLOBAL_MAIN_URL="https://tranquilcrmone.com/mobileapp/";
  public static  String GLOBAL_MAIN_EMPTY_URL="https://tranquilcrmone.com/";
 // public static  String GLOBAL_MAIN_URL_DEMO="https://demo.tranquilcrmone.com/mobileapp/";
//  public static  String GLOBAL_MAIN_URL="https://tranquilcrmone.com/mobileapp/";

 //   public static String GLOBAL_MAIN_URL = MySharedPreferences.getPreferences("GLOBAL_MAIN_URL, "");

 //  public static String GLOBAL_MAIN_URL=MySharedPreferences.getPreferences(context, SharedPreferenceValues.GLOBAL_MAIN_URL);



    public void someMethod(Context context) {
        // Retrieve the value from SharedPreferences
      /*  if(GLOBAL_MAIN_URL==null){
            GLOBAL_MAIN_URL = MySharedPreferences.getPreferences(context, SharedPreferenceValues.GLOBAL_MAIN_URL);
        }*/



    }

 //   public static  final String GLOBAL_MAIN_URL=MySharedPreferences.getPreferences(context, SharedPreferenceValues.GLOBAL_MAIN_URL);
    public static final String GLOBAL_MAIN_URL_FOR_ICONS="https://tranquilcrmone.com/attachments/";
    public static final String VIDEO_URL="https://tranquilcrmone.com/Leads/mobilevideos";




    public static final String FIRE_BASE_ADD_TASK="add_task";
    public static final String FIRE_BASE_REMAINDER="remainders";

    //Shared preferences
    public static final String PUNCH_IN_STATUS = "PUNCH_IN_STATUS";
    public static final String PUNCH_IN_ID = "PUNCH_IN_ID";
    public static final String PUNCH_IN_AREA = "PUNCH_IN_AREA";
    public static final String PUNCH_IN_TIME = "PUNCH_IN_TIME";
    public static final String PUNCH_OUT_AREA = "PUNCH_OUT_AREA";
    public static final String PUNCH_OUT_TIME = "PUNCH_OUT_TIME";
    public static final String ATTENDANCE_DATE = "PUNCH_IN_DATE";
    public static final String VIEW_TYPE = "VIEW_TYPE";
    public static final String PAGE_REFRESH = "PAGE_REFRESH";


    public static final String VEHICLE_TYPE = "VEHICLE_TYPE";

    public static final String VEHICLE_BIKE = "bike";

    public static final String VEHICLE_CAR = "car";



    public static final String LOGOUT_STATUS = "LOGOUT_STATUS";
    public static final String LEAD_ID="LEAD_ID";
    public static final String LEAD_MOBILE="LEAD_MOBILE";
    public static final String LEAD_EMAIL="LEAD_EMAIL";
    public static final String PRODUCT_ID="PRODUCT_ID";
    public static final String TAX_TYPE_ID="PRODUCT_ID";
    public static final String QUOTATION_ID="QUOTATION_ID";

    public static final String YES = "YES";


    public class SharedPreferenceValues{
        public static final String SAVED_FCM_ID="saved-fcm-id";
        public static final String USER_ID="USER_ID";
        public static final String USER_TYPE="USER_TYPE";
        public static final String USER_NAME="USER_NAME";
        public static final String UPDATED_PIC="UPDATED_PIC";
        public static final String USER_MOBILE="MOBILE_NUMBER";
        public static final String USER_COMPANY_ID="USER_COMPANY_ID";
        public static final String USER_COMPANY_NAME="USER_COMPANY_NAME";
        public static final String USER_DESIGNATION="USER_DESIGNATION";
        public static final String USER_EMAIL_ID="USER_EMAIL_ID";
        public static final String USER_REG_COUNT="6";
        public static final String USER_REG_PASSWORD="USER_PASSWORD";
        public static final String USER_EXPIRY_DAYS="USER_EXPIRY_DAYS_COUNT";
        public static final String USER_PROFILE_PIC="USER_PROFILE_PIC";



        //payment values
        public static final String PAYMENT_SUCCESS="PAYMENT_SUCCESS";
        public static final String PAYMENT_SUCCESS_YES="YES";
        public static final String PAYMENT_SUCCESS_NO="NO";


        //offline
        public static final String OFFLINE_HOME_URL="MENU_OFFLINE_HOME_URL";
        public static final String OFFLINE_INVOICE_DATA="OFFLINE_INVOICE_DATE";
        public static final String OFFLINE_RECEIVABLES="OFFLINE_RECEIVABLES";

        public static final String OFFLINE_PAYABLE="OFFLINE_PAYABLE";
        public static final String OFFLINE_TAX_CALCULATIONS="OFFLINE_TAX_CALCULATIONS";
        public static final String OFFLINE_REQUIREMENTS="OFFLINE_REQUIREMENTS";
        public static final String OFFLINE_CLOSED_BUSINESS="OFFLINE_CLOSED_BUSINESS";
        public static final String OFFLINE_COMPLETED="OFFLINE_COMPLETED";
       public static final String GLOBAL_MAIN_URL = "GLOBAL_MAIN_URL";
        public static final String LOGO_URL = "LOG_URL";


        //   public final String GLOBAL_MAIN_URL = MySharedPreferences.getPreferences("GLOBAL_MAIN_URL", "");

    }

    //database_values
    public static final String HOME_DATA_BASE="home_categories.db";
    public static Handler applicationHandler = new Handler();
}
