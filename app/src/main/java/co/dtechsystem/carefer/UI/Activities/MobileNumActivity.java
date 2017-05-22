package co.dtechsystem.carefer.UI.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.lamudi.phonefield.PhoneEditText;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MobileNumActivity extends BaseActivity {
    private PhoneEditText phoneEditText;
    private Button submit_button;
    String CountryID;
    TelephonyManager tm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num);
        submit_button = (Button) findViewById(R.id.submit_button);
        phoneEditText = (PhoneEditText) findViewById(R.id.edit_text);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        CountryID = tm.getNetworkCountryIso();
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, 123);
        } else {
            AutoDetectMobileSim1();

        }
        phoneDropAndValid();

    }

    private void AutoDetectMobileSim1() {

        try {

            @SuppressLint("HardwareIds") String number = tm.getLine1Number();

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

    private void phoneDropAndValid() {

        assert phoneEditText != null;
        assert submit_button != null;

        phoneEditText.setHint(R.string.phone_hint);
        phoneEditText.setDefaultCountry(CountryID);
        Utils.gradientTextView(submit_button, activity);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validations.isInternetAvailable(activity, true)) {
                    if (phoneEditText.getPhoneNumber() != null && !phoneEditText.getPhoneNumber().equals("")) {
                        String Phone = phoneEditText.getPhoneNumber();
                        if (Phone != null && !Phone.equals("")) {
                            if (phoneEditText.isValid()) {
                                Utils.savePreferences(activity, "User_Mobile", phoneEditText.getPhoneNumber());
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
        phoneEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    phoneEditText.setDefaultCountry(CountryID);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AutoDetectMobileSim1();
                }
            }

        }
    }
}
