package com.example.ratings.storeratings;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class StoreRating {

    public static class RatingEntry implements BaseColumns{
        static final String TABLE_NAME = "ratings";
        static final String RATING_TIMESTAMP = "rating_timestamp";
        static final String RATING = "rating";
        static final String TOTAL_STARS = "total_stars";

    }

}
