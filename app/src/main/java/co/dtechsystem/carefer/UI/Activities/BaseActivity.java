package co.dtechsystem.carefer.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.Locale;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Loading;
import co.dtechsystem.carefer.Utils.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    Loading loading;
    protected Activity activity;
    AQuery aQuery;
    Gson gson;
    Intent intent;
    public static String sUser_Mobile = "", sUser_Mobile_Varify = "", sPrivacy_check = "", sUser_ID;
    Locale locale, localeEn;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/shahd.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gson = new Gson();
        aQuery = new AQuery(this);
        activity = this;
        loading = new Loading(this, getResources().getString(R.string.loading));
        intent = getIntent();
        String sRegId = Utils.readPreferences(activity, "regId", "");
        if (sRegId != null && !sRegId.equals("")) {
        } else {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Utils.savePreferences(activity, "regId", refreshedToken);
        }
        sUser_Mobile = Utils.readPreferences(activity, "User_Mobile", "");
        sUser_Mobile_Varify = Utils.readPreferences(activity, "User_Mobile_varify", "");
        sPrivacy_check = Utils.readPreferences(activity, "User_privacy_check", "");
        sUser_ID = Utils.readPreferences(activity, "User_ID", "");
        locale = new Locale("ar");
        localeEn = new Locale("en");
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(localeEn);
        Locale.setDefault(localeEn);
        //noinspection deprecation
        resources.updateConfiguration(configuration, displayMetrics);
    }


    @SuppressWarnings({"unused", "UnusedParameters"})
    protected void CloseActivity(View v) {
        finish();
    }

    @SuppressWarnings("unused")
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @SuppressWarnings("unused")
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).create().show();
    }

    @SuppressWarnings("unused")
    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

    private void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Utils.hideKeyboard(activity);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}