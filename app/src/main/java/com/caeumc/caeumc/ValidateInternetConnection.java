package com.caeumc.caeumc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * Created by EDNEI on 13/01/2016.
 */
class ValidateInternetConnection {

    private final Context ctx;

    public ValidateInternetConnection (Context context) {
        this.ctx = context;
    }

    public Integer isConnected () {
        // int tipoConexao = -1;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getState() == NetworkInfo.State.CONNECTED) {

                    if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        return ConnectivityManager.TYPE_MOBILE;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        return ConnectivityManager.TYPE_WIFI;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }

            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}