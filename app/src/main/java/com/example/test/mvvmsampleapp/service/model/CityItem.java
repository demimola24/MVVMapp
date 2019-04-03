package com.example.test.mvvmsampleapp.service.model;

import android.widget.ArrayAdapter;

public class CityItem {
   public int id;
   public String name;

   public Coord coord;

    public Coord getCoord() {
        return coord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }



    @Override
    public String toString() {
        return name;
    }
}
