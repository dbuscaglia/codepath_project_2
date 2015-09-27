package danbuscaglia.googleimagesearch.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by danbuscaglia on 9/26/15.
 */
public class ConnectionHelper {

    private static final String TAG = "ConnectionHelper";

    public static long lastNoConnectionTs = -1;
    public static boolean isOnline = true;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        return isConnected;
    }

    public static boolean isConnectedOrConnecting(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

}