package com.example.test.mvvmsampleapp.service.repository;

import com.example.test.mvvmsampleapp.service.model.InnerWeather;
import com.example.test.mvvmsampleapp.service.model.InnerWeatherC;
import com.example.test.mvvmsampleapp.service.model.Project;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GetterService {
    String HTTPS_API_URL = "http://api.openweathermap.org/data/2.5/";

    @GET("forecast")
    Call<Project> getProjectList(@Query("lat") Double lat,@Query("lon") Double lon,@Query("APPID") String appid,@Query("units") String units);

    @GET("weather")
    Call<InnerWeatherC> getProjectCurrent(@Query("lat") Double lat,@Query("lon") Double lon, @Query("APPID") String appid, @Query("units") String units);
}
