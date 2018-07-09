package com.example.ramil.myapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasketResponse
{
    @SerializedName("success")
    @Expose
    private int mSuccess;

    @SerializedName("message")
    @Expose
    private String mMessage;

    public int getSuccess() {
        return mSuccess;
    }

    public void setSuccess(int success) {
        mSuccess = success;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public String toString() {
        return "Post{" +
                "success ='" + mSuccess +
                ", message =" + mMessage + '\'' +
                '}';
    }
}
