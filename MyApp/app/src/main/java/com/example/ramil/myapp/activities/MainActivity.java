package com.example.ramil.myapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.adapter.GVImageAdapter;
import com.example.ramil.myapp.adapter.ProductRVadapter;
import com.example.ramil.myapp.adapter.ViewPagerAdapter;
import com.example.ramil.myapp.interfaces.ServiceAPI;
import com.example.ramil.myapp.model.Products;
import com.example.ramil.myapp.model.ProductsList;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "http://192.168.0.56/";

    private Toolbar toolbar;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initToolbar();

        BottomNavigationView botNavView = (BottomNavigationView) findViewById(R.id.bottomNav_Bar);
        botNavView.setOnNavigationItemSelectedListener(getBottomNavigationListener());

        Menu menu = botNavView.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        viewPager = (ViewPager) findViewById(R.id.slider);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new mTimerTask(), 2000, 4000);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceAPI service = retrofit.create(ServiceAPI.class);

        getPopularProducts(service);
    }

    public void getPopularProducts(ServiceAPI api)
    {
        Call<ProductsList> call = api.getPopularProducts();
        final GridView gridView = (GridView) findViewById(R.id.gridView);

        call.enqueue(new Callback<ProductsList>() {
            @Override
            public void onResponse(Call<ProductsList> call, Response<ProductsList> response) {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.toString() );
                Log.d(TAG, "onResponse: Полученные данные:" + response.body().toString() );

                if( response.body().getSuccess() == 1 ) {
                     ArrayList<Products> data = response.body().getProducts();
                     gridView.setAdapter(new GVImageAdapter(MainActivity.this, data));
                } else {
                    Log.d(TAG, "onResponse: ООООООШИИИИИБКААААА" );
                }
            }

            @Override
            public void onFailure(Call<ProductsList> call, Throwable t) {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(MainActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initToolbar()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    @NonNull
    private BottomNavigationView.OnNavigationItemSelectedListener getBottomNavigationListener()
    {
        return new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.ic_home:

                        break;

                    case R.id.ic_catalog:
                        Intent intent1 = new Intent(MainActivity.this, CategoriesActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_basket:
                        Intent intent2 = new Intent(MainActivity.this, BasketActivity.class);
                        startActivity(intent2);
                        break;

                }

                return true;
            }
        };
    }

    public class mTimerTask extends TimerTask
    {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else
                        viewPager.setCurrentItem(0);
                }
            });
        }
    }

}
