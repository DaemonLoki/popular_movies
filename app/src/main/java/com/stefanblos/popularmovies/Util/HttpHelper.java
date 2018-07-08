package com.stefanblos.popularmovies.Util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stefanblos.popularmovies.Model.Movie;
import com.stefanblos.popularmovies.Model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();

    // NETWORKING CONSTANTS
    private final static String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private final static String MOVIE_DB_API_KEY_QUERY = "api_key";
    public final static String MOVIE_DB_POPULAR = "popular";
    public final static String MOVIE_DB_TOP_RATED = "top_rated";
    public final static String MOVIE_DB_FAVORITES = "favorites";
    private final static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342";

    // MOVIE_DB CONSTANTS
    private final static String KEY_ID = "id";
    private final static String KEY_TITLE = "title";
    private final static String KEY_POSTER_PATH = "poster_path";
    private final static String KEY_OVERVIEW = "overview";
    private final static String KEY_VOTE_AVERAGE = "vote_average";
    private final static String KEY_RELEASE_DATE = "release_date";

    // REVIEW CONSTANTS
    private final static String MOVIE_DB_REVIEWS_KEY = "reviews";
    private final static String KEY_AUTHOR = "author";
    private final static String KEY_CONTENT = "content";

    // VIDEO CONSTANTS
    private final static String MOVIE_DB_VIDEOS_KEY = "videos";
    private final static String KEY_KEY = "key";
    private final static String KEY_NAME = "name";
    private final static String KEY_SITE = "site";
    private final static String KEY_TYPE = "type";
    private final static String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";
    private final static String YOUTUBE_VIDEO_KEY_QUERY = "v";

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
     * This method creates a URL to fetch the reviews for a movie with the given id
     */
    @Nullable
    public static URL createMovieReviewsUrl(String movieId, String apiKey) {
        Uri builtUri =
                Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(MOVIE_DB_REVIEWS_KEY)
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

    @Nullable
    public static URL createMovieVideosUrl(String movieId, String apiKey) {
        Uri builtUri =
                Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(MOVIE_DB_VIDEOS_KEY)
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

    public static List<Review> getReviewsFromJSONString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            ArrayList<Review> reviews = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                Review review = getReviewFromJSONObject(resultsArray.getJSONObject(i));
                if (review != null) {
                    reviews.add(review);
                }
            }
            return reviews;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<String> getVideoLinksFromJSONString(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.getJSONArray("results");
            ArrayList<String> videoLinks = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String videoLink = getVideoLinkFromJSONObject(resultsArray.getJSONObject(i));
                if (!videoLink.isEmpty()) {
                    videoLinks.add(videoLink);
                }
            }
            return videoLinks;
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

    @Nullable
    private static Review getReviewFromJSONObject(JSONObject jsonObject) {
        try {
            String author = jsonObject.getString(KEY_AUTHOR);
            String content = jsonObject.getString(KEY_CONTENT);
            Log.d(TAG, "Review: author: " + author + ", content: " + content);
            return new Review(author, content);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static String getVideoLinkFromJSONObject(JSONObject jsonObject) {
        try {
            String key = jsonObject.getString(KEY_KEY);
            String name = jsonObject.getString(KEY_NAME);
            String site = jsonObject.getString(KEY_SITE);
            String type = jsonObject.getString(KEY_TYPE);
            URL youtube_link = createYoutubeURLByKey(key);
            Log.d(TAG, "[VIDEO] key: " + key + ", name: " + name + ", site: " + site +
                    ", type: " + String.valueOf(type) + ", youtube_link: " + youtube_link);
            return name;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    private static URL createYoutubeURLByKey(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_VIDEO_KEY_QUERY, key).build();
        Log.d(TAG, "Youtube link is: " + builtUri.toString());
        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL could not be created!", e.fillInStackTrace());
            return null;
        }
    }
}
