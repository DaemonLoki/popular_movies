package com.stefanblos.popularmovies.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stefanblos.popularmovies.Data.AppDatabase;
import com.stefanblos.popularmovies.Model.Movie;

public class MovieDetailViewModel extends ViewModel {

    private LiveData<Movie> movie;

    MovieDetailViewModel(AppDatabase database, int movieId) {
        movie = database.moviesDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
