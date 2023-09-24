package crm.tranquil_sales_steer.recordings.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnUpgradeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RecordingService.startIfEnabled(context);
    }
}
