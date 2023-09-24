package crm.tranquil_sales_steer.ui.activities.site_visits;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;

import crm.tranquil_sales_steer.R;
import crm.tranquil_sales_steer.data.requirements.MySharedPreferences;
import crm.tranquil_sales_steer.data.requirements.Utilities;
import crm.tranquil_sales_steer.domain.restApis.ApiClient;
import crm.tranquil_sales_steer.domain.restApis.ApiInterface;
import crm.tranquil_sales_steer.ui.adapters.SearchAdapter;
import crm.tranquil_sales_steer.ui.models.SearchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteVisitSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Toolbar addconnectiontoolbar;

    ArrayList<SearchResponse> searchResponses = new ArrayList<>();
    SearchAdapter searchAdapter;

    ListView updateSiteVisitLVID;
    TextView noResultTVID;
    ProgressBar siteVisitPB;
    String userID, userType;
    String searchname;
    int start = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_visit_update);
        Utilities.setStatusBarGradiant(this);

        addconnectiontoolbar = (Toolbar) findViewById(R.id.toolbars);
        setSupportActionBar(addconnectiontoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userID = MySharedPreferences.getPreferences(SiteVisitSearchActivity.this, "USER_ID");
        userType = MySharedPreferences.getPreferences(SiteVisitSearchActivity.this, "USER_TYPE");

        updateSiteVisitLVID = (ListView) findViewById(R.id.updateSiteVisitLVID);
        noResultTVID = (TextView) findViewById(R.id.noResultTVID);
        siteVisitPB = (ProgressBar) findViewById(R.id.siteVisitPB);

      /*  if (IntCheck.isOnline(this)) {
            getSearchResponse();
        } else {
            Toast.makeText(this, "There is no internet connection...!", Toast.LENGTH_SHORT).show();
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crmoptionopen, menu);

        MenuItem menuItem = menu.findItem(R.id.menusearchfull);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconifiedByDefault(false);
        EditText searchEditText =  searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.parseColor("#B6B6B6"));
        searchEditText.setHintTextColor(Color.parseColor("#B6B6B6"));
        searchEditText.setHint("Search mobile or name");
        ImageView magImage = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        magImage.setVisibility(View.GONE);
        magImage.setImageDrawable(null);
        searchView.setOnQueryTextListener(SiteVisitSearchActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override


    public boolean onQueryTextSubmit(String query) {

        if (query.length()>3){
            searchname = query.replace(" ", "%20");
            noResultTVID.setVisibility(View.GONE);
            siteVisitPB.setVisibility(View.VISIBLE);
            updateSiteVisitLVID.setVisibility(View.GONE);


            if (Utilities.isNetworkAvailable(SiteVisitSearchActivity.this)) {
                try {
                    getSearchResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "There is no internet connection...!", Toast.LENGTH_SHORT).show();
            }
        }


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.length()>3){
            searchname = newText.replace(" ", "%20");

            noResultTVID.setVisibility(View.GONE);
            siteVisitPB.setVisibility(View.VISIBLE);
            updateSiteVisitLVID.setVisibility(View.GONE);


            if (Utilities.isNetworkAvailable(SiteVisitSearchActivity.this)) {
                try {
                    getSearchResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "There is no internet connection...!", Toast.LENGTH_SHORT).show();
            }
        }



        return false;
    }


    private void getSearchResponse() {
        siteVisitPB.setVisibility(View.VISIBLE);
           ApiInterface apiInterface = ApiClient.getClientNew(this).create(ApiInterface.class);
        Call<ArrayList<SearchResponse>> call = apiInterface.updateSiteVisit(searchname, userID);
        Log.e("api==>",call.request().toString());
        call.enqueue(new Callback<ArrayList<SearchResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchResponse>> call, Response<ArrayList<SearchResponse>> response) {
                siteVisitPB.setVisibility(View.GONE);
                noResultTVID.setVisibility(View.GONE);
                updateSiteVisitLVID.setVisibility(View.VISIBLE);
                if (response.body() != null && response.code() == 200) {
                    searchResponses = response.body();
                    try {
                        if (searchResponses != null && searchResponses.size() > 0) {

                            for (int i = 0; i < searchResponses.size(); i++) {
                                searchAdapter = new SearchAdapter(SiteVisitSearchActivity.this, searchResponses);
                                updateSiteVisitLVID.setAdapter(searchAdapter);
                                MySharedPreferences.setPreference(SiteVisitSearchActivity.this, "customer_id", "" + searchResponses.get(i).getCustomer_name());
                            }
                        } else {
                            updateSiteVisitLVID.setVisibility(View.GONE);
                            noResultTVID.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    updateSiteVisitLVID.setVisibility(View.GONE);
                    noResultTVID.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SearchResponse>> call, Throwable t) {
                siteVisitPB.setVisibility(View.GONE);
                noResultTVID.setVisibility(View.VISIBLE);
                updateSiteVisitLVID.setVisibility(View.GONE);
            }
        });

    }
}
