package com.example.projekat1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "weather5.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "weather";

    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_WEEKDAY = "weekday";
    private static final String COLUMN_TEMPERATURE = "temperature";
    private static final String COLUMN_PRESSURE = "pressure";
    private static final String COLUMN_HUMIDITY = "humidity";
    private static final String COLUMN_SUNRISE = "sunrise";
    private static final String COLUMN_SUNSET = "sunset";
    private static final String COLUMN_WIND_SPEED = "wind_speed";
    private static final String COLUMN_WIND_DIR = "wind_direction";



    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_TABLE = "CREATE TABLE " + TABLE_NAME + " ("+
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_CITY + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_TEMPERATURE + " DOUBLE," +
                COLUMN_PRESSURE + " TEXT," +
                COLUMN_HUMIDITY + " TEXT," +
                COLUMN_SUNRISE + " TEXT," +
                COLUMN_SUNSET + " TEXT," +
                COLUMN_WIND_SPEED + " TEXT," +
                COLUMN_WIND_DIR + " TEXT" +
                ");";
        db.execSQL(SQL_TABLE);

    }


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(PodaciPrognoze podaci_prognoze){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_CITY, podaci_prognoze.getmCity());
        values.put(COLUMN_DATE,podaci_prognoze.getmDate());
        values.put(COLUMN_HUMIDITY, podaci_prognoze.getmHummidity());
        values.put(COLUMN_PRESSURE,  podaci_prognoze.getmPressure());
        values.put(COLUMN_SUNRISE,podaci_prognoze.getmSunRise());
        values.put(COLUMN_SUNSET,podaci_prognoze.getmSunSet());
        values.put(COLUMN_WIND_DIR,  podaci_prognoze.getmWindDirection());
        values.put(COLUMN_TEMPERATURE,podaci_prognoze.getmTemperature());
        values.put(COLUMN_WIND_SPEED,podaci_prognoze.getmWindSpeed());

        Log.d("poz", "pozdravi pre ifa za insert");
        if(searchDataBase(podaci_prognoze.getmCity(), podaci_prognoze.getmDate()) == null){
            Log.d("pozzz", "pozdravi IZ IZ IZ ifa za insert");

            db.insert(TABLE_NAME, null, values);
        }else{
            deleteFromDataBase(podaci_prognoze.getmCity(), podaci_prognoze.getmDate());
            db.insert(TABLE_NAME, null, values);
        }
        close();
    }
    public void insert_za_notifikaciju(PodaciPrognoze podaci_prognoze){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_CITY, podaci_prognoze.getmCity());
        values.put(COLUMN_DATE,podaci_prognoze.getmDate());
        values.put(COLUMN_HUMIDITY, podaci_prognoze.getmHummidity());
        values.put(COLUMN_PRESSURE,  podaci_prognoze.getmPressure());
        values.put(COLUMN_SUNRISE,podaci_prognoze.getmSunRise());
        values.put(COLUMN_SUNSET,podaci_prognoze.getmSunSet());
        values.put(COLUMN_WIND_DIR,  podaci_prognoze.getmWindDirection());
        values.put(COLUMN_TEMPERATURE,podaci_prognoze.getmTemperature());
        values.put(COLUMN_WIND_SPEED,podaci_prognoze.getmWindSpeed());

        Log.d("poz", "pozdravi pre ifa za insert notifikacije");
        Log.d("temp", "TEMPERATURA PRE INSERTA ZA NOTIFIKACIJU" + podaci_prognoze.getmTemperature());
        if(searchDataBase_za_notifikaciju(podaci_prognoze.getmCity(), podaci_prognoze.getmDate(),podaci_prognoze.getmTemperature()) == null){
            Log.d("pozzz", "pozdravi IZ IZ IZ ifa za insert notifikacijeEE");

            db.insert(TABLE_NAME, null, values);
        }
        close();
    }

    public PodaciPrognoze searchDataBase_za_notifikaciju(String grad, String datum, Double temperature){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CITY + " = \"" + grad + "\" AND "
                + COLUMN_DATE + " = \"" + datum + "\" AND "
                + COLUMN_TEMPERATURE + " = \"" + temperature +"\" ;", null, null);

        PodaciPrognoze pp;
        if(cursor.getCount() <= 0){
            Log.d("pozdrav", "pozdrav iz provera za null ako ne(valjda) postoji u bazi");
            return null;

        }else{
            cursor.moveToFirst();
            pp = napraviPodatakPrognoze(cursor);
            return pp;
        }
    }

    public PodaciPrognoze searchDataBase(String grad, String datum){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(" SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CITY + " = \"" + grad + "\" AND "
                + COLUMN_DATE + " = \"" + datum + "\" ;", null, null);

        PodaciPrognoze pp;
        if(cursor.getCount() <= 0){
            Log.d("pozdrav", "pozdrav iz provera za null ako  postoji u bazi");
            return null;

        }else{
            cursor.moveToFirst();
            pp = napraviPodatakPrognoze(cursor);
            return pp;
        }
    }




    public PodaciPrognoze napraviPodatakPrognoze(Cursor cursor){

        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        String sunSet = cursor.getString(cursor.getColumnIndex(COLUMN_SUNSET));
        String sunRise = cursor.getString(cursor.getColumnIndex(COLUMN_SUNRISE));
        String windDirection = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIR));
        double temperatura = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEMPERATURE));
        String windSpeed = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_SPEED));
        String humidity = cursor.getString(cursor.getColumnIndex(COLUMN_HUMIDITY));
        String pressure = cursor.getString(cursor.getColumnIndex(COLUMN_PRESSURE));

        PodaciPrognoze pp = new PodaciPrognoze(date, city, sunSet, sunRise, windDirection, temperatura, windSpeed, humidity, pressure);
        return pp;
    }

    public void deleteFromDataBase(String grad, String datum){
        Log.d ("pozzzzzzz", "pozdrav iz deletea");
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_CITY+" = ? AND "+COLUMN_DATE+"= ?", new String[]{grad, datum});
        Log.d ("pozzzzzzaz", "pozdrav iz posle deletaa jos dalje");
    }

    public PodaciPrognoze[] nalazenjeNizaSaIstomLok(String grad){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?",
                new String[] {grad}, null, null, null);

        if(cursor.getCount() <= 0){
            return null;
        }


        PodaciPrognoze[] all_data = new PodaciPrognoze[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            all_data[i++] = napraviPodatakPrognoze(
                    cursor);
        }
        db.close();
        return all_data;
    }

    public PodaciPrognoze readPrognoza(String location) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_CITY + "=?",
                new String[] {location}, null, null, null);
        cursor.moveToFirst();
        PodaciPrognoze pom = napraviPodatakPrognoze(cursor);

        close();
        return pom;
    }

    public StatisticInstance[] readData() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        StatisticInstance[] si = new StatisticInstance[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            PodaciPrognoze pom = napraviPodatakPrognoze(cursor);
            StatisticInstance s = new StatisticInstance(pom.getmDate(), pom.getmTemperature() ,pom.getmPressure(), pom.getmHummidity());
            si[i++] = s;
        }

        close();
        return si;
    }

    public PodaciPrognoze getLastWeather(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
        cursor.moveToLast();
        PodaciPrognoze pp = napraviPodatakPrognoze(cursor);

        close();
        return pp;
    }
}
