package com.stefanblos.popularmovies.View;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stefanblos.popularmovies.Model.Review;
import com.stefanblos.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView authorTextView;
        final TextView contentTextView;

        ViewHolder(View v) {
            super(v);
            authorTextView = v.findViewById(R.id.tv_review_author);
            contentTextView = v.findViewById(R.id.tv_review_text);
        }
    }

    private List<Review> mReviewList;

    ReviewListAdapter() {
        mReviewList = new ArrayList<>();
    }

    public void setReviewList(List<Review> reviews) {
        mReviewList = reviews;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = mReviewList.get(position);
        holder.authorTextView.setText(review.getAuthor());
        holder.contentTextView.setText(review.getText());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}
