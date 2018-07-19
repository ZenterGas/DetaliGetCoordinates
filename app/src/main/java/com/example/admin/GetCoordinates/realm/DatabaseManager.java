package com.example.admin.GetCoordinates.realm;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDriver;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseManager {

    public void saveDeliveryList(final String data, final String driver, final ArrayList<Client> clients){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmDriver deliveryList = realm.createObject(RealmDriver.class);
                deliveryList.setDriver(driver);
                deliveryList.setData(data);
                for (Client client : clients){
                    RealmDeliverTarget realmClient = realm.createObject(RealmDeliverTarget.class, client.code);
                    realmClient.setName(client.name);
                    realmClient.setNewLatitude(client.latitude);
                    realmClient.setNewLongitude(client.longitude);
                    realmClient.setOldLatitude(client.latitude);
                    realmClient.setOldLongitude(client.longitude);
                    realmClient.setModified(false);
                }
            }
        });
    }

    public void clearCurrentDeliveryList(){
        Realm realm = Realm.getDefaultInstance();
        RealmDriver deliveryList = realm.where(RealmDriver.class).findFirst();
        RealmResults<RealmDeliverTarget> clients = realm.where(RealmDeliverTarget.class).findAll();
        if (deliveryList != null){
            realm.beginTransaction();
            clients.deleteAllFromRealm();
            deliveryList.deleteFromRealm();
            realm.commitTransaction();
        }
    }

    public String getDriver(){
        RealmDriver deliveryList = Realm.getDefaultInstance().where(RealmDriver.class).findFirst();
        if (deliveryList != null){
            return deliveryList.getDriver();
        } else {
            return null;
        }
    }

    public String getData(){
        RealmDriver deliveryList = Realm.getDefaultInstance().where(RealmDriver.class).findFirst();
        if (deliveryList != null){
            return deliveryList.getData();
        } else {
            return null;
        }
    }

    public RealmResults<RealmDeliverTarget> getClient(){
        return Realm.getDefaultInstance().where(RealmDeliverTarget.class).sort("name").findAll();
    }

    public void updateDataInDB(double latitude, double longitude, String code) {
        Realm realm = Realm.getDefaultInstance();
        RealmDeliverTarget updatedItem = realm.where(RealmDeliverTarget.class).sort("name").equalTo("code", code).findFirst();
        realm.beginTransaction();
        updatedItem.setNewLatitude(latitude);
        updatedItem.setNewLongitude(longitude);
        updatedItem.setModified(true);
        realm.commitTransaction();
    }

    public void cancelChange(String code){
        Realm realm = Realm.getDefaultInstance();
        RealmDeliverTarget updatedItem = realm.where(RealmDeliverTarget.class).sort("name").equalTo("code", code).findFirst();
        realm.beginTransaction();
        updatedItem.setNewLatitude(updatedItem.getOldLatitude());
        updatedItem.setNewLongitude(updatedItem.getOldLongitude());
        updatedItem.setModified(false);
        realm.commitTransaction();
    }

    public RealmResults<RealmDeliverTarget> getDataForUpload(){
        return Realm.getDefaultInstance().where(RealmDeliverTarget.class).sort("name").equalTo("isModified", true).findAll();
    }

}
