package com.example.ednei.updateapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.ednei.updateapp.util.DialogUtil;
import com.example.ednei.updateapp.util.InstallAppUtil;

public class NotificationBroadcCastReceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            new InstallAppUtil().openIntentInstall(context);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error install: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
