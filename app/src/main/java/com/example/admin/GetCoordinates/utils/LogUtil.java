package com.example.admin.GetCoordinates.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Process;

import com.example.admin.GetCoordinates.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private File logFile;
    private Context context;
    public enum LogsType {d, e, i, v, w}
    private int pid;
    private long tid;

    public LogUtil(Context context) {
        this.context = context;
        initFile();
    }

    private void initFile() {
        String appDirectoryPath = Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name);
        File appDirectory = new File(appDirectoryPath);
        pid = Process.myPid();
        if (!appDirectory.exists()) {
            appDirectory.mkdir();
        }
        logFile = new File(appDirectoryPath + File.separator + "logs.txt");
        if (!logFile.exists()){
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendLog(LogsType type, String tag, String msg) {
        try {
            tid = Thread.currentThread().getId();

            String logType = returnLogTypeInString(type);

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM  hh:mm:ss");
            String stringDate = format.format(date);

            String packageName = getAppPackageName();

            StringBuilder stringLog = new StringBuilder();
            stringLog.append("\n")
                    .append(stringDate).append(" ")
                    .append(pid).append("-").append(tid).append(File.separator)
                    .append(packageName).append(" ")
                    .append(logType)
                    .append(tag).append(" ")
                    .append(msg);

            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.valueOf(stringLog));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAppPackageName() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            String packageName = packageInfo.packageName;
            return packageName;
        } else {
            return "Unknown package name";
        }
    }

    private String returnLogTypeInString(LogsType type){
        switch (type) {
            case d:
                return "D" + File.separator;
            case e:
                return "E" + File.separator;
            case i:
                return "I" + File.separator;
            case v:
                return "V" + File.separator;
            case w:
                return "W" + File.separator;
        }
        return null;
    }

}
