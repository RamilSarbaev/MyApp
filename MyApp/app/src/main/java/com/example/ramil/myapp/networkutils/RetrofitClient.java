package com.example.ramil.myapp.networkutils;

import com.example.ramil.myapp.model.BasketList;
import com.example.ramil.myapp.model.BasketResponse;
import com.example.ramil.myapp.model.CategoriesList;
import com.example.ramil.myapp.model.ProductsList;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.0.13/";

    private static RetrofitClient sInstance;

    private static ServiceAPI sApiService;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sApiService = retrofit.create(ServiceAPI.class);
    }

    public static RetrofitClient getInstance() {
        if (sInstance == null)
            sInstance = new RetrofitClient();

        return sInstance;
    }

    public Observable<ProductsList> getPopularProducts() {
        return sApiService.getPopularProducts();
    }

    public Observable<ProductsList> getAllProducts(int categoryId) {
        return sApiService.getAllProducts(categoryId);
    }

    public Observable<CategoriesList> getAllCategories() {
        return sApiService.getAllCategories();
    }

    public Observable<ProductsList> getProduct(int productId) {
        return sApiService.getProduct(productId);
    }

    public Observable<BasketResponse> createOrder(int productId, int count) {
        return sApiService.createOrder(productId, count);
    }

    public Observable<BasketList> getBasket() {
        return sApiService.getBasket();
    }

    public Observable<BasketResponse> deleteBasket() {
        return sApiService.deleteBasket();
    }

    public Observable<BasketResponse> deleteBasketItem(int productId) {
        return sApiService.deleteBasketsItem(productId);
    }
}
