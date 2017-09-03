package com.rommel.payconiqapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.data.RepositoriesAdapter;
import com.rommel.payconiqapp.data.RepositoryObject;
import com.rommel.payconiqapp.util.RealmUtil;

import java.util.ArrayList;

/**
 * The activity displayed if no Internet connection is available.
 */
public class OfflineActivity extends AppCompatActivity {

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
        RepositoriesAdapter adapter = new RepositoriesAdapter(this, R.layout.item_repository, offlineRecords);
        ListView repositoriesList = (ListView) findViewById(R.id.repositories_list);
        repositoriesList.setAdapter(adapter);
    }
}
