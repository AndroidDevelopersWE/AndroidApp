package co.dtechsystem.carefer.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;

public class MobileNumActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_num);
    }

    public void ban_Next_to_Verification_Click(View v) {
        if(aQuery.find(R.id.et_mobile_number_amobile).getText().length()>0) {
            if (Utils.ValidateNumberFromLibPhone(activity, aQuery.find(R.id.et_mobile_number_amobile).getText().toString())) {
                Utils.savePreferences(activity, "User_Mobile", aQuery.find(R.id.et_mobile_number_amobile).getText().toString());
                Intent i = new Intent(this, MobileNumVerifyActivity.class);
                startActivity(i);
                finish();
            }
        }
        else {
            showToast("Please Enter mobile number first");
        }
    }
}
