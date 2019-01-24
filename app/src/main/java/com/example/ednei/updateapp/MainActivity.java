package com.example.ednei.updateapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ednei.updateapp.util.DownloadUtil;
import com.example.ednei.updateapp.util.VerifyVersion;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView versionName = (TextView) findViewById(R.id.textViersion);
        TextView versionCode = (TextView) findViewById(R.id.textVersionCode);
        versionName.setText("Version name: " + BuildConfig.VERSION_NAME);
        versionCode.setText("Version code: " + BuildConfig.VERSION_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        //verify version app
        new VerifyVersion(MainActivity.this).execute();
    }

}
