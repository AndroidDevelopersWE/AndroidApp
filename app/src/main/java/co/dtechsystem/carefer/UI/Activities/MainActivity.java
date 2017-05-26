package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.Google.GPSServiceRequest;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private DrawerLayout mDrawerLayout;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private boolean firstCAll = false;
    private String mPlaceName = "";
    private TextView tv_title_main;
    private final ArrayList<Bitmap> mImagesMaps = new ArrayList<>();
    private LatLng mLatLngCurrent;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SetUpLeftbar();
        tv_title_main = (TextView) findViewById(R.id.tv_title_main);
        SetShaderToViews();
    }

    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_main, activity);
    }

    @SuppressWarnings("UnusedParameters")
    public void btnExploereClick(View v) {
        if (Validations.isInternetAvailable(activity, true)) {
            Bundle args = new Bundle();
            args.putParcelable("LatLngCurrent", mLatLngCurrent);
            Intent i = new Intent(this, ShopsListActivity.class);
            i.putExtra("placeName", mPlaceName);
            i.putExtra("bundle", args);
            startActivity(i);
        }
    }

    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        GPSServiceRequest.displayLocationSettingsRequest(activity);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @SuppressWarnings("PointlessBooleanExpression")
            @Override
            public void onMyLocationChange(Location location) {
                // TODO Auto-generated method stub
                if (firstCAll != true) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                    firstCAll = true;
                    APiGetCurrentAddress(location);
                    mLatLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                }
//                Locale locale = new Locale("ar");
//                Geocoder gcd = new Geocoder(getBaseContext(), locale);
//                try {
//                    List<Address> addresses;
//                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                    if (addresses.size() > 0) {
//                        String city = addresses.get(0).getLocality().toString();
//                        String Country = addresses.get(0).getCountryName().toString();
////                        String locationname=addresses.get(0).getSubLocality().toString();
//                        mplaceName = city + ", " + Country;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


            }
        });
    }

    private void APiGetCurrentAddress(final Location location) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + "," + location.getLongitude()
                        + "&sensor=true&language=ar", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            JSONArray results = response.getJSONArray("results");
                            JSONObject jsonObject = results.getJSONObject(0);
                            mPlaceName = jsonObject.getString("formatted_address");
                            if (Validations.isInternetAvailable(activity, true)) {
                                APiGetShopslistData(location);
                            }

                        } catch (JSONException e) {
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }


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

    private void APiGetShopslistData(final Location location) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiShopsListData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        ShopsListModel mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
                        if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
                            SetShopsPointMap(mShopsListModel.getShopsList(), location);
                            loading.close();
                        } else {
                            loading.close();
                            showToast(activity.getResources().getString(R.string.no_record_found));
                        }

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

    private void SetShopsPointMap(final List<ShopsListModel.ShopslistRecord> shopsList, Location location) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map);

        for (int i = 0; i < shopsList.size(); i++) {


            // adding a marker on map with image from  drawable
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(Double.parseDouble(shopsList.get(i).getLatitude()),
//                            Double.parseDouble(shopsList.get(i).getLongitude())))
//                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
            Location target = new Location("Target");
            target.setLatitude(Double.parseDouble(shopsList.get(i).getLatitude()));
            target.setLongitude(Double.parseDouble(shopsList.get(i).getLongitude()));
            if (location.distanceTo(target) < 10000) {

//                marker.setInfoWindowAnchor((float)x, (float)y);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(shopsList.get(i).getLatitude()),
                                Double.parseDouble(shopsList.get(i).getLongitude()))).icon(icon));
                Glide.with(activity)
                        .load(AppConfig.BaseUrlImages + "shop-" + shopsList.get(i).getID() + "/" + shopsList.get(i).getShopImage())
                        .asBitmap()
                        .override((int) activity.getResources().getDimension(R.dimen._100sdp), (int) activity.getResources().getDimension(R.dimen._100sdp))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                // Do something with bitmap here.
                                mImagesMaps.add(bitmap);
                            }

                            @SuppressWarnings("deprecation")
                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_img_place_holder);
                                Bitmap ic_img_place_holder = ((BitmapDrawable) myDrawable).getBitmap();
                                mImagesMaps.add(ic_img_place_holder);
                            }
                        });
            }
        }
        //get the map container height

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()

        {

            // Use default InfoWindow frame
            @SuppressWarnings("deprecation")
            @Override
            public View getInfoWindow(Marker arg0) {

                @SuppressLint("InflateParams") View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_google_map_shop, null);
                String id = "";
                for (int i = 0; i < shopsList.size(); i++) {
                    double latmap = arg0.getPosition().latitude;
                    double latmy = Double.parseDouble(shopsList.get(i).getLatitude());
                    if (latmap == latmy) {
                        TextView tv_shop_name_shop_list = (TextView) customMarkerView.findViewById(R.id.tv_shop_name_shop_list);
                        TextView tv_service_type_shop_list = (TextView) customMarkerView.findViewById(R.id.tv_service_type_shop_list);
                        TextView tv_desc_shop_list = (TextView) customMarkerView.findViewById(R.id.tv_desc_shop_list);
                        RatingBar rb_shop_shop_list = (RatingBar) customMarkerView.findViewById(R.id.rb_shop_shop_list);
                        SelectableRoundedImageView iv_shop_map_item = (SelectableRoundedImageView) customMarkerView.findViewById(R.id.iv_shop_map_item);
                        tv_shop_name_shop_list.setText(shopsList.get(i).getShopName());

                        tv_service_type_shop_list.setText(shopsList.get(i).getServiceType());
                        tv_desc_shop_list.setText(shopsList.get(i).getShopDescription());
                        rb_shop_shop_list.setRating(Float.parseFloat(shopsList.get(i).getShopRating()));
                        try {
                            id = shopsList.get(i).getID();
                            iv_shop_map_item.setImageBitmap(mImagesMaps.get(i));
                        } catch (Exception e) {
//                            Bitmap bmp = Bitmap.createBitmap((int) activity.getResources().getDimension(R.dimen._100sdp), (int) activity.getResources().getDimension(R.dimen._100sdp), Bitmap.Config.ARGB_8888);
//                            Canvas canvas = new Canvas(bmp);
//                            canvas.drawColor(getResources().getColor(R.color.colorOrange));
//                            mImagesMaps.add(i, bmp);
                            e.printStackTrace();
                        }
//                        }

                        break;
                    }
                }
                final String finalId = id;
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker arg0) {
                        if (Validations.isInternetAvailable(activity, true)) {
                            Intent mIntent = new Intent(activity, ShopDetailsActivity.class);
                            mIntent.putExtra("ShopID", finalId);
                            activity.startActivity(mIntent);
                        }
                    }
                });
                arg0.setInfoWindowAnchor((float) -5.2, (float) 3.2);

                return customMarkerView;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {


                return null;

            }
        });

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

    @SuppressWarnings({"StatementWithEmptyBody", "NullableProblems"})
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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

    @SuppressWarnings({"UnnecessaryReturnStatement", "NullableProblems"})
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);

                } else {
                    // User refused to grant permission. You can add AlertDialog here
                    showToast(getResources().getString(R.string.toast_permission));
                }
                return;
            }

        }
    }
}
