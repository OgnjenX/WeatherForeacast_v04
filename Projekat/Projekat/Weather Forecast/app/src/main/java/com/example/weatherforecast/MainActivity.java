package com.example.weatherforecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bAddCity;
    EditText etLocation;

    ListView mList;

    ElementAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAddCity = findViewById(R.id.button_add);
        etLocation = findViewById(R.id.edit_text_location);
        mList = findViewById(R.id.list_view_main);

        bAddCity.setOnClickListener(this);

        adapter = new ElementAdapter(this);

        adapter.addElement(new Element(getString(R.string.Belgrade)));
        adapter.addElement(new Element(getString(R.string.London)));
        adapter.addElement(new Element(getString(R.string.Paris)));
        mList.setAdapter(adapter);

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.deleteElement(position);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {

        adapter.addElement(new Element(etLocation.getText().toString()));

    }
}
