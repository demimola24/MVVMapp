package com.example.test.mvvmsampleapp.service.model;

import com.google.gson.annotations.SerializedName;

public class Temp {
    @SerializedName("temp")
    public String temp;

    public String getTemp() {
        return temp+"\u2103";
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}