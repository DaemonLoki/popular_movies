package com.stefanblos.popularmovies.Model;

public class Review {

    private final String _author;
    private final String _text;

    public Review(String author, String text) {
        _author = author;
        _text = text;
    }

    public String getAuthor() { return _author; }

    public String getText() { return _text; }

}
