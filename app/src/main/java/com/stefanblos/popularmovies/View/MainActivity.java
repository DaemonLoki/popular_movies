package com.stefanblos.popularmovies.View;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.stefanblos.popularmovies.Model.Movie;
import com.stefanblos.popularmovies.R;
import com.stefanblos.popularmovies.Util.Constants;
import com.stefanblos.popularmovies.Util.HttpHelper;
import com.stefanblos.popularmovies.Util.MovieFetchTask;
import com.stefanblos.popularmovies.ViewModel.MainViewModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MovieListAdapter.OnMoviePosterClickedListener, MovieFetchTask.OnMovieTaskCompleted {

    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private MovieListAdapter mAdapter;
    private String mSearchType = HttpHelper.MOVIE_DB_POPULAR;
    private GridLayoutManager mLayoutManager;
    private Parcelable mGridState;

    private final static String GRID_STATE_KEY = "GRID_STATE_KEY";
    private final static String SEARCH_TYPE_KEY = "SEARCH_TYPE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv_movies);
        mAdapter = new MovieListAdapter(mMovieList, this);
        recyclerView.setAdapter(mAdapter);

        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mSearchType.equals(HttpHelper.MOVIE_DB_FAVORITES)) {
            fetchMovies();
        } else {
            setupViewModelForFavorites();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mGridState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(GRID_STATE_KEY, mGridState);
        outState.putString(SEARCH_TYPE_KEY, mSearchType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mGridState = savedInstanceState.getParcelable(GRID_STATE_KEY);
            mSearchType = savedInstanceState.getString(SEARCH_TYPE_KEY);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mGridState = null;
        switch (item.getItemId()) {
            case R.id.menu_item_popular:
                mSearchType = HttpHelper.MOVIE_DB_POPULAR;
                fetchMovies();
                break;
            case R.id.menu_item_top_rated:
                mSearchType = HttpHelper.MOVIE_DB_TOP_RATED;
                fetchMovies();
                break;
            case R.id.menu_item_favorites:
                mSearchType = HttpHelper.MOVIE_DB_FAVORITES;
                setTitle(getString(R.string.favorites_title));
                setupViewModelForFavorites();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setupViewModelForFavorites() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final LiveData<List<Movie>> moviesLiveData = viewModel.getMovies();
        moviesLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (mSearchType.equals(HttpHelper.MOVIE_DB_FAVORITES)) {
                    mAdapter.setMovies(movies);
                } else {
                    if (movies != null) {
                        moviesLiveData.removeObserver(this);
                    }
                }
            }
        });
    }

    private void fetchMovies() {
        String apiKey = getString(R.string.moviedb_api_key);
        URL url = HttpHelper.createMovieDBUrl(mSearchType, apiKey);
        new MovieFetchTask(MainActivity.this).execute(url);
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

    @Override
    public void onFetchMovieTaskCompleted(ArrayList<Movie> movies) {
        mMovieList = movies;
        mAdapter.setMovies(mMovieList);
        if (mGridState != null) {
            mLayoutManager.onRestoreInstanceState(mGridState);
        } else {
            mLayoutManager.scrollToPosition(0);
        }
        String title = getString(R.string.movies_title);
        switch (mSearchType) {
            case HttpHelper.MOVIE_DB_POPULAR:
                title = getString(R.string.popular_movies_title);
                break;
            case HttpHelper.MOVIE_DB_TOP_RATED:
                title = getString(R.string.top_rated_movies_title);
                break;
        }
        setTitle(title);
    }
}
