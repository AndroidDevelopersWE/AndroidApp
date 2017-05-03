package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;

public class ShopDetailsOrderActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    private String mshopID, mshopName, mshopType, mshopRating, mlatitude, mlongitude, mshopImage;
    ArrayList<String> mServicesIdArray = new ArrayList<>();
    ArrayList<String> mBrandsIdArray = new ArrayList<>();
    ArrayList<String> mModelsIdArray = new ArrayList<>();
    String mServicesId, mBrandsId, mModelsId;
    List listservices = new ArrayList();
    List brands = new ArrayList();

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details_order);
        SetUpLeftbar();
        GetDataForViews();
        SetDataTOViews();
    }

    // Get Views Data
    public void GetDataForViews() {
        if (intent != null) {
            mshopID = intent.getStringExtra("shopID");
            mshopName = intent.getStringExtra("shopName");
            mshopType = intent.getStringExtra("shopType");
            mshopRating = intent.getStringExtra("shopRating");
            mlatitude = intent.getStringExtra("latitude");
            mlongitude = intent.getStringExtra("longitude");
            mshopImage = intent.getStringExtra("shopImage");
        }
    }

    // Set views data
    public void SetDataTOViews() {
        loading.show();
        APiGetBrandsServiceModelsData(AppConfig.APiShopsDetailsData + mshopID + "/cusid/" + sUser_ID, "Services & Brands");
        aQuery.id(R.id.tv_shop_name_shop_details_order).text(mshopName);
        aQuery.id(R.id.tv_shop_type_shop_details_order).text(mshopType);
        aQuery.id(R.id.rb_shop_rating_shop_details_order).rating(Float.parseFloat(mshopRating));

        //Lists initilization
        listservices.clear();
        listservices.add(0, "Service Type");
        brands.clear();
        brands.add(0, "Brand");
        mServicesIdArray.clear();
        mBrandsIdArray.clear();
        mModelsIdArray.clear();
        mServicesIdArray.add(0, "0");
        mBrandsIdArray.add(0, "0");
        mModelsIdArray.add(0, "0");
//        ArrayList<String> years = new ArrayList<String>();
//        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
//        for (int i = 1900; i <= thisYear; i++) {
//            years.add(Integer.toString(i));
//        }
//        ArrayAdapter<String> modelyearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

    }

    public void APiGetBrandsServiceModelsData(final String Url, final String Type) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {

                            if (Type.equals("Services & Brands")) {
                                JSONArray shopServiceTypes = response.getJSONArray("shopServiceTypes");
                                for (int i = 0; i < shopServiceTypes.length(); i++) {
                                    JSONObject jsonObject = shopServiceTypes.getJSONObject(i);
                                    listservices.add(jsonObject.getString("serviceTypeName"));
                                    mServicesIdArray.add(jsonObject.getString("ID"));

                                }
                                ArrayAdapter StringdataAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, listservices);
                                aQuery.id(R.id.sp_srvice_type_shop_details_order).adapter(StringdataAdapter);
                                JSONArray brandsData = response.getJSONArray("shopBrands");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    brands.add(jsonObject.getString("brandName"));
                                    mBrandsIdArray.add(jsonObject.getString("ID"));
                                }
                                ArrayAdapter StringdataAdapterbrands = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, brands);
                                aQuery.id(R.id.sp_brand_type_shop_details_order).adapter(StringdataAdapterbrands);
                                APiGetBrandsServiceModelsData(AppConfig.APiShopsDetailsOrderModel, "ModelYear");
                            } else {
                                List models = new ArrayList();
                                models.add(0, "Model");
                                JSONArray brandsData = response.getJSONArray("modelData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    models.add(jsonObject.getString("modelName"));
                                    mModelsIdArray.add(jsonObject.getString("ID"));
                                }
                                ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, models);
                                aQuery.id(R.id.sp_car_model_order).adapter(StringModeldataAdapter);
                                loading.close();
                                SetSpinnerListener();
                            }

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
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void SetSpinnerListener() {
        aQuery.id(R.id.sp_srvice_type_shop_details_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mServicesId = mServicesIdArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aQuery.id(R.id.sp_brand_type_shop_details_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBrandsId = mBrandsIdArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aQuery.id(R.id.sp_car_model_order).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mModelsId = mModelsIdArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void GotoOrderNow(View V) {
        String spServiceTypeText = aQuery.id(R.id.sp_srvice_type_shop_details_order).getSelectedItem().toString();
        String spbrandTypeText = aQuery.id(R.id.sp_brand_type_shop_details_order).getSelectedItem().toString();
        String spmodelTypeText = aQuery.id(R.id.sp_car_model_order).getSelectedItem().toString();
        if (spServiceTypeText.equals("Service Type") || spbrandTypeText.equals("Brand") || spmodelTypeText.equals("Model")) {
            showToast("Please select one from dropdown!");
        } else {
            Intent i = new Intent(this, OrderNowActivity.class);
            i.putExtra("latitude", mlatitude);
            i.putExtra("longitude", mlongitude);
            i.putExtra("shopID", mshopID);
            i.putExtra("serviceID", mServicesId);
            i.putExtra("brandID", mBrandsId);
            i.putExtra("modelID", mModelsId);
            i.putExtra("shopImage", mshopImage);

            startActivity(i);
        }
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
