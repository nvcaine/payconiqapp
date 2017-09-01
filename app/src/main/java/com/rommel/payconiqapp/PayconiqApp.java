package com.rommel.payconiqapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Payconiq application class. Used for initializing Realm.
 */

public class PayconiqApp extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        Realm.init(this);
    }
}
