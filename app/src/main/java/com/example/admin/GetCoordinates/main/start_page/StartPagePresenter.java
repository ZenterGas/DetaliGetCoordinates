package com.example.admin.GetCoordinates.main.start_page;

import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.mvp.classes.BasePresenter;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;

import io.realm.RealmResults;

class StartPagePresenter <T extends StartPageFragment, P extends StartPageModel> extends BasePresenter<T, P> {

    StartPagePresenter(MainActivity activity) {
        super(activity);
    }

    public void getDeliveryList(){
        getModel().getDeliveryList();
    }

    public void setDeliveryList(String data, String driver, RealmResults<RealmDeliverTarget> clients){
        getView().setDeliveryList(data, driver, clients);
    }

    public void setCoordinates(double latitude, double longitude, String code){
        getModel().setCoordinates(latitude, longitude, code);
    }

    public void cancelChange(String code){
        getModel().cancelChange(code);
    }

    public void hideSwitch(){
        getView().hideSwitch();
    }

}
