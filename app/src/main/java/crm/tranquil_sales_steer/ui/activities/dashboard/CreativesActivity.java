package crm.tranquil_sales_steer.ui.activities.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.PaginationScrollListener;
import crm.tranquil_sales_steer.data.requirements.Screenshot;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.CreativesAdapter;
import crm.tranquil_sales_steer.ui.models.CreativesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreativesActivity extends AppCompatActivity implements CreativesAdapter.CreativeClickListener {

    RelativeLayout backRLID;
    ProgressBar pb;
    AppCompatTextView noDataTVID;
    TextView headerTittleTVID;
    String cpPicStr,secondPicStr;
    private Screenshot screenshot;

    int start = 0;
    boolean isLoading;
    boolean isLastPage;
    public static boolean isClickable = true;
    LinearLayoutManager linearLayoutManager;
    CreativesAdapter adapter;
    ArrayList<CreativesResponse> creativesResponses = new ArrayList<>();
    RecyclerView creativesRVID;


    @SuppressLint({"SetTextI18n", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatives);

        Utilities.setStatusBarGradiant(this);

        screenshot = new Screenshot(this);


        backRLID = findViewById(R.id.backRLID);

        pb = findViewById(R.id.pb);
        noDataTVID = findViewById(R.id.noDataTVID);
        headerTittleTVID = findViewById(R.id.headerTittleTVID);


        backRLID.setOnClickListener(v -> {
            Utilities.finishAnimation(this);

        });

        headerTittleTVID.setText("Creatives");





        if (Utilities.isNetworkAvailable(this)){
            loadCreatives();
        }else {
            Toast.makeText(this, "Please check your network", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadCreatives() {

        start = 0;
        isLoading=false;
        isLastPage=false;
        creativesRVID = findViewById(R.id.creativesRVID);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new CreativesAdapter(this,creativesResponses,CreativesActivity.this);

        creativesRVID.setAdapter(adapter);
        creativesRVID.setLayoutManager(linearLayoutManager);
        creativesRVID.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

    private void loadFirstPage() {

        pb.setVisibility(View.VISIBLE);
        creativesRVID.setVisibility(View.GONE);
        noDataTVID.setVisibility(View.GONE);
        isClickable = false;

           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CreativesResponse>> call = apiInterface.getCreatives( start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CreativesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CreativesResponse>> call, Response<ArrayList<CreativesResponse>> response) {

                pb.setVisibility(View.GONE);
                creativesRVID.setVisibility(View.VISIBLE);
                noDataTVID.setVisibility(View.GONE);
                //refreshFab.setVisibility(View.VISIBLE);
                isClickable = false;

                if (response.body() != null && response.code() == 200) {
                    creativesResponses.clear();
                    creativesResponses = response.body();
                    if (creativesResponses.size() > 0) {
                        isClickable = true;
                        startedValues();
                        adapter.addAll(creativesResponses);

                        if (creativesResponses.size() == 20) {
                            adapter.addLoadingFooter();
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
            public void onFailure(Call<ArrayList<CreativesResponse>> call, Throwable t) {
                setErrorViews();
            }
        });

    }

    private void loadNextPage() {

        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CreativesResponse>> call = apiInterface.getCreatives( start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CreativesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<CreativesResponse>> call, Response<ArrayList<CreativesResponse>> response) {
                isClickable = true;
                if (response.body() != null && response.code() == 200) {
                    creativesResponses = response.body();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    if (creativesResponses.size() > 0) {
                        adapter.addAll(creativesResponses);

                        if (creativesResponses.size() == 20) {
                            startedValues();
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            adapter.removeLoadingFooter();
                        }
                    } else {
                        adapter.removeLoadingFooter();
                    }
                } else {
                    adapter.removeLoadingFooter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CreativesResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }


    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        creativesRVID.setVisibility(View.GONE);
        noDataTVID.setVisibility(View.VISIBLE);
    }

    private void startedValues() {
        start = start + 20;
    }


    @Override
    public void onCreativeItemClick(CreativesResponse response, View v, int pos, CreativesAdapter.CreativesVH holder) {
        switch (v.getId()) {
            case R.id.whatsAppShareIVID:
                try {
                    holder.creativeMainRLID.setDrawingCacheEnabled(true);
                    holder.creativeMainRLID.buildDrawingCache(true);
                    Bitmap bitmap = Bitmap.createBitmap( holder.creativeMainRLID.getDrawingCache());
                    holder.creativeMainRLID.setDrawingCacheEnabled(false);

                    screenshot.shareScreenShot(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //screenshot.takeScreenshotFromView(holder.creativeMainRLID);


                break;

            case R.id.facebookShareIVID:

                try {
                    holder.creativeMainRLID.setDrawingCacheEnabled(true);
                    holder.creativeMainRLID.buildDrawingCache(true);
                    Bitmap bitmap = Bitmap.createBitmap( holder.creativeMainRLID.getDrawingCache());
                    holder.creativeMainRLID.setDrawingCacheEnabled(false);

                    shareToSocialMedia(bitmap,"2");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.instagramShareIVID:

                try {
                    holder.creativeMainRLID.setDrawingCacheEnabled(true);
                    holder.creativeMainRLID.buildDrawingCache(true);
                    Bitmap bitmap = Bitmap.createBitmap( holder.creativeMainRLID.getDrawingCache());
                    holder.creativeMainRLID.setDrawingCacheEnabled(false);

                    shareToSocialMedia(bitmap,"3");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.twitterShareIVID:

                try {
                    holder.creativeMainRLID.setDrawingCacheEnabled(true);
                    holder.creativeMainRLID.buildDrawingCache(true);
                    Bitmap bitmap = Bitmap.createBitmap( holder.creativeMainRLID.getDrawingCache());
                    holder.creativeMainRLID.setDrawingCacheEnabled(false);

                    shareToSocialMedia(bitmap,"4");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


        }

    }

    private void shareToSocialMedia(Bitmap bitmap, String socialMediaType) {

        Log.e("Screen==>","" +"YES");

        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(CreativesActivity.this.getContentResolver(), bitmap, String.valueOf(System.currentTimeMillis()), null);

            Log.e("screenshot==>",""+ path);

            Uri imageUri = Uri.parse(path);

            if (socialMediaType.equalsIgnoreCase("2")) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("image/png");
                waIntent.setPackage("com.facebook.katana");
                waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(waIntent);
            } else if (socialMediaType.equalsIgnoreCase("3")){
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("image/png");
                waIntent.setPackage("com.instagram.android");
                waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(waIntent);
            }else if (socialMediaType.equalsIgnoreCase("4")){
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("image/png");
                waIntent.setPackage("com.twitter.android");
                waIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(waIntent);
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (socialMediaType.equalsIgnoreCase("2")){
                Toast.makeText(this, "Facebook is not installed in your device", Toast.LENGTH_SHORT).show();
            }else if (socialMediaType.equalsIgnoreCase("3")){

                Toast.makeText(this, "Instagram is not installed in your device", Toast.LENGTH_SHORT).show();


            }else if (socialMediaType.equalsIgnoreCase("4")){

                Toast.makeText(this, "Twitter is not installed in your device", Toast.LENGTH_SHORT).show();


            }
        }


    }
}