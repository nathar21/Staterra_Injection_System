package com.staterra.staterrainjectionsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends Activity {

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
        systemCheck = (Button) findViewById(R.id.systemCheck);

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