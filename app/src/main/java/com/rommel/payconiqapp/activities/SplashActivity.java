package com.rommel.payconiqapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.interfaces.INetworkStateUpdateCallback;
import com.rommel.payconiqapp.util.NetworkUtil;
import com.rommel.payconiqapp.util.RequestUtil;

/**
 * The initial activity displayed to the user.
 * The device connection is tested and the user is redirected accordingly.
 */
public class SplashActivity extends AppCompatActivity {

    private static String LOG_TAG = SplashActivity.class.getName();

    private Button onlineButton;
    private Button offlineButton;

    /**
     * Redirect the user to the appropriate activity based on connectivity
     * @param view
     */
    public void showRepoList(View view) {
        // recheck connectivity to prevent users from disabling connection after startup
        boolean connectionAvailable = RequestUtil.isConnected(this);
        navigateToListActivity(connectionAvailable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initInterface();
        checkConnection();

        NetworkUtil.getInstance().setStateChangeHandler(new INetworkStateUpdateCallback() {
            @Override
            public void onConnect() {
                Log.d(LOG_TAG, "### Connected in activity");
                updateInterface(true);
            }

            @Override
            public void onDisconnect() {
                Log.d(LOG_TAG, "### Disconnected in activity");
                updateInterface(false);
            }
        });
    }

    private void checkConnection() {

        boolean connectionAvailable = RequestUtil.isConnected(this);

        updateInterface(connectionAvailable);
        Log.d(LOG_TAG, "Connection available: " + connectionAvailable);
    }

    /**
     * Initialize view instances.
     */
    private void initInterface() {

        onlineButton = (Button) findViewById(R.id.splash_online_button);
        offlineButton = (Button) findViewById(R.id.splash_offline_button);
    }

    /**
     * Update online/offline button visibility based on connection availability
     * @param connectionAvailable
     */
    private void updateInterface(boolean connectionAvailable) {

        if (connectionAvailable) {
            onlineButton.setVisibility(View.VISIBLE);
            offlineButton.setVisibility(View.GONE);
        } else {
            onlineButton.setVisibility(View.GONE);
            offlineButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Navigate to the appropriate activity based on connection availability
     * @param connectionAvailable
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
