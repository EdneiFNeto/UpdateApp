package com.example.ednei.updateapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionAsyncTask extends AsyncTask<String, Integer, Boolean> {

    private ProgressDialog bar ;
    private Context context;
    private static String TAG="VersionLog";
    private String PATH = Environment.getExternalStorageDirectory()+"/Download/";
    private String nameApk = "";
    private String StrURL= "";

    public VersionAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        bar = new ProgressDialog(context);
        bar.setCancelable(false);
        bar.setTitle("Downloading...");
        bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        bar.setIndeterminate(true);
        bar.setCanceledOnTouchOutside(false);
        bar.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        bar.setIndeterminate(false);
        bar.setMax(100);
        bar.setProgress(progress[0]);
        String msg = "";

        if (progress[0] > 99) {
            msg = "Finishing... ";
        } else {
            msg = "Downloading... " + progress[0] + "%";
        }

        bar.setMessage(msg);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        //install app
        bar.dismiss();

        if (result) {
            openIntentInstall(context, PATH, nameApk);
        } else {
            Toast.makeText(context, "Error: Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        Boolean flag = false;

        try {

            URL url = new URL(StrURL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            int total_size = c.getContentLength();
            c.connect();


            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, nameApk);

            if (outputFile.exists()) {
                outputFile.delete();
            }

            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            int per = 0;
            int downloaded = 0;

            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
                downloaded += len1;
                per = (int) (downloaded * 100 / total_size);
                publishProgress(per);
            }

            fos.close();
            is.close();

            flag = true;
        } catch (Exception e) {
            Log.e(TAG, "Update Error: " + e.getMessage());
            flag = false;
        }

        return flag;
    }

    private void openIntentInstall(Context context, String location, String nameApk) {

        File toInstall = new File(location, nameApk);
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
