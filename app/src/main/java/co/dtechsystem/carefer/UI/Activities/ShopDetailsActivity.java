package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

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
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;
import me.relex.circleindicator.CircleIndicator;

public class ShopDetailsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ShopsImagesRecycleViewAdapter mShopsImagesRecycleViewAdapter;
    private ShopsDetailsModel mShopsDetailsModel;
    private String mShopID, CityId, mplaceName;
    private int mStatus = 0;
    private LinearLayout lay_full_image;
    private LinearLayout lay_shop_details, lay_specialised_Brand_shop;
    private RecyclerView rv_images_shop_details;

    private ShopsImagesPagerAdapter mShopsImagesPagerAdapter;
    private ViewPager mViewPager;
    @SuppressWarnings("unused")
    private String responsePublic, ShopsListDataResponse, citiesNamesIDsResponse, isLocationAvail;
    private TextView tv_title_shop_details;
    private LatLng mLatlngCurrent;


    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);
        lay_full_image = (LinearLayout) findViewById(R.id.lay_full_image);
        lay_shop_details = (LinearLayout) findViewById(R.id.lay_shop_details);
        lay_specialised_Brand_shop = (LinearLayout) findViewById(R.id.lay_specialised_Brand_shop);
        rv_images_shop_details = (RecyclerView) findViewById(R.id.rv_images_shop_details);
        tv_title_shop_details = (TextView) findViewById(R.id.tv_title_shop_details);

        SetUpLeftbar();
        SetShaderToViews();
        getIntents();
        favouriteClicks();

    }

    public void getIntents() {
        Intent mIntent = getIntent();
        if (mIntent != null) {
            mShopID = mIntent.getStringExtra("ShopID");
            CityId = mIntent.getStringExtra("CityId");
            ShopsListDataResponse = intent.getStringExtra("ShopsListDataResponse");
            citiesNamesIDsResponse = intent.getStringExtra("citiesNamesIDsResponse");
            isLocationAvail = intent.getStringExtra("isLocationAvail");
            Bundle bundle = intent.getParcelableExtra("bundle");
            if (bundle != null) {
                mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
            }
            mplaceName = intent.getStringExtra("placeName");
            if (Validations.isInternetAvailable(activity, true)) {
                loading.show();
                if (mShopID != null && !mShopID.equals("")) {
                    APiGetShopsDetailsData(mShopID);
                }
            }
        }
    }

    public void btn_reviews_click(View v) {
        Intent i = new Intent(activity, ReviewActivity.class);
        i.putExtra("ShopID", mShopID);
        startActivity(i);
    }

    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_shop_details, activity);
    }
//    public static void getRecylerPosition(int position) {
//        ShopDetailsActivity shopDetailsActivit = new ShopDetailsActivity();
//        shopDetailsActivit.initPagerImages(position);
//    }

    private void initPagerImages() {
        rv_images_shop_details.addOnItemTouchListener(
                new RecyclerImagesItemClickListener(activity, new RecyclerImagesItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (mShopsDetailsModel.getShopImages() != null && mShopsDetailsModel.getShopImages().size() > 0) {
                            lay_full_image.setVisibility(View.VISIBLE);
                            lay_shop_details.setVisibility(View.GONE);
                            mShopsImagesPagerAdapter = new ShopsImagesPagerAdapter(activity, mShopsDetailsModel.getShopImages(), mShopID);
                            mViewPager = (ViewPager) findViewById(R.id.pager);
                            mViewPager.setAdapter(mShopsImagesPagerAdapter);
                            mViewPager.setCurrentItem(position);
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


    private void favouriteClicks() {
        aQuery.find(R.id.iv_fav_shop_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mStatus) {
                    case 0:
                        if (Validations.isInternetAvailable(activity, true)) {
                            loading.show();
                            APiShopFavourite(sUser_ID, mShopID, "add");
                            mStatus = 1;
                        }
                        break;
                    case 1:
                        if (Validations.isInternetAvailable(activity, true)) {
                            loading.show();
                            APiShopFavourite(sUser_ID, mShopID, "del");
                            mStatus = 0;
                        }
                        break;
                }


            }

        });

    }

    public void GotoShopDetailsOrder(@SuppressWarnings("UnusedParameters") View V) {
        if (Validations.isInternetAvailable(activity, true)) {
            Intent i = new Intent(this, OrderNowActivity.class);
            if (mShopsDetailsModel.getShopsDetail() != null && mShopsDetailsModel.getShopsDetail().size() > 0) {
                i.putExtra("shopID", mShopID);
                i.putExtra("shopName", mShopsDetailsModel.getShopsDetail().get(0).getShopName());
                i.putExtra("shopType", mShopsDetailsModel.getShopsDetail().get(0).getShopType());
                i.putExtra("shopRating", mShopsDetailsModel.getShopsDetail().get(0).getShopRating());
                i.putExtra("latitude", mShopsDetailsModel.getShopsDetail().get(0).getLatitude());
                i.putExtra("longitude", mShopsDetailsModel.getShopsDetail().get(0).getLongitude());
                i.putExtra("contact", mShopsDetailsModel.getShopsDetail().get(0).getContactNumber());
                if (CityId != null && !CityId.equals("")) {
                    i.putExtra("CityId", CityId);
                    i.putExtra("ShopsListDataResponse", ShopsListDataResponse);
                    i.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                    i.putExtra("isLocationAvail", isLocationAvail);
                    Bundle args = new Bundle();
                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
                    i.putExtra("placeName", mplaceName);
                    i.putExtra("bundle", args);
                }
                if (mShopsDetailsModel.getShopsDetail().get(0).getShopImage() != null) {
                    final String Url = AppConfig.BaseUrlImages + "shop-" + mShopID + "/thumbnails/";
                    i.putExtra("shopImage", Url + mShopsDetailsModel.getShopsDetail().get(0).getShopImage());
                } else {
                    i.putExtra("shopImage", "");
                }

                startActivity(i);
            } else {
                showToast(getResources().getString(R.string.some_went_wrong));
            }
        }
    }

    @Override
    protected void onRestart() {
//        getIntents();
        super.onRestart();
    }


    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void APiGetShopsDetailsData(final String ShopID) {
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
                        mShopsDetailsModel = gson.fromJson("{shopsDetail: []}", ShopsDetailsModel.class);

                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void APiShopFavourite(final String UserId, final String shopID, final String action) {
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
                                    showToast(getResources().getString(R.string.toast_shop_deleted_fav));
                                }
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("userFavouriteShop");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String ID = jsonObject1.getString("ID");
                                if (ID != null && !ID.equals("")) {
                                    aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_fill);
                                    showToast(getResources().getString(R.string.toast_shop_added_fav));
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
            @SuppressWarnings("Convert2Diamond")
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

    private void SetImagesListData() {
        rv_images_shop_details.getItemAnimator().setChangeDuration(700);
        rv_images_shop_details.setAdapter(mShopsImagesRecycleViewAdapter);
        LinearLayoutManager mgridLayoutManager = new LinearLayoutManager(this);
        mgridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_images_shop_details.setLayoutManager(mgridLayoutManager);
        aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_fill);
        initPagerImages();
    }

    private void SetShopsDetailsData() {
        aQuery.id(R.id.tv_shop_name_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopName());
        aQuery.id(R.id.tv_shop_service_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopType());
        aQuery.id(R.id.rb_shop_rating_shop_details).rating(Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating()));
        aQuery.id(R.id.tv_shop_des_shop_details).text(mShopsDetailsModel.getShopsDetail().get(0).getShopDescription());
        TextView tv_shop_des_shop_details = (TextView) findViewById(R.id.tv_shop_des_shop_details);
        if (tv_shop_des_shop_details.getLayout() != null && tv_shop_des_shop_details.getLayout().getLineCount() > 4) {
//            makeTextViewResizable(tv_shop_des_shop_details, 4, "View More", true);
            aQuery.id(R.id.tv_shop_des_view_more_shop_details).getTextView().setVisibility(View.VISIBLE);

        } else {
            aQuery.id(R.id.tv_shop_des_view_more_shop_details).getTextView().setVisibility(View.GONE);

        }
        aQuery.id(R.id.tv_shop_des_shop_details).getTextView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                aQuery.id(R.id.tv_shop_des_shop_details).getTextView().getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        if (mShopsDetailsModel.getShopsDetail().get(0).getSpecialisedBrand() != null && !mShopsDetailsModel.getShopsDetail().get(0).getSpecialisedBrand().equals("")) {
            aQuery.id(R.id.tv_specialised_Brand_shop).text(mShopsDetailsModel.getShopsDetail().get(0).getSpecialisedBrand());
            lay_specialised_Brand_shop.setVisibility(View.VISIBLE);

        } else {
            lay_specialised_Brand_shop.setVisibility(View.GONE);
        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getServiceType() != null) {
//            String[] serviceArray = mShopsDetailsModel.getShopsDetail().get(0).getServiceType().split(",");
            aQuery.id(R.id.tv_services_shops).text(mShopsDetailsModel.getShopsDetail().get(0).getServiceType());

        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getNationality() != null) {
//            String[] nationalityArray = mShopsDetailsModel.getShopsDetail().get(0).getNationality().split(",");
            aQuery.id(R.id.tv_nationality_shop).text(mShopsDetailsModel.getShopsDetail().get(0).getNationality());

        }

        aQuery.id(R.id.tv_city_shop).text(mShopsDetailsModel.getShopsDetail().get(0).getCity());
        if (mShopsDetailsModel.getShopsDetail().get(0).getProvideWarranty().equals("1")) {
            aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_yes));

        } else {
            aQuery.id(R.id.iv_provide_warrnty_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_no));

        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getProvideReplaceParts().equals("1")) {
            aQuery.id(R.id.iv_replace_parts_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_yes));

        } else {
            aQuery.id(R.id.iv_replace_parts_shop).getImageView().setBackground(ContextCompat.getDrawable(activity, R.drawable.ic_no));

        }
        if (mShopsDetailsModel.getShopsDetail().get(0).getFavourite() != null &&
                mShopsDetailsModel.getShopsDetail().get(0).getFavourite().equals("true")) {
            mStatus = 1;
            aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_fill);
        } else {
            mStatus = 0;
            aQuery.find(R.id.iv_fav_shop_list).background(R.drawable.ic_fav_star_empty);
        }

        if (mShopsDetailsModel.getShopsDetail().get(0).getShopRating() != null && Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating()) > 0) {
            aQuery.id(R.id.tv_reviews_shop_details).text(getResources().getString(R.string.tv_reviews));
            aQuery.id(R.id.tv_avg_rating).getTextView().setVisibility(View.VISIBLE);
            aQuery.id(R.id.tv_total_rating).getTextView().setVisibility(View.VISIBLE);
            aQuery.id(R.id.tv_rating_type).getTextView().setVisibility(View.VISIBLE);
            float totalRatingShop = Float.parseFloat(mShopsDetailsModel.getShopsDetail().get(0).getShopRating());
            String avgRate = String.format("%.01f", totalRatingShop);
            aQuery.id(R.id.tv_avg_rating).text(avgRate);
            aQuery.id(R.id.tv_total_rating).text(getResources().getString(R.string.tv_see_all) + " " + mShopsDetailsModel.getShopsDetail().get(0).getReviewCount() + " " + getResources().getString(R.string.tv_reviews));
            if (totalRatingShop > 4.4) {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_excelent));

            } else if (totalRatingShop > 3.4) {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_good));


            } else if (totalRatingShop > 2.4) {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_average));

            } else {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_lower));

            }

        } else {
            aQuery.id(R.id.tv_avg_rating).getTextView().setVisibility(View.GONE);
            aQuery.id(R.id.tv_total_rating).getTextView().setVisibility(View.GONE);
            aQuery.id(R.id.tv_rating_type).getTextView().setVisibility(View.GONE);
            aQuery.id(R.id.tv_reviews_shop_details).text(getResources().getString(R.string.tv_not_enough_reviews));

        }

    }

    public void showDescriptionActivity(View c) {
        if (mShopsDetailsModel.getShopsDetail() != null && mShopsDetailsModel.getShopsDetail().size() > 0) {

            intent = new Intent(activity, ShopDescriptionActivity.class);
            intent.putExtra("shopName", mShopsDetailsModel.getShopsDetail().get(0).getShopName());
            intent.putExtra("shopDescription", mShopsDetailsModel.getShopsDetail().get(0).getShopDescription());
            startActivity(intent);
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
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @SuppressLint("RtlHardcoded")
    public void btn_drawyerMenuOpen(@SuppressWarnings("UnusedParameters") View v) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

}
