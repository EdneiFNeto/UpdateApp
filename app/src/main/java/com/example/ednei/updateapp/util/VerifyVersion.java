package com.example.ednei.updateapp.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ednei.updateapp.BuildConfig;
import com.example.ednei.updateapp.ShowNote;
import com.example.ednei.updateapp.enuns.ConfigEnum;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class VerifyVersion extends AsyncTask<String, String, String> {

    private String TAG = "VerifyVersionLog";
    private List list;
    private Context context;

    public VerifyVersion(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (list.size() > 0) {


            if (!list.get(0).toString().equals(BuildConfig.VERSION_NAME)) {

                try {
                    //delete file
                    File file = new File(ConfigEnum.PATH.getConfig(), ConfigEnum.NAME_APK.getConfig());
                    if(file.exists())
                        file.delete();

                    //download new apk
                    DownloadManagerUtil.downloadByDownloadManager(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Eror Download "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        JSONParser parser = new JSONParser();
        list = new ArrayList();

        try {

            //URL url = new URL("https://appeste.000webhostapp.com/versionApp.php");
            URL url = new URL(ConfigEnum.URL_JSON.getConfig());
            URLConnection urlConnection = url.openConnection();

            if (urlConnection.getURL() != null) {

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;

                //read json get url
                while ((inputLine = in.readLine()) != null) {

                    Log.e(TAG, "Entrou no while");
                    JSONArray arrayJson = (JSONArray) parser.parse(inputLine);

                    for (int i = 0; i < arrayJson.size(); i++) {

                        JSONObject jsonObject = (JSONObject) arrayJson.get(i);
                        String version = (String) jsonObject.get("versionName");
                        String versionCode = (String) jsonObject.get("versionCode");
                        list.add(version);
                        Log.e(TAG, "version: " + version);
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Version Error " + e.getMessage());
        }

        return null;
    }
}
