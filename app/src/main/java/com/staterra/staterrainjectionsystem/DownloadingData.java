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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;


public class DownloadingData extends ActionBarActivity {

    MyBlueTooth bluetooth;
    boolean btBounded = false;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    TextView message;

    private Handler mHandler = new Handler();


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
    protected void onStop(){
        super.onStop();
        if(btBounded) {
            unbindService(mConnection);
            btBounded = false;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloading_data);
        message = (TextView)findViewById(R.id.downloadMsg);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setProgress(0);
        Intent intent = new Intent(this, MyBlueTooth.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    try{
                        Thread.sleep(100);
                    }catch(Exception e){

                    }
                    mProgressStatus = getProgress();

                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                            if(mProgressStatus == 100){
                                message.setText("Done");
                            }
                        }
                    });
                }

            }
        }).start();
    }

    private int getProgress(){
        int progress = 0;
        try{
            if(bluetooth.isConnected()){
                try{
                    progress = bluetooth.getProgress();
                }catch(Exception e){

                }
            }
        }catch(Exception e){

        }

        return progress;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_downloading_data, menu);
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
}
