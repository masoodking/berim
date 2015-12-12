package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saeed on 11/7/2015.
 */
public class User {

    private String id;

    private String nickName;

    private String phoneNumber;

    private String password;

    private String roomId;

    public static User createFromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setId(jsonObject.getString("id"));
        if (jsonObject.has("nickName")) {
            user.setNickName(jsonObject.getString("nickName"));
        }
        user.setPassword(jsonObject.getString("password"));
        user.setPhoneNumber(jsonObject.getString("phoneNumber"));
        user.setRoomId(jsonObject.getString("roomId"));
        return user;
    }

    public User() {
    }

    public User(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        User user = (User) o;

        if (!id.equals(user.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
