package co.dtechsystem.carefer.UI.Activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Adapters.ReviewsRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ReviewsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class ReviewActivity extends BaseActivity {
    private String mshopID;
    RecyclerView rv_shop_reviews;
    ReviewsRecycleViewAdapter mReviewsRecycleViewAdapter;
    TextView tv_title_review;
    String shopRatings = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        rv_shop_reviews = (RecyclerView) findViewById(R.id.rv_shop_reviews);
        tv_title_review = (TextView) findViewById(R.id.tv_title_review);
        mshopID = intent.getStringExtra("ShopID");
        shopRatings = intent.getStringExtra("shopRatings");
        if (mshopID != null && !mshopID.equals("")) {
            if (Validations.isInternetAvailable(activity, true)) {
                loading.show();
                APiShopReviews(mshopID);
            }
        }
        SetShaderToViews();
    }

    private void SetShaderToViews() {
        Utils.gradientTextView(tv_title_review, activity);
    }

    //    public void CloseActivity(View v) {
//        finish();
//    }
    private void SetListData(String priceAVG, String qualityAVG, String timeAVG) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_shop_reviews);
        recyclerView.getItemAnimator().setChangeDuration(700);
        recyclerView.setAdapter(mReviewsRecycleViewAdapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mgridLayoutManager);
        float priceRate = 0, qualityRate = 0, timeRate = 0;

        if (priceAVG != null && !priceAVG.equals("")) {
            priceRate = Float.parseFloat(priceAVG);
            aQuery.find(R.id.rb_price_rate).getRatingBar().setRating(priceRate);

        } else {
            aQuery.find(R.id.rb_price_rate).getRatingBar().setRating(0);
        }
        if (qualityAVG != null && !qualityAVG.equals("")) {
            qualityRate = Float.parseFloat(qualityAVG);
            aQuery.find(R.id.rb_quality_rate).getRatingBar().setRating(qualityRate);

        } else {
            aQuery.find(R.id.rb_quality_rate).getRatingBar().setRating(0);
        }
        if (timeAVG != null && !timeAVG.equals("")) {
            timeRate = Float.parseFloat(timeAVG);
            aQuery.find(R.id.rb_time_rate).getRatingBar().setRating(timeRate);

        } else {
            aQuery.find(R.id.rb_time_rate).getRatingBar().setRating(0);
        }
        if (shopRatings != null && !shopRatings.equals("") && Float.parseFloat(shopRatings) > 0) {
            float totalRatingShop = Float.parseFloat(shopRatings);
            String avgRate = String.format("%.01f", totalRatingShop);
            aQuery.id(R.id.tv_avg_rating).text(avgRate);
            if (totalRatingShop > 4.4) {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_excelent));

            } else if (totalRatingShop > 3.4) {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_good));


            } else if (totalRatingShop > 2.4) {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_average));

            } else {
                aQuery.id(R.id.tv_rating_type).text(getResources().getString(R.string.tv_lower));

            }


        }
    }

    private void APiShopReviews(final String shopID) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiGetShopReviews,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            ReviewsModel mReviewsModel = gson.fromJson(response, ReviewsModel.class);
                            if (mReviewsModel.getShopReviews() != null && mReviewsModel.getShopReviews().size() > 0) {
                                mReviewsRecycleViewAdapter = new ReviewsRecycleViewAdapter(activity, mReviewsModel.getShopReviews());
                                JSONObject mainObj = new JSONObject(response);
                                JSONArray AVGRatings = mainObj.getJSONArray("AVGRatings");
                                JSONObject jsonObject = AVGRatings.getJSONObject(0);
                                String priceAVG = jsonObject.getString("priceAVG");
                                String qualityAVG = jsonObject.getString("qualityAVG");
                                String timeAVG = jsonObject.getString("priceAVG");
                                SetListData(priceAVG, qualityAVG, timeAVG);
                                loading.close();
                            } else {
                                loading.close();
                                finish();
                                showToast(getResources().getString(R.string.no_record_found));

                            }
                        } catch (Exception c) {
                            loading.close();
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            c.printStackTrace();
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
                params.put("shopID", shopID);


                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RetryPolicy policy = new DefaultRetryPolicy(AppConfig.socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        queue.add(postRequest);
    }
}
