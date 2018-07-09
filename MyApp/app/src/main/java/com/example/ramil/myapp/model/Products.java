package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Products
{
    @SerializedName("pid")
    @Expose
    private int mPid;

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("price")
    @Expose
    private double mPrice;

    @SerializedName("description")
    @Expose
    private String mDescription;

    @SerializedName("available")
    @Expose
    private int mAvailable;

    @SerializedName("id_category")
    @Expose
    private int mIdCategory;

    @SerializedName("image")
    @Expose
    private String mImage;

    public Products(String name, int id)
    {
        setName(name);
        setPid(id);
    }

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

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getAvailable() {
        return mAvailable;
    }

    public void setAvailable(int available) {
        mAvailable = available;
    }

    public int getIdCategory() {
        return mIdCategory;
    }

    public void setIdCategory(int idCategory) {
        mIdCategory = idCategory;
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
                "pid ='" + mPid + '\'' +
                ", name ='" + mName + '\'' +
                ", price ='" + mPrice + '\'' +
                ", description ='" + mDescription + '\'' +
                ", available ='" + mAvailable + '\'' +
                ", id_category ='" + mIdCategory + '\'' +
                ", image ='" + mImage + '\'' +
                '}';
    }
}
