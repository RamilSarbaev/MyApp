package com.example.ramil.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class ProductActivity extends SingleFragmentActivity {

    private static final String EXTRA_PRODUCT_ID = "com.example.ramil.myapp.product_id";

    @Override
    protected Fragment createFragment() {
        int productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, 0);

        return ProductFragment.newInstance(productId);
    }

    public static Intent newIntent(Context packageContext, int productId) {
        Intent intent = new Intent(packageContext, ProductActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        return intent;
    }
}
