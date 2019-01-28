package com.example.ednei.updateapp.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.ednei.updateapp.R;
import com.example.ednei.updateapp.ShowNote;
import com.example.ednei.updateapp.enuns.ConfigEnum;

import java.io.File;

public class DialogUtil {

    private static AlertDialog alerta;

    public static void showDialog(final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Atualização");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Nova versão disponivel para atualização, clique para instalar.");
        builder.getContext().setTheme(R.style.AppTheme);
        builder.setPositiveButton("Atualzar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    InstallAppUtil.openIntentInstall(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("DialogUtilLog", "Error install " + e.getMessage());
                }
            }
        });

        builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
