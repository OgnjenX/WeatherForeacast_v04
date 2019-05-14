package com.example.weatherforecast;

import android.opengl.EGLExt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    Spinner dropdown;

    Button temperatura,izlazak,vetar;

    TextView grad,dan,tvTemperature,tvPritisak,tvVlaznost,tvSunrise,tvSunset,tvWindSpeed,tvWindDirection;

    String string;

    Calendar kalendar;

    LinearLayout temperaturaLayout , izlazakLayout , vetarLayout;

    private HttpHelper httpHelper;

    public static String BaseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String ApiKey = "&APPID=ba47633e62262a2fa2ffd72c835c3c6e&units=metric";
    public static String GET_WEATHER;
    public static String ImgURL = "http://openweathermap.org/img/w/10d.png";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        temperatura = findViewById(R.id.temperatura);
        temperatura.setOnClickListener(this);

        izlazak = findViewById(R.id.izlazak);
        izlazak.setOnClickListener(this);

        vetar = findViewById(R.id.vetar);
        vetar.setOnClickListener(this);

        temperaturaLayout = findViewById(R.id.layoutTemp);
        izlazakLayout = findViewById(R.id.layoutIzlazak);
        vetarLayout = findViewById(R.id.layoutVetar);


        grad = findViewById(R.id.textLokacija);
        string = getString(R.string.Lokacija) + getIntent().getExtras().getString("text_grad_u_listi");
        grad.setText(string);
        GET_WEATHER = BaseURL + getIntent().getExtras().getString("text_grad_u_listi") + ApiKey;

        kalendar = Calendar.getInstance();
        dan = findViewById(R.id.textDan);
        String[] dani = new String[] {"Ponedeljak","Utorak","Sreda","Cetvrtak","Petak","Subota","Nedelja"};
        dan.setText("Dan: "+ dani[kalendar.get(Calendar.DAY_OF_WEEK)-2]);

        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"°C", "°F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        temperaturaLayout.setVisibility(View.INVISIBLE);
        izlazakLayout.setVisibility(View.INVISIBLE);
        vetarLayout.setVisibility(View.INVISIBLE);


        tvTemperature = findViewById(R.id.textTemperatura);
        tvPritisak = findViewById(R.id.textPritisak);
        tvVlaznost = findViewById(R.id.textVlaznost);
        tvSunrise = findViewById(R.id.textIzlazakSunca);
        tvSunset = findViewById(R.id.textZalazakSunca);
        tvWindSpeed = findViewById(R.id.textBrzinaVetra);
        tvWindDirection = findViewById(R.id.textPravac);


        Log.d("URL","url: " + GET_WEATHER);

        httpHelper = new HttpHelper();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str = httpHelper.getJSONObjectFromURL1(ImgURL);
                    Log.d("str","str: " + str);

                    JSONObject jsonObject = httpHelper.getJSONObjectFromURL(GET_WEATHER);
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    JSONObject sysObject = jsonObject.getJSONObject("sys");
                    JSONObject windObject = jsonObject.getJSONObject("wind");

                    final String temperature = mainObject.get("temp").toString();
                    final String pressure = mainObject.get("pressure").toString();
                    final String humidity = mainObject.get("humidity").toString();

                    long sunrise1 = Long.valueOf(sysObject.get("sunrise").toString()) * 1000;
                    Date date_sunrise = new Date(sunrise1);
                    final String sunrise = new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date_sunrise);

                    long sunset1 = Long.valueOf(sysObject.get("sunset").toString()) * 1000;
                    Date date_sunset = new Date(sunset1);
                    final String sunset = new SimpleDateFormat("hh:mma", Locale.ENGLISH).format(date_sunset);

                    double degree = windObject.getDouble("deg");
                    final String wind_direction = degreeToString(degree);
                    final String wind_speed = windObject.get("speed").toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final double temp = Double.parseDouble(temperature);
                            int roundTemp = (int)temp;
                            final String tempCelsius = Integer.toString(roundTemp);

                            tvTemperature.setText("Temperatura: " + tempCelsius + "°C");
                            tvPritisak.setText("Pritisak: " + pressure + "hPa");
                            tvVlaznost.setText("Vlaznost: " + humidity + "%");

                            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selected = parent.getSelectedItem().toString();
                                    if(selected.equals("°C")){
                                        tvTemperature.setText("Temperatura: " + tempCelsius + "°C");
                                    }else{
                                        double temperatureFahrad = temp * (9/5)*32;
                                        int tempFahrad = (int)temperatureFahrad;
                                        final String tFahrad = Integer.toString(tempFahrad);
                                        tvTemperature.setText("Temperatura: " + tFahrad + "°F");
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    tvTemperature.setText("Temperatura: " + tempCelsius + "°C");
                                }
                            });
                            tvSunrise.setText("Izlazak sunca: " + sunrise);
                            tvSunset.setText("Zalazak sunca: " + sunset);
                            tvWindSpeed.setText("Brzina vetra: " + wind_speed + "m/s");
                            tvWindDirection.setText("Pravac vetra: " + wind_direction);

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
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.temperatura:
                temperaturaLayout.setVisibility(View.VISIBLE);
                izlazakLayout.setVisibility(View.INVISIBLE);
                vetarLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.izlazak:
                temperaturaLayout.setVisibility(View.INVISIBLE);
                izlazakLayout.setVisibility(View.VISIBLE);
                vetarLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.vetar:
                temperaturaLayout.setVisibility(View.INVISIBLE);
                izlazakLayout.setVisibility(View.INVISIBLE);
                vetarLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public String degreeToString(double degree){
        if(degree <= 22.5){
                return "North-Northeast";
        }
        if(degree <= 45){
            return "Northeast";
        }
        if(degree <= 67.5){
            return "East-Northeast";
        }
        if(degree <= 90){
            return "East";
        }
        if(degree <= 112.5){
            return "East-Southeast";
        }
        if(degree <= 135){
            return "Southeast";
        }
        if(degree <= 157.5){
            return "South-Southeast";
        }
        if(degree <= 180){
            return "South";
        }
        if(degree <= 202.5){
            return "South-Southwest";
        }
        if(degree <= 225){
            return "Southwest";
        }
        if(degree <= 247.5){
            return "West-Southwest";
        }
        if(degree <= 270){
            return "West";
        }
        if(degree <= 292.5){
            return "West-Northwest";
        }
        if(degree <= 315){
            return "Northwest";
        }
        if(degree <= 337.5){
            return "North-Northwest";
        }
        return "North";
    }
}
