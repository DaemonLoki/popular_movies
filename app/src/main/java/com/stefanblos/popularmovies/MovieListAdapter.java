package com.stefanblos.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stefanblos.popularmovies.Model.Movie;

import java.util.ArrayList;

/*
    Adapter that manages the content of the RecyclerView holding the Movie Posters
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView movieTitleTextView;

        ViewHolder(View v) {
            super(v);
            movieTitleTextView = v.findViewById(R.id.tv_movie_title);
        }

    }

    private ArrayList<Movie> mMovieList;

    MovieListAdapter(ArrayList<Movie> movies) {
        mMovieList = movies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        holder.movieTitleTextView.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

}
