package com.example.ramil.myapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.adapter.ProductRVadapter;
import com.example.ramil.myapp.interfaces.ServiceAPI;
import com.example.ramil.myapp.model.Products;
import com.example.ramil.myapp.model.ProductsList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CatalogActivity extends AppCompatActivity {

    private static final String TAG = "CatalogActivity";
    private static final String BASE_URL = "http://192.168.0.56/";

    private Toolbar toolbar;
    private TextView textView;
    private EditText filter;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        initToolbar();
        setSupportActionBar(toolbar);

        BottomNavigationView botNavView = (BottomNavigationView) findViewById(R.id.bottomNav_Bar);
        botNavView.setOnNavigationItemSelectedListener(getBottomNavigationListener());

        Menu menu = botNavView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        //фиксированный размер лейаута RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        context = this;
        getAllProducts();
    }

    private void initToolbar()
    {
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
                        Intent intent0 = new Intent(CatalogActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_catalog:
                        break;

                    case R.id.ic_basket:
                        Intent intent2 = new Intent(CatalogActivity.this, BasketActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        };
    }

    private void getAllProducts()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        int id_cat = getIntent().getIntExtra("id_cat", 0);
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle(name);

        ServiceAPI service = retrofit.create(ServiceAPI.class);
        Call<ProductsList> call = service.getAllProducts(id_cat);

        call.enqueue(new Callback<ProductsList>()
        {
            @Override
            public void onResponse(Call<ProductsList> call, Response<ProductsList> response)
            {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.toString() );
                Log.d(TAG, "onResponse: Полученные данные:" + response.body().toString() );

                if( response.body().getSuccess() == 1 ) {
                    ArrayList<Products> productsList = response.body().getProducts();
                    mAdapter = new ProductRVadapter(productsList, context);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    textView = (TextView) findViewById(R.id.emptyContentText);
                    textView.setText("В этой категории нет товаров");
                }
            }

            @Override
            public void onFailure(Call<ProductsList> call, Throwable t)
            {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(CatalogActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
