package co.dtechsystem.carefer.UI.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;
import co.dtechsystem.carefer.Utils.Validations;

public class MobileNumVerifyActivity extends BaseActivity {
    private Button btn_verify_pin;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    private EditText et_1_verify;
    private EditText et_2_verify;
    private EditText et_3_verify;
    private EditText et_4_verify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num_verification);
        btn_verify_pin = (Button) findViewById(R.id.btn_verify_pin);
        et_1_verify = (EditText) findViewById(R.id.et_1_verify);
        et_2_verify = (EditText) findViewById(R.id.et_2_verify);
        et_3_verify = (EditText) findViewById(R.id.et_3_verify);
        et_4_verify = (EditText) findViewById(R.id.et_4_verify);
        SetFocusForEdit();

    }

    @SuppressWarnings("UnusedParameters")
    public void ben_Next_to_carafePolicy_Click(View v) {
        if (Validations.isInternetAvailable(activity, true)) {
            Utils.savePreferences(activity, "User_Mobile_varify", "1");
            Intent i = new Intent(this, CareferPolicyActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void SetFocusForEdit() {
        Utils.gradientTextView(btn_verify_pin, activity);
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

    private final BroadcastReceiver SMSBroadcastReceiver = new BroadcastReceiver() {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Intent recieved: " + intent.getAction());

            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    assert pdus != null;
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if (messages.length > -1) {
                        String message = messages[0].getMessageBody();
                        String pin;
                        if (message.contains("Carefer")) {
                            pin = message.replaceAll("[^0-9]", "");
                            if (pin != null && pin.length() == 4) {
                                et_1_verify.setText(String.valueOf(pin.charAt(0)));
                                et_2_verify.setText(String.valueOf(pin.charAt(1)));
                                et_3_verify.setText(String.valueOf(pin.charAt(2)));
                                et_4_verify.setText(String.valueOf(pin.charAt(3)));
                            }
                            Toast.makeText(context, "Pin received: " + pin, Toast.LENGTH_LONG).show();

                        }

                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SMS_RECEIVED);
        filter.setPriority(999);
        registerReceiver(SMSBroadcastReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(SMSBroadcastReceiver);
        super.onPause();
    }
}
