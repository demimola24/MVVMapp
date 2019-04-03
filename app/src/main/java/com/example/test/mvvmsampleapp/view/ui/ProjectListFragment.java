package com.example.test.mvvmsampleapp.view.ui;

import android.Manifest;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.test.mvvmsampleapp.R;
import com.example.test.mvvmsampleapp.databinding.FragmentProjectListBinding;
import com.example.test.mvvmsampleapp.service.model.AdapterModel;
import com.example.test.mvvmsampleapp.service.model.CityItem;
import com.example.test.mvvmsampleapp.service.model.InnerWeather;
import com.example.test.mvvmsampleapp.service.model.InnerWeatherC;
import com.example.test.mvvmsampleapp.service.repository.CurrentRepository;
import com.example.test.mvvmsampleapp.service.repository.ProjectRepository;
import com.example.test.mvvmsampleapp.util.Utilities;
import com.example.test.mvvmsampleapp.view.adapter.ProjectAdapter;
import com.example.test.mvvmsampleapp.view.callback.ProjectClickCallback;
import com.example.test.mvvmsampleapp.viewmodel.PlaceListViewModel;
import com.example.test.mvvmsampleapp.viewmodel.ProjectListsViewModel;
import com.example.test.mvvmsampleapp.viewmodel.ProjectViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProjectListFragment extends LifecycleFragment implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "ProjectListFragment";
    private ProjectAdapter projectAdapter;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    private FragmentProjectListBinding binding;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    //  private FragmentProjectBinding currentBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_project_list, container, false);
        final AutoCompleteTextView edtProduct = binding.edtPlace;
        edtProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityItem selected = (CityItem) parent.getItemAtPosition(position);
                edtProduct.setText("");
                if (Utilities.isDeviceOnline(getContext())){
                    CurrentRepository.getInstance().getProjectCurrent(selected.getCoord().lat, selected.getCoord().lon);
                    ProjectRepository.getInstance().getProjectList(selected.getCoord().lat, selected.getCoord().lon);

                    binding.setWeathercurrent("------");
                    binding.setWeatherplace("loading");
                }else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                Log.d("Called", "OnClick Called " + selected.name);
            }
        });

        projectAdapter = new ProjectAdapter(projectClickCallback);
        binding.projectList.setAdapter(projectAdapter);
        binding.setIsLoading(true);

        try {
            if (!isGooglePlayServicesAvailable()) {
                Toast.makeText(getContext(), "Location Service is unavailable", Toast.LENGTH_SHORT).show();
            }
            createLocationRequest();
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGoogleApiClient.connect();
                    Log.d(TAG, "onStart fired ..............");
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(getContext(), "Loading User Location", Toast.LENGTH_LONG).show();

        return binding.getRoot();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        try {
            mGoogleApiClient.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            stopLocationUpdates();
        } catch (Exception ex) {

        }
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        } catch (Exception ex) {

        }
        Log.d(TAG, "Location update stopped .......................");
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ProjectListsViewModel viewModel =
                ViewModelProviders.of(this).get(ProjectListsViewModel.class);

        final ProjectViewModel viewModel2 =
                ViewModelProviders.of(this).get(ProjectViewModel.class);

        final PlaceListViewModel viewModel3 =
                ViewModelProviders.of(this).get(PlaceListViewModel.class);

        observeViewModel(viewModel);
        observeViewModel2(viewModel2);
        observeViewModel3(viewModel3);
    }

    private void observeViewModel(ProjectListsViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProjectListObservable().observe(this, new Observer<List<InnerWeather>>() {
            @Override
            public void onChanged(@Nullable List<InnerWeather> projects) {
                if (projects != null) {
                    binding.setIsLoading(false);
                    Log.d("onChanged", "onChanged");
                    projectAdapter.setProjectList(projects);
                }
            }
        });
    }

    private void observeViewModel3(PlaceListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProjectObservable().observe(this, new Observer<List<CityItem>>() {
            @Override
            public void onChanged(@Nullable List<CityItem> projects) {
                if (projects != null) {
                    binding.setSearchItemsLoaded(true);
                    Log.d("onChanged", "onChanged Auto");
                    ArrayAdapter<CityItem> placeAdapter = new ArrayAdapter<CityItem>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, projects);
                    AutoCompleteTextView edtProduct = binding.edtPlace;
                    edtProduct.setAdapter(placeAdapter);
                }
            }
        });
    }

    private void observeViewModel2(ProjectViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getProjectObservable().observe(this, new Observer<InnerWeatherC>() {
            @Override
            public void onChanged(@Nullable InnerWeatherC project) {
                if (project != null) {
                    binding.setIsLoading(false);
                    binding.setWeathercurrent(project.temperature.getTemp());
                    binding.setWeatherplace(project.name);
                    Log.d("onChanged", "onChanged");

                }
            }
        });
    }

    private final ProjectClickCallback projectClickCallback = new ProjectClickCallback() {
        @Override
        public void onClick(InnerWeather project) {
        }
    };


    protected void startLocationUpdates() {
//        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                mCurrentLocation =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                Log.d("getLastLocation","getLastLocation: "+mCurrentLocation.getLongitude());
                if (mCurrentLocation!=null){
                    if (Utilities.isDeviceOnline(getContext())){
                        CurrentRepository.getInstance().getProjectCurrent(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                        ProjectRepository.getInstance().getProjectList(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());

                        binding.setWeathercurrent("------");
                        binding.setWeatherplace("loading");
                    }else {
                        Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                }
            }

        }, 3000); // 5000ms delay
    }

    @Override
    public void onLocationChanged(Location location) {
    }
}
