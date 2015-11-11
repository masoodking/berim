package ir.ac.ut.models;

import java.util.ArrayList;

import ir.ac.ut.models.Review;

/**
 * Created by Masood on 10/1/2015 AD.
 */
public class Place {

    private String id;
    private ArrayList<Review> reviews;
    private String name;
    private String location;
    private String description;

    public Place(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
