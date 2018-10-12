package com.affogatostudios.movienight.model;

public class Genre {

    private String name;
    private int genreId;
    private boolean isChecked = false;

    public Genre() {
    }

    public Genre(String name, int genreId, boolean isChecked) {
        this.name = name;
        this.genreId = genreId;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
