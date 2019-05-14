package com.example.weatherforecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button1;
    EditText unosGrada;
    ListView listaGradova;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unosGrada = findViewById(R.id.UnosGrada);
        button1 = findViewById(R.id.DodajGrad);
        button1.setOnClickListener(this);

        adapter = new CustomAdapter(this);

        adapter.AddGrad(new Grad("Belgrade"));
        adapter.AddGrad(new Grad("Moscow"));
        adapter.AddGrad(new Grad("London"));
        adapter.AddGrad(new Grad("Oslo"));

        listaGradova = findViewById(R.id.ListaGradova);
        listaGradova.setAdapter(adapter);

        listaGradova.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.RemoveGrad(position);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DodajGrad :
                if(unosGrada.getText().toString().matches("") ) {
                    unosGrada.setHint("Grad nije unet!");
                } else
                    {
                        adapter.AddGrad(new Grad(unosGrada.getText().toString()));
                        unosGrada.setText("");
                        unosGrada.setHint("");
                    }
            break;
        }
    }
}
