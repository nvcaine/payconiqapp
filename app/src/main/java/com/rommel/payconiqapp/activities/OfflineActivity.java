package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.data.RepositoryObject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * The activity displayed if no Internet connection is available.
 */
public class OfflineActivity extends Activity {

    private static String LOG_TAG = OfflineActivity.class.getName();

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offline);

        realm = Realm.getDefaultInstance();
        RealmResults<RepositoryObject> repos = realm.where(RepositoryObject.class).findAll();

        if (repos.size() == 0)
            executeRealmTransaction(realm);

        for (int i = 0; i < repos.size(); i++) {
            RepositoryObject item = repos.get(i);
            Log.d(LOG_TAG, "Offline repo item: " + item.getName() + " - " + item.getId());
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        realm.close();
    }

    private void executeRealmTransaction(Realm realm) {

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                RepositoryObject r = realm.createObject(RepositoryObject.class);

                r.setName("Test repo");
                r.setId("110110");
            }
        });

    }
}
