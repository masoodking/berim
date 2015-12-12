package ir.ac.ut.models;

/**
 * Created by saeed on 11/7/2015.
 */
public class User {
    private String name;
    private String id;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(User user){
        return user.getID().equals(id);
    }
}
