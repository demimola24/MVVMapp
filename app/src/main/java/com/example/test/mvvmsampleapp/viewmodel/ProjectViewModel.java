package com.example.test.mvvmsampleapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.test.mvvmsampleapp.service.model.InnerWeather;
import com.example.test.mvvmsampleapp.service.model.InnerWeatherC;
import com.example.test.mvvmsampleapp.service.repository.CurrentRepository;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {
    private final LiveData<InnerWeatherC> projectObservable;

    public ProjectViewModel(Application application) {
        super(application);

        // If any transformation is needed, this can be simply done by Transformations class ...
        projectObservable = CurrentRepository.getInstance().getProjectCurrent(51.547957, -0.062989);
    }
    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<InnerWeatherC> getProjectObservable() {
        return projectObservable;
    }
}
