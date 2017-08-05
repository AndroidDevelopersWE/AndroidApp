package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;

public class ShareActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private TextView tv_title_share_app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        tv_title_share_app = (TextView) findViewById(R.id.tv_title_share_app);
        SetShaderToViews();
        SetUpLeftbar();
        setlistenrstosharebtns();
    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_share_app, activity);
    }

    private void setlistenrstosharebtns() {
        aQuery.find(R.id.btn_share_fb).clicked(this);
        aQuery.find(R.id.btn_share_twitter).clicked(this);
        aQuery.find(R.id.btn_share_gtalk).clicked(this);
        aQuery.find(R.id.btn_share_insta).clicked(this);
        aQuery.find(R.id.btn_share_pintrest).clicked(this);
        aQuery.find(R.id.btn_share_whatsap).clicked(this);
        aQuery.find(R.id.btn_share_telegram).clicked(this);
        aQuery.find(R.id.btn_share_snapchat).clicked(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_share_fb:
                Utils.SharePublic(activity, "https://m.facebook.com/sharer.php", "com.facebook.katana");
                break;
            case R.id.btn_share_twitter:
                Utils.SharePublic(activity, "https://mobile.twitter.com/compose/tweet", "com.twitter.android");
                break;
            case R.id.btn_share_gtalk:
                Utils.SharePublic(activity, "https://plus.google.com/share?url=https://play.google.com/store/apps/details?id=" + getPackageName(), "com.google.android.apps.plus");
                break;

            case R.id.btn_share_insta:
                Utils.SharePublic(activity, "https://www.instagram.com/", "com.instagram.android");

                break;
            case R.id.btn_share_pintrest:
                Utils.SharePublic(activity, "http://pinterest.com/pin/create", "com.pinterest");
                break;

            case R.id.btn_share_whatsap:
                Utils.SharePublic(activity, "https://web.whatsapp.com/", "com.whatsapp");

                break;
            case R.id.btn_share_telegram:
                Utils.SharePublic(activity, "https://web.telegram.org", "org.telegram.messenger");
                break;

            case R.id.btn_share_snapchat:
                Utils.SharePublic(activity, "https://www.snap.com", "com.snapchat.android");
                break;

        }

    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("UnusedParameters")
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
//            Intent i = new Intent(this, ShareActivity.class);
//            startActivity(i);

        } else if (id == R.id.nav_about_us) {
            Intent i = new Intent(this, AboutUsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
