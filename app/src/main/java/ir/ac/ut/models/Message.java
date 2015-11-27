package ir.ac.ut.models;

/**
 * Created by Masood on 10/27/2015 AD.
 */
public class Message {

    private long id;

    private String text;

    private Long from;

    private Long to;

    private boolean seen;
    public Message(long id, String text, Long from, Long to) {
        this.id = id;
        this.text = text;
        this.from = from;
        this.to = to;
    }

    /**
     * todo REMOVE THIS CONSTRUCTOR
     * @param text
     */
    public Message(String text){
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
}
