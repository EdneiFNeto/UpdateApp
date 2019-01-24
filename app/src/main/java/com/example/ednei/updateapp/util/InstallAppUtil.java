package com.example.ednei.updateapp.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.example.ednei.updateapp.BuildConfig;

import java.io.File;

public class InstallAppUtil {

    private static String PATH = Environment.getExternalStorageDirectory()+"/Download/";
    private static String nameApk = "tv-realee.apk";

    public static void openIntentInstall(Context context) throws  Exception{

        File toInstall = new File(PATH, nameApk);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider", toInstall);
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(toInstall);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
