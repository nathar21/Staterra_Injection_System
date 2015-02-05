package com.staterra.staterrainjectionsystem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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
    String tamperStr = "No Data";
    String battLifeStr = "No Data";
    String nutrUsedStr = "No Data";
    String nutrLeftStr = "No Data";
    Thread thread;
    //TextView numLast;
    //TextView totalApp;
    //TextView avgApp;
    private Button main;

    final Handler mHandler = new Handler();

    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };

    protected void startLongRunningOperation() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                while(true){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){

                    }
                    getStatus();
                    mHandler.post(mUpdateResults);
                }
            }
        };
        t.start();
    }

    private void updateResultsInUi() {
        tamper.setText(tamperStr);
        battLife.setText(battLifeStr);
        nutrUsed.setText(nutrUsedStr);
        nutrLeft.setText(nutrLeftStr);
    }


    @Override
    protected void onStart() {
        super.onStart();
    };

    @Override
    protected void onResume() {
        super.onResume();
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
        tamper = (TextView)findViewById(R.id.tamper);
        battLife = (TextView)findViewById(R.id.battLife);
        nutrUsed = (TextView)findViewById(R.id.nutrUsed);
        nutrLeft = (TextView)findViewById(R.id.nutrLeft);
        createButtons();
        startLongRunningOperation();
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

    private void getStatus(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (bluetooth.isConnected()) {
                bluetooth.getSystemStatus();
                tamperStr = bluetooth.getTamper();
                battLifeStr = bluetooth.getBatteryLife();
                nutrUsedStr = bluetooth.getNutrUsed();
                nutrLeftStr = bluetooth.getNutrLeft();
            }
        }catch(Exception e){}
    }

    @Override
    protected void onPause(){
        super.onPause();

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
