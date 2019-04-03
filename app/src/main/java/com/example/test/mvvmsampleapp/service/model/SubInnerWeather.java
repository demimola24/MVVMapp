package com.example.test.mvvmsampleapp.service.model;

import com.google.gson.annotations.SerializedName;

public class SubInnerWeather {
    @SerializedName("description")
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}