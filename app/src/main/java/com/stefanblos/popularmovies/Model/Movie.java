package com.stefanblos.popularmovies.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 *   Movie class that holds necessary information for a movie
 */
@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey
    private final Integer id;
    private final String title;
    private final String imageLink;
    private final String overview;
    private final float voteAvg;
    private final String releaseDate;

    public Movie(Integer id, String title, String imageLink,String overview, float voteAvg, String releaseDate) {
        this.id = id;
        this.title = title;
        this.imageLink = imageLink;
        this.overview = overview;
        this.voteAvg = voteAvg;
        this.releaseDate = releaseDate;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getImageLink() { return imageLink; }
    public String getOverview() { return overview; }
    public float getVoteAvg() { return voteAvg; }
    public String getReleaseDate() { return releaseDate; }

    private Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.imageLink = in.readString();
        this.overview = in.readString();
        this.voteAvg = in.readFloat();
        this.releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(imageLink);
        parcel.writeString(overview);
        parcel.writeFloat(voteAvg);
        parcel.writeString(releaseDate);
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
