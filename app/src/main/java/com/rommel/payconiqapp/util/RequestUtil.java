package com.rommel.payconiqapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.rommel.payconiqapp.interfaces.IRequestCallback;

import org.json.JSONArray;

/**
 * Helper class used to encapsulate network functionality.
 */
public class RequestUtil {

    private static String LOG_TAG = RequestUtil.class.getName();

    /**
     * Check for an available Internet connection
     * @return true if available
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        return false;
    }

    /**
     * Perform GET request
     * @param url the URL to call
     * @param callback the handler to be executed on success
     * @param requestQueue RequestQueue instance to add request to
     */
    public static void performRequest(final String url, RequestQueue requestQueue, final IRequestCallback<JSONArray> callback) {

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

    /**
     * Create queue for sending request
     * @param context context for initializing Volley
     * @return a RequestQueue object
     */
    public static RequestQueue getNewRequestQueue(Context context) {

        return Volley.newRequestQueue(context);
    }
}
