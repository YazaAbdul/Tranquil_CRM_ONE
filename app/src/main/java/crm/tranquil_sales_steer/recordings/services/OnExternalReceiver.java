package crm.tranquil_sales_steer.recordings.services;

import android.content.Context;

public class OnExternalReceiver extends com.github.axet.androidlibrary.services.OnExternalReceiver {
    @Override
    public void onBootReceived(Context context) {
        super.onBootReceived(context);
        RecordingService.startIfEnabled(context);
    }
}
