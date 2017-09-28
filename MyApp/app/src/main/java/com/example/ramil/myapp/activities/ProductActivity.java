package com.example.ramil.myapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.interfaces.ServiceAPI;
import com.example.ramil.myapp.model.BasketResponse;
import com.example.ramil.myapp.model.Products;
import com.example.ramil.myapp.model.ProductsList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";
    private static final String BASE_URL = "http://192.168.0.56/";

    private Toolbar toolbar;
    private TextView nameTitle, availableTitle, priceTitle, decriptionTitle;
    private ImageView imageView;
    private Button buyButton;
    private int id, available;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        initToolbar();

        BottomNavigationView botNavView = (BottomNavigationView) findViewById(R.id.bottomNav_Bar);
        botNavView.setOnNavigationItemSelectedListener(getBottomNavigationListener());

        Menu menu = botNavView.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);

        nameTitle = (TextView) findViewById(R.id.productTitle);
        availableTitle = (TextView) findViewById(R.id.productAvailableTitle);
        priceTitle = (TextView) findViewById(R.id.productPriceTitle);
        decriptionTitle = (TextView) findViewById(R.id.productDescriptionTitle);
        imageView = (ImageView) findViewById(R.id.productImage);
        buyButton = (Button) findViewById(R.id.buyButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ServiceAPI service = retrofit.create(ServiceAPI.class);

        getProduct( service );

        buyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ProductActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.number_dialog, null);
                final com.shawnlin.numberpicker.NumberPicker mNumberPicker =
                        (com.shawnlin.numberpicker.NumberPicker) mView.findViewById(R.id.numberPicker);
                mNumberPicker.setMaxValue( available );
                mBuilder.setMessage("Выберите количество:")
                        .setCancelable(false)
                        .setPositiveButton("Купить", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                createOrder(service, id, mNumberPicker.getValue());
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    private void getProduct( ServiceAPI api)
    {
        int pid = getIntent().getIntExtra("pid", 0);

        Call<ProductsList> call = api.getProduct(pid);

        call.enqueue(new Callback<ProductsList>()
        {
            @Override
            public void onResponse(Call<ProductsList> call, Response<ProductsList> response)
            {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.toString() );
                Log.d(TAG, "onResponse: Полученные данные:" + response.body().toString() );

                if( response.body().getSuccess() == 1 ) {
                    ArrayList<Products> products = response.body().getProducts();

                    String str = Double.toString( products.get(0).getPrice() );

                    nameTitle.setText(products.get(0).getName());
                    availableTitle.setText( "В наличии: " + products.get(0).getAvailable() + "шт");
                    priceTitle.setText(str + "р.");
                    decriptionTitle.setText(products.get(0).getDescription());

                    Picasso.with(imageView.getContext())
                            .load(products.get(0).getImage())
                            .resize( 400, 200 )
                            .into(imageView);

                    id = products.get(0).getPid();
                    available = products.get(0).getAvailable();

                } else {
                /*--------------------------Ошибка----------------------------*/
                }
            }

            @Override
            public void onFailure(Call<ProductsList> call, Throwable t)
            {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(ProductActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void createOrder( ServiceAPI api, int id, int count)
    {
        Call<BasketResponse> call = api.createOrder(id, count);

        call.enqueue(new Callback<BasketResponse>() {
            @Override
            public void onResponse(Call<BasketResponse> call, Response<BasketResponse> response)
            {
                Log.d(TAG, "onResponse: Ответ Сервера:" + response.body().toString() );
                Toast.makeText(ProductActivity.this, "Товар был перенесен в корзину",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BasketResponse> call, Throwable t)
            {
                Log.e(TAG, "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(ProductActivity.this, "Все плохо!", Toast.LENGTH_LONG).show();
            }
        });
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
                        Intent intent0 = new Intent(ProductActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.ic_catalog:
                        break;

                    case R.id.ic_basket:
                        Intent intent2 = new Intent(ProductActivity.this, BasketActivity.class);
                        startActivity(intent2);
                        break;
                }
                return true;
            }
        };
    }


}
