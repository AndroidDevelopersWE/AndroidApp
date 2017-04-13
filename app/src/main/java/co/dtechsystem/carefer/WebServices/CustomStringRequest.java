package co.dtechsystem.carefer.WebServices;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CustomStringRequest extends StringRequest {

	private int mStatusCode;
	private ErrorListener mErrorListener;
	Map<String, String> Params;
	public CustomStringRequest(int method, String url,
							   Listener<String> listener, ErrorListener errorListener, Map<String, String> params) {
		super(method, url, listener, errorListener);
		this.Params = params;
		mErrorListener = errorListener;
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return Params;
	}
	
	@Override
	public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
		// TODO Auto-generated method stub
		retryPolicy = new DefaultRetryPolicy(10000, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return super.setRetryPolicy(retryPolicy);
	}

	public int getStatusCode() {
		return mStatusCode;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		mStatusCode = response.statusCode;
		Log.e("StatusCode", "" + mStatusCode);
		return super.parseNetworkResponse(response);
	}

	
	@Override
	protected String getParamsEncoding() {
		// TODO Auto-generated method stub
		return "UTF-8";
	}
	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub

		Log.e("Deliver Error", "True");
		if(error==null || error.getMessage()==null || error.getMessage().trim().length()==0){
			error = new VolleyError(VolleySetup.ErrorMessage);
		}

		mErrorListener.onErrorResponse(error);
		super.parseNetworkError(error);
	}

	@Override
	protected VolleyError parseNetworkError(VolleyError volleyError) {
		if (volleyError.networkResponse != null
				&& volleyError.networkResponse.data != null) {
			Log.e("status Code", " code "
					+ volleyError.networkResponse.statusCode);

			try {
				JSONObject jsonobject = new JSONObject(new String(volleyError.networkResponse.data));
				VolleyError error;
				if(jsonobject.optString("message")!=null && jsonobject.optString("message").trim().length()>0){
					error = new VolleyError(
						jsonobject.optString("message"));
				}else{
					error = new VolleyError(VolleySetup.ErrorMessage);
				}
				volleyError = error;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			
			VolleyError error = new VolleyError(VolleySetup.ErrorMessage);
			volleyError = error;
		}

		return volleyError;
	}
}