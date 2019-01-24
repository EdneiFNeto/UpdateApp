package com.example.ednei.updateapp;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.ednei.updateapp.util.DialogUtil;
import com.example.ednei.updateapp.util.DownloadUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Verify version app ,
 * Download app from notification
 */

public class VersionAsyncTask extends AsyncTask<String, String, String> {

    private ProgressDialog bar;
    private Context context;
    private static String TAG = "VersionLog";
    private List list;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected String doInBackground(String... strings) {

        JSONParser parser = new JSONParser();
        list = new ArrayList();

       /* try{

            URL url = new URL("https://appeste.000webhostapp.com/versionApp.php");
            URLConnection urlConnection = url.openConnection();

            if(urlConnection.getURL()!= null){

                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;

                //read json get url
                while((inputLine = in.readLine()) != null) {

                    Log.e(TAG, "Entrou no while" );

                    JSONArray arrayJson = (JSONArray) parser.parse(inputLine);

                    for(int i= 0;i < arrayJson.size(); i++) {

                        JSONObject jsonObject = (JSONObject) arrayJson.get(i);
                        String version  = (String) jsonObject.get("version");
                        Log.e(TAG, "version: "+version);
                    }
                }
            }

        }catch (Exception e){
            Log.e(TAG, "Error "+e.getMessage());
        }*/

        Log.e(TAG, "Rodando...");

        return null;
    }
}
