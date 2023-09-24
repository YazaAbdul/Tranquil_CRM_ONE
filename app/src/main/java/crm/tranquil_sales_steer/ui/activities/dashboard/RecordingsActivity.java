package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.RecordingsAdapter;
import crm.tranquil_sales_steer.ui.adapters.RecordingsAdapter;
import crm.tranquil_sales_steer.ui.models.RecordingsList;
import crm.tranquil_sales_steer.ui.models.RecordingsList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordingsActivity extends AppCompatActivity implements RecordingsAdapter.LeadClickListener {

    RecyclerView recordingsRVID;
    AppCompatTextView noDataTVID;
    AppCompatImageView noDataIVID;
    ProgressBar pb;
    TextView headerTittleTVID;
    RelativeLayout backRLID,noDataRLID;
    RecordingsAdapter recordingsAdapter;
    ArrayList<RecordingsList> recordingsLists = new ArrayList<>();

    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    LinearLayoutManager linearLayoutManager;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        Utilities.setStatusBarGradiant(this);

        recordingsRVID = findViewById(R.id.recordingsRVID);
        noDataTVID = findViewById(R.id.noDataTVID);
        noDataIVID = findViewById(R.id.noDataIVID);
        pb = findViewById(R.id.pb);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);
        backRLID = findViewById(R.id.backRLID);
        noDataRLID = findViewById(R.id.noDataRLID);

        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);
        });

        headerTittleTVID.setText("Recordings");

        if (Utilities.isNetworkAvailable(this)){
            loadRecordings();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }
        
    }

    private void loadRecordings() {
        //MySharedPreferences.setPreference(this, AppConstants.CLEAR, AppConstants.NO);
        recordingsLists.clear();
        start = 0;
        isLoading=false;
        isLastPage=false;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recordingsAdapter = new RecordingsAdapter(this,RecordingsActivity.this,recordingsLists);

        recordingsRVID.setAdapter(recordingsAdapter);
        recordingsRVID.setLayoutManager(linearLayoutManager);
        recordingsRVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.d("pagination","api called");
                isLastPage =false;
                isLoading = true;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                Log.d("pagination","is last page");
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                Log.d("pagination","is loading");
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadNextPage() {

        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<RecordingsList>> call = apiInterface.getAllRecordings(start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RecordingsList>>() {
            @Override
            public void onResponse(Call<ArrayList<RecordingsList>> call, Response<ArrayList<RecordingsList>> response) {
                isClickable = true;
                if (response.body() != null && response.code() == 200) {
                    recordingsLists = response.body();
                    recordingsAdapter.removeLoadingFooter();
                    isLoading = false;

                    if (recordingsLists.size() > 0) {
                        recordingsAdapter.addAll(recordingsLists);

                        if (recordingsLists.size() == 20) {
                            startedValues();
                            recordingsAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            recordingsAdapter.removeLoadingFooter();
                        }
                    } else {
                        recordingsAdapter.removeLoadingFooter();
                    }
                } else {
                    recordingsAdapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecordingsList>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                recordingsAdapter.removeLoadingFooter();
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void loadFirstPage() {
        pb.setVisibility(View.VISIBLE);
        recordingsRVID.setVisibility(View.GONE);
        noDataRLID.setVisibility(View.GONE);
        isClickable = false;
        
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<RecordingsList>> call = apiInterface.getAllRecordings(start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<RecordingsList>>() {
            @Override
            public void onResponse(Call<ArrayList<RecordingsList>> call, Response<ArrayList<RecordingsList>> response) {

                pb.setVisibility(View.GONE);
                recordingsRVID.setVisibility(View.VISIBLE);
                noDataRLID.setVisibility(View.GONE);
                //refreshFab.setVisibility(View.VISIBLE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    recordingsLists.clear();
                    recordingsLists = response.body();
                    if (recordingsLists.size() > 0) {
                        isClickable = true;
                        startedValues();
                        recordingsAdapter.addAll(recordingsLists);

                        if (recordingsLists.size() == 20) {
                            recordingsAdapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                        }
                    } else {
                        setErrorViews();
                    }
                } else {
                    setErrorViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecordingsList>> call, Throwable t) {
                setErrorViews();
            }
        });
    }
    

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        recordingsRVID.setVisibility(View.GONE);
        noDataRLID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 20;
    }


    static  String AUDIO_PATH =
            "";
    private MediaPlayer mediaPlayer;
    private int playbackPosition=0;


    @Override
    public void onLeadItemClick(RecordingsList response, View v, int pos, RecordingsAdapter.ItemViewHolder holder) {
        AUDIO_PATH = response.getSound_file();
        Uri a = Uri.parse(AUDIO_PATH);

        Intent viewMediaIntent = new Intent();
        viewMediaIntent.setAction(android.content.Intent.ACTION_VIEW);
        viewMediaIntent.setDataAndType(a, "audio/*");
        viewMediaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(viewMediaIntent);


       /* Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(response.getSound_file()), "audio/*");
        startActivity(intent);*/

       /* try {
            playAudio(AUDIO_PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void playAudio(String url) throws Exception {
        killMediaPlayer();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(mediaPlayer!=null) {
            try {
                mediaPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}