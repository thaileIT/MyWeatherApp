package com.example.weatherapp.model;

public class Weather {
    public String Day;
    public String Status;
    public String ImageStatus;
    public String Temp;

    public Weather() {
    }

    public Weather(String day, String status, String imageStatus, String temp) {
        Day = day;
        Status = status;
        ImageStatus = imageStatus;
        Temp = temp;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImageStatus() {
        return ImageStatus;
    }

    public void setImageStatus(String imageStatus) {
        ImageStatus = imageStatus;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }
}
