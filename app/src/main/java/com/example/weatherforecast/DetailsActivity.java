package com.example.weatherforecast;

import android.content.Intent;
import android.opengl.EGLExt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

    Button temperatura,izlazak,vetar,statistics;

    TextView grad,dan,tvTemperature,tvPritisak,tvVlaznost,tvSunrise,tvSunset,tvWindSpeed,tvWindDirection,lastUpdated;

    String string;

    Calendar kalendar;

    LinearLayout temperaturaLayout , izlazakLayout , vetarLayout;

    ImageButton imageButton;

    private HttpHelper httpHelper;

    private static final String KEY = "city";

    public static String BaseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    public static String ApiKey = "&APPID=ba47633e62262a2fa2ffd72c835c3c6e&units=metric";
    public static String GET_WEATHER;
    public static String ImgURL = "http://openweathermap.org/img/w/10d.png";

    WeatherDBHelper data_base;

    private static String city;

    String today,lastUpdateDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        city = getCity();

        data_base = new WeatherDBHelper(this);

        temperatura = findViewById(R.id.temperatura);
        temperatura.setOnClickListener(this);

        izlazak = findViewById(R.id.izlazak);
        izlazak.setOnClickListener(this);

        vetar = findViewById(R.id.vetar);
        vetar.setOnClickListener(this);

        temperaturaLayout = findViewById(R.id.layoutTemp);
        izlazakLayout = findViewById(R.id.layoutIzlazak);
        vetarLayout = findViewById(R.id.layoutVetar);

        imageButton = findViewById(R.id.imageButtonDetailsRefresh);
        statistics = findViewById(R.id.buttonDetailsStats);


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
        lastUpdated = findViewById(R.id.textViewDetailsLastUpdated);

        today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        city = getCity();


        Log.d("URL","url: " + GET_WEATHER);

        httpHelper = new HttpHelper();

        dummyValues();

        getDataFromInternet();


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
            case R.id.imageButtonDetailsRefresh:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                }).start();
            case R.id.buttonDetailsStats:
                Intent intent = new Intent(DetailsActivity.this, StatisticsActivity.class);
                intent.putExtra(KEY, city);
                startActivity(intent);
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

    private void dummyValues() {
        data_base.insert(new Weather("06-05-2019", "Novi Sad", 19.0, 0.66, 1.01, "05:00", "21:00", 16.0, "North-Northeast","Ponedeljak"));
        data_base.insert(new Weather("07-05-2019", "Novi Sad", 16.0, 0.62, 1.04, "05:02", "21:04", 14.0, "North-Northeast","Utorak"));
        data_base.insert(new Weather("08-05-2019", "Novi Sad", 17.0, 0.69, 1.02, "05:03", "21:04", 15.0, "North-Northeast","Sreda"));
        data_base.insert(new Weather("09-05-2019", "Novi Sad", 14.0, 0.62, 0.98, "05:01", "21:05", 12.0, "North-Northeast","Cetvrtak"));
        data_base.insert(new Weather("10-05-2019", "Novi Sad", 15.0, 0.58, 0.99, "04:58", "21:05", 8.0, "North-Northeast","Petak"));
        data_base.insert(new Weather("11-05-2019", "Novi Sad", 15.0, 0.70, 1.00, "05:07", "21:06", 17.0, "North-Northeast","Subota"));
        data_base.insert(new Weather("12-05-2019", "Novi Sad", 21.0, 0.69, 1.01, "04:56", "21:06", 115.0, "North-Northeast","Nedelja"));
    }

    private String getCity(){
        String city = "Novi Sad";

        try{
            if(getIntent().hasExtra(KEY)){
                city = getIntent().getStringExtra(KEY);
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return city;
    }

    private void refreshData(){
        Weather weather = new Weather(city);

        data_base.insert(weather);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setLatestData();
            }
        });

    }
    private void setLatestData() {
        WeatherDBHelper dbHelper = new WeatherDBHelper(this);
        Weather weather = dbHelper.getItem(city);

        grad.setText(String.format("%s %s", getString(R.string.Lokacija), city));
        dan.setText(String.format("%s %s", getString(R.string.textViewDetailsDate), weather.getDate()));

        tvTemperature.setText(String.format("%s %.2f", getString(R.string.Temperatura), weather.getTemperature()));
        tvVlaznost.setText(String.format("%s %.2f", getString(R.string.Vlaznost), weather.getHumidity()));
        tvPritisak.setText(String.format("%s %.2f", getString(R.string.Pritisak), weather.getPressure()));

        tvWindSpeed.setText(String.format("%s %.2f", getString(R.string.BrzinaVetra), weather.getWindSpeed()));
        tvWindDirection.setText(String.format("%s %s", getString(R.string.Pravac), weather.getWindDirection()));

        tvSunrise.setText(weather.getSunrise());
        tvSunset.setText(weather.getSunset());

        if (today.equals(weather.getDate()))
            lastUpdated.setVisibility(View.INVISIBLE);
        else
            lastUpdated.setVisibility(View.VISIBLE);
    }
 private void getDataFromInternet() {
     new Thread(new Runnable() {
         @Override
         public void run() {
             try {
                 String str = httpHelper.getJSONObjectFromURL1(ImgURL);
                 Log.d("str", "str: " + str);

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
                         int roundTemp = (int) temp;
                         final String tempCelsius = Integer.toString(roundTemp);

                         tvTemperature.setText("Temperatura: " + tempCelsius + "°C");
                         tvPritisak.setText("Pritisak: " + pressure + "hPa");
                         tvVlaznost.setText("Vlaznost: " + humidity + "%");

                         dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                             @Override
                             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                 String selected = parent.getSelectedItem().toString();
                                 if (selected.equals("°C")) {
                                     tvTemperature.setText("Temperatura: " + tempCelsius + "°C");
                                 } else {
                                     double temperatureFahrad = temp * (9 / 5) * 32;
                                     int tempFahrad = (int) temperatureFahrad;
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
}
