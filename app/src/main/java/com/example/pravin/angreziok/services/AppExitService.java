package com.example.pravin.angreziok.services;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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

        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {

                    appDatabase = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .build();

                    String curSession = appDatabase.getStatusDao().getValue("CurrentSession");
                    String toDateTemp = appDatabase.getSessionDao().getToDate(curSession);

                    Log.d("AppExitService:", "curSession : " + curSession + "      toDateTemp : " + toDateTemp);

                    if (toDateTemp.equalsIgnoreCase("na")) {
                        appDatabase.getSessionDao().UpdateToDate(curSession, AOPApplication.getCurrentDateTime());
                    }
/*            StatusDBHelper statusDBHelper = new StatusDBHelper(this);
            SessionDBHelper sessionDBHelper = new SessionDBHelper(this);

            String curSession = statusDBHelper.getValue("CurrentSession");
            String curStrSession = statusDBHelper.getValue("CurrentStorySession");
            sessionDBHelper.UpdateToDate(""+curSession, KksApplication.getCurrentDateTime());
            sessionDBHelper.UpdateToDate(""+curStrSession, KksApplication.getCurrentDateTime());
*/
                    BackupDatabase.backup(getApplicationContext());
                    Log.d("AppExitService:", "onTaskRemoved: HAHAHAHAHAHAHAHAHAAHAHAAAAAA");
                    stopSelf();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute()
    }
}