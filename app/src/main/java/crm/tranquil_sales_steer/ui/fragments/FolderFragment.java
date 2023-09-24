package crm.tranquil_sales_steer.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.AppConstants;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.activities.dashboard.SearchViewActivity;
import crm.tranquil_sales_steer.ui.adapters.AllUploadedFilesAdapter;
import crm.tranquil_sales_steer.ui.adapters.FoldersAdapter;
import crm.tranquil_sales_steer.ui.models.AllUploadFilesResponse;
import crm.tranquil_sales_steer.ui.models.FoldersResponse;
import crm.tranquil_sales_steer.ui.models.SendFileResponse;
import crm.tranquil_sales_steer.ui.models.StatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static crm.tranquil_sales_steer.ui.activities.dashboard.LeadHistoryActivity.mobileStr;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FolderFragment extends Fragment implements AllUploadedFilesAdapter.AllUploadedFilesClickListener {

    private static final String ARG_KEY_LEAD_ID = "lead_id";
    private static final String ARG_KEY_MOBILE = "lead_mobile";
    String VALUE_LEAD_ID,VALUE_LEAD_MOBILE,userID,userType;

    FoldersAdapter adapter;
    ArrayList<FoldersResponse> foldersResponses = new ArrayList<>();
    AllUploadedFilesAdapter allUploadedFilesAdapter;
    ArrayList<AllUploadFilesResponse> allUploadFilesResponses = new ArrayList<>();
    RecyclerView foldersRVID,allUploadedFilesRVID;
    ProgressBar pb;
    AppCompatTextView noDataTVID;
    TextView headerTittleTVID;
    RelativeLayout backRLID;
    String type,leadID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FolderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FolderFragment newInstance(String param1, String param2) {
        FolderFragment fragment = new FolderFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_folder, container, false);

        userID = MySharedPreferences.getPreferences(getContext(), "" + AppConstants.SharedPreferenceValues.USER_ID);

        foldersRVID = view.findViewById(R.id.foldersRVID);
        allUploadedFilesRVID =view.findViewById(R.id.allUploadedFilesRVID);
        pb = view.findViewById(R.id.pb);
        noDataTVID = view.findViewById(R.id.noDataTVID);

        if (Utilities.isNetworkAvailable(getContext())){
            loadFolders();
            loadAllUploadedFiles();
        }else {
            Toast.makeText(getContext(), "Please check your network", Toast.LENGTH_SHORT).show();
        }

        return inflater.inflate(R.layout.fragment_folder, container, false);
    }

    private void loadAllUploadedFiles() {
        pb.setVisibility(View.VISIBLE);
     //   allUploadedFilesRVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<AllUploadFilesResponse>> call = apiInterface.getAllUploadedFiles();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<AllUploadFilesResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AllUploadFilesResponse>> call, Response<ArrayList<AllUploadFilesResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.GONE);
                allUploadedFilesRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    allUploadFilesResponses = response.body();

                    if (allUploadFilesResponses.size() > 0){

                        allUploadedFilesRVID.setLayoutManager(new LinearLayoutManager(getActivity()));
                        allUploadedFilesAdapter=new AllUploadedFilesAdapter(allUploadFilesResponses,getActivity(), FolderFragment.this);
                     allUploadedFilesRVID.setAdapter(allUploadedFilesAdapter);
                        Toast.makeText(getContext(), "successfully in on response" , Toast.LENGTH_SHORT).show();
                    }else {
                        pb.setVisibility(View.GONE);
                        //noDataTVID.setVisibility(View.VISIBLE);
                     //   allUploadedFilesRVID.setVisibility(View.GONE);
                    }
                }else {
                    pb.setVisibility(View.GONE);
                    //noDataTVID.setVisibility(View.VISIBLE);
                  //  allUploadedFilesRVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AllUploadFilesResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                //noDataTVID.setVisibility(View.VISIBLE);
              //  foldersRVID.setVisibility(View.GONE);

            }
        });
    }

    private void loadFolders() {

        pb.setVisibility(View.VISIBLE);
        noDataTVID.setVisibility(View.GONE);
      //  foldersRVID.setVisibility(View.GONE);

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<ArrayList<FoldersResponse>> call = apiInterface.getFoldersTypes();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<FoldersResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<FoldersResponse>> call, Response<ArrayList<FoldersResponse>> response) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.GONE);
                foldersRVID.setVisibility(View.VISIBLE);

                if (response.body() != null && response.code() == 200){

                    foldersResponses = response.body();

                    if (foldersResponses.size() > 0){
                        foldersRVID.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new FoldersAdapter(foldersResponses, getActivity());
                        foldersRVID.setAdapter(adapter);
                        Toast.makeText( getContext(), "succesfully inside folder", Toast.LENGTH_SHORT).show();
                    }else {
                        pb.setVisibility(View.GONE);
                        noDataTVID.setVisibility(View.VISIBLE);
                     //   foldersRVID.setVisibility(View.GONE);
                    }

                }else {
                    pb.setVisibility(View.GONE);
                    noDataTVID.setVisibility(View.VISIBLE);
                  //  foldersRVID.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FoldersResponse>> call, Throwable t) {

                pb.setVisibility(View.GONE);
                noDataTVID.setVisibility(View.VISIBLE);
           //     foldersRVID.setVisibility(View.GONE);

            }
        });
    }

    public Fragment newInstance(String customerID) {
        FolderFragment subFrag = new FolderFragment();
        Bundle b = new Bundle();
        //b.putString(ARG_KEY_LEAD_ID, customerId);
        b.putString(ARG_KEY_MOBILE, mobileStr);
        subFrag.setArguments(b);
        return subFrag;

    }
    @Override
    public void onUploadedFilesClickItem(AllUploadFilesResponse response, View v, int pos, AllUploadedFilesAdapter.AllUploadedFilesVH holder) {

        switch (v.getId()) {
            case R.id.deleteRLID:
                getDeleteResponse(response.getId());
                break;

            case R.id.sendRLID:
                if (type != null && type.equalsIgnoreCase("FROM_LEADS")) {

                    getSendFileToLead(leadID,response.getId());

                } else {

                    Intent intent = new Intent(getContext(), SearchViewActivity.class);
                    intent.putExtra("TYPE", "FOLDERS");
                    startActivity(intent);
                }
                break;
        }

    }
    private void getDeleteResponse(String id) {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<StatusResponse> call = apiInterface.getDeleteFile(id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<StatusResponse>() {
            @Override
            public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {

                if (response.body() != null && response.code() == 200){

                    StatusResponse statusResponse = response.body();

                    if (statusResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(getContext(), "File Deleted Successfully", Toast.LENGTH_SHORT).show();

                        loadAllUploadedFiles();

                    }else {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {

                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void getSendFileToLead(String lead_id, String c_id) {

           ApiInterface apiInterface = ApiClient.getClientNew(getActivity()).create(ApiInterface.class);
        Call<SendFileResponse> call = apiInterface.getSendFile(userID,lead_id,c_id);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<SendFileResponse>() {
            @Override
            public void onResponse(Call<SendFileResponse> call, Response<SendFileResponse> response) {

                if (response.body() != null && response.code() == 200){

                    SendFileResponse sendFileResponse = response.body();

                    if (sendFileResponse.getStatus().equalsIgnoreCase("1")){
                        Toast.makeText(getContext(), "File Sent Successfully", Toast.LENGTH_SHORT).show();


                    }else {

                        Toast.makeText(getContext(), "Something Wrong", Toast.LENGTH_SHORT).show();


                        /*if (searchResponses.get(0).getLead_email().isEmpty()){
                            Toast.makeText(SearchViewActivity.this, "There is no email", Toast.LENGTH_SHORT).show();
                        }else {


                        }*/

                    }
                }
            }



            @Override
            public void onFailure(Call<SendFileResponse> call, Throwable t) {

                Toast.makeText(getContext(), "Something Wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }
}