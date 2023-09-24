package crm.tranquil_sales_steer.recordings.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.github.axet.androidlibrary.app.NotificationManagerCompat;
import com.github.axet.androidlibrary.app.Prefs;
import com.github.axet.androidlibrary.preferences.OptimizationPreferenceCompat;
import com.github.axet.androidlibrary.widgets.NotificationChannelCompat;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.Locale;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CallApplication extends com.github.axet.audiolibrary.app.MainApplication {

    public static final String PREFERENCE_DELETE = "delete";
    public static final String PREFERENCE_DELETE_OFF = Prefs.PrefString(R.string.delete_off);
    public static final String PREFERENCE_DELETE_1DAY = Prefs.PrefString(R.string.delete_1day);
    public static final String PREFERENCE_DELETE_1WEEK = Prefs.PrefString(R.string.delete_1week);
    public static final String PREFERENCE_DELETE_1MONTH = Prefs.PrefString(R.string.delete_1month);
    public static final String PREFERENCE_DELETE_3MONTH = Prefs.PrefString(R.string.delete_3month);
    public static final String PREFERENCE_DELETE_6MONTH = Prefs.PrefString(R.string.delete_6month);
    public static final String PREFERENCE_FORMAT = "format";
    public static final String PREFERENCE_CALL = "call";
    public static final String PREFERENCE_OPTIMIZATION = "optimization";
    public static final String PREFERENCE_NEXT = "next";
    public static final String PREFERENCE_DETAILS_CONTACT = "_contact";
    public static final String PREFERENCE_DETAILS_CALL = "_call";
    public static final String PREFERENCE_SOURCE = "source";
    public static final String PREFERENCE_FILTER_IN = "filter_in";
    public static final String PREFERENCE_FILTER_OUT = "filter_out";
    public static final String PREFERENCE_DONE_NOTIFICATION = "done_notification";
    public static final String PREFERENCE_MIXERPATHS = "mixer_paths";
    public static final String PREFERENCE_VOICE = "voice";
    public static final String PREFERENCE_VOLUME = "volume";
    public static final String PREFERENCE_VERSION = "version";
    public static final String PREFERENCE_BOOT = "boot";

    public static final String CALL_OUT = "out";
    public static final String CALL_IN = "in";

    public NotificationChannelCompat channelPersistent;
    public NotificationChannelCompat channelStatus;

    @SuppressWarnings("unchecked")
    @SuppressLint("PrivateApi")
    public static String getprop(String key) {
        try {
            Class klass = Class.forName("android.os.SystemProperties");
            Method method = klass.getMethod("get", String.class);
            return (String) method.invoke(null, key);
        } catch (Exception e) {
            Log.d(TAG, "no system prop", e);
            return null;
        }
    }

    public static CallApplication from(Context context) {
        return (CallApplication) com.github.axet.audiolibrary.app.MainApplication.from(context);
    }

    private void checkUserLogin() {
        String splashCheck = MySharedPreferences.getPreferences( getApplicationContext(), AppConstants.SharedPreferenceValues.USER_REG_COUNT);

        if (splashCheck.equals("6")) {

            String userID = MySharedPreferences.getPreferences( getApplicationContext(), AppConstants.SharedPreferenceValues.SAVED_FCM_ID);
            Log.e("fcm_id==>", userID);
            if (Utilities.isNetworkAvailable(getApplicationContext())) {
                getGeneratedFcmID(userID);
            }

        }
    }

    private void getGeneratedFcmID(String oldFcmId) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String newToken = task.getResult();
                    Log.e("generated==>", newToken);
                    Log.e("old==>", oldFcmId);
                    if (!oldFcmId.equals(newToken)) {
                        Log.e("new_token==>", newToken);
                        sendServerToFcmID(newToken);
                    }
                });
    }

    private void sendServerToFcmID(String fcmID) {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        String userID = MySharedPreferences.getPreferences( getApplicationContext(), AppConstants.SharedPreferenceValues.USER_ID);
        Log.e("data==>", android_id + " , " + userID);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<MessageResponse> call = apiInterface.updateFCMID(userID, android_id, fcmID, "android");
        Log.e("api==>", call.request().toString());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                Log.e("response==>", new Gson().toJson(response.body()));
                if (response.body() != null && response.code() == 200) {
                    if (response.body().getMsg().equals("FCM ID Updated")) {
                        Log.e("fcm_id_update==>", "success");
                        MySharedPreferences.setPreference(getApplicationContext(), AppConstants.SharedPreferenceValues.SAVED_FCM_ID, fcmID);
                    }
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e("fcm_id_update==>", "error : " + t.getMessage());
            }
        });
    }


    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            Log.e("handler==>", "called");
            switch (msg.what) {
                case 1:
                    Log.e("handler==>", "checking method");
                    checkUserLogin();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        AppConstants.applicationHandler = mHandler;
        checkUserLogin();
        channelPersistent = new NotificationChannelCompat(this, "icon", "Persistent Icon", NotificationManagerCompat.IMPORTANCE_LOW);
        channelStatus = new NotificationChannelCompat(this, "status", "Status", NotificationManagerCompat.IMPORTANCE_LOW);

        OptimizationPreferenceCompat.setPersistentServiceIcon(this, true);

        switch (getVersion(PREFERENCE_VERSION, R.xml.call_pref_general)) {
            case -1:
                SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor e = shared.edit();
                MixerPaths m = new MixerPaths();
                if (!m.isSupported() || !m.isEnabled()) {
                    e.putString(CallApplication.PREFERENCE_ENCODING, Storage.EXT_3GP);
                }
                SharedPreferences.Editor edit = shared.edit();
                edit.putInt(PREFERENCE_VERSION, 2);
                edit.commit();
                break;
            case 0:
                version_0_to_1();
                break;
            case 1:
                version_1_to_2();
                break;
        }
    }

    void version_0_to_1() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.putFloat(PREFERENCE_VOLUME, shared.getFloat(PREFERENCE_VOLUME, 0) + 1); // update volume from 0..1 to 0..1..4
        edit.putInt(PREFERENCE_VERSION, 1);
        edit.commit();
    }

    void version_1_to_2() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = shared.edit();
        edit.remove(PREFERENCE_SORT);
        edit.putInt(PREFERENCE_VERSION, 2);
        edit.commit();
    }

    public static String getContact(Context context, Uri f) {
        final SharedPreferences shared = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String p = getFilePref(f) + PREFERENCE_DETAILS_CONTACT;
        return shared.getString(p, null);
    }

    public static void setContact(Context context, Uri f, String id) {
        final SharedPreferences shared = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String p = getFilePref(f) + PREFERENCE_DETAILS_CONTACT;
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(p, id);
        editor.commit();
    }

    public static String getCall(Context context, Uri f) {
        final SharedPreferences shared = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String p = getFilePref(f) + PREFERENCE_DETAILS_CALL;
        return shared.getString(p, null);
    }

    public static void setCall(Context context, Uri f, String id) {
        final SharedPreferences shared = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String p = getFilePref(f) + PREFERENCE_DETAILS_CALL;
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(p, id);
        editor.commit();
    }

    public static String getString(Context context, Locale locale, int id, Object... formatArgs) {
        return getStringNewRes(context, locale, id, formatArgs);
    }

    public static String getStringNewRes(Context context, Locale locale, int id, Object... formatArgs) {
        Resources res;
        Configuration conf = new Configuration(context.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= 17)
            conf.setLocale(locale);
        else
            conf.locale = locale;
        res = new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), conf);
        String str;
        if (formatArgs.length == 0)
            str = res.getString(id);
        else
            str = res.getString(id, formatArgs);
        new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), context.getResources().getConfiguration()); // restore side effect
        return str;
    }

    public static String[] getStrings(Context context, Locale locale, int id) {
        Resources res;
        Configuration conf = new Configuration(context.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= 17)
            conf.setLocale(locale);
        else
            conf.locale = locale;
        res = new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), conf);
        String[] str;
        str = res.getStringArray(id);
        new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), context.getResources().getConfiguration()); // restore side effect
        return str;
    }
}
