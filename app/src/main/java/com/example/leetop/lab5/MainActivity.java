package com.example.leetop.lab5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

//registration
    EditText createUserNameText;
    EditText createPassText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Register Menu");



        createUserNameText = findViewById(R.id.userNameText);
        createPassText = findViewById(R.id.passText);


        //get access to shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        Boolean firstTime = sharedPrefs.getBoolean("firstTime", true);
        String username = sharedPrefs.getString("username", "");


        //first time in app; user needs to register
        if (firstTime)
        {
            Toast.makeText(this, "First time in app", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();
        }
        else
        {
            //if an account exists, send to login menu
            if(!username.equals("")){
                Intent intent= new Intent(this, login.class);
                startActivity(intent);
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();



    }

    public void submit (View view){


        //only create account if username and password are not blank
        if(!createUserNameText.getText().toString().equals("") && !createPassText.getText().toString().equals("")) {

            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("username", createUserNameText.getText().toString());
            editor.putString("password", createPassText.getText().toString());

            //create user settings data
            editor.putInt("threshold", 20);
            editor.putInt("sound threshold", 15);
            editor.putInt("sound occurrence threshold", 50);

            Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
            editor.commit();

            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Username and password cannot be blank", Toast.LENGTH_LONG).show();
        }

    }


}
