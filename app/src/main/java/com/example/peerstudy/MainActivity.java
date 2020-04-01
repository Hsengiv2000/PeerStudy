package com.example.peerstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static  String roomcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

 final String ip = "http://192.168.1.113";
        setContentView(R.layout.activity_main);
       final Button heroButton= findViewById(R.id.heroButton);
       final EditText time  = (EditText)findViewById(R.id.time);
       final EditText name   =findViewById(R.id.name);
       final TextView tv = findViewById(R.id.display);
       final EditText code = findViewById(R.id.code);
        heroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time.getText().toString().length()!=0 &&code.getText().toString().length()!=0 && name.getText().toString().length()!=0){
                BackgroundWorker backgroundWorker=new BackgroundWorker(getApplicationContext());
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                backgroundWorker.execute(ip + ":1234/hero?code=" + code.getText().toString() + "&ip=" + ipAddress);
                tv.setText(code.getText().toString());
                Intent myIntent = new Intent(MainActivity.this, RoomActivity.class);

                myIntent.putExtra("code", code.getText().toString());
                myIntent.putExtra("time" , time.getText().toString());
                myIntent.putExtra("name" , name.getText().toString());
                MainActivity.this.startActivity(myIntent);}
                else{
            Toast t = Toast.makeText(getApplicationContext(), time.getText().toString() + code.getText().toString() , Toast.LENGTH_LONG);
                t.show();
                }
            }
        });

    }
}
