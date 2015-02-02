package com.staterra.staterrainjectionsystem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class SystemStatus extends ActionBarActivity {

    MyBlueTooth bluetooth;
    boolean btBounded = false;
    GlobalData globalData;
    TextView tamper;
    TextView battLife;
    TextView nutrUsed;
    TextView nutrLeft;
    //TextView numLast;
    //TextView totalApp;
    //TextView avgApp;
    private Button main;

    @Override
    protected void onStart() {
        super.onStart();
        try{
            if(bluetooth.isConnected()){
                    bluetooth.getSystemStatus();
                    Thread.sleep(1000);
                    tamper.setText(bluetooth.systemStatusArr[0]);
                    battLife.setText(bluetooth.systemStatusArr[1]);
                    nutrUsed.setText(bluetooth.systemStatusArr[2]);
                    nutrLeft.setText(bluetooth.systemStatusArr[3]);
            }
        }catch(Exception e){}

    };

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MyBlueTooth.LocalBinder binder = (MyBlueTooth.LocalBinder) service;
            bluetooth = binder.getService();
            btBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            btBounded = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__status);
        Intent intent = new Intent(this, MyBlueTooth.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        globalData = (GlobalData)getIntent().getParcelableExtra(WelcomeScreen.PAR_KEY);
        tamper = (TextView)findViewById(R.id.tamper);
        battLife = (TextView)findViewById(R.id.battLife);
        nutrUsed = (TextView)findViewById(R.id.nutrUsed);
        nutrLeft = (TextView)findViewById(R.id.nutrLeft);
        createButtons();
    }

    private void createButtons(){
        main = (Button)findViewById(R.id.buttonMain3);

        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_system__status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(btBounded) {
            unbindService(mConnection);
            btBounded = false;
        }
    }
}
