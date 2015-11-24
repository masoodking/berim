package ir.ac.ut.models;

/**
 * Created by saeed on 11/3/2015.
 */
public class Chat {
    private User talkee;

    public Chat(String s, String s1, String s2) {
        talkee = new User("سعید");
    }

    public String getName() {
        return talkee.getName();
    }


//    public long getId() {
//        return id;
//    }
}
