package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories
{
    @SerializedName("pid")
    @Expose
    private int mPid;

    @SerializedName("name")
    @Expose
    private String mName;

    public int getPid() {
        return mPid;
    }

    public void setPid(int pid) {
        mPid = pid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "pid ='" + mPid + '\'' +
                ", name ='" + mName + '\'' +
                '}';
    }
}
