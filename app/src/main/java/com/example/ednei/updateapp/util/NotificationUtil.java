package com.example.ednei.updateapp.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.example.ednei.updateapp.R;
import com.example.ednei.updateapp.services.NotificationBroadcCastReceive;

public class NotificationUtil {


    private static NotificationCompat.Builder mBuilder;
    private static NotificationManager mNotificationManager;
    private static Intent broadcast;
    private static PendingIntent peddPendingIntent;

    public static void notificationDownloadCompleted(Context context) {

        broadcast = new Intent(context, NotificationBroadcCastReceive.class);
        peddPendingIntent = PendingIntent.getBroadcast(context, 0, broadcast, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ATUALIZAÇÃO")
                        .setAutoCancel(true)
                        .setContentIntent(peddPendingIntent)
                        .setOnlyAlertOnce(true)
                        .setContentText("Nova versão disponível, clique para atualize.");

        mBuilder.setFullScreenIntent(peddPendingIntent, false);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(02, mBuilder.build());
    }
}
