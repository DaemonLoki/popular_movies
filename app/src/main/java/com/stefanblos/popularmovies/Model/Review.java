package com.stefanblos.popularmovies.Model;

public class Review {

    private final String author;
    private final String text;

    public Review(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public String getAuthor() { return author; }

    public String getText() { return text; }

}
