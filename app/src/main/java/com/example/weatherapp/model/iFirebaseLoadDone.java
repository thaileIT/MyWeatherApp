package com.example.weatherapp.model;

import java.util.List;

public interface iFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<City> cityList);
    void onFirebaseLoadFailed(String message);
}
