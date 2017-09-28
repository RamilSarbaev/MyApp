package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categories
{
    @SerializedName("pid")
    @Expose
    private int pid;

    @SerializedName("name")
    @Expose
    private String name;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
