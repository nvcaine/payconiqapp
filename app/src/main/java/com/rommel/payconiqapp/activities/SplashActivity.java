package com.rommel.payconiqapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.interfaces.INetworkStateUpdateCallback;
import com.rommel.payconiqapp.util.NetworkUtil;
import com.rommel.payconiqapp.util.RequestUtil;

/**
 * The initial activity displayed to the user.
 */
public class SplashActivity extends AppCompatActivity {

    private Button onlineButton;

    public void loadRepositories(View view) {
        navigateToListActivity(true);
    }

    public void proceedOffline(View view) {
        navigateToListActivity(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        init();
    }

    /**
     * Initialize view instances and interface specifications.
     */
    private void init() {

        onlineButton = (Button) findViewById(R.id.splash_online_button);

        updateInterface(RequestUtil.isConnected(this));
        initNetworkStateListener();
    }

    /**
     * Add network state change listener and handlers.
     */
    private void initNetworkStateListener() {

        NetworkUtil.getInstance().setStateChangeHandler(new INetworkStateUpdateCallback() {
            @Override
            public void onConnect() {
                updateInterface(true);
            }

            @Override
            public void onDisconnect() {
                updateInterface(false);
            }
        });
    }

    /**
     * Update online button visibility based on connection availability.
     * Offline button remains always visible.
     * @param connectionAvailable true/false
     */
    private void updateInterface(boolean connectionAvailable) {

        int onlineButtonVisibility = View.GONE;

        if (connectionAvailable) {
            onlineButtonVisibility = View.VISIBLE;
        }

        onlineButton.setVisibility(onlineButtonVisibility);
    }

    /**
     * Navigate to the appropriate activity based on connection availability
     * @param connectionAvailable true/false
     */
    private void navigateToListActivity(boolean connectionAvailable) {

        Class<?> destinationActivityClass = OfflineActivity.class;

        if (connectionAvailable) {
            destinationActivityClass = OnlineActivity.class;
        }

        Intent intent = new Intent(this, destinationActivityClass);
        startActivity(intent);
    }
}
