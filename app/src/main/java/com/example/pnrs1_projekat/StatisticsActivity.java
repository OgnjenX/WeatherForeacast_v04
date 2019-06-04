package com.example.pnrs1_projekat;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener{

    //ElementDbHelper DATABASE 'weather.db' AS stf;

    TextView city,monday, mondayTemperature, mondayPreassure, mondayHumidity;
    TextView tuesday, tuesdayTemperature, tuesdayPreassure, tuesdayHumidity;
    TextView wednesday, wednesdayTemperature, wednesdayPreassure, wednesdayHumidity;
    TextView thursday, thursdayTemperature, thursdayPreassure, thursdayHumidity;
    TextView friday, fridayTemperature, fridayPreassure, fridayHumidity;
    TextView saturday, saturdayTemperature, saturdayPreassure, saturdayHumidity;
    TextView sunday, sundayTemperature, sundayPreassure, sundayHumidity;
    TextView dayOfMinTemperature, dayOfMaxTemperature, minTemperature, maxTemperature;
    ImageButton coldDays, warmDays;

    String sCity, sMonday, sMondayTemperature, sMondayPreassure, sMondayHumidity;
    String sTuesday, sTuesdayTemperature, sTuesdayPreassure, sTuesdayHumidity;
    String sWednesday, sWednesdayTemperature, sWednesdayPreassure, sWednesdayHumidity;
    String sThursday, sThursdayTemperature, sThursdayPreassure, sThursdayHumidity;
    String sFriday, sFridayTemperature, sFridayPreassure, sFridayHumidity;
    String sSaturday, sSaturdayTemperature, sSaturdayPreassure, sSaturdayHumidity;
    String sSunday, sSundayTemperature, sSundayPreassure, sSundayHumidity;
    String sDayOfMinTemperature, sDayOfMaxTemperature, sMinTemperature, sMaxTemperature;
    String[] weekDays = new String[] { "Ponedeljak", "Utorak","Sreda", "Cetvrtak","Petak", "Subota", "Nedelja" };


    //private ElementDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        city = findViewById(R.id.city);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);

        mondayTemperature = findViewById(R.id.mondayTemperature);
        tuesdayTemperature = findViewById(R.id.tuesdayTemperature);
        wednesdayTemperature = findViewById(R.id.wednesdayTemperature);
        thursdayTemperature = findViewById(R.id.thursdayTemperature);
        fridayTemperature = findViewById(R.id.fridayTemperature);
        saturdayTemperature = findViewById(R.id.saturdayTemperature);
        sundayTemperature = findViewById(R.id.sundayTemperature);

        mondayPreassure = findViewById(R.id.mondayPreassure);
        tuesdayPreassure = findViewById(R.id.tuesdayPreassure);
        wednesdayPreassure = findViewById(R.id.wednesdayPreassure);
        thursdayPreassure = findViewById(R.id.thursdayPreassure);
        fridayPreassure = findViewById(R.id.fridayPreassure);
        saturdayPreassure = findViewById(R.id.saturdayPreassure);
        sundayPreassure = findViewById(R.id.sundayPreassure);

        mondayHumidity = findViewById(R.id.mondayHumidity);
        tuesdayHumidity = findViewById(R.id.tuesdayHumidity);
        wednesdayHumidity = findViewById(R.id.wednesdayHumidity);
        thursdayHumidity = findViewById(R.id.thursdayHumidity);
        fridayHumidity = findViewById(R.id.fridayHumidity);
        saturdayHumidity = findViewById(R.id.saturdayHumidity);
        sundayHumidity = findViewById(R.id.sundayHumidity);

        dayOfMinTemperature = findViewById(R.id.dayOfMinTemperature);
        dayOfMaxTemperature = findViewById(R.id.dayOfMaxTemperature);
        minTemperature = findViewById(R.id.minTemperature);
        maxTemperature = findViewById(R.id.maxTemperature);

        coldDays = findViewById(R.id.coldDays);
        warmDays = findViewById(R.id.warmDays);

        coldDays.setOnClickListener(this);
        warmDays.setOnClickListener(this);

        Intent myIntent = getIntent();
        Bundle b = new Bundle();
        b = myIntent.getExtras();
        String[] stringList = new String[22];
        stringList = b.getStringArray("city");

        city.setText(stringList[21]);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayOfTheWeek = sdf.format(date);
        int d = 0, j = 0;

        switch (dayOfTheWeek){
            case "Monday":
                d = 1;
                break;
            case "Tuesday":
                d = 2;
                break;
            case "Wednesday":
                d = 3;
                break;
            case "Thursday":
                d = 4;
                break;
            case "Friday":
                d = 5;
                break;
            case "Saturday":
                d = 6;
                break;
            case "Sunday":
                d = 7;
                break;
                default:
        }
//0.1.2 danasnji dan
        ///3.4.5 jucerasnji
        //...
        for (int i=0; i<7; i++){
            switch(d){
                case 1:
                    mondayTemperature.setText(stringList[3*j] + " °C");
                    mondayPreassure.setText(stringList[3*j+1] + " mb");
                    mondayHumidity.setText(stringList[3*j+2] + "%");
                    if(j == 0){
                        monday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        mondayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        mondayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        mondayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                case 2:
                    tuesdayTemperature.setText(stringList[3*j] + " °C");
                    tuesdayPreassure.setText(stringList[3*j+1] + " mb");
                    tuesdayHumidity.setText(stringList[3*j+2] + " %");
                    if(j == 0){
                        tuesday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tuesdayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tuesdayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tuesdayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                case 3:
                    wednesdayTemperature.setText(stringList[3*j] + " °C");
                    wednesdayPreassure.setText(stringList[3*j+1] + " mb");
                    wednesdayHumidity.setText(stringList[3*j+2] + " %");
                    if(j == 0){
                        wednesday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        wednesdayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        wednesdayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        wednesdayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                case 4:
                    thursdayTemperature.setText(stringList[3*j] + " °C");
                    thursdayPreassure.setText(stringList[3*j+1] + " mb");
                    thursdayHumidity.setText(stringList[3*j+2] + " %");
                    if(j == 0){
                        thursday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        thursdayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        thursdayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        thursdayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                case 5:
                    fridayTemperature.setText(stringList[3*j] + " °C");
                    fridayPreassure.setText(stringList[3*j+1] + " mb");
                    fridayHumidity.setText(stringList[3*j+2] + " %");
                    if(j == 0){
                        friday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        fridayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        fridayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        fridayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                case 6:
                    saturdayTemperature.setText(stringList[3*j] + " °C");
                    saturdayPreassure.setText(stringList[3*j+1] + " mb");
                    saturdayHumidity.setText(stringList[3*j+2] + " %");
                    if(j == 0){
                        saturday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        saturdayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        saturdayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        saturdayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                case 7:
                    sundayTemperature.setText(stringList[3*j] + " °C");
                    sundayPreassure.setText(stringList[3*j+1] + " mb");
                    sundayHumidity.setText(stringList[3*j+2] + " %");
                    if(j == 0){
                        sunday.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        sundayTemperature.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        sundayPreassure.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        sundayHumidity.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    }
                    break;
                    default:
            }
            d--;
            if(d == 0) d = 7;
            j++;
        }

        sMinTemperature = stringList[0];
        sMaxTemperature = stringList[0];
        sDayOfMinTemperature = weekDays[(d-1)%7];
        sDayOfMaxTemperature = weekDays[(d-1)%7];

        for (int i=3; i<21; i+=3){
            if(Double.parseDouble(stringList[i]) < Double.parseDouble(sMinTemperature)){
                sMinTemperature = stringList[i];
                sDayOfMinTemperature = weekDays[(i/3+d-1)%7];
            }else if (Double.parseDouble(stringList[i]) == Double.parseDouble(sMinTemperature))
                sDayOfMinTemperature += "\n" + weekDays[(i/3+d-1)%7];

            if(Double.parseDouble(stringList[i]) > Double.parseDouble(sMaxTemperature)){
                sMaxTemperature = stringList[i];
                sDayOfMaxTemperature = weekDays[(i/3+d-1)%7];
            }else if (Double.parseDouble(stringList[i]) == Double.parseDouble(sMaxTemperature))
                sDayOfMaxTemperature += "\n" + weekDays[(i/3+d-1)%7];
        }
        dayOfMinTemperature.setText(sDayOfMinTemperature);
        dayOfMaxTemperature.setText(sDayOfMaxTemperature);
        minTemperature.setText(sMinTemperature);
        maxTemperature.setText(sMaxTemperature);

    }

    @Override
    public void onClick(View v) {
        int len;
        switch (v.getId()){
            case R.id.coldDays:
                len = mondayTemperature.getText().toString().length();
                if(Double.parseDouble(mondayTemperature.getText().toString().substring(0, len-3)) > 15){
                    mondayTemperature.setVisibility(View.INVISIBLE);
                    mondayPreassure.setVisibility(View.INVISIBLE);
                    mondayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    mondayTemperature.setVisibility(View.VISIBLE);
                    mondayPreassure.setVisibility(View.VISIBLE);
                    mondayHumidity.setVisibility(View.VISIBLE);
                }
                len = tuesdayTemperature.getText().toString().length();
                if(Double.parseDouble(tuesdayTemperature.getText().toString().substring(0, len-3)) > 15){
                    tuesdayTemperature.setVisibility(View.INVISIBLE);
                    tuesdayPreassure.setVisibility(View.INVISIBLE);
                    tuesdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    tuesdayTemperature.setVisibility(View.VISIBLE);
                    tuesdayPreassure.setVisibility(View.VISIBLE);
                    tuesdayHumidity.setVisibility(View.VISIBLE);
                }
                len = wednesdayTemperature.getText().toString().length();
                if(Double.parseDouble(wednesdayTemperature.getText().toString().substring(0, len-3)) > 15){
                    wednesdayTemperature.setVisibility(View.INVISIBLE);
                    wednesdayPreassure.setVisibility(View.INVISIBLE);
                    wednesdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    wednesdayTemperature.setVisibility(View.VISIBLE);
                    wednesdayPreassure.setVisibility(View.VISIBLE);
                    wednesdayHumidity.setVisibility(View.VISIBLE);
                }
                len = thursdayTemperature.getText().toString().length();
                if(Double.parseDouble(thursdayTemperature.getText().toString().substring(0, len-3)) > 15){
                    thursdayTemperature.setVisibility(View.INVISIBLE);
                    thursdayPreassure.setVisibility(View.INVISIBLE);
                    thursdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    thursdayTemperature.setVisibility(View.VISIBLE);
                    thursdayPreassure.setVisibility(View.VISIBLE);
                    thursdayHumidity.setVisibility(View.VISIBLE);
                }
                len = fridayTemperature.getText().toString().length();
                if(Double.parseDouble(fridayTemperature.getText().toString().substring(0, len-3)) > 15){
                    fridayTemperature.setVisibility(View.INVISIBLE);
                    fridayPreassure.setVisibility(View.INVISIBLE);
                    fridayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    fridayTemperature.setVisibility(View.VISIBLE);
                    fridayPreassure.setVisibility(View.VISIBLE);
                    fridayHumidity.setVisibility(View.VISIBLE);
                }
                len = saturdayTemperature.getText().toString().length();
                if(Double.parseDouble(saturdayTemperature.getText().toString().substring(0, len-3)) > 15){
                    saturdayTemperature.setVisibility(View.INVISIBLE);
                    saturdayPreassure.setVisibility(View.INVISIBLE);
                    saturdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    saturdayTemperature.setVisibility(View.VISIBLE);
                    saturdayPreassure.setVisibility(View.VISIBLE);
                    saturdayHumidity.setVisibility(View.VISIBLE);
                }
                len = sundayTemperature.getText().toString().length();
                if(Double.parseDouble(sundayTemperature.getText().toString().substring(0, len-3)) > 15){
                    sundayTemperature.setVisibility(View.INVISIBLE);
                    sundayPreassure.setVisibility(View.INVISIBLE);
                    sundayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    sundayTemperature.setVisibility(View.VISIBLE);
                    sundayPreassure.setVisibility(View.VISIBLE);
                    sundayHumidity.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.warmDays:
                len = mondayTemperature.getText().toString().length();
                if(Double.parseDouble(mondayTemperature.getText().toString().substring(0, len-3)) < 15){
                    mondayTemperature.setVisibility(View.INVISIBLE);
                    mondayPreassure.setVisibility(View.INVISIBLE);
                    mondayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    mondayTemperature.setVisibility(View.VISIBLE);
                    mondayPreassure.setVisibility(View.VISIBLE);
                    mondayHumidity.setVisibility(View.VISIBLE);
                }
                len = tuesdayTemperature.getText().toString().length();
                if(Double.parseDouble(tuesdayTemperature.getText().toString().substring(0, len-3)) < 15){
                    tuesdayTemperature.setVisibility(View.INVISIBLE);
                    tuesdayPreassure.setVisibility(View.INVISIBLE);
                    tuesdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    tuesdayTemperature.setVisibility(View.VISIBLE);
                    tuesdayPreassure.setVisibility(View.VISIBLE);
                    tuesdayHumidity.setVisibility(View.VISIBLE);
                }
                len = wednesdayTemperature.getText().toString().length();
                if(Double.parseDouble(wednesdayTemperature.getText().toString().substring(0, len-3)) < 15){
                    wednesdayTemperature.setVisibility(View.INVISIBLE);
                    wednesdayPreassure.setVisibility(View.INVISIBLE);
                    wednesdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    wednesdayTemperature.setVisibility(View.VISIBLE);
                    wednesdayPreassure.setVisibility(View.VISIBLE);
                    wednesdayHumidity.setVisibility(View.VISIBLE);
                }
                len = thursdayTemperature.getText().toString().length();
                if(Double.parseDouble(thursdayTemperature.getText().toString().substring(0, len-3)) < 15){
                    thursdayTemperature.setVisibility(View.INVISIBLE);
                    thursdayPreassure.setVisibility(View.INVISIBLE);
                    thursdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    thursdayTemperature.setVisibility(View.VISIBLE);
                    thursdayPreassure.setVisibility(View.VISIBLE);
                    thursdayHumidity.setVisibility(View.VISIBLE);
                }
                len = fridayTemperature.getText().toString().length();
                if(Double.parseDouble(fridayTemperature.getText().toString().substring(0, len-3)) < 15){
                    fridayTemperature.setVisibility(View.INVISIBLE);
                    fridayPreassure.setVisibility(View.INVISIBLE);
                    fridayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    fridayTemperature.setVisibility(View.VISIBLE);
                    fridayPreassure.setVisibility(View.VISIBLE);
                    fridayHumidity.setVisibility(View.VISIBLE);
                }
                len = saturdayTemperature.getText().toString().length();
                if(Double.parseDouble(saturdayTemperature.getText().toString().substring(0, len-3)) < 15){
                    saturdayTemperature.setVisibility(View.INVISIBLE);
                    saturdayPreassure.setVisibility(View.INVISIBLE);
                    saturdayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    saturdayTemperature.setVisibility(View.VISIBLE);
                    saturdayPreassure.setVisibility(View.VISIBLE);
                    saturdayHumidity.setVisibility(View.VISIBLE);
                }
                len = sundayTemperature.getText().toString().length();
                if(Double.parseDouble(sundayTemperature.getText().toString().substring(0, len-3)) < 15){
                    sundayTemperature.setVisibility(View.INVISIBLE);
                    sundayPreassure.setVisibility(View.INVISIBLE);
                    sundayHumidity.setVisibility(View.INVISIBLE);
                }else{
                    sundayTemperature.setVisibility(View.VISIBLE);
                    sundayPreassure.setVisibility(View.VISIBLE);
                    sundayHumidity.setVisibility(View.VISIBLE);
                }
                break;
                default:
        }
    }
}
