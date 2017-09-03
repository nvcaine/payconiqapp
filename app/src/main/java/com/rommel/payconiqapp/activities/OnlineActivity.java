package com.rommel.payconiqapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.data.RepositoriesAdapter;
import com.rommel.payconiqapp.data.RepositoryObject;
import com.rommel.payconiqapp.interfaces.ISimpleCallback;
import com.rommel.payconiqapp.util.AlertUtil;
import com.rommel.payconiqapp.util.RealmUtil;
import com.rommel.payconiqapp.util.RequestUtil;
import com.rommel.payconiqapp.util.SettingsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The activity the user is redirected after initialization, if an Internet connection is available.
 */
public class OnlineActivity extends AppCompatActivity {

    private ListView repositoriesList;
    private RepositoriesAdapter adapter;
    private int currentPage = 1;
    private RequestQueue requestQueue;

    // used to prevent auto-invoking the scroll listener in case an alert is displayed
    private boolean receivedError = false;

    /**
     * Scroll listener used for loading subsequent repositories.
     * A variable is used to disable scroll listening while loading next page.
     */
    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
        }

        @Override
        public void onScroll(AbsListView absListView, int firstIndex, int visibleCount, int totalCount) {

            if (!receivedError) {
                checkForListUpdates(repositoriesList.getLastVisiblePosition(), totalCount, SettingsUtil.AUTOLOAD_LIMIT);
            } else {
                receivedError = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online);

        init();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        RealmUtil.close();
    }

    /**
     * Get repository list instance and add scroll listener.
     */
    private void init() {

        adapter = new RepositoriesAdapter(this, R.layout.item_repository, new ArrayList<RepositoryObject>());
        repositoriesList = (ListView) findViewById(R.id.repositories_list);
        repositoriesList.setAdapter(adapter);
        requestQueue = RequestUtil.getNewRequestQueue(this);

        loadRepositories(currentPage);
    }

    /**
     * Load the repository data for the current page and run a callback on completion.
     *
     * @param currentPage the current page index
     */
    private void loadRepositories(final int currentPage) {

        String url = getURLWithParams(SettingsUtil.REPOS_URL, currentPage, SettingsUtil.ITEMS_PER_PAGE);
        RequestUtil.performRequest(url, requestQueue, new ISimpleCallback<JSONArray>() {

            @Override
            public void executeCallback(JSONArray jsonArray) {

                parseAndUpdateRepositories(jsonArray, SettingsUtil.ITEMS_PER_PAGE);
                OnlineActivity.this.currentPage++;
            }
        }, new ISimpleCallback<VolleyError>() {
            @Override
            public void executeCallback(VolleyError volleyError) {
                AlertUtil.showSimpleAlert(OnlineActivity.this,
                        "Error loading data:\n\n" + volleyError.getMessage(),
                        new ISimpleCallback<Object>() {
                            @Override
                            public void executeCallback(Object result) {
                                receivedError = true;
                                repositoriesList.setOnScrollListener(scrollListener);
                            }
                        });
            }
        });
    }

    /**
     * Convert JSONArray object to RepositoryObject list.
     * Display list.
     *
     * @param jsonArray    the JSON data representing repositories
     * @param itemsPerPage number of items loaded in a single request
     */
    private void parseAndUpdateRepositories(JSONArray jsonArray, int itemsPerPage) {

        ArrayList<RepositoryObject> repositories = null;

        try {
            repositories = parseRepositories(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateRepositoriesList(repositories, itemsPerPage);
        RealmUtil.getInstance().syncOfflineRecords(repositories);
    }

    /**
     * Get the URL for loading the current page data
     *
     * @param url          domain and API endpoint URL
     * @param currentPage  the number of the current page
     * @param itemsPerPage number of items loaded in a single request
     * @return a string representing the URL with proper parameters
     */
    private String getURLWithParams(String url, int currentPage, int itemsPerPage) {

        return url + "?page=" + currentPage + "&per_page=" + itemsPerPage;
    }

    /**
     * Parse a JSON object data set to a list of RepositoryObject objects
     *
     * @param data the JSON objects array
     * @return ArrayList containing RepositoryObject objects
     * @throws JSONException thrown in case an item is missing 'id' or 'name'
     */
    private ArrayList<RepositoryObject> parseRepositories(JSONArray data) throws JSONException {

        ArrayList<RepositoryObject> result = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            RepositoryObject repository = getRepositoryObjectFromJSON(data.getJSONObject(i));
            result.add(repository);
        }

        return result;
    }

    /**
     * Convert JSON object to RepositoryObject
     *
     * @param data JSON data to parse
     * @return RepositoryObject object
     * @throws JSONException thrown in case data object does not have 'id' and 'name'
     */
    private RepositoryObject getRepositoryObjectFromJSON(JSONObject data) throws JSONException {

        RepositoryObject result;

        result = new RepositoryObject(
                data.getString("id"),
                data.getString("name")
        );

        return result;
    }

    /**
     * Update current repositories data set.
     * Reset repositories list scroll listener.
     *
     * @param repositories repositories data set
     * @param itemsPerPage number of items loaded in a single request
     */
    private void updateRepositoriesList(ArrayList<RepositoryObject> repositories, int itemsPerPage) {

        if (repositories != null) {
            adapter.updateDataSet(repositories);
            repositoriesList.setOnScrollListener(scrollListener);

            checkIfDataFullyLoaded(repositories, itemsPerPage);
            Toast.makeText(this, "Loading complete", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Notify user and remove scroll listener if no more data is available
     *
     * @param repositories current batch of repositories
     * @param itemsPerPage number of items per page
     */
    private void checkIfDataFullyLoaded(ArrayList<RepositoryObject> repositories, int itemsPerPage) {

        if (repositories != null && (repositories.size() < itemsPerPage || repositories.size() == 0)) {

            Toast.makeText(this, "End of list reached", Toast.LENGTH_SHORT).show();
            repositoriesList.setOnScrollListener(null);
        }
    }

    /**
     * Check if additional repositories should be requested.
     * If so, load the next page data and disable scroll listener.
     *
     * @param lastIndex     the current last visible item index
     * @param totalCount    total number of items in data set
     * @param autoloadLimit start loading next page when this index is reached by scrolling
     */
    private void checkForListUpdates(int lastIndex, int totalCount, int autoloadLimit) {

        if (lastIndex >= totalCount - autoloadLimit) {
            Toast.makeText(this, "Loading page " + currentPage, Toast.LENGTH_SHORT).show();
            repositoriesList.setOnScrollListener(null);
            loadRepositories(currentPage);
        }
    }
}
