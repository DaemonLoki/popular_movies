package com.stefanblos.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stefanblos.popularmovies.Model.Movie;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> mMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_movies);

        // Mock data, TO BE REMOVE:
        mMovieList.add(new Movie("Test Movie 1", "empty"));
        mMovieList.add(new Movie("Test Movie 2", "empty"));
        mMovieList.add(new Movie("Test Movie 3", "empty"));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        MovieListAdapter adapter = new MovieListAdapter(mMovieList);
        recyclerView.setAdapter(adapter);
    }
}
