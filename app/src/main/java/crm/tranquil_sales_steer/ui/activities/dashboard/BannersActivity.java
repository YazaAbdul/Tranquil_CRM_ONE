package crm.tranquil_sales_steer.ui.activities.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.ui.adapters.BannersAdapter;
import crm.tranquil_sales_steer.ui.models.CardsResponse;

public class BannersActivity extends AppCompatActivity {

    BannersAdapter bannersAdapter;
    RecyclerView cardsRVID;
    ArrayList<CardsResponse> cardsResponses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banners);

        cardsRVID = findViewById(R.id.cardsRVID);

        //loadSkyCards();
    }

    /*private void loadSkyCards() {

        cardsRVID.setVisibility(View.GONE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<CardsResponse>> call = apiInterface.getSkycards();
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<CardsResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ArrayList<CardsResponse>> call, Response<ArrayList<CardsResponse>> response) {

                cardsRVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    cardsResponses = response.body();
                    if (cardsResponses.size() > 0) {

                        bannersAdapter = new BannersAdapter( cardsResponses,BannersActivity.this);
                        cardsRVID.setAdapter( bannersAdapter);
                        *//*for (int i = 0; i < cardsResponses.size(); i++) {
                            //cardsRVID.setFocusable(false);
                            //MySharedPreferences.setArrayListData(DashBoardActivity.this, "" + AppConstants.SharedPreferenceValues.OFFLINE_HOME_URL, dashboardResponses);
                            //saveCardsActivitiesArrayList(cardsResponses, "" + AppConstants.SharedPreferenceValues.OFFLINE_HOME_URL);
                        }*//*
                    } else {
                        Toast.makeText(BannersActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(BannersActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<ArrayList<CardsResponse>> call, Throwable t) {
                Toast.makeText(BannersActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}