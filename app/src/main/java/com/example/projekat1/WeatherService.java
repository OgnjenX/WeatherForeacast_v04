package com.example.projekat1;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.getIntent;
import static com.example.projekat1.DetailsActivity.BASE_URL;


public class WeatherService extends Service {
    private static final String LOG_TAG = "ExampleService";
    private static final long PERIOD = 10000L;
    private RunnableExample mRunnable;
    private Spinner dropdown;


    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String GRAD ;
    public static String KEY = "&APPID=ba47633e62262a2fa2ffd72c835c3c6e&units=metric";
    public String GET_WEATHER ;

    public String city;
    private HttpHelper mHttpHelper;
    private DBHelper mDBHelper;

    private final IBinder binder = new LocalBinder();




    public class LocalBinder extends Binder {
        WeatherService getService(){
            return WeatherService.this;
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();

        //city = getIntent().getExtras().toString();
        mRunnable = new RunnableExample();
        mHttpHelper = new HttpHelper();
        mDBHelper = new DBHelper(this);
        mRunnable.start();




    }

  /*  @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
        city = intent.getStringExtra("Grad");
       Log.d("Weather service ", "evo ga gradDDDDDDDDDDDD"+ city);
        return super.onStartCommand(intent, flags, startId);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();

        mRunnable.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        city = intent.getStringExtra("Grad");
        return binder;

    }



    private class RunnableExample implements Runnable {
        private Handler mHandler;
        private boolean mRun = false;

        public RunnableExample() {
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

            //ovde smesti preuzimanje iz JSON-a
            Log.d("ssa", "OVO JE GRADDD PRE HTTP ZAHTEVA  "+ city);
            getHTTP();
            //
            PodaciPrognoze pp = mDBHelper.getLastWeather();
            NotificationCompat.Builder b = new NotificationCompat.Builder(WeatherService.this);
            b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.simbol_weather)
                    .setTicker("{your tiny message}")
                    .setContentTitle("Temperatura je azurirana")
                    .setContentText("Trenutrno: " + pp.getmTemperature() + "°C")
                    .setContentInfo("INFO");

            NotificationManager nm = (NotificationManager) WeatherService.this.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1, b.build());

            Log.d(LOG_TAG, "Hello from Runnable");
            mHandler.postDelayed(this, PERIOD);
        }
    }


    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public void getHTTP(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                GET_WEATHER = BASE_URL + city + KEY;
                Log.d("sas", "OVO JE LINKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"+GET_WEATHER);
                try {

                    //preuzimanje iz JSON objekata
                    Log.d("s", "ovo je formiranje JSONa u Weather servicu");
                    GET_WEATHER = BASE_URL + city + KEY;
                    JSONObject jsonobject = mHttpHelper.getJSONObjectFromURL(GET_WEATHER);

                    JSONObject mainobject = jsonobject.getJSONObject("main");
                    JSONObject sysobject = jsonobject.getJSONObject("sys");
                    JSONObject windobject = jsonobject.getJSONObject("wind");

                    final String speed  = windobject.get("speed").toString();
                    double degree = windobject.getDouble("deg");
                    final String odakle_duva = stepenUStr(degree);

                    long sunce = Long.valueOf(sysobject.get("sunrise").toString())*1000;
                    Date date = new Date(sunce);
                    final String izlazak = new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date);

                    long night = Long.valueOf(sysobject.get("sunset").toString()) * 1000;
                    Date date2 = new Date(night);

                    final String zalazak = new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date2);
                    final Double temp = mainobject.getDouble("temp");
                    final String pressure = mainobject.get("pressure").toString();
                    final String humidity = mainobject.get("humidity").toString();

                    Calendar dan = Calendar.getInstance();
                    SimpleDateFormat pom = new SimpleDateFormat("dd MMM yyyy");
                    String pom1 = pom.format(date);
                    //String date1 = pom.format(dan.getTime()).toString();

                    PodaciPrognoze pp = new PodaciPrognoze(pom1, city, zalazak, izlazak, odakle_duva,temp, speed, humidity, pressure);

                    mDBHelper.insert_za_notifikaciju(pp);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public String stepenUStr(Double degree) {
        if(degree <= 22.5 && degree > 337.5){
            return "Sever";
        }
        if(degree > 22.5 && degree <= 67.5){
            return "Severo-istok";
        }
        if(degree > 67.5 && degree <= 112.5){
            return "Istok";
        }
        if(degree > 112.5 && degree <= 157.5){
            return "Jugo-istok";
        }
        if(degree > 157.5 && degree <= 202.5){
            return "Jug";
        }
        if(degree > 202.5 && degree <= 247.5){
            return "Jugo-zapad";
        }
        if(degree > 247.525 && degree <= 292.5){
            return "Zapad";
        }
        return "Severo-zapad";
    }
}

