package com.example.pnrs1_projekat;

import android.util.Log;
import android.widget.Button;

public class ElementRow {
    public Button button;
    public String location;

    public ElementRow(String location1){
        location = location1;
    }

    public String getLocation() {
        return location;
    }
}
