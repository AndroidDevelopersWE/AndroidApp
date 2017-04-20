package co.dtechsystem.carefer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.analytics.Tracker;

import java.util.Date;
import java.util.HashMap;

import co.dtechsystem.carefer.UI.Activities.BaseActivity;
import co.dtechsystem.carefer.UI.Activities.CareferPolicyActivity;
import co.dtechsystem.carefer.UI.Activities.MainActivity;
import co.dtechsystem.carefer.UI.Activities.MobileNumActivity;
import co.dtechsystem.carefer.UI.Activities.MobileNumVerifyActivity;

public class SplashActivity extends BaseActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Obtain the shared Tracker instance.
//        AnalyticsApplication application = (AnalyticsApplication) getApplication().getApplicationContext();
//        mTracker = application.getDefaultTracker();
        SplashScreenThread();
//        getCallDetails();
    }

    public void SplashScreenThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = null;
                if (sUser_Mobile != null && !sUser_Mobile.equals("") && sUser_Mobile_Varify != null &&
                        sUser_Mobile_Varify.equals("1") && sPrivacy_check != null && sPrivacy_check.equals("verified")) {
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

    private void getCallDetails() {
        StringBuffer sb = new StringBuffer();
        Uri contacts = CallLog.Calls.CONTENT_URI;
        HashMap rowDataCall;
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

        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {

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
        managedCursor.close();
        System.out.println(sb);
    }
}
