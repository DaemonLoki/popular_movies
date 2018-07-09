package com.stefanblos.popularmovies.Util;

import android.os.AsyncTask;

import com.stefanblos.popularmovies.Model.Trailer;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class VideoLinkFetchTask extends AsyncTask<URL, Void, List<Trailer>> {

    private OnVideoTaskCompleted mTaskCompleted;

    public VideoLinkFetchTask(OnVideoTaskCompleted taskCompleted) {
        mTaskCompleted = taskCompleted;
    }

    @Override
    protected List<Trailer> doInBackground(URL... urls) {
        URL url = urls[0];

        try {
            String results = HttpHelper.getResponseFromHttpUrl(url);
            return HttpHelper.getTrailersFromJSONString(results);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if (trailers != null) {
            mTaskCompleted.onFetchVideoLinkCompleted(trailers);
        }
    }

    public interface OnVideoTaskCompleted {
        void onFetchVideoLinkCompleted(List<Trailer> trailers);
    }
}
