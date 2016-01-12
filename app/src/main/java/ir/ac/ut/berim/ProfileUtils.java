package ir.ac.ut.berim;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ir.ac.ut.models.User;

/**
 * Created by Masood on 12/12/2015 AD.
 */
public class ProfileUtils {

    private static final String USER_ID = "user_id";

    private static final String USER_NICKNAME = "user_nickname";

    private static final String USER_PASSWORD = "user_password";

    private static final String USER_ROOM_ID = "user_room_id";

    private static final String USER_PHONE_NUMBER = "user_phone_number";

    private static final String USER_IS_LOGIN = "user_is_login";

    public static void loginUser(Context context, User user){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ID, user.getId());
        editor.putString(USER_NICKNAME, user.getNickName());
        editor.putString(USER_PASSWORD, user.getPassword());
        editor.putString(USER_ROOM_ID, user.getRoomId());
        editor.putString(USER_PHONE_NUMBER, user.getPhoneNumber());
        editor.putBoolean(USER_IS_LOGIN, true);
        editor.commit();
    }

    public static User getUser(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        User user = new User();
        user.setId(prefs.getString(USER_ID, null));
        user.setRoomId(prefs.getString(USER_ROOM_ID, null));
        user.setPhoneNumber(prefs.getString(USER_PHONE_NUMBER, null));
        user.setPassword(prefs.getString(USER_PASSWORD, null));
        user.setNickName(prefs.getString(USER_NICKNAME, null));
        return user;
    }
    public static void logoutUser(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ID, null);
        editor.putString(USER_NICKNAME, null);
        editor.putString(USER_PASSWORD, null);
        editor.putString(USER_ROOM_ID, null);
        editor.putString(USER_PHONE_NUMBER, null);
        editor.putBoolean(USER_IS_LOGIN, false);
        editor.commit();
    }

    public static boolean isLogin(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(USER_IS_LOGIN, false);
    }

    public static void saveSP(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSP(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void removeSP(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, null);
        editor.commit();
    }
}
