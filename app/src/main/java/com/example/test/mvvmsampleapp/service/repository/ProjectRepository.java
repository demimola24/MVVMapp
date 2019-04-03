package com.example.test.mvvmsampleapp.service.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.test.mvvmsampleapp.service.model.InnerWeather;
import com.example.test.mvvmsampleapp.service.model.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProjectRepository {
    private GetterService getterService;
    private static ProjectRepository projectRepository;
    MutableLiveData<List<InnerWeather>> data = new MutableLiveData<>();

    private ProjectRepository() {
        //TODO this getterService instance will be injected using Dagger in part #2 ...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetterService.HTTPS_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getterService = retrofit.create(GetterService.class);
    }

    public synchronized static ProjectRepository getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (projectRepository == null) {
            if (projectRepository == null) {
                projectRepository = new ProjectRepository();
            }
        }
        return projectRepository;
    }

    public LiveData<List<InnerWeather>> getProjectList(Double lat, Double lon) {


        getterService.getProjectList(lat, lon,"3bb5393f60824e2db7fa1f112db3d3bd","metric").enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                Log.d("ProjectRepository","ProjectRepository: passed "+response.body().list.size());
                data.setValue(response.body().list);
            }


            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                // TODO better error handling in part #2 ...
                Log.d("ProjectRepository","ProjectRepository: failed"+call.toString()+" "+t.getMessage());
                t.printStackTrace();
                data.setValue(null);
            }
        });

        return data;
    }
}
