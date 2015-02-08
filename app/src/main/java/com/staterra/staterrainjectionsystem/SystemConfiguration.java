package com.staterra.staterrainjectionsystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SystemConfiguration extends ActionBarActivity {

    GlobalData globalData;
    TextView tankTemp;
    private Button main;
    private Button login;
    private Button flowCal;
    private Button eventDisc;
    private Button advSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_configuration);
        globalData = (GlobalData)getIntent().getParcelableExtra(WelcomeScreen.PAR_KEY);
        tankTemp = (TextView)findViewById(R.id.tankTemp);
        tankTemp.setText(globalData.getTankTemp());
        createButtons();
    }

    private void createButtons(){
        main = (Button)findViewById(R.id.buttonMain1);
        flowCal = (Button)findViewById(R.id.flowCal);
        eventDisc = (Button)findViewById(R.id.eventDisc);
        advSet = (Button)findViewById(R.id.advSet);


        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        flowCal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SystemConfiguration.this, FlowCalibration.class);
                SystemConfiguration.this.startActivity(myIntent);
            }
        });
        eventDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SystemConfiguration.this, EventDiscriminator.class);
                SystemConfiguration.this.startActivity(myIntent);
            }
        });
        advSet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(SystemConfiguration.this, LoginAvdSet.class);
                SystemConfiguration.this.startActivity(myIntent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_system_configuration, menu);
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
