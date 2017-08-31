package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.interfaces.IRequestCallback;
import com.rommel.payconiqapp.util.RequestUtil;

import org.json.JSONArray;

/**
 * Created by Rommel on 8/29/2017.
 */
public class OnlineActivity extends Activity {

    private static String LOG_TAG = OnlineActivity.class.getName();
    private static String REPOS_URL = "https://api.github.com/users/JakeWharton/repos?";
    private static int ITEMS_PER_PAGE = 15;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online);
        loadRepositories(currentPage);
    }

    private void loadRepositories(int currentPage) {

        RequestUtil.performRequest(getURLWithParams(currentPage), new IRequestCallback() {
            @Override
            public void executeCallback(JSONArray jsonArray) {
                Log.d(LOG_TAG, "JSON Data:" + jsonArray.toString());
            }
        }, this);
    }

    private String getURLWithParams(int currentPage) {

        return REPOS_URL + "page=" + currentPage + "&per_page=" + ITEMS_PER_PAGE;
    }
}
