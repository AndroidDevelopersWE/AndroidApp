package co.dtechsystem.carefer.WebServices;

import org.json.JSONObject;

import java.util.Map;

public interface VolleyResponseListener {
	
	Map<String, String> OnPreExecute();
	void OnSuccessListener(JSONObject mJsonObject);
	void OnErrorListener(String msg);
	

}
