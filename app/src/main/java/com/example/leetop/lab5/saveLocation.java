package com.example.leetop.lab5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import static java.lang.System.out;

public class saveLocation extends AppCompatActivity {


    //Reference to sqlite database
    MyDatabase db;

    EditText nameTxt, commentTxt;

    //For GPS Location
    LocationManager lm;
    double longitude;
    double latitude ;

    float score;
    String rating;
    String soundRating;

    ImageView imgView;

    Bitmap imageBitmap;
    String uri = "none";

    //Photo intent request code
    static final int REQUEST_TAKE_PHOTO = 1;

    //Path where image is stored.  This var will be stored in the database.
    String mCurrentPhotoPath = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_location);
        db = new MyDatabase(this);

        imageBitmap = null;

        longitude = 0.0;
        latitude = 0.0;

        soundRating = "none";

        try {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if(lm != null) {

                //Update the current GPS coordinates
                lm.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }
                            @Override
                            public void onProviderEnabled(String provider) {
                            }
                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                            @Override
                            public void onLocationChanged(final Location location) {

                                //Updates the current GPS coordinates in case the user has moved
                                longitude = location.getLongitude();
                                latitude = location.getLatitude();
                            }
                        });

                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                //longitude = location.getLongitude();
                //latitude = location.getLatitude();
            }

        }
        catch (SecurityException e){

        }




        nameTxt = findViewById(R.id.nameText);
        commentTxt = findViewById(R.id.commentText);

        //get data from analyzing activity
        Bundle extras = getIntent().getExtras();
        score = extras.getFloat("score", 0.0f);
        rating  = extras.getString("rating", "none");
        soundRating  = extras.getString("sound rating", "none");



        imgView = findViewById(R.id.imageView);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //save image path for when orientation is changed
        outState.putString("imagePath", mCurrentPhotoPath);
        outState.putDouble("lat", latitude);
        outState.putDouble("long", longitude);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Restore image when orientation is changed
        //mCurrentPhotoPath = savedInstanceState.getString("imagePath");
        latitude = savedInstanceState.getDouble("lat");
        latitude = savedInstanceState.getDouble("long");

        //if(!mCurrentPhotoPath.equals("none")){
        //    previewCapturedImage();
        //}

    }



    public void photoButton(View v) {
        //launch photo taking app
        dispatchTakePictureIntent(REQUEST_TAKE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if photo taking was successful
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_TAKE_PHOTO:
                    previewCapturedImage();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(this, "User cancelled the capture", Toast.LENGTH_SHORT).show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
        }


    }



    //This is where the data is written to the database
    public void addButton(View v){

        String name = nameTxt.getText().toString();
        String comment = commentTxt.getText().toString();


        //only if the image exists
        if(imageBitmap != null){

            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            if(mCurrentPhotoPath == null){
                mCurrentPhotoPath = "none";
            }
        }


        //Write all the data to the database
        long id = db.insertData(name, latitude, longitude, comment, score, rating, mCurrentPhotoPath, soundRating);
        if (id < 0)
        {
            Toast.makeText(this, "Saving failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
        }

        //close the activity
        this.finish();
    }





    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("ex", "cannot create file");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }


    private void previewCapturedImage() {
        try {
            Log.d("preview", mCurrentPhotoPath);
            final Bitmap bitmap = CameraUtils.scaleDownAndRotatePic(mCurrentPhotoPath);
            imgView.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
