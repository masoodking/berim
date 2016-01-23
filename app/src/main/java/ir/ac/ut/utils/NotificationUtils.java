package ir.ac.ut.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ir.ac.ut.berim.R;
import ir.ac.ut.berim.RegisterActivity;

/**
 * Created by Masood on 1/23/2016 AD.
 */
public class NotificationUtils {

    public static final int NOTIFICATION_ID = 12345;

    public static void sendMessageNotif(Context context, String notificationTitle,
            String notificationMessage) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMessage);
        Intent targetIntent = new Intent(context, RegisterActivity.class);
        PendingIntent contentIntent = PendingIntent
                .getActivity(context, 0, targetIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) context
                .getSystemService(
                        Context.NOTIFICATION_SERVICE);
        builder.setAutoCancel(true);
        nManager.notify(NOTIFICATION_ID, builder.build());
    }
}
