package com.example.leetop.lab5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase (Context c){
        context = c;
        helper = new MyHelper(context);
    }

    //writes to the Sqlite database
    public long insertData (String name, double lat, double lng, String comment, float score, String rating, String image, String soundRating)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.LAT, lat);
        contentValues.put(Constants.LONG, lng);
        contentValues.put(Constants.COMMENT, comment);
        contentValues.put(Constants.SCORE, score);
        contentValues.put(Constants.RATING, rating);
        contentValues.put(Constants.IMAGE, image);
        contentValues.put(Constants.SOUND_RATING, soundRating);


        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    //returns all data from a row
    public Cursor getData()
    {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {Constants.UID, Constants.NAME, Constants.LAT, Constants.LONG, Constants.IMAGE, Constants.COMMENT, Constants.SCORE, Constants.RATING, Constants.SOUND_RATING};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }


    //deletes a row based on the name
    public int deleteData(String name)
    {
        db = helper.getWritableDatabase();

        return db.delete(Constants.TABLE_NAME, "Name = ?", new String[]  {name});

    }

}
