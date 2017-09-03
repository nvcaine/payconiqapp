package com.rommel.payconiqapp.util;

import com.rommel.payconiqapp.interfaces.INetworkStateUpdateCallback;

/**
 * Used to mediate functionality between network state updates and activity interface.
 */
public class NetworkUtil {

    private static NetworkUtil instance;

    private INetworkStateUpdateCallback networkStateUpdateCallback;

    private NetworkUtil() {}

    public static NetworkUtil getInstance() {

        if (instance == null) {
            instance = new NetworkUtil();
        }

        return instance;
    }

    public void setStateChangeHandler(INetworkStateUpdateCallback callback) {
        networkStateUpdateCallback = callback;
    }

    public void onConnect() {
        networkStateUpdateCallback.onConnect();
    }

    public void onDisconnect() {
        networkStateUpdateCallback.onDisconnect();
    }
}
