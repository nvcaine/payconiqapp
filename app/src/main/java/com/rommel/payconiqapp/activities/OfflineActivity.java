package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;

import com.rommel.payconiqapp.R;

/**
 * The activity displayed if no Internet connection is available.
 */
public class OfflineActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offline);
    }
}
