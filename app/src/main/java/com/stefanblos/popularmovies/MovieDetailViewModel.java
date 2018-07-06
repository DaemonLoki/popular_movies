package com.stefanblos.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.stefanblos.popularmovies.Data.AppDatabase;
import com.stefanblos.popularmovies.Model.Movie;

public class MovieDetailViewModel extends ViewModel {

    private static final String TAG = MovieDetailViewModel.class.getSimpleName();

    private LiveData<Movie> movie;

    public MovieDetailViewModel(AppDatabase database, int movieId) {
        movie = database.moviesDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
