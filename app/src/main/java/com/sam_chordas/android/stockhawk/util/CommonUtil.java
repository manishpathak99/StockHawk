package com.sam_chordas.android.stockhawk.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

/**
 * Created by manishpathak on 4/9/16.
 */
public class CommonUtil {

    public static void showSnackBar(final Activity activity, @NonNull final String message) {
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    /**
     * Is tablet boolean.
     *
     * @param context given context
     * @return true if its a tablet
     */
    public static boolean isTablet(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void showToastInUiThread(@NonNull final Context ctx,
                                          @NonNull final String stringMsg) {

        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, stringMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
