package com.example.ednei.updateapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.ednei.updateapp.enuns.ConfigEnum;

import java.io.File;

public class ShowNote extends Activity {

    private BroadcastReceiver receiver;
    private long enqueue;
    private DownloadManager dm;
    boolean isDeleted;

    private String TAG = "ShowNoteLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Atualização");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Nova versão disponivel para atualização, clique para instalar.");
        builder.getContext().setTheme(R.style.AppTheme);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                File file = new File(ConfigEnum.PATH.getConfig() + "" + ConfigEnum.NAME_APK.getConfig());
                if (file.exists()) {
                    isDeleted = file.delete();
                    deleteAndInstall();
                } else {
                    firstTimeInstall();
                }
            }
        });

        builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShowNote.this.finish();
            }
        });

        builder.show();
    }

    private void firstTimeInstall() {
        Log.d("May be 1st Update:", "OR deleteed from folder");
        downloadAndInstall();
    }

    private void deleteAndInstall() {
        if (isDeleted) {
            Log.d(TAG, "Deleted Existance file:" + String.valueOf(isDeleted));
            downloadAndInstall();

        } else {
            Log.d(TAG, "NOT DELETED:" + String.valueOf(isDeleted));
            Toast.makeText(getApplicationContext(), "Error in Updating...Please try Later", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadAndInstall() {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(ConfigEnum.URL_APK.getConfig()));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ConfigEnum.NAME_APK.getConfig());
        enqueue = dm.enqueue(request);
        final String PATH = ConfigEnum.PATH.getConfig();
        final String NAME_APK = ConfigEnum.NAME_APK.getConfig();

        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                try{

                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Toast.makeText(getApplicationContext(), "Download Completed", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Download completed");
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);

                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                            Log.d(TAG, "ainfo" + uriString);

                            if (downloadId == c.getInt(0)) {

                                Log.d(TAG, "DOWNLOAD PATH:" + c.getString(c.getColumnIndex("local_uri")));
                                Log.d(TAG, "isRooted:" + String.valueOf(isRooted()));

                                if (!isRooted()) {

                                    //if your device is not rooted
                                    Intent intent_install = new Intent(Intent.ACTION_VIEW);
                                    intent_install.setDataAndType(Uri.fromFile(new File(PATH + "" + NAME_APK)), "application/vnd.android.package-archive");

                                    Log.d(TAG, "phone path" + PATH + "" + NAME_APK);
                                    startActivity(intent_install);

                                    Toast.makeText(getApplicationContext(), "App Installing", Toast.LENGTH_LONG).show();

                                } else {

                                    //if your device is rooted then you can install or update app in background directly
                                    Toast.makeText(getApplicationContext(), "App Installing...Please Wait", Toast.LENGTH_LONG).show();
                                    File file = new File(PATH, NAME_APK);
                                    Log.d(TAG, "Files and path: "+PATH + "" + NAME_APK);

                                    if (file.exists()) {

                                        try {

                                            String command;
                                            Log.d(TAG, "File exists:" + NAME_APK);
                                            command = "pm install -r " + PATH + "" + NAME_APK;
                                            Log.d("COMMAND:", command);
                                            Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                                            proc.waitFor();
                                            Toast.makeText(getApplicationContext(), "App Installed Successfully, verion ", Toast.LENGTH_LONG).show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "Error install " + e.getMessage());
                                        }
                                    } else {
                                        Log.e(TAG, "Error install, not exists app.");
                                    }
                                }
                            }
                        }
                    }

                    c.close();
                }

                }catch (Exception e){

                    Log.e(TAG, "Error "+e.getMessage());
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private static boolean isRooted() {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName) {

        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/", "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

