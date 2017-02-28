package top.lemonsoda.gunners.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chuanl on 2/27/17.
 */

public class NetworkUtils {

    /**
     * Check the status of Internet connection
     *
     * @param context
     * @return
     */
    public static Boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isConnected() && networkinfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
