package com.example.projekat1;

public class StatisticInstance {

    public String mDate;
    public Double mTemperature;
    public String mPressure;
    public String mHummidity;

    public StatisticInstance(String mDate, Double mTemperature, String mPressure, String mHummidity) {
        this.mDate = mDate;
        this.mTemperature = mTemperature;
        this.mPressure = mPressure;
        this.mHummidity = mHummidity;
    }

    public String getmDate() {
        return mDate;
    }

    public Double getmTemperature() {
        return mTemperature;
    }

    public String getmPressure() {
        return mPressure;
    }

    public String getmHummidity() {
        return mHummidity;
    }
}
