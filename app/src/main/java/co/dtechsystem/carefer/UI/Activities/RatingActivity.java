package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class RatingActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String mshopID;
    private String morderID;
    private String mShopName;
    private TextView tv_title_rating;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        tv_title_rating = (TextView) findViewById(R.id.tv_title_rating);

        SetUpLeftbar();
        GetDataForViews();
        SetShaderToViews();
    }

    public void GotoOrderNow(@SuppressWarnings("UnusedParameters") View V) {
        if (Validations.isInternetAvailable(activity, true)) {
            Intent i = new Intent(this, OrderNowActivity.class);
            startActivity(i);
        }
    }

    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_rating, activity);
    }

    // Get Views Data
    private void GetDataForViews() {
        if (intent != null) {
            mshopID = intent.getStringExtra("shopID");
            morderID = intent.getStringExtra("orderID");
            mShopName = intent.getStringExtra("ShopName");
            aQuery.find(R.id.tv_shop_name_rating).text(mShopName);

        }
    }

    public void RatingbtnClick(@SuppressWarnings("UnusedParameters") View v) {
        float price_rate = aQuery.find(R.id.rb_price_rate).getRatingBar().getRating();
        float quality_rate = aQuery.find(R.id.rb_quality_rate).getRatingBar().getRating();
        float time_rate = aQuery.find(R.id.rb_time_rate).getRatingBar().getRating();
        String et_coments_rate = aQuery.find(R.id.et_coments_rate).getText().toString();
        if (et_coments_rate != null && !et_coments_rate.equals("")) {

            if (price_rate != 0.0 || quality_rate != 0.0 || time_rate != 0.0) {
                loading.show();
                APisendRating(sUser_ID, mshopID, morderID, String.valueOf(price_rate), String.valueOf(quality_rate),
                        String.valueOf(time_rate), et_coments_rate);
            } else {
                showToast(getResources().getString(R.string.toast_please_give_rating));

            }
        } else {
            showToast(getResources().getString(R.string.toast_please_add_comments));
        }
    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void APisendRating(final String UserId, final String shopID, final String orderID, final String priceRating,
                               final String qualityRating, final String timeRating, final String comments) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiRatingShop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
//                            //noinspection UnusedAssignment
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            if (result.equals("1")) {
                                showToast(getResources().getString(R.string.toast_review_added));
                                finish();
                                loading.close();
                            }
                            loading.close();
                        } catch (JSONException e) {
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            loading.close();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", UserId);
                params.put("shopID", shopID);
                params.put("orderID", orderID);
                params.put("comments", comments);
                params.put("priceRating", priceRating);
                params.put("qualityRating", qualityRating);
                params.put("timeRating", timeRating);


                return params;
            }
        };
// add it to the RequestQueue
        queue.add(postRequest);
    }

    @SuppressLint("RtlHardcoded")
    public void btn_drawyerMenuOpen(View v) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
