package co.dtechsystem.carefer.WebServices;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleySetup {
	private static RequestQueue mRequestQueue;

	public static String ErrorMessage="";
	private VolleySetup() {
		// no instances
	}

	public static void init(Context context) {

		try{
			mRequestQueue = Volley.newRequestQueue(context);

			ErrorMessage = "Something Went Wrong";
		}catch (Exception e){
			e.printStackTrace();
		}


		//AppConfig.init(context);
	}

	
	
	/*public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
*/

	
	

}
