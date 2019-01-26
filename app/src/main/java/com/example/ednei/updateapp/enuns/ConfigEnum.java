package com.example.ednei.updateapp.enuns;

import android.os.Environment;

public enum ConfigEnum {

    PATH(Environment.getExternalStorageDirectory() + "/Download/"),
    NAME_APK("tv-release.apk"),
    STR_URL("https://www.divertenet.com.br/apps/tv-release.apk");

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
