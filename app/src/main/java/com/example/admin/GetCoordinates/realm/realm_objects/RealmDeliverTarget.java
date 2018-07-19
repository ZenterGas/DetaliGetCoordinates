package com.example.admin.GetCoordinates.realm.realm_objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmDeliverTarget extends RealmObject {

    @PrimaryKey
    private String code;

    private String name;
    private double newLatitude;
    private double newLongitude;
    private double oldLatitude;
    private double oldLongitude;

    private boolean isModified;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNewLatitude() {
        return newLatitude;
    }

    public void setNewLatitude(double latitude) {
        newLatitude = latitude;
    }

    public double getNewLongitude() {
        return newLongitude;
    }

    public void setNewLongitude(double longitude) {
        newLongitude = longitude;
    }

    public double getOldLatitude() {
        return oldLatitude;
    }

    public void setOldLatitude(double oldLatitude) {
        this.oldLatitude = oldLatitude;
    }

    public double getOldLongitude() {
        return oldLongitude;
    }

    public void setOldLongitude(double oldLongitude) {
        this.oldLongitude = oldLongitude;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }
}
