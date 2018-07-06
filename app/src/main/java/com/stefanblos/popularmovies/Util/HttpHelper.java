package com.stefanblos.popularmovies.Util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stefanblos.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();

    // NETWORKING CONSTANTS
    final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    final static String MOVIE_DB_API_KEY_QUERY = "api_key";
    public final static String MOVIE_DB_POPULAR = "popular";
    public final static String MOVIE_DB_TOP_RATED = "top_rated";
    public final static String MOVIE_DB_FAVORITES = "favorites";
    final static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342";

    // MOVIE_DB CONSTANTS
    final static String KEY_ID = "id";
    final static String KEY_TITLE = "title";
    final static String KEY_POSTER_PATH = "poster_path";
    final static String KEY_OVERVIEW = "overview";
    final static String KEY_VOTE_AVERAGE = "vote_average";
    final static String KEY_RELEASE_DATE = "release_date";

    /*
     * This method is intended to create the URL for the AsyncTask that fetches the movie list
     * depending on the search type (popular or top rated) that is currently selected
     */
    @Nullable
    public static URL createMovieDBUrl(String searchType, String apiKey) {
        Uri builtUri =
                Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                        .appendPath(searchType)
                        .appendQueryParameter(MOVIE_DB_API_KEY_QUERY, apiKey)
                        .build();
        Log.d(TAG, "Built uri is: " + builtUri.toString());
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL could not be created!", e.fillInStackTrace());
            return null;
        }
    }

    /*
     * Method to get a response from an HTTPS request like the one to the MovieDB database is.
     * Pretty similar to the Udacity example code, only changed from http to https.
     */
    @Nullable
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /*
     * Helper to extract all Movies from the JSON string that is the response from the request
     */
    public static ArrayList<Movie> getMoviesFromJSONString(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObj.getJSONArray("results");
            ArrayList<Movie> movies = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                Movie movie = getMovieFromJSONObject(resultsArray.getJSONObject(i));
                if (movie != null) {
                    movies.add(movie);
                }
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /*
     * Method to get a single movie with all attributes from a JSON object
     */
    @Nullable
    private static Movie getMovieFromJSONObject(JSONObject jsonObj) {
        try {
            Integer id = jsonObj.getInt(KEY_ID);
            String title = jsonObj.getString(KEY_TITLE);
            String posterPath = jsonObj.getString(KEY_POSTER_PATH);
            String imageUrl = POSTER_BASE_URL + posterPath;
            String description = jsonObj.getString(KEY_OVERVIEW);
            float vote_avg = (float) jsonObj.getDouble(KEY_VOTE_AVERAGE);
            String releaseDate = jsonObj.getString(KEY_RELEASE_DATE);
            return new Movie(id, title, imageUrl, description, vote_avg, releaseDate);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
