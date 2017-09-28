package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Basket
{
    @SerializedName("id_order")
    @Expose
    private int id_order;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("count")
    @Expose
    private int count;

    @SerializedName("total_price")
    @Expose
    private int total_price;

    @SerializedName("image")
    @Expose
    private String image;


    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Products{" +
                "id_order='" + id_order + '\'' +
                ", name='" + name + '\'' +
                ", count='" + count + '\'' +
                ", total_price='" + total_price + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
