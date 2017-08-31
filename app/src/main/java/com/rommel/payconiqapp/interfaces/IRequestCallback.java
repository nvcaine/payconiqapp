package com.rommel.payconiqapp.interfaces;

import org.json.JSONArray;

/**
 * Interface for handling successful requests.
 */
public interface IRequestCallback {

    void executeCallback(JSONArray jsonArray);
}
