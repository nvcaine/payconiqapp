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
 * Created by Rommel on 8/29/2017.
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

        repositoresList = (ListView) findViewById(R.id.repositories_list);
        repositoresList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstIndex, int visibleCount, int totalCount) {
                int lastIndex = repositoresList.getLastVisiblePosition();

                if(lastIndex >= totalCount - AUTOLOAD_LIMIT)
                    Log.d(LOG_TAG, "Load additional items.");
                else
                    Log.d(LOG_TAG, "Scroll, but no load.");
            }
        });

        loadRepositories(currentPage);
    }

    private void loadRepositories(int currentPage) {

        RequestUtil.performRequest(getURLWithParams(currentPage), new IRequestCallback() {

            @Override
            public void executeCallback(JSONArray jsonArray) {

                ArrayList<RepositoryDTO> repositories = null;

                try {
                    repositories = parseRepositories(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                updateRepositoriesList(repositories);
            }
        }, this);
    }

    private String getURLWithParams(int currentPage) {

        return REPOS_URL + "page=" + currentPage + "&per_page=" + ITEMS_PER_PAGE;
    }

    private ArrayList<RepositoryDTO> parseRepositories(JSONArray data) throws JSONException {

        ArrayList<RepositoryDTO> result = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            RepositoryDTO repository = getRepositoryObjectFromJSON(data.getJSONObject(i));
            result.add(repository);
        }

        return result;
    }

    private RepositoryDTO getRepositoryObjectFromJSON(JSONObject data) throws JSONException {

        RepositoryDTO result;

        result = new RepositoryDTO(
                data.getString("id"),
                data.getString("name")
        );

        return result;
    }

    private void updateRepositoriesList(ArrayList<RepositoryDTO> repositories) {

        if (repositories != null) {
            RepositoriesAdapter adapter = new RepositoriesAdapter(this, R.layout.item_repository, repositories);
            repositoresList.setAdapter(adapter);
        }
    }
}
