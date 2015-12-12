package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Masood on 10/27/2015 AD.
 */
public class Message {

    private long id;

    private String text;

    private Long from;

    private Long to;

    private String username;

    private Long roomId;

    private String date;

    private MessageStatus status;

    private enum MessageStatus{
        SENT,
        SEEN,
        FAIL
    };

    public static Message createFromJson(JSONObject jsonObject) throws JSONException{
        Message message = new Message();
        if(jsonObject.has("id")){
            message.setId(jsonObject.getLong("id"));
        }

        if(jsonObject.has("senderId")){
            message.setFrom(jsonObject.getLong("senderId"));
        }

        if(jsonObject.has("roomId")){
            message.setRoomId(jsonObject.getLong("roomId"));
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
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
