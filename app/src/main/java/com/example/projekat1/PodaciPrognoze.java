package com.example.projekat1;

import java.lang.ref.SoftReference;

public class PodaciPrognoze {

    private String mDate, mCity, mSunSet, mSunRise , mWindDirection, mHummidity, mPressure , mWindSpeed;
    private double mTemperature;

    public PodaciPrognoze(String mDate, String mCity, String mSunSet, String mSunRise, String mWindDirection, double mTemperature, String mWindSpeed, String mHummidity, String mPressure) {
        this.mDate = mDate;
        this.mCity = mCity;
        this.mSunSet = mSunSet;
        this.mSunRise = mSunRise;
        this.mWindDirection = mWindDirection;
        this.mTemperature = mTemperature;
        this.mWindSpeed = mWindSpeed;
        this.mHummidity = mHummidity;
        this.mPressure = mPressure;
    }



    public String getmDate() {
        return mDate;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmSunSet() {
        return mSunSet;
    }

    public String getmSunRise() {
        return mSunRise;
    }

    public String getmWindDirection() {
        return mWindDirection;
    }

    public double getmTemperature() {
        return mTemperature;
    }

    public String getmWindSpeed() {
        return mWindSpeed;
    }

    public String getmHummidity() {
        return mHummidity;
    }

    public String getmPressure() {
        return mPressure;
    }
}
