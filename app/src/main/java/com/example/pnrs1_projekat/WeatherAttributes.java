package com.example.pnrs1_projekat;

public class WeatherAttributes {
    private String cityName, date, temperature, humidity, pressure, sunRise, sunSet, windSpeed, windDirection;

    public WeatherAttributes(String cityName, String date, String temperature, String humidity, String pressure, String sunRise, String sunSet, String windSpeed, String windDirection) {
        this.cityName = cityName;
        this.date = date;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sunRise = sunRise;
        this.sunSet = sunSet;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSunRise() {
        return sunRise;
    }

    public String getSunSet() {
        return sunSet;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }
}
