package co.dtechsystem.carefer.UI.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.Filter.ShopsFilterClass;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

@SuppressWarnings("unchecked")
public class FiltersActivity extends BaseActivity {
    private ExpandableListView lv_service_type;
    private ExpandableListView lv_brands;
    private ExpandableListView lv_place_type;

    public static String ShopsDataResponse = "";

    private ShopsListModel mShopsListModel;

    private SwitchCompat sw_provide_warranty_filter;
    private SwitchCompat sw_provide_replace_parts_filter;
    private SwitchCompat sw_top_rated_filter;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistBeforeFiltration;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistAfterFiltration;
    private String provide_warranty = "";
    private String provide_ReplaceParts = "";
    private String topRated = "";
    private String placeType = "";
    private String brandType = "";
    private String serviceType = "";
    private ArrayAdapter<String> arrayAdapterPlaceType;
    private ArrayAdapter<String> arrayAdapterBrands;
    private ArrayAdapter<String> arrayAdapterServices;
    private int TotalRecord;
    private int FilterRecord;
    private LatLng mLatlngCurrent;
    private ArrayList<Integer> CheckedServices = new ArrayList<>();
    private ArrayList<Integer> CheckedBrands = new ArrayList<>();
    private ArrayList<Integer> CheckedShopTypes = new ArrayList<>();
    private TextView tv_title_filter;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        tv_title_filter = (TextView) findViewById(R.id.tv_title_filter);
        SetDragAbleFilterViews();
        SetShaderToViews();
        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            APiGetShopslistData(AppConfig.APiGetFilterTypes);
        }

    }
    /**
     * Handle shades of text view title of activity in multicolor
     */
    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_filter, activity);
    }

    /**
     * Handle all type of intents like receiving strings and bundles from previous activity
     */
    private void GetIntents() {
        if (intent.getStringExtra("provide_warranty") != null && intent.getStringExtra("provide_ReplaceParts") != null &&
                intent.getStringExtra("topRated") != null && intent.getStringExtra("placeType") != null && intent.getStringExtra("brandType") != null && intent.getStringExtra("serviceType") != null && intent.getStringExtra("FilterRecord") != null &&
                intent.getIntegerArrayListExtra("CheckedServices") != null && intent.getIntegerArrayListExtra("CheckedBrands") != null &&
                intent.getIntegerArrayListExtra("CheckedShopTypes") != null) {
            provide_warranty = intent.getStringExtra("provide_warranty");
            provide_ReplaceParts = intent.getStringExtra("provide_ReplaceParts");
            topRated = intent.getStringExtra("topRated");
            placeType = intent.getStringExtra("placeType");
            brandType = intent.getStringExtra("brandType");
            serviceType = intent.getStringExtra("serviceType");
            CheckedServices = intent.getIntegerArrayListExtra("CheckedServices");
            CheckedBrands = intent.getIntegerArrayListExtra("CheckedBrands");
            CheckedShopTypes = intent.getIntegerArrayListExtra("CheckedShopTypes");
            FilterRecord = Integer.parseInt(intent.getStringExtra("FilterRecord"));
            if (provide_warranty.equals("1")) {
                sw_provide_warranty_filter.setChecked(true);
            }
            if (provide_ReplaceParts.equals("1")) {
                sw_provide_replace_parts_filter.setChecked(true);
            }
            if (topRated.equals("5.00")) {
                sw_top_rated_filter.setChecked(true);
            }

            if (!placeType.equals("")) {
                aQuery.find(R.id.tv_place_type_filter).text(placeType);


            }
            if (!brandType.equals("")) {
                aQuery.find(R.id.tv_brand_type_filter).text(brandType);
            }
            if (!serviceType.equals("")) {
                aQuery.find(R.id.tv_service_type_filter).text(serviceType);
            }
            if (FilterRecord < 20) {
                TotalRecord = _ShopslistBeforeFiltration.size();
                aQuery.find(R.id.tv_total_record_found_filter).text(FilterRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
            } else {
                TotalRecord = _ShopslistBeforeFiltration.size();
                aQuery.find(R.id.tv_total_record_found_filter).text(FilterRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
            }
        } else {
            if (_ShopslistBeforeFiltration.size() > 0) {
                TotalRecord = _ShopslistBeforeFiltration.size();
                FilterRecord = TotalRecord;
                aQuery.find(R.id.tv_total_record_found_filter).text(TotalRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                if (FilterRecord < 20) {
                    aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                    aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                } else {
                    aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                    aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                }
            }
        }
    }

    /**
     * Handle list view for filter like brands,service and shop type and initializes their views
     */
    private void SetDragAbleFilterViews() {
        lv_service_type = (ExpandableListView) findViewById(R.id.lv_service_type);
        lv_brands = (ExpandableListView) findViewById(R.id.lv_brands);
        lv_place_type = (ExpandableListView) findViewById(R.id.lv_place_type);
        sw_provide_warranty_filter = (SwitchCompat) findViewById(R.id.sw_provide_warranty_filter);
        sw_provide_replace_parts_filter = (SwitchCompat) findViewById(R.id.sw_provide_replace_parts_filter);
        sw_top_rated_filter = (SwitchCompat) findViewById(R.id.sw_top_rated_filter);

//        ShopsDataResponse = intent.getStringExtra("ShopsDataResponse");
        Bundle bundle = intent.getParcelableExtra("bundle");
        if (bundle != null) {
            mLatlngCurrent = bundle.getParcelable("LatLngCurrent");
        }
        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();

        GetIntents();
    }

    /**
     * Handle first call and recall after setting filter and coming back to filter activity
     */
    private void SetFiltersToViews() {

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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                }
            }
        });

        sw_top_rated_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    topRated = "5.00";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
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

    /**
     * Web APi for fetch all shops data
     * @param Url Takes string as param to fetch all shops data
     */
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
//                            listservices.add(0, getResources().getString(R.string.dp_service_type));
                            List brands = new ArrayList();
                            List placeType = new ArrayList();
                            //noinspection unchecked
//                            brands.add(0, getResources().getString(R.string.dp_brand));
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
                            arrayAdapterServices = new ArrayAdapter<>(activity,
                                    android.R.layout.simple_list_item_multiple_choice, listservices);
                            arrayAdapterBrands = new ArrayAdapter<>(activity,
                                    android.R.layout.simple_list_item_multiple_choice, brands);
                            arrayAdapterPlaceType = new ArrayAdapter<>(activity,
                                    android.R.layout.simple_list_item_multiple_choice, placeType);
                            SetFiltersToViews();

                            mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                            _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
//                            if (_ShopslistBeforeFiltration.size() > 0) {
//                                TotalRecord = _ShopslistBeforeFiltration.size();
//                                FilterRecord = TotalRecord;
//                                aQuery.find(R.id.tv_total_record_found_filter).text(TotalRecord + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
//                            }
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

        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        getRequest.setRetryPolicy(policy);
        // add it to the RequestQueue

        queue.add(getRequest);
    }

    /**
     * Show user a dialog of place types list where use choose for types
     */

    private void ShowFilterPlaceTypeListViewDialog() {
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
        for (int i = 0; i < CheckedShopTypes.size(); i++) {

            if (CheckedShopTypes != null && CheckedShopTypes.size() > 0) {
                lv_filter_list.setItemChecked(CheckedShopTypes.get(i), true);
            }
        }
        btn_ok_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = lv_filter_list.getCheckedItemPosition();
                if (id > 0) {
                    CheckedShopTypes.add(id);
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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                    dialog.hide();
                } else {
                    if (id != -1) {
                        CheckedShopTypes.add(id);
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
                                _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                        aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                        FilterRecord = _ShopslistAfterFiltration.size();
                        if (FilterRecord < 20) {
                            aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                            aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                        } else {
                            aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                            aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                        }
                        dialog.hide();
                    }
                }
            }
        });
        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        dialog.show();
    }
    /**
     * Show user a dialog of brands list where use choose for types
     */
    private void ShowFilterBrandsListViewDialog() {
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
        for (int i = 0; i < CheckedBrands.size(); i++) {
            if (CheckedBrands != null && CheckedBrands.size() > 0) {
                lv_filter_list.setItemChecked(CheckedBrands.get(i), true);
            }
        }
        btn_ok_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = lv_filter_list.getCount();
                SparseBooleanArray checked = lv_filter_list.getCheckedItemPositions();
                ArrayList<String> brands = new ArrayList<>();
                CheckedBrands.clear();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {

                        long id = lv_filter_list.getItemIdAtPosition(i);
//                        if (id != 0) {
                        CheckedBrands.add((int) id);
                        brands.add(lv_filter_list.getItemAtPosition(i).toString());
//                        }
                    }

                }
                String Brands = brands.toString();
                if (Brands.startsWith("[") && Brands.endsWith("]")) {
                    Brands = Brands.replace("[", "");
                    Brands = Brands.replace("]", "");
                }
                if (brands.size() > 0) {
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
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                    dialog.hide();
                } else {
                    CheckedBrands.clear();
                    aQuery.find(R.id.tv_brand_type_filter).text(getResources().getString(R.string.dp_brand));

                    brandType = "";
                    if (_ShopslistAfterFiltration != null) {
                        _ShopslistAfterFiltration.clear();
                    }
                    if (_ShopslistBeforeFiltration.size() == 0) {
                        mShopsListModel = gson.fromJson(ShopsDataResponse.toString(), ShopsListModel.class);
                        _ShopslistBeforeFiltration = mShopsListModel.getShopsList();
                    }
                    _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                            _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                    dialog.hide();
                }
            }
        });
        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        dialog.show();
    }
    /**
     * Show user a dialog of services list where use choose for types
     */
    private void ShowFilterServicesListViewDialog() {
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
        for (int i = 0; i < CheckedServices.size(); i++) {
            if (CheckedServices != null && CheckedServices.size() > 0) {
                lv_filter_list.setItemChecked(CheckedServices.get(i), true);
            }
        }

        btn_ok_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = lv_filter_list.getCount();
                CheckedServices.clear();
                ArrayList<String> services = new ArrayList<>();
                SparseBooleanArray checked = lv_filter_list.getCheckedItemPositions();
                for (int i = 0; i < len; i++) {
                    if (checked.get(i)) {
                        long id = lv_filter_list.getItemIdAtPosition(i);
//                        if (id != 0) {
                        CheckedServices.add((int) id);
                        services.add(lv_filter_list.getItemAtPosition(i).toString());
//                        }
                    }
                }
                String Services = services.toString();
                if (Services.startsWith("[") && Services.endsWith("]")) {
                    Services = Services.replace("[", "");
                    Services = Services.replace("]", "");
                }
                if (services.size() > 0) {
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
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }
                    dialog.hide();
                } else {
//                    services.clear();
                    CheckedServices.clear();
                    aQuery.find(R.id.tv_service_type_filter).text(getResources().getString(R.string.dp_service_type));
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
                    aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
                    FilterRecord = _ShopslistAfterFiltration.size();
                    if (FilterRecord < 20) {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
                    } else {
                        aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
                        aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
                    }

                    dialog.hide();
                }
            }
        });
        btn_cancel_dialog_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        dialog.show();
    }

    /**
     * Handle button click when user click on apply filter
     * @param v
     */
    public void setApplyFilterBtnClick(View v) {
        ShopsListActivity shopsListActivity = new ShopsListActivity();
        if (_ShopslistAfterFiltration != null && _ShopslistAfterFiltration.size() > 0) {
            ArrayList<String> ShopsIds = new ArrayList<>();
            for (int i = 0; i < _ShopslistAfterFiltration.size(); i++) {
                ShopsIds.add(_ShopslistAfterFiltration.get(i).getID());
            }

//            JSONArray jsonObject = new JSONArray(_ShopslistAfterFiltration);

//            ShopsListRecycleViewAdapter mshopsListRecycleViewAdapter = new ShopsListRecycleViewAdapter(activity, _ShopslistBeforeFiltration, mLatlngCurrent);
//            shopsListActivity.SetFilters(mshopsListRecycleViewAdapter);
//            finish();
            Intent intent = new Intent();
            intent.putStringArrayListExtra("ShopslistAfterFiltration", ShopsIds);
            ShopsListActivity.ShopsListDataResponse = ShopsDataResponse;
//            intent.putExtra("response", ShopsDataResponse);
            intent.putExtra("provide_warranty", provide_warranty);
            intent.putExtra("provide_ReplaceParts", provide_ReplaceParts);
            intent.putExtra("topRated", topRated);
            intent.putExtra("placeType", placeType);
            intent.putExtra("brandType", brandType);
            intent.putExtra("serviceType", serviceType);
            intent.putExtra("FilterRecord", String.valueOf(FilterRecord));
            intent.putIntegerArrayListExtra("CheckedBrands", CheckedBrands);
            intent.putIntegerArrayListExtra("CheckedShopTypes", CheckedShopTypes);
            intent.putIntegerArrayListExtra("CheckedServices", CheckedServices);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            showToast(getResources().getString(R.string.no_record_found));
//            finish();
        }

    }
    /**
     * Handle button click when user click on reset filter
     * @param v
     */
    public void setResetFilterBtnClick(View v) {
        provide_warranty = "";
        provide_ReplaceParts = "";
        topRated = "";
        placeType = "";
        brandType = "";
        serviceType = "";
        _ShopslistAfterFiltration = ShopsFilterClass.filterShopsWithProviders(activity,
                _ShopslistBeforeFiltration, _ShopslistBeforeFiltration, provide_warranty, provide_ReplaceParts, topRated, placeType, brandType, serviceType);
        aQuery.find(R.id.tv_total_record_found_filter).text(_ShopslistAfterFiltration.size() + getResources().getString(R.string.toast_record_found_out_of) + TotalRecord);
        FilterRecord = _ShopslistAfterFiltration.size();
        if (FilterRecord < 20) {
            aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorFroly));
            aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_expand_your_choices));
        } else {
            aQuery.find(R.id.lay_filter_rocords_found).backgroundColor(ContextCompat.getColor(activity, R.color.colorNarvik));
            aQuery.find(R.id.tv_choice_type_filter).text(getResources().getString(R.string.tv_good_choice_see_shops));
        }
        sw_provide_warranty_filter.setChecked(false);
        sw_provide_replace_parts_filter.setChecked(false);
        sw_top_rated_filter.setChecked(false);
        aQuery.find(R.id.tv_place_type_filter).text(getResources().getString(R.string.spinner_place_type));
        aQuery.find(R.id.tv_brand_type_filter).text(getResources().getString(R.string.dp_brand));
        aQuery.find(R.id.tv_service_type_filter).text(getResources().getString(R.string.dp_service_type));
    }
}
