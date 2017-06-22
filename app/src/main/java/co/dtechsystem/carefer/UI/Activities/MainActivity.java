package co.dtechsystem.carefer.UI.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<Integer, Bitmap> mImagesMaps = new HashMap<Integer, Bitmap>();
    private LatLng mLatLngCurrent;
    Location mNewLocation, mOldLocation;
    int idle;
    String ShopsListDataResponse = "", citiesNamesIDsResponse = "";
    String CityId = "";

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);
//            return;
            } else {
                mapFragment.getMapAsync(this);

            }
        }
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
            i.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
            i.putExtra("placeName", mPlaceName);
            i.putExtra("ShopsListDataResponse", ShopsListDataResponse);
            i.putExtra("CityId",CityId);
            i.putExtra("bundle", args);
            startActivity(i);

        }
    }

    @SuppressWarnings("UnusedParameters")
    public void btnSearchThisAreaClick(View v) {
        if (Validations.isInternetAvailable(activity, true)) {
//                                float dis=mOldLocation.distanceTo(mNewLocation);
//            if (mOldLocation.distanceTo(mNewLocation) > 10000) {
            mMap.clear();
            aQuery.find(R.id.btn_search_shops_here_main).getButton().setVisibility(View.GONE);
            if (mNewLocation != null) {
                mOldLocation = mNewLocation;
                aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.VISIBLE);
                if (CityId != null && !CityId.equals("")) {
                    APiGetAllCities(mNewLocation);
                } else {
                    APiGetAllCities(mNewLocation);

                }
            }

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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        GPSServiceRequest.displayLocationSettingsRequest(activity);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @SuppressWarnings("PointlessBooleanExpression")
            @Override
            public void onMyLocationChange(final Location location) {
                // TODO Auto-generated method stub
                if (mNewLocation == null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                    firstCAll = true;
                    mNewLocation = location;
                    mOldLocation = mNewLocation;
                    mLatLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
//                    loading.show();
                    if (Validations.isInternetAvailable(activity, true) && location != null) {
//                        APiGetCurrentAddress("Location", location);
                        APiGetAllCities(location);
                    }
                } else {
                    if (mPlaceName != null && !mPlaceName.equals("")) {
                    } else {
                        if (Validations.isInternetAvailable(activity, true) && location != null) {
//                            APiGetCurrentAddress("Address", location);
                            APiGetAllCities(location);
                        }
                    }

                    mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                        @Override
                        public void onCameraChange(CameraPosition cameraPosition) {
                            Location location = new Location("");
                            location.setLatitude(cameraPosition.target.latitude);
                            location.setLongitude(cameraPosition.target.longitude);
                            if (location != null) {
                                mNewLocation = location;
                            }

                        }

                    });


                }

            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (idle == 2) {
                    aQuery.find(R.id.btn_search_shops_here_main).getButton().setVisibility(View.VISIBLE);
                }
                if (idle < 2) {
                    idle++;
                }

            }
        });
    }

    private void APiGetAllCities(final Location location) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiGetCitiesList, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            citiesNamesIDsResponse = response.getJSONArray("citiesList").toString();
                            JSONArray jsonArray = response.getJSONArray("citiesList");
                            try {
                                Geocoder geocoder = new Geocoder(activity, locale);
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                mPlaceName = addresses.get(0).getLocality();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectCities = jsonArray.getJSONObject(i);
                                String cityName = jsonObjectCities.getString("name");

                                if (mPlaceName!=null&&mPlaceName.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
                                    CityId = jsonObjectCities.getString("ID");
                                    break;

                                }
                            }

                            if (CityId != null && !CityId.equals("")) {
                                APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, location, CityId);
                            } else {
                                APiGetShopslistData(AppConfig.APiShopsListData, location, CityId);

                            }
//                            for (int i = 0; i < results.length(); i++) {
//                                JSONObject jsonObject = results.getJSONObject(i);
//                                String ID = jsonObject.getString("ID");
//                                String name = jsonObject.getString("name");
//                                citiesNamesIDs.put(name, ID);
//                            }
//                            if (citiesNamesIDs != null) {
//                                for (int j = 0; j < citiesNamesIDs.keySet().toArray().length; j++) {
//                                    if (mPlaceName!=null&&mPlaceName.toLowerCase(locale).contains(citiesNamesIDs.keySet().toArray()[j].toString().toLowerCase(locale))) {
//                                        String id = citiesNamesIDs.get(j).toString();
//                                    }
//                                    else if (mPlaceName!=null&&mPlaceName.toLowerCase(localeEn).contains(citiesNamesIDs.keySet().toArray()[j].toString().toLowerCase(locale))) {
//                                        String id = citiesNamesIDs.get(j).toString();
//                                    }
//
//                                }
//                            }
                        } catch (JSONException e) {
                            aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);

                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void APiGetCurrentAddress(final String Type, final Location location) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET,
                "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + "," + location.getLongitude()
                        + "&sensor=true&language=ar", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
//                        try {
//                            totalDataofCurrentLatlngNames = response.getJSONArray("results");
////                            JSONObject jsonObject = results.getJSONObject(2);
////                            JSONArray address_components = jsonObject.getJSONArray("address_components");
////                            JSONObject jsonObject1 = address_components.getJSONObject(0);
////                            mPlaceName = jsonObject1.getString("short_name");
//
//                            if (Type.equals("Location")) {
//                                if (Validations.isInternetAvailable(activity, true)) {
//                                    aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.VISIBLE);
//
//                                    APiGetAllCities(location);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            loading.close();
//                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
//                            e.printStackTrace();
//                        }


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

    private void APiGetShopslistData2(String Url, final Location location, final String CityID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        ShopsListDataResponse = response.toString();
                        ShopsListModel mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
                        if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {

                            if (location != null) {

                                SetShopsPointMap(mShopsListModel.getShopsList(), location);
                            }
                        } else {
                            loading.close();
                            aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
                            showToast(activity.getResources().getString(R.string.no_record_found));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        getRequest.setRetryPolicy(policy);
// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void APiGetShopslistData(String Url, final Location location, final String CityID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {

                            // display response
                            ShopsListDataResponse = response;
                            ShopsListModel mShopsListModel = gson.fromJson(response, ShopsListModel.class);
                            if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {

                                if (location != null) {

                                    SetShopsPointMap(mShopsListModel.getShopsList(), location);
                                }
                            } else {
                                loading.close();
                                aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
                                showToast(activity.getResources().getString(R.string.no_record_found));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cityID", CityID);
                return params;
            }
        };
// add it to the RequestQueue
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    private void SetShopsPointMap(final List<ShopsListModel.ShopslistRecord> shopsList, Location location) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map);
        int count = 0;
        for (int i = 0; i < shopsList.size(); i++) {
            Location target = new Location("Target");
            target.setLatitude(Double.parseDouble(shopsList.get(i).getLatitude()));
            target.setLongitude(Double.parseDouble(shopsList.get(i).getLongitude()));
            float dis = location.distanceTo(target);

            if (location.distanceTo(target) < 10000) {

//                marker.setInfoWindowAnchor((float)x, (float)y);
                count++;
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(shopsList.get(i).getLatitude()),
                                Double.parseDouble(shopsList.get(i).getLongitude()))).icon(icon));
                final int finalI = i;
                try {

                    Glide.with(activity)
                            .load(AppConfig.BaseUrlImages + "shop-" + shopsList.get(i).getID() + "/" + shopsList.get(i).getShopImage())
                            .asBitmap()
                            .override((int) activity.getResources().getDimension(R.dimen._100sdp), (int) activity.getResources().getDimension(R.dimen._100sdp))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                    // Do something with bitmap here.
                                    mImagesMaps.put(Integer.parseInt(shopsList.get(finalI).getID()), bitmap);

                                }

                                @SuppressWarnings("deprecation")
                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                    Drawable myDrawable = getResources().getDrawable(R.drawable.ic_img_place_holder);
                                    Bitmap ic_img_place_holder = ((BitmapDrawable) myDrawable).getBitmap();
                                    mImagesMaps.put(Integer.parseInt(shopsList.get(finalI).getID()), ic_img_place_holder);
                                }
                            });
                } catch (Exception d) {
                    d.printStackTrace();
                }
            }
            aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
            loading.close();
        }
        if (count == 0) {
            showToast(getResources().getString(R.string.no_record_found));
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
                            iv_shop_map_item.setImageBitmap(mImagesMaps.get(Integer.parseInt(id)));
                        } catch (Exception e) {
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
        mDrawerLayout.openDrawer(Gravity.END);
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
        drawer.closeDrawer(GravityCompat.END);
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
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);

                } else {
                    // User refused to grant permission. You can add AlertDialog here
                    mLatLngCurrent = new LatLng(24.7255553, 46.5423347);
                    mNewLocation = new Location("");
                    mOldLocation = new Location("");
                    mOldLocation.setLatitude(mLatLngCurrent.latitude);
                    mOldLocation.setLongitude(mLatLngCurrent.longitude);
                    mNewLocation.setLatitude(mLatLngCurrent.latitude);
                    mNewLocation.setLongitude(mLatLngCurrent.longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngCurrent, 13));
                    if (Validations.isInternetAvailable(activity, true) && mOldLocation != null) {
                        APiGetAllCities(mOldLocation);
//                        APiGetCurrentAddress("Location", mOldLocation);
                    }
                    showToast(getResources().getString(R.string.toast_permission));
                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }
}
