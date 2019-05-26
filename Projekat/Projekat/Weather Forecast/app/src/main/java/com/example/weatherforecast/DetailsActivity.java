package com.example.weatherforecast;

import android.opengl.EGLExt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvLocation, tvDay, tvTemperature, tvSun, tvWind;
    Calendar cDate;
    Button bTemperature, bSun, bWind;
    Spinner sDegree;
    LinearLayout lTemperature, lSun, lWind;

    // za lokaciju iz bundla
    String lokacija;

    private HttpHelper httpHelper;
    public static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String API_KEY = "&APPID=5d43cc6458c7267c1386423dfdf463b4&units=metric";
    public String GET_WEATHER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /* uzimanje ID-a svih komponenti */

        tvLocation = findViewById(R.id.text_view_location);
        tvDay = findViewById(R.id.text_view_day);
        tvTemperature = findViewById(R.id.text_view_temperature);
        tvSun = findViewById(R.id.text_view_sun);
        tvWind = findViewById(R.id.text_view_wind);

        bTemperature = findViewById(R.id.button_temperature);
        bSun = findViewById(R.id.button_sun);
        bWind = findViewById(R.id.button_wind);

        sDegree = findViewById(R.id.spinner_degree);

        lTemperature = findViewById(R.id.layout_temperature);
        lSun = findViewById(R.id.layout_sun);
        lWind = findViewById(R.id.layout_wind);

        /* podesavanje Buttona (OnClick()) */

        bTemperature.setOnClickListener(this);
        bSun.setOnClickListener(this);
        bWind.setOnClickListener(this);

        /* uzimanje lokacije prosledjene preko EditText-a */

        Bundle bundleLokacija = getIntent().getExtras();
        tvLocation.setText("Lokacija: " + bundleLokacija.get("poslataLokacija").toString());
        lokacija = bundleLokacija.get("poslataLokacija").toString();
        GET_WEATHER = BASE_URL + lokacija + API_KEY;

        Log.d("URL", "url: " + GET_WEATHER);

        //String GET_WEATHER = BASE_URL + bundleLokacija.get("poslataLokacija").toString() + API_key;


        /* uzimanje dana u nedelji iz Calendar-a */
        cDate = Calendar.getInstance();
        String[] strDan = new String[] {"Nedelja", "Ponedeljak", "Utorak", "Sreda", "Cetvrtak", "Petak", "Subota"};
        tvDay.setText("Dan: " + strDan[cDate.get(Calendar.DAY_OF_WEEK) - 1]);

        /* Drop-down za temperaturu */
        String[] strSpiner = new String[] {"°C", "°F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strSpiner);
        sDegree.setAdapter(adapter);

        /* inicijalizovanje vidljivosti Layout-a */

        lTemperature.setVisibility(View.INVISIBLE);
        lSun.setVisibility(View.INVISIBLE);
        lWind.setVisibility(View.INVISIBLE);

        httpHelper = new HttpHelper();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonobject = httpHelper.getJSONObjectFromURL(GET_WEATHER);
                    JSONObject mainobject = jsonobject.getJSONObject("main");
                    JSONObject sysobject = jsonobject.getJSONObject("sys");
                    JSONObject windobject = jsonobject.getJSONObject("wind");

                    final String temp = mainobject.get("temp").toString();
                    final String pressure = mainobject.get("pressure").toString();
                    final String humidity = mainobject.get("humidity").toString();

                    long sun = Long.valueOf(sysobject.get("sunrise").toString()) * 1000;
                    Date date1 = new Date(sun);
                    final String sunrise = new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date1);

                    long night = Long.valueOf(sysobject.get("sunset").toString()) * 1000;
                    Date date2 = new Date(night);
                    final String sunset = new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date2);

                    final String wind_speed = windobject.get("speed").toString();
                    /*final String degree = windobject.get("deg").toString();
                    double deg = Double.parseDouble(degree);*/
                    double degree = windobject.getDouble("deg");
                    final String wind_direction = degreeToString(degree);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTemperature.setText("Temperatura: " + temp + "\nPritisak: " + pressure + " hPA" + "\nVlažnost vazduha: " + humidity + " %");
                            tvSun.setText("Izlazak sunca: " + sunrise + "\n\nZalazak sunca: " + sunset);
                            tvWind.setText("Brzina vetra: " + wind_speed + " m/s" + "\nPravac: " + wind_direction);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_temperature:
                lTemperature.setVisibility(View.VISIBLE);
                lSun.setVisibility(View.INVISIBLE);
                lWind.setVisibility(View.INVISIBLE);

                bTemperature.setBackground(getDrawable(R.drawable.details_activity_on_click));
                bSun.setBackground(getDrawable(R.drawable.details_activity_buttons));
                bWind.setBackground(getDrawable(R.drawable.details_activity_buttons));
                break;

            case R.id.button_sun:
                lTemperature.setVisibility(View.INVISIBLE);
                lSun.setVisibility(View.VISIBLE);
                lWind.setVisibility(View.INVISIBLE);

                bTemperature.setBackground(getDrawable(R.drawable.details_activity_buttons));
                bSun.setBackground(getDrawable(R.drawable.details_activity_on_click));
                bWind.setBackground(getDrawable(R.drawable.details_activity_buttons));
                break;

            case R.id.button_wind:
                lTemperature.setVisibility(View.INVISIBLE);
                lSun.setVisibility(View.INVISIBLE);
                lWind.setVisibility(View.VISIBLE);

                bTemperature.setBackground(getDrawable(R.drawable.details_activity_buttons));
                bSun.setBackground(getDrawable(R.drawable.details_activity_buttons));
                bWind.setBackground(getDrawable(R.drawable.details_activity_on_click));
                break;
        }
    }

    public String degreeToString(Double degree) {
        if (degree>337.5)
            return "North";
        if (degree>292.5)
            return "North West";
        if(degree>247.5)
            return "West";
        if(degree>202.5)
            return "South West";
        if(degree>157.5)
            return "South";
        if(degree>122.5)
            return "South East";
        if(degree>67.5)
            return "East";
        if(degree>22.5){
            return "North East";
        } else
            return "North";
    }
}

