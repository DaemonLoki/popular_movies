package com.stefanblos.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stefanblos.popularmovies.Model.Movie;
import com.stefanblos.popularmovies.Util.Constants;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(Constants.INTENT_MOVIE_EXTRA)) {
            Toast.makeText(this, "Couldn't find movie!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Movie was not set on Intent!");
            finish();
            return;
        }

        ImageView movieImageView = findViewById(R.id.iv_movie_poster);

        Movie movie = intent.getParcelableExtra(Constants.INTENT_MOVIE_EXTRA);
        Picasso.with(this).load(movie.getImageLink()).into(movieImageView);

        setTitle(movie.getTitle());
    }
}
