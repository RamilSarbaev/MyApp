package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductsList
{
    @SerializedName("success")
    @Expose
    private int mSuccess;

    @SerializedName("products")
    @Expose
    private ArrayList<Products> mProducts;

    public int getSuccess() {
        return mSuccess;
    }

    public void setSuccess(int success) {
        mSuccess = success;
    }

    public ArrayList<Products> getProducts() {
        return mProducts;
    }

    public void setProducts(ArrayList<Products> products) {
        mProducts = products;
    }

    @Override
    public String toString() {
        return "ProductsList{" +
                "products ='" + mProducts +
                ", success =" + mSuccess + '\'' +
                '}';
    }
}
