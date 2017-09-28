package com.example.ramil.myapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.adapter.CategoryRVadapter;
import com.example.ramil.myapp.interfaces.ServiceAPI;
import com.example.ramil.myapp.model.Categories;
import com.example.ramil.myapp.model.CategoriesList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoriesActivity extends AppCompatActivity {

    private static final String TAG = "CategoriesActivity";
    private static final String BASE_URL = "http://192.168.0.56//";

    private RecyclerView mRecyclerView;
    private CategoryRVadapter mAdapter;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_activity);

        initToolbar();

        BottomNavigationView botNavView = (BottomNavigationView) findViewById(R.id.bottomNav_Bar);
        botNavView.setOnNavigationItemSelectedListener(getBottomNavigationListener());

        Menu menu = botNavView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        //фиксированный размер лейаута RecyclerView
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        context = this;
        getAllCategories();
    }

    private void initToolbar()
    {
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.catalog_item);
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
                        Intent intent0 = new Intent(CategoriesActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_catalog:
                        break;

                    case R.id.ic_basket:
                        Intent intent2 = new Intent(CategoriesActivity.this, BasketActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        };
    }

    private void getAllCategories()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceAPI service = retrofit.create(ServiceAPI.class);
        Call<CategoriesList> call = service.getAllCategories();

        call.enqueue(new Callback<CategoriesList>()
        {
            @Override
            public void onResponse(Call<CategoriesList> call, Response<CategoriesList> response)
            {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.toString() );
                Log.d(TAG, "onResponse: Полученные данные:" + response.body().toString() );

                ArrayList<Categories> categoriesList = response.body().getCategories();

                mAdapter = new CategoryRVadapter(categoriesList, context);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<CategoriesList> call, Throwable t)
            {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(CategoriesActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }

        });

    }

}
