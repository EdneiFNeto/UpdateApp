package com.example.ednei.updateapp.enuns;

import android.os.Environment;

import com.example.ednei.updateapp.BuildConfig;

public enum ConfigEnum {

    IP("192.168.0.27"),
    PATH(Environment.getExternalStorageDirectory() + "/Download/"),
    NAME_APK("app-release.apk"),
    URL_APK("http://"+IP.getConfig()+"/Dev/apps/"+NAME_APK.getConfig()),
    URL_JSON("http://"+IP.getConfig()+"/Dev/apps/version.php"),
    VERSION_NAME(""+BuildConfig.VERSION_NAME),
    VERSION_CODE(""+BuildConfig.VERSION_CODE),
    APPLICATION_ID(""+BuildConfig.APPLICATION_ID);

    private String config;

    ConfigEnum (String config) {
        this.config = config;
    }

    public String getConfig() {
        return config;
    }

    public void setCongig(String config) {
        this.config= config;
    }
}
