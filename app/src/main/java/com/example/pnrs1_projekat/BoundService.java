package com.example.pnrs1_projekat;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BoundService extends Service {
    private static final long PERIOD = 5*1000L;
    private BoundServiceExample mBounderExample = null;
    private final IBinder mBinder = new LocalBinder();
    private boolean serviceActive;
    HttpHelper httpHelper;
    private String[] acceptedData = new String[9];
    public String nTemp;

    public BoundService() {
    }

    public class LocalBinder extends Binder {
        BoundService getService() {
            // Return this instance of BoundService so clients can call public methods
            return BoundService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate(){
        if (mBounderExample == null) {
            mBounderExample = new BoundServiceExample();
        }
        super.onCreate();
        mBounderExample.start();
        serviceActive = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBounderExample.stop();
        serviceActive = false;
    }
    public void start(){
        serviceActive = true;
        mBounderExample.start();
    }
    public void stop(){
        serviceActive = false;
        mBounderExample.stop();
    }
    public boolean getServiceStatus(){
        return serviceActive;
    }
    private class BoundServiceExample implements Runnable {
        private Handler mHandler;
        private boolean mRun = false;

        public BoundServiceExample() {
            mHandler = new Handler(getMainLooper());
        }

        public void start() {
            mRun = true;
            mHandler.postDelayed(this, PERIOD);
        }

        public void stop() {
            mRun = false;
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if (!mRun) {
                return;
            }

            //temp.getDataFromInternet();
            new Thread(new Runnable() {
                public void run() {
                    String GET_CITY = "https://api.openweathermap.org/data/2.5/weather?q=";
                    String API_KEY = "&APPID=6e2c4ea501a981241b6224f707c04e49";
                    String sTemperature = "", sWind = "", sPressure = "", sHumidity = "", sSunRise = "", sSunSet = "", direction = "";
                    String time = "", time0 = "", today = "";
                    int sWindDir;

                    httpHelper = new HttpHelper();
                    try {
                        final String FINAL_URL = GET_CITY + "Novi Sad" + "&units=metric" + API_KEY;
                        JSONObject jsonobject = httpHelper.getJSONObjectFromURL(FINAL_URL);
                        JSONObject main = (JSONObject) jsonobject.get("main");
                        JSONObject sys = (JSONObject) jsonobject.get("sys");
                        JSONObject wind = (JSONObject) jsonobject.get("wind");

                        sTemperature = main.getString("temp");
                        nTemp = sTemperature;
                        sHumidity = main.getString("humidity");
                        sPressure = main.getString("pressure");
                        sSunRise = sys.getString("sunrise");
                        sSunSet = sys.getString("sunset");

                        TimeZone tz = TimeZone.getTimeZone("GMT+2");
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        df.setTimeZone(tz);

                        Date sunRiseDate = new Date(Integer.parseInt(sSunRise) * 1000L);
                        time = df.format(sunRiseDate);

                        Date sunSetDate = new Date(Integer.parseInt(sSunSet) * 1000L);
                        time0 = df.format(sunSetDate);

                        try {
                            sWind = wind.getString("speed");
                        } catch (JSONException e) {
                            sWind = "0";
                        } catch (Exception e) {
                            sWind = "0";
                        }

                        try {
                            sWindDir = Integer.parseInt(wind.getString("deg"));

                            if (sWindDir > 337 || sWindDir <= 22) direction = "Sever";
                            if (sWindDir > 22 || sWindDir <= 67) direction = "Sever-Istok";
                            if (sWindDir > 67 || sWindDir <= 112) direction = "Istok";
                            if (sWindDir > 112 || sWindDir <= 157) direction = "Jug-Istok";
                            if (sWindDir > 157 || sWindDir <= 202) direction = "Jug";
                            if (sWindDir > 202 || sWindDir <= 247) direction = "Jug-Zapad";
                            if (sWindDir > 247 || sWindDir <= 292) direction = "Zapad";
                            if (sWindDir > 292 || sWindDir <= 337) direction = "Sever-Zapad";

                        } catch (JSONException e) {
                            direction = "Nema informacija";
                        } catch (Exception e) {
                            direction = "Nema informacija";
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Date date = Calendar.getInstance().getTime();
                    today = new SimpleDateFormat("dd.MM.yyyy.").format(date);

                    ContentValues values = new ContentValues();
                    values.put(ElementDbHelper.COLUMN_DATE, today);
                    values.put(ElementDbHelper.COLUMN_CITY, "Novi Sad");
                    values.put(ElementDbHelper.COLUMN_TEMPERATURE, sTemperature);
                    values.put(ElementDbHelper.COLUMN_PREASSURE, sPressure);
                    values.put(ElementDbHelper.COLUMN_HUMIDITY, sHumidity);
                    values.put(ElementDbHelper.COLUMN_SUNRISE, time);
                    values.put(ElementDbHelper.COLUMN_SUNSET, time0);
                    values.put(ElementDbHelper.COLUMN_WIND_SPEED, sWind);
                    values.put(ElementDbHelper.COLUMN_WIND_DIRECTION, direction);

                    acceptedData[0] = today;
                    acceptedData[1] = "Novi Sad";
                    acceptedData[2] = sTemperature;
                    acceptedData[3] = sPressure;
                    acceptedData[4] = sHumidity;
                    acceptedData[5] = time;
                    acceptedData[6] = time0;
                    acceptedData[7] = sWind;
                    acceptedData[8] = direction;


                    DetailsActivity.mDbHelper.insert(values);
                }

            }).start();
            Log.d("test", "log1");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(BoundService.this, MainActivity.CHANNEL_ID);
            builder.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("PantelaWeather")
                    .setSmallIcon(R.drawable.update)
                    .setContentTitle("Temperatura azurirana")
                    .setContentText("Novi Sad " + nTemp + " °C")
                    .setContentInfo("info")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Log.d("test", "log6");

            NotificationManager notificationManager = (NotificationManager) BoundService.this.getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());

            Log.d("test", "Hello from Runnable");
            mHandler.postDelayed(this, PERIOD);
        }
    }
}
