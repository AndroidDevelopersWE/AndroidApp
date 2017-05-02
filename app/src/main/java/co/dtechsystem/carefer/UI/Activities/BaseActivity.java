package co.dtechsystem.carefer.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.Locale;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Loading;
import co.dtechsystem.carefer.Utils.Utils;

/**
 * Created by DELL on 4/10/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    Loading loading;
    protected Activity activity;
    AQuery aQuery;
    Gson gson;
    Intent intent;
    public static String sRegId;
    public static String sUser_Mobile = "", sUser_Mobile_Varify = "", sPrivacy_check = "", sUser_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/arabic.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        gson = new Gson();
        aQuery = new AQuery(this);
        activity = this;
        loading = new Loading(this, getResources().getString(R.string.loading));
        intent = getIntent();
        sRegId = Utils.readPreferences(activity, "regId", "");
        if (sRegId != null && !sRegId.equals("")) {
        } else {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Utils.savePreferences(activity, "regId", refreshedToken);
        }
        sUser_Mobile = Utils.readPreferences(activity, "User_Mobile", "");
        sUser_Mobile_Varify = Utils.readPreferences(activity, "User_Mobile_varify", "");
        sPrivacy_check = Utils.readPreferences(activity, "User_privacy_check", "");
        sUser_ID = Utils.readPreferences(activity, "User_ID", "");
        Locale locale = new Locale("ar");
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void changetoAr(View v) {
        Locale locale = new Locale("ar");
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void changetoEn(View v) {
        Locale locale = new Locale("en");
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, displayMetrics);
    }

    protected void CloseActivity(View v) {
        finish();
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

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
                    }
                }).create().show();
    }

    protected void showProgress(String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            dismissProgress();

        mProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.app_name), msg);
    }

    protected void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    //Design custom marker for map
    protected Bitmap getMarkerBitmapFromView(String shopname, String serviceType, String des, String rating) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_shops, null);
        TextView tv_shop_name_shop_list = (TextView) customMarkerView.findViewById(R.id.tv_shop_name_shop_list);
        TextView tv_service_type_shop_list = (TextView) customMarkerView.findViewById(R.id.tv_service_type_shop_list);
        TextView tv_desc_shop_list = (TextView) customMarkerView.findViewById(R.id.tv_desc_shop_list);
        RatingBar rb_shop_shop_list = (RatingBar) customMarkerView.findViewById(R.id.rb_shop_shop_list);
        tv_shop_name_shop_list.setText(shopname);
        tv_service_type_shop_list.setText(serviceType);
        tv_desc_shop_list.setText(des);
        rb_shop_shop_list.setRating(Float.parseFloat(rating));

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
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
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}