package com.example.test.mvvmsampleapp.service.model;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InnerWeather {
    @SerializedName("main")
    public Temp temperature;

    @SerializedName("weather")
    public List<SubInnerWeather> weather;

    @SerializedName("dt_txt")
    public String date_text;


    public String getDate_text() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("h:mm a dd MMMM yyyy");
        try {
            Date date = format.parse(date_text);
            this.date_text = format2.format(date);
            System.out.println("Date ->" + date);
        } catch (ParseException e) {
            e.printStackTrace();
            this.date_text = date_text;
        }
        return date_text;
    }
}