package com.example.ratings.storeratings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class RatingDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Ratings.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StoreRating.RatingEntry.TABLE_NAME + " (" +
                    StoreRating.RatingEntry._ID + " INTEGER PRIMARY KEY," +
                    StoreRating.RatingEntry.RATING_TIMESTAMP + " TIMESTAMP," +
                    StoreRating.RatingEntry.RATING + " INT," +
                    StoreRating.RatingEntry.TOTAL_STARS + " INT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StoreRating.RatingEntry.TABLE_NAME;

    public RatingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addToDB(long timestamp, int reviewStars, int totalStars) throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StoreRating.RatingEntry.RATING_TIMESTAMP, timestamp);
        values.put(StoreRating.RatingEntry.TOTAL_STARS, totalStars);
        values.put(StoreRating.RatingEntry.RATING, reviewStars);

        long row=db.insert(StoreRating.RatingEntry.TABLE_NAME, null, values);
        if(row==-1){
            throw new Exception("Error while storing rating");
        }
    }

    public List<Rating> getDataFromDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sortOrder =
                StoreRating.RatingEntry.RATING_TIMESTAMP + " DESC";
        String[] projection = {
                StoreRating.RatingEntry.RATING_TIMESTAMP,
                StoreRating.RatingEntry.TOTAL_STARS,
                StoreRating.RatingEntry.RATING
        };

        Cursor cursor = db.query(
                StoreRating.RatingEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Rating> items = new ArrayList<>();
        while(cursor.moveToNext()){
            long timestamp = cursor.getLong(cursor.getColumnIndex(StoreRating.RatingEntry.RATING_TIMESTAMP));
            int stars = cursor.getInt(cursor.getColumnIndex(StoreRating.RatingEntry.RATING));
            int totalStars = cursor.getInt(cursor.getColumnIndex(StoreRating.RatingEntry.TOTAL_STARS));
            System.out.println("Rating="+stars);
            items.add(new Rating(timestamp, stars, totalStars));
        }

        cursor.close();
        return items;
    }
}