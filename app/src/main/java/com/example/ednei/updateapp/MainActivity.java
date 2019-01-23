package com.example.ednei.updateapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView versionName = (TextView) findViewById(R.id.textViersion);
        TextView versionCode = (TextView) findViewById(R.id.textVersionCode);
        versionName.setText("Version name: "+BuildConfig.VERSION_NAME);
        versionCode.setText("Version code: "+BuildConfig.VERSION_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkPermisssion()){

        }else{
            requestPermission();
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    private boolean checkPermisssion(){

        int write_external_extorage = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return write_external_extorage == PackageManager.PERMISSION_GRANTED ||
                write_external_extorage == PackageManager.PERMISSION_DENIED;
    }
}
