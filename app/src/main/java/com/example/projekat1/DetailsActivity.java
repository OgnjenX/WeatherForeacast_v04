package com.example.projekat1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;


public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private Spinner dropdown;
    TextView lokacija, dan_tv, temperatura_txv, vetar_tv, iz_sunca_tv , last_updated_txv;
    Calendar dan;
    LinearLayout temperatura_l, vetar_l, izlazak_zalazak_l;
    String mDate;
    Button stop_notif_btn;


    Button temperatura, izlazak_zalazak, vetar, statistika;
    ImageButton image_btn;
    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String GRAD ;
    public static String KEY = "&APPID=ba47633e62262a2fa2ffd72c835c3c6e&units=metric";
    public String GET_WEATHER ;


    private Context mContext;
    private HttpHelper mHttpHelper;

    //data base polja
    private DBHelper mDBHelper;

    //service polja
    WeatherService mService;
    boolean mBound = false;


    //metode za bindovanje servisa
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, WeatherService.class);
        Bundle b = new Bundle();
        b.putString("Grad", GRAD);
        intent.putExtras(b);
        bindService(intent,connection,Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherService.LocalBinder binder = (WeatherService.LocalBinder)service;
            mService = binder.getService();
            mBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBound == true) {
            unbindService(connection);
            mBound = false;
        }
    }
    ///////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        lokacija = findViewById(R.id.lokacija);
        final Bundle blokacija = getIntent().getExtras();
        lokacija.setText("Lokacija: " + blokacija.get("grad"));
        GRAD =  blokacija.get("grad").toString();
        GET_WEATHER = BASE_URL + GRAD + KEY;



        Log.d("asas",GET_WEATHER);


        //Buttons seting id and listeners
        temperatura = findViewById(R.id.temperatura);
        temperatura.setOnClickListener(this);

        izlazak_zalazak = findViewById(R.id.izlazak_zalazak);
        izlazak_zalazak.setOnClickListener(this);

        vetar = findViewById(R.id.vetar);
        vetar.setOnClickListener(this);

        statistika = findViewById(R.id.statistika_btn);
        statistika.setOnClickListener(this);

        image_btn = findViewById(R.id.image_btn_id);
        image_btn.setOnClickListener(this);



        //TextViews seting id
        dan_tv = findViewById(R.id.dan);
        temperatura_txv = findViewById(R.id.temperatura_tv);
        vetar_tv = findViewById(R.id.vetar_tv);
        iz_sunca_tv = findViewById(R.id.iz_sunca_tv);
        last_updated_txv = findViewById(R.id.Last_updated_txv);



        dan = Calendar.getInstance();
       /* final String[] str = new String[] {"Ponedeljak", "Utorak" , "Sreda", "Cetrvrtak", "Petak ", "Subota", "Nedelja"};
        dan_tv.setText("Dan: "+ str[dan.get(Calendar.DAY_OF_WEEK)-2]);*/

        SimpleDateFormat date = new SimpleDateFormat("dd MMM yyyy");
        dan_tv.setText("Datum: " + date.format(dan.getTime()));
        mDate = date.format(dan.getTime()).toString();

       /* private ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }*/


        dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"°C", "°F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);


        temperatura_l = findViewById(R.id.temperatura_layout);
        vetar_l = findViewById(R.id.vetar_layout);
        izlazak_zalazak_l = findViewById(R.id.izlazak_zalazak_layout);

        temperatura_l.setVisibility(View.INVISIBLE);
        izlazak_zalazak_l.setVisibility(View.INVISIBLE);
        vetar_l.setVisibility(View.INVISIBLE);

        //inicijalizacija Helper-a
        //HTTP
        mHttpHelper = new HttpHelper();

        //DATA BASE - DB Helpera
        mDBHelper = new DBHelper(this);

       /* for(int i = 7; i <= 14; i++) {
            String i_string = Integer.toString(i);
            PodaciPrognoze pp = new PodaciPrognoze(i+".5.2019", "Novi Sad", "20.01","5.00", "zapad", 10+i, "0"+i, "43","1022");
            mDBHelper.insert(pp);

        }*/


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
            //preuzimanje iz JSON objekata

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
            final String temp = mainobject.get("temp").toString();
            final String pressure = mainobject.get("pressure").toString();
            final String humidity = mainobject.get("humidity").toString();

            runOnUiThread(new Runnable() {
                @Override

                public void run() {

                    selectValue(dropdown, "°C");
                    double temp1 = Double.parseDouble(temp);
                    int temp2 =(int) temp1;

                            /*temperatura_txv.setText("Temperatura: " + temp2 + "\nPritisak: " + pressure + "kPa" + "\nVlažnost vazduha: " + humidity + " %");
                            iz_sunca_tv.setText("Izlazak sunca: " + izlazak + "\n\nZalazak sunca: " + zalazak);
                            vetar_tv.setText("Brzina vetra: " + speed + " m/s" + "\nPravac: " + odakle_duva );*/

                    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = parent.getItemAtPosition(position).toString();
                            if(selectedItem.equals("°C")){
                                double temp1 = Double.parseDouble(temp);
                                int temp2 =(int) temp1;
                                //temperatura_txv.setText("Temperatura: " + temp2 + "\nPritisak: " + pressure + "hPa" + "\nVlažnost vazduha: " + humidity + " %");
                            }else{
                                double temp1 = Double.parseDouble(temp);
                                temp1 = temp1*(9/5) +32;
                                int temp2 =(int) temp1;
                                //temperatura_txv.setText("Temperatura: " + temp2 + "\nPritisak: " + pressure + "kPa" + "\nVlažnost vazduha: " + humidity + " %");
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                            /*
                                Ubacivanje podataka u tabelu podataka
                                INSERT METODA

                             */

                    PodaciPrognoze pp = new PodaciPrognoze(mDate, blokacija.get("grad").toString(), zalazak, izlazak, odakle_duva, temp1, speed, humidity, pressure);
                    mDBHelper.insert(pp);



                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}).start();





        //Weather service notification




        stop_notif_btn = findViewById(R.id.stop_notification);
        stop_notif_btn.setOnClickListener(this);





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

    public void onClick(View v) {
        PodaciPrognoze pp = mDBHelper.getLastWeather();
//        Log.d("provera", "ime grada koje prezuima iz baze" + pp.getmCity().toString());
        switch (v.getId()){

            case R.id.stop_notification:
                Intent intent1 = new Intent(this, WeatherService.class);
                unbindService(connection);
                mBound = false;
                stopService(intent1);


                break;

            case R.id.temperatura:
                temperatura_l.setVisibility(View.VISIBLE);
                izlazak_zalazak_l.setVisibility(View.INVISIBLE);
                vetar_l.setVisibility(View.INVISIBLE);
                last_updated_txv.setText("(Last updated)  " );



                temperatura_txv.setText("Temperatura: " + pp.getmTemperature() + "\nPritisak: " + pp.getmPressure()+ "kPa" + "\nVlažnost vazduha: " + pp.getmHummidity() + " %");


                break;

            case R.id.izlazak_zalazak:

                temperatura_l.setVisibility(View.INVISIBLE);
                izlazak_zalazak_l.setVisibility(View.VISIBLE);
                vetar_l.setVisibility(View.INVISIBLE);
                iz_sunca_tv.setText("Izlazak sunca: " + pp.getmSunRise() + "\n\nZalazak sunca: " + pp.getmSunSet());

                last_updated_txv.setText("(Last updated) " );


                break;

            case R.id.vetar:
                temperatura_l.setVisibility(View.INVISIBLE);
                izlazak_zalazak_l.setVisibility(View.INVISIBLE);
                vetar_l.setVisibility(View.VISIBLE);
                last_updated_txv.setText("(Last updated) ");

                vetar_tv.setText("Brzina vetra: " + pp.getmWindSpeed() + " m/s" + "\nPravac: " + pp.getmWindDirection() );

                break;

            case R.id.statistika_btn:
                Intent intent = new Intent(this, Statistic.class);
                intent.putExtra("edit1", GRAD);
                this.startActivity(intent);

                break;


            case R.id.image_btn_id:

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //preuzimanje iz JSON objekata

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
                            final String temp = mainobject.get("temp").toString();
                            final String pressure = mainobject.get("pressure").toString();
                            final String humidity = mainobject.get("humidity").toString();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    selectValue(dropdown, "°C");
                                    double temp1 = Double.parseDouble(temp);
                                    int temp2 =(int) temp1;

                                    /*temperatura_txv.setText("Temperatura: " + temp2 + "\nPritisak: " + pressure + "kPa" + "\nVlažnost vazduha: " + humidity + " %");
                                    iz_sunca_tv.setText("Izlazak sunca: " + izlazak + "\n\nZalazak sunca: " + zalazak);
                                    vetar_tv.setText("Brzina vetra: " + speed + " m/s" + "\nPravac: " + odakle_duva );*/

                                    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            String selectedItem = parent.getItemAtPosition(position).toString();
                                            if(selectedItem.equals("°C")){
                                                double temp1 = Double.parseDouble(temp);
                                                int temp2 =(int) temp1;
                                                temperatura_txv.setText("Temperatura: " + temp2 + "\nPritisak: " + pressure + "hPa" + "\nVlažnost vazduha: " + humidity + " %");
                                            }else{
                                                double temp1 = Double.parseDouble(temp);
                                                temp1 = temp1*(9/5) +32;
                                                int temp2 =(int) temp1;
                                                temperatura_txv.setText("Temperatura: " + temp2 + "\nPritisak: " + pressure + "kPa" + "\nVlažnost vazduha: " + humidity + " %");
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                            /*
                                Ubacivanje podataka u tabelu podataka
                                INSERT METODA

                             */

                                    PodaciPrognoze pp = new PodaciPrognoze(mDate, GRAD, zalazak, izlazak, odakle_duva, temp1, speed, humidity, pressure);
                                    mDBHelper.insert(pp);

                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();



                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
