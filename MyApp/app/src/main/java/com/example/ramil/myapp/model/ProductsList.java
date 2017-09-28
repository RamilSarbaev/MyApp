package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductsList
{
    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("products")
    @Expose
    private ArrayList<Products> products;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductsList{" +
                "products='" + products +
                ", success=" + success + '\'' +
                '}';
    }
}
