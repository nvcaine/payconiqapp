package com.rommel.payconiqapp.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.rommel.payconiqapp.interfaces.IRequestCallback;

import org.json.JSONArray;

/**
 * Helper class used to encapsulate request functionality from activity classes.
 */
public class RequestUtil {

    private static String LOG_TAG = RequestUtil.class.getName();

    private static RequestQueue requestQueue;

    /**
     * Perform GET request
     * @param url the URL to call
     * @param callback the handler to be executed on success
     * @param context context for initializing Volley
     */
    public static void performRequest(final String url, final IRequestCallback<JSONArray> callback, Context context) {

        requestQueue = Volley.newRequestQueue(context); // do not init every time

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                callback.executeCallback(jsonArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(LOG_TAG, "Error sending request to " + url + ": " + volleyError.getMessage());
            }
        });

        requestQueue.add(jsonRequest);
        Log.d(LOG_TAG, "Request sent to URL: " + url);
    }
}
