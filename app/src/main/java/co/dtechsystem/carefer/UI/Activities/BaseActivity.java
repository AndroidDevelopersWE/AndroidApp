package co.dtechsystem.carefer.UI.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Farsi;
import co.dtechsystem.carefer.Utils.Loading;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by DELL on 4/10/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    Loading loading;
    protected Activity activity;
    AQuery aQuery;
    Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Tahoma.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        gson = new Gson();
        aQuery = new AQuery(this);
        activity = this;
        loading = new Loading(this, "Loading...");
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


}