package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import co.dtechsystem.carefer.Adapters.SimpleShopsDetailsAdapter;
import co.dtechsystem.carefer.Adapters.SimpleShopsListAdapter;
import co.dtechsystem.carefer.R;

public class OrderNowActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_now);
        SetUpLeftbar();
    }

    public void CAllToShop(View V) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DirectionsToShop(View v) {
        Intent i = new Intent(this, DirectionsActivity.class);
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
