package com.sam_chordas.android.stockhawk.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;

/**
 * Created by manishpathak on 3/9/16.
 */
public class NetworkUtil {

    public static boolean checkInternetConnection(Context paramContext) {
        // need manifest permission <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void showNetWorkSnackBar(final Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(activity.findViewById(android.R.id.content), "Check internet connectivity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        }
}
