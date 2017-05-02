package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;

public class MobileNumVerifyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num_verification);
        SetFocusForEdit();

    }

    public void ben_Next_to_carafePolicy_Click(View v) {
        Utils.savePreferences(activity, "User_Mobile_varify", "1");
        Intent i = new Intent(this, CareferPolicyActivity.class);
        startActivity(i);
        finish();
    }

    public void SetFocusForEdit() {
        final EditText et_1_verify = (EditText) findViewById(R.id.et_1_verify);
        final EditText et_2_verify = (EditText) findViewById(R.id.et_2_verify);
        final EditText et_3_verify = (EditText) findViewById(R.id.et_3_verify);
        final EditText et_4_verify = (EditText) findViewById(R.id.et_4_verify);
        et_1_verify.setFocusableInTouchMode(true);
        et_1_verify.requestFocus();
        et_1_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_2_verify.setFocusableInTouchMode(true);
                    et_2_verify.requestFocus();
                } else {
                    et_1_verify.setFocusableInTouchMode(true);
                    et_1_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        et_2_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_3_verify.setFocusableInTouchMode(true);
                    et_3_verify.requestFocus();
                } else {
                    et_2_verify.setFocusableInTouchMode(true);
                    et_2_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        et_3_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_4_verify.setFocusableInTouchMode(true);
                    et_4_verify.requestFocus();
                } else {
                    et_3_verify.setFocusableInTouchMode(true);
                    et_3_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        et_4_verify.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    et_4_verify.clearFocus();
                    Utils.hideKeyboard(activity);
                } else {
                    et_4_verify.setFocusableInTouchMode(true);
                    et_4_verify.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
    }
}
