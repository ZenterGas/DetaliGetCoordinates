package com.example.admin.GetCoordinates.retrofit_api;

import com.example.admin.GetCoordinates.retrofit_api.response.Client;
import com.example.admin.GetCoordinates.retrofit_api.response.Driver;
import com.example.admin.GetCoordinates.retrofit_api.response.SuccessfulUpload;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitApi {

    @GET
    Call<ArrayList<Driver>> getDrivers(@Url String url, @Header("Authorization") String auth);

    @GET
    Call<ArrayList<Client>> getClient(@Url String url, @Header("Authorization") String auth);

    @POST("http://web.gaz.local/base_1/hs/ex/gps/")
    Call<String> setClientsCoordinates(@Header("Authorization") String auth, @Body ArrayList<Client> clients);

}
