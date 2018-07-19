package com.example.admin.GetCoordinates.mvp.classes;

import android.content.res.Resources;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.mvp.interfaces.ModelInterface;
import com.example.admin.GetCoordinates.realm.DatabaseManager;
import com.example.admin.GetCoordinates.retrofit_api.RetrofitApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class BaseModel implements ModelInterface {

    private final Resources resources;
    private DatabaseManager databaseManager;

    protected BaseModel (Resources resources){
        this.resources = resources;
        databaseManager = new DatabaseManager();
    }

    protected Resources getResources() {
        return resources;
    }

    protected RetrofitApi getRetrofit(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(RetrofitApi.class);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
