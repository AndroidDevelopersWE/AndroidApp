package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

public class OrderNowActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private String mlatitude;
    private String mlongitude;
    private String mshopID;
    private String mServicesId;
    private String mBrandsId;
    private String mModelsId;
    private String morderType;
    private String mshopImage;
    private String mContact;

    private ImageView iv_shop_image_blur;
    private CircleImageView iv_shop_profile;
    private TextView tv_title_order_now;
    private int morderID;
    private boolean mOrderPlaced;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);
        iv_shop_image_blur = (ImageView) findViewById(R.id.iv_shop_image_blur);
        iv_shop_profile = (CircleImageView) findViewById(R.id.iv_shop_profile);
        tv_title_order_now = (TextView) findViewById(R.id.tv_title_order_now);
        mOrderPlaced = false;
        SetShaderToViews();
        SetUpLeftbar();
        GetDataForViews();
        SetdataToViews();
    }

    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_order_now, activity);
    }

    // Get Views Data
    private void GetDataForViews() {
        if (intent != null) {
            mlatitude = intent.getStringExtra("latitude");
            mlongitude = intent.getStringExtra("longitude");
            mshopID = intent.getStringExtra("shopID");
            mServicesId = intent.getStringExtra("serviceID");
            mBrandsId = intent.getStringExtra("brandID");
            mModelsId = intent.getStringExtra("modelID");
            mshopImage = intent.getStringExtra("shopImage");
            mContact = intent.getStringExtra("contact");
        }
    }

    private void SetdataToViews() {
        if (mshopImage != null && !mModelsId.equals("")) {
            aQuery.find(R.id.pg_shop_image_blur).visibility(View.VISIBLE);
            Glide.with(activity).load(mshopImage)
                    .into(iv_shop_profile);
            Glide.with(activity)
                    .load(mshopImage)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            // Do something with bitmap here.
                            aQuery.find(R.id.pg_shop_image_blur).visibility(View.GONE);
                            Blurry.with(activity).from(bitmap).into(iv_shop_image_blur);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            aQuery.find(R.id.pg_shop_image_blur).visibility(View.GONE);
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    // Do network action in this function
//                    getBitmapFromURL(mshopImage);
//                }
//            }).start();


        }
    }

    private void APiPlaceOrder(final String UserId, final String shopID, final String serviceID, final String brandID,
                               final String modelID, final String orderType, final String customerMobileNo) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiSaveOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            morderID = jsonObject.getInt("orderID");
                            if (morderID != 0) {
                                showToast(getResources().getString(R.string.toast_order_placed));
                                mOrderPlaced = true;
                            }
                        } catch (JSONException e) {
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
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
                @SuppressWarnings("Convert2Diamond") Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", UserId);
                params.put("shopID", shopID);
                params.put("serviceTypeID", serviceID);
                params.put("brandID", brandID);
                params.put("modelID", modelID);
                params.put("orderType", orderType);
                params.put("customerMobileNo", customerMobileNo);


                return params;
            }
        };
// add it to the RequestQueue
        queue.add(postRequest);
    }

    @SuppressWarnings({"PointlessBooleanExpression", "UnusedParameters"})
    public void CAllToShop(View V) {
        if (Validations.isInternetAvailable(activity, true)) {
            try {

                if (mContact != null && !mContact.equals("")) {
                    if (mOrderPlaced == false) {
                        //noinspection StatementWithEmptyBody
                        if (morderType != null && morderType.equals("navigate")) {
                        } else {
                            morderType = "call";
                        }

                        loading.show();
                        APiPlaceOrder(sUser_ID, mshopID, mServicesId, mBrandsId, mModelsId, morderType, sUser_Mobile);

                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mContact));
                    startActivity(intent);
                } else {
                    showToast(getResources().getString(R.string.toast_no_shop_contact_found));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({"PointlessBooleanExpression", "UnusedParameters"})
    public void DirectionsToShop(View v) {
        if (mOrderPlaced == false) {
            //noinspection StatementWithEmptyBody
            if (morderType != null && !morderType.equals("navigate")) {
            } else {
                morderType = "navigate";
            }
            if (Validations.isInternetAvailable(activity, true)) {
                loading.show();
                APiPlaceOrder(sUser_ID, mshopID, mServicesId, mBrandsId, mModelsId, morderType, sUser_Mobile);
            }
        }
        Intent i = new Intent(this, NavigationsActivity.class);
        i.putExtra("latitude", mlatitude);
        i.putExtra("longitude", mlongitude);
        i.putExtra("shopID", mshopID);
        startActivity(i);
    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("UnusedParameters")
    @SuppressLint("RtlHardcoded")
    public void btn_drawyerMenuOpen(View v) {
        mDrawerLayout.openDrawer(Gravity.LEFT);
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
