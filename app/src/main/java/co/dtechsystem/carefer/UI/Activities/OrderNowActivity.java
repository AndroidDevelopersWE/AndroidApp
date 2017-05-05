package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.CircleTransform;
import jp.wasabeef.blurry.Blurry;

public class OrderNowActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    String mlatitude, mlongitude, mshopID, mServicesId, mBrandsId, mModelsId, morderType, mshopImage;
    ImageView iv_shop_image_blur;
    ImageView iv_shop_profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);
        iv_shop_image_blur = (ImageView) findViewById(R.id.iv_shop_image_blur);
        iv_shop_profile = (ImageView) findViewById(R.id.iv_shop_profile);

        SetUpLeftbar();
        GetDataForViews();
        SetdataToViews();
    }

    // Get Views Data
    public void GetDataForViews() {
        if (intent != null) {
            mlatitude = intent.getStringExtra("latitude");
            mlongitude = intent.getStringExtra("longitude");
            mshopID = intent.getStringExtra("shopID");
            mServicesId = intent.getStringExtra("serviceID");
            mBrandsId = intent.getStringExtra("brandID");
            mModelsId = intent.getStringExtra("modelID");
            mshopImage = intent.getStringExtra("shopImage");
        }
    }

    public void SetdataToViews() {
        if (mshopImage != null && !mModelsId.equals("")) {
//            Glide.with(this).load(mshopImage)
//                    .into(iv_shop_image_blur);
            aQuery.find(R.id.pg_shop_image_blur).visibility(View.VISIBLE);
            Glide.with(activity).load(mshopImage)
                    .transform(new CircleTransform(activity))
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

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            final Bitmap myBitmap = BitmapFactory.decodeStream(input);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    aQuery.find(R.id.pg_shop_image_blur).visibility(View.GONE);
                    Blurry.with(activity).from(myBitmap).into(iv_shop_image_blur);
                }
            });

            return myBitmap;
        } catch (IOException e) {
            aQuery.find(R.id.pg_shop_image_blur).visibility(View.GONE);
            e.printStackTrace();
            return null;
        }
    }

    public void CAllToShop(View V) {
        try {
            morderType = "call";
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DirectionsToShop(View v) {
        if (morderType != null && !morderType.equals("")) {
        } else {
            morderType = "navigate";
        }

        Intent i = new Intent(this, NavigationsActivity.class);
        i.putExtra("latitude", mlatitude);
        i.putExtra("longitude", mlongitude);
        i.putExtra("shopID", mshopID);
        i.putExtra("serviceID", mServicesId);
        i.putExtra("brandID", mBrandsId);
        i.putExtra("modelID", mModelsId);
        i.putExtra("orderType", morderType);
        startActivity(i);
    }

    public void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

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
