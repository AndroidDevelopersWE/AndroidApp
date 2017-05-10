package co.dtechsystem.carefer.UI.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;

public class MyDetailsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout mDrawerLayout;
    String mcustomerName, mcustomerMobile;

    TextView tv_title_my_details, tv_mobile_number_my_details, tv_name_my_details, tv_car_brand_my_details,
            tv_car_model_my_details, tv_last_oil_my_details;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);
        initializeViews();
        SetUpLeftbar();
        loading.show();
        APiMyDetails(AppConfig.APiGetCustomerDetails + sUser_ID, "getUserDetails", "", "", "");
    }

    public void initializeViews() {
        tv_title_my_details = (TextView) findViewById(R.id.tv_title_my_details);
        tv_mobile_number_my_details = (TextView) findViewById(R.id.tv_mobile_number_my_details);
        tv_name_my_details = (TextView) findViewById(R.id.tv_name_my_details);
        tv_car_brand_my_details = (TextView) findViewById(R.id.tv_car_brand_my_details);
        tv_car_model_my_details = (TextView) findViewById(R.id.tv_car_model_my_details);
        tv_last_oil_my_details = (TextView) findViewById(R.id.tv_last_oil_my_details);
        SetShaderToViews();

    }

    public void SetData() {
        String et_car_brand_my_details = Utils.readPreferences(activity, "CustomerCarBrand", "");
        String et_car_model_my_details = Utils.readPreferences(activity, "CustomerCarModel", "");
        String et_last_oil_my_details = Utils.readPreferences(activity, "CustomerCarOilChange", "");
        if (!et_car_brand_my_details.equals("")) {
            aQuery.find(R.id.et_car_brand_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);

        }
        if (!et_car_model_my_details.equals("")) {
            aQuery.find(R.id.et_car_model_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);

        }
        if (!et_last_oil_my_details.equals("")) {
            aQuery.find(R.id.et_last_oil_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);

        }
        if (!mcustomerName.equals("")) {
            aQuery.find(R.id.et_user_name_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);

        }
        aQuery.find(R.id.et_car_brand_my_details).text(et_car_brand_my_details);
        aQuery.find(R.id.et_car_model_my_details).text(et_car_model_my_details);
        aQuery.find(R.id.et_last_oil_my_details).text(et_last_oil_my_details);
        aQuery.find(R.id.et_mobile_my_details).getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_mobile_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_edit_hover), null);
                } else {
                    aQuery.find(R.id.et_mobile_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_edit), null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        aQuery.find(R.id.et_user_name_my_details).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_user_name_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_user_name_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_car_brand_my_details).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_car_brand_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_car_brand_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_car_model_my_details).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_car_model_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_car_model_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_last_oil_my_details).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    aQuery.find(R.id.et_last_oil_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit_hover), null, null, null);
                } else {
                    aQuery.find(R.id.et_last_oil_my_details).getEditText().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_edit), null, null, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aQuery.find(R.id.et_last_oil_my_details).getEditText().setInputType(InputType.TYPE_NULL);

        aQuery.find(R.id.et_last_oil_my_details).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(activity);
                ShowDatePicker();
            }
        });


    }


    public void ShowDatePicker() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, locale);

                aQuery.find(R.id.et_last_oil_my_details).text(sdf.format(myCalendar.getTime()));
            }

        };
        new DatePickerDialog(activity, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_my_details, activity);
        Utils.gradientTextViewShort(tv_mobile_number_my_details, activity);
        Utils.gradientTextViewShort(tv_name_my_details, activity);
        Utils.gradientTextViewShort(tv_car_brand_my_details, activity);
        Utils.gradientTextViewShort(tv_car_model_my_details, activity);
        Utils.gradientTextViewShort(tv_last_oil_my_details, activity);
    }

    public void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void submitUserData(View v) {

        String customerName = aQuery.find(R.id.et_user_name_my_details).getText().toString();
        String customerMobile = aQuery.find(R.id.et_mobile_my_details).getText().toString();

        String et_car_brand_my_details = aQuery.find(R.id.et_car_brand_my_details).getText().toString();
        String et_car_model_my_details = aQuery.find(R.id.et_car_model_my_details).getText().toString();
        String et_last_oil_my_details = aQuery.find(R.id.et_last_oil_my_details).getText().toString();
        if (customerName.equals("") || customerMobile.equals("") || et_car_brand_my_details.equals("") ||
                et_car_model_my_details.equals("") || et_last_oil_my_details.equals("")) {
            showToast("Please enter values");
        } else {
            Utils.savePreferences(activity, "CustomerCarBrand", et_car_brand_my_details);
            Utils.savePreferences(activity, "CustomerCarModel", et_car_model_my_details);
            Utils.savePreferences(activity, "CustomerCarOilChange", et_last_oil_my_details);
            loading.show();
            APiMyDetails(AppConfig.APisetCustomerDetails + sUser_ID, "setUserDetails", customerName, customerMobile, sUser_Mobile_Varify);
        }
    }

    public void APiMyDetails(String URL, final String Type, final String customerName, final String customerMobile, final String isVerified) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            if (Type.equals("getUserDetails")) {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray customerDetail = jsonObject.getJSONArray("customerDetail");
                                JSONObject jsonObject1 = customerDetail.getJSONObject(0);
                                mcustomerName = jsonObject1.getString("customerName");
                                mcustomerMobile = jsonObject1.getString("customerMobile");
                                aQuery.find(R.id.et_user_name_my_details).text(mcustomerName);
                                aQuery.find(R.id.et_mobile_my_details).text(mcustomerMobile);
                                SetData();
                                loading.close();
                            } else {
                                showToast("Updated your record...");
                                finish();
                            }


                        } catch (JSONException e) {

                            loading.close();
                            e.printStackTrace();
                        }
                        loading.close();
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
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!Type.equals("getUserDetails")) {
                    params.put("customerName", customerName);
                    params.put("customerMobile", customerMobile);
                    params.put("isVerified", isVerified);
                }


                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_details) {
//            Intent i = new Intent(this, MyDetailsActivity.class);
//            startActivity(i);
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
