package com.example.admin.GetCoordinates.main.get_information_page;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.mvp.classes.BaseModel;
import com.example.admin.GetCoordinates.mvp.interfaces.PresenterInterface;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;
import com.example.admin.GetCoordinates.retrofit_api.response.Driver;
import com.example.admin.GetCoordinates.utils.LogUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.GetCoordinates.MainApplication.logs;

public class GetInformationModel extends BaseModel {

    private GetInformationPresenter presenter;

    GetInformationModel(Resources resources) {
        super(resources);
    }

    @Override
    public void applyPresenter(PresenterInterface presenter) {
        this.presenter = (GetInformationPresenter) presenter;
    }

    void getDrivers(final String date) {
        String logginPass = "server:SERVER";
        String baseAuth = "Basic " + Base64.encodeToString(logginPass.getBytes(), Base64.NO_WRAP);
        Call<ArrayList<Driver>> call = getRetrofit().getDrivers(date, baseAuth);
        call.enqueue(new Callback<ArrayList<Driver>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Driver>> call, @NonNull Response<ArrayList<Driver>> response) {
                if (response.body() != null){
                    presenter.showDriverList(response.body());
                } else {
                    presenter.showError(getResources().getString(R.string.unknown_error));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Driver>> call, @NonNull Throwable t) {
                presenter.showError(getResources().getString(R.string.connect_to_local));
                logs.appendLog(LogUtil.LogsType.w, "GetInformationModel", "getDrivers: error local connection");
            }
        });
    }

    void getClients(final String date, final String driverCode) {
        String logginPass = "server:SERVER";
        String baseAuth = "Basic " + Base64.encodeToString(logginPass.getBytes(), Base64.NO_WRAP);
        Call<ArrayList<Client>> call = getRetrofit().getClient(date + "/" + driverCode, baseAuth);
        call.enqueue(new Callback<ArrayList<Client>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Client>> call, @NonNull Response<ArrayList<Client>> response) {
                presenter.settingRecycle(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Client>> call, @NonNull Throwable t) {
                logs.appendLog(LogUtil.LogsType.w, "GetInformationModel", "getDrivers: error local connection");
            }
        });
    }

    void saveClient(String data, String driverName, ArrayList<Client> clients){
        getDatabaseManager().clearCurrentDeliveryList();
        getDatabaseManager().saveDeliveryList(data, driverName, clients);
    }

}
