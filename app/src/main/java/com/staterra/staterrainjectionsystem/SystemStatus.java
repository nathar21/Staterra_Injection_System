package com.staterra.staterrainjectionsystem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SystemStatus extends ActionBarActivity {

    GlobalData globalData;
    TextView tamper;
    TextView battLife;
    TextView nutrUsed;
    TextView nutrLeft;
    TextView numLast;
    TextView totalApp;
    TextView avgApp;
    private Button main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system__status);

        //Hey Matt... Am I doing this right?
        //can you access these variables??
        //can't find the getTankTemp method... ?
        globalData = (GlobalData)getIntent().getParcelableExtra(MainActivity.PAR_KEY);
        //tamper = (TextView)findViewById(R.id.tamper);
        //tamper.setText(globalData.getTankTemp());
        //battLife = (TextView)findViewById(R.id.tamper);
        //battLife.setText(globalData.getTankTemp());
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
}
