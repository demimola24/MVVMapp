package com.example.test.mvvmsampleapp.service.model;

import com.google.gson.annotations.SerializedName;

public class CityData {
    @SerializedName("name")
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}