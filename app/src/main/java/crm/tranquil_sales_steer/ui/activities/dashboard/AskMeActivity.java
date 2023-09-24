package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Locale;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.TeleSourceTypeAdapter;
import crm.tranquil_sales_steer.ui.models.AskMeChoisesResponse;
import crm.tranquil_sales_steer.ui.models.AskMeResponse;
import crm.tranquil_sales_steer.ui.models.TeleSourceTypeResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskMeActivity extends AppCompatActivity {

    RelativeLayout backRLID,speakerRLID,searchRLID,volumeOffRLID,volumeRLID,voiceRLID;
    RecyclerView teleSourceTypeRVID;
    ProgressBar pb;
    LinearLayout noDataLLID;
    String userID,userType;
    ShimmerFrameLayout shimmerSFLID;
    AppCompatTextView askMeResultTVID;
    AskMeResponse askMeResponse;
    ArrayList<AskMeChoisesResponse> askMeChoisesResponses = new ArrayList<>();
    AppCompatEditText searchETID;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    String searchResultStr;
    TextToSpeech t1;
    String ip;

    //Google Analytics
    private FirebaseAnalytics analytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_me);
        Utilities.setStatusBarGradiant(this);

        analytics = FirebaseAnalytics.getInstance(this);

        backRLID = findViewById(R.id.backRLID);
        shimmerSFLID = findViewById(R.id.shimmerSFLID);
        askMeResultTVID = findViewById(R.id.askMeResultTVID);
        speakerRLID = (RelativeLayout) findViewById(R.id.speakerRLID);
        searchRLID = (RelativeLayout)findViewById(R.id.searchRLID);
        searchETID = findViewById(R.id.searchETID);
        volumeOffRLID = findViewById(R.id.volumeOffRLID);
        volumeRLID = findViewById(R.id.volumeRLID);
        voiceRLID = findViewById(R.id.voiceRLID);

        shimmerSFLID.setVisibility(View.GONE);
        voiceRLID.setVisibility(View.GONE);


        @SuppressLint("WifiManagerLeak") WifiManager wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
         ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



        searchRLID.setOnClickListener(v -> {
            searchValidation(ip);
        });

        speakerRLID.setOnClickListener(v -> {
            startVoiceInput();
        });

        userID = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_ID);
        userType = MySharedPreferences.getPreferences(this, "" + AppConstants.SharedPreferenceValues.USER_TYPE);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
            speakOff();
        });

        volumeRLID.setOnClickListener(v -> {


            speakOff();
            Toast.makeText(this, "Speak off", Toast.LENGTH_SHORT).show();
            //volumeOffRLID.setVisibility(View.VISIBLE);
            volumeRLID.setVisibility(View.GONE);

        });

        volumeOffRLID.setOnClickListener(v -> {


            /*volumeRLID.setVisibility(View.VISIBLE);
            volumeOffRLID.setVisibility(View.GONE);*/
        });


    }

    private void searchValidation(String ip) {

        searchResultStr = searchETID.getText().toString();

        if (TextUtils.isEmpty(searchResultStr)){
            Toast.makeText(this, "Please give search input", Toast.LENGTH_SHORT).show();
        }else {
            if (Utilities.isNetworkAvailable(this)) {
                loadSearchResult(searchResultStr,ip);
            } else {
                Toast.makeText(this, "Please give search input", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void startVoiceInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchETID.setText(result.get(0));
                    loadSearchResult(result.get(0),ip);
                }
                break;
            }

        }
    }

    private void loadSearchResult(String searchResultStr, String ip) {



        askMeResultTVID.setVisibility(View.GONE);
        voiceRLID.setVisibility(View.GONE);
        shimmerSFLID.setVisibility(View.VISIBLE);
        shimmerSFLID.startShimmer();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("text", searchResultStr)
                .addFormDataPart("user_id", userID)
                .addFormDataPart("ip", ip);

            RequestBody result = builder.build();

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<AskMeResponse> call = apiInterface.getAskMeResult(result);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<AskMeResponse>() {
            @Override
            public void onResponse(Call<AskMeResponse> call, Response<AskMeResponse> response) {

                if (response.body() != null && response.code() == 200){

                    Log.e("response==>", "" + "YES");

                    shimmerSFLID.setVisibility(View.GONE);
                    askMeResultTVID.setVisibility(View.VISIBLE);
                    voiceRLID.setVisibility(View.VISIBLE);

                    askMeResponse = response.body();

                    askMeChoisesResponses = askMeResponse.getChoices();




                    askMeResultTVID.setText(askMeChoisesResponses.get(0).getText());
                    speak();

                    try {




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        Log.e("result==>", "" + askMeChoisesResponses.get(0).getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(AskMeActivity.this, "Result not available", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AskMeResponse> call, Throwable t) {
                Toast.makeText(AskMeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void speakOff() {

        String voiceText = "";
        t1.speak(voiceText, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void speak() {

        String voiceText = askMeResultTVID.getText().toString();
        t1.speak(voiceText, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        speakOff();
    }

    @Override
    protected void onPause() {
        super.onPause();

        speakOff();
    }
}