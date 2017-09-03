package com.rommel.payconiqapp.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Used to listen for network state updates.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = NetworkStateReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null) {

            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);

            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.i(LOG_TAG, "Connected to network " + ni.getTypeName());
                NetworkUtil.getInstance().onConnect();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(LOG_TAG, "Disconnected from network.");
                NetworkUtil.getInstance().onDisconnect();
            }
        }
    }
}
