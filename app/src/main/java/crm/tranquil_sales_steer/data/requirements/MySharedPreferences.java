package crm.tranquil_sales_steer.data.requirements;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by venkei on 18-Mar-19.
 */
public class MySharedPreferences {

    public static Context mContext;
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;

    public static void setPreference(Context context, String key, String value) {
        mContext = context;
        editor = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getPreferences(Context context, String key) {
        mContext = context;
        prefs = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE);
        String text = prefs.getString(key, "");
        return text;
    }


    public static void setArrayListData(Context context, String key, ArrayList list){
        mContext = context;
        prefs = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    /*public static ArrayList<String> getArrayListData(Context context,String key){
        mContext = context;
        prefs = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }*/

    public static ArrayList getArrayListData(Context context, String key,ArrayList list){
        mContext = context;
        prefs = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Object>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void clearArrayListData(Context context,String key){
        mContext = context;
        prefs = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = prefs.edit();
        String json1 = gson.toJson("");
        editor.putString("" + key, "");
        editor.apply();
    }

    public static void clearPreferences(Activity context) {
        mContext = context;
        SharedPreferences settings = mContext.getSharedPreferences("crm", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

}
