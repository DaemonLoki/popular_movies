package com.stefanblos.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.stefanblos.popularmovies.Model.Movie;
import com.stefanblos.popularmovies.Util.Constants;
import com.stefanblos.popularmovies.Util.HttpHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.OnMoviePosterClickedListener {

    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private MovieListAdapter mAdapter;
    private String mSearchType = HttpHelper.MOVIE_DB_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchMovies();

        RecyclerView recyclerView = findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieListAdapter(mMovieList, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchMovies() {
        String apiKey = getString(R.string.moviedb_api_key);
        URL url = HttpHelper.createMovieDBUrl(mSearchType, apiKey);
        new MovieFetchTask().execute(url);
    }

    @Override
    public void onMoviePosterClicked(Movie clickedMovie, ImageView imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Constants.INTENT_MOVIE_EXTRA, clickedMovie);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,
                        imageView, ViewCompat.getTransitionName(imageView));
        startActivity(intent, options.toBundle());
    }

    /**
     * My task to fetch movies from the MovieDB database
     */
    class MovieFetchTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            URL url = urls[0];

            try {
                String results = HttpHelper.getResponseFromHttpUrl(url);
                return HttpHelper.getMoviesFromJSONString(results);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if (movies != null) {
                mMovieList = movies;
                mAdapter.setMovies(mMovieList);
                String title = "Movies";
                switch (mSearchType) {
                    case HttpHelper.MOVIE_DB_POPULAR:
                        title = "Popular Movies";
                        break;
                    case HttpHelper.MOVIE_DB_TOP_RATED:
                        title = "Top Rated Movies";
                        break;
                }
                setTitle(title);
            }
        }
    }
}
