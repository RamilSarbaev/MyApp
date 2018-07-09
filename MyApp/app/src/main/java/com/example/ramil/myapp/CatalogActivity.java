package com.example.ramil.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class CatalogActivity extends SingleFragmentActivity {

    private static final String EXTRA_CATEGORY_ID = "com.example.ramil.myapp.category_id";
    private static final String EXTRA_CATEGORY_NAME = "com.example.ramil.myapp.category_name";

    @Override
    protected Fragment createFragment() {
        int categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 0);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);

        return CatalogFragment.newInstance(categoryId, categoryName);
    }

    public static Intent newIntent(Context packageContext, int categoryId, String categoryName) {
        Intent intent = new Intent(packageContext, CatalogActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        return intent;
    }
}
