package com.rommel.payconiqapp.interfaces;

/**
 * Interface for defining network state updates handlers.
 */
public interface INetworkStateUpdateCallback {

    void onConnect();
    void onDisconnect();
}
