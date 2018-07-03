package com.stefanblos.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stefanblos.popularmovies.Model.Movie;

import java.util.ArrayList;

/**
 *  Adapter that manages the content of the RecyclerView holding the Movie Posters
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView moviePosterImageView;

        ViewHolder(View v) {
            super(v);
            moviePosterImageView = v.findViewById(R.id.iv_movie_poster);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickListener.onMoviePosterClicked(mMovieList.get(position), moviePosterImageView);
        }

    }

    private ArrayList<Movie> mMovieList;
    private Context mContext;
    private final OnMoviePosterClickedListener mClickListener;

    MovieListAdapter(ArrayList<Movie> movies, Context context) {
        mMovieList = movies;
        mContext = context;
        mClickListener = (OnMoviePosterClickedListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);
        Picasso.with(mContext)
                .load(movie.getImageLink())
                .into(holder.moviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMovies(ArrayList<Movie> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }

    public interface OnMoviePosterClickedListener {
        void onMoviePosterClicked(Movie clickedMovie, ImageView imageView);
    }

}
