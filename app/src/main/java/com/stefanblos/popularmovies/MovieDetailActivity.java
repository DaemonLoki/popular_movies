package com.stefanblos.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
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
import com.stefanblos.popularmovies.Util.Constants;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    // UI
    private View mBackgroundView;
    private TextView mTitleTV;
    private LinearLayout mSubTitleLL;
    private TextView mDescriptionTV;
    private ImageView mFavoriteImageView;

    private static final float ANIMATION_OFFSET = 800.0f;

    private AppDatabase mDb;

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
        mTitleTV = findViewById(R.id.tv_movie_title);
        mSubTitleLL = findViewById(R.id.ll_sub_title);
        TextView voteAvgTV = findViewById(R.id.tv_vote_avg);
        TextView releaseDateTV = findViewById(R.id.tv_release_date);
        mDescriptionTV = findViewById(R.id.tv_movie_overview);

        // setup movie image
        ImageView movieImageView = findViewById(R.id.iv_movie_poster);
        final Movie movie = intent.getParcelableExtra(Constants.INTENT_MOVIE_EXTRA);
        Picasso.with(this).load(movie.getImageLink()).into(movieImageView);

        String voteAvgText = String.valueOf(movie.getVoteAvg()) + " / 10";
        voteAvgTV.setText(voteAvgText);
        releaseDateTV.setText(movie.getReleaseDate());
        mDescriptionTV.setText(movie.getOverview());

        setTitle(movie.getTitle());

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (mDb.moviesDao().loadMovieById(movie.getId()) != null) {
            movie.setFavorite(true);
        }
        setFavoriteIcon(movie);
        mFavoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movie.setFavorite(!movie.isFavorite());
                setFavoriteIcon(movie);
                if (movie.isFavorite()) {
                    mDb.moviesDao().insertMovie(movie);
                } else {
                    mDb.moviesDao().deleteMovie(movie);
                }
            }
        });

        enterAnimation();
    }

    private void setFavoriteIcon(Movie movie) {
        Drawable d = getDrawable(movie.isFavorite()
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

    /*
     * Method that will do the enter animation
     */
    private void enterAnimation() {
        mBackgroundView.setAlpha(0.0f);
        mTitleTV.setAlpha(0.0f);
        mSubTitleLL.setAlpha(0.0f);
        mDescriptionTV.setAlpha(0.0f);
        mTitleTV.setTranslationY(ANIMATION_OFFSET);
        mSubTitleLL.setTranslationY(ANIMATION_OFFSET);
        mDescriptionTV.setTranslationY(ANIMATION_OFFSET);

        mBackgroundView.animate()
                .setStartDelay(100)
                .setDuration(500)
                .alpha(0.6f);

        mTitleTV.animate()
                .setStartDelay(600)
                .setDuration(600)
                .translationYBy(-ANIMATION_OFFSET)
                .setInterpolator(new FastOutSlowInInterpolator())
                .alpha(1.0f);

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
}
