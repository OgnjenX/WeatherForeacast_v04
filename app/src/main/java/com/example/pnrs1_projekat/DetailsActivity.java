package com.example.pnrs1_projekat;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {

    public TextView location;
    public TextView dateTextView;
    public TextView textViewLastUpdate;
    public Button buttonStatistics;
    public ImageButton buttonUpdate;
    public String s;
    public FrameLayout frameLayout;
    public LinearLayout temperatureLayout, sunRiseSetLayout, windLayout;
    public Button temperatureButton, sunRiseSetButton, windButton;
    public Spinner unitForDegrees;
    public TextView temperature, windSpeed, pressure, humidity, sunRise, sunSet, windDir;

    ////////HTTP//////
    public static HttpHelper httpHelper;
    public static String GET_CITY = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String API_KEY = "&APPID=6e2c4ea501a981241b6224f707c04e49";
    public static String city = null;
    String sTemperature;
    String sWind;
    String sPressure;
    String sHumidity;
    String sSunRise;
    String sSunSet;
    int sWindDir;
    String direction = null;
    String today, lastUpdateDay;
    String time, time0;

    public static ElementDbHelper mDbHelper;
    private Cursor cursor;
    private WeatherAttributes[] weatherAttributes;
    private Bundle b;

    Intent serviceIntent;
    private BoundService mService;
    public static boolean mBound = false;
    private Button stopServiceButton, startServiceButton;

    //zadatak6
    MyNDK ndk = new MyNDK();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /*android servisi*/
        startServiceButton = findViewById(R.id.startServiceButton);
        stopServiceButton = findViewById(R.id.stopServiceButton);
        startServiceButton.setOnClickListener(this);
        stopServiceButton.setOnClickListener(this);

        /* LOKACIJA */
        location = findViewById(R.id.location);
        Intent myIntent = getIntent();
        b = myIntent.getExtras();
        s = b.getString("location");
        city = s;
        s = location.getText() + " " + s;
        location.setText(s);

        /* DATUM */
        Date date = Calendar.getInstance().getTime();
        today = new SimpleDateFormat("dd.MM.yyyy.").format(date);
        dateTextView = findViewById(R.id.dateTextView);

        temperatureLayout = findViewById(R.id.temperatureLayout);
        sunRiseSetLayout = findViewById(R.id.sunRiseSetLayout);
        windLayout = findViewById(R.id.windLayout);
        frameLayout = findViewById(R.id.frameLayout);

        temperatureLayout.setVisibility(GONE);
        sunRiseSetLayout.setVisibility(GONE);
        windLayout.setVisibility(GONE);

        temperatureButton = findViewById(R.id.temperatureButton);
        sunRiseSetButton = findViewById(R.id.sunRiseSetButton);
        windButton = findViewById(R.id.windButton);
        buttonUpdate = findViewById(R.id.imageButtonUpdate);
        buttonStatistics = findViewById(R.id.buttonStatistics);

        temperatureButton.setOnClickListener(this);
        sunRiseSetButton.setOnClickListener(this);
        windButton.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonStatistics.setOnClickListener(this);

        temperature = findViewById(R.id.temperature);
        windSpeed = findViewById(R.id.windSpeedTextView);
        pressure = findViewById(R.id.pressureTextView);
        humidity = findViewById(R.id.humidityTextView);
        sunRise = findViewById(R.id.sunRiseTextView);
        sunSet = findViewById(R.id.sunSetTextView);
        windDir = findViewById(R.id.windDirectionTextView);
        textViewLastUpdate = findViewById(R.id.textViewLastUpdate);


        //ContentResolver resolver = getContentResolver();
        //Cursor cursor = resolver.query(Uri.parse(ElementProvider.CONTENT_URI + "/" + city), null, ElementDbHelper.COLUMN_CITY+"=?", null,
        //        null);
        httpHelper = new HttpHelper();

        mDbHelper = new ElementDbHelper(this);
/*
        ContentValues values = new ContentValues();

        values.put(ElementDbHelper.COLUMN_DATE, "11.05.2019.");
        values.put(ElementDbHelper.COLUMN_CITY, "Belgrade");
        values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
        values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
        values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
        values.put(ElementDbHelper.COLUMN_SUNRISE, time);
        values.put(ElementDbHelper.COLUMN_SUNSET, time0);
        values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
        values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);
        mDbHelper.insert(values);
        values.clear();
        values.put(ElementDbHelper.COLUMN_DATE, "12.05.2019.");
        values.put(ElementDbHelper.COLUMN_CITY, "Belgrade");
        values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
        values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
        values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
        values.put(ElementDbHelper.COLUMN_SUNRISE, time);
        values.put(ElementDbHelper.COLUMN_SUNSET, time0);
        values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
        values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);
        mDbHelper.insert(values);
        values.clear();
        values.put(ElementDbHelper.COLUMN_DATE, "13.05.2019.");
        values.put(ElementDbHelper.COLUMN_CITY, "Belgrade");
        values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
        values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
        values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
        values.put(ElementDbHelper.COLUMN_SUNRISE, time);
        values.put(ElementDbHelper.COLUMN_SUNSET, time0);
        values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
        values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);
        mDbHelper.insert(values);
        values.clear();
        values.put(ElementDbHelper.COLUMN_DATE, "14.05.2019.");
        values.put(ElementDbHelper.COLUMN_CITY, "Belgrade");
        values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
        values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
        values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
        values.put(ElementDbHelper.COLUMN_SUNRISE, time);
        values.put(ElementDbHelper.COLUMN_SUNSET, time0);
        values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
        values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);
        mDbHelper.insert(values);
        values.clear();
        values.put(ElementDbHelper.COLUMN_DATE, "15.05.2019.");
        values.put(ElementDbHelper.COLUMN_CITY, "Belgrade");
        values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
        values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
        values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
        values.put(ElementDbHelper.COLUMN_SUNRISE, time);
        values.put(ElementDbHelper.COLUMN_SUNSET, time0);
        values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
        values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);
        mDbHelper.insert(values);
        values.clear();
        values.put(ElementDbHelper.COLUMN_DATE, "16.05.2019.");
        values.put(ElementDbHelper.COLUMN_CITY, "Belgrade");
        values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
        values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
        values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
        values.put(ElementDbHelper.COLUMN_SUNRISE, time);
        values.put(ElementDbHelper.COLUMN_SUNSET, time0);
        values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
        values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);
        mDbHelper.insert(values);
        values.clear();
        */
        try {
            weatherAttributes = mDbHelper.readWeather(city);
            WeatherAttributes temp = weatherAttributes[weatherAttributes.length - 1];

            dateTextView.setText(dateTextView.getText().toString() + " " + temp.getDate());
            temperature.setText(temp.getTemperature() + " °C");
            humidity.setText(/*R.string.airHumidityActivityDetails*/"Vlaznost vazduha: " + temp.getHumidity() + " %");
            pressure.setText("Pritisak: " + temp.getPressure() + " mb");
            sunRise.setText("Izlazak sunca: " + temp.getSunRise() + " h");
            sunSet.setText("Izlazak sunca: " + temp.getSunSet() + " h");
            windSpeed.setText("Brzina: " + temp.getWindSpeed() + " m/s");
            windDir.setText("Pravac: " + temp.getWindDirection());

            if (today.equals(temp.getDate())) {
                textViewLastUpdate.setVisibility(View.INVISIBLE);
                buttonUpdate.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            dateTextView.setText(dateTextView.getText().toString() + " " + today);
            textViewLastUpdate.setVisibility(GONE);
            buttonUpdate.setVisibility(GONE);

            getDataFromInternet();
        }

        unitForDegrees = findViewById(R.id.spinnerJedinica);
        String[] items = new String[]{"°C", "°F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        unitForDegrees.setAdapter(adapter);

        unitForDegrees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private String selecteditem, previousItem = null;
            private double degrees = 0;

            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                selecteditem = adapter.getItemAtPosition(i).toString();
                if (selecteditem == "°C" && previousItem == "°F") {
                    degrees = Double.parseDouble(temperature.getText().toString().substring(0, temperature.getText().toString().length() - 3));

                    degrees = ndk.convertDegrees(degrees, 0);
                    Log.d("test", String.valueOf(degrees));

                    degrees = round(degrees, 2);
                    temperature.setText(String.valueOf(degrees) + " °C");
                    previousItem = "°C";
                }
                if (selecteditem == "°F") {
                    degrees = Double.parseDouble(temperature.getText().toString().substring(0, temperature.getText().toString().length() - 3));

                    degrees = ndk.convertDegrees(degrees, 1);
                    Log.d("test", String.valueOf(degrees));

                    degrees = round(degrees, 2);
                    temperature.setText(String.valueOf(degrees) + " °F");
                    previousItem = "°F";
                }
            }

            private double round(double value, int places) {
                if (places < 0) throw new IllegalArgumentException();

                long factor = (long) Math.pow(10, places);
                value = value * factor;
                long tmp = Math.round(value);
                return (double) tmp / factor;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

        @Override
        public void onClick(View v) {
            serviceIntent = new Intent(this, BoundService.class);
            switch (v.getId()){
                case R.id.temperatureButton:
                    temperatureLayout.setVisibility(v.VISIBLE);
                    sunRiseSetLayout.setVisibility(v.INVISIBLE);
                    windLayout.setVisibility(v.INVISIBLE);
                    break;
                case R.id.sunRiseSetButton:
                    temperatureLayout.setVisibility(v.INVISIBLE);
                    sunRiseSetLayout.setVisibility(v.VISIBLE);
                    windLayout.setVisibility(v.INVISIBLE);
                    break;
                case R.id.windButton:
                    temperatureLayout.setVisibility(v.INVISIBLE);
                    sunRiseSetLayout.setVisibility(v.INVISIBLE);
                    windLayout.setVisibility(v.VISIBLE);
                    break;
                case R.id.imageButtonUpdate:
                    dateTextView.setText("Datum: " + today);
                    textViewLastUpdate.setVisibility(v.INVISIBLE);
                    buttonUpdate.setVisibility(v.INVISIBLE);
                    getDataFromInternet();
                    break;
                case R.id.startServiceButton:
                    startService(serviceIntent);
                    if(!bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)) {
                        Log.d("Android servis", "Bind Failed!");
                    }
                    break;
                case R.id.stopServiceButton:
                    if (mService != null){
                        Log.d("Android servis", "unbindService on button click");
                        unbindService(this);
                        mService = null;
                    } else {
                        Log.d("Android servis", "service is null");
                    }
                    stopService(serviceIntent);
                    break;
                case R.id.buttonStatistics:

                    WeatherAttributes[] temp = mDbHelper.readWeather(city);
                    String[] stringList = new String[22];
                    int l = weatherAttributes.length-1;
                    int j = 1;
                    for (int i=l; i>=l-6; i--){
                        switch (j) {
                            case 1:
                                stringList[0] = weatherAttributes[i].getTemperature();
                                stringList[1] = weatherAttributes[i].getPressure();
                                stringList[2] = weatherAttributes[i].getHumidity();
                                break;
                            case 2:
                                stringList[3] = weatherAttributes[i].getTemperature();
                                stringList[4] = weatherAttributes[i].getPressure();
                                stringList[5] = weatherAttributes[i].getHumidity();
                                break;
                            case 3:
                                stringList[6] = weatherAttributes[i].getTemperature();
                                stringList[7] = weatherAttributes[i].getPressure();
                                stringList[8] = weatherAttributes[i].getHumidity();
                                break;
                            case 4:
                                stringList[9] = weatherAttributes[i].getTemperature();
                                stringList[10] = weatherAttributes[i].getPressure();
                                stringList[11] = weatherAttributes[i].getHumidity();
                                break;
                            case 5:
                                stringList[12] = weatherAttributes[i].getTemperature();
                                stringList[13] = weatherAttributes[i].getPressure();
                                stringList[14] = weatherAttributes[i].getHumidity();
                                break;
                            case 6:
                                stringList[15] = weatherAttributes[i].getTemperature();
                                stringList[16] = weatherAttributes[i].getPressure();
                                stringList[17] = weatherAttributes[i].getHumidity();
                                break;
                            case 7:
                                stringList[18] = weatherAttributes[i].getTemperature();
                                stringList[19] = weatherAttributes[i].getPressure();
                                stringList[20] = weatherAttributes[i].getHumidity();
                                break;
                            default:
                        }
                        j++;
                    }

                    stringList[21] = city;
                    Intent intent = new Intent(getApplicationContext(),StatisticsActivity.class);
                    Bundle b = new Bundle();
                    b.putStringArray("city", stringList);
                    intent.putExtras(b);
                    startActivity(intent);
                    break;
                default:
            }
        }



    public void getDataFromInternet(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    final String FINAL_URL = GET_CITY + city + "&units=metric" + API_KEY;
                    JSONObject jsonobject = httpHelper.getJSONObjectFromURL(FINAL_URL);
                    JSONObject main = (JSONObject) jsonobject.get("main");
                    JSONObject sys = (JSONObject) jsonobject.get("sys");
                    JSONObject wind = (JSONObject) jsonobject.get("wind");

                    sTemperature = main.getString("temp");
                    sHumidity = main.getString("humidity");
                    sPressure = main.getString("pressure");
                    sSunRise = sys.getString("sunrise");
                    sSunSet = sys.getString("sunset");

                    TimeZone tz = TimeZone.getTimeZone("GMT+2");
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                    df.setTimeZone(tz);

                    Date sunRiseDate = new java.util.Date(Integer.parseInt(sSunRise)*1000L);
                    time = df.format(sunRiseDate);

                    Date sunSetDate = new java.util.Date(Integer.parseInt(sSunSet)*1000L);
                    time0 = df.format(sunSetDate);

                    try{
                        sWind = wind.getString("speed");
                    }catch(JSONException e) {
                        sWind = "0";
                    }catch (Exception e){
                        sWind = "0";
                    }

                    try{
                        sWindDir = Integer.parseInt(wind.getString("deg"));

                        if(sWindDir > 337 || sWindDir <= 22) direction = "Sever";
                        if(sWindDir > 22 || sWindDir <= 67) direction = "Sever-Istok";
                        if(sWindDir > 67 || sWindDir <= 112) direction = "Istok";
                        if(sWindDir > 112 || sWindDir <= 157) direction = "Jug-Istok";
                        if(sWindDir > 157 || sWindDir <= 202) direction = "Jug";
                        if(sWindDir > 202 || sWindDir <= 247) direction = "Jug-Zapad";
                        if(sWindDir > 247 || sWindDir <= 292) direction = "Zapad";
                        if(sWindDir > 292 || sWindDir <= 337) direction = "Sever-Zapad";

                    }catch(JSONException e) {
                        direction = "Nema informacija";
                    }catch (Exception e){
                        direction = "Nema informacija";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        temperature.setText(sTemperature + " °C");
                        humidity.setText("Vlaznost vazduha: " + sHumidity + " %");
                        pressure.setText("Pritisak: " + sPressure + " mb");
                        sunRise.setText("Izlazak sunca: " + time + " h");
                        sunSet.setText("Izlazak sunca: " + time0 + " h");
                        windSpeed.setText("Brzina: " + sWind + " m/s");
                        windDir.setText("Pravac: " + direction);
                    }
                });

                ContentValues values = new ContentValues();
                values.put(ElementDbHelper.COLUMN_DATE, today);
                values.put(ElementDbHelper.COLUMN_CITY, city);
                values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
                values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
                values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
                values.put(ElementDbHelper.COLUMN_SUNRISE, time);
                values.put(ElementDbHelper.COLUMN_SUNSET, time0);
                values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
                values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);

                mDbHelper.insert(values);

                //ContentResolver resolver = getContentResolver();
                //resolver.insert(ElementProvider.CONTENT_URI, values);
            }
        }).start();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if(mService == null){
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mBound = false;
    }
}