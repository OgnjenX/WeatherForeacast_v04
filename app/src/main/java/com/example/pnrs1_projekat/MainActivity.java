package com.example.pnrs1_projekat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection{
    public static String CHANNEL_ID;
    private final String TAG = "main";
    Button buttonMainActivity;
    EditText myLocation;
    ListView listOfCities;
    ElementRowAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //zadatak6
        MyNDK ndk = new MyNDK();
        int res = ndk.increment(5);
        Log.d("test", String.valueOf(res));

        //zadatak5
        createNotificationChannel();

        buttonMainActivity = findViewById(R.id.buttonMainActivity);
        myLocation = (EditText) findViewById(R.id.location);


        buttonMainActivity.setOnClickListener(this);


        adapter = new ElementRowAdapter(this);
        adapter.addElement(new ElementRow("Novi Sad"));
        //adapter.addElement(new ElementRow("Belgrade"));
        //adapter.addElement(new ElementRow("Bijeljina"));

        listOfCities = (ListView) findViewById(R.id.listOfCities);
        listOfCities.setAdapter(adapter);

        listOfCities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                adapter.removeElement(position);
                return true;
            }
        });

    }

    @Override
        public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonMainActivity:
                if(myLocation.getText().toString().matches("")){
                    myLocation.setHint("Unesite grad!");
                }else{
                    adapter.addElement(new ElementRow(myLocation.getText().toString()));
                    myLocation.setText("");
                    myLocation.setHint("");
                }
                break;
            case R.id.startServiceButton:

                break;
            case R.id.stopServiceButton:

                break;
                default:
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            //String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PantelaWeather", importance);
            channel.setDescription("temperature update");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}