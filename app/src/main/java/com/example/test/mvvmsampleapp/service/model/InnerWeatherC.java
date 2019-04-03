package com.example.test.mvvmsampleapp.service.model;

import com.google.gson.annotations.SerializedName;


public class InnerWeatherC {
    @SerializedName("main")
    public Temp temperature;

    @SerializedName("name")
    public String name;

}
