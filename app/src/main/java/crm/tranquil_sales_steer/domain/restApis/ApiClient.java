package crm.tranquil_sales_steer.domain.restApis;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${venkei} on 02-Apr-18.
 */

public class ApiClient {

    private static Retrofit retrofit = null;
    private static Retrofit newretrofit = null;
    private static Retrofit newretrofitdemo = null;

    public static Retrofit getClientNew(Activity activity) {
        String url = MySharedPreferences.getPreferences(activity, AppConstants.SharedPreferenceValues.GLOBAL_MAIN_URL);
        Log.e("URL==>","check "+url);
        if (url.isEmpty() ){
            url = AppConstants.GLOBAL_MAIN_EMPTY_URL;
        }
        Log.e("URL==>",""+url);
        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                /*.addInterceptor(loggingInterceptor)*/
                .build();


        Log.e("URL==>","client "+url);

        newretrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        Log.e("URL==>","clientend "+url);

        return newretrofit;
    }

    public static Retrofit getClientNew(Context activity) {
        String url = MySharedPreferences.getPreferences(activity, AppConstants.SharedPreferenceValues.GLOBAL_MAIN_URL);
        Log.e("URL==>","check "+url);
        if (url.isEmpty() ){
            url = AppConstants.GLOBAL_MAIN_EMPTY_URL;
        }
        Log.e("URL==>",""+url);
        Gson gson = new GsonBuilder().setLenient().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                /*.addInterceptor(loggingInterceptor)*/
                .build();


        Log.e("URL==>","client "+url);

        newretrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        Log.e("URL==>","clientend "+url);

        return newretrofit;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder().setLenient().create();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    /*.addInterceptor(loggingInterceptor)*/
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.GLOBAL_MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
            Log.e("URL==>","main url  "+AppConstants.GLOBAL_MAIN_URL);
        }

        return retrofit;
    }


 /*   public static Retrofit getClientdemo() {
        if (newretrofitdemo == null) {

            Gson gson = new GsonBuilder().setLenient().create();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    *//*.addInterceptor(loggingInterceptor)*//*
                    .build();

            newretrofitdemo = new Retrofit.Builder()
                    .baseUrl(AppConstants.GLOBAL_MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();

        }

        return newretrofitdemo;
    }*/


}