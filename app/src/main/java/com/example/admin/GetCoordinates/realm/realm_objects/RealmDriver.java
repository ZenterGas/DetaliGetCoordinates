package com.example.admin.GetCoordinates.realm.realm_objects;

import io.realm.RealmObject;

public class RealmDriver extends RealmObject {

    private String driver;
    private String data;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
