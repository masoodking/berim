package ir.ac.ut.models;

import java.util.ArrayList;

/**
 * Created by Masood on 10/1/2015 AD.
 */
public class Place {

    private String id;
    private ArrayList<Review> reviews;
    private String name;
    private String location;
    private String description;
    private int voteNumber;

    public Place(String id, String name, String description, int voteNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.voteNumber = voteNumber;
    }

    public Place(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(int voteNumber) {
        this.voteNumber = voteNumber;
    }
}
