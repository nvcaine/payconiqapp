package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.adapters.RepositoriesAdapter;
import com.rommel.payconiqapp.data.RepositoryObject;
import com.rommel.payconiqapp.util.RealmUtil;

import java.util.ArrayList;

/**
 * The activity displayed if no Internet connection is available.
 */
public class OfflineActivity extends Activity {

    private static String LOG_TAG = OfflineActivity.class.getName();

    private ListView repositoriesList;
    private RepositoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offline);

        init();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        RealmUtil.close();
    }

    private void init() {

        ArrayList<RepositoryObject> offlineRecords = RealmUtil.getInstance().getOfflineRecords();
        adapter = new RepositoriesAdapter(this, R.layout.item_repository, offlineRecords);
        repositoriesList = (ListView) findViewById(R.id.repositories_list);
        repositoriesList.setAdapter(adapter);
    }


}
