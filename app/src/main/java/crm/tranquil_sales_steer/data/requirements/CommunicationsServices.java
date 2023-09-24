package crm.tranquil_sales_steer.data.requirements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import crm.tranquil_sales_steer.data.HandlerUtility;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.models.CommunicationsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by venkei on 25-Jul-19.
 */
public class CommunicationsServices {


    public static void InsertCommunication(Activity activity, final String type, String leadID, String userID, String message, String subject) {

        //Utilities.showProgress(kProgressHUD,activity,"Loading...","Please wait....");


        ApiInterface apiInterface=ApiClient.getClientNew(activity).create(ApiInterface.class);
        Call<ArrayList<CommunicationsResponse>> call = apiInterface.getInsertCommunicationResponse(leadID,message,subject,type,userID,false);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CommunicationsResponse>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ArrayList<CommunicationsResponse>> call, Response<ArrayList<CommunicationsResponse>> response) {

               // Utilities.dismissProgress(kProgressHUD);

                if (response.body() != null && response.code() == 200) {
                    ArrayList<CommunicationsResponse> statusResponses = response.body();
                    if (statusResponses != null && statusResponses.size() > 0) {
                        for (int i = 0; i < statusResponses.size(); i++) {
                            if (statusResponses.get(i).getStatus().equals("1")) {

                                Log.e("Before_handler_caller_id==>","" + statusResponses.get(i).getCall_id());

                                if (HandlerUtility.insertHandler!=null){
                                    try {
                                        JSONObject obj = new JSONObject();
                                        obj.put("id",statusResponses.get(0).getCall_id());
                                        Log.e("caller_id","" + statusResponses.get(i).getCall_id());
                                        HandlerUtility.insertHandler.obtainMessage(12,obj).sendToTarget();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Log.d("INSERT_MESSAGE", "" + type + " : Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            } else {
                                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                            }
                        }
                    } else {
                        Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                        Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                    }
                } else {
                    Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                    Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommunicationsResponse>> call, Throwable t) {
                Log.d("INSERT_MESSAGE", "" + type + " : Not Inserted");
                Log.e("INSERT_MESSAGE", "" + type + " : Inserted");
            }
        });

    }

}
