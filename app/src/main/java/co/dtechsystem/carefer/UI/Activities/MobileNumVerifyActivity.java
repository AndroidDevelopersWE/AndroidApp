package co.dtechsystem.carefer.UI.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
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
    private TextView chronometer_sms;
    private boolean mAutoReceivedCode = false;
    CountDownTimer mCountDownTimer;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num_verification);
        btn_verify_pin = (Button) findViewById(R.id.btn_verify_pin);
//        btn_resend_code = (Button) findViewById(R.id.btn_resend_code);
        et_1_verify = (EditText) findViewById(R.id.et_1_verify);
        et_2_verify = (EditText) findViewById(R.id.et_2_verify);
        et_3_verify = (EditText) findViewById(R.id.et_3_verify);
        et_4_verify = (EditText) findViewById(R.id.et_4_verify);
        chronometer_sms = (TextView) findViewById(R.id.chronometer_sms);
        SetFocusForEdit();
        setdataToViews();

    }

    public void setdataToViews() {
//        btn_resend_code.setVisibility(View.GONE);
        StartTimer(120000);
//        btn_resend_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                firstSms = false;
//                btn_resend_code.setVisibility(View.GONE);
//                StartTimer();
//            }
//        });
    }

    public void StartTimer(long Time) {
        mCountDownTimer = new CountDownTimer(Time, 1000) {
            public void onTick(long millisUntilFinished) {
                chronometer_sms.setVisibility(View.VISIBLE);
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                chronometer_sms.setText(getResources().getString(R.string.toast_verfication_sent_mobile) + "\n" + v + ":" + String.format("%02d", va));
            }

            public void onFinish() {
                chronometer_sms.setText(getResources().getString(R.string.toast_verfication_sent_mobile) + "\n" + "00:00");
                if (mAutoReceivedCode == false) {
                    CustomResendCodeDialog();
                }
//                    chronometer_sms.setVisibility(View.GONE);
//                    btn_resend_code.setVisibility(View.VISIBLE);
            }
        };

        mCountDownTimer.start();

    }


    @SuppressWarnings("UnusedParameters")

    public void ben_Next_to_carafePolicy_Click(View v) {
        if (Validations.isInternetAvailable(activity, true)) {
            if (et_1_verify.length() > 0 && et_2_verify.length() > 0 && et_3_verify.length() > 0 && et_4_verify.length() > 0) {
                String VerificationCode = et_1_verify.getText().toString() + et_2_verify.getText().toString() +
                        et_3_verify.getText().toString() + et_4_verify.getText().toString();
                loading.show();
                APiVarifyCustomer(sUser_ID, VerificationCode);
            } else {
                showToast(getResources().getString(R.string.toast_fill_all_fields));
            }

        }
    }

    private void SetFocusForEdit() {
//        Utils.gradientTextView(btn_verify_pin, activity);
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

    //Resend code dialog fun
    public void CustomResendCodeDialog() {
        //Sorting dialog fun
        // custom dialog
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_resend_code);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_resend_code = (Button) dialog.findViewById(R.id.btn_resend_code);
        Button btn_re_enter_mobile = (Button) dialog.findViewById(R.id.btn_re_enter_mobile);
        Button btn_cancel_mobile = (Button) dialog.findViewById(R.id.btn_cancel_mobile);
        // if button is clicked, close the custom dialog

        btn_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                APiCreateUserPhone(sUser_Mobile);
                dialog.dismiss();
                StartTimer(120000);
            }
        });
        btn_re_enter_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MobileNumActivity.class);
                Utils.savePreferences(activity, "User_Mobile", "");
                Utils.savePreferences(activity, "User_ID", "");
                dialog.dismiss();
                startActivity(i);
                finish();
            }
        });
        btn_cancel_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
                                chronometer_sms.setVisibility(View.GONE);
                                mAutoReceivedCode = true;
                                loading.show();
                                APiVarifyCustomer(sUser_ID, pin);
                            }
//                            Toast.makeText(context, "Pin received: " + pin, Toast.LENGTH_LONG).show();

                        }

                    }
                }
            }
        }
    };

    private void APiVarifyCustomer(final String UserID, final String verificationCode) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiVarifyCustomer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject customerDetails = jsonObject.getJSONObject("customer");
                            String Status = customerDetails.getString("statusCode");
                            if (Status.equals("1")) {
                                Utils.savePreferences(activity, "User_Mobile_varify", Status);
                                if (mCountDownTimer != null) {
                                    mCountDownTimer.cancel();
                                    mCountDownTimer = null;
                                }
                                Intent i = new Intent(activity, CareferPolicyActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                loading.close();
                                showToast(getResources().getString(R.string.toast_mobile_Verified));
                                finish();
                            } else {
                                showToast(getResources().getString(R.string.invalid_phone_number));
                            }
                        } catch (JSONException e) {
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            loading.close();
                            e.printStackTrace();
                        }
                        loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customerID", UserID);
                params.put("verificationCode", verificationCode);

                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

    public void APiCreateUserPhone(final String customerMobile) {
        // prepare the Request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppConfig.APiCreateUserPhone,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject customerDetails = jsonObject.getJSONObject("customer");
                            String smsAPIResponse = customerDetails.getString("smsAPIResponse");
                            if (smsAPIResponse != null && !smsAPIResponse.equals("SMS sent successfully.")) {
                                showToast(smsAPIResponse);
                                loading.close();
                            } else {
                                showToast(getResources().getString(R.string.toast_verfication_sent_mobile));
                                loading.close();
                            }
                        } catch (JSONException e) {
                            showToast(getResources().getString(R.string.some_went_wrong_parsing));
                            loading.close();
                            e.printStackTrace();
                        }
                        loading.close();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.close();
                        showToast(getResources().getString(R.string.some_went_wrong));
                        // error
                        error.printStackTrace();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @SuppressWarnings("Convert2Diamond")
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobileNumber", customerMobile);

                return params;
            }
        };
// add it to the RequestQueue
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
    }

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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
