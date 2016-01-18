package com.caeumc.caeumc;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by EDNEI on 16/01/2016.
 */
public class GetUsernameTask extends AsyncTask<Void, Void, Void> {
        Activity mActivity;
        String mScope;
        String mEmail;

        GetUsernameTask(Activity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
        }

@Override
protected Void doInBackground(Void... params) {
        try {
        String token = fetchToken();
        if (token != null) {
        // **Insert the good stuff here.**
        // Use the token to access the user's Google data.



        }
        } catch (IOException e) {
        // The fetchToken() method handles Google-specific exceptions,
        // so this indicates something went wrong at a higher level.
        // TIP: Check for network connectivity before starting the AsyncTask.

        }
        return null;
        }

/**
 * Gets an authentication token from Google and handles any
 * GoogleAuthException that may occur.
 */
protected String fetchToken() throws IOException {
        try {
            Account account = new Account(mEmail, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            return GoogleAuthUtil.getToken(mActivity, account, mScope);

        } catch (UserRecoverableAuthException userRecoverableException) {
        // GooglePlayServices.apk is either old, disabled, or not present
        // so we need to show the user some UI in the activity to recover.
        NavDrawerActivity.handleException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {
        // Some other type of unrecoverable exception has occurred.
        // Report and log the error as appropriate for your app.

        }
        return null;
        }

        }
