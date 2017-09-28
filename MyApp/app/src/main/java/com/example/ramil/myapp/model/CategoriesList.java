package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoriesList
{
    @SerializedName("success")
    @Expose
    private int success;

    @SerializedName("categories")
    @Expose
    private ArrayList<Categories> categories;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<Categories> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoriesList{" +
                "categories='" + categories +
                ", success=" + success + '\'' +
                '}';
    }
}
