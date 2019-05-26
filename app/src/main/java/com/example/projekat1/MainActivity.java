package com.example.projekat1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button dodaj_grad;
    EditText unos_novog_grada;
    RadioButton rb;
    ListView gradovi;
    CharacterAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gradovi = findViewById(R.id.list_view);
        dodaj_grad = findViewById(R.id.prikazi_button);
        dodaj_grad.setOnClickListener(this);
        unos_novog_grada = findViewById(R.id.novi_grad);



        adapter = new CharacterAdapter(this);
        rb = findViewById(R.id.radio_button_id);
        adapter.addCharacter(new Character(getString(R.string.Novi_Sad)));
        adapter.addCharacter(new Character(getString(R.string.Subotica)));
        adapter.addCharacter(new Character(getString(R.string.Beograd)));
        adapter.addCharacter(new Character(getString(R.string.Paris)));
        adapter.addCharacter(new Character(getString(R.string.Amsterdam)));
        adapter.addCharacter(new Character(getString(R.string.Madrid)));

        gradovi.setAdapter(adapter);

        gradovi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.deleteCharacter(position);
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.prikazi_button:
                if(unos_novog_grada.getText().toString().matches("")){
                    unos_novog_grada.setHint("Unesi nesto!");
                }else{
                    adapter.addCharacter(new Character(unos_novog_grada.getText().toString()));

                }
                break;

        }







    }



}