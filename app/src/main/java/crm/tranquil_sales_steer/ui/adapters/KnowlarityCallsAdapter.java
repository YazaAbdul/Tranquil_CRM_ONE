package crm.tranquil_sales_steer.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.ui.models.KnowlarityCallsResponse;
import crm.tranquil_sales_steer.ui.models.KnowlarityCallsResponse;

public class KnowlarityCallsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_ITEM = 1;
    private static final int VIEW_LOADING = 0;
    private boolean isLoadingAdded = false;
    Activity activity;
    ArrayList<KnowlarityCallsResponse> knowlarityCallsResponses = new ArrayList<>();
    KnowlarityCallClickListener knowlarityCallClickListener;

    public KnowlarityCallsAdapter(Activity activity, ArrayList<KnowlarityCallsResponse> knowlarityCallsResponses, KnowlarityCallClickListener knowlarityCallClickListener) {
        this.activity = activity;
        this.knowlarityCallsResponses = knowlarityCallsResponses;
        this.knowlarityCallClickListener = knowlarityCallClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (i) {
            case VIEW_ITEM:
                viewHolder = new KnowlarityCallVH(inflater.inflate(R.layout.knowlarity_call_item, parent, false));
                break;

            case VIEW_LOADING:
                viewHolder = new LoadingViewHolder(inflater.inflate(R.layout.progressbar1, parent, false));
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        switch (getItemViewType(i)) {
            case VIEW_ITEM:
                KnowlarityCallVH itemVH = (KnowlarityCallVH) holder;
                KnowlarityCallsResponse item = knowlarityCallsResponses.get(i);


                itemVH.mobileTVID.setText(item.getCustomer_name());
                itemVH.callIDTVID.setText(item.getDuration());
                itemVH.destinationTVID.setText(item.getMobile_number());
                itemVH.statusTVID.setText(item.getCall_end());

                renderWebPage(item.getAudio_file(), itemVH.audioWVID);


                //itemVH.playerRLID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                itemVH.convertRLID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                itemVH.skipCVID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                itemVH.deleteCVID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                itemVH.convertCVID.setOnClickListener(v -> knowlarityCallClickListener.onKnowlarityCallItemClick(item,v,i,itemVH));
                /*itemVH.playerRLID.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(item.getAudio_file()), "audio/*");
                    activity.startActivity(intent);
                });*/

                itemVH.downRLID.setOnClickListener(v -> {
                    itemVH.audioWVID.setVisibility(View.VISIBLE);
                    itemVH.campaignStatusLLID.setVisibility(View.VISIBLE);
                    itemVH.upRLID.setVisibility(View.VISIBLE);
                    itemVH.downRLID.setVisibility(View.GONE);
                });



                itemVH.upRLID.setOnClickListener(v -> {
                    itemVH.audioWVID.setVisibility(View.GONE);
                    itemVH.upRLID.setVisibility(View.GONE);
                    itemVH.campaignStatusLLID.setVisibility(View.GONE);
                    itemVH.downRLID.setVisibility(View.VISIBLE);
                });

                itemVH.visibleRLID.setOnClickListener(v -> {
                    itemVH.audioWVID.setVisibility(View.VISIBLE);
                    itemVH.campaignStatusLLID.setVisibility(View.VISIBLE);
                    itemVH.upRLID.setVisibility(View.VISIBLE);
                    itemVH.goneRLID.setVisibility(View.VISIBLE);
                    itemVH.downRLID.setVisibility(View.GONE);
                    itemVH.visibleRLID.setVisibility(View.GONE);
                });

                itemVH.goneRLID.setOnClickListener(v -> {
                    itemVH.audioWVID.setVisibility(View.GONE);
                    itemVH.upRLID.setVisibility(View.GONE);
                    itemVH.goneRLID.setVisibility(View.GONE);
                    itemVH.campaignStatusLLID.setVisibility(View.GONE);
                    itemVH.downRLID.setVisibility(View.VISIBLE);
                    itemVH.visibleRLID.setVisibility(View.VISIBLE);
                });

                break;

            case VIEW_LOADING:
                LoadingViewHolder loadingVH = (LoadingViewHolder) holder;
                loadingVH.progressBar1.setVisibility(View.VISIBLE);
                break;
        }

    }



    @SuppressLint("SetJavaScriptEnabled")
    protected void renderWebPage(String urlToRender,WebView mWebView) {
        Log.e("url==>",urlToRender);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //loading.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(urlToRender);
    }



    @Override
    public int getItemCount() {

        return knowlarityCallsResponses == null ? 0 : knowlarityCallsResponses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == knowlarityCallsResponses.size() - 1 && isLoadingAdded) ? VIEW_LOADING : VIEW_ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new KnowlarityCallsResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = knowlarityCallsResponses.size() - 1;
        KnowlarityCallsResponse result = getItem(position);
        if (result != null) {
            knowlarityCallsResponses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(KnowlarityCallsResponse movie) {
        knowlarityCallsResponses.add(movie);
        notifyItemInserted(knowlarityCallsResponses.size() - 1);
    }

    public void addAll(List<KnowlarityCallsResponse> moveResults) {
        for (KnowlarityCallsResponse result : moveResults) {
            add(result);
        }
    }


    public KnowlarityCallsResponse getItem(int position) {
        return knowlarityCallsResponses.get(position);
    }

    public class KnowlarityCallVH extends RecyclerView.ViewHolder{


        AppCompatTextView mobileTVID,callIDTVID,destinationTVID,statusTVID;
        RelativeLayout playerRLID,downRLID,upRLID,convertRLID,visibleRLID,goneRLID;
        WebView audioWVID;
        LinearLayout campaignStatusLLID;
        CardView skipCVID,deleteCVID,convertCVID;

        public KnowlarityCallVH(@NonNull View itemView) {
            super(itemView);

            mobileTVID = itemView.findViewById(R.id.mobileTVID);
            callIDTVID = itemView.findViewById(R.id.callIDTVID);
            destinationTVID = itemView.findViewById(R.id.destinationTVID);
            statusTVID = itemView.findViewById(R.id.statusTVID);
            playerRLID = itemView.findViewById(R.id.playerRLID);
            audioWVID = itemView.findViewById(R.id.audioWVID);
            downRLID = itemView.findViewById(R.id.downRLID);
            upRLID = itemView.findViewById(R.id.upRLID);
            convertRLID = itemView.findViewById(R.id.convertRLID);
            visibleRLID = itemView.findViewById(R.id.visibleRLID);
            goneRLID = itemView.findViewById(R.id.goneRLID);
            campaignStatusLLID = itemView.findViewById(R.id.campaignStatusLLID);
            skipCVID = itemView.findViewById(R.id.skipCVID);
            deleteCVID = itemView.findViewById(R.id.deleteCVID);
            convertCVID = itemView.findViewById(R.id.convertCVID);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar1 = itemView.findViewById(R.id.progressBar1);
        }
    }

    public interface KnowlarityCallClickListener {
        void onKnowlarityCallItemClick(KnowlarityCallsResponse response, View v, int pos, KnowlarityCallVH holder);
    }
}
