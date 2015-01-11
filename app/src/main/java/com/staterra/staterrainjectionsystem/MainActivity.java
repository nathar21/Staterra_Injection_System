package com.staterra.staterrainjectionsystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

//testing lola begs too good
public class MainActivity extends ActionBarActivity {

    GlobalData globalData = new GlobalData();
    MyBlueTooth blueTooth;
    Button page1;
    Button page2;
    Button page3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blueTooth = new MyBlueTooth(this);
        createButtons();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        blueTooth.stopWorker = true;
        if (blueTooth.mmInputStream != null) {
            try {blueTooth.mmInputStream.close();} catch (Exception e) {}
            blueTooth.mmInputStream = null;
        }

        if (blueTooth.mmOutputStream != null) {
            try {blueTooth.mmOutputStream.close();} catch (Exception e) {}
            blueTooth.mmOutputStream = null;
        }

        if (blueTooth.mmSocket != null) {
            try {blueTooth.mmSocket.close();} catch (Exception e) {}
            blueTooth.mmSocket = null;
        }
    }

    private void createButtons(){
        page1 = (Button)findViewById(R.id.button);
        page2 = (Button)findViewById(R.id.button2);
        page3 = (Button)findViewById(R.id.button3);

        page1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, CurrentEnvironmentalStatus.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        page2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DataExportation.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        page3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(MainActivity.this, CurrentTankStatus.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
