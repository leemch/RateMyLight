package com.example.leetop.lab5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    //Goes to light analyzing menu
    public void goToAnalyze(View v){

        Intent intent= new Intent(this, analyzing.class);
        startActivity(intent);

    }

    //Go to view saved locations
    public void goToSaved(View v){

        Intent intent= new Intent(this, SavedLocations.class);
        startActivity(intent);

    }


    //Go to user settings menu
    public void goToSettings(View v){

        Intent intent= new Intent(this, settings.class);
        startActivity(intent);

    }
}
