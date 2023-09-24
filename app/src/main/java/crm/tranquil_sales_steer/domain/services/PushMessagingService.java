package crm.tranquil_sales_steer.domain.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.activities.dashboard.LeadHistoryActivity;

public class PushMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        JSONObject object = new JSONObject(remoteMessage.getData());

        Log.e("push==>","1 : "+object);
        if (object!=null){
            getPush(object);
        }
    }

    private void getPush(JSONObject object){
        long notificatioId = System.currentTimeMillis();
        String body = object.optString("body");
        String title = object.optString("title");
        String lead_id = object.optString("lead_id");
        String name = object.optString("name");
        String number = object.optString("number");
        String pic = object.optString("pic");

        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent1 = new Intent(this, LeadHistoryActivity.class);
        intent1.putExtra("CUSTOMER_ID",""+lead_id);
        intent1.putExtra("CUSTOMER_NAME",""+name);
        intent1.putExtra("CUSTOMER_MOBILE",number);
        intent1.putExtra("pageFrom", true);
      /*  *//*intent1.putExtra("ACTIVITY_ID","1");
        intent1.putExtra("ACTIVITY_NAME","Customer Meet");*//*
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        //PendingIntent intent2 = this.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent intent2
                = PendingIntent.getActivity(
                this, 0, intent1,
                0);*/

        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent2 = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent1, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01", "hello", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(15000);

        Bitmap bitmap = getBitmap(pic);


        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle  = bigTextStyle.bigText(body);

        Notification notification = builder
                .setContentTitle(title)
                .setContentText(body)
                .setSound(alarmsound)
                //.setSmallIcon(R.drawable.notification_icon)
                .setSmallIcon(R.drawable.favicon)
                .setLargeIcon(bitmap)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .setStyle(bigTextStyle)
                .setGroupSummary(true)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify((int)notificatioId, notification);

    }

    public static Integer getRandomNumberString() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999);
        return number;
        //return ""+number+ts;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("generated_token==>",""+s);
    }

    private Bitmap getBitmap(String StrUrl){

         try {
            URL url = new URL(StrUrl);

            URLConnection connection = url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
        return     BitmapFactory.decodeStream(input);

        } catch (MalformedURLException e) {
            e.printStackTrace();
             return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
