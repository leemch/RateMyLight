package com.example.leetop.lab5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

import static java.net.Proxy.Type.HTTP;

public class locationInfoActivity extends AppCompatActivity {

    //image view to show photo
    ImageView imgView;

    TextView infoTxt;
    MyDatabase db;
    String[]  results;

    String index, infoStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_info);

        //Text view to display the info
        infoTxt = findViewById(R.id.infoText);

        //Analyzing data
        Bundle extras = getIntent().getExtras();
        String info = extras.getString("info");
        index = extras.getString("index");

        //all data is in a comma delimited string
        //we use split to put it into an array
        results = info.split(",");


        infoStr = "Name: "  + results[0] + "\n" + "Comment: " + results[4] + "\n" + "Score: "  + results[5] + "/10" + "\n" + "Rating: " + results[6] + "\n" + "Sound rating: " + results[7];
        infoTxt.setText(infoStr);

        imgView = findViewById(R.id.imageView);

        previewCapturedImage(results[3]);
    }




    public void deleteButton(View view){

        //get a reference to the database and call the delete method based on Name.
        db = new MyDatabase(this);
        if(db.deleteData(results[0]) > 0){
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }


        //Only delete image if it exists
        if(!results[3].equals("none")) {
            //int cnt = this.getContentResolver().delete(Uri.parse(results[3]), null, null);
            File fdelete = new File(results[3]);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + results[3]);
                } else {
                    System.out.println("file not Deleted :" + results[3]);
                }
            }
        }

        //close the activity
        this.finish();

    }


    public void viewMapButton(View view){


        //gets the latitude and longitude from the results and calls an implicit intent to show it on a map
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" +results[1]+ ">,<" + results[2]
        +">?q=<" +results[1]+ ">,<" + results[2] +">"));

        startActivity(intent);


    }


    //display the photo by using the CameraUtils class
    private void previewCapturedImage(String path) {
        try {
            Log.d("preview", path);
            final Bitmap bitmap = CameraUtils.scaleDownAndRotatePic(path);
            imgView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
