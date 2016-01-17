package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saeed on 11/3/2015.
 */
public class Room {

    private String id;

    private String name;

    private String createdDate;

    private Message lastMessage;

    private String placeId;

    private int maxUserCount;

    public Room() {
    }

    public static Room createFromJson(JSONObject jsonObject) throws JSONException {
        Room room = new Room();
        if (jsonObject.has("id")) {
            room.setId(jsonObject.getString("id"));
        }

        if (jsonObject.has("name")) {
            room.setName(jsonObject.getString("name"));
        }

        if (jsonObject.has("createdDate")) {
            room.setCreatedDate(jsonObject.getString("createdDate"));
        }

        if (jsonObject.has("lastMessage")) {
            room.setLastMessage(Message.createFromJson(jsonObject.getJSONObject("lastMessage")));
        }

        if (jsonObject.has("placeId")) {
            room.setPlaceId(jsonObject.getString("placeId"));
        }

        if (jsonObject.has("maxUserCount")) {
            room.setMaxUserCount(jsonObject.getInt("maxUserCount"));
        }

        return room;
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

    public String getValidName(){
        try {
            if (name != null && !name.equals("null") && name.length() > 0) {
                return name;
            } else {
                return lastMessage.getSender().getValidUserName();
            }
        }catch (Exception e){
            return "ناشناس";
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public int getMaxUserCount() {
        return maxUserCount;
    }

    public void setMaxUserCount(int maxUserCount) {
        this.maxUserCount = maxUserCount;
    }
}
