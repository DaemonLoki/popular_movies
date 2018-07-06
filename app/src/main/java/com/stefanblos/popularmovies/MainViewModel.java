package com.stefanblos.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.stefanblos.popularmovies.Data.AppDatabase;
import com.stefanblos.popularmovies.Model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application app) {
        super(app);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        movies = database.moviesDao().loadAllMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
