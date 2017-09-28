package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BasketList
{
    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("basket")
    @Expose
    private ArrayList<Basket> basket;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<Basket> getBasket() {
        return basket;
    }

    public void setBasket(ArrayList<Basket> basket) {
        this.basket = basket;
    }

    @Override
    public String toString() {
        return "BasketList{" +
                "basket='" + basket +
                ", success=" + success + '\'' +
                '}';
    }
}
