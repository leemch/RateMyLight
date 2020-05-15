package com.example.leetop.lab5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    EditText usernameTextView, passwordTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameTextView = findViewById(R.id.userLogin);
        passwordTextView = findViewById(R.id.passLogin);
        getSupportActionBar().setTitle("Login Menu");
    }

    public void retrieve (View view){

        //get access to the shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", null);
        String password = sharedPrefs.getString("password", null);

        //if user name and password are correct
        if (username.equals(usernameTextView.getText().toString())&&password.equals(passwordTextView.getText().toString()))
        {
            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();

            //launch the main menu
            Intent intent= new Intent(this, MainMenu.class);
            startActivity(intent);
        }
        else
        {
            //if login info is incorrect, send to registration menu
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
            Toast.makeText(this, "No login info found", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void gotoRegister (View view){
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("firstTime", true);
        editor.commit();

        Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
