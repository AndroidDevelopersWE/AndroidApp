package co.dtechsystem.carefer.UI.Activities;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

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
import java.util.HashMap;
import java.util.List;

import co.dtechsystem.carefer.Adapters.ExpandableListAdapterBrands;
import co.dtechsystem.carefer.Adapters.ExpandableListAdapterPlaceType;
import co.dtechsystem.carefer.Adapters.ExpandableListAdapterServices;
import co.dtechsystem.carefer.Adapters.ShopsFilterClass;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;

public class FiltersActivity extends BaseActivity {
    ExpandableListView lv_service_type, lv_brands, lv_place_type;

    ExpandableListAdapterServices listAdapterServices;
    ExpandableListAdapterBrands listAdapterBrands;
    ExpandableListAdapterPlaceType listAdapterPlaceType;

    List<String> listDataHeaderService;
    List<String> listDataHeaderBrands;
    List<String> listDataHeaderPlaceType;

    HashMap<String, List<String>> listDataChildService;
    HashMap<String, List<String>> listDataChildBrands;
    HashMap<String, List<String>> listDataChildPlaceType;
    String ShopsDataResponse;

    public ShopsListModel mShopsListModel;

    SwitchCompat sw_provide_warranty_filter, sw_provide_replace_parts_filter, sw_top_rated_filter;
    public static List<ShopsListModel.ShopslistRecord> _ShopslistAfterFiltration;
    String provide_warranty="", provide_ReplaceParts="", topRated="";

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        SetDragAbleFilterViews();
        loading.show();
        APiGetShopslistData(AppConfig.APiServiceTypeData, "Services");

    }

    public void SetDragAbleFilterViews() {
        lv_service_type = (ExpandableListView) findViewById(R.id.lv_service_type);
        lv_brands = (ExpandableListView) findViewById(R.id.lv_brands);
        lv_place_type = (ExpandableListView) findViewById(R.id.lv_place_type);
        sw_provide_warranty_filter = (SwitchCompat) findViewById(R.id.sw_provide_warranty_filter);
        sw_provide_replace_parts_filter = (SwitchCompat) findViewById(R.id.sw_provide_replace_parts_filter);
        sw_top_rated_filter = (SwitchCompat) findViewById(R.id.sw_top_rated_filter);

        listDataHeaderService = new ArrayList<String>();
        listDataHeaderBrands = new ArrayList<String>();
        listDataHeaderPlaceType = new ArrayList<String>();

        listDataChildService = new HashMap<String, List<String>>();
        listDataChildBrands = new HashMap<String, List<String>>();
        listDataChildPlaceType = new HashMap<String, List<String>>();

        listDataHeaderService.add("Services");
        listDataHeaderBrands.add("Brands");
        listDataHeaderPlaceType.add("Place Type");
        ShopsDataResponse = intent.getStringExtra("ShopsDataResponse");

    }

    public void SetFiltersToViews() {

        sw_provide_warranty_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    provide_warranty = "1";
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistAfterFiltration, mShopsListModel.getShopsList(), provide_warranty, provide_ReplaceParts, topRated, "", "", "");
                    showToast("Record Found:" + _ShopslistAfterFiltration.size());
                } else {
                    provide_warranty = "";
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistAfterFiltration, mShopsListModel.getShopsList(), provide_warranty, provide_ReplaceParts, topRated, "", "", "");
                    showToast("Record Found:" + _ShopslistAfterFiltration.size());

                }
            }
        });

        sw_provide_replace_parts_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    provide_ReplaceParts = "1";
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            mShopsListModel.getShopsList(), mShopsListModel.getShopsList(), provide_warranty, provide_ReplaceParts, topRated, "", "", "");
                    showToast("Record Found:" + _ShopslistAfterFiltration.size());

                } else {
                    provide_ReplaceParts = "";
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            mShopsListModel.getShopsList(), mShopsListModel.getShopsList(), provide_warranty, provide_ReplaceParts, topRated, "", "", "");
                    showToast("Record Found:" + _ShopslistAfterFiltration.size());
                }
            }
        });

        sw_top_rated_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    topRated = "5";
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            mShopsListModel.getShopsList(), mShopsListModel.getShopsList(), provide_warranty, provide_ReplaceParts, topRated, "", "", "");
                    showToast("Record Found:" + _ShopslistAfterFiltration.size());

                } else {
                    topRated = "";
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            mShopsListModel.getShopsList(), mShopsListModel.getShopsList(), provide_warranty, provide_ReplaceParts, topRated, "", "", "");
                    showToast("Record Found:" + _ShopslistAfterFiltration.size());
                }
            }
        });

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
//                            listservices.add(0, "Service Type");

                            List brands = new ArrayList();
                            List placeType = new ArrayList();
                            //noinspection unchecked
//                            brands.add(0, "Brand");
                            if (Type.equals("Services")) {
                                JSONArray brandsData = response.getJSONArray("serviceTypeData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    listservices.add(jsonObject.getString("serviceTypeName"));

                                }
                                @SuppressWarnings("unchecked") ArrayAdapter StringdataAdapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, listservices);
//                                sp_service_type_shops_list.setAdapter(StringdataAdapter);
//                                APiGetShopslistData(AppConfig.APiBrandData, "Brands");
                                if (listDataHeaderService.size() > 0) {
                                    listDataChildService.put(listDataHeaderService.get(0), listservices);
                                    listAdapterServices = new ExpandableListAdapterServices(activity, listDataHeaderService, listDataChildService);
                                }

                                // setting list adapter
                                lv_service_type.setAdapter(listAdapterServices);
                                APiGetShopslistData(AppConfig.APiBrandData, "Brands");
                            } else if (Type.equals("Brands")) {
                                JSONArray brandsData = response.getJSONArray("brandsData");
                                for (int i = 0; i < brandsData.length(); i++) {
                                    JSONObject jsonObject = brandsData.getJSONObject(i);
                                    //noinspection unchecked
                                    brands.add(jsonObject.getString("brandName"));
                                }
                                if (listDataHeaderBrands.size() > 0) {
                                    listDataChildBrands.put(listDataHeaderBrands.get(0), brands);
                                    listAdapterBrands = new ExpandableListAdapterBrands(activity, listDataHeaderBrands, listDataChildBrands);
                                }
                                lv_brands.setAdapter(listAdapterBrands);
                                APiGetShopslistData(AppConfig.APiGetPlaceTypes, "PlaceTypes");
                            } else {
                                JSONArray placeTypeData = response.getJSONArray("placeType");
                                for (int i = 0; i < placeTypeData.length(); i++) {
                                    JSONObject jsonObject = placeTypeData.getJSONObject(i);
                                    //noinspection unchecked
                                    placeType.add(jsonObject.getString("name"));
                                }
                                if (listDataHeaderPlaceType.size() > 0) {
                                    listDataChildPlaceType.put(listDataHeaderPlaceType.get(0), placeType);
                                    listAdapterPlaceType = new ExpandableListAdapterPlaceType(activity, listDataHeaderPlaceType, listDataChildPlaceType);
                                }
                                lv_place_type.setAdapter(listAdapterPlaceType);
                                mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                                _ShopslistAfterFiltration = mShopsListModel.getShopsList();
                                SetFiltersToViews();
                                loading.close();
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
}
