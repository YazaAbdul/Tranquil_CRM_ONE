package crm.tranquil_sales_steer.data.requirements;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import crm.tranquil_sales_steer.R;

/**
 * Created by venkei on 02-Jun-19.
 */
public class AlertUtilities {

    public static void bottomDisplayStaticAlert(Activity activity, String tittle, String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_validations);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        //dialog.getWindow().setGravity(Gravity.BOTTOM);
        TextView tittleTVID = dialog.findViewById(R.id.tittleTVID);
        TextView messageTVID = dialog.findViewById(R.id.messageTVID);
        TextView okBtn = dialog.findViewById(R.id.okBtn);
        //RelativeLayout cancelRLID = dialog.findViewById(R.id.cancelRLID);
        tittleTVID.setText(tittle);
        messageTVID.setText(message);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public static void SuccessAlertDialog(final Activity activity, String successMessage, String FILENAME) {
        MediaPlayer mPlayer;

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_animated_success);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mPlayer = MediaPlayer.create(activity, R.raw.ringtone);
        mPlayer.start();
        //LottieAnimationView animationView = dialog.findViewById(R.id.animationView);
        // openFile(activity, FILENAME, animationView);
        TextView successTVID = dialog.findViewById(R.id.successTVID);
        successTVID.setText(successMessage);

        Button okTVID = dialog.findViewById(R.id.okTVID);
        okTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.finish();
                activity.overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
            }
        });
    }


    public static void EmailSuccessAlertDialog(final Activity activity, String successMessage) {
        MediaPlayer mPlayer;
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_animated_email_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        mPlayer = MediaPlayer.create(activity, R.raw.ringtone);
        mPlayer.start();
        //LottieAnimationView animationView = dialog.findViewById(R.id.animationView);
        // openFile(activity, FILENAME, animationView);


        TextView successTVID = dialog.findViewById(R.id.successTVID);
        successTVID.setText(successMessage);


        Button okTVID = dialog.findViewById(R.id.okTVID);
        okTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public static void InvoicesAlertDialog(final Activity activity, String successMessage) {
        MediaPlayer mPlayer;

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_animated_invoices_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        mPlayer = MediaPlayer.create(activity, R.raw.ringtone);
        mPlayer.start();
        //LottieAnimationView animationView = dialog.findViewById(R.id.animationView);
        // openFile(activity, FILENAME, animationView);


        TextView successTVID = dialog.findViewById(R.id.successTVID);
        successTVID.setText(successMessage);

        Button okTVID = dialog.findViewById(R.id.okTVID);
        okTVID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                activity.finish();
                activity.overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);

            }
        });
    }


    public static void startAnimation(Activity activity) {
        activity.overridePendingTransition(R.anim.act_pull_in_right, R.anim.act_push_out_left);
    }

    public static void finishAnimation(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.act_pull_in_left, R.anim.act_push_out_right);
    }
}
