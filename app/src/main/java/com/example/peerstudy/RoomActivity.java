package com.example.peerstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;
enum State{

    PAUSE, RUNNING, STOP;
}

public class RoomActivity extends AppCompatActivity {
    final String ip = "http://ec2-18-188-168-249.us-east-2.compute.amazonaws.com";//"http://192.168.1.113";
     String code;
     String name;
     TextView notifs;
    CountDownTimer countDownTimer ;
    long currentTime ;
    State state = State.RUNNING;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(ip+":3000");
        } catch (URISyntaxException e) {}
    }
  //TODO SAVE STATES WHILE SWITCHING ACTIVITIES AND FIX UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);
        state = state.RUNNING;
        mSocket.connect();
        Intent intent = getIntent();
       final TextView debugText  = findViewById(R.id.debugText);
       final TextView messages = findViewById(R.id.messages);
       final EditText textField = findViewById(R.id.textfield);
       notifs = findViewById(R.id.notifs);
       notifs.setMovementMethod(new ScrollingMovementMethod());
       final Button send  = findViewById(R.id.send);
       messages.setMovementMethod(new ScrollingMovementMethod());
        final Button start = findViewById(R.id.start);
        code = intent.getStringExtra("code");
      final  String duration = intent.getStringExtra("time");
      name  = intent.getStringExtra("name");

        mSocket.emit("joinRoom" , code,name);
        mSocket.on("roomLocked", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Thread t = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = Toast.makeText(getApplicationContext(), "ROOM LOCKED NOW" , Toast.LENGTH_SHORT);
                                t.show();



                            }
                        });
                    }
                };
                t.start();

                Log.i("THISSS" , "WHATT");
                Intent myIntent = new Intent(RoomActivity.this, MainActivity.class);
                RoomActivity.this.startActivity(myIntent);

            }
        });


        mSocket.on("startSignal", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                countDownTimer.start();
            }
        });
        mSocket.on("successalert", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String recName = (String) args[1];
                Thread t = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = Toast.makeText(getApplicationContext(),"ASK FRIEND TO STUDY" , Toast.LENGTH_LONG);


                                t.show();

                                notifs.append("\n"+"ROOM: "+recName + " left the app");

                            }
                        });
                    }
                };
                t.start();

            }
        });
        mSocket.on("newjoin", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String data = (String)args[0];
                final String recName = (String)args[1];

              Log.i("argsss" , data);
              Log.i("receieved from socket" , "yayysysfyifaisdf");

            //  debugText.setText("change");
                Thread t = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                debugText.setText("number of people in the room: "+data);
                                notifs.append("\n"+"ROOM: "+ recName + "joined ");
                            }
                        });
                    }
                };
                t.start();
            }
        });
        mSocket.on("returned", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String recName = (String) args[0];
                Thread t = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifs.append("\n"+"ROOM: "+recName+" returned " );
                            }
                        });
                    }
                };
                t.start();

            }
        });
        mSocket.on("recMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String data = (String) args[0];
                final String recName = (String) args[1];
                Thread t = new Thread() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.append("\n"+recName+": " + data);
                            }
                        });
                    }
                };
                t.start();

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("sendMessage" , code, textField.getText().toString() , name);
                // TODO impleement sending when msg is a space
                messages.append("\n"+"YOU: "+textField.getText().toString());
                textField.setText("");
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer=new CountDownTimer(Integer.parseInt(duration)*1000*60, 1000){

                    public void onTick(long millisUntilFinished) {
                        debugText.setText(String.valueOf( millisUntilFinished / 60000)+":"+String.valueOf((millisUntilFinished/1000)%60));

                        if (state !=State.RUNNING && state!= State.STOP) {
                            if((currentTime - millisUntilFinished  )/1000 < 5){
                                Toast t = Toast.makeText(getApplicationContext(), "AHEM?" ,Toast.LENGTH_SHORT );
                                t.show();
                            }

                            else{
                                Toast t = Toast.makeText(getApplicationContext(), "GO STUDY!" ,Toast.LENGTH_LONG );
                                t.show();

                            }

                        }
                        else{
                            currentTime = millisUntilFinished;
                        }


                    }

                    public void onFinish() {
                        Toast t = Toast.makeText(getApplicationContext(), "DONE!" , Toast.LENGTH_LONG);
                        t.show();
                        start.setVisibility(View.VISIBLE);
                        start.setClickable(true);
                        start.setText("restart");
                    }
                };
                start.setVisibility(View.INVISIBLE);
                start.setClickable(false);
                mSocket.emit("startTimer" , code, duration);
            }
        });
      /*  Log.i("value" , value);
        BackgroundWorker backgroundWorker=new BackgroundWorker(getApplicationContext());
        backgroundWorker.execute(ip+":3001/?code="+value);
        BackgroundWorker alerter = new BackgroundWorker(getApplicationContext());
        alerter.execute(ip+":3001/alert?code="+value);*/


    }

    @Override
    protected void onPause(){
        super.onPause();
        state= State.PAUSE;
     //   currentTime =
        Thread t = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifs.append("\n"+"YOU left the app");

                    }
                });
            }
        };
        t.start();
        mSocket.emit("alertRoom", code, name);


    }
    @Override
    public void onBackPressed() {
        state  =State.PAUSE;

      //  super.onBackPressed();
        moveTaskToBack(true);
    }
    @Override
    protected void onResume(){

        super.onResume();
        state = State.RUNNING;
        Thread t = new Thread() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifs.append("\n"+"YOU returned");

                    }
                });
            }
        };
        t.start();

        mSocket.emit("return" , code, name);


    }
    @Override
    protected void onDestroy(){
        state  =State.STOP;


        super.onDestroy();
    }

}
