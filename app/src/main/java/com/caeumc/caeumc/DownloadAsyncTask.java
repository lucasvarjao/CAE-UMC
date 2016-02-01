package com.caeumc.caeumc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.jar.JarFile;

/*
 * Created by EDNEI on 10/01/2016.
 */
class DownloadAsyncTask extends AsyncTask<Void, Integer, File> {
    private final File mPath;
    private final Context mContext;
    private final Boolean mTexto;
    private final String mUrl;
    private PowerManager.WakeLock mWakeLock;

    DownloadAsyncTask (File path, Context context, Boolean texto, String url) {
        this.mPath = path;
        this.mContext = context;
        this.mTexto = texto;
        this.mUrl = url;

    }

    @Override
    protected void onPreExecute () {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        if (!mTexto) {
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            NavDrawerActivity.mProgressDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate (Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        if (!mTexto) {
            NavDrawerActivity.mProgressDialog.setIndeterminate(false);
            NavDrawerActivity.mProgressDialog.setMax(100);
            NavDrawerActivity.mProgressDialog.setProgress(progress[0]);
        }
    }

    @Override
    protected File doInBackground (Void... params) {
        File path = mPath;

        boolean statusdownload = DownloadFilesFTP(mPath);
        if (statusdownload) {
            return path;
        } else {
            return null;
        }

    }

    @Override
    protected void onPostExecute (File path) {
        if (!mTexto) {
            mWakeLock.release();
            NavDrawerActivity.mProgressDialog.dismiss();
        }
        if (path != null) {
            if (!mTexto) {
                boolean validAPK = true;
                try {
                    new JarFile(path);
                } catch (Exception ex) {
                    validAPK = false;
                    ex.printStackTrace();
                }
                if (validAPK) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.fromFile(path), "application/vnd.android.package-archive");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Log.d("Lofting", "About to install new .apk");
                    mContext.startActivity(i);
                } else {
                    Log.v("Validar APK", "Inválida");
                }
            }
        } else {
            Log.e("Instalar app", "deu ruim");
        }

    }

    private boolean DownloadFilesFTP (File caminho) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(mUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.v("Resposta", "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage());
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(caminho);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return false;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }

        if (!mTexto) {

            try {
                Runtime.getRuntime().exec("chmod 666 " + caminho);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
        /*FTPClient con = null;

        try {
            con = new FTPClient();
            con.connect("ftp.caeumc.com");

            if (con.login("lucasvarjao", "Q9mcbr1ncj")) {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                con.setAutodetectUTF8(true);
                //String data = "/sdcard/vivekm4a.m4a";

                InputStream inputStream = con.retrieveFileStream("/public_html/download/" + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(data);
                org.apache.commons.io.IOUtils.copy(inputStream, fileOutputStream);
                fileOutputStream.flush();
                org.apache.commons.io.IOUtils.closeQuietly(fileOutputStream);
                org.apache.commons.io.IOUtils.closeQuietly(inputStream);

                boolean comandOK = con.completePendingCommand();

                //OutputStream out = new FileOutputStream(data);
                // boolean result=false;
                // result = con.retrieveFile("/public_html/download/" + fileName, out);
                //out.close();
                if (comandOK) Log.v("download result", "succeeded");
                con.logout();
                con.disconnect();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.v("download result", "failed");
            e.printStackTrace();
            return false;
        }


    }*/

}
