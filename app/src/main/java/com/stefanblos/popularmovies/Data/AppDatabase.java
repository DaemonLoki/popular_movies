package com.stefanblos.popularmovies.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.stefanblos.popularmovies.Model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    private final static String TAG = AppDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movielist";
    private static AppDatabase mInstance;

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return mInstance;
    }

    public abstract MoviesDao moviesDao();

}
