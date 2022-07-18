package com.example.weatherapp.model;

public class City {
    String name,district;

    public City(String name, String district) {
        this.name = name;
        this.district = district;
    }

    public City() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
