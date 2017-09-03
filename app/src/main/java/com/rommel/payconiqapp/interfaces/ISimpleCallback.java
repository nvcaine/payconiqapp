package com.rommel.payconiqapp.interfaces;

/**
 * Interface for handling asynchronous operations.
 */
public interface ISimpleCallback<T> {

    void executeCallback(T data);
}
