package com.example.project10;

/**
 * class for help to use list
 */

public class Things {
    String name;
    Boolean favorite;
    int choice; // 1 for song and 2 for video
    int link;

    public Things(String name, Boolean favorite, int choice, int link) {
        this.name = name;
        this.favorite = favorite;
        this.choice = choice;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }
}
