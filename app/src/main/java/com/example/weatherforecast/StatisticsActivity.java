package com.example.weatherforecast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {

    private static String CITY;

    private ImageButton sun;
    private ImageButton snowflake;
    private WeatherDBHelper db;
    private ListView table1, table2;
    private TableAdapter tableAdapter1, tableAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TextView city = findViewById(R.id.textViewStatsCity);

        CITY = getCity();
        city.setText(CITY);

        db = new WeatherDBHelper(this);

        table1 = findViewById(R.id.listViewStatsBasic);
        table2 = findViewById(R.id.listViewStatsExtremes);

        tableAdapter1 = new TableAdapter();
        tableAdapter2 = new TableAdapter();

        fillBasicData(0);
        fillExtremesData();

        sun = findViewById(R.id.imageButtonStatsSun);
        snowflake = findViewById(R.id.imageButtonStatsSnowflake);

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillBasicData(1);
            }
        });

        snowflake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillBasicData(2);
            }
        });
    }

    private String getCity() {

        String city = "Novi Sad";

        try {
            String KEY = "city";
            if (getIntent().hasExtra(KEY))
                city = getIntent().getStringExtra(KEY);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return city;
    }

    private void fillBasicData(int x) {
        Weather f;
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        tableAdapter1.clear();

        for (int i = 0; i < 7; i++) {
            f = db.getItemByWeekDay(CITY, days[i], x);
            if (f != null)
                tableAdapter1.addItem(days[i], String.valueOf(f.getTemperature()), String.valueOf(f.getPressure()), String.valueOf(f.getHumidity()));
            else
                tableAdapter1.addItem(days[i], "", "", "");
        }

        table1.setAdapter(tableAdapter1);
    }

    private void fillExtremesData() {
        Weather[] forecasts;

        forecasts = db.getItems(CITY, 1);
        tableAdapter2.addItem("Min temp:", forecasts[0].getWeekDay(), "", String.valueOf(forecasts[0].getTemperature()));
        for (int i = 1; i < forecasts.length; i++)
            tableAdapter2.addItem("", forecasts[i].getWeekDay(), "", String.valueOf(forecasts[i].getTemperature()));

        forecasts = db.getItems(CITY, 2);
        tableAdapter2.addItem("Max temp:", forecasts[0].getWeekDay(), "", String.valueOf(forecasts[0].getTemperature()));
        for (int i = 1; i < forecasts.length; i++)
            tableAdapter2.addItem("", forecasts[i].getWeekDay(), "", String.valueOf(forecasts[i].getTemperature()));

        table2.setAdapter(tableAdapter2);
    }
}
