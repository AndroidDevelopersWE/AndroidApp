package co.dtechsystem.carefer.UI.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.Adapters.ShopsListRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;
import co.dtechsystem.carefer.Widget.MultiSpinner;

public class ShopsListActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    @SuppressLint("StaticFieldLeak")
    private static ShopsListRecycleViewAdapter mshopsListRecycleViewAdapter;
    private DrawerLayout mDrawerLayout;
    private Spinner sp_service_type_shops_list;
    private Spinner sp_brand_type_shop_list;
    private String mplaceName;
    private MultiSpinner sp_providers_shop_list;
    private EditText et_search_shops_main;
    private ArrayAdapter<String> adapterFilter;
    private TextView tv_total_results_shops_list;
    private TextView tv_title_shops_list;
    private ShopsListModel mShopsListModel;
    private LatLng mLatlngCurrent;
    private SwipeRefreshLayout lay_pull_refresh_shops_list;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);
        sp_service_type_shops_list = (Spinner) findViewById(R.id.sp_service_type_shops_list);
        sp_brand_type_shop_list = (Spinner) findViewById(R.id.sp_brand_type_shop_list);
        sp_providers_shop_list = (MultiSpinner) findViewById(R.id.sp_providers_shop_list);
        //noinspection UnusedAssignment
        Spinner sp_cities_shops_list = (Spinner) findViewById(R.id.sp_cities_shops_list);
        et_search_shops_main = (EditText) findViewById(R.id.et_search_shops_main);
        tv_total_results_shops_list = (TextView) findViewById(R.id.tv_total_results_shops_list);
        tv_title_shops_list = (TextView) findViewById(R.id.tv_title_shops_list);
        lay_pull_refresh_shops_list = (SwipeRefreshLayout) findViewById(R.id.lay_pull_refresh_shops_list);
        lay_pull_refresh_shops_list.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorHeliotrope));
        lay_pull_refresh_shops_list.setOnRefreshListener(this);
        Utils.hideKeyboard(activity);
        SetShaderToViews();
        SetUpLeftbar();

        getDataForView();
        setDataToView();
        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            APiGetShopslistData(AppConfig.APiServiceTypeData, "Services");
        }
        setDataToViews();
    }

    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_shops_list, activity);
    }

    private void setDataToViews() {
        // create spinner list elements for Filter
        adapterFilter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        //added searching
        adapterFilter.add("توفير الضمان");
        adapterFilter.add("قدم استبدال الأجزاء");
        adapterFilter.add("نوع الاختبار");
        adapterFilter.add("أعلى تصنيف");
        adapterFilter.add("مسافة");

        sp_providers_shop_list.setAdapter(adapterFilter, false, onSelectedListenerFilter);
        // set initial selection
        boolean[] selectedItems = new boolean[adapterFilter.getCount()];
        sp_providers_shop_list.setSelected(selectedItems);
        aQuery.find(R.id.tv_providers_shop_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp_providers_shop_list.performClick();
            }
        });


        aQuery.find(R.id.tv_sorting_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomSortingDialog();
            }
        });

        //Searching with name in list
        et_search_shops_main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.iv_search_close_shops_list).visibility(View.VISIBLE);
                    aQuery.find(R.id.iv_search_shops_list).visibility(View.GONE);
                } else {
                    aQuery.find(R.id.iv_search_close_shops_list).visibility(View.GONE);
                    aQuery.find(R.id.iv_search_shops_list).visibility(View.VISIBLE);
                }
                ShopsListRecycleViewAdapter.filterShopsName(s.toString());
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                }
                try {
                    tv_total_results_shops_list.setText(Integer.toString(mShopsListModel.getShopsList().size()) + getResources().getString(R.string.tv_total_results));
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        aQuery.find(R.id.iv_search_shops_list).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                aQuery.find(R.id.lay_title_shops_list).visibility(View.GONE);
//                aQuery.find(R.id.lay_et_search_shops_list).visibility(View.VISIBLE);
//            }
//        });
        aQuery.find(R.id.iv_search_close_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aQuery.find(R.id.iv_search_shops_list).visibility(View.VISIBLE);
                aQuery.find(R.id.iv_search_close_shops_list).visibility(View.GONE);
                et_search_shops_main.setText("");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void getDataForView() {
        if (intent != null) {

            Bundle bundle = intent.getParcelableExtra("bundle");
            if (bundle != null) {
                mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
            }
            mplaceName = intent.getStringExtra("placeName");
        }
    }

    private void setDataToView() {
        if (mplaceName != null && !mplaceName.equals("")) {
            aQuery.find(R.id.tv_location_name_shops_list).text(mplaceName);
        }
    }


//    public static void expandedRefresh() {
//        mshopsListRecycleViewAdapter.notifyDataSetChanged();
//    }

    @SuppressWarnings("unused")
    public void setSpinnerFilter() {
        try {
            sp_service_type_shops_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemService = sp_service_type_shops_list.getSelectedItem().toString();
                    String selectedItemBrand = sp_brand_type_shop_list.getSelectedItem().toString();

                    if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") && selectedItemBrand != null &&
                            selectedItemBrand.equals("Brand")) {
                        ShopsListRecycleViewAdapter.filterShops("Service", "No", selectedItemService, selectedItemBrand);
                        if (mshopsListRecycleViewAdapter != null) {
                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        }
                    } else if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        ShopsListRecycleViewAdapter.filterShops("Service Type", "Yes", selectedItemService, selectedItemBrand);
                        if (mshopsListRecycleViewAdapter != null) {
                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        }

                    } else {
                        assert selectedItemBrand != null;
                        if ((selectedItemService != null && selectedItemService.equals("Service Type")) && selectedItemBrand.equals("Brand")) {
                            ShopsListRecycleViewAdapter.filterShops("Default", "No", selectedItemService, selectedItemBrand);
                            if (mshopsListRecycleViewAdapter != null) {
                                mshopsListRecycleViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    tv_total_results_shops_list.setText(Integer.toString(mShopsListModel.getShopsList().size()) + getResources().getString(R.string.tv_total_results));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sp_brand_type_shop_list.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
                @SuppressWarnings("UnusedAssignment")
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemBrand = sp_brand_type_shop_list.getSelectedItem().toString();
                    String selectedItemService = "";
                    selectedItemService = sp_service_type_shops_list.getSelectedItem().toString();

                    if (selectedItemService != null && selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        ShopsListRecycleViewAdapter.filterShops("Brand", "No", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();

                    } else if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        ShopsListRecycleViewAdapter.filterShops("Brand", "Yes", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();

                    } else {
                        assert selectedItemBrand != null;
                        if ((selectedItemService != null && selectedItemService.equals("Service Type")) && selectedItemBrand.equals("Brand")) {
                            ShopsListRecycleViewAdapter.filterShops("Default", "No", selectedItemService, selectedItemBrand);
                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        }
                    }
                    tv_total_results_shops_list.setText(Integer.toString(mShopsListModel.getShopsList().size()) + getResources().getString(R.string.tv_total_results));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            }));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private final MultiSpinner.MultiSpinnerListener onSelectedListenerFilter = new MultiSpinner.MultiSpinnerListener() {

        @SuppressWarnings({"ConstantConditions", "MismatchedQueryAndUpdateOfStringBuilder"})
        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            StringBuilder builder = new StringBuilder();
            ArrayList<String> selectedItems = new ArrayList<>();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    //noinspection RedundantStringToString,ConstantConditions
                    selectedItems.add(adapterFilter.getItem(i).toString());
                    builder.append(adapterFilter.getItem(i)).append(" ");
                }
            }
            String ProvideWarranty, ProvideReplacementParts, shopType, topRated,Distance;
            if (selected[0]) {
                ProvideWarranty = "1";
            } else {
                ProvideWarranty = "";
            }

            if (selected[1]) {
                ProvideReplacementParts = "1";
            } else {
                ProvideReplacementParts = "";
            }
            if (selected[2]) {
                //noinspection RedundantStringToString
                shopType = adapterFilter.getItem(2).toString();
            } else {
                shopType = "";
            }
            if (selected[3]) {
                topRated = "5";
            } else {
                topRated = "";
            }
            if (selected[4]) {
                Distance = "Highest";
            } else {
                Distance = "";
            }


            ShopsListRecycleViewAdapter.filterShopsWithProviders(selectedItems, ProvideWarranty, ProvideReplacementParts, shopType, topRated,Distance,mLatlngCurrent);
            if (mshopsListRecycleViewAdapter != null) {
                mshopsListRecycleViewAdapter.notifyDataSetChanged();
            }
//            Toast.makeText(activity, builder.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    //Sorting dialog fun
    public void CustomSortingDialog() {
        //Sorting dialog fun
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_sorting);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_name_sorting = (Button) dialog.findViewById(R.id.btn_name_sorting);
        Button btn_rating_sorting = (Button) dialog.findViewById(R.id.btn_rating_sorting);
        Button btn_cancel_sorting = (Button) dialog.findViewById(R.id.btn_cancel_sorting);
        // if button is clicked, close the custom dialog

        btn_name_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomNameSortingDialog();

                dialog.dismiss();
            }
        });
        btn_rating_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopsListRecycleViewAdapter.SortingShopsWithNameRating("Rating","");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        btn_cancel_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    //Name Sorting dialog fun
    public void CustomNameSortingDialog() {
        //Sorting dialog fun
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_sort_name);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_name_sorting = (Button) dialog.findViewById(R.id.btn_name_sorting);
        Button btn_rating_sorting = (Button) dialog.findViewById(R.id.btn_rating_sorting);
        Button btn_cancel_sorting = (Button) dialog.findViewById(R.id.btn_cancel_sorting);
        // if button is clicked, close the custom dialog

        btn_name_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShopsListRecycleViewAdapter.SortingShopsWithNameRating("Name","Ascending");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        btn_rating_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopsListRecycleViewAdapter.SortingShopsWithNameRating("Name","Descending");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        btn_cancel_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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

    private void APiGetShopslistData(final String Url, final String Type) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressWarnings("IfCanBeSwitch")
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            List listservices = new ArrayList();
                            //noinspection unchecked
                            listservices.add(0, "Service Type");
                            List brands = new ArrayList();
                            //noinspection unchecked
                            brands.add(0, "Brand");
                            if (Type.equals("Services")) {
                                JSONArray brandsData = response.getJSONArray("serviceTypeData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    listservices.add(jsonObject.getString("serviceTypeName"));
                                }
                                @SuppressWarnings("unchecked") ArrayAdapter StringdataAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, listservices);
                                sp_service_type_shops_list.setAdapter(StringdataAdapter);
                                APiGetShopslistData(AppConfig.APiBrandData, "Brands");
                            } else if (Type.equals("Brands")) {
                                JSONArray brandsData = response.getJSONArray("brandsData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    brands.add(jsonObject.getString("brandName"));
                                }
                                @SuppressWarnings("unchecked") ArrayAdapter StringdataAdapterbrands = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, brands);
                                sp_brand_type_shop_list.setAdapter(StringdataAdapterbrands);
                                APiGetShopslistData(AppConfig.APiShopsListData, "Shops");
                            } else {
                                mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
                                if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
                                    mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, mShopsListModel.getShopsList(), mLatlngCurrent);
                                    SetListData();
                                    loading.close();
                                } else {
                                    if (lay_pull_refresh_shops_list.isRefreshing()) {
                                        lay_pull_refresh_shops_list.setRefreshing(false);
                                    }
                                    loading.close();
                                    showToast(getResources().getString(R.string.no_record_found));

                                }
//                                setSpinnerFilter();
                            }

                        } catch (JSONException e) {
                            loading.close();
                            if (lay_pull_refresh_shops_list.isRefreshing()) {
                                lay_pull_refresh_shops_list.setRefreshing(false);
                            }
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (lay_pull_refresh_shops_list.isRefreshing()) {
                            lay_pull_refresh_shops_list.setRefreshing(false);
                        }
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    //Setting Shops List Data
    @SuppressLint("SetTextI18n")
    private void SetListData() {
        try {

            tv_total_results_shops_list.setText(Integer.toString(mShopsListModel.getShopsList().size()) + getResources().getString(R.string.tv_total_results));
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_shop_list);
            recyclerView.getItemAnimator().setChangeDuration(700);
            recyclerView.setAdapter(mshopsListRecycleViewAdapter);
            GridLayoutManager mgridLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mgridLayoutManager);
            if (lay_pull_refresh_shops_list.isRefreshing()) {
                lay_pull_refresh_shops_list.setRefreshing(false);
            }
        } catch (Exception e) {
            if (lay_pull_refresh_shops_list.isRefreshing()) {
                lay_pull_refresh_shops_list.setRefreshing(false);
            }
            e.printStackTrace();
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

    @Override
    public void onRefresh() {
        if (Validations.isInternetAvailable(activity, true)) {
            APiGetShopslistData(AppConfig.APiShopsListData, "Shops");
        }
    }
}
