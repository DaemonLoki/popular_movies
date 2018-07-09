package com.stefanblos.popularmovies.Util;

import android.os.AsyncTask;

import com.stefanblos.popularmovies.Model.Review;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ReviewFetchTask extends AsyncTask<URL, Void, List<Review>> {

    private OnReviewTaskCompleted mTaskCompleted;

    public ReviewFetchTask(OnReviewTaskCompleted taskCompleted) {
        mTaskCompleted = taskCompleted;
    }

    @Override
    protected List<Review> doInBackground(URL... urls) {
        URL url = urls[0];

        try {
            String results = HttpHelper.getResponseFromHttpUrl(url);
            return HttpHelper.getReviewsFromJSONString(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (reviews != null) {
            mTaskCompleted.onFetchReviewsTaskCompleted(reviews);
        }
    }

    public interface OnReviewTaskCompleted {
        void onFetchReviewsTaskCompleted(List<Review> reviews);
    }
}
