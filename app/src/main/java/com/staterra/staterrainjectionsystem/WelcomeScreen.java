package com.staterra.staterrainjectionsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class WelcomeScreen extends Activity {

    TextView welcomeDate;
    TextView siteLocation;
    String wlcDateStr;
    String siteLocStr = "UWB Demo";

    public final static String PAR_KEY = "com.staterra.staterrainjectionsystem.GlobalData.par";
    GlobalData globalData = new GlobalData();
    Button systemCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_screen);
        Intent i = new Intent(this, MyBlueTooth.class);
        i.putExtra("KEY1", "Value to be used by the service");
        startService(i);
        createButtons();
    }

    private void createButtons() {
        welcomeDate = (TextView)findViewById(R.id.welcomeDate);
        siteLocation = (TextView)findViewById(R.id.siteLocation);
        systemCheck = (Button) findViewById(R.id.systemCheck);
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);
        String date = String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
        welcomeDate.setText(date);
        siteLocation.setText(siteLocStr);
        systemCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(WelcomeScreen.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(PAR_KEY, globalData);
                myIntent.putExtras(mBundle);
                WelcomeScreen.this.startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}