package co.dtechsystem.carefer.UI.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
    public static List<ShopsListModel.ShopslistRecord> _ShopslistBeforeFiltration;
    public static List<ShopsListModel.ShopslistRecord> _ShopslistAfterFiltration;
    String provide_warranty = "", provide_ReplaceParts = "", topRated = "", placeType = "", brandType = "", serviceType = "";
    ArrayAdapter<String> arrayAdapterPlaceType;
    ArrayAdapter<String> arrayAdapterBrands;
    ArrayAdapter<String> arrayAdapterServices;
    int TotalRecord;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        SetDragAbleFilterViews();
        loading.show();
        APiGetShopslistData(AppConfig.APiGetFilterTypes);

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
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                } else {
                    provide_warranty = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);

                }
            }
        });

        sw_provide_replace_parts_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    provide_ReplaceParts = "1";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);

                } else {
                    provide_ReplaceParts = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                }
            }
        });

        sw_top_rated_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    topRated = "5";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);

                } else {
                    topRated = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                }
            }
        });
        lv_place_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position != 0) {
//                    TextView textView = (TextView) view.findViewById(R.id.lblListItem);
                placeType = parent.getItemAtPosition(position).toString();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        aQuery.find(R.id.tv_place_type_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilterPlaceTypeListViewDialog();
            }
        });
        aQuery.find(R.id.tv_brand_type_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilterBrandsListViewDialog();
            }
        });

        aQuery.find(R.id.tv_service_type_filter).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilterServicesListViewDialog();
            }
        });


    }

    private void APiGetShopslistData(final String Url) {
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
                            listservices.add(0, getResources().getString(R.string.dp_service_type));
                            List brands = new ArrayList();
                            List placeType = new ArrayList();
                            //noinspection unchecked
                            brands.add(0, getResources().getString(R.string.dp_brand));
                            JSONArray serviceTypeData = response.getJSONArray("serviceTypeData");
                            for (int i = 0; i < serviceTypeData.length(); i++) {
                                JSONObject jsonObject = serviceTypeData.getJSONObject(i);
                                //noinspection unchecked
                                listservices.add(jsonObject.getString("serviceTypeName"));
                            }
//
                            JSONArray brandsData = response.getJSONArray("brandsData");
                            for (int i = 0; i < brandsData.length(); i++) {
                                JSONObject jsonObject = brandsData.getJSONObject(i);
                                //noinspection unchecked
                                brands.add(jsonObject.getString("brandName"));
                            }

                            JSONArray placeTypeData = response.getJSONArray("placeType");
                            placeType.add(0, getResources().getString(R.string.spinner_place_type));
                            for (int i = 0; i < placeTypeData.length(); i++) {
                                JSONObject jsonObject = placeTypeData.getJSONObject(i);
                                //noinspection unchecked
                                placeType.add(jsonObject.getString("name"));
                            }
                            arrayAdapterServices = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_list_item_multiple_choice, listservices);
                            arrayAdapterBrands = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_list_item_multiple_choice, brands);
                            arrayAdapterPlaceType = new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_list_item_multiple_choice, placeType);


                            mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                            _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                            TotalRecord = mShopsListModel.getShopsList().size();
                            aQuery.find(R.id.tv_total_record_found_filter).text(TotalRecord + " Record Found Out Of " + TotalRecord);

                            SetFiltersToViews();
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
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

        getRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                error.printStackTrace();

            }
        });
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    public void ShowFilterPlaceTypeListViewDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_list_filter);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
        Button btn_ok_dialog_filter = (Button) dialog.findViewById(R.id.btn_ok_dialog_filter);
        Button btn_cancel_dialog_filter = (Button) dialog.findViewById(R.id.btn_cancel_dialog_filter);
        final ListView lv_filter_list = (ListView) dialog.findViewById(R.id.lv_filter_list);
        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv_filter_list.setAdapter(arrayAdapterPlaceType);
        btn_ok_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = lv_filter_list.getCheckedItemPosition();
                if (id > 0) {
                    aQuery.find(R.id.tv_place_type_filter).text(lv_filter_list.getItemAtPosition(id).toString());
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }

                    placeType = lv_filter_list.getItemAtPosition(id).toString();
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                    dialog.dismiss();
                } else {
                    aQuery.find(R.id.tv_place_type_filter).text(lv_filter_list.getItemAtPosition(id).toString());

                    placeType = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                    dialog.dismiss();
                }
            }
        });
        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void ShowFilterBrandsListViewDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_list_filter);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
        Button btn_ok_dialog_filter = (Button) dialog.findViewById(R.id.btn_ok_dialog_filter);
        Button btn_cancel_dialog_filter = (Button) dialog.findViewById(R.id.btn_cancel_dialog_filter);
        final ListView lv_filter_list = (ListView) dialog.findViewById(R.id.lv_filter_list);
        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv_filter_list.setAdapter(arrayAdapterBrands);
        btn_ok_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = lv_filter_list.getCount();
                SparseBooleanArray checked = lv_filter_list.getCheckedItemPositions();
                ArrayList<String> brands = new ArrayList<String>();

                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        brands.add(lv_filter_list.getItemAtPosition(i).toString());

                    }
                }
                String Brands = brands.toString();
                if (Brands.startsWith("[") && Brands.endsWith("]")) {
                    Brands = Brands.replace("[", "");
                    Brands = Brands.replace("]", "");
                }
                if (len > 0) {
                    aQuery.find(R.id.tv_brand_type_filter).text(Brands);
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }


                    brandType = Brands;
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                    dialog.dismiss();
                } else {
                    aQuery.find(R.id.tv_brand_type_filter).text(Brands);

                    brandType = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, "");
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                    dialog.dismiss();
                }
            }
        });
        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void ShowFilterServicesListViewDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_list_filter);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(true);
        // set the custom dialog components - text, image and button
        Button btn_ok_dialog_filter = (Button) dialog.findViewById(R.id.btn_ok_dialog_filter);
        Button btn_cancel_dialog_filter = (Button) dialog.findViewById(R.id.btn_cancel_dialog_filter);
        final ListView lv_filter_list = (ListView) dialog.findViewById(R.id.lv_filter_list);
        lv_filter_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv_filter_list.setAdapter(arrayAdapterServices);
        btn_ok_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = lv_filter_list.getCount();
                SparseBooleanArray checked = lv_filter_list.getCheckedItemPositions();
                ArrayList<String> brands = new ArrayList<String>();

                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        brands.add(lv_filter_list.getItemAtPosition(i).toString());

                    }
                }
                String Services = brands.toString();
                if (Services.startsWith("[") && Services.endsWith("]")) {
                    Services = Services.replace("[", "");
                    Services = Services.replace("]", "");
                }
                if (len > 0) {
                    aQuery.find(R.id.tv_service_type_filter).text(Services);
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }


                    serviceType = Services;
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                    dialog.dismiss();
                } else {
                    aQuery.find(R.id.tv_service_type_filter).text(Services);

                    serviceType = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);

                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + " Record Found Out Of " + TotalRecord);
                    dialog.dismiss();
                }
            }
        });
        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}