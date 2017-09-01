package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.adapters.RepositoriesAdapter;
import com.rommel.payconiqapp.dto.RepositoryDTO;
import com.rommel.payconiqapp.interfaces.IRequestCallback;
import com.rommel.payconiqapp.util.RequestUtil;
import com.rommel.payconiqapp.util.SettingsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The activity the user is redirected after initialization, if an Internet connection is available.
 */
public class OnlineActivity extends Activity {

    private static String LOG_TAG = OnlineActivity.class.getName();

    private int currentPage = 1;

    private ListView repositoresList;

    private RepositoriesAdapter adapter = new RepositoriesAdapter(this, R.layout.item_repository, new ArrayList<RepositoryDTO>());

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

            checkForListUpdates(repositoresList.getLastVisiblePosition(), totalCount, SettingsUtil.AUTOLOAD_LIMIT);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online);

        initUI();
    }

    /**
     * Get repository list instance and add scroll listener.
     */
    private void initUI() {

        repositoresList = (ListView) findViewById(R.id.repositories_list);
        repositoresList.setAdapter(adapter);

        loadRepositories(currentPage);
    }

    /**
     * Load the repository data for the current page and run a callback on completion.
     *
     * @param currentPage the current page index
     */
    private void loadRepositories(int currentPage) {

        String url = getURLWithParams(SettingsUtil.REPOS_URL, currentPage, SettingsUtil.ITEMS_PER_PAGE);
        RequestUtil.performRequest(url, new IRequestCallback<JSONArray>() {

            @Override
            public void executeCallback(JSONArray jsonArray) {

                parseAndUpdateRepositories(jsonArray, SettingsUtil.ITEMS_PER_PAGE);
            }
        }, this);
    }

    /**
     * Convert JSONArray object to RepositoryDTO list.
     * Display list.
     *
     * @param jsonArray the JSON data representing repositories
     */
    private void parseAndUpdateRepositories(JSONArray jsonArray, int itemsPerPage) {

        ArrayList<RepositoryDTO> repositories = null;

        try {
            repositories = parseRepositories(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateRepositoriesList(repositories, itemsPerPage);
    }

    /**
     * Get the URL for loading the current page data
     *
     * @param currentPage the number of the current page
     * @return a string representing the URL with proper parameters
     */
    private String getURLWithParams(String url, int currentPage, int itemsPerPage) {

        return url + "page=" + currentPage + "&per_page=" + itemsPerPage;
    }

    /**
     * Parse a JSON object data set to a list of RepositoryDTO objects
     *
     * @param data the JSON objects array
     * @return ArrayList containing RepositoryDTO objects
     * @throws JSONException
     */
    private ArrayList<RepositoryDTO> parseRepositories(JSONArray data) throws JSONException {

        ArrayList<RepositoryDTO> result = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            RepositoryDTO repository = getRepositoryObjectFromJSON(data.getJSONObject(i));
            result.add(repository);
        }

        return result;
    }

    /**
     * Convert JSON object to RepositoryDTO
     *
     * @param data JSON data to parse
     * @return RepositoryDTO object
     * @throws JSONException
     */
    private RepositoryDTO getRepositoryObjectFromJSON(JSONObject data) throws JSONException {

        RepositoryDTO result;

        result = new RepositoryDTO(
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
     */
    private void updateRepositoriesList(ArrayList<RepositoryDTO> repositories, int itemsPerPage) {

        if (repositories != null) {
            adapter.updateDataSet(repositories);
            repositoresList.setOnScrollListener(scrollListener);

            checkIfDataFullyLoaded(repositories, itemsPerPage);
        }
    }

    private void checkIfDataFullyLoaded(ArrayList<RepositoryDTO> repositories, int itemsPerPage) {

        if (repositories != null && (repositories.size() < itemsPerPage || repositories.size() == 0)) {

            Toast.makeText(this, "End of list reached", Toast.LENGTH_SHORT).show();
            repositoresList.setOnScrollListener(null);
        }
    }

    /**
     * Check if additional repositories should be requested.
     * If so, load the next page data and disable scroll listener.
     *
     * @param lastIndex  the current last visible item index
     * @param totalCount total number of items in data set
     */
    private void checkForListUpdates(int lastIndex, int totalCount, int autoloadLimit) {

        String logMessage = "NOT loading additional items.";

        if (lastIndex >= totalCount - autoloadLimit) {
            logMessage = "Load additional items.";
            Toast.makeText(this, "Loading page " + (++currentPage), Toast.LENGTH_SHORT).show();
            repositoresList.setOnScrollListener(null);
            loadRepositories(currentPage);
        }

        Log.d(LOG_TAG, logMessage);
    }
}
