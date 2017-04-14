package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import co.dtechsystem.carefer.Adapters.ShopsListRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;

public class ShopDetailsOrderActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    private String mshopName, mshopType, mshopRating, mserviceType, mbrands;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details_order);
        SetUpLeftbar();
        GetDataForViews();
        SetDataTOViews();
    }

    // Get Views Data
    public void GetDataForViews() {
        if (intent != null) {
            mshopName = intent.getStringExtra("shopName");
            mshopType = intent.getStringExtra("shopType");
            mshopRating = intent.getStringExtra("shopRating");
            mserviceType = intent.getStringExtra("serviceType");
            mbrands = intent.getStringExtra("brands");
        }
    }

    // Set views data
    public void SetDataTOViews() {
        loading.show();
        APiGetModelsYears();
        aQuery.id(R.id.tv_shop_name_shop_details_order).text(mshopName);
        aQuery.id(R.id.tv_shop_type_shop_details_order).text(mshopType);
        aQuery.id(R.id.rb_shop_rating_shop_details_order).rating(Float.parseFloat(mshopRating));

//        ArrayList<String> years = new ArrayList<String>();
//        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
//        for (int i = 1900; i <= thisYear; i++) {
//            years.add(Integer.toString(i));
//        }
//        ArrayAdapter<String> modelyearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        List<String> serviceType = Arrays.asList(mserviceType.split(","));
        ArrayAdapter<String> servicetypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, serviceType);
        aQuery.id(R.id.sp_srvice_type_shop_details_order).adapter(servicetypeAdapter);
        List<String> brands = Arrays.asList(mbrands.split(","));
        ArrayAdapter<String> brandsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, brands);
        aQuery.id(R.id.sp_models_shop_details_order).adapter(brandsAdapter);

    }

    public void APiGetModelsYears() {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiShopsDetailsOrderModel, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            List models = new ArrayList();

                            JSONArray brandsData = response.getJSONArray("modelData");
                            for (int i = 0; i < brandsData.length(); i++) {
                                JSONObject jsonObject = brandsData.getJSONObject(i);
                                models.add(jsonObject.getString("modelName"));
                            }
                            ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, models);
                            aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                            loading.close();
                        } catch (JSONException e) {
                            loading.close();
                            showToast("Something Went Wrong Parsing.");
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        showToast("Something Went Wrong...");
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void GotoOrderNow(View V) {
        Intent i = new Intent(this, OrderNowActivity.class);
        startActivity(i);
    }

    public void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void btn_drawyerMenuOpen(View v) {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_details) {
            Intent i = new Intent(this, MyDetailsActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_my_orders) {
            Intent i = new Intent(this, MyOrdersActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_fav_shops) {
            Intent i = new Intent(this, FavouriteShopsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this, ShareActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_about_us) {
            Intent i = new Intent(this, AboutUsActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
