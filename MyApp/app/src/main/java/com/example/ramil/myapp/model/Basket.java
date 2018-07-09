package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Basket
{
    @SerializedName("id_order")
    @Expose
    private int mIdOrder;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("count")
    @Expose
    private int mCount;

    @SerializedName("total_price")
    @Expose
    private int mTotalPrice;

    @SerializedName("image")
    @Expose
    private String mImage;


    public int getIdOrder() {
        return mIdOrder;
    }

    public void setIdOrder(int idOrder) {
        mIdOrder = idOrder;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        mTotalPrice = totalPrice;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    @Override
    public String toString() {
        return "Products{" +
                "IdOrder='" + mIdOrder + '\'' +
                ", Name='" + mName + '\'' +
                ", Count='" + mCount + '\'' +
                ", TotalPrice='" + mTotalPrice + '\'' +
                ", Image='" + mImage + '\'' +
                '}';
    }
}
