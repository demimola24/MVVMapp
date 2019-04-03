package com.example.test.mvvmsampleapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.test.mvvmsampleapp.service.model.InnerWeather;
import com.example.test.mvvmsampleapp.service.repository.ProjectRepository;

import java.util.List;

public class ProjectListsViewModel extends AndroidViewModel {
    private final LiveData<List<InnerWeather>> projectListObservable;

    public ProjectListsViewModel(Application application) {
        super(application);

        // If any transformation is needed, this can be simply done by Transformations class ...
        projectListObservable = ProjectRepository.getInstance().getProjectList(51.547957, -0.062989);
    }

    /**
     * Expose the LiveData Projects query so the UI can observe it.
     */
    public LiveData<List<InnerWeather>> getProjectListObservable() {
        return projectListObservable;
    }
}
