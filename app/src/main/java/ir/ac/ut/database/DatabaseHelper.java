package ir.ac.ut.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Masood on 1/11/2016 AD.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "berim_persian_version";
    public static final int VERSION = 1;
    private static DatabaseHelper helper;

    SQLiteDatabase db = getWritableDatabase();

    /**
     *
     * messages
     *
     */
    public static final String MESSAGE_TABLE_NAME = "messages";
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String FILE_ADDRESS = "fileAddress";
    public static final String ROOM_ID = "roomId";
    public static final String SENDER = "sender";
    public static final String STATUS = "status";
    public static final String TEXT = "text";
    public static final String UPDATE_STATUS = "updateStatus";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (helper == null)
            helper = new DatabaseHelper(context);

        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MESSAGES_TABLE = String
                .format("CREATE TABLE IF NOT EXISTS %s ("
                                + "%s TEXT, %s TEXT ,%"
                                + "s TEXT,%s TEXT ,%s TEXT ,"
                                + " %s TEXT , %s TEXT , %s TEXT)",
                        MESSAGE_TABLE_NAME, ID, DATE, FILE_ADDRESS, ROOM_ID,
                        SENDER, STATUS, TEXT, UPDATE_STATUS);

        db.execSQL(CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

    }

//    public void InsertTopic(SQLiteDatabase db, List<Message> messages) {
//        for (int i = 0; i < messages.size(); i++) {
//            String where = ID + "=" + messages.get(i).getId();
//            if (db.update(TOPIC_TABLE_NAME,
//                    convertTopicToContentValues(messages.get(i)), where, null) == 0)
//                db.insert(TOPIC_TABLE_NAME, null,
//                        convertTopicToContentValues(messages.get(i)));
//        }
//
//    }
//
//    public List<Topics> getTopics(String where) {
//
//        return convertCursorToTopics(getReadableDatabase().query(
//                TOPIC_TABLE_NAME, null, where, null, null, null, null));
//
//    }
//
//    public List<Topics> convertCursorToTopics(Cursor cursor) {
//
//        List<Topics> list = new ArrayList<Topics>();
//        while (cursor.moveToNext()) {
//
//            list.add(new Topics(
//                    cursor.getInt(cursor.getColumnIndex(ID)),
//                    cursor.getInt(cursor.getColumnIndex(CATEGORY_ID)),
//                    cursor.getString(cursor.getColumnIndex(TITLE)),
//                    cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
//                    cursor.getString(cursor.getColumnIndex(ICON)),
//                    cursor.getInt(cursor.getColumnIndex(ISSELECTED)) == 1 ? true
//                            : false,
//                    cursor.getInt(cursor.getColumnIndex(ISPOPULAR)) == 1 ? true
//                            : false,
//                    cursor.getInt(cursor.getColumnIndex(ISNEW)) == 1 ? true
//                            : false,
//                    cursor.getInt(cursor.getColumnIndex(ISUPDATED)) == 1 ? true
//                            : false,
//                    cursor.getInt(cursor.getColumnIndex(ISRECOMMENDED)) == 1 ? true
//                            : false));
//
//        }
//
//        cursor.close();
//
//        return list;
//
//    }
//
//    public int getTopicCount(String where) {
//
//        Cursor cursor = getReadableDatabase().query(TOPIC_TABLE_NAME, null,
//                where, null, null, null, null);
//
//        int count = cursor.getCount();
//        cursor.close();
//
//        return count;
//
//    }
//
//
//    private ContentValues convertTopicToContentValues(Message topics) {
//
//        ContentValues values = new ContentValues();
//        values.put(ID, topics.getId());
//        values.put(CATEGORY_ID, topics.getCategoryId());
//        values.put(TITLE, topics.getTitle());
//        values.put(DESCRIPTION, topics.getShortDesc());
//        values.put(ICON, topics.getIcon());
//        values.put(ISSELECTED, topics.isSelectedTopic() ? 1 : 0);
//        values.put(ISPOPULAR, topics.isPopularTopic() ? 1 : 0);
//        values.put(ISRECOMMENDED, topics.isRecommended() ? 1 : 0);
//        values.put(ISNEW, topics.isNewTopic() ? 1 : 0);
//        values.put(ISUPDATED, topics.isUpdatedTopic() ? 1 : 0);
//
//        return values;
//    }
}