package com.stefanblos.popularmovies.Util;

import android.os.AsyncTask;

import com.stefanblos.popularmovies.Model.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * My task to fetch movies from the MovieDB database
 */
public class MovieFetchTask extends AsyncTask<URL, Void, ArrayList<Movie>> {

    private OnMovieTaskCompleted mTaskCompleted;

    public MovieFetchTask(OnMovieTaskCompleted taskCompleted) {
        mTaskCompleted = taskCompleted;
    }

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
            mTaskCompleted.onFetchMovieTaskCompleted(movies);
        }
    }

    public interface OnMovieTaskCompleted {
        void onFetchMovieTaskCompleted(ArrayList<Movie> movies);
    }
}
