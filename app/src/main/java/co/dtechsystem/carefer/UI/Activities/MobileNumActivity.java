package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lamudi.phonefield.PhoneEditText;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;

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
        });

    }
}
