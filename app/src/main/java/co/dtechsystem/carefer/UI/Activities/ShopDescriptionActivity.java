package co.dtechsystem.carefer.UI.Activities;

import android.os.Bundle;

import co.dtechsystem.carefer.R;


public class ShopDescriptionActivity extends BaseActivity {
    String shopName, shopDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_description);
        if (getIntent() != null) {
            shopName = intent.getStringExtra("shopName");
            shopDescription = intent.getStringExtra("shopDescription");
            aQuery.id(R.id.tv_shop_name_des).text(shopName);
            aQuery.id(R.id.tv_shop_description_des).text(shopDescription);

        }
    }
}
