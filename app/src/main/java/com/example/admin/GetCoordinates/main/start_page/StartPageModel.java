package com.example.admin.GetCoordinates.main.start_page;

import android.content.res.Resources;

import com.example.admin.GetCoordinates.mvp.classes.BaseModel;
import com.example.admin.GetCoordinates.mvp.interfaces.PresenterInterface;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;

import io.realm.RealmResults;

public class StartPageModel extends BaseModel {

    private StartPagePresenter presenter;

    StartPageModel(Resources resources) {
        super(resources);
    }

    @Override
    public void applyPresenter(PresenterInterface presenter) {
        this.presenter = (StartPagePresenter) presenter;
    }

    public void getDeliveryList(){
        String driver = getDatabaseManager().getDriver();
        String data = getDatabaseManager().getData();
        RealmResults<RealmDeliverTarget> clients = getDatabaseManager().getClient();
        if (driver != null){
            presenter.setDeliveryList(data, driver, clients);
        } else {
            presenter.hideSwitch();
        }
    }

    public void setCoordinates(double latitude, double longitude, String code){
        getDatabaseManager().updateDataInDB(latitude, longitude, code);
    }

    public void cancelChange(String code){
        getDatabaseManager().cancelChange(code);
    }

}
