package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Masood on 10/27/2015 AD.
 */
public class Message {

    private String id;

    private String text;

    private String from;

    private String to;

    private String username;

    private String roomId;

    private String date;

    private MessageStatus status;

    public enum MessageStatus{
        SENT,
        SEEN,
        FAIL
    };

    public static Message createFromJson(JSONObject jsonObject) throws JSONException{
        Message message = new Message();
        if(jsonObject.has("id")){
            message.setId(jsonObject.getString("id"));
        }

        if(jsonObject.has("senderId")){
            message.setFrom(jsonObject.getString("senderId"));
        }

        if(jsonObject.has("roomId")){
            message.setRoomId(jsonObject.getString("roomId"));
        }

        if(jsonObject.has("text")){
            message.setText(jsonObject.getString("text"));
        }

        if(jsonObject.has("date")){
            message.setDate(jsonObject.getString("date"));
        }

        return message;
    }

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
