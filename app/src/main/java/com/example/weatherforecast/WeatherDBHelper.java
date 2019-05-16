package com.example.weatherforecast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String COLUMN_WEEKDAY = "weekday";

    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public Weather getItem(String city) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?", new String[]{city}, null, null, null, null);

        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToLast();
        Weather weather = createWeather(cursor);

        cursor.close();
        db.close();

        return weather;
    }

    public Weather getItemByWeekDay(String city, String weekday, int x) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        if (x == 0)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city + "\" AND "  + weekday + "\" ;", null, null);
        if (x == 1)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city + "\" AND "  + weekday + "\" AND " + COLUMN_TEMPERATURE + " >= 10 ;", null, null);
        if (x == 2)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + city + "\" AND "  + weekday + "\" AND " + COLUMN_TEMPERATURE + " < 10 ;", null, null);

        assert cursor != null;
        if (cursor.getCount() <= 0)
            return null;

        cursor.moveToLast();
        Weather forecast = createWeather(cursor);

        cursor.close();
        db.close();

        return forecast;
    }

    public Weather[] getItems(String gotham, int batman) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (batman == 0)
            cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?", new String[]{gotham}, null, null, COLUMN_TEMPERATURE, null);
        else if (batman == 1)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\" and " + COLUMN_TEMPERATURE + " = " +
                    "(SELECT MIN(" + COLUMN_TEMPERATURE + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\");", null, null);
        else if (batman == 2)
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\" and " + COLUMN_TEMPERATURE + " = " +
                    "(SELECT MAX(" + COLUMN_TEMPERATURE + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_CITY + " = \"" + gotham + "\");", null, null);

        assert cursor != null;
        if (cursor.getCount() <= 0)
            return null;

        Weather[] forecasts = new Weather[cursor.getCount()];

        int i = 0;
        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            forecasts[i++] = createWeather(cursor);
        }

        db.close();

        return forecasts;
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insert(Weather weather) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE,weather.getDate());
        values.put(COLUMN_CITY,weather.getCity());
        values.put(COLUMN_TEMPERATURE,weather.getTemperature());
        values.put(COLUMN_PRESSURE,weather.getPressure());
        values.put(COLUMN_HUMIDITY,weather.getHumidity());
        values.put(COLUMN_SUNRISE,weather.getSunrise());
        values.put(COLUMN_SUNSET,weather.getSunset());
        values.put(COLUMN_WIND_SPEED,weather.getWindSpeed());
        values.put(COLUMN_WIND_DIRECTION,weather.getWindDirection());

        db.insert(TABLE_NAME, null, values);
        close();
    }

    public Weather[] readWeather() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        Weather[] citys = new Weather[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            citys[i++] = createWeather(cursor);
        }

        close();
        return citys;
    }


    public Weather readWeather(String city) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?",
                new String[] {city}, null, null, null);
        cursor.moveToFirst();
        Weather student = createWeather(cursor);

        close();
        return student;
    }

    public void deleteWeather(String city) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CITY + "=?", new String[] {city});
        close();
    }

    private Weather createWeather(Cursor cursor) {
        String Date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String City = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        double Temperature = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEMPERATURE));
        double Pressure = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRESSURE));
        double Humidity = cursor.getDouble(cursor.getColumnIndex(COLUMN_HUMIDITY));
        String Sunrise = cursor.getString(cursor.getColumnIndex(COLUMN_SUNRISE));
        String Sunset = cursor.getString(cursor.getColumnIndex(COLUMN_SUNSET));
        double WindSpeed = cursor.getDouble(cursor.getColumnIndex(COLUMN_WIND_SPEED));
        String WindDirection = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIRECTION));
        String WeekDay = cursor.getString(cursor.getColumnIndex(COLUMN_WEEKDAY));

        return new Weather(Date,City,Temperature,Pressure,Humidity,Sunrise,Sunset,WindSpeed,WindDirection,WeekDay);
    }
}
