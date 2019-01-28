package com.example.ednei.updateapp.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import com.example.ednei.updateapp.ShowNote;
import com.example.ednei.updateapp.enuns.ConfigEnum;

public class DownloadManagerUtil {

    private static String TAG="DownloadMagLog";
    private static long downloadID;

    public static void downloadByDownloadManager(Context context) throws Exception{


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ConfigEnum.URL_APK.getConfig()));
        request.setDescription("Baixando atualizações");
        request.setTitle("Atualização");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ConfigEnum.NAME_APK.getConfig());

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID  = manager.enqueue(request);

        //when download completed, show notification
        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    //push notification
                    //context.startActivity(new Intent(context, ShowNote.class));
                    //NotificationUtil.notificationDownloadCompleted(context);
                    DialogUtil.showDialog(context);
                }
            }
        };

        //register broadcast receive
        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
