package com.example.admin.GetCoordinates.main.upload_data_page;

import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.mvp.classes.BasePresenter;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;

import java.util.ArrayList;

import io.realm.RealmResults;

class UploadDataPresenter<T extends UploadDataFragment, P extends UploadDataModel> extends BasePresenter<T, P> {

    private RealmResults<RealmDeliverTarget> dataForUpload;

    UploadDataPresenter(MainActivity activity) {
        super(activity);
    }

    public RealmResults<RealmDeliverTarget> getDataForUpload(){
        dataForUpload = getModel().getDataForUpload();
        return dataForUpload;
    }

    public void uploadDataToServer(){
        getModel().uploadData(createListForUpload());
    }

    private ArrayList<Client> createListForUpload(){
        ArrayList<Client> clientsData = new ArrayList<>();
        for (RealmDeliverTarget deliveryTarget : dataForUpload) {
            Client clientData = new Client();
            clientData.code = deliveryTarget.getCode();
            clientData.name = deliveryTarget.getName();
            clientData.latitude = deliveryTarget.getNewLatitude();
            clientData.longitude = deliveryTarget.getNewLongitude();
            clientsData.add(clientData);
        }
        return clientsData;
    }

    public void showSuccessDialog(String successful){
        getView().showDialogWithText(successful, false);
    }

    public void showErrorDialog(String error){
        getView().showDialogWithText(error, true);
    }

}
