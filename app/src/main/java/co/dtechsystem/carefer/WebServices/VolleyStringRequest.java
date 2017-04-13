package co.dtechsystem.carefer.WebServices;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class VolleyStringRequest {

	private int mStatusCode;
	private Response.ErrorListener mErrorListener;
	private VolleyResponseListener mVolleyResponseListener;
	
	int method;
	String url;
	
	CustomStringRequest request;
    
	
	Response.ErrorListener ErrorLisner =  new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			
		}
	}; 
	
	
	Response.Listener<String> SuccessLisner = new Response.Listener<String>() {

		@Override
		public void onResponse(String arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	
	public VolleyStringRequest(int method, String url) {
		
		this.method = method;
		this.url = url;
		
		
		SuccessLisner = new Response.Listener<String>() {

			@Override
			public void onResponse(String arg0) {
				// TODO Auto-generated method stub

				Log.e("OnVolleySuccess", arg0);
				try {
					mVolleyResponseListener.OnSuccessListener(new JSONObject(arg0));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		ErrorLisner = new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				Log.e("OnVolleyError", arg0.getMessage());
				mVolleyResponseListener.OnErrorListener(arg0.getMessage());
			}
		};
		
		
		
		mErrorListener = ErrorLisner;
	}

	public void SetVolleyResponseListener( VolleyResponseListener responselistener){
		mVolleyResponseListener = responselistener;
	}
	
	
	public void Execute(RequestQueue queue){
		Map<String, String> Params  = mVolleyResponseListener.OnPreExecute();
		
		Log.e("Api", this.url);
		if(Params!=null && Params.size()>0){
			
			try{
				for(String key : Params.keySet()) {
					Log.e("params", key+"=>"+Params.get(key));
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
		}else{
			Log.e("Params", "Null");
		}

		request = new CustomStringRequest(this.method, this.url, SuccessLisner, ErrorLisner,Params);
		request.setRetryPolicy(new DefaultRetryPolicy(10000, 0,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(request);
		
	}
	
	
}
