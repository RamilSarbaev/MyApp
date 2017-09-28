package com.example.ramil.myapp.interfaces;


import com.example.ramil.myapp.model.BasketList;
import com.example.ramil.myapp.model.BasketResponse;
import com.example.ramil.myapp.model.CategoriesList;
import com.example.ramil.myapp.model.ProductsList;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceAPI
{
    @GET("get_all_products.php")
    Call<ProductsList> getAllProducts(@Query("id_cat") int id_cat);

    @GET("get_all_categories.php")
    Call<CategoriesList> getAllCategories();

    @GET("get_product_details.php")
    Call<ProductsList> getProduct(@Query("pid") int pid);

    @GET("get_basket.php")
    Call<BasketList> getBasket();

    @GET("delete_basket.php")
    Call<BasketResponse> deleteBasket();

    @GET("get_popular_products.php")
    Call<ProductsList> getPopularProducts();

    @POST("create_item_in_basket.php")
    @FormUrlEncoded
    Call<BasketResponse> createOrder(@Field("id_product") int id,
                                     @Field("count") int count);

    @POST("delete_baskets_item.php")
    @FormUrlEncoded
    Call<BasketResponse> deleteBasketsItem(@Field("id_order") int id);
}
