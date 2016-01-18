package com.caeumc.caeumc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.splunk.mint.Logger;
import com.splunk.mint.Mint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LaunchScreenActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    Intent intent;
    boolean updateAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(LaunchScreenActivity.this, "8729e5df");

       // setContentView(R.layout.activity_launch_screen);

       // prefs = getSharedPreferences("com.caeumc.caeumc", MODE_PRIVATE);

        try {
            new BackgroundTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private class BackgroundTask extends AsyncTask {
        Intent intent;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(LaunchScreenActivity.this, NavDrawerActivity.class);
            //prefs.edit().putBoolean("firstrun", false).commit();
        }
        @Override
        protected Object doInBackground(Object[] params) {
            /*  Use this method to load background
            * data that your app needs. */
            if (checkFirstRun()) {
                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<InitializeDataBase> initializeDataBase = InitializeDataBase.listAll(InitializeDataBase.class);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            Pass your loaded data here using Intent
//            intent.putExtra("data_key", "");
            intent.putExtra("UPDATE_AVAILABLE", updateAvailable);
            NavDrawerActivity.attAvailable = updateAvailable;
            startActivity(intent);
            finish();
        }
    }

    private boolean checkFirstRun() {

        final String PREFS_NAME = "com.caeumc.caeumc";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;
        boolean firstRun = false;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = BuildConfig.VERSION_CODE;
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
            firstRun = false;
        }

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            firstRun = false;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            firstRun = true;

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
            firstRun = false;
        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();
        return firstRun;
    }

    private boolean checkUpdate() {

        final String PREFS_NAME = "com.caeumc.caeumc";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = BuildConfig.VERSION_CODE;
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }

        int savedVersionCode = 0;
        String fileName = "VersionApplication.txt";
        File versionApplication = new File(getApplicationContext().getCacheDir(), fileName);
        String url = "http://caeumc.com/download/VersionApplication.txt";
        //boolean statusFTPDownload = DownloadFilesFTP(versionApplication, fileName);
        try {
            new DownloadAsyncTask(versionApplication, fileName, getApplicationContext(), true, url).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // if (statusFTPDownload) {
        String versionServer = readFileAsString(fileName);
        if (versionServer != null) {
            if (!versionServer.isEmpty()) {
                savedVersionCode = Integer.parseInt(versionServer);
            } else {
                savedVersionCode = -1;
            }
        } else {
            savedVersionCode = -1;
        }

        //} else {
        // Log.e("Download arquivo versão", "Deu ruim");
        //  savedVersionCode = -1;
        // }

        // Get saved version code

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            return false;

        } else if (savedVersionCode == DOESNT_EXIST) {

            return false;


        } else if (currentVersionCode < savedVersionCode) {

            return true;

        } else {
            return false;
        }
        // Update the shared preferences with the current version code
    }

    public String readFileAsString(String fileName) {
        // Context context = App.instance.getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(getApplicationContext().getCacheDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Logger.logError(e.getMessage());
        } catch (IOException e) {
            Logger.logError(e.getMessage());
        }

        return stringBuilder.toString();
    }


}


