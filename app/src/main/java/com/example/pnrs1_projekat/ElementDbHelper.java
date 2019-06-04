package com.example.pnrs1_projekat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ElementDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Weather";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_CITY = "City";
    public static final String COLUMN_TEMPERATURE = "Temperature";
    public static final String COLUMN_PREASSURE = "Preassure";
    public static final String COLUMN_HUMIDITY = "Humidity";
    public static final String COLUMN_SUNRISE = "Sunrise";
    public static final String COLUMN_SUNSET = "Sunset";
    public static final String COLUMN_WIND_SPEED = "WindSpeed";
    public static final String COLUMN_WIND_DIRECTION = "WindDirection";

    public ElementDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_TEMPERATURE + " TEXT, " +
                COLUMN_PREASSURE + " TEXT, " +
                COLUMN_HUMIDITY + " TEXT, " +
                COLUMN_SUNRISE + " TEXT, " +
                COLUMN_SUNSET + " TEXT, " +
                COLUMN_WIND_SPEED + " TEXT, " +
                COLUMN_WIND_DIRECTION + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        close();
    }
    public WeatherAttributes[] readWeather(String city) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?",
                new String[] {city}, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        WeatherAttributes[] weatherAttributes = new WeatherAttributes[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            weatherAttributes[i++] = createWeather(cursor);
        }

        close();
        return weatherAttributes;

    }

    public void deleteCity(String city) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CITY + "=?", new String[] {city});
        close();
    }
    private WeatherAttributes createWeather(Cursor cursor) {
        String cityName, date, temperature, humidity, pressure, sunRise, sunSet, windSpeed, windDirection;

        cityName = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_CITY));
        date = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_DATE));
        temperature = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_TEMPERATURE));
        humidity = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_HUMIDITY));
        pressure = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_PREASSURE));
        sunRise = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_SUNRISE));
        sunSet = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_SUNSET));
        windSpeed = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_WIND_SPEED));
        windDirection = cursor.getString(cursor.getColumnIndex(ElementDbHelper.COLUMN_WIND_DIRECTION));

        return new WeatherAttributes(cityName, date, temperature, humidity, pressure, sunRise, sunSet, windSpeed, windDirection);
    }
}
