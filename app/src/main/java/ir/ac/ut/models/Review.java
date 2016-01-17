package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saeed on 11/7/2015.
 */
public class Review {

    private User user;

    private String description;

    private String placeId;

    private String date;

    private int rate;

    private String text;

    public static Review createFromJson(JSONObject jsonObject) throws JSONException {
        Review review = new Review();
        if (jsonObject.has("user")) {
            review.setUser(User.createFromJson(jsonObject.getJSONObject("user")));
        }
        if (jsonObject.has("placeId")) {
            review.setPlaceId(jsonObject.getString("placeId"));
        }
        if (jsonObject.has("date")) {
            review.setDate(jsonObject.getString("date"));
        }
        if (jsonObject.has("text")) {
            review.setText(jsonObject.getString("text"));
        }
        if (jsonObject.has("rate")) {
            review.setRate(jsonObject.getInt("rate"));
        }
        return review;
    }

    public Review() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
