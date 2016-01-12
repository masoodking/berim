package ir.ac.ut.models;

/**
 * Created by saeed on 11/3/2015.
 */
public class Room {

    private String roomId;

    private String title;

    private String lastText;

    private String lastUpdate;

    private User talkee;

    public Room(String title, String lastText) {
        this.title = title;
        this.lastText = lastText;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastText() {
        return lastText;
    }

    public void setLastText(String lastText) {
        this.lastText = lastText;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public User getTalkee() {
        return talkee;
    }

    public void setTalkee(User talkee) {
        this.talkee = talkee;
    }
}
