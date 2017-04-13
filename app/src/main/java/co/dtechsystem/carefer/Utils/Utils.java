package co.dtechsystem.carefer.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract  class Utils {
    public static void savePreferences(Activity activity, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readPreferences(Activity activity, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        return sp.getString(key, defaultValue);
    }
    public static String formattedDateFromString(String inputFormat, String outputFormat, String inputDate) {
        if (inputFormat.equals("")) { // if inputFormat = "", set a default input format.
            inputFormat = "yyyy-MM-dd hh:mm:ss";
        }
        if (outputFormat.equals("")) {
            outputFormat = "EEEE d 'de' MMMM 'del' yyyy"; // if inputFormat = "", set a default output format.
        }
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat);

        // You can set a different Locale, This example set a locale of Country Mexico.
//            SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "MX"));
//            SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "MX"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;

    }
    public void makeToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }
    public RequestQueue getRequestQueue(Activity activity, RequestQueue mRequestQueue) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(activity);
        }

        return mRequestQueue;
    }
    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    public static String ValidateNumberFromLibPhone(Context context, String number){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil
                .getInstance();
        Phonenumber.PhoneNumber phNumberProto = null;

        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            Log.e("********   Value ********" + tm.getNetworkCountryIso(),"");
            String CountryCode =tm.getNetworkCountryIso() ;
            Log.e("********   Upper Value ********" + CountryCode.toUpperCase(),"");
            // I set the default region to IN (Indian)
            // You can find your country code here http://www.iso.org/iso/country_names_and_code_elements
            phNumberProto = phoneUtil.parse(number, "IN");

        } catch (NumberParseException e) {
            // if there's any error
            System.err
                    .println("NumberParseException was thrown: "
                            + e.toString());
        }

        // check if the number is valid
        boolean isValid = phoneUtil.isValidNumber(phNumberProto);

        if (isValid) {

            // get the valid number's international format
            String internationalFormat = phoneUtil.format(
                    phNumberProto,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);


            System.out.println("Validation" + internationalFormat);
            Log.e("Validate Number" + internationalFormat,"");
            return internationalFormat;

        } else {

            // prompt the user when the number is invalid
            Toast.makeText(context,
                    "Phone number is INVALID: " + number,
                    Toast.LENGTH_SHORT).show();

        }
        return "";
    }
    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }


}
