package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Models.OrderDetailModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class OrderDetailActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private TextView tv_title_order_detail;
    private AQuery aq;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initialise();
        SetUpLeftbar();
        SetShaderToViews();

        if (Validations.isInternetAvailable(activity, true)) {
            loading.show();
            ApiGetOrderDetails(getIntent().getExtras().getString("orderID"));
        }

    }


    private void initialise(){
        aq = new AQuery(this);
        tv_title_order_detail = (TextView) findViewById(R.id.tv_title_order_detail);

        aq.id(R.id.tv_order_no_heading).text(activity.getResources().getString(R.string.tv_order_no));
        aq.id(R.id.tv_order_type_heading).text(activity.getResources().getString(R.string.tv_order_type));
        aq.id(R.id.tv_order_date_heading).text(activity.getResources().getString(R.string.tv_date_order));
        aq.id(R.id.tv_comment_heading).text(activity.getResources().getString(R.string.tv_receive_comment));
        aq.id(R.id.tv_moved_shop_heading).text(activity.getResources().getString(R.string.moved_shop_price));
        aq.id(R.id.tv_model_name_heading).text(activity.getResources().getString(R.string.tv_model_name));
        aq.id(R.id.tv_brand_heading).text(activity.getResources().getString(R.string.tv_brand_name));
        aq.id(R.id.tv_service_type_heading).text(activity.getResources().getString(R.string.tv_service_type));
        aq.id(R.id.tv_type_heading).text(activity.getResources().getString(R.string.tv_type));//
        aq.id(R.id.tv_shop_heading).text(activity.getResources().getString(R.string.tv_shop));
        aq.id(R.id.tv_location_heading).text(activity.getResources().getString(R.string.tv_location));
        SetUpLeftbar();
        SetShaderToViews();

    }


    //set up left menu bar to show user menu
    private void SetUpLeftbar() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void SetShaderToViews() {
        Utils.gradientTextViewLong(tv_title_order_detail, activity);

    }

    //Opens the drawyer menu/leftbar
    @SuppressWarnings("UnusedParameters")
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
            // get menu from navigationView
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


    private void ApiGetOrderDetails(String orderID){

        String url = AppConfig.APIGetOrderDetails + orderID;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jObject) {

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        OrderDetailModel orderDetailModelList = gson.fromJson(String.valueOf(jObject), OrderDetailModel.class);

                        String df = orderDetailModelList.getOrderDetails().get(0).getOrderNo();
                        String dff = orderDetailModelList.getOrderDetails().get(0).getOrderType();
                        if(orderDetailModelList.getOrderDetails().get(0).getOrderNo().isEmpty()){
                            aq.id(R.id.tv_order_no).text("NA");
                        }else{
                            aq.id(R.id.tv_order_no).text(orderDetailModelList.getOrderDetails().get(0).getOrderNo());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getOrderType().isEmpty()){
                            aq.id(R.id.tv_order_type).text("NA");
                        }else{
                            aq.id(R.id.tv_order_type).text(orderDetailModelList.getOrderDetails().get(0).getOrderType());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getOrderDate().isEmpty()){
                            aq.id(R.id.tv_order_date).text("NA");
                        }else{
                            aq.id(R.id.tv_order_date).text(orderDetailModelList.getOrderDetails().get(0).getOrderDate());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getReceiveCarComments().isEmpty()){
                            aq.id(R.id.tv_comment).text("NA");
                        }else{
                            aq.id(R.id.tv_comment).text(orderDetailModelList.getOrderDetails().get(0).getReceiveCarComments());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getMovedShopPrice().isEmpty()){
                            aq.id(R.id.tv_moved_shop).text("NA");
                        }else{
                            aq.id(R.id.tv_moved_shop).text(orderDetailModelList.getOrderDetails().get(0).getMovedShopPrice());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getModelName().isEmpty()){
                            aq.id(R.id.tv_model_name).text("NA");
                        }else{
                            aq.id(R.id.tv_model_name).text(orderDetailModelList.getOrderDetails().get(0).getModelName());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getBrandName().isEmpty()){
                            aq.id(R.id.tv_brand).text("NA");
                        }else{
                            aq.id(R.id.tv_brand).text(orderDetailModelList.getOrderDetails().get(0).getBrandName());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getServiceTypeName().isEmpty()){
                            aq.id(R.id.tv_service_type).text("NA");
                        }else{
                            aq.id(R.id.tv_service_type).text(orderDetailModelList.getOrderDetails().get(0).getServiceTypeName());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getType().isEmpty()){
                            aq.id(R.id.tv_type).text("NA");
                        }else{
                            aq.id(R.id.tv_type).text(orderDetailModelList.getOrderDetails().get(0).getType());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getShopName().isEmpty()){
                            aq.id(R.id.tv_shop_name).text("NA");
                        }else{
                            aq.id(R.id.tv_shop_name).text(orderDetailModelList.getOrderDetails().get(0).getShopName());
                        }

                        if(orderDetailModelList.getOrderDetails().get(0).getCustomerLocation().isEmpty()){
                            aq.id(R.id.tv_location).text("NA");
                        }else{
                            aq.id(R.id.tv_location).text(orderDetailModelList.getOrderDetails().get(0).getCustomerLocation());
                        }


                        loading.close();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loading.close();
                    }
                });

        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonRequest.setRetryPolicy(policy);

        Volley.newRequestQueue(this).add(jsonRequest);

    }

}
