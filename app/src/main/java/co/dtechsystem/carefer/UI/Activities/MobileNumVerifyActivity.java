package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;

public class MobileNumVerifyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num_verification);
    }

    public void ben_Next_to_carafePolicy_Click(View v) {
        Utils.savePreferences(activity, "User_Mobile_varify", "1");
        Intent i = new Intent(this, CareferPolicyActivity.class);
        startActivity(i);
        finish();
    }
}
