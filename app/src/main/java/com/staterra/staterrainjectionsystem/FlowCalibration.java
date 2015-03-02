package com.staterra.staterrainjectionsystem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class FlowCalibration extends ActionBarActivity {

    private Button menu;
    private Button flowSet;
    TextView flowRate;
    EditText enteredFlow;
    MyBlueTooth bluetooth;
    boolean btBounded = false;
    boolean isLongRunningOperation = false;
    Thread t;
    final Handler mHandler = new Handler();
    String flowRateStr;
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };

    private void updateResultsInUi() {
        flowRate.setText(bluetooth.getFlowRateStr());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MyBlueTooth.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_flow_calibration);
        createButtons();
        startLongRunningOperation();
    }

    protected void startLongRunningOperation() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        isLongRunningOperation = true;
        t = new Thread() {
            public void run() {
                while(isLongRunningOperation){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){

                    }
                    getFlow();
                    mHandler.post(mUpdateResults);
                }
            }
        };
        t.start();
    }

    public void getFlow(){
        try{
            bluetooth.getSingleFlowRate();
        }catch(Exception e){

        }
    }

    protected void stopLongRunningOperation(){
        isLongRunningOperation = false;
        try{
            t.join();
        }catch(Exception e){

        }
    }

    private void createButtons(){
        menu = (Button)findViewById(R.id.menuFlow);
        flowRate = (TextView)findViewById(R.id.flow);
        flowSet = (Button)findViewById(R.id.flowSet);
        enteredFlow = (EditText)findViewById(R.id.enteredFlow);
        flowRate.setText("ND");
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopLongRunningOperation();
                finish();
            }
        });
        flowSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int newFlow;
                try{
                    newFlow = Integer.parseInt(enteredFlow.getText().toString());
                    bluetooth.setNewFlow(newFlow);
                }catch(Exception e){

                }
            }
        });

    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flow_calibration, menu);
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
