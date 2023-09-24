package crm.tranquil_sales_steer.ui.activities.remainders;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import crm.tranquil_sales_steer.ui.activities.dashboard.OutGoingCalls;

public class PhoneCallReceiver extends BroadcastReceiver {

    Context c;
    private String outgoing,phonenumber;

    @Override
    public void onReceive(Context context, Intent intent) {

        c = context;
        setResultData(null);
        setResultCode(Activity.RESULT_OK);
        phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        phonenumber = getResultData();
        try {
            if (phonenumber == null)
            {
                phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            }
            setResultData(phonenumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // callActionHandler.postDelayed(runRingingActivity, 1000);

        c = context;

       /* if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
        {
            outgoing = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            int state = 2;
            Intent intentPhoneCall = new Intent(c, OutGoingCalls.class);
            intentPhoneCall.putExtra("incomingnumber", outgoing);
            intentPhoneCall.putExtra("state", state);
            intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intentPhoneCall);
        }*/

        try
        {
            TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();
            tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        catch (Exception e)
        {
            Log.e("Phone Receive Error", " " + e);
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener
    {
        public void onCallStateChanged(final int state, final String incomingNumber)
        {
            Handler callActionHandler = new Handler();
            Runnable runRingingActivity = new Runnable()
            {
                @Override
                public void run()
                {
                    /*if (state == 1)
                    {
                        Intent intentPhoneCall = new Intent(c, OutGoingCalls.class);
                        intentPhoneCall.putExtra("incomingnumber", incomingNumber);
                        intentPhoneCall.putExtra("state", state);
                        intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(intentPhoneCall);
                    }*/
                }
            };

            if (state == 1)
            {
                callActionHandler.postDelayed(runRingingActivity, 100);
            }

            if (state == 0)
            {
                callActionHandler.removeCallbacks(runRingingActivity);
            }
        }
}
}
