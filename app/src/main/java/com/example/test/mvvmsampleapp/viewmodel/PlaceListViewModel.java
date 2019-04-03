package com.example.test.mvvmsampleapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.test.mvvmsampleapp.service.model.CityItem;
import com.example.test.mvvmsampleapp.service.model.InnerWeatherC;
import com.example.test.mvvmsampleapp.service.repository.CurrentRepository;
import com.example.test.mvvmsampleapp.service.repository.PlaceRepository;

import java.util.List;

public class PlaceListViewModel extends AndroidViewModel {
    private final LiveData<List<CityItem>> projectObservable;

    public PlaceListViewModel(Application application) {
        super(application);

        // If any transformation is needed, this can be simply done by Transformations class ...
        projectObservable = PlaceRepository.getInstance().getPlaceList(getApplication().getApplicationContext());
    }
    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<List<CityItem>> getProjectObservable() {
        return projectObservable;
    }
}
