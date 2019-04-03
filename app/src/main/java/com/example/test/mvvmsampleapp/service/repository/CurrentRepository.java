package com.example.test.mvvmsampleapp.service.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.test.mvvmsampleapp.service.model.InnerWeatherC;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrentRepository  {
    private GetterService getterService;
    private static CurrentRepository currentRepository;
     MutableLiveData<InnerWeatherC> data = new MutableLiveData<>();

    private CurrentRepository() {
        //TODO this getterService instance will be injected using Dagger in part #2 ...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetterService.HTTPS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getterService = retrofit.create(GetterService.class);
    }

    public synchronized static CurrentRepository getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (currentRepository == null) {
            if (currentRepository == null) {
                currentRepository = new CurrentRepository();
            }
        }
        return currentRepository;
    }



    public LiveData<InnerWeatherC> getProjectCurrent(Double lat, Double lon) {


        getterService.getProjectCurrent(lat, lon,"3bb5393f60824e2db7fa1f112db3d3bd","metric").enqueue(new Callback<InnerWeatherC>() {
            @Override
            public void onResponse(Call<InnerWeatherC> call, Response<InnerWeatherC> response) {
                Log.d("ProjectRepository","ProjectRepository: passed C: "+response.body().temperature.temp);
                    data.setValue(response.body());

            }


            @Override
            public void onFailure(Call<InnerWeatherC> call, Throwable t) {
                // TODO better error handling in part #2 ...
                Log.d("ProjectRepository","ProjectRepository: failed"+call.toString()+" "+t.getMessage());
                t.printStackTrace();
                data.setValue(null);
            }
        });

        return data;
    }
}
