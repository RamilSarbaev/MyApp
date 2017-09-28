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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.adapter.BasketRVadapter;
import com.example.ramil.myapp.adapter.CategoryRVadapter;
import com.example.ramil.myapp.interfaces.ServiceAPI;
import com.example.ramil.myapp.model.Basket;
import com.example.ramil.myapp.model.BasketList;
import com.example.ramil.myapp.model.BasketResponse;
import com.example.ramil.myapp.model.Categories;
import com.example.ramil.myapp.model.CategoriesList;

import java.security.PrivateKey;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity {

    private static final String TAG = "BasketActivity";
    private static final String BASE_URL = "http://192.168.0.56/";

    private RecyclerView mRecyclerView;
    private BasketRVadapter mAdapter;
    private Context context;

    private TextView countTitle;
    private TextView totalPriceTitle;
    private TextView emptyBasketTitle;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_activity);

        initToolbar();

        BottomNavigationView botNavView = (BottomNavigationView) findViewById(R.id.bottomNav_Bar);
        botNavView.setOnNavigationItemSelectedListener(getBottomNavigationListener());

        Menu menu = botNavView.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        //фиксированный размер лейаута RecyclerView
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        countTitle = (TextView) findViewById(R.id.basketCountTitle);
        totalPriceTitle = (TextView) findViewById(R.id.basketPriceTitle);
        emptyBasketTitle = (TextView) findViewById(R.id.emptyContentText);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ServiceAPI service = retrofit.create(ServiceAPI.class);

        context = this;
        getBasket(service);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBasket(service);
            }
        });
    }

    private void getBasket(final ServiceAPI service)
    {
        Call<BasketList> call = service.getBasket();

        call.enqueue(new Callback<BasketList>()
        {
            @Override
            public void onResponse(Call<BasketList> call, Response<BasketList> response)
            {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.toString() );
                Log.d(TAG, "onResponse: Полученные данные:" + response.body().toString() );

                if( response.body().getSuccess() == 1 )
                {
                    ArrayList<Basket> basketList = response.body().getBasket();

                    mAdapter = new BasketRVadapter(basketList, context, service);
                    mRecyclerView.setAdapter(mAdapter);

                    int count = 0;
                    int totalPrice = 0;

                    for (int i = 0; i < basketList.size(); i++) {
                        count += basketList.get(i).getCount();
                        totalPrice += basketList.get(i).getTotal_price();
                    }

                    if (count == 1)
                        countTitle.setText(String.valueOf(count) + " товар на сумму:");
                    else if (count == 2 || count == 3 || count == 4)
                        countTitle.setText(String.valueOf(count) + " товара на сумму:");
                    else
                        countTitle.setText(String.valueOf(count) + " товаров на сумму:");
                    totalPriceTitle.setText(String.valueOf(totalPrice) + "р.");
                } else {
                    emptyBasketTitle = (TextView) findViewById(R.id.emptyContentText);
                    emptyBasketTitle.setText("В корзине пока нет товаров");
                    confirmBtn.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<BasketList> call, Throwable t)
            {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(BasketActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void deleteBasket(ServiceAPI service)
    {
        Call<BasketResponse> call = service.deleteBasket();

        call.enqueue(new Callback<BasketResponse>()
        {
            @Override
            public void onResponse(Call<BasketResponse> call, Response<BasketResponse> response)
            {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.toString() );
                Log.d(TAG, "onResponse: Полученные данные:" + response.body().toString() );
                BasketActivity.this.recreate();
            }

            @Override
            public void onFailure(Call<BasketResponse> call, Throwable t)
            {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(BasketActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }

        });
    }


    private void initToolbar()
    {
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.basket_item);
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
                        Intent intent0 = new Intent(BasketActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_catalog:
                        Intent intent1 = new Intent(BasketActivity.this, CategoriesActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_basket:

                        break;
                }
                return true;
            }
        };

    }

}
