package ir.ac.ut.models;

/**
 * Created by saeed on 11/7/2015.
 */
public class Review {
    private User user;
    private String description;

    public Review(User user, String s) {
        this.user = user;
        description = s;
    }

    public User getUser() {
        return user;
    }

    public String getDescription(){
        return description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
