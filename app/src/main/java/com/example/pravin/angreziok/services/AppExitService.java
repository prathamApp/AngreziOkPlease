package com.example.pravin.angreziok.services;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.pravin.angreziok.database.AppDatabase;

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

            appDatabase = Room.databaseBuilder(this,
                    AppDatabase.class, AppDatabase.DB_NAME)
                    .build();

            com.example.pravin.angreziok.domain.Status status;
            status = new com.example.pravin.angreziok.domain.Status();
            status.setStatusKey("CurrentSession");
            status.setValue("NA");
            appDatabase.getStatusDao().updateValue("CurrentSession",""+currentStatus);

/*            StatusDBHelper statusDBHelper = new StatusDBHelper(this);
            SessionDBHelper sessionDBHelper = new SessionDBHelper(this);

            String curSession = statusDBHelper.getValue("CurrentSession");
            String curStrSession = statusDBHelper.getValue("CurrentStorySession");
            sessionDBHelper.UpdateToDate(""+curSession, KksApplication.getCurrentDateTime());
            sessionDBHelper.UpdateToDate(""+curStrSession, KksApplication.getCurrentDateTime());
            BackupDatabase.backup(this);*/
            Log.d("AppExitService:", "onTaskRemoved: HAHAHAHAHAHAHAHAHAAHAHAAAAAA");
            stopSelf();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}