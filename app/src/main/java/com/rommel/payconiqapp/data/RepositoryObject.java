package com.rommel.payconiqapp.data;

import io.realm.RealmObject;

/**
 * Used to represent a singe repository data object.
 */
public class RepositoryObject extends RealmObject {

    private String id;
    private String name;

    public RepositoryObject() {}

    public RepositoryObject(String id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
