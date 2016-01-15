package ir.ac.ut.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Masood on 10/27/2015 AD.
 */
public class Message {

    private String id;

    private String text;

    private String roomId;

    private User sender;

    private MessageStatus status;

    private String updateStatus;

    private String fileAddress;

    private String date;

    public enum MessageStatus {
        SENT,
        SEEN,
        FAIL
    }

    ;

    public Message(String id, String text, String roomId, User sender,
            MessageStatus status, String updateStatus, String fileAddress, String date) {
        this.id = id;
        this.text = text;
        this.roomId = roomId;
        this.sender = sender;
        this.status = status;
        this.updateStatus = updateStatus;
        this.fileAddress = fileAddress;
        this.date = date;
    }

    public static Message createFromJson(JSONObject jsonObject) throws JSONException {
        Message message = new Message();
        if (jsonObject.has("id")) {
            message.setId(jsonObject.getString("id"));
        }

        if (jsonObject.has("roomId")) {
            message.setRoomId(jsonObject.getString("roomId"));
        }

        if (jsonObject.has("text")) {
            message.setText(jsonObject.getString("text"));
        }

        if (jsonObject.has("file")) {
            message.setFileAddress(jsonObject.getString("file"));
        }

        if (jsonObject.has("date")) {
            message.setDate(jsonObject.getString("date"));
        }

        if (jsonObject.has("status")) {
            message.setStatus(converMessageStatus(jsonObject.getString("status")));
        }

        if (jsonObject.has("updateStatus")) {
            message.setUpdateStatus(jsonObject.getString("updateStatus"));
        }

        if (jsonObject.has("sender")) {
            message.setSender(User.createFromJson(jsonObject.getJSONObject("sender")));
        }

        return message;
    }

    public Message() {
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static MessageStatus converMessageStatus(String status) {
        Message.MessageStatus messageStatus;
        if (status.equals("seen")) {
            return Message.MessageStatus.SEEN;
        } else if (status.equals("fail")) {
            return Message.MessageStatus.FAIL;
        } else {
            return Message.MessageStatus.SENT;
        }
    }
}
