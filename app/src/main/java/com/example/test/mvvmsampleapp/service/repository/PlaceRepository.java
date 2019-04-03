package com.example.test.mvvmsampleapp.service.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.test.mvvmsampleapp.R;
import com.example.test.mvvmsampleapp.service.model.CityItem;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class PlaceRepository {
    private static PlaceRepository placeRepository;

    private PlaceRepository() {
        //TODO this getterService instance will be injected using Dagger in part #2 ...
    }

    public synchronized static PlaceRepository getInstance() {
        //TODO No need to implement this singleton in Part #2 since Dagger will handle it ...
        if (placeRepository == null) {
            if (placeRepository == null) {
                placeRepository = new PlaceRepository();
            }
        }
        return placeRepository;
    }

    public LiveData<List<CityItem>> getPlaceList(final Context context) {
         MutableLiveData<List<CityItem>> data = new MutableLiveData<>();
        new AsyncCaller(data,context).execute();

//        new Thread(new Runnable() {
//            public void run() {
//                Handler handler = new Handler(Looper.getMainLooper());
//            }
//        }).start();




        return data;
    }

    public List<CityItem> loadJSONFromAsset(Context context) {
        List<CityItem> cityItems = new ArrayList<>();
        try {
            final Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.city_list);

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            List<CityItem> cityItemList = new ArrayList<CityItem>();
            reader.beginArray();
            while (reader.hasNext()) {
                CityItem message =  new Gson().fromJson(reader, CityItem.class);
                cityItemList.add(message);
            }
            reader.endArray();
            reader.close();
            return cityItemList;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        Context context;
        MutableLiveData<List<CityItem>> data;
         List<CityItem> cityItemList;

        public AsyncCaller(MutableLiveData<List<CityItem>> data, Context context) {
            this.data = data;
            this.context  = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cityItemList = new ArrayList<>();
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            final MutableLiveData<List<CityItem>> data = new MutableLiveData<>();
            cityItemList = loadJSONFromAsset(context);
//            Handler mHandler = new Handler();
//            mHandler.post(new Runnable() {
//                @Override
//                public void run () {
//                    data.setValue(cityItemList);
//                }
//            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            data.setValue(cityItemList);
            //this method will be running on UI thread
        }

    }
}
