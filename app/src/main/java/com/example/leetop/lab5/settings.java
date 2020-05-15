package com.example.leetop.lab5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {


    TextView welcomeTextView;
    EditText thresholdEdit, soundThresholdEdit, soundOccurrenceThreshEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Welcome to the settings menu");

        welcomeTextView = findViewById(R.id.welcomeText);
        thresholdEdit = findViewById(R.id.thresholdEdit);
        soundThresholdEdit = findViewById(R.id.soundThresholdEdit);
        soundOccurrenceThreshEdit = findViewById(R.id.soundOccurrenceEdit);


        //get access to the shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        int threshold = sharedPrefs.getInt("threshold",20);
        int soundThreshold = sharedPrefs.getInt("sound threshold",20);
        int soundOccurrenceThreshold = sharedPrefs.getInt("sound occurrence threshold",50);

        thresholdEdit.setText(threshold + "");
        soundThresholdEdit.setText(soundThreshold + "");
        soundOccurrenceThreshEdit.setText(soundOccurrenceThreshold + "");


    }


    public void apply (View view){
        int threshold = Integer.parseInt(thresholdEdit.getText().toString());
        int soundThreshold = Integer.parseInt(soundThresholdEdit.getText().toString());
        int soundOccurrenceThreshold = Integer.parseInt(soundOccurrenceThreshEdit.getText().toString());


        //Save the settings that are in the EditTexts.
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("threshold", threshold);
        editor.putInt("sound threshold", soundThreshold);
        editor.putInt("sound occurrence threshold", soundOccurrenceThreshold);


        Toast.makeText(this, "Settings are applied and saved", Toast.LENGTH_LONG).show();
        editor.commit();

    }


}
