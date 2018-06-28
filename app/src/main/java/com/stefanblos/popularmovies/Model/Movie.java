package com.stefanblos.popularmovies.Model;

/*
    Movie class that holds necessary information for a movie
 */
public class Movie {

    private String _title;
    private String _imageLink;

    public Movie(String title, String imageLink) {
        _title = title;
        _imageLink = imageLink;
    }

    public String getTitle() { return _title; }
    public String getImageLink() { return _imageLink; }

}
