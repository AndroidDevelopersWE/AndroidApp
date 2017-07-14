package co.dtechsystem.carefer.UI.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String ShopsData;
    RecyclerView recyclerView;
    Button btn_back_top_shops_list;
    String ShopsListDataResponse = "", citiesNamesIDsResponse = "", CityId = "", isLocationAvail = "";
    private final List listCities = new ArrayList();
    private final List listCitiesId = new ArrayList();
    int check = 0;
    boolean found = false;
    String CityName = "", provide_warranty = "", provide_ReplaceParts = "", topRated = "", placeType = "", brandType = "", serviceType = "", FilterRecord = "";
    Intent FilteredData;
    ArrayList<Integer> CheckedServices = new ArrayList<Integer>();
    ArrayList<Integer> CheckedBrands = new ArrayList<Integer>();
    ArrayList<Integer> CheckedShopTypes = new ArrayList<Integer>();
    String callType = "";
    boolean locationSettings = false;

    @SuppressWarnings("deprecation")
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_list);
        sp_service_type_shops_list = (Spinner) findViewById(R.id.sp_service_type_shops_list);
        sp_brand_type_shop_list = (Spinner) findViewById(R.id.sp_brand_type_shop_list);
        sp_providers_shop_list = (MultiSpinner) findViewById(R.id.sp_providers_shop_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_shop_list);
        btn_back_top_shops_list = (Button) findViewById(R.id.btn_back_top_shops_list);
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
        if (intent.getExtras() != null) {
            ShopsListDataResponse = intent.getStringExtra("ShopsListDataResponse");
            citiesNamesIDsResponse = intent.getStringExtra("citiesNamesIDsResponse");
            CityId = intent.getStringExtra("CityId");
            isLocationAvail = intent.getStringExtra("isLocationAvail");
            if (intent.getExtras().getString("callType") != null) {
                callType = intent.getStringExtra("callType");
            } else {
                callType = "";
            }
        }
        getDataForView();
        setDataToView();
        setDataToViews();
        if (ShopsListDataResponse != null && !ShopsListDataResponse.equals("") && citiesNamesIDsResponse != null && !citiesNamesIDsResponse.equals("")) {
            ShopsData = ShopsListDataResponse;
            loading.show();
//                SetListData("List", mShopsListModel.getShopsList().size());
            AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
            asyncTaskRunner.execute("List", "");
            if (citiesNamesIDsResponse != null && !citiesNamesIDsResponse.equals("")) {
                if (listCities != null) {
                    listCities.clear();
                }
                if (mplaceName != null && !mplaceName.equals("")) {
//                        listCities.add(0, mplaceName);
                } else {
                    listCities.add(0, getResources().getString(R.string.tv_city));
                    listCitiesId.add(0, "0");
                }

                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(citiesNamesIDsResponse);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String CityId = jsonObject.getString("ID");
                        listCities.add(name);
                        listCitiesId.add(CityId);
                    }
                    setCityDropDownData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {
            if (Validations.isInternetAvailable(activity, true)) {
                loading.show();
                APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, "Shops", "City", CityId);
            }
        }


    }

    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_shops_list, activity);
    }

    private void setDataToViews() {
        // create spinner list elements for Filter
        adapterFilter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        //added searching
        adapterFilter.add(getResources().getString(R.string.spinner_provided_warranty));
        adapterFilter.add(getResources().getString(R.string.spinner_provided_replace_parts));
        adapterFilter.add(getResources().getString(R.string.spinner_place_type));
        adapterFilter.add(getResources().getString(R.string.spinner_top_rated));
        adapterFilter.add(getResources().getString(R.string.spinner_distance));

        sp_providers_shop_list.setAdapter(adapterFilter, false, onSelectedListenerFilter);
        // set initial selection
        boolean[] selectedItems = new boolean[adapterFilter.getCount()];
        sp_providers_shop_list.setSelected(selectedItems);
        aQuery.find(R.id.tv_providers_shop_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sp_providers_shop_list.performClick();
                if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {

                    Intent intent = new Intent(activity, FiltersActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("LatLngCurrent", mLatlngCurrent);
                    intent.putExtra("ShopsDataResponse", ShopsData);
                    if (FilteredData != null && FilteredData.getExtras() != null) {
                        intent.putExtra("provide_warranty", provide_warranty);
                        intent.putExtra("provide_ReplaceParts", provide_ReplaceParts);
                        intent.putExtra("topRated", topRated);
                        intent.putExtra("placeType", placeType);
                        intent.putExtra("brandType", brandType);
                        intent.putExtra("serviceType", serviceType);
                        intent.putExtra("FilterRecord", FilterRecord);
                        intent.putIntegerArrayListExtra("CheckedBrands", CheckedBrands);
                        intent.putIntegerArrayListExtra("CheckedShopTypes", CheckedShopTypes);
                        intent.putIntegerArrayListExtra("CheckedServices", CheckedServices);
                    }
                    startActivityForResult(intent, 1);
                } else {

                }
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
                ShopsListRecycleViewAdapter.filterShopsName(s.toString(), mLatlngCurrent);
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

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
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

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
                            tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                        }
                    } else if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        ShopsListRecycleViewAdapter.filterShops("Service Type", "Yes", selectedItemService, selectedItemBrand);
                        if (mshopsListRecycleViewAdapter != null) {
                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
                            tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                        }

                    } else {
                        assert selectedItemBrand != null;
                        if ((selectedItemService != null && selectedItemService.equals("Service Type")) && selectedItemBrand.equals("Brand")) {
                            ShopsListRecycleViewAdapter.filterShops("Default", "No", selectedItemService, selectedItemBrand);
                            if (mshopsListRecycleViewAdapter != null) {
                                mshopsListRecycleViewAdapter.notifyDataSetChanged();
                                tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

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
                        tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());


                    } else if (selectedItemService
                            != null && !selectedItemService.equals("Service Type") &&
                            selectedItemBrand != null && !selectedItemBrand.equals("Brand")) {
                        ShopsListRecycleViewAdapter.filterShops("Brand", "Yes", selectedItemService, selectedItemBrand);
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());


                    } else {
                        assert selectedItemBrand != null;
                        if ((selectedItemService != null && selectedItemService.equals("Service Type")) && selectedItemBrand.equals("Brand")) {
                            ShopsListRecycleViewAdapter.filterShops("Default", "No", selectedItemService, selectedItemBrand);
                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
                            tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

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
            String ProvideWarranty, ProvideReplacementParts, shopType, topRated, Distance;
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


            ShopsListRecycleViewAdapter.filterShopsWithProviders(selectedItems, ProvideWarranty, ProvideReplacementParts, shopType, topRated, Distance, mLatlngCurrent);
            if (mshopsListRecycleViewAdapter != null) {
                mshopsListRecycleViewAdapter.notifyDataSetChanged();
                tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());
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
        Button btn_distance_sorting = (Button) dialog.findViewById(R.id.btn_distance_sorting);
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

                ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Rating", "", mLatlngCurrent, "");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
                dialog.dismiss();
            }
        });
        btn_distance_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationAvail != null && isLocationAvail.equals("Yes")) {
                    ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Distance", "", mLatlngCurrent, "");
                    if (mshopsListRecycleViewAdapter != null) {
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                    }
                    dialog.dismiss();
                } else {
                    showToast(getResources().getString(R.string.toast_location_not_found));
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();
                        locationSettings = true;
                    } catch (Exception e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }

                }
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

    @Override
    protected void onResume() {
        if (locationSettings) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(activity, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
                finish();
            }
        }
        super.onResume();
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

                ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Name", "Ascending", mLatlngCurrent, "");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
                dialog.dismiss();
            }
        });
        btn_rating_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Name", "Descending", mLatlngCurrent, "");
                if (mshopsListRecycleViewAdapter != null) {
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

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
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (callType != null && callType.equals("Navigation") || callType.equals("callOrder")) {
                intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            super.onBackPressed();
        }
    }

    @Override
    public void onRefresh() {
        if (Validations.isInternetAvailable(activity, true)) {
            FilteredData = null;
            APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, "Shops", "", CityId);
        } else {
            if (lay_pull_refresh_shops_list.isRefreshing()) {
                lay_pull_refresh_shops_list.setRefreshing(false);
            }
        }
    }

    public void setCityDropDownData() {

        final ArrayAdapter StringModeldataAdapter = new ArrayAdapter(activity, R.layout.lay_spinner_item, listCities);
        aQuery.id(R.id.sp_city_name_shops_list).adapter(StringModeldataAdapter);

        aQuery.id(R.id.lay_city_drop_ic_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aQuery.id(R.id.sp_city_name_shops_list).getSpinner().performClick();
            }
        });
        aQuery.id(R.id.tv_location_name_shops_list).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aQuery.id(R.id.sp_city_name_shops_list).getSpinner().performClick();
            }
        });

        aQuery.id(R.id.sp_city_name_shops_list).itemSelected(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    CityName = aQuery.id(R.id.sp_city_name_shops_list).getSpinner().getSelectedItem().toString();
                    if (CityName.equals(getResources().getString(R.string.tv_city))) {
                        aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
//                        ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("City", "", mLatlngCurrent, "");
//                        if (mshopsListRecycleViewAdapter != null) {
//                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
//                            aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
//                           tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results)+" "+mshopsListRecycleViewAdapter.getItemCount() );
//
//
//                        }
                    } else {
                        CityId = listCitiesId.get(position).toString();
                        if (CityId != null && !CityId.equals("")) {
                            if (Validations.isInternetAvailable(activity, true)) {
                                loading.show();
                                APiGetShopslistData(AppConfig.APiPostShopsListDataByCity, "Shops", "", CityId);

                            }
                        } else {
                            APiGetShopslistData(AppConfig.APiGetCitiesList, "", "City", CityId);

                        }
                    }
//                        ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("City", "", mLatlngCurrent, CityName);
//                        if (mshopsListRecycleViewAdapter != null) {
//                            mshopsListRecycleViewAdapter.notifyDataSetChanged();
//                            aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
//                           tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results)+" "+mshopsListRecycleViewAdapter.getItemCount() );
//
//
//                        }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < listCities.size(); i++) {
            String name = listCities.get(i).toString().toLowerCase(locale);
            if (name.contains(mplaceName.toLowerCase(locale))) {
                aQuery.id(R.id.sp_city_name_shops_list).getSpinner().setSelection(i);
                found = true;
                break;
            }
        }

    }

    private void APiGetShopslistData(final String Url, final String Type, final String City, final String CityID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {
                            if (Type.equals("Shops") && City.equals("City") || City.equals("")) {
                                ShopsData = response.toString();
                                ShopsListDataResponse = response.toString();
//                                mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
//                                if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
//                                    mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, mShopsListModel.getShopsList(), mLatlngCurrent, btn_back_top_shops_list, isLocationAvail);
                                if (CityName != null && !CityName.equals("")) {
                                    aQuery.id(R.id.tv_location_name_shops_list).text(CityName);
                                } else {
                                    aQuery.id(R.id.tv_location_name_shops_list).text(mplaceName);

                                }
                                AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                                asyncTaskRunner.execute("List", "");
//                                    SetListData("List", mShopsListModel.getShopsList().size());
//                                    loading.close();
//                                } else {
//                                    if (lay_pull_refresh_shops_list.isRefreshing()) {
//                                        lay_pull_refresh_shops_list.setRefreshing(false);
//                                    }
//                                    loading.close();
//                                    showToast(getResources().getString(R.string.no_record_found));
//                                }
                                if (City.equals("City")) {
                                    APiGetShopslistData(AppConfig.APiGetCitiesList, "", "City", CityID);
                                }
                            } else if (Type.equals("") && City.equals("City")) {
                                if (mplaceName != null && !mplaceName.equals("")) {
                                    if (listCities != null) {
                                        listCities.clear();
                                    }
//                                    listCities.add(0, mplaceName);
                                } else {
                                    citiesNamesIDsResponse = response.toString();
                                    listCities.add(0, getResources().getString(R.string.tv_city));
                                    listCitiesId.add(0, "0");
                                }
                                JSONArray jsonArray = new JSONArray(citiesNamesIDsResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String ID = jsonObject.getString("ID");
                                    String name = jsonObject.getString("name");
                                    listCities.add(name);
                                    listCitiesId.add(ID);
                                }
                                setCityDropDownData();
                                loading.close();
                            }
//                                setSpinnerFilter();


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
                        error.printStackTrace();
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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        ArrayList<String> prams = new ArrayList<>();

        @Override
        protected String doInBackground(String... params) {
            if (ShopsListDataResponse != null && !ShopsListDataResponse.equals("")) {
                mShopsListModel = gson.fromJson(ShopsListDataResponse, ShopsListModel.class);
            } else {
                ShopsListDataResponse = "{\"shopsList\":[{}]}";
            }
            prams.clear();
            prams.add(0, params[0]);
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            if (mShopsListModel.getShopsList() != null && mShopsListModel.getShopsList().size() > 0) {
                if (!prams.get(0).equals("Filter")) {
                    mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, mShopsListModel.getShopsList(), mLatlngCurrent, btn_back_top_shops_list, isLocationAvail);
                }
                SetListData(prams.get(0), mShopsListModel.getShopsList().size());
                loading.close();
            } else {
                if (lay_pull_refresh_shops_list.isRefreshing()) {
                    lay_pull_refresh_shops_list.setRefreshing(false);
                }
                loading.close();
                showToast(getResources().getString(R.string.no_record_found));
            }
            // execution of result of Long time consuming operation

        }
    }

    //Setting Shops List Data
    @SuppressLint("SetTextI18n")
    private void SetListData(String Type, int Size) {
        try {
            tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + Integer.toString(Size));
            recyclerView.getItemAnimator().setChangeDuration(700);
            recyclerView.setAdapter(mshopsListRecycleViewAdapter);
            if (!Type.equals("Filter")) {
                if (isLocationAvail != null && isLocationAvail.equals("Yes")) {
                    mshopsListRecycleViewAdapter.SortFilterDistanceDefault();
                    mshopsListRecycleViewAdapter.notifyDataSetChanged();
                } else {
                    ShopsListRecycleViewAdapter.SortingShopsWithNameRatingCity("Name", "Ascending", mLatlngCurrent, "");
                    if (mshopsListRecycleViewAdapter != null) {
                        mshopsListRecycleViewAdapter.notifyDataSetChanged();
                        tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                    }
                }

            } else {
                if (mshopsListRecycleViewAdapter != null) {
                    tv_total_results_shops_list.setText(getResources().getString(R.string.tv_total_results) + " " + mshopsListRecycleViewAdapter.getItemCount());

                }
            }

            LinearLayoutManager mgridLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mgridLayoutManager);
            if (!Type.equals("Filter")) {
                if (lay_pull_refresh_shops_list.isRefreshing()) {
                    lay_pull_refresh_shops_list.setRefreshing(false);
                }
            }


            btn_back_top_shops_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerView.smoothScrollToPosition(0);
                }
            });
        } catch (Exception e) {
            if (!Type.equals("Filter")) {
                if (lay_pull_refresh_shops_list.isRefreshing()) {
                    lay_pull_refresh_shops_list.setRefreshing(false);
                }
            }
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                FilteredData = data;
                ArrayList<String> ShopsIds = data.getStringArrayListExtra("ShopslistAfterFiltration");
                String response = data.getStringExtra("response");

                provide_warranty = data.getStringExtra("provide_warranty");
                provide_ReplaceParts = data.getStringExtra("provide_ReplaceParts");
                topRated = data.getStringExtra("topRated");
                placeType = data.getStringExtra("placeType");
                brandType = data.getStringExtra("brandType");
                serviceType = data.getStringExtra("serviceType");
                FilterRecord = data.getStringExtra("FilterRecord");
                mShopsListModel = gson.fromJson(response.toString(), ShopsListModel.class);
                CheckedServices = data.getIntegerArrayListExtra("CheckedServices");
                CheckedBrands = data.getIntegerArrayListExtra("CheckedBrands");
                CheckedShopTypes = data.getIntegerArrayListExtra("CheckedShopTypes");


                List<ShopsListModel.ShopslistRecord> _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                List<ShopsListModel.ShopslistRecord> _ShopslistAfterFiltration = new ArrayList<ShopsListModel.ShopslistRecord>();
                if (_ShopslistBeforeFiltration != null) {

//                    if (_ShopslistBeforeFiltration.size() == 0) {
//                        _ShopslistBeforeFiltration.addAll(_ShopslistAfterFiltration);
//                    }
//                    _ShopslistAfterFiltration.clear();
                    for (int i = 0; i < ShopsIds.size(); i++) {
                        for (int j = 0; j < _ShopslistBeforeFiltration.size(); j++) {
                            if (ShopsIds.get(i).toString().equals(_ShopslistBeforeFiltration.get(j).getID().toString())) {
                                _ShopslistAfterFiltration.add(_ShopslistBeforeFiltration.get(j));
                                break;
                            } else {
//                                _ShopslistAfterFiltration.remove(i);
//                                break;
//                                _ShopslistBeforeFiltration.remove(i);
//                                break;
                            }
                        }
                    }
                    if (mshopsListRecycleViewAdapter != null) {
                        mshopsListRecycleViewAdapter = null;
//                mshopsListRecycleViewAdapter.notifyDataSetChanged();
                    }

                }
                mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, _ShopslistAfterFiltration, mLatlngCurrent, btn_back_top_shops_list, isLocationAvail);
                AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
                asyncTaskRunner.execute("Filter", String.valueOf(mShopsListModel.getShopsList().size()));
//                SetListData("Filter", _ShopslistAfterFiltration.size());

            }
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
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }


}
