package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoriesList
{
    @SerializedName("success")
    @Expose
    private int mSuccess;

    @SerializedName("categories")
    @Expose
    private ArrayList<Categories> mCategories;

    public int getSuccess() {
        return mSuccess;
    }

    public void setSuccess(int success) {
        mSuccess = success;
    }

    public ArrayList<Categories> getCategories() {
        return mCategories;
    }

    public void setCategories(ArrayList<Categories> categories) {
        mCategories = categories;
    }

    @Override
    public String toString() {
        return "CategoriesList{" +
                "categories ='" + mCategories +
                ", success =" + mSuccess + '\'' +
                '}';
    }
}
