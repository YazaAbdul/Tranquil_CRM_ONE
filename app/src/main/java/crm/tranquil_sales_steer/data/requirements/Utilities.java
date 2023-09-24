package crm.tranquil_sales_steer.data.requirements;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.kaopiz.kprogresshud.KProgressHUD;

import crm.tranquil_sales_steer.R;

/**
 * Created by venkei on 19-Mar-19.
 */
public class Utilities {
    public static NetworkInfo netInfo;
    public static AlertDialog.Builder builder;



    private static final int CALL_PERMISSION_REQUEST_CODE = 123;

    public static void makePhoneCall(String number,Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_REQUEST_CODE);
        } else {
            initiateCall(number,activity);
        }
    }

    private static void initiateCall(String number,Activity activity) {
        Intent callIntent = new Intent(Intent.ACTION_CALL );
        callIntent.setData(Uri.parse("tel:" + number));
        activity. startActivity(callIntent);
    }

    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_WHITE = 1;
    public final static int THEME_BLUE = 2;


    public static boolean isNetworkAvailable(Context _Context) {
        ConnectivityManager cm = (ConnectivityManager) _Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static void AlertDaiolog(Activity activity, String tittle, String discrption, int visibility, String yes, String no) {
        LayoutInflater inflater = from_Context(activity);
        View layout;
        if (inflater != null) {
            layout = inflater.inflate(R.layout.alert_validations, (ViewGroup) activity.findViewById(R.id.customAlert));
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(layout);
            dialog.dismiss();
            TextView tv = layout.findViewById(R.id.tittleTVID);
            tv.setText(tittle);
            TextView disc = layout.findViewById(R.id.messageTVID);
            disc.setText(discrption);
            TextView yesBtn = layout.findViewById(R.id.okBtn);
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            yesBtn.setText((CharSequence) no);
        }
    }

    public static void finishAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }

    public static void showToast(Activity activity, String msg) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, activity.findViewById(R.id.custom_toast_container));

        TextView text = layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static void startAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
    }


    private static LayoutInflater from_Context(Context context) {
        LayoutInflater layoutInflater = null;
        try {
            if (context != null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            if (layoutInflater == null) {
                throw new AssertionError("LayoutInflater not found.");
            }
        } catch (Exception e) {
            Log.w("HARI-->DEBUG", e);
            layoutInflater = null;
        }
        return layoutInflater;
    }

    public static void showProgress(KProgressHUD hud, Activity activity, String title, String description) {

        if (hud == null) {
            hud = KProgressHUD.create(activity)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Loading..")
                    .setDetailsLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }
    }

    public static void dismissProgress(KProgressHUD hud) {
        if (hud != null && hud.isShowing()) {
            hud.dismiss();
        }
    }


    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.SheetDialog);
                break;
            case THEME_WHITE:
                activity.setTheme(R.style.SheetDialog);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.SheetDialog2);
                break;
        }
    }

    public static String CapitalText(String title) {

        String upperString = null;
        try {
            upperString = title.substring(0, 1).toUpperCase() + title.substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return upperString;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            @SuppressLint("UseCompatLoadingForDrawables") Drawable background = activity.getResources().getDrawable(R.drawable.bg_gradient_main);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            //window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }




}
