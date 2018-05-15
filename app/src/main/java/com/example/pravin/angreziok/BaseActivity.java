package com.example.pravin.angreziok;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.services.STTService;
import com.example.pravin.angreziok.services.TTSService;

import java.util.Locale;

/**
 * Created by Pravin on 19/03/2018.
 */

public class BaseActivity extends AppCompatActivity {

    public static TTSService ttsService;
    public static STTService sttService;

    static CountDownTimer cd;
    static Long timeout = (long) 20000 * 60;
    static Long duration = timeout;
    static Boolean setTimer = false;
    static String pauseTime;
    private AppDatabase appDatabase;
    String AppStartDateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsService = new TTSService(getApplication());
        ttsService.setActivity(this);
        ttsService.setSpeechRate(0.7f);
        ttsService.setLanguage(new Locale("hi", "IN"));
        sttService = STTService.init(this);
        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sttService.shutdown();
    }

    public void ActivityOnPause() {

        setTimer = true;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase = Room.databaseBuilder(BaseActivity.this,
                        AppDatabase.class, AppDatabase.DB_NAME)
                        .build();
                String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                if (AppStartDateTime == null)
                    pauseTime = AOPApplication.getCurrentDateTime(false,"");
                else
                    pauseTime = AOPApplication.getCurrentDateTime(true,AppStartDateTime);
                return null;
            }
        }.execute();
        Log.d("APP_END", "onFinish: Startd the App: " + duration);
        Log.d("APP_END", "onFinish: Startd the App: " + pauseTime);

        cd = new CountDownTimer(duration, 1000) {
            //cd = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                duration = millisUntilFinished;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onFinish() {
                new AsyncTask<Object, Void, Object>() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        try {

                            appDatabase = Room.databaseBuilder(BaseActivity.this,
                                    AppDatabase.class, AppDatabase.DB_NAME)
                                    .build();

                            String curSession = appDatabase.getStatusDao().getValue("CurrentSession");
                            String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                            String toDateTemp = appDatabase.getSessionDao().getToDate(curSession);

                            Log.d("AppExitService:", "curSession : " + curSession + "      toDateTemp : " + toDateTemp);

                            if (toDateTemp.equalsIgnoreCase("na")) {
                                appDatabase.getSessionDao().UpdateToDate(curSession, pauseTime);
                            }
                            BackupDatabase.backup(BaseActivity.this);
                            finishAffinity();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();


            }
        }.start();
    }

    public void ActivityResumed() {
        if (setTimer) {
            setTimer = false;
            cd.cancel();
            duration = timeout;
            Log.d("APP_END", "ActivityResumed: in IF: " + duration);
        }
        Log.d("APP_END", "ActivityResumed: duration: " + duration);

    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityResumed();
    }
}
