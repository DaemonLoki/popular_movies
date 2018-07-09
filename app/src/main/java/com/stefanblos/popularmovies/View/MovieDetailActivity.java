package com.stefanblos.popularmovies.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stefanblos.popularmovies.Data.AppDatabase;
import com.stefanblos.popularmovies.Model.Movie;
import com.stefanblos.popularmovies.Model.Review;
import com.stefanblos.popularmovies.Model.Trailer;
import com.stefanblos.popularmovies.R;
import com.stefanblos.popularmovies.Util.AppExecutors;
import com.stefanblos.popularmovies.Util.Constants;
import com.stefanblos.popularmovies.Util.HttpHelper;
import com.stefanblos.popularmovies.Util.ReviewFetchTask;
import com.stefanblos.popularmovies.Util.VideoLinkFetchTask;
import com.stefanblos.popularmovies.ViewModel.MovieDetailViewModel;
import com.stefanblos.popularmovies.ViewModel.MovieDetailViewModelFactory;

import java.net.URL;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements
        TrailerListAdapter.OnTrailerCardClickedListener, ReviewFetchTask.OnReviewTaskCompleted,
        VideoLinkFetchTask.OnVideoTaskCompleted {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    // UI
    private View mBackgroundView;
    private LinearLayout mSubTitleLL;
    private TextView mDescriptionTV;
    private ImageView mFavoriteImageView;

    private static final float ANIMATION_OFFSET = 800.0f;

    private AppDatabase mDb;
    private Movie mMovie;
    private boolean isFavoriteMovie = false;
    private ReviewListAdapter mReviewsAdapter;
    private TrailerListAdapter mTrailersAdapter;

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

        mFavoriteImageView = findViewById(R.id.iv_favorite);
        mBackgroundView = findViewById(R.id.view_background);
        mSubTitleLL = findViewById(R.id.ll_sub_title);
        TextView voteAvgTV = findViewById(R.id.tv_vote_avg);
        TextView releaseDateTV = findViewById(R.id.tv_release_date);
        mDescriptionTV = findViewById(R.id.tv_movie_overview);

        // setup movie image
        ImageView movieImageView = findViewById(R.id.iv_movie_poster);
        mMovie = intent.getParcelableExtra(Constants.INTENT_MOVIE_EXTRA);
        Picasso.with(this).load(mMovie.getImageLink()).into(movieImageView);

        String voteAvgText = String.valueOf(mMovie.getVoteAvg()) + " / 10";
        voteAvgTV.setText(voteAvgText);
        releaseDateTV.setText(mMovie.getReleaseDate());
        mDescriptionTV.setText(mMovie.getOverview());

        setTitle(mMovie.getTitle());

        mDb = AppDatabase.getInstance(getApplicationContext());

        setupViewModel();
        mFavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (isFavoriteMovie) {
                        mDb.moviesDao().deleteMovie(mMovie);
                    } else {
                        mDb.moviesDao().insertMovie(mMovie);
                    }
                }
            });
            }
        });

        setupReviewsRecyclerView();

        setupTrailersRecyclerView();

        fetchReviews();

        fetchVideoLinks();

        enterAnimation();
    }

    private void setupReviewsRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        mReviewsAdapter = new ReviewListAdapter();
        recyclerView.setAdapter(mReviewsAdapter);
    }

    private void setupTrailersRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        mTrailersAdapter = new TrailerListAdapter(this);
        recyclerView.setAdapter(mTrailersAdapter);
    }

    private void setupViewModel() {
        MovieDetailViewModelFactory f = new MovieDetailViewModelFactory(mDb, mMovie.getId());
        final MovieDetailViewModel viewModel =
                ViewModelProviders.of(this, f).get(MovieDetailViewModel.class);
        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                isFavoriteMovie = (movie != null);
                setFavoriteIcon();
            }
        });
    }

    private void setFavoriteIcon() {
        Drawable d = getDrawable(isFavoriteMovie
                ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        mFavoriteImageView.setImageDrawable(d);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
        }
        return true;
    }

    private void fetchReviews() {
        String apiKey = getString(R.string.moviedb_api_key);
        URL reviewsUrl = HttpHelper.createMovieReviewsUrl(String.valueOf(mMovie.getId()), apiKey);
        new ReviewFetchTask(MovieDetailActivity.this).execute(reviewsUrl);
    }

    private void fetchVideoLinks() {
        String apiKey = getString(R.string.moviedb_api_key);
        URL videosUrl = HttpHelper.createMovieVideosUrl(String.valueOf(mMovie.getId()), apiKey);
        new VideoLinkFetchTask(MovieDetailActivity.this).execute(videosUrl);
    }

    /*
     * Method that will do the enter animation
     */
    private void enterAnimation() {
        mBackgroundView.setAlpha(0.0f);
        mSubTitleLL.setAlpha(0.0f);
        mDescriptionTV.setAlpha(0.0f);
        mSubTitleLL.setTranslationY(ANIMATION_OFFSET);
        mDescriptionTV.setTranslationY(ANIMATION_OFFSET);

        mBackgroundView.animate()
                .setStartDelay(100)
                .setDuration(500)
                .alpha(0.6f);

        mSubTitleLL.animate()
                .setStartDelay(700)
                .setDuration(700)
                .translationYBy(-ANIMATION_OFFSET)
                .setInterpolator(new FastOutSlowInInterpolator())
                .alpha(1.0f);

        mDescriptionTV.animate()
                .setStartDelay(800)
                .setDuration(800)
                .translationYBy(-ANIMATION_OFFSET)
                .setInterpolator(new FastOutSlowInInterpolator())
                .alpha(1.0f);
    }

    @Override
    public void onTrailerCardClicked(String key) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                HttpHelper.createYoutubeURLByKey(key));
        getApplicationContext().startActivity(webIntent);
    }

    @Override
    public void onFetchReviewsTaskCompleted(List<Review> reviews) {
        mReviewsAdapter.setReviewList(reviews);
    }

    @Override
    public void onFetchVideoLinkCompleted(List<Trailer> trailers) {
        mTrailersAdapter.setTrailerList(trailers);
    }
}
