package com.example.test.mvvmsampleapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {

    public static boolean isDeviceOnline(Context context) {
        ConnectivityManager cMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
        if (netInfo == null || netInfo.getState() == null)
            return false;
        return netInfo.getState().equals(NetworkInfo.State.CONNECTED);
    }
}
