package com.example.weatherforecast;

import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Element {
    public RadioButton mRadioButton;
    public String mCityName;
    public Button mButton;

    public Element(String cityName) {
        mCityName = cityName;
    }
}
