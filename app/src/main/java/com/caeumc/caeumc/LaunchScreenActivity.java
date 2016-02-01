package com.caeumc.caeumc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.splunk.mint.Mint;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LaunchScreenActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    Intent intent;
    boolean updateAvailable;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(LaunchScreenActivity.this, "8729e5df");

        // setContentView(R.layout.activity_launch_screen);

        // prefs = getSharedPreferences("com.caeumc.caeumc", MODE_PRIVATE);

        try {
            new BackgroundTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        Intent intent;

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            intent = new Intent(LaunchScreenActivity.this, NavDrawerActivity.class);
            //prefs.edit().putBoolean("firstrun", false).commit();
        }

        @Override
        protected Void doInBackground (Void[] params) {
            /*  Use this method to load background
            * data that your app needs. */
            if (checkFirstRun()) {
                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
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
        protected void onPostExecute (Void o) {
            super.onPostExecute(o);
//            Pass your loaded data here using Intent
//            intent.putExtra("data_key", "");
            startActivity(intent);
            finish();
        }
    }

    private boolean checkFirstRun () {

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
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
        return firstRun;
    }

}


