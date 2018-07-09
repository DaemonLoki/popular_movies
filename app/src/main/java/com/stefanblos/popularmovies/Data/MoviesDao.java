package com.stefanblos.popularmovies.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.stefanblos.popularmovies.Model.Movie;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> loadAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE id == :id")
    LiveData<Movie> loadMovieById(int id);
}
