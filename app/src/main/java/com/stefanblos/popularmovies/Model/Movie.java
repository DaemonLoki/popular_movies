package com.stefanblos.popularmovies.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 *   Movie class that holds necessary information for a movie
 */
@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey
    private final Integer _id;
    private final String _title;
    private final String _imageLink;
    private final String _overview;
    private final float _voteAvg;
    private final String _releaseDate;
    @Ignore
    private boolean _isFavorite;

    public Movie(Integer id, String title, String imageLink,String overview, float voteAvg, String releaseDate) {
        _id = id;
        _title = title;
        _imageLink = imageLink;
        _overview = overview;
        _voteAvg = voteAvg;
        _releaseDate = releaseDate;
        _isFavorite = false;
    }

    public Integer getId() { return _id; }
    public String getTitle() { return _title; }
    public String getImageLink() { return _imageLink; }
    public String getOverview() { return _overview; }
    public float getVoteAvg() { return _voteAvg; }
    public String getReleaseDate() { return _releaseDate; }
    public boolean isFavorite() { return _isFavorite; }

    public void setFavorite(boolean fave) { _isFavorite = fave; }

    private Movie(Parcel in) {
        this._id = in.readInt();
        this._title = in.readString();
        this._imageLink = in.readString();
        this._overview = in.readString();
        this._voteAvg = in.readFloat();
        this._releaseDate = in.readString();
        this._isFavorite = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(_id);
        parcel.writeString(_title);
        parcel.writeString(_imageLink);
        parcel.writeString(_overview);
        parcel.writeFloat(_voteAvg);
        parcel.writeString(_releaseDate);
        parcel.writeByte((byte) (_isFavorite ? 1 : 0));
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };

}
