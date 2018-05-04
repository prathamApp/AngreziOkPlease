package com.example.pravin.angreziok.services;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;

public class AppExitService extends Service {

    private AppDatabase appDatabase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        try {

            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    try {

                        appDatabase = Room.databaseBuilder(AppExitService.this,
                                AppDatabase.class, AppDatabase.DB_NAME)
                                .build();

                        String curSession = appDatabase.getStatusDao().getValue("CurrentSession");
                        String toDateTemp = appDatabase.getSessionDao().getToDate(curSession);

                        if (toDateTemp.equalsIgnoreCase("na")) {
                            appDatabase.getSessionDao().UpdateToDate(curSession, AOPApplication.getCurrentDateTime());
                        }
                        BackupDatabase.backup(AppExitService.this);
                        stopSelf();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();

            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}