package co.dtechsystem.carefer;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import co.dtechsystem.carefer.UI.Activities.BaseActivity;
import co.dtechsystem.carefer.UI.Activities.CareferPolicyActivity;
import co.dtechsystem.carefer.UI.Activities.MainActivity;
import co.dtechsystem.carefer.UI.Activities.MobileNumActivity;
import co.dtechsystem.carefer.UI.Activities.MobileNumVerifyActivity;
import co.dtechsystem.carefer.Utils.Utils;

public class SplashActivity extends BaseActivity {
    Locale locale;
    String Language;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //noinspection UnusedAssignment
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Obtain the shared Tracker instance.
//        AnalyticsApplication application = (AnalyticsApplication) getApplication().getApplicationContext();
//        mTracker = application.getDefaultTracker();
         Language = Utils.readPreferences(activity, "language", "");
        if (Language != null && !Language.equals("")) {
            SplashScreenThread();
        } else {
            CustomLanguageDialog();
        }

//        mScaler.scaleImage(R.drawable.img_splash_screen,iv_splash );
//        getCallDetails();

    }

    //Language Change dialog fun
    public String[] CustomLanguageDialog() {
        final String[] languageSelection = new String[1];
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.lay_dialog_choose_language);
        dialog.setTitle(getResources().getString(R.string.dialog_language_choose));
        dialog.setCancelable(false);
        // set the custom dialog components - text, image and button
        Button btn_language_eng = (Button) dialog.findViewById(R.id.btn_language_eng);
        Button btn_language_ar = (Button) dialog.findViewById(R.id.btn_language_ar);
        Button btn_cancel_lang = (Button) dialog.findViewById(R.id.btn_cancel_lang);
        // if button is clicked, close the custom dialog

        btn_language_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.savePreferences(activity, "language", "en");
                locale = new Locale("en");
                setLanguage(locale);
                dialog.dismiss();
                SplashScreenThread();
            }
        });
        btn_language_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.savePreferences(activity, "language", "ar");
                locale = new Locale("ar");
                setLanguage(locale);
                dialog.dismiss();
                SplashScreenThread();
            }
        });
        btn_cancel_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.savePreferences(activity, "language", "");
                dialog.dismiss();
            }
        });

        dialog.show();
        return languageSelection;
    }

    private void SplashScreenThread() {
        int SPLASH_DISPLAY_LENGTH = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                @SuppressWarnings("UnusedAssignment") Intent mainIntent = null;
                if (sUser_Mobile != null && !sUser_Mobile.equals("") && sUser_Mobile_Varify != null &&
                        sUser_Mobile_Varify.equals("1") && sPrivacy_check != null && sPrivacy_check.equals("1")) {
                    mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                } else if (sUser_Mobile != null && sUser_Mobile.equals("")) {
                    mainIntent = new Intent(SplashActivity.this, MobileNumActivity.class);

                } else if (sUser_Mobile_Varify != null && sUser_Mobile_Varify.equals("")) {
                    mainIntent = new Intent(SplashActivity.this, MobileNumVerifyActivity.class);
                } else if (sPrivacy_check != null && sPrivacy_check.equals("")) {
                    mainIntent = new Intent(SplashActivity.this, CareferPolicyActivity.class);
                } else {
                    mainIntent = new Intent(SplashActivity.this, MobileNumActivity.class);
                }
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
//                mTracker.setScreenName("Image~" + "SplashActivity");
//                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @SuppressWarnings({"unused", "Convert2Diamond", "StringConcatenationInsideStringBufferAppend"})
    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        @SuppressWarnings("UnusedAssignment") Uri contacts = CallLog.Calls.CONTENT_URI;
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") HashMap rowDataCall;
//        Cursor managedCursor = getContentResolver().query(contacts, null, null, null, null);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

        int number = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.NUMBER) : 0;
        int type = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.TYPE) : 0;
        int date = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.DATE) : 0;
        int duration = managedCursor != null ? managedCursor.getColumnIndex(CallLog.Calls.DURATION) : 0;
        sb.append("Call Details :");
        while (managedCursor != null && managedCursor.moveToNext()) {

            //noinspection UnusedAssignment
            rowDataCall = new HashMap<String, String>();

            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            String callDayTime = new Date(Long.valueOf(callDate)).toString();
            // long timestamp = convertDateToTimestamp(callDayTime);
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");


        }
        assert managedCursor != null;
        managedCursor.close();
        System.out.println(sb);
    }


}
