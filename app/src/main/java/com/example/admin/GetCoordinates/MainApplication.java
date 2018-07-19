package com.example.admin.GetCoordinates;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.GetCoordinates.utils.LogUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static LogUtil logs;

    //git test
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("geoLocation.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        logs = new LogUtil(getApplicationContext());
        logs.appendLog(LogUtil.LogsType.i, "MainApplication: onCreate", "application start");
    }

}
