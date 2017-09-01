package com.rommel.payconiqapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.rommel.payconiqapp.R;
import com.rommel.payconiqapp.adapters.RepositoriesAdapter;
import com.rommel.payconiqapp.dto.RepositoryDTO;
import com.rommel.payconiqapp.interfaces.IRequestCallback;
import com.rommel.payconiqapp.util.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The activity the user is redirected after initialization, if an Internet connection is available.
 */
public class OnlineActivity extends Activity {

    private static String LOG_TAG = OnlineActivity.class.getName();
    private static String REPOS_URL = "https://api.github.com/users/JakeWharton/repos?";
    private static int ITEMS_PER_PAGE = 15;
    private static int AUTOLOAD_LIMIT = 3;
    private int currentPage = 0;

    private ListView repositoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_online);

        initUI();
        loadRepositories(currentPage);
    }

    /**
     * Get repository list instance and add scroll listener.
     */
    private void initUI() {

        repositoresList = (ListView) findViewById(R.id.repositories_list);
        repositoresList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstIndex, int visibleCount, int totalCount) {

                checkForListUpdates(repositoresList.getLastVisiblePosition(), totalCount);
            }
        });
    }

    /**
     * Load the repository data for the current page and run a callback on completion.
     * @param currentPage the current page index
     */
    private void loadRepositories(int currentPage) {

        RequestUtil.performRequest(getURLWithParams(currentPage), new IRequestCallback<JSONArray>() {

            @Override
            public void executeCallback(JSONArray jsonArray) {

                parseAndUpdateRepositories(jsonArray);
            }
        }, this);
    }

    /**
     * Convert JSONArray object to RepositoryDTO list.
     * Display said results.
     * @param jsonArray the JSON data representing repositories
     */
    private void parseAndUpdateRepositories(JSONArray jsonArray) {

        ArrayList<RepositoryDTO> repositories = null;

        try {
            repositories = parseRepositories(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateRepositoriesList(repositories);
    }

    /**
     * Get the URL for loading the current page data
     * @param currentPage the number of the current page
     * @return a string representing the URL with proper parameters
     */
    private String getURLWithParams(int currentPage) {

        return REPOS_URL + "page=" + currentPage + "&per_page=" + ITEMS_PER_PAGE;
    }

    /**
     * Parse a JSON object data set to a list of RepositoryDTO objects
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
     * Update current repositories data set
     * @param repositories repositories data set
     */
    private void updateRepositoriesList(ArrayList<RepositoryDTO> repositories) {

        if (repositories != null) {
            RepositoriesAdapter adapter = new RepositoriesAdapter(this, R.layout.item_repository, repositories);
            repositoresList.setAdapter(adapter);
        }
    }

    /**
     * Check if additional repositories should be requested.
     * @param lastIndex the current last visible item index
     * @param totalCount total number of items in data set
     */
    private void checkForListUpdates(int lastIndex, int totalCount) {

        String logMessage = "Scroll, but no load.";

        if (lastIndex >= totalCount - AUTOLOAD_LIMIT)
            logMessage = "Load additional items.";

        Log.d(LOG_TAG, logMessage);
    }
}
