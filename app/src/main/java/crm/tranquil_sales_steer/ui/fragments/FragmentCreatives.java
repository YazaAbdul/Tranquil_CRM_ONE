package crm.tranquil_sales_steer.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
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
import crm.tranquil_sales_steer.ui.adapters.CreateivesFragmentoneAdapter;
import crm.tranquil_sales_steer.ui.models.ImagescreateiveResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCreatives#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCreatives extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCreatives() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentCreatives.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentCreatives newInstance(String param1, String param2) {
        FragmentCreatives fragment = new FragmentCreatives();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        
    }
    RecyclerView creativesRVID;
    LinearLayoutManager linearLayoutManager;
   CreateivesFragmentoneAdapter adapter;
 //   CreativesAdapter creativesAdapter;
    ProgressBar pb;
    RelativeLayout createiveRVDetails;
    AppCompatTextView noDataTVID,greetingsTVID;
    SwitchCompat switchUser;
   ArrayList<ImagescreateiveResponse> imagescreateiveResponse = new ArrayList<>();
   // ArrayList<CreativesResponse> creativesResponses = new ArrayList<>();
    boolean isClickable = true;


    int start = 0;
    boolean isLoading;
    boolean isLastPage;
  private   Screenshot screenshot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_creatives, container, false);
        creativesRVID = view.findViewById(R.id.creativesRVID);
        pb = view.findViewById(R.id.pb);
        noDataTVID = view.findViewById(R.id.noDataTVID);
        greetingsTVID = view.findViewById(R.id.greetingsTVID);
        screenshot = new Screenshot(getContext());
        if (Utilities.isNetworkAvailable(requireActivity())) {
            loadCreatives();
        } else {
            Toast.makeText(getActivity(), "Please check your network", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    private void loadCreatives() {
        start = 0;
        isLoading=false;
        isLastPage=false;
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    //    adapter = new CreateivesFragmentoneAdapter(getActivity(), imagescreateiveResponse, this::onCreativeItemClick);
      adapter = new CreateivesFragmentoneAdapter(getActivity(), imagescreateiveResponse, this::onCreativeItemClick);



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
    private void loadNextPage() {

        Log.d("START_VALUE",""+start);
           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<ImagescreateiveResponse>> call = apiInterface.getimagescreative( start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ImagescreateiveResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ImagescreateiveResponse>> call, Response<ArrayList<ImagescreateiveResponse>> response) {
                isClickable=true;
                if(response.body()!=null && response.code()==200){
                    imagescreateiveResponse=response.body();
                    adapter.removeLoadingFooter();;
                    isLoading=false;

                    if (imagescreateiveResponse.size() > 0) {
                        adapter.addAll(imagescreateiveResponse);

                        if (imagescreateiveResponse.size() == 20) {
                            startedValues();
                            adapter.addLoadingFooter();
                        } else {
                            isLastPage = true;
                            adapter.removeLoadingFooter();
                        }
                    } else {
                        adapter.removeLoadingFooter();
                    }

                }
                else {
                    adapter.removeLoadingFooter();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ImagescreateiveResponse>> call, Throwable t) {
                Log.d("ERROR : ", "" + t.getMessage());
                adapter.removeLoadingFooter();
            }
        });
    }
    private void loadFirstPage() {

        pb.setVisibility(View.VISIBLE);
        creativesRVID.setVisibility(View.GONE);
        noDataTVID.setVisibility(View.GONE);
        isClickable = false;

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<ImagescreateiveResponse>> call = apiInterface.getimagescreative( start);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<ImagescreateiveResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ImagescreateiveResponse>> call, Response<ArrayList<ImagescreateiveResponse>> response) {
                pb.setVisibility(View.GONE);
                creativesRVID.setVisibility(View.VISIBLE);
                noDataTVID.setVisibility(View.GONE);
                isClickable = false;
                if (response.body() != null && response.code() == 200) {
                    imagescreateiveResponse.clear();
                    imagescreateiveResponse = response.body();
                    if (imagescreateiveResponse.size() > 0) {
                        isClickable = true;
                        startedValues();
                        adapter.addAll(imagescreateiveResponse);

                        if (imagescreateiveResponse.size() == 20) {
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
            public void onFailure(Call<ArrayList<ImagescreateiveResponse>> call, Throwable t) {
                setErrorViews();
            }
        });

    }
    private void startedValues() {
        start = start + 20;
    }

    private void setErrorViews() {
        isClickable = true;
        pb.setVisibility(View.GONE);
        creativesRVID.setVisibility(View.GONE);
        noDataTVID.setVisibility(View.VISIBLE);
    }


    @SuppressLint("ResourceAsColor")
    public void onCreativeItemClick(ImagescreateiveResponse response, View v, int pos, CreateivesFragmentoneAdapter.CreativesVH holder) {
        switch (v.getId()) {
            case R.id.whatsAppShareIVID:
                try {
                  //  Toast.makeText(getContext(), "clickd whatspapp", Toast.LENGTH_SHORT).show();
//                    holder.creativeMainRLID.setDrawingCacheEnabled(true);
//                    holder.creativeMainRLID.buildDrawingCache(true);
//                    Bitmap bitmap = Bitmap.createBitmap( holder.creativeMainRLID.getDrawingCache());
//                    holder.creativeMainRLID.setDrawingCacheEnabled(false);
//
//                    screenshot.shareScreenShot(bitmap);

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
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, String.valueOf(System.currentTimeMillis()), null);

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
                Toast.makeText(getActivity(), "Facebook is not installed in your device", Toast.LENGTH_SHORT).show();
            }else if (socialMediaType.equalsIgnoreCase("3")){

                Toast.makeText(getActivity(), "Instagram is not installed in your device", Toast.LENGTH_SHORT).show();


            }else if (socialMediaType.equalsIgnoreCase("4")){

                Toast.makeText(getActivity(), "Twitter is not installed in your device", Toast.LENGTH_SHORT).show();


            }
        }


    }
}