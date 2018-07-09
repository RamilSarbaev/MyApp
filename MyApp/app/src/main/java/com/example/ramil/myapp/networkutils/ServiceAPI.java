package com.example.ramil.myapp.networkutils;


import com.example.ramil.myapp.model.BasketList;
import com.example.ramil.myapp.model.BasketResponse;
import com.example.ramil.myapp.model.CategoriesList;
import com.example.ramil.myapp.model.ProductsList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceAPI
{
    @GET("get_all_products.php")
    Observable<ProductsList> getAllProducts(@Query("id_cat") int id_cat);

    @GET("get_all_categories.php")
    Observable<CategoriesList> getAllCategories();

    @GET("get_product_details.php")
    Observable<ProductsList> getProduct(@Query("pid") int pid);

    @GET("get_basket.php")
    Observable<BasketList> getBasket();

    @GET("delete_basket.php")
    Observable<BasketResponse> deleteBasket();

    @GET("get_popular_products.php")
    Observable<ProductsList> getPopularProducts();

    @POST("create_item_in_basket.php")
    @FormUrlEncoded
    Observable<BasketResponse> createOrder(@Field("id_product") int id,
                                     @Field("count") int count);

    @POST("delete_baskets_item.php")
    @FormUrlEncoded
    Observable<BasketResponse> deleteBasketsItem(@Field("id_order") int id);
}
