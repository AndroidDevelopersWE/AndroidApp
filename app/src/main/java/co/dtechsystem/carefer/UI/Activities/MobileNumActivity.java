package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.lamudi.phonefield.PhoneEditText;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MobileNumActivity extends BaseActivity {
    PhoneEditText phoneEditText;
    Button submit_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num);
        submit_button = (Button) findViewById(R.id.submit_button);
        phoneEditText = (PhoneEditText) findViewById(R.id.edit_text);
        phoneDropAndValid();
        AutoDetectMobileSim1();
    }

    public void AutoDetectMobileSim1() {
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String number = tm.getLine1Number();
            String CountryID = tm.getNetworkCountryIso().toString();
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(number, CountryID.toUpperCase());
            String countryCode = String.valueOf(numberProto.getCountryCode());
            if (number.startsWith("00")) {
                number = number.replaceFirst("00", "");
            }
            if (number.startsWith("0")) {
                number = number.replaceFirst("0", "");
            }
            if (!number.startsWith(countryCode)) {
                number = countryCode + number;
            }
            if (!number.startsWith("+")) {
                number = "+" + number;
            }
            phoneEditText.setDefaultCountry(CountryID);
            phoneEditText.getEditText().setText(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void phoneDropAndValid() {

        assert phoneEditText != null;
        assert submit_button != null;

        phoneEditText.setHint(R.string.phone_hint);
        phoneEditText.setDefaultCountry("SA");
        Utils.gradientTextView(submit_button, activity);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validations.isInternetAvailable(activity, true)) {
                    if (phoneEditText.getPhoneNumber() != null && !phoneEditText.getPhoneNumber().equals("")) {
                        String Phone = phoneEditText.getPhoneNumber();
                        if (Phone != null && !Phone.equals("")) {
                            if (phoneEditText.isValid()) {
                                Utils.savePreferences(activity, "User_Mobile", phoneEditText.getPhoneNumber().toString());
                                Intent i = new Intent(activity, MobileNumVerifyActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                showToast(getResources().getString(R.string.invalid_phone_number));
                            }
                        }
                    } else {
                        showToast(getResources().getString(R.string.enter_mobile));
                    }
                }
            }
        });

    }
}
