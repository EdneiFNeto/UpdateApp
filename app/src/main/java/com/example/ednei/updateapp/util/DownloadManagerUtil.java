package com.example.ednei.updateapp.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.ednei.updateapp.enuns.ConfigEnum;

public class DownloadManagerUtil {

    private static String TAG="DownloadMagLog";
    private static long downloadID;

    public static void downloadByDownloadManager(Context context) throws Exception{


        String url = "https://www.divertenet.com.br/apps/tv-release.apk";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("A zip package with some files");
        request.setTitle("Atualização");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-release.apk");

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID  = manager.enqueue(request);

        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {

                    //push notification
                    NotificationUtil.notificationDownloadCompleted(context);
                }
            }
        };

        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
