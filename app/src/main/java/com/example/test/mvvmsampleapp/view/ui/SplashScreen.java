package com.example.test.mvvmsampleapp.view.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

public class SplashScreen extends Activity {



    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.splash_screen);


//        editor.putBoolean("NewNotification", true);
//        editor.commit();


        permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);
        statusCheck();
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayLocationSettingsRequest(SplashScreen.this);
            Log.d("location setting", "setting false");
        } else {
            Log.d("location setting", "setting true");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(
                            new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                } else {
                    CloseSplash();
                }
            } else {
                CloseSplash();
            }
        }
    }
    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                    } else {
                        CloseSplash();
                    }
                } else {
                    CloseSplash();
                }
                break;
        }
    }


    public void CloseSplash(){
        Thread splashScreenStarter = new Thread() {
            public void run() {
                int i = 0;
                try {
                    int waitingTime = 0;
                    while (waitingTime < 1000) {
                        sleep(500);
                        waitingTime = waitingTime + 400;
                    }
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    finish();

                }
            }

        };
        splashScreenStarter.start();
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d("Fresh","Fresh 1");
                        try {
                            startService(new Intent(SplashScreen.this, LocationServices.class));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (permissionsToRequest.size() > 0) {
                                    requestPermissions(permissionsToRequest.toArray(
                                            new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                                } else {
                                    CloseSplash();
                                }
                            } else {
                                CloseSplash();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent2 = new Intent(Intent.ACTION_MAIN);
                            intent2.addCategory(Intent.CATEGORY_HOME);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent2);
                            finish();
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("settings", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SplashScreen.this, 1);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("ds", "PendingIntent unable to execute request.");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (permissionsToRequest.size() > 0) {
                                    requestPermissions(permissionsToRequest.toArray(
                                            new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                                } else {
                                    CloseSplash();
                                }
                            } else {
                                CloseSplash();
                            }
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("ds", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (permissionsToRequest.size() > 0) {
                                requestPermissions(permissionsToRequest.toArray(
                                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                            } else {
                                CloseSplash();
                            }
                        } else {
                            CloseSplash();
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        System.out.println(resultCode);
        if (resultCode == RESULT_OK) {
            Log.d("Fresh","Fresh 2");
            //StartAnimations();
            //GetUtil();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(
                            new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                } else {
                    CloseSplash();
                }
            } else {
                CloseSplash();
            }
        } else {
            Intent intent2 = new Intent(Intent.ACTION_MAIN);
            intent2.addCategory(Intent.CATEGORY_HOME);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
            finish();
        }
    }
}
