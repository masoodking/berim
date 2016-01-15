package ir.ac.ut.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ir.ac.ut.models.Message;
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

        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
    }

    public void InsertMessage(SQLiteDatabase db, List<Message> messages) {
        for (int i = 0; i < messages.size(); i++) {
            String where = ID + "=" + messages.get(i).getId();
            if (db.update(MESSAGE_TABLE_NAME,
                    convertMessageToContentValues(messages.get(i)), where, null) == 0) {
                db.insert(MESSAGE_TABLE_NAME, null,
                        convertMessageToContentValues(messages.get(i)));
            }
        }
    }

    public List<Message> getMessage(String where) {
        return convertCursorToMessage(getReadableDatabase().query(
                MESSAGE_TABLE_NAME, null, where, null, null, null, null));
    }

    public void getChatList() {
        Cursor cursor = db.query(
        /* FROM */ MESSAGE_TABLE_NAME,
        /* SELECT */ new String[]{"*", "COUNT(" + STATUS + ") AS count"},
        /* WHERE */ null,
        /* WHERE args */ null,
        /* GROUP BY */ SENDER_ID,
        /* HAVING */ null,
        /* ORDER BY */ DATE + " DESC"
        );
    }

    public List<Message> convertCursorToMessage(Cursor cursor) {
        List<Message> list = new ArrayList<Message>();
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
        values.put(STATUS, message.getStatus().toString());
        values.put(UPDATE_STATUS, message.getUpdateStatus());
        values.put(FILE_ADDRESS, message.getFileAddress());
        values.put(DATE, message.getDate());

        return values;
    }
}