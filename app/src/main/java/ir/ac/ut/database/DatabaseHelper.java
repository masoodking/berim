package ir.ac.ut.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import ir.ac.ut.berim.BerimApplication;
import ir.ac.ut.berim.ProfileUtils;
import ir.ac.ut.models.Message;
import ir.ac.ut.models.Room;
import ir.ac.ut.models.User;

/**
 * Created by Masood on 1/11/2016 AD.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "berim_persian_version";

    public static final int VERSION = 1;

    private static DatabaseHelper helper;

    SQLiteDatabase db = getWritableDatabase();

    /**
     * messages
     */
    public static final String MESSAGE_TABLE_NAME = "messages";

    public static final String ID = "id";

    public static final String DATE = "date";

    public static final String FILE_ADDRESS = "fileAddress";

    public static final String ROOM_ID = "roomId";

    public static final String SENDER_LAST_SEEN = "sender_last_seen";

    public static final String SENDER_NICKNAME = "sender_nick_name";

    public static final String SENDER_ROOM_ID = "sender_room_id";

    public static final String SENDER_PHONE_NUMBER = "sender_phone_number";

    public static final String SENDER_ID = "sender_id";

    public static final String SENDER_AVATAR = "sender_avatar";

    public static final String STATUS = "status";

    public static final String TEXT = "text";

    public static final String UPDATE_STATUS = "updateStatus";


    /**
     * rooms
     */
    public static final String ROOM_TABLE_NAME = "rooms";

    public static final String NAME = "name";

    public static final String PLACE_ID = "place_id";

    public static final String LAST_MESSAGE_ID = "last_message";

    public static final String MAX_USER_COUNT = "max_user_count";

    public static final String CREATE_DATE = "create_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DatabaseHelper(context);
        }

        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MESSAGES_TABLE = String
                .format("CREATE TABLE IF NOT EXISTS %s ("
                                + "%s TEXT , %s TEXT ,"
                                + "%s TEXT ,%s TEXT ,%s TEXT ,"
                                + "%s TEXT ,%s TEXT ,"
                                + "%s TEXT ,%s TEXT ,"
                                + "%s TEXT ,%s TEXT ,"
                                + "%s TEXT , %s TEXT)",
                        MESSAGE_TABLE_NAME, ID, DATE, FILE_ADDRESS, ROOM_ID,
                        SENDER_LAST_SEEN, SENDER_NICKNAME, SENDER_ROOM_ID, SENDER_PHONE_NUMBER,
                        SENDER_ID, SENDER_AVATAR, STATUS, TEXT, UPDATE_STATUS);

        String CREATE_ROOM_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ("
                        + "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                ROOM_TABLE_NAME, ID, NAME, PLACE_ID, LAST_MESSAGE_ID, MAX_USER_COUNT,
                CREATE_DATE);
        db.execSQL(CREATE_MESSAGES_TABLE);
        db.execSQL(CREATE_ROOM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }

    public void InsertMessage(List<Message> messages) {
        if (messages == null || messages.size() == 0) {
            return;
        }
        db.delete(MESSAGE_TABLE_NAME, ID + "='not-set'", null);
        for (int i = 0; i < messages.size(); i++) {
            String where = ID + "='" + messages.get(i).getId() + "'";
            if (db.update(MESSAGE_TABLE_NAME,
                    convertMessageToContentValues(messages.get(i)), where, null) == 0) {
                db.insert(MESSAGE_TABLE_NAME, null,
                        convertMessageToContentValues(messages.get(i)));
            }
        }
    }

    public void InsertMessage(Message message) {
        String where = ID + "='" + message.getId() + "'";
        if (db.update(MESSAGE_TABLE_NAME,
                convertMessageToContentValues(message), where, null) == 0) {
            db.insert(MESSAGE_TABLE_NAME, null,
                    convertMessageToContentValues(message));
        }
    }

    public void InsertMessageNoUpdate(Message message) {
        db.insert(MESSAGE_TABLE_NAME, null,
                convertMessageToContentValues(message));
    }

    public Message getMessageById(String id) {
        String where = ID + "='" + id + "'";
        ArrayList<Message> messages = convertCursorToMessage(getReadableDatabase().query(
                MESSAGE_TABLE_NAME, null, where, null, null, null, null));
        return messages.size() > 0 ? messages.get(0) : null;
    }

    public ArrayList<Message> getMessage(boolean setSeen, String where) {
        ArrayList<Message> data = convertCursorToMessage(getReadableDatabase().query(
                MESSAGE_TABLE_NAME, null, where, null, null, null, null));
        if (setSeen) {
            for (Message message : data) {
                if (message.getStatus() != Message.MessageStatus.SEEN) {
                    message.setStatus(Message.MessageStatus.SEEN);
                    InsertMessage(message);
                }
            }
        }
        return data;
    }

    public void InsertRoom(List<Room> messages) {
        for (int i = 0; i < messages.size(); i++) {
            String where = ID + "='" + messages.get(i).getId() + "'";
            if (db.update(ROOM_TABLE_NAME,
                    convertRoomToContentValues(messages.get(i)), where, null) == 0) {
                db.insert(ROOM_TABLE_NAME, null,
                        convertRoomToContentValues(messages.get(i)));
            }
        }
    }

    public ArrayList<Room> getRoom(String where) {
        return convertCursorToRoom(getReadableDatabase().query(
                ROOM_TABLE_NAME, null, where, null, null, null, null));
    }

    public Room getRoomById(String id) {
        String where = ID + "='" + id + "'";
        ArrayList<Room> rooms = convertCursorToRoom(getReadableDatabase().query(
                ROOM_TABLE_NAME, null, where, null, null, null, null));
        return rooms.get(0);
    }

    public ArrayList<Room> getChatList() {
        String myUserId = ProfileUtils.getUser(BerimApplication.getInstance()).getId();
        ArrayList<Message> messages = getMessage(false, null);
        HashMap hashMap = new HashMap<String, Message>();
        ArrayList<Room> berims = getRoom(null);
        String berimRoomsId = "";
        for (Room berim : berims) {
            if (berim.getMaxUserCount() > 1) {
                berimRoomsId = berimRoomsId + "-" + berim.getId();
            }
        }
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (berimRoomsId.contains(msg.getRoomId())) {
                continue;
            }
            if (!hashMap
                    .containsKey("<" + msg.getSender().getRoomId() + "," + msg.getRoomId() + ">")
                    && !hashMap
                    .containsKey("<" + msg.getRoomId() + "," + msg.getSender().getRoomId() + ">")) {
                Room room = new Room();
                room.setId(msg.getRoomId());
                room.setLastMessage(msg);
                room.setMaxUserCount(1);
                if (msg.getSender().getId()
                        .equals(myUserId)) {
                    room.setName("-");
                } else {
                    room.setName(msg.getSender().getValidUserName());
                }
                if (msg.getStatus() != Message.MessageStatus.SEEN) {
                    room.setUnreadMessageCount(1);
                }
                hashMap.put("<" + msg.getSender().getRoomId() + "," + msg.getRoomId() + ">", room);
            } else {
                Room rm = (Room) hashMap
                        .get("<" + msg.getSender().getRoomId() + "," + msg.getRoomId() + ">");
                if (rm != null) {
                    if (msg.getStatus() != Message.MessageStatus.SEEN
                            && !msg.getSender().getId().equals(myUserId)) {
                        rm.setUnreadMessageCount(rm.getUnreadMessageCount() + 1);
                    }
                    if (!msg.getSender().getId().equals(myUserId)) {
                        rm.setName(msg.getSender().getValidUserName());
                    }
                    hashMap.remove("<" + msg.getSender().getRoomId() + "," + msg.getRoomId() + ">");
                    hashMap.put("<" + msg.getSender().getRoomId() + "," + msg.getRoomId() + ">",
                            rm);
                } else {
                    rm = (Room) hashMap
                            .get("<" + msg.getRoomId() + "," + msg.getSender().getRoomId() + ">");
                    if (msg.getStatus() != Message.MessageStatus.SEEN
                            && msg.getSender().getId() != myUserId) {
                        rm.setUnreadMessageCount(rm.getUnreadMessageCount() + 1);
                    }
                    if (!msg.getSender().getId().equals(myUserId)) {
                        rm.setName(msg.getSender().getValidUserName());
                    }
                    hashMap.remove("<" + msg.getRoomId() + "," + msg.getSender().getRoomId() + ">");
                    hashMap.put("<" + msg.getRoomId() + "," + msg.getSender().getRoomId() + ">",
                            rm);
                }
            }
        }
        ArrayList<Room> rooms = new ArrayList<>();
        Collection rms = hashMap.values();
        for (Object rm : rms) {
            rooms.add((Room) rm);
        }
        return rooms;
    }

//    public ArrayList<Room> getChatList() {
//        Cursor cursor = db.query(
//        /* FROM */ MESSAGE_TABLE_NAME,
//        /* SELECT */ new String[]{"*", "COUNT(" + STATUS + ") AS count"},
//        /* WHERE */ null,
//        /* WHERE args */ null,
//        /* GROUP BY */ SENDER_ROOM_ID +", " + ROOM_ID,
//        /* HAVING */ null,
//        /* ORDER BY */ DATE + " DESC"
//        );
//        ArrayList<Message> messages = convertCursorToMessage(cursor);
//        ArrayList<Room> rooms = new ArrayList<>();
//        for (Message msg : messages) {
//            Room room = new Room();
//            room.setId(msg.getRoomId());
//            room.setLastMessage(msg);
//            room.setMaxUserCount(1);
//            room.setName(msg.getSender().getValidUserName());
//            rooms.add(room);
//        }
//        return rooms;
//    }

    public ArrayList<Message> convertCursorToMessage(Cursor cursor) {
        ArrayList<Message> list = new ArrayList<Message>();
        while (cursor.moveToNext()) {

            User user = new User();
            user.setId(cursor.getString(cursor.getColumnIndex(SENDER_ID)));
            user.setNickName(cursor.getString(cursor.getColumnIndex(SENDER_NICKNAME)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(SENDER_PHONE_NUMBER)));
            user.setAvatar(cursor.getString(cursor.getColumnIndex(SENDER_AVATAR)));
            user.setRoomId(cursor.getString(cursor.getColumnIndex(SENDER_ROOM_ID)));
            user.setLastSeen(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(SENDER_LAST_SEEN))));

            String status = cursor.getString(cursor.getColumnIndex(STATUS));
            list.add(new Message(
                    cursor.getString(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(TEXT)),
                    cursor.getString(cursor.getColumnIndex(ROOM_ID)),
                    user, Message.converMessageStatus(status),
                    cursor.getString(cursor.getColumnIndex(UPDATE_STATUS)),
                    cursor.getString(cursor.getColumnIndex(FILE_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(DATE))
            ));

        }
        cursor.close();
        return list;
    }

    public ArrayList<Room> convertCursorToRoom(Cursor cursor) {
        ArrayList<Room> list = new ArrayList<Room>();
        while (cursor.moveToNext()) {
            Message lastMessage = getMessageById(
                    cursor.getString(cursor.getColumnIndex(LAST_MESSAGE_ID)));

            Room room = new Room();
            room.setId(cursor.getString(cursor.getColumnIndex(ID)));
            room.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            room.setLastMessage(lastMessage);
            room.setPlaceId(cursor.getString(cursor.getColumnIndex(PLACE_ID)));
            room.setMaxUserCount(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(MAX_USER_COUNT))));
            room.setCreatedDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));

            list.add(room);
        }
        cursor.close();
        return list;
    }

    public int getMessageCount(String where) {
        Cursor cursor = getReadableDatabase().query(MESSAGE_TABLE_NAME, null,
                where, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    private ContentValues convertMessageToContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(ID, message.getId());
        values.put(TEXT, message.getText());
        values.put(ROOM_ID, message.getRoomId());
        values.put(SENDER_ID, message.getSender().getId());
        values.put(SENDER_NICKNAME, message.getSender().getNickName());
        values.put(SENDER_PHONE_NUMBER, message.getSender().getPhoneNumber());
        values.put(SENDER_ROOM_ID, message.getSender().getRoomId());
        values.put(SENDER_AVATAR, message.getSender().getAvatar());
        values.put(SENDER_LAST_SEEN, message.getSender().getLastSeen());
        values.put(STATUS, message.getStringStatus());
        values.put(UPDATE_STATUS, message.getUpdateStatus());
        values.put(FILE_ADDRESS, message.getFileAddress());
        values.put(DATE, message.getDate());

        return values;
    }

    private ContentValues convertRoomToContentValues(Room room) {
        ContentValues values = new ContentValues();
        values.put(ID, room.getId());
        values.put(CREATE_DATE, room.getCreatedDate());
        values.put(MAX_USER_COUNT, room.getMaxUserCount());
        values.put(NAME, room.getName());
        values.put(PLACE_ID, room.getPlaceId());
        if (room.getLastMessage() != null) {
            values.put(LAST_MESSAGE_ID, room.getLastMessage().getId());
        }
        return values;
    }

    public void dropDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
        db.close();
        helper = null;
    }
}