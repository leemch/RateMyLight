package com.example.leetop.lab5;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedLocations extends Activity implements AdapterView.OnItemClickListener{
    RecyclerView myRecycler;
    MyDatabase db;
    MyAdapter myAdapter;
    MyHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);
        myRecycler = (RecyclerView) findViewById(R.id.recycler);


    }

    public void makeList(){

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        //get data from the row in the database
        Cursor cursor = db.getData();

        int index1 = cursor.getColumnIndex(Constants.NAME);
        int index2 = cursor.getColumnIndex(Constants.LAT);
        int index3 = cursor.getColumnIndex(Constants.LONG);
        int index4 = cursor.getColumnIndex(Constants.IMAGE);
        int index5 = cursor.getColumnIndex(Constants.COMMENT);
        int index6 = cursor.getColumnIndex(Constants.SCORE);
        int index7 = cursor.getColumnIndex(Constants.RATING);
        int index8 = cursor.getColumnIndex(Constants.SOUND_RATING);


        //insert data to each row of the Recycler view
        ArrayList<String> mArrayList = new ArrayList<String>();

        //go through every row in the data base
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String name = cursor.getString(index1);
            String lat = cursor.getString(index2);
            String lng = cursor.getString(index3);
            String img = cursor.getString(index4);
            String comment = cursor.getString(index5);
            String score = cursor.getString(index6);
            String rating = cursor.getString(index7);
            String soundRating = cursor.getString(index8);

            //insert data to each row of the Recycler view
            String s = name + "," + lat + "," + lng + "," + img + "," + comment + "," + score + "," + rating + "," + soundRating;
            mArrayList.add(s);
            cursor.moveToNext();
        }

        myAdapter = new MyAdapter(mArrayList);
        myRecycler.setAdapter(myAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout clickedRow = (LinearLayout) view;
        TextView nameTxt = (TextView) findViewById(R.id.nameEntry);
        TextView scoreTxt = (TextView) findViewById(R.id.scoreText);
        TextView ratingTxt = (TextView) findViewById(R.id.ratingText);
        TextView infoTxt = (TextView) findViewById(R.id.infoText);


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

        //refresh the list
        makeList();

    }



}
