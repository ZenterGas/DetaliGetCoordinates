package com.example.admin.GetCoordinates.main.get_information_page;

import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.mvp.classes.BasePresenter;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;
import com.example.admin.GetCoordinates.retrofit_api.response.Driver;

import java.util.ArrayList;

class GetInformationPresenter <T extends GetInformationFragment, P extends GetInformationModel> extends BasePresenter<T, P> {

    GetInformationPresenter(MainActivity activity){
        super(activity);
    }

    public void getDrivers(String data){
        getModel().getDrivers(data);
    }

    public void getClients(String date, String driverCode){
        getModel().getClients(date, driverCode);
    }

    public void showDriverList(ArrayList<Driver> drivers){
        getView().showDriverList(drivers);
    }

    public void showError(String textError){
        getView().showErrorDialog(textError);
    }

    public void settingRecycle(ArrayList<Client> clients){
        getView().settingRecycle(clients);
    }

    public void saveClients(String data, String driverName, ArrayList<Client> clients) {
        getModel().saveClient(data, driverName, clients);
    }

}
