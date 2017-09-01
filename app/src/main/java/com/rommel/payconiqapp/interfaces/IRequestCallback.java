package com.rommel.payconiqapp.interfaces;

/**
 * Interface for handling successful requests.
 */
public interface IRequestCallback<T> {

    void executeCallback(T jsonArray);
}
