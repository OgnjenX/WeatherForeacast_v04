package com.example.weatherforecast;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "weather";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_CITY = "City";
    public static final String COLUMN_TEMPERATURE = "Temperature";
    public static final String COLUMN_PRESSURE = "Pressure";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_SUNRISE = "Sunrise";
    public static final String COLUMN_SUNSET = "Sunset";
    public static final String COLUMN_WIND_SPEED = "WindSpeed";
    public static final String COLUMN_WIND_DIRECTION = "WindDirection";

    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_TEMPERATURE + " TEXT, " +
                COLUMN_PRESSURE + " TEXT, " +
                COLUMN_HUMIDITY + " TEXT, " +
                COLUMN_SUNRISE + " TEXT, " +
                COLUMN_SUNSET + " TEXT, " +
                COLUMN_WIND_SPEED + " TEXT, " +
                COLUMN_WIND_DIRECTION + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
