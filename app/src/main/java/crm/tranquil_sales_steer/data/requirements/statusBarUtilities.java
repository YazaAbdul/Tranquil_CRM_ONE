package crm.tranquil_sales_steer.data.requirements;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import crm.tranquil_sales_steer.R;

/**
 * Created by venkei on 28-Jun-19.
 */
public class statusBarUtilities {

    public static void statusBarSetup(Activity _activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = _activity.getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                _activity.getWindow().setStatusBarColor(_activity.getResources().getColor(R.color.black_n));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    _activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    _activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    _activity.getWindow().setStatusBarColor(_activity.getResources().getColor(R.color.black_n));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void dasshBoardStatusBarSetup(Activity _activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = _activity.getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                _activity.getWindow().setStatusBarColor(_activity.getResources().getColor(R.color.black_n));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    _activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    _activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    _activity.getWindow().setStatusBarColor(_activity.getResources().getColor(R.color.black_n));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }



    public static void loginStatusBarSetup(Activity _activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = _activity.getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                _activity.getWindow().setStatusBarColor(_activity.getResources().getColor(R.color.colorPrimaryDark));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    _activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    _activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    _activity.getWindow().setStatusBarColor(_activity.getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
