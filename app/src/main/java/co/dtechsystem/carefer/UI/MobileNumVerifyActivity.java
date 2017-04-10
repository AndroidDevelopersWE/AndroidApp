package co.dtechsystem.carefer.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import co.dtechsystem.carefer.R;

public class MobileNumVerifyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num_verification);
    }

    public void btn_Next_to_careferPolicy_Click(View v) {
        Intent i = new Intent(this, CareferPolicyActivity.class);
        startActivity(i);
    }
}
