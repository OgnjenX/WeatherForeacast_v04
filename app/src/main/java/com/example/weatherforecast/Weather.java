package com.example.weatherforecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Weather {
    private String mDate;
    private String mCity;
    private double mTemperature;
    private double mPressure;
    private double mHumidity;
    private String mSunrise;
    private String mSunset;
    private double mWindSpeed;
    private String mWindDirection;
    private String mWeekDay;

    public Weather(String city){
        HttpHelper httpHelper = new HttpHelper();

        final String EXTRAS = "&units=metric";
        final String SECRET_KEY = "&APPID=8827e28e1ab35a7ff0a4a64651bea798";
        final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";

        final String URL = BASE_URL + city + EXTRAS + SECRET_KEY;

        try {
            JSONObject json = httpHelper.getJSONObjectFromURL(URL);

            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            JSONObject sys = json.getJSONObject("sys");

            this.mTemperature = main.getDouble("temp");
            this.mHumidity = main.getDouble("humidity");
            this.mPressure = main.getDouble("pressure");

            this.mWindSpeed = wind.getDouble("speed");

            this.mCity = json.getString("name");
            this.mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            this.mWeekDay = new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date());

            try {
                this.mWindDirection = degreeToString(wind.getDouble("deg"));
            } catch (JSONException e) {
                e.printStackTrace();
                this.mWindDirection = "404";
            }

            this.mSunrise = convertUnixTime(sys.getString("sunrise"));
            this.mSunset = convertUnixTime(sys.getString("sunset"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Weather(String Date,String City,double Temperature,double Pressure,double Humidity,String Sunrise,String Sunset,double WindSpeed,String WindDirection,String WeekDay){
        mDate = Date;
        mCity = City;
        mTemperature = Temperature;
        mPressure = Pressure;
        mHumidity = Humidity;
        mSunrise = Sunrise;
        mSunset = Sunset;
        mWindSpeed = WindSpeed;
        mWindDirection = WindDirection;
        mWeekDay = WeekDay;
    }


    public String getDate(){ return mDate;}

    public String getCity(){ return mCity;}

    public double getTemperature(){return mTemperature;}

    public double getPressure(){return mPressure;}

    public double getHumidity(){return mHumidity;}

    public String getSunrise(){return mSunrise;}

    public String getSunset(){return mSunset;}

    public double getWindSpeed(){return mWindSpeed;}

    public String getWindDirection(){return mWindDirection;}

    public String getWeekDay() {
        return mWeekDay;
    }

    private static String convertUnixTime(String time) {
        long ut = Long.parseLong(time);
        Date date = new Date(ut * 1000L);

        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(date);
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
