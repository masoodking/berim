package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Masood on 10/1/2015 AD.
 */
public class Place implements Serializable {

    private String id;

    private ArrayList<Review> reviews;

    private String name;

    private String address;

    private String description;

    private float rate;

    private String category;

    private String longitude;

    private String latitude;

    private String avatar;

    public Place() {
    }

    public Place(String name, String desc, String avatar){
        this.name = name;
        this.description = desc;
        this.avatar = avatar;
    }
    public static Place createFromJson(JSONObject jsonObject) throws JSONException {
        Place place = new Place();
        if (jsonObject.has("id")) {
            place.setId(jsonObject.getString("id"));
        }
        if (jsonObject.has("name")) {
            place.setName(jsonObject.getString("name"));
        }
        if (jsonObject.has("address")) {
            place.setAddress(jsonObject.getString("address"));
        }
        if (jsonObject.has("description")) {
            place.setDescription(jsonObject.getString("description"));
        }
        if (jsonObject.has("rate")) {
            place.setRate(jsonObject.optInt("rate"));
        }
        if (jsonObject.has("category")) {
            place.setCategory(jsonObject.getString("category"));
        }
        if (jsonObject.has("longitude")) {
            place.setLongitude(jsonObject.getString("longitude"));
        }
        if (jsonObject.has("latitude")) {
            place.setLatitude(jsonObject.getString("latitude"));
        }
        if (jsonObject.has("avatar")) {
            place.setAvatar(jsonObject.getString("avatar"));
        }

        place.reviews = new ArrayList<>();
        if(jsonObject.has("reviews")) {
            for (int i = 0; i < jsonObject.getJSONArray("reviews").length(); i++) {
                place.reviews.add(
                        Review.createFromJson(jsonObject.getJSONArray("reviews").getJSONObject(i)));
            }
        }
        return place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews() {
        this.reviews = reviews;
    }
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
