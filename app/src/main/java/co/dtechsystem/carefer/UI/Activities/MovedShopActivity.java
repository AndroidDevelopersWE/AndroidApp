package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.dtechsystem.carefer.Adapters.MovedShopAdapter;
import co.dtechsystem.carefer.Models.MovedShopPriceModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Constants;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;
import co.dtechsystem.carefer.Widget.ListSpinnerDialog;
import co.dtechsystem.carefer.services.FetchAddressIntentService;

@SuppressWarnings("unchecked")
public class MovedShopActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PLACE_PICKER_REQUEST = 1001;
    private DrawerLayout mDrawerLayout;


    private TextView tv_car_brand_my_details;
    private TextView tv_car_model_my_details;
    private TextView tv_service_type;

    private TextView tv_brands;
    private final Calendar myCalendar = Calendar.getInstance(locale);
    private final ArrayList<String> mServicesIdArray = new ArrayList<>();
    private final ArrayList<String> mBrandsIdArray = new ArrayList<>();
    private final ArrayList<String> mModelsIdArray = new ArrayList<>();
    private String mBrandsId;
    private String mModelsId;
    private String mServicesId;
    private boolean mModelData;
    private boolean mBrandData;
    private ArrayList<String> brands = new ArrayList<String>();
    private ArrayList<String> models = new ArrayList();
    private ArrayList<String> mServices = new ArrayList();
    private  MovedShopPriceModel movedShopPriceModel;

    private boolean firstBrand = true;
    private boolean firstModel = true;
    private boolean firstService = true;
    private boolean No_Price_list = false;

    private ArrayAdapter StringModeldataAdapter;
    private LatLng latLng;
    private boolean mPriceIsSet = false;
    private String mPrice;
    private String mAddress;

    RecyclerView listRecyclerView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager listLayoutManager;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // your language
        String languageToLoad = "ar";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_moved_shop);

        initializeViews();

        SetUpLeftbar();

        getBrandAndDescriptionFromServer();

        startGetAddressIntentService();

    }

    private void getBrandAndDescriptionFromServer() {
        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            loading2.show();
            //  APiMyDetails(AppConfig.APiGetCustomerDetails + sUser_ID, "getUserDetails", "", "", "", "", "", "", "");
            APiGetBrandsServiceModelsData(AppConfig.APiBrandData, "Brands", "");
            APIgetDescription(AppConfig.APiGetMovedShopDesc);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(this, data);
                latLng = selectedPlace.getLatLng();
                mAddress = selectedPlace.getAddress().toString();
                aQuery.id(R.id.tv_address).text(selectedPlace.getAddress());
            }
        }
    }

    private void initializeViews() {

        listRecyclerView = (RecyclerView) findViewById(R.id.moved_shoped_price_recycler_view);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setHasFixedSize(true);
        listLayoutManager = new LinearLayoutManager(this);
        listRecyclerView.setLayoutManager(listLayoutManager);


        tv_car_brand_my_details = (TextView) findViewById(R.id.tv_car_brand_my_details);
        tv_car_model_my_details = (TextView) findViewById(R.id.tv_car_model_my_details);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);

        tv_brands = (TextView) findViewById(R.id.et_car_brand_my_details);
        tv_brands.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (brands == null || brands.isEmpty()) {
                    getBrandAndDescriptionFromServer();
                } else{
                    ListSpinnerDialog dialog = new ListSpinnerDialog(MovedShopActivity.this, brands);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                            int selected=((ListSpinnerDialog) dialog).getSelected();
                            if (selected>=0)
                            {
                                tv_brands.setText(brands.get(selected));
                                if (Validations.isInternetAvailable(activity, true)) {
                                    loading.show();
                                    aQuery.id(R.id.tv_price_detail).text(getResources().getString(R.string.tv_price_detail));
                                    APiGetBrandsServiceModelsData(AppConfig.APiGetBrandModels, "ModelYear", mBrandsIdArray.get(selected));
                                    aQuery.find(R.id.et_car_model_my_details).text("");

                                }
                                mBrandsId = mBrandsIdArray.get(selected);
                                mModelsId="";
                            }
                        }
                    });
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                }

                   // mSpinner_brands.showDialog();

            }
        });
        aQuery.id(R.id.et_car_model_my_details).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aQuery.find(R.id.tv_service_type_selector).text("");
                if (brands == null || models.isEmpty()) {
                    getBrandAndDescriptionFromServer();
                } else{
                    ListSpinnerDialog dialog = new ListSpinnerDialog(MovedShopActivity.this, models);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            int selected=((ListSpinnerDialog) dialog).getSelected();
                            if (selected>=0)
                            {
                                try{
                                    aQuery.id(R.id.tv_price_detail).visibility(View.VISIBLE).text(getResources().getString(R.string.tv_price_detail));
                                    aQuery.id(R.id.moved_shoped_price_recycler_view).visibility(View.GONE);
                                    aQuery.find(R.id.et_car_model_my_details).text(models.get(selected));
                                    mModelsId = mModelsIdArray.get(selected);
                                    aQuery.find(R.id.tv_service_type_selector).text("");
                                    mServicesId="";

                                    if (firstService)
                                        APiGetServicesData(AppConfig.APiServiceTypeData);
                                }catch (Exception e){
                                    SendFireBaseError(String.valueOf(e));
                                }

                            }
                        }
                    });
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                }

            }
        });
        aQuery.id(R.id.tv_service_type_selector).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServices != null || !mServices.isEmpty()){
                    ListSpinnerDialog dialog = new ListSpinnerDialog(MovedShopActivity.this, mServices);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            int selected=((ListSpinnerDialog) dialog).getSelected();
                            if (selected>=0)
                            {
                                aQuery.find(R.id.tv_service_type_selector).text(mServices.get(selected));
                                mServicesId = mServicesIdArray.get(selected);
                                APiGetPrice(AppConfig.APiGetPriceShop);
                            }
                        }
                    });
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                }
            }
        });
        aQuery.id(R.id.tv_edit_location).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MovedShopActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        SetShaderToViews();

    }

    private void SetSpinnerListener() {
    }

    private void APiGetBrandsServiceModelsData(final String Url, final String Type, final String BrandId) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response

                        aQuery.id(R.id.tv_price_detail).visibility(View.VISIBLE).text(getResources().getString(R.string.tv_price_detail));
                        aQuery.id(R.id.moved_shoped_price_recycler_view).visibility(View.GONE);
                        aQuery.find(R.id.et_car_model_my_details).text("");
                        aQuery.find(R.id.tv_service_type_selector).text("");
                        mBrandsIdArray.clear();
                        mModelsIdArray.clear();
                        brands.clear();

                        try {
                            JSONObject response = new JSONObject(res);
                            if (Type.equals("Brands")) {

                                JSONArray brandsData = response.getJSONArray("brandsData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    brands.add(jsonObject.getString("brandName"));
                                    mBrandsIdArray.add(jsonObject.getString("ID"));
                                }

                                loading.close();
                            } else {
                                JSONArray modelsData = response.getJSONArray("models");
                                models.clear();
                                if (modelsData.length() == 0) {
//                                    models.add(0, getResources().getString(R.string.dp_model));

                                    //aQuery.find(R.id.sp_brand_type_shop_details_order).text(aQuery.find(R.string.toast_select_one_drop).getText());

                                }

                                for (int i = 0; i < modelsData.length(); i++) {
                                    JSONObject jsonObject = modelsData.getJSONObject(i);
                                    //noinspection unchecked
                                    models.add(jsonObject.getString("modelName"));
                                    mModelsIdArray.add(jsonObject.getString("ID"));
                                }
                                //mSpinner_models.setList(models.toArray(new CharSequence[models.size()]));
                                //aQuery.find(R.id.et_car_model_my_details).text(getResources().getString(R.string.dp_model));
                                loading.close();

                            }
                            //SetSpinnerListener();
                            loading.close();
                        } catch (JSONException e) {
                            loading.close();
                            SendFireBaseError(String.valueOf(e));
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!Type.equals("Services & Brands")) {
                    params.put("brandID", BrandId);
                }


                return params;
            }
        };

// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void APiGetServicesData(final String Url) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response

                        mServicesIdArray.clear();
                        mServices.clear();
                        try {
                            JSONObject response = new JSONObject(res);
                            //mServices.add(getResources().getString(R.string.dp_service_type));
                            //mServicesIdArray.add("");
                            JSONArray servicesData = response.getJSONArray("serviceTypeData");

                            for (int i = 0; i < servicesData.length(); i++) {

                                JSONObject jsonObject = servicesData.getJSONObject(i);
                                //noinspection unchecked
                                mServices.add(jsonObject.getString("serviceTypeName"));
                                mServicesIdArray.add(jsonObject.getString("ID"));
                            }

                            loading.close();

                        } catch (JSONException e) {
                            loading.close();
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                       // SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };

// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    private void APiGetPrice(final String Url) {
        // prepare the Request

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        try {
                            aQuery.id(R.id.tv_price_detail).visibility(View.GONE);
                            aQuery.id(R.id.moved_shoped_price_recycler_view).visibility(View.VISIBLE);

                            JSONObject response = new JSONObject(res);
                            mPriceIsSet = true;

                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            movedShopPriceModel = gson.fromJson(String.valueOf(res), MovedShopPriceModel.class);

                            if(movedShopPriceModel.getPrice().isEmpty()){
                                No_Price_list = false;
                                aQuery.id(R.id.tv_price_detail).visibility(View.VISIBLE).text(getResources().getString(R.string.tv_service_with_out_price));
                            }else{
                                No_Price_list = true;
                            }

                            for(int i=0; i <movedShopPriceModel.getPrice().size(); i++){
                                movedShopPriceModel.getPrice().get(i).setIs_selected("false");
                            }

                            // populating menu category list.
                            listAdapter = new MovedShopAdapter(MovedShopActivity.this, movedShopPriceModel.getPrice());
                            listRecyclerView.setAdapter(listAdapter);



                            loading.close();

                        } catch (JSONException e) {
                            loading.close();
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        SendFireBaseError(String.valueOf(error));
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("brand", mBrandsId);
                params.put("model", mModelsId);
                params.put("servicetype", mServicesId);

                return params;
            }
        };

// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }


    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {

        Utils.gradientTextViewShort(tv_car_brand_my_details, activity);
        Utils.gradientTextViewShort(tv_car_model_my_details, activity);
        Utils.gradientTextViewShort(tv_service_type, activity);
        Utils.gradientTextViewShort(aQuery.id(R.id.tv_price).getTextView(), activity);
        Utils.gradientTextViewShort(aQuery.id(R.id.tv_location).getTextView(), activity);


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

    /**
     * compares the starting of the brands with the given string
     *
     * @param sequence
     * @return
     */
    ArrayList<String> getSearchResult(CharSequence sequence) {
        ArrayList<String> resultant = new ArrayList<String>();
        String seq = sequence.toString();
        for (String brand : brands) {
            if (brand.startsWith(seq))
                resultant.add(brand);
        }

        return resultant;
    }


    public void submitUserData(View view) {
        if (validateEntries()) {

            if(No_Price_list){
                JSONObject jsonPriceObj = MakingJsonObjOfPrice();
                try{
                    JSONArray jsonArray = jsonPriceObj.getJSONArray("PriceDetail");
                    if(jsonArray.length()>0){
                        loading.show();
                        mPrice = jsonPriceObj.toString();
                        APiSendMovedCarOrder(AppConfig.APiSaveOrder, mBrandsId, mModelsId, mServicesId, sUser_ID, "" + latLng.latitude, "" + latLng.longitude, mAddress, mPrice);
                    }else{
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid_price),Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.e("error",e.toString());
                }
            }else{

                mPrice = "0.0";
                APiSendMovedCarOrder(AppConfig.APiSaveOrder, mBrandsId, mModelsId, mServicesId, sUser_ID, "" + latLng.latitude, "" + latLng.longitude, mAddress, mPrice);
            }



        }

    }


    private JSONObject MakingJsonObjOfPrice(){


        //Create json array for price and  description
        JSONArray priceDetailArray = new JSONArray();

        // creating json objects and storing in OrderDetailArray
        try {

            for(int i=0; i< movedShopPriceModel.getPrice().size(); i++)
            {
                if(movedShopPriceModel.getPrice().get(i).getIs_selected().equals("true")){

                    JSONObject priceDetailObj =new JSONObject();
                    priceDetailObj.put("price", movedShopPriceModel.getPrice().get(i).getPrice());
                    priceDetailObj.put("priceDesc", movedShopPriceModel.getPrice().get(i).getPriceDesc());

                    // adding objects in array
                    priceDetailArray.put(priceDetailObj);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adding orderDetailArray to store object
        JSONObject priceMainObj = new JSONObject();
        try {
            priceMainObj.put("PriceDetail",priceDetailArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return priceMainObj;
    }


    private boolean validateEntries() {

        if (mBrandsId == null || mBrandsId.isEmpty()) {
            Toast.makeText(MovedShopActivity.this, R.string.toast_select_brand_type, Toast.LENGTH_LONG).show();
            return false;

        }
        if (mModelsId  == null || mModelsId.isEmpty()) {
            Toast.makeText(MovedShopActivity.this, R.string.toast_select_model_type, Toast.LENGTH_LONG).show();
            return false;

        }
        if (mServicesId  == null || mServicesId.isEmpty()) {
            Toast.makeText(MovedShopActivity.this, R.string.toast_select_service_type, Toast.LENGTH_LONG).show();
            return false;

        }
        if (latLng == null) {
            if (mLastLocation==null){
                Toast.makeText(MovedShopActivity.this, R.string.select_location, Toast.LENGTH_LONG).show();
                return false;}
            else {
                latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            }
        }

        return true;

    }

    /**
     * @param Url
     */
    private void APiSendMovedCarOrder(final String Url, final String brandID, final String modelId, final String servieId, final String customerId, final String lat, final String lng, final String address, final String price) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            int morderID = jsonObject.getInt("orderID");
                            if (morderID != 0) {
                                orderPlaced("" + morderID);
                            }


                            // aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
                            loading.close();

                        } catch (JSONException e) {
                            loading.close();
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
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("shopID", "0");
                params.put("serviceTypeID", servieId);
                params.put("brandId", brandID);
                params.put("customerID", customerId);
                params.put("modelId", modelId);
                params.put("orderServiceType", "movedShop");
                params.put("orderStatus", "1");
                params.put("address", address);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("price", price);

                return params;
            }
        };

// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    private void orderPlaced(String orderId) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(getResources().getString(R.string.app_name));
        alertDialog.setMessage(getResources().getString(R.string.toast_order_placed) + " orderID = " + orderId);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dialog.dismiss();


                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                MovedShopActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    public void APIgetDescription(String Url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        // display response
                        try {
                            JSONObject jsonObject = new JSONObject(res);
                            String description = jsonObject.getString("description");
                            if (description != null) {
                                aQuery.id(R.id.tv_moved_shop_receive_car_description).text(description);
                            }

                            // aQuery.id(R.id.sp_brand_type_shop_details_order).getSpinner().performClick();
                            loading2.close();

                        } catch (JSONException e) {
                            loading2.close();
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading2.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };
        queue.add(postRequest);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput(mAddressOutput);
            mAddress=mAddressOutput;

        }
    }

    private void displayAddressOutput(String address) {
        aQuery.id(R.id.tv_address).text(address);

    }

    protected Location mLastLocation;
    private MovedShopActivity.AddressResultReceiver mResultReceiver;
    private FusedLocationProviderClient mFusedLocationClient;

    protected void startGetAddressIntentService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                mLastLocation = location;
                                Intent intent = new Intent(MovedShopActivity.this, FetchAddressIntentService.class);
                                mResultReceiver = new MovedShopActivity.AddressResultReceiver(new Handler());
                                intent.putExtra(Constants.RECEIVER, mResultReceiver);
                                intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
                                startService(intent);

                            } else {
                                Toast.makeText(activity, getString(R.string.toast_location_not_found), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

}
