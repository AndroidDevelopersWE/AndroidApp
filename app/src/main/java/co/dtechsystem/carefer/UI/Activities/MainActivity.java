package co.dtechsystem.carefer.UI.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.SplashActivity;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MainActivity extends BaseActivity
        implements  OnMapReadyCallback {
    private DrawerLayout mDrawerLayout;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private boolean firstCAll = false;
    private String mPlaceName = "";
    private TextView tv_title_main;
    private Map<Integer, Bitmap> mImagesMaps = new HashMap<>();
    private LatLng mLatLngCurrent;
    private Location mNewLocation;
    private Location mOldLocation;
    private int idle;
    private String ShopsListDataResponse = "";
    private String citiesNamesIDsResponse = "";
    private String isLocationAvail = "";
    private String CityId = "";
    private JSONArray totalDataofCurrentLatlngNames;
    private boolean SearchingCityfinished = false;
    String fromListLocation = "";
    private boolean locationSettings = false;

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
                LocationManager lm = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                if(gps_enabled)
                mapFragment.getMapAsync(this);
                else{
                    showLocationDialog();
                    //request to enable gps location



                }
            }
        }
        SetUpLeftbar();
        tv_title_main = (TextView) findViewById(R.id.tv_title_main);
        SetShaderToViews();



    }

    /**
     * Handle all type of intents like receiving strings and bundles from previous activity
     */
    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_main, activity);
    }

    /**
     * Handle button click explorer to explore shops list against current city
     *
     * @param v
     */
    @SuppressWarnings("UnusedParameters")
    public void btnExploereClick(View v) {
        if (ShopsListDataResponse != null && !ShopsListDataResponse.equals("")) {
            if (Validations.isInternetAvailable(activity, true)) {
                Bundle args = new Bundle();
                args.putParcelable("LatLngCurrent", mLatLngCurrent);
                Intent i = new Intent(this, ShopsListActivity.class);
                i.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                i.putExtra("placeName", mPlaceName);
                ShopsListActivity.ShopsListDataResponse = ShopsListDataResponse;
//                i.putExtra("ShopsListDataResponse",ShopsListActivity.ShopsListDataResponse);
                i.putExtra("CityId", CityId);
                i.putExtra("isLocationAvail", isLocationAvail);

                i.putExtra("bundle", args);
                startActivity(i);
            }

        } else {
            showToast(getResources().getString(R.string.toast_please_wait));
        }
    }

    /**
     * click listner for moved shop button
     * @param v
     */
    public void btnMovedShopClick(View v){
        Intent i = new Intent(this,MovedShopActivity.class);
        startActivity(i);
    }

    /**
     * click listner for receive car button
     * @param v
     */
    public void btnReceiveCarClick(View v){
        Intent i = new Intent(this,ReceiveCarActivity.class);
        startActivity(i);
    }

    /**
     * Handle click funtion when user click on Search this Area search in the 10km area for shops
     *
     * @param v
     */
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
                    SearchingCityfinished = false;
                    APiGetAllCities(mNewLocation);
                } else {
                    SearchingCityfinished = false;
                    APiGetAllCities(mNewLocation);

                }
            }

        }

    }

    /**
     * Handle left bar menu
     */
    private void SetUpLeftbar() {
      //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setAnimation(null);


    }

    /**
     * Handle Map to load
     *
     * @param googleMap
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
//        GPSServiceRequest.displayLocationSettingsRequest(activity);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    isLocationAvail = "Yes";
                    Utils.savePreferences(activity, "isLocationAvail", "Yes");
                    mLatLngCurrent = new LatLng(location.getLatitude(), location.getLongitude());
                    Utils.savePreferences(activity, "mLatLngCurrent", String.valueOf(mLatLngCurrent.latitude + "," + mLatLngCurrent.longitude));
//                    loading.show();
                    if (Validations.isInternetAvailable(activity, true) && location != null) {
//                        APiGetCurrentAddress("Location", location);
                        SearchingCityfinished = false;
                        APiGetAllCities(location);
                    }
                } else {
                    if (mPlaceName != null && !mPlaceName.equals("")) {
                    } else {
                        if (Validations.isInternetAvailable(activity, true) && location != null) {
//                            APiGetCurrentAddress("Address", location);
                            SearchingCityfinished = false;
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
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()

        {
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
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mMap.setMyLocationEnabled(true);
        }
        boolean isLocationServiceEnabled=Utils.isLocationServiceEnabled(activity);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (mLatLngCurrent == null || !isLocationServiceEnabled) {
                mMap.setMyLocationEnabled(true);
                // User refused to grant permission. You can add AlertDialog here
                isLocationAvail = "No";
                Utils.savePreferences(activity, "isLocationAvail", "No");
                mLatLngCurrent = new LatLng(24.586867, 46.741052);
                Utils.savePreferences(activity, "mLatLngCurrent", String.valueOf(mLatLngCurrent.latitude + "," + mLatLngCurrent.longitude));
                mNewLocation = new Location("");
                mOldLocation = new Location("");
                mOldLocation.setLatitude(mLatLngCurrent.latitude);
                mOldLocation.setLongitude(mLatLngCurrent.longitude);
                mNewLocation.setLatitude(mLatLngCurrent.latitude);
                mNewLocation.setLongitude(mLatLngCurrent.longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngCurrent, 13));
                if (Validations.isInternetAvailable(activity, true) && mOldLocation != null) {
                    SearchingCityfinished = false;
                    APiGetAllCities(mOldLocation);
//                        APiGetCurrentAddress("Location", mOldLocation);
                }
                showToast(getResources().getString(R.string.toast_permission));
            }
        }else{
            //TODO: build version id greater than 23.. do something!!!1
        }

    }

    /**
     * Web api to fetch all record of cities for fetch record of shops against current city
     *
     * @param location
     */
    private void APiGetAllCities(final Location location) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiGetCitiesList, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            String mPlaceName1 = "";
//                            if (location.getLatitude() != 24.586867) {
//                                isLocationAvail = "Yes";
//                                mNewLocation = location;
//                                Utils.savePreferences(activity, "isLocationAvail", "Yes");
//
//                            }
                            citiesNamesIDsResponse = response.getJSONArray("citiesList").toString();
                            Utils.savePreferences(activity, "citiesNamesIDsResponse", citiesNamesIDsResponse);
                            JSONArray jsonArray = response.getJSONArray("citiesList");
                            List<Address> addresses = null;
                            try {

                                Geocoder geocoder = new Geocoder(activity, locale);
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses != null && addresses.size() > 0) {
                                    mPlaceName1 = addresses.get(0).getLocality();
                                } else {
                                    mPlaceName1 = "الرياض";
                                    Utils.savePreferences(activity, "mPlaceName", "الرياض");
                                }

                            } catch (IOException e) {
                                APiGetCurrentAddress(location, jsonArray);
                                e.printStackTrace();
                            }
                            if (addresses != null && addresses.size() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObjectCities = jsonArray.getJSONObject(i);
                                    String cityName = jsonObjectCities.getString("name");

                                    if (mPlaceName1 != null && mPlaceName1.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
                                        CityId = jsonObjectCities.getString("ID");
                                        Utils.savePreferences(activity, "CityId", CityId);
                                        Utils.savePreferences(activity, "mPlaceName", mPlaceName1);
                                        mPlaceName = mPlaceName1;
                                        break;
                                    } else if (!isProbablyArabic(mPlaceName1)) {
                                        Utils.savePreferences(activity, "mPlaceName", "الرياض");
                                        mPlaceName = "الرياض";
                                        if (mPlaceName1 != null && mPlaceName1.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
                                            CityId = jsonObjectCities.getString("ID");
                                            Utils.savePreferences(activity, "CityId", CityId);
                                            break;
                                        }
                                    }
//                                } else {
//                                    if (isProbablyArabic(mPlaceName)) {
//                                    } else {
//                                        if (mPlaceName != null && !mPlaceName.equals("")) {
//                                            if (!isProbablyArabic(mPlaceName)) {
//                                                mPlaceName = "الرياض";
//                                            }
//                                        } else {
//                                            mPlaceName = "الرياض";
//                                        }
//                                        if (mPlaceName != null && mPlaceName.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
//                                            CityId = jsonObjectCities.getString("ID");
//                                            break;
//
//                                        }
//                                    }
//
//                                }
                                }
                                if (mPlaceName.equals("") && CityId.equals("")) {
                                    mPlaceName = "الرياض";
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        JSONObject jsonObjectCities = jsonArray.getJSONObject(k);
                                        String cityName = jsonObjectCities.getString("name");
                                        if (mPlaceName != null && mPlaceName.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
                                            mPlaceName = cityName;
                                            CityId = jsonObjectCities.getString("ID");
                                            Utils.savePreferences(activity, "CityId", CityId);
                                            Utils.savePreferences(activity, "mPlaceName", mPlaceName);
                                            break;

                                        }
                                    }

                                }
                                if (CityId != null && !CityId.equals("")) {
                                    APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, location, CityId);
                                } else {
                                    APiGetShopslistData(AppConfig.APiShopsListData, location, CityId);

                                }
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
//                            }  }
                        } catch (JSONException e) {
                            aQuery.id(R.id.pg_search_this_area).getProgressBar().setVisibility(View.INVISIBLE);
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                            SendFireBaseError(String.valueOf(e));
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
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        queue.add(getRequest);
    }

    /**
     * Handle Received City name string to check if it is arabic .
     *
     * @param cityName Takes String as prams of city name to check its arabic
     * @return
     */
    private static boolean isProbablyArabic(String cityName) {
        if (cityName != null) {
            for (int i = 0; i < cityName.length(); ) {
                int c = cityName.codePointAt(i);
                if (c >= 0x0600 && c <= 0x06E0)
                    return true;
                i += Character.charCount(c);
            }
        }
        return false;
    }

    /**
     * Web APi for fetch current address of user using web api of google
     *
     * @param location  Takes Location as param for user current latitude and longitude
     * @param jsonArray
     */
    private void APiGetCurrentAddress(final Location location, final JSONArray jsonArray) {
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
                            totalDataofCurrentLatlngNames = response.getJSONArray("results");
                            for (int i = 0; i < totalDataofCurrentLatlngNames.length() && !SearchingCityfinished; i++) {
                                JSONObject jsonObject = totalDataofCurrentLatlngNames.getJSONObject(i);
                                JSONArray address_components = jsonObject.getJSONArray("address_components");
                                for (int j = 0; j < address_components.length() && !SearchingCityfinished; j++) {
                                    JSONObject typesLocality = address_components.getJSONObject(j);
                                    String mPlaceName1 = typesLocality.getString("short_name");
                                    if (isProbablyArabic(mPlaceName1)) {


                                        for (int k = 0; k < jsonArray.length() && !SearchingCityfinished; k++) {
                                            JSONObject jsonObjectCities = jsonArray.getJSONObject(k);
                                            String cityName = jsonObjectCities.getString("name");

                                            if (mPlaceName1 != null && mPlaceName1.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
                                                mPlaceName = cityName;
                                                Utils.savePreferences(activity, "mPlaceName", mPlaceName);
                                                CityId = jsonObjectCities.getString("ID");
                                                Utils.savePreferences(activity, "CityId", CityId);
                                                SearchingCityfinished = true;
                                                break;

                                            }
                                        }


                                    }

                                }
                            }


                            if (mPlaceName.equals("") && CityId.equals("")) {
                                mPlaceName = "الرياض";
                                for (int k = 0; k < jsonArray.length(); k++) {
                                    JSONObject jsonObjectCities = jsonArray.getJSONObject(k);
                                    String cityName = jsonObjectCities.getString("name");
                                    if (mPlaceName != null && mPlaceName.toLowerCase(locale).contains(cityName.toLowerCase(locale))) {
                                        mPlaceName = cityName;
                                        CityId = jsonObjectCities.getString("ID");
                                        Utils.savePreferences(activity, "CityId", CityId);
                                        Utils.savePreferences(activity, "mPlaceName", mPlaceName);
                                        break;

                                    }
                                }

                            }
                            if (CityId != null && !CityId.equals("")) {
                                APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, location, CityId);
                            } else {
                                APiGetShopslistData(AppConfig.APiShopsListData, location, CityId);

                            }
                        } catch (JSONException e) {
                            loading.close();
                            SendFireBaseError(String.valueOf(e));
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        queue.add(getRequest);
    }

    /**
     * Web API for fetch whole record of shops with city name
     *
     * @param Url      Takes String as param for web api url to fetch record
     * @param location Takes Location as param for user current latitude and longitude
     * @param CityID   Takes String as pram of filtered city id to fetch record
     */
    private void APiGetShopslistData(String Url, final Location location, final String CityID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
//                        Log.d("Response", response);
                        try {

                            // display response
                            ShopsListDataResponse = response;
                            Utils.savePreferences(activity, "ShopsListDataResponse", ShopsListDataResponse);
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
                        SendFireBaseError(String.valueOf(error));
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
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }

    /**
     * Handle all filtred shops markers point to draw on map as it located in 10km area
     *
     * @param shopsList
     * @param location  Takes Location as param for user current latitude and longitude
     */
    private void SetShopsPointMap(final List<ShopsListModel.ShopslistRecord> shopsList, Location location) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map);
        int count = 0;
        for (int i = 0; i < shopsList.size(); i++) {
            Location target = new Location("Target");
            target.setLatitude(Double.parseDouble(shopsList.get(i).getLatitude()));
            target.setLongitude(Double.parseDouble(shopsList.get(i).getLongitude()));
            float dis = location.distanceTo(target);
            if (mLatLngCurrent.latitude == 24.586867) {

//                marker.setInfoWindowAnchor((float)x, (float)y);
                count++;
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(shopsList.get(i).getLatitude()),
                                Double.parseDouble(shopsList.get(i).getLongitude()))).icon(icon));
                final int finalI = i;
                try {

                    Glide.with(activity)
                            .load(AppConfig.BaseUrlImages + "shop-" + shopsList.get(i).getID() + "/thumbnails/" + shopsList.get(i).getShopImage())
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
                    SendFireBaseError(String.valueOf(d));
                    d.printStackTrace();
                }
            } else {
                if (location.distanceTo(target) < 10000) {
                    count++;
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(shopsList.get(i).getLatitude()),
                                    Double.parseDouble(shopsList.get(i).getLongitude()))).icon(icon));
                    final int finalI = i;
                    try {

                        Glide.with(activity)
                                .load(AppConfig.BaseUrlImages + "shop-" + shopsList.get(i).getID() + "/thumbnails/" + shopsList.get(i).getShopImage())
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
                        SendFireBaseError(String.valueOf(d));
                    }
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
                            mIntent.putExtra("CityId", CityId);
                            ShopDetailsActivity.ShopsListDataResponse = ShopsListDataResponse;
//                            mIntent.putExtra("ShopsListDataResponse", ShopsListDataResponse);
                            mIntent.putExtra("citiesNamesIDsResponse", citiesNamesIDsResponse);
                            mIntent.putExtra("isLocationAvail", isLocationAvail);
                            Bundle args = new Bundle();
                            args.putParcelable("LatLngCurrent", mLatLngCurrent);
                            mIntent.putExtra("placeName", mPlaceName);
                            mIntent.putExtra("bundle", args);
                            activity.startActivity(mIntent);
                        }
                    }
                });
                Projection projection = mMap.getProjection();
                LatLng markerLocation = arg0.getPosition();
                Point screenPosition = projection.toScreenLocation(markerLocation);
                arg0.setInfoWindowAnchor((float) -3.4, (float) 2.4);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_home:

                    return true;
                case R.id.nav_my_orders:

                    return true;
                case R.id.nav_fav_shops:

                    return true;
                case R.id.nav_profile:

                    return true;
                case R.id.nav_settings:

                    return true;
            }
            return false;
        }

    };
    /*
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
*/
    /**
     * Handle User permission for api 23 and above
     *
     * @param requestCode  Takes Integer value to check which permission call is selected
     * @param permissions
     * @param grantResults
     */
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
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        showPermissionDialog();
                    }
                    // User refused to grant permission. You can add AlertDialog here
                    isLocationAvail = "No";
                    Utils.savePreferences(activity, "isLocationAvail", "No");
                    mLatLngCurrent = new LatLng(24.586867, 46.741052);
                    Utils.savePreferences(activity, "mLatLngCurrent", String.valueOf(mLatLngCurrent.latitude + "," + mLatLngCurrent.longitude));
                    mNewLocation = new Location("");
                    mOldLocation = new Location("");
                    mOldLocation.setLatitude(mLatLngCurrent.latitude);
                    mOldLocation.setLongitude(mLatLngCurrent.longitude);
                    mNewLocation.setLatitude(mLatLngCurrent.latitude);
                    mNewLocation.setLongitude(mLatLngCurrent.longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngCurrent, 13));
                    if (Validations.isInternetAvailable(activity, true) && mOldLocation != null) {
                        SearchingCityfinished = false;
                        APiGetAllCities(mOldLocation);
//                        APiGetCurrentAddress("Location", mOldLocation);
                    }
                    showToast(getResources().getString(R.string.toast_permission));
                }
                return;
            }

        }
    }

    /**
     * Handle user permission dialog for accept or neglect for giving permissions
     */
    private void showPermissionDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage(getResources().getString(R.string.dialog_need_your_permission));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                                locationSettings = true;
                            } else {
                                dialog.dismiss();
                                locationSettings = true;
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }


                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    /**
     * Handle Resume of activity and check if user alows permissions then restart app so apply all functions
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (locationSettings) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(activity, SplashActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finishAffinity();
            } else {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Utils.isLocationServiceEnabled(activity)) {
                    Intent i = new Intent(activity, SplashActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finishAffinity();
                }
            }
        }
        if(mapFragment!=null)
            mapFragment.getMapAsync(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
                recreate();
            }
        }
    }

    private void showLocationDialog(){
        // notify user
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(MainActivity.this.getResources().getString(R.string.toast_location_not_found));
        dialog.setPositiveButton(MainActivity.this.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.this.startActivity(myIntent);
                //get gps
            }
        });
        dialog.setNegativeButton(MainActivity.this.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                // go to riyadh as default
                mapFragment.getMapAsync(MainActivity.this);


            }
        });

        dialog.show();
    }

    /**
     * this is called when phone icon is clicked on home map screen which opens the dialog which asks to make call to the operator for assistance
     *
     * @param view
     */
    public void phoneCLicked(View view){

        //call 0599666521 : open dialer
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + getResources().getString(R.string.carefer_phone)));
        startActivity(intent);
    }

}
