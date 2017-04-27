package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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

import co.dtechsystem.carefer.Adapters.ShopsListRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;

public class ShopsListActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    GridLayoutManager mgridLayoutManager;
    static ShopsListRecycleViewAdapter mshopsListRecycleViewAdapter;
    DrawerLayout mDrawerLayout;
    Spinner sp_service_type_shops_list;
    Spinner sp_brand_type_shop_list;
    private String mplaceName;
    EditText et_search_shops_main;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);
        sp_service_type_shops_list = (Spinner) findViewById(R.id.sp_service_type_shops_list);
        sp_brand_type_shop_list = (Spinner) findViewById(R.id.sp_brand_type_shop_list);
        et_search_shops_main = (EditText) findViewById(R.id.et_search_shops_main);
        SetUpLeftbar();
        loading.show();
        getDataForView();
        setDataToView();
        APiGetShopslistData(AppConfig.APiServiceTypeData, "Services");
        setDataToViews();
    }

    public void setDataToViews() {
        et_search_shops_main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mshopsListRecycleViewAdapter.filterShopsName(s.toString());
                mshopsListRecycleViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void getDataForView() {
        if (intent != null) {
            mplaceName = intent.getStringExtra("placeName");
        }
    }

    public void setDataToView() {
        if (mplaceName != null && !mplaceName.equals("")) {
            aQuery.find(R.id.tv_location_name_shops_list).text(mplaceName);
        }
    }

    public void SetListData() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_shop_list);
        recyclerView.getItemAnimator().setChangeDuration(700);
        recyclerView.setAdapter(mshopsListRecycleViewAdapter);
        mgridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mgridLayoutManager);
    }

    public static void expandedRefresh() {
        mshopsListRecycleViewAdapter.notifyDataSetChanged();
    }

    public void setSpinnerFilter() {
        try {
            sp_service_type_shops_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemService = sp_service_type_shops_list.getSelectedItem().toString();
                    String selectedItemBrand = sp_brand_type_shop_list.getSelectedItem().toString();

                    if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") && selectedItemBrand != null &&
                            selectedItemBrand.equals("Brand")) {
                        mshopsListRecycleViewAdapter.filterShops("Service", "No", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    } else if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        mshopsListRecycleViewAdapter.filterShops("Service Type", "Yes", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();

                    } else if (selectedItemService.equals("Service Type") && selectedItemBrand.equals("Brand")) {
                        mshopsListRecycleViewAdapter.filterShops("Default", "No", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sp_brand_type_shop_list.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemBrand = sp_brand_type_shop_list.getSelectedItem().toString();
                    String selectedItemService = "";
                    selectedItemService = sp_service_type_shops_list.getSelectedItem().toString();

                    if (selectedItemService != null && selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        mshopsListRecycleViewAdapter.filterShops("Brand", "No", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();

                    } else if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        mshopsListRecycleViewAdapter.filterShops("Brand", "Yes", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();

                    } else if (selectedItemService.equals("Service Type") && selectedItemBrand.equals("Brand")) {
                        mshopsListRecycleViewAdapter.filterShops("Default", "No", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
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
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void APiGetShopslistData(final String Url, final String Type) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            List listservices = new ArrayList();
                            listservices.add(0, "Service Type");
                            List brands = new ArrayList();
                            brands.add(0, "Brand");
                            if (Type.equals("Services")) {
                                JSONArray brandsData = response.getJSONArray("serviceTypeData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    listservices.add(jsonObject.getString("serviceTypeName"));
                                }
                                ArrayAdapter StringdataAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, listservices);
                                sp_service_type_shops_list.setAdapter(StringdataAdapter);
                                APiGetShopslistData(AppConfig.APiBrandData, "Brands");
                            } else if (Type.equals("Brands")) {
                                JSONArray brandsData = response.getJSONArray("brandsData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    brands.add(jsonObject.getString("brandName"));
                                }
                                ArrayAdapter StringdataAdapterbrands = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, brands);
                                sp_brand_type_shop_list.setAdapter(StringdataAdapterbrands);
                                APiGetShopslistData(AppConfig.APiShopsListData, "Shops");
                            } else {
                                ShopsListModel mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
                                if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
                                    mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, mShopsListModel.getShopsList());
                                    SetListData();
                                    loading.close();
                                } else {
                                    loading.close();
                                    showToast(getResources().getString(R.string.no_record_found));

                                }
                                setSpinnerFilter();
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
