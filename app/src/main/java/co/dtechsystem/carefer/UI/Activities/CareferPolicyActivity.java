package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.dtechsystem.carefer.R;

public class CareferPolicyActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carefer_policy);
    }
    public void btn_Next_to_mainmenu_Click(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
