package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BasketList
{
    @SerializedName("success")
    @Expose
    private int mSuccess;

    @SerializedName("basket")
    @Expose
    private ArrayList<Basket> mBasket;

    public int getSuccess() {
        return mSuccess;
    }

    public void setSuccess(int success) {
        mSuccess = success;
    }

    public ArrayList<Basket> getBasket() {
        return mBasket;
    }

    public void setBasket(ArrayList<Basket> basket) {
        mBasket = basket;
    }

    @Override
    public String toString() {
        return "BasketList{" +
                "Basket ='" + mBasket +
                ", Success =" + mSuccess + '\'' +
                '}';
    }
}
