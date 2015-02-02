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

    Button systemConfig;
    Button dataExport;
    Button systemStatus;
    GlobalData globalData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalData = (GlobalData)getIntent().getParcelableExtra(WelcomeScreen.PAR_KEY);
        setContentView(R.layout.activity_main);
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

    private void createButtons(){
        systemConfig = (Button)findViewById(R.id.systemConfig);
        dataExport = (Button)findViewById(R.id.dataExport);
        systemStatus = (Button)findViewById(R.id.systemStatus);

        systemConfig.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SystemConfiguration.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(WelcomeScreen.PAR_KEY, globalData);
                myIntent.putExtras(mBundle);
                MainActivity.this.startActivity(myIntent);
            }
        });
        dataExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, DataExportation.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
        systemStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, SystemStatus.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(WelcomeScreen.PAR_KEY, globalData);
                myIntent.putExtras(mBundle);
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
