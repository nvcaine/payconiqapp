package com.rommel.payconiqapp.util;

import android.util.Log;

import com.rommel.payconiqapp.data.RepositoryObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Realm wrapper to avoid adding activity dependencies.
 */
public class RealmUtil {

    private static final String LOG_TAG = RealmUtil.class.getName();

    private static RealmUtil instance;
    private Realm realm;

    private RealmUtil() {}

    /**
     * Singleton instance access method
     * @return the RealmUtil instance
     */
    public static RealmUtil getInstance() {

        if(instance == null) {
            instance = new RealmUtil();
        }

        instance.realm = Realm.getDefaultInstance();

        return instance;
    }

    /**
     * Return all records saved locally.
     * @return a list of RepositoryObject items
     */
    public ArrayList<RepositoryObject> getOfflineRecords() {

        RealmResults<RepositoryObject> repos = realm.where(RepositoryObject.class).findAll();

        return parseRealmResults(repos);
    }

    /**
     * Parse a list of repositories to check if they are saved offline.
     * @param repositories a list of RepositoryObject data
     */
    public void syncOfflineRecords(ArrayList<RepositoryObject> repositories) {

        for (int i = 0; i < repositories.size(); i++) {
            checkAndStoreRecord(repositories.get(i));
        }
    }


    public static void close() {

        instance.realm.close();
    }

    /**
     * Check if a record has already been saved. If not, it is added to offline records.
     * @param repository a RepositoryObject item
     */
    private void checkAndStoreRecord(final RepositoryObject repository) {

        RepositoryObject storedRecord = realm.where(RepositoryObject.class).equalTo("id", repository.getId()).findFirst();

        if (storedRecord == null) {
            realm.executeTransaction(new Realm.Transaction() {

                @Override
                public void execute(Realm realm) {
                    realm.insert(repository);
                }
            });
            Log.d(LOG_TAG, "Added new repository object: " + repository.getName() + " - " + repository.getId());
        }
    }

    /**
     * Convert a collection of RealmResults to ArrayList
     * @param repos RealmResults collection of RepositoryObject data
     * @return ArrayList of RepositoryObject items
     */
    private static ArrayList<RepositoryObject> parseRealmResults(RealmResults<RepositoryObject> repos) {

        ArrayList<RepositoryObject> results = new ArrayList<>();

        for (int i = 0; i < repos.size(); i++) {
            RepositoryObject repo = repos.get(i);
            results.add(repo);
        }

        return results;
    }
}
