package com.example.projekat1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class Statistic extends AppCompatActivity {
        private static String CITY;

        private  TextView mCity;
        private DBHelper mDbHelper;
        private StatisticInstanceAdapter mAdapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_statistic);

            mCity = findViewById(R.id.city_id);

            Bundle bundleLocation = getIntent().getExtras();
            CITY = bundleLocation.get("edit1").toString();
            mCity.setText(CITY);

            mAdapter = new StatisticInstanceAdapter(this);
            ListView list = (ListView) findViewById(R.id.list_view_statistic);
            list.setAdapter(mAdapter);
            //list.setOnItemClickListener(this);

            mDbHelper = new DBHelper(this);

            StatisticInstance[] si = mDbHelper.readData();

            mAdapter.update(si);


            /*
            dropdown = (Spinner) findViewById(R.id.spinner1);
            String[] items = new String[]{"°C", "°F"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(this);
              * */
        }

    @Override
    protected void onResume() {
        super.onResume();

        StatisticInstance[] si = mDbHelper.readData();
        mAdapter.update(si);
    }

    /*private void fillBasicData(int x) {
        PodaciPrognoze pp;
        String[] days = {"Ponedeljak", "Utorak", "Sreda", "Cetvrtak", "Petak", "Subota", "Nedelja"};

        table1.clear();

        for (int i = 0; i < 7; i++) {
            pp = db.searchDataBase(CITY, days[i]);
            if (pp != null)
                table1.addStatisticInstance(PodaciPrognoze(days[i], String.valueOf(pp.getmTemperature()),pp.getmPressure(),pp.getmHummidity()));
            else
                tableAdapter1.addItem(days[i], "", "", "");
        }

        table1.setAdapter(tableAdapter1);
    }*/


}
