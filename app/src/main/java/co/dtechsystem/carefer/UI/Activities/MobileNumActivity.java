package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.dtechsystem.carefer.R;

public class MobileNumActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num);
    }

    public void ban_Next_to_Verification_Click(View v) {
        Intent i = new Intent(this, MobileNumVerifyActivity.class);
        startActivity(i);
    }
}
