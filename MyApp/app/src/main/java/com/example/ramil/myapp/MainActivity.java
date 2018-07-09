package com.example.ramil.myapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        if (savedInstanceState != null)
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);

        setFragment(mCurrentIndex);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener((@NonNull MenuItem item) -> {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.ic_home:
                        mCurrentIndex = 0;
                        setFragment(mCurrentIndex);
                        break;
                    case R.id.ic_catalog:
                        mCurrentIndex = 1;
                        setFragment(mCurrentIndex);
                        break;
                    case R.id.ic_basket:
                        mCurrentIndex = 2;
                        setFragment(mCurrentIndex);
                        break;
                }

                return true;
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void setFragment(int index) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment;

        switch (index) {
            case 1:
                fragment = CategoriesFragment.newInstance();
                break;
            case 2:
                fragment = BasketFragment.newInstance();
                break;
            default:
                fragment = HomeFragment.newInstance();
        }

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
