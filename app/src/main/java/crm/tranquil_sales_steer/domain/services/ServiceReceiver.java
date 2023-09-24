package crm.tranquil_sales_steer.domain.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import crm.tranquil_sales_steer.data.HandlerUtility;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.CrmSearchNumberResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by venkei on 09-Aug-19.
 */
public class ServiceReceiver extends BroadcastReceiver {

    String companyID, userID;
    private String tag="callService==>";

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static long callStartTime;
    private static long callEndTime;
    private static boolean isIncoming;

    private static String savedNumber;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {

            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e("INCOMINGCALL", ""+incomingNumber);
            Log.e(tag, "INCOMINGCALL "+incomingNumber);
            companyID = MySharedPreferences.getPreferences( context, "" + AppConstants.SharedPreferenceValues.USER_COMPANY_ID);
            userID = MySharedPreferences.getPreferences( context, "" + AppConstants.SharedPreferenceValues.USER_ID);
            Log.e(tag, "userID "+userID);
            Log.d("USER_IDS->", "" + userID);
            //findNumber(context,companyID,incomingNumber);
            Log.e(tag, "state "+state);
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                Log.d("Call Received State", "" + userID);
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                Log.d("Call Idle State", "" + userID);
            }

            if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {

                savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            } else {
                String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                int state1 = 0;
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    state1 = TelephonyManager.CALL_STATE_IDLE;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    state1 = TelephonyManager.CALL_STATE_OFFHOOK;
                } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    state1 = TelephonyManager.CALL_STATE_RINGING;
                }

                onCallStateChanged(context, state1, number);
            }



            /*Intent i = new Intent(context, CallCompleteActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("minutes",callStartTime);
            i.putExtra("seconds",callEndTime);
            i.putExtra("start_time",callStartTime);
            i.putExtra("end_time",callEndTime);
            context.startActivity(i);*/




        } catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, "Exception "+e.getMessage() +" lastState "+lastState);
        }
    }
    private void onCallStateChanged(Context context, int state, String number) throws ParseException {
        Log.e(tag, "onCallStateChanged "+state +" lastState "+lastState);

        if (lastState == state) {
            Log.e(tag, "onCallStateChanged returned "+state +" lastState "+lastState);
            //No change, debounce extras
            return;
        }



        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:
                Log.e(tag, "CALL_STATE_RINGING ");
                isIncoming = true;

                callStartTime = new Date().getTime();
                Log.e("CALLSTARTTIME", ""+callStartTime);
                Log.e(tag, "CALLSTARTTIME "+callStartTime);
                savedNumber = number;

                //Toast.makeText(context, "Incoming Call Ringing", Toast.LENGTH_SHORT).show();

                break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.e(tag, "CALL_STATE_OFFHOOK ");
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date().getTime();
                    Log.e("Call_Start_Time==>", "" + callStartTime);
                    //Toast.makeText(context, "Outgoing Call Started", Toast.LENGTH_SHORT).show();
                }

                break;

            case TelephonyManager.CALL_STATE_IDLE:
                Log.e(tag, "CALL_STATE_IDLE "+lastState);

                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    //   Toast.makeText(context, "Ringing but no pickup" + savedNumber + " Call time " + callStartTime + " Date " + new Date(), Toast.LENGTH_SHORT).show();
                } else if (isIncoming) {

                    //  Toast.makeText(context, "Incoming " + savedNumber + " Call time " + callStartTime, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(tag, "main obj "+lastState);
                    Calendar cal = Calendar.getInstance();
                    //@SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SS");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss.SS");
                    String strDate = sdf.format(cal.getTime());

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf1 = new SimpleDateFormat();
                    //sdf1.applyPattern("dd/MM/yyyy HH:mm:ss.SS");
                    sdf1.applyPattern("yyyy/dd/MM HH:mm:ss.SS");
                    Date date = sdf1.parse(strDate);



                    callEndTime = date.getTime();


                    Log.e("Call_End_Time==>", "" + callEndTime);


                    Log.e("Call_Time==>", "" + callStartTime + ", "+callEndTime);
                    Log.e(tag, "Call_Time "+callStartTime +" callEndTime "+callEndTime);
                    long diff = callEndTime - callStartTime;
                    long minutes = (diff / 1000) / 60;
                    long seconds = (diff / 1000) % 60;
                    //Toast.makeText(context, savedNumber + " Call time " + minutes + "  min  and " + seconds + " sec", Toast.LENGTH_SHORT).show();


                    /*Intent activityIntent = new Intent("crm.tranquil_sales_steer.domain.services.ServiceReceiver");
                    activityIntent.putExtra("minutes",minutes);
                    activityIntent.putExtra("seconds",seconds);
                    activityIntent.putExtra("start_time",callStartTime);
                    activityIntent.putExtra("end_time",callEndTime);

                    // send the intent to the activity
                    context.sendBroadcast(activityIntent);*/

                    if (HandlerUtility.insertHandler!=null){
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("minutes",minutes);
                            obj.put("seconds",seconds);
                            obj.put("start_time",callStartTime);
                            obj.put("end_time",callEndTime);
                            Log.e(tag, "insertHandler "+callStartTime +" callEndTime "+callEndTime);
                            HandlerUtility.insertHandler.obtainMessage(10,obj).sendToTarget();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }

                break;
        }
        lastState = state;
    }

    public static IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("crm.tranquil_sales_steer.domain.services.ServiceReceiver");
        return intentFilter;
    }

    private void findNumber(Context activity, String companyID, String incomingNumber) {
        incomingNumber = incomingNumber.replace("\u002B91", "");
        //Toast.makeText(activity, "" + incomingNumber, Toast.LENGTH_SHORT).show();
        searchNumberInCrm(activity, companyID, incomingNumber);
        insertNumberToDataBase(activity,userID,incomingNumber);
    }

    private void insertNumberToDataBase(final Context context, String userID, final String incomingNumber) {
        ApiInterface apiInterface=ApiClient.getClientNew((Activity) context.getApplicationContext()).create(ApiInterface.class);
        Call<ArrayList<StatusResponse>> call = apiInterface.storeIncomingCallsInDataBase(incomingNumber, userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<StatusResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StatusResponse>> call, Response<ArrayList<StatusResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    ArrayList<StatusResponse> leadResponse = response.body();
                    if (leadResponse != null) {
                        for (int i = 0; i < leadResponse.size(); i++) {
                            if (leadResponse.get(i).getStatus().equalsIgnoreCase("1")) {
                                Log.d("INSERTED_", " YES " + incomingNumber);
                            } else {
                                Log.d("INSERTED_", "NO");
                            }
                        }
                    } else {
                        Log.d("INSERTED_", "NO");
                    }
                } else {
                    Log.d("INSERTED_", "NO");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatusResponse>> call, Throwable t) {
                Log.d("INSERTED_", "NO");
            }
        });
    }

    private void searchNumberInCrm(final Context activity, final String companyID, final String incomingNumber) {

           ApiInterface apiInterface = ApiClient.getClientNew(activity).create(ApiInterface.class);
        Call<ArrayList<CrmSearchNumberResponse>> call = apiInterface.getCrmNumberSearchResponse(incomingNumber, companyID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CrmSearchNumberResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CrmSearchNumberResponse>> call, Response<ArrayList<CrmSearchNumberResponse>> response) {
                if (response.body() != null && response.code() == 200) {
                    ArrayList<CrmSearchNumberResponse> numberResponses = response.body();
                    if (numberResponses.size() > 0) {
                        for (int i = 0; i < numberResponses.size(); i++) {
                            getCustomerInfo(activity, companyID, numberResponses.get(i).getLead_id());
                        }

                    } else {
                        //insertNumberToDataBase(activity, userID, incomingNumber);
                    }
                } else {
                    //insertNumberToDataBase(activity, userID, incomingNumber);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CrmSearchNumberResponse>> call, Throwable t) {
                //insertNumberToDataBase(activity, userID, incomingNumber);
            }
        });
    }

    private void getCustomerInfo(Context activity, String companyID, String leadID) {

        /*Intent i = new Intent(activity, CallersDialogActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("COMPANY_ID", "" + companyID);
        i.putExtra("LEAD_ID", "" + leadID);
        activity.startActivity(i);*/

    }
}
