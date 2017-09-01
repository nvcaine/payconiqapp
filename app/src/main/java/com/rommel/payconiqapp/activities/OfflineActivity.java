package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.adapters.RepositoriesAdapter;
import com.rommel.payconiqapp.data.RepositoryObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * The activity displayed if no Internet connection is available.
 */
public class OfflineActivity extends Activity {

    private static String LOG_TAG = OfflineActivity.class.getName();

    private ListView repositoriesList;
    private RepositoriesAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offline);

        init();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        realm.close();
    }

    private void init() {

        realm = Realm.getDefaultInstance();
        RealmResults<RepositoryObject> repos = realm.where(RepositoryObject.class).findAll();

        adapter = new RepositoriesAdapter(this, R.layout.item_repository, parseRealmResults(repos));
        repositoriesList = (ListView) findViewById(R.id.repositories_list);
        repositoriesList.setAdapter(adapter);
    }

    private ArrayList<RepositoryObject> parseRealmResults(RealmResults<RepositoryObject> repos) {

        ArrayList<RepositoryObject> results = new ArrayList<>();

        for (int i = 0; i < repos.size(); i++) {
            RepositoryObject repo = repos.get(i);
            results.add(repo);
        }

        return results;
    }
}
