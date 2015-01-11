package com.staterra.staterrainjectionsystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class CurrentEnvironmentalStatus extends ActionBarActivity {

    private Button main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_environmental_status);
        createButtons();
    }

    private void createButtons(){
        main = (Button)findViewById(R.id.buttonMain1);

        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent;
                myIntent = new Intent(CurrentEnvironmentalStatus.this, MainActivity.class);
                CurrentEnvironmentalStatus.this.startActivity(myIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_environmental_status, menu);
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
