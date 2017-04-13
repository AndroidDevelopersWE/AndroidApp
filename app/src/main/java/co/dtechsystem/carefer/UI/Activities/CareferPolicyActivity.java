package co.dtechsystem.carefer.UI.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Farsi;

public class CareferPolicyActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carefer_policy);

        loading.show();
        APiGetCareferPolicyData();
    }


    public void APiGetCareferPolicyData() {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.APiCareferPolicy, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            JSONArray policyData = response.getJSONArray("policyData");
                            JSONObject jsonObject = policyData.getJSONObject(0);
                            Typeface tf = Farsi.GetFarsiFont(activity);
//                            TextView textView = (TextView) findViewById(R.id.tv_carefer_policy_details);
                            aQuery.id(R.id.tv_carefer_policy_details).text(jsonObject.getString("policyContent"));
//                            textView.setTypeface(tf);
//                            textView.setText(jsonObject.getString("policyContent"));
                            Log.d("Response", response.toString());
                            loading.close();
                        } catch (JSONException e) {
                            loading.close();
                            showToast("Something Went Wrong Parsing.");
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast("Something Went Wrong...");
                        Log.d("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    public void btn_Next_to_mainmenu_Click(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
