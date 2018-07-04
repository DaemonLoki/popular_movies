package com.stefanblos.popularmovies;

import android.content.Intent;
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
import com.stefanblos.popularmovies.Model.Movie;
import com.stefanblos.popularmovies.Util.Constants;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    // UI
    private View mBackgroundView;
    private TextView mTitleTV;
    private LinearLayout mSubTitleLL;
    private TextView mDescriptionTV;

    private static final float ANIMATION_OFFSET = 800.0f;

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

        mBackgroundView = findViewById(R.id.view_background);
        mTitleTV = findViewById(R.id.tv_movie_title);
        mSubTitleLL = findViewById(R.id.ll_sub_title);
        TextView voteAvgTV = findViewById(R.id.tv_vote_avg);
        TextView releaseDateTV = findViewById(R.id.tv_release_date);
        mDescriptionTV = findViewById(R.id.tv_movie_overview);

        // setup movie image
        ImageView movieImageView = findViewById(R.id.iv_movie_poster);
        Movie movie = intent.getParcelableExtra(Constants.INTENT_MOVIE_EXTRA);
        Picasso.with(this).load(movie.getImageLink()).into(movieImageView);

        String voteAvgText = String.valueOf(movie.getVoteAvg()) + " / 10";
        voteAvgTV.setText(voteAvgText);
        releaseDateTV.setText(movie.getReleaseDate());
        mDescriptionTV.setText(movie.getOverview());

        setTitle(movie.getTitle());

        enterAnimation();
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