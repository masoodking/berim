package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by saeed on 11/7/2015.
 */
public class User implements Serializable {

    private String id;

    private String nickName;

    private String phoneNumber;

    private String roomId;

    private String avatar;

    private int lastSeen;

    public static User createFromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setId(jsonObject.has("id") ? jsonObject.getString("id") : null);
        user.setNickName(jsonObject.has("nickName") ? jsonObject.getString("nickName") : null);
        user.setPhoneNumber(
                jsonObject.has("phoneNumber") ? jsonObject.getString("phoneNumber") : null);
        user.setRoomId(jsonObject.has("roomId") ? jsonObject.getString("roomId") : null);
        user.setAvatar(jsonObject.has("avatar") ? jsonObject.getString("avatar") : null);
        user.setLastSeen(jsonObject.has("lastSeen") ? jsonObject.optInt("lastSeen") : 0);
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

    public String getValidUserName() {
        if (nickName != null && !nickName.equals("null")) {
            return nickName;
        } else {
            return phoneNumber;
        }
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(int lastSeen) {
        this.lastSeen = lastSeen;
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
