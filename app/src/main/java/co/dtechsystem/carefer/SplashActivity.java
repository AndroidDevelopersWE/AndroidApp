package co.dtechsystem.carefer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import co.dtechsystem.carefer.Google.AnalyticsApplication;
import co.dtechsystem.carefer.UI.Activities.MobileNumActivity;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Obtain the shared Tracker instance.
//        AnalyticsApplication application = (AnalyticsApplication) getApplication().getApplicationContext();
//        mTracker = application.getDefaultTracker();
        SplashScreenThread();
    }

    public void SplashScreenThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MobileNumActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
//                mTracker.setScreenName("Image~" + "SplashActivity");
//                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
