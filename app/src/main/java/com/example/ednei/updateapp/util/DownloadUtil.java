package com.example.ednei.updateapp.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.ednei.updateapp.MainActivity;
import com.example.ednei.updateapp.R;
import com.example.ednei.updateapp.enuns.ConfigEnum;
import com.example.ednei.updateapp.services.NotificationBroadcCastReceive;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Download from notification,
 * Show Dialog install app
 */
public class DownloadUtil extends AsyncTask<String, Integer, Boolean> {

    private static final String ACTION_UPDATE = "action_update";
    private String TAG = "DownloadLog";
    /*private String PATH = Environment.getExternalStorageDirectory() + "/Download/";
    private String nameApk = "tv-release.apk";
    private String urlDownloadApp = "https://www.divertenet.com.br/apps/tv-release.apk";
    */

    private ConfigEnum configEnum;
    private String PATH = configEnum.PATH.toString();
    private String nameApk = configEnum.NAME_APK.toString();
    private String urlDownloadApp = configEnum.URL_JSON.toString();

    private NotificationManager mNotifyMgr;
    private NotificationCompat.Builder mBuilder;
    int mNotificationId;

    private Context context;

    public DownloadUtil(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Download")
                .setAutoCancel(true);
        mNotificationId = 001;
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if ((values[0]) % 5 == 0) {
            mBuilder.setProgress(100, values[0], false);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);


        if (result) {
            mNotifyMgr.cancelAll();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notificationDownloadCompleted();

                }
            }, 2000);
        } else {
            Toast.makeText(context, "Error Download", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        Boolean flag = false;

        try {

            URL url = new URL(urlDownloadApp);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            int total_size = c.getContentLength();
            c.connect();

            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, nameApk);

            if (outputFile.exists()) {
                outputFile.delete();
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            int per = 0;
            int downloaded = 0;

            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
                downloaded += len1;
                per = (int) (downloaded * 100 / total_size);
                publishProgress(per);
            }

            fos.close();
            is.close();

            flag = true;
        } catch (Exception e) {
            Log.e(TAG, "Update Error: " + e.getMessage());
            flag = false;
        }

        return flag;
    }

    private void showNotification() {
        mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Baixando atualizações")
                .setAutoCancel(true);
        mNotificationId = 001;
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private void notificationDownloadCompleted() {

        Intent broadcast = new Intent(context, NotificationBroadcCastReceive.class);
        PendingIntent peddPendingIntent = PendingIntent.getBroadcast(context, 0, broadcast,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("ATUALIZAÇÃO")
                        .setAutoCancel(true)
                        .setColor(Color.BLUE)
                        .setContentIntent(peddPendingIntent)
                        .addAction(R.drawable.ic_update, "Atualizar", peddPendingIntent)
                        .setOnlyAlertOnce(true)
                        .setContentText("Nova versão disponível, atualize seu app.");

        mBuilder.setFullScreenIntent(peddPendingIntent, false);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(02, mBuilder.build());
    }
}
