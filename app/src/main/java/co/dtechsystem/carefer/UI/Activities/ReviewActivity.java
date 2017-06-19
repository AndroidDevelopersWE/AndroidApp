package co.dtechsystem.carefer.UI.Activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.Adapters.ReviewsRecycleViewAdapter;
import co.dtechsystem.carefer.Models.ReviewsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Validations;

public class ReviewActivity extends BaseActivity {
    private String mshopID;
    RecyclerView rv_shop_reviews;
    ReviewsRecycleViewAdapter mReviewsRecycleViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        rv_shop_reviews = (RecyclerView) findViewById(R.id.rv_shop_reviews);
        mshopID = intent.getStringExtra("ShopID");
        if (mshopID != null && !mshopID.equals("")) {
            if (Validations.isInternetAvailable(activity, true)) {
                loading.show();
                APiShopReviews(mshopID);
            }
        }
    }
//    public void CloseActivity(View v) {
//        finish();
//    }
    private void SetListData() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_shop_reviews);
        recyclerView.getItemAnimator().setChangeDuration(700);
        recyclerView.setAdapter(mReviewsRecycleViewAdapter);
        GridLayoutManager mgridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mgridLayoutManager);
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
                                SetListData();
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
        queue.add(postRequest);
    }
}
