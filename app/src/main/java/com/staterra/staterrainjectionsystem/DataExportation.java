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

import java.io.IOException;


public class DataExportation extends ActionBarActivity {
    MyBlueTooth bluetooth;
    boolean btBounded = false;
    private Button main;
    private Button export;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyBlueTooth.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_exportation);
        createButtons();
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


    private void createButtons(){
        main = (Button)findViewById(R.id.buttonMain2);
        export = (Button)findViewById(R.id.buttonExport);

        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(bluetooth.isConnected()){
                    try{
                        bluetooth.restartProgress();
                        bluetooth.getDataFile();
                    }catch(IOException e){

                    }
                }
                Intent myIntent;
                myIntent = new Intent(DataExportation.this, DownloadingData.class);
                DataExportation.this.startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(btBounded) {
            unbindService(mConnection);
            btBounded = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data__exportation, menu);
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
