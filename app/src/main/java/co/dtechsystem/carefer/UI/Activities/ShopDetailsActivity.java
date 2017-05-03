package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Adapters.RecyclerImagesItemClickListener;
import co.dtechsystem.carefer.Adapters.ShopsImagesPagerAdapter;
import co.dtechsystem.carefer.Adapters.ShopsImagesRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import me.relex.circleindicator.CircleIndicator;

public class ShopDetailsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    LinearLayoutManager mgridLayoutManager;
    ShopsImagesRecycleViewAdapter mShopsImagesRecycleViewAdapter;
    ShopsDetailsModel mShopsDetailsModel;
    String mShopID;
    Intent mIntent;
    int mStatus = 0;
    LinearLayout lay_full_image, lay_shop_details;
    RecyclerView rv_images_shop_details;

    ShopsImagesPagerAdapter mShopsImagesPagerAdapter;
    ViewPager mViewPager;
    String responsePublic;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        lay_full_image = (LinearLayout) findViewById(R.id.lay_full_image);
        lay_shop_details = (LinearLayout) findViewById(R.id.lay_shop_details);
        rv_images_shop_details = (RecyclerView) findViewById(R.id.rv_images_shop_details);

        SetUpLeftbar();
        mIntent = getIntent();
        if (mIntent != null) {
            mShopID = mIntent.getStringExtra("ShopID");
            loading.show();
            APiGetShopsDetailsData(mShopID);
        }
        favouriteClicks();

    }

//    public static void getRecylerPosition(int position) {
//        ShopDetailsActivity shopDetailsActivit = new ShopDetailsActivity();
//        shopDetailsActivit.initPagerImages(position);
//    }

    public void initPagerImages() {
        rv_images_shop_details.addOnItemTouchListener(
                new RecyclerImagesItemClickListener(activity, new RecyclerImagesItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
                            lay_full_image.setVisibility(View.VISIBLE);
                            lay_shop_details.setVisibility(View.GONE);
                            mShopsImagesPagerAdapter = new ShopsImagesPagerAdapter(activity, mShopsDetailsModel.getShopImages(), mShopID, position);
                            mViewPager = (ViewPager) findViewById(R.id.pager);
                            mViewPager.setAdapter(mShopsImagesPagerAdapter);
//                            mShopsImagesPagerAdapter.notifyDataSetChanged();
                            final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                            indicator.setViewPager(mViewPager);
//                            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                                @Override
//                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                                    mShopsImagesPagerAdapter = new ShopsImagesPagerAdapter(activity, mShopsDetailsModel.getShopImages(), mShopID, 121);
//                                    mViewPager = (ViewPager) findViewById(R.id.pager);
//                                    mViewPager.setAdapter(mShopsImagesPagerAdapter);
////                            mShopsImagesPagerAdapter.notifyDataSetChanged();
//                                    final CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
//                                    indicator.setViewPager(mViewPager);
//                                }
//
//                                @Override
//                                public void onPageSelected(int position) {
//
//                                }
//
//                                @Override
//                                public void onPageScrollStateChanged(int state) {
//
//                                }
//                            });
                        } else {
                            lay_full_image.setVisibility(View.GONE);
                            lay_shop_details.setVisibility(View.VISIBLE);
                        }

                    }
                })
        );

    }


    public void favouriteClicks() {
        aQuery.find(R.id.iv_fav_shop_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mStatus) {
                    case 0:
                        loading.show();
                        APiShopFavourite(sUser_ID, mShopID, "add");
                        mStatus = 1;
                        break;
                    case 1:
                        loading.show();
                        APiShopFavourite(sUser_ID, mShopID, "del");
                        mStatus = 0;
                        break;
                }


            }

        });

    }

    public void GotoShopDetailsOrder(View V) {
        Intent i = new Intent(this, ShopDetailsOrderActivity.class);
        i.putExtra("shopID", mShopID);
        i.putExtra("shopName", mShopsDetailsModel.getShopsDetail().get(0).getShopName());
        i.putExtra("shopType", mShopsDetailsModel.getShopsDetail().get(0).getShopType());
        i.putExtra("shopRating", mShopsDetailsModel.getShopsDetail().get(0).getShopRating());
        i.putExtra("latitude", mShopsDetailsModel.getShopsDetail().get(0).getLatitude());
        i.putExtra("longitude", mShopsDetailsModel.getShopsDetail().get(0).getLatitude());
        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
            final String Url = AppConfig.BaseUrlImages + "shop-" + mShopID + "/";
            i.putExtra("shopImage", Url + mShopsDetailsModel.getShopImages().get(0).getImageName());
        } else {
            i.putExtra("shopImage", "");
        }

        startActivity(i);
    }

    public void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void APiGetShopsDetailsData(final String ShopID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiShopsDetailsData + ShopID + "/cusid/" + sUser_ID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        responsePublic = response.toString();
                        mShopsDetailsModel = gson.fromJson(response.toString(), ShopsDetailsModel.class);
                        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
                            mShopsImagesRecycleViewAdapter = new ShopsImagesRecycleViewAdapter(activity,
                                    mShopsDetailsModel.getShopImages(), ShopID);
                            SetImagesListData();
                        }
//                        else {
//                            loading.close();
//                            showToast("No Images Record found yet!");
//                        }
                        if (mShopsDetailsModel.getShopsDetail() != null && mShopsDetailsModel.getShopsDetail().size() > 0) {
                            SetShopsDetailsData();
                            loading.close();
                        }
                        loading.close();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void APiShopFavourite(final String UserId, final String shopID, final String action) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiShopFavourite,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (action.equals("del")) {
                                String success = jsonObject.getString("success");
                                if (success != null && success.equals("true")) {
                                    aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_empty);
                                    showToast("Shop deleted from favourite...");
                                }
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("userFavouriteShop");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String ID = jsonObject1.getString("ID");
                                if (ID != null && !ID.equals("")) {
                                    aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_fill);
                                    showToast("Shop Added in your favourite list...");
                                }
                            }
                            loading.close();
                        } catch (JSONException e) {
                            loading.close();
                            e.printStackTrace();
                        }
                        loading.close();
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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", UserId);
                params.put("shopID", shopID);
                params.put("action", action);


                return params;
            }
        };
// add it to the RequestQueue
        queue.add(postRequest);
    }

    public void SetImagesListData() {
        rv_images_shop_details.getItemAnimator().setChangeDuration(700);
        rv_images_shop_details.setAdapter(mShopsImagesRecycleViewAdapter);
        mgridLayoutManager = new LinearLayoutManager(this);
        mgridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_images_shop_details.setLayoutManager(mgridLayoutManager);
        aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_fill);
        initPagerImages();
    }

    public void SetShopsDetailsData() {
        aQuery.id(R.id.tv_shop_name_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopName());
        aQuery.id(R.id.tv_shop_service_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopType());
        aQuery.id(R.id.rb_shop_rating_shop_details).rating(Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating()));
        aQuery.id(R.id.tv_shop_des_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopDescription());
        if (mShopsDetailsModel.getShopsDetail().get(0).getFavourite() != null &&
                mShopsDetailsModel.getShopsDetail().get(0).getFavourite().equals("true")) {
            mStatus = 1;
            aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_fill);
        } else {
            mStatus = 0;
            aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_empty);
        }
    }

//    public void loadImagesSliderbulits() {
//        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
//            for (int i = 0; i < mShopsDetailsModel.getShopImages().size(); i++) {
//
//                ImageView myButton = new ImageView(this);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
//                params.setMargins(10, 10, 10, 10);
//                myButton.setLayoutParams(params);
//                myButton.setBackgroundResource(R.drawable.dr_round_icon);
//                lay_builts_images.addView(myButton);
//            }
//        }
//
//
//    }

    public void btn_drawyerMenuOpen(View v) {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (lay_full_image.getVisibility() == View.VISIBLE) {
                lay_full_image.setVisibility(View.GONE);
                lay_shop_details.setVisibility(View.VISIBLE);
            } else {
                super.onBackPressed();
            }
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
