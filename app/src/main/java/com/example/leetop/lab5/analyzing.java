package com.example.leetop.lab5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;

public class analyzing extends AppCompatActivity implements SensorEventListener {

    //Sensor things
    SensorManager mySensorManager;
    Sensor lightSensor, accelSensor;

    //rating vars
    float rating, lightReading;

    TextView scoreTxt, ratingTxt, soundTxt, soundRatingText;

    ImageView imgView;

    boolean isFlat;
    boolean isAnalyzing;
    boolean isTrackingSound;


    int lightThreshold = 20;
    int soundThreshold = 2000;
    int soundOccurThreshold = 50;
    double deviation;

    String ratingStr;
    String soundRating;

    Button saveBtn, startBtn, soundButton;

    SoundMeter soundTracker;
    private Handler mHandler = new Handler();

    int noiseCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzing);

        //initialize sensors
        mySensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Image view to hold light bulb indicator
        imgView = findViewById(R.id.imageView);
        imgView.setVisibility(View.INVISIBLE);


        scoreTxt = findViewById(R.id.scoreText);
        ratingTxt = findViewById(R.id.ratingText);
        soundTxt = findViewById(R.id.soundLvlText);
        soundRatingText = findViewById(R.id.soundRatingText);


        rating = 0.0f;
        lightReading = 0;
        ratingStr = "";
        soundRating = "none";


        isFlat = false;
        isAnalyzing = false;
        isTrackingSound = false;

        saveBtn = findViewById(R.id.saveButton);
        startBtn = findViewById(R.id.startButton);

        //get user settings from Shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        lightThreshold = sharedPrefs.getInt("threshold", 20);
        soundThreshold = sharedPrefs.getInt("sound threshold", 15);
        soundOccurThreshold = sharedPrefs.getInt("sound occurrence threshold", 50);

        //standard deviation
        deviation = lightThreshold/10.0;

        soundButton = findViewById(R.id.trackSoundbtn);
        soundTracker = new SoundMeter();

        noiseCounter = 0;


    }


    //Runnable method that constantly checks the sound level
    private Runnable soundTrackingTask = new Runnable() {
        public void run() {
            double amp = soundTracker.getAmplitude();
            Log.i("Noise", "runnable mPollTask");

            if(amp > soundThreshold){
                noiseCounter++;
                soundTxt.setText("Loud noise occurrences: "+ noiseCounter);

                soundRating = getSoundRating(noiseCounter);
                soundRatingText.setText(soundRating);
            }

            mHandler.postDelayed(soundTrackingTask, 300);
        }
    };


    public void goToSaveLocation(View view) {

        //Send analyzed data to be saved
        Intent intent = new Intent(this, saveLocation.class);
        intent.putExtra("score", rating);
        intent.putExtra("rating", ratingStr);
        intent.putExtra("sound rating", soundRating);
        startActivity(intent);
    }


    public void analyzeButton(View view) {

        if (!isAnalyzing) {
            startAnalyzing();
        } else {
            stopAnalyzing();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save boolean to maintain scanning when orientation is changed
        outState.putBoolean("analyzing", isAnalyzing);
        outState.putBoolean("tracking sound", isTrackingSound);
        outState.putInt("sound occurrences", noiseCounter);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //receive boolean maintain scanning when orientation is changed
        noiseCounter = savedInstanceState.getInt("sound occurrences");
        isAnalyzing = savedInstanceState.getBoolean("analyzing");
        isTrackingSound = savedInstanceState.getBoolean("tracking sound");

    }


    @Override
    protected void onResume() {

        //Restore the sensor listeners
        if (isAnalyzing) {
            startAnalyzing();
        }

        //keep tracking sound
        if(isTrackingSound){
            soundTxt.setText("Loud noise occurrences: "+ noiseCounter);

            soundRating = getSoundRating(noiseCounter);
            soundRatingText.setText(soundRating);
            startTrackingSound();
        }
        super.onResume();
    }


    @Override
    protected void onPause() {

        mySensorManager.unregisterListener(this);
        super.onPause();

    }


    @Override
    protected void onDestroy() {

        stopTrackingSound();
        super.onDestroy();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float[] sensorVals = event.values;

        switch (event.sensor.getType()) {

            case Sensor.TYPE_LIGHT:
                if (lightSensor != null) {

                    lightReading = sensorVals[0];

                    //only get reading if device is flat
                    if (isFlat) {


                        //rating algorithm
                        rating = (float)(sensorVals[0]/deviation);

                        if (rating > 10)
                            rating = 10.0f;
                        ///////////////////////////

                        ratingStr = getRating(rating);

                        //update text views
                        ratingTxt.setText(ratingStr);
                        scoreTxt.setText("Score is: " + rating + "/10");
                        saveBtn.setVisibility(View.VISIBLE);

                    } else {
                        ratingTxt.setText("");
                        scoreTxt.setText("Place the phone flat on the table.");
                        saveBtn.setVisibility(View.INVISIBLE);
                    }
                }
                break;

            case Sensor.TYPE_ACCELEROMETER:

                if (accelSensor != null) {

                    if(isAnalyzing) {
                        //normalize the readings
                        double norm_Of_g = Math.sqrt(sensorVals[0] * sensorVals[0] + sensorVals[1] * sensorVals[1] + sensorVals[2] * sensorVals[2]);

                        sensorVals[0] = (float) (sensorVals[0] / norm_Of_g);
                        sensorVals[1] = (float) (sensorVals[1] / norm_Of_g);
                        sensorVals[2] = (float) (sensorVals[2] / norm_Of_g);

                        //calculate the inclination
                        int inclination = (int) Math.round(Math.toDegrees(Math.acos(sensorVals[2])));

                        //checking if the phone is flat
                        if (inclination < 15 || inclination > 165) {
                            //device is flat
                            isFlat = true;

                        } else {
                            isFlat = false;
                        }
                    }
                }
                break;

        }
    }


    //custom method to return the appropriate text rating based on the score
    public String getRating(float r) {

        if (isFlat) {

            if (r > 8.0) {
                ratingTxt.setTextColor(Color.rgb(0, 0, 255));
                return "Excellent";
            }

            if (r <= 8.0 && r > 6.0) {
                ratingTxt.setTextColor(Color.rgb(175, 175, 0));
                return "Good";
            }

            if (r <= 6.0 && r > 4.0) {
                ratingTxt.setTextColor(Color.rgb(102, 102, 102));
                return "Okay";
            }

            if (r <= 4.0) {
                ratingTxt.setTextColor(Color.rgb(255, 0, 0));
                return "Bad";
            }

        }

        return "";
    }

    //custom method to return the appropriate text rating based on the score
    public String getSoundRating(int soundOccurrences) {


            if (soundOccurrences > soundOccurThreshold) {
                soundRatingText.setTextColor(Color.rgb(255, 0, 0));
                return "Extremely noisy";
            }

            double ratio = (float)soundOccurrences/soundOccurThreshold;
            Log.d("ratio", ratio +"");

            if (ratio > .75 ) {
                soundRatingText.setTextColor(Color.rgb(102, 102, 102));
                return "Too noisy";
            }

            if (ratio > 0.5) {
                soundRatingText.setTextColor(Color.rgb(175, 175, 0));
                return "Moderate noise";
            }

            if (ratio <= 0.5) {
                soundRatingText.setTextColor(Color.rgb(0, 0, 255));
                return "Minimal noise";
            }


        return "none";
    }


    public void trackSoundButton(View v) {


        if (!isTrackingSound) {
            isTrackingSound = true;
            startTrackingSound();

        } else {
            isTrackingSound = false;
            stopTrackingSound();
        }
    }


        //enables microphone
        public void startTrackingSound() {

            try {
                soundButton.setText("Stop Tracking");
                Log.i("Noise", "==== start ===");
                soundTracker.start();

                //Noise monitoring start
                mHandler.postDelayed(soundTrackingTask, 300);
            }
            catch(Exception e){

            }
        }

        //disables microphone
        public void stopTrackingSound() {

        try {
            Log.d("Noise", "==== Stop Noise Monitoring===");
            soundButton.setText("Track Sound");
            mHandler.removeCallbacks(soundTrackingTask);
            soundTracker.stop();
        }
        catch (Exception e){

        }

    }

    //disables light sensor and accelerometer and stops analyzing
        public void stopAnalyzing(){
            isAnalyzing = false;
            scoreTxt.setText("");
            saveBtn.setVisibility(View.INVISIBLE);
            startBtn.setText("Start");
            ratingTxt.setText("Press start");
            imgView.setVisibility(View.INVISIBLE);
            mySensorManager.unregisterListener(this);
        }

        //enables light sensor, accelerometer and begins analyzing
    public void startAnalyzing(){
        isAnalyzing = true;
        startBtn.setText("Stop");
        imgView.setVisibility(View.VISIBLE);

        if (lightSensor != null)
            mySensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (accelSensor != null)
            mySensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



}


