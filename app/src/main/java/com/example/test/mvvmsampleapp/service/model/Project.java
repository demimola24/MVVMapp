package com.example.test.mvvmsampleapp.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Project {

    @SerializedName("list")
    public List<InnerWeather> list;

    @SerializedName("city")
    public CityData cityData;


    public Project() {
    }
}
