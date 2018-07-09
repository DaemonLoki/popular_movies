package com.stefanblos.popularmovies.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stefanblos.popularmovies.Model.Trailer;
import com.stefanblos.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView typeTextView;
        final TextView nameTextView;

        ViewHolder(View v) {
            super(v);
            typeTextView = v.findViewById(R.id.tv_video_type);
            nameTextView = v.findViewById(R.id.tv_video_name);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String key = mTrailerList.get(getAdapterPosition()).getKey();
            mListener.onTrailerCardClicked(key);
        }
    }

    private List<Trailer> mTrailerList;
    private OnTrailerCardClickedListener mListener;

    TrailerListAdapter(Context context) {
        mTrailerList = new ArrayList<>();
        mListener = (OnTrailerCardClickedListener) context;
    }

    void setTrailerList(List<Trailer> trailers) {
        mTrailerList = trailers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trailer trailer = mTrailerList.get(position);
        holder.typeTextView.setText(trailer.getType().toUpperCase());
        holder.nameTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    public interface OnTrailerCardClickedListener {
        void onTrailerCardClicked(String key);
    }

}
