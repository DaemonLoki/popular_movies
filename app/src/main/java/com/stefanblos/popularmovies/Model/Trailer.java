package com.stefanblos.popularmovies.Model;

public class Trailer {

    private final String name;
    private final String type;
    private final String key;

    public Trailer(String name, String type, String key) {
        this.name = name;
        this.type = type;
        this.key = key;
    }

    public String getName() { return name; }

    public String getType() { return type; }

    public String getKey() { return key; }
}
