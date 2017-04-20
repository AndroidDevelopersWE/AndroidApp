package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;

public class CareferPolicyActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carefer_policy);

        loading.show();
        APiCareferPolicyDataSaveUser(AppConfig.APiCareferPolicy, "Policy", "", "");
    }


    public void APiCareferPolicyDataSaveUse1r(String Url, final String Type, String customerMobile) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            if (Type.equals("Policy")) {
                                JSONArray policyData = response.getJSONArray("policyData");
                                JSONObject jsonObject = policyData.getJSONObject(0);
//                            Typeface tf = Farsi.GetFarsiFont(activity);
                                aQuery.id(R.id.tv_carefer_policy_details).text(jsonObject.getString("policyContent"));
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
                        showToast(getResources().getString(R.string.some_went_wrong));
                        loading.close();
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void APiCareferPolicyDataSaveUser(String URL, final String Type, final String customerMobile, final String isVerified) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (Type.equals("Policy")) {
                                JSONArray policyData = jsonObject.getJSONArray("policyData");
                                JSONObject jsonObject1 = policyData.getJSONObject(0);
//                            Typeface tf = Farsi.GetFarsiFont(activity);
                                aQuery.id(R.id.tv_carefer_policy_details).text(jsonObject1.getString("policyContent"));
                                loading.close();
                            } else {
                                JSONArray customerDetails = jsonObject.getJSONArray("customerDetails");
                                JSONObject jsonObject1 = customerDetails.getJSONObject(0);
                                String ID = jsonObject1.getString("ID");
                                Utils.savePreferences(activity,"User_ID",ID);
                                Utils.savePreferences(activity, "User_privacy_check", "verified");
                                Intent i = new Intent(activity, MainActivity.class);
                                startActivity(i);
                                loading.close();
                                showToast("User Registered...");
                                finish();
                            }


                        } catch (JSONException e) {
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
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
                if (!Type.equals("Policy")) {
                    params.put("customerName", "");
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

    public void btn_Next_to_mainmenu_Click(View v) {
        loading.show();
        APiCareferPolicyDataSaveUser(AppConfig.APiRegisterCustomer, "RegisterUser", sUser_Mobile, sUser_Mobile_Varify);
    }

}
