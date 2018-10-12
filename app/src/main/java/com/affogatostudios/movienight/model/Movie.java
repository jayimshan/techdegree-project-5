package com.affogatostudios.movienight.model;

import android.widget.ImageView;

public class Movie {

    private String title;
    private String genre;
    private String overview;
    private float rating;
    private int numberOfRatings;
    private String releaseDate; // Integer releaseYear
    private String imagePath;
    private String imagePath2;
    private String noImage = "https://image.tmdb.org/t/p/w300null";

    public Movie() {
    }

    public Movie(String title, String genre, String overview, float rating, int numberOfRatings, String releaseDate, String imagePath, String imagePath2) {
        this.title = title;
        this.genre = genre;
        this.overview = overview;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
        this.releaseDate = releaseDate;
        if (imagePath.equals(noImage)) {
            this.imagePath = noImage;
        } else {
            this.imagePath = imagePath;
        }
        this.imagePath2 = imagePath2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
    /*
    public float getRating() {
        return rating;
    }
    */
    public String getRating() {
        return Float.toString(rating);
        // return String.valueOf(rating);
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }
}
