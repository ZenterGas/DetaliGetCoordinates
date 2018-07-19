package com.example.admin.GetCoordinates.main.upload_data_page;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.mvp.classes.BaseModel;
import com.example.admin.GetCoordinates.mvp.interfaces.PresenterInterface;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;
import com.example.admin.GetCoordinates.retrofit_api.response.SuccessfulUpload;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDataModel extends BaseModel {

    private UploadDataPresenter presenter;

    UploadDataModel(Resources resources) {
        super(resources);
    }

    @Override
    public void applyPresenter(PresenterInterface presenter) {
        this.presenter = (UploadDataPresenter) presenter;
    }

    public RealmResults<RealmDeliverTarget> getDataForUpload() {
        return getDatabaseManager().getDataForUpload();
    }

    public void uploadData(final ArrayList<Client> clientsData) {
        final String logginPass = "server:SERVER";
        final String baseAuth = "Basic " + Base64.encodeToString(logginPass.getBytes(), Base64.NO_WRAP);
        Call<String> call = getRetrofit().setClientsCoordinates(baseAuth, clientsData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String respond = response.body();
                if (respond != null) {
                    if (respond.equals("1")) {
                        presenter.showSuccessDialog("Выгрузка успешно завершена");
                    } else {
                        presenter.showErrorDialog(respond);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                if (t instanceof UnknownHostException) {
                    presenter.showErrorDialog(getResources().getString(R.string.connect_to_local));
                } else {
                    presenter.showErrorDialog("Не предвиденная ошибка! Обратитесь к програмистам");
                }
            }
        });
    }

}
