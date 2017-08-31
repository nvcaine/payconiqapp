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
 * Created by Rommel on 8/31/2017.
 */
public class RequestUtil {

    private static String LOG_TAG = RequestUtil.class.getName();

    private static RequestQueue requestQueue;

    public static void performRequest(final String url, final IRequestCallback callback, Context context) {

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
    }
}
