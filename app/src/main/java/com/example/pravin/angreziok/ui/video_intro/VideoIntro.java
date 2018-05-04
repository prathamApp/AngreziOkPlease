package com.example.pravin.angreziok.ui.video_intro;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.VideoView;
import com.example.pravin.angreziok.domain.Status;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Session;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;
import com.example.pravin.angreziok.util.PD_Utility;
import com.example.pravin.angreziok.util.SDCardUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoIntro extends BaseActivity implements VideoIntroContract.VideoIntroView {

    @BindView(R.id.intro_videoView)
    VideoView videoView;

    String videoPath;
    private AppDatabase appDatabase;
    VideoIntroContract.VideoIntroPresenter presenter;
    String final_sd_path, sdCardPathString;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intro);
        ButterKnife.bind(this);
        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        presenter = new VideoIntroPresenterImpl(this, this, videoView);
        videoPath = PD_Utility.getExternalPath(this) + "Videos/intro.mp4";
        PD_Utility.showLog("ext_path::", videoPath);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter.playVideo(Uri.parse(videoPath));

        /*
            "DeviceID", Settings.Secure.getString(myContext.getContentResolver(), Settings.Secure.ANDROID_ID), Build.SERIAL);
            "CurrentSession", "", "");
            "SdCardPath", "NA", "");
            "AppLang", "NA", "");
            "insertedStudents", "N", "");
            "CurrentStorySession", "", "");
        */

        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Session startSesion = new Session();
                    String currentStatus = "" + UUID.randomUUID().toString();
                    startSesion.setSessionID("" + currentStatus);
                    startSesion.setFromDate(AOPApplication.getCurrentDateTime());
                    startSesion.setToDate("NA");
                    appDatabase.getSessionDao().insert(startSesion);

                    com.example.pravin.angreziok.domain.Status status;
                    String tempKey = appDatabase.getStatusDao().getKey("SdCardPath");
                    if (tempKey == null) {
                        status = new com.example.pravin.angreziok.domain.Status();
                        status.setStatusKey("DeviceID");
                        status.setValue("" + Settings.Secure.getString(VideoIntro.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                        status.setDescription("" + Build.SERIAL);
                        appDatabase.getStatusDao().insert(status);

                        status = new com.example.pravin.angreziok.domain.Status();
                        status.setStatusKey("CurrentSession");
                        status.setValue("NA");
                        appDatabase.getStatusDao().insert(status);

                        status = new com.example.pravin.angreziok.domain.Status();
                        status.setStatusKey("SdCardPath");
                        status.setValue("NA");
                        appDatabase.getStatusDao().insert(status);

                        status = new com.example.pravin.angreziok.domain.Status();
                        status.setStatusKey("AppLang");
                        status.setValue("NA");
                        appDatabase.getStatusDao().insert(status);

                    }

                    status = new com.example.pravin.angreziok.domain.Status();
                    status.setStatusKey("CurrentSession");
                    status.setValue("NA");
                    appDatabase.getStatusDao().updateValue("CurrentSession",""+currentStatus);

                    String sdCardPathString = null;
                    ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(VideoIntro.this);
                    for (String path : sdcard_path) {
                        if (new File(path + "/.AOP_External").exists()) {
                            sdCardPathString = path + "/.AOP_External/";
                            appDatabase.getStatusDao().updateValue("SdCardPath",""+sdCardPathString);
                        }
                    }

                    BackupDatabase.backup(VideoIntro.this);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    @OnClick(R.id.skip_button)
    public void skipVideo() {
        videoView.pause();
        videoView.stopPlayback();
        startActivity();
/*        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installIntent);*/
    }

    @Override
    public void startActivity() {

        File file = new File(Environment.getExternalStorageDirectory().toString() + "/.AOPInternal");
        if (!file.exists())
            file.mkdir();

        file = new File(Environment.getExternalStorageDirectory().toString() + "/.AOPInternal/UsageJsons");
        if (!file.exists())
            file.mkdir();

        file = new File(Environment.getExternalStorageDirectory().toString() + "/.AOPInternal/SelfUsageJsons");
        if (!file.exists())
            file.mkdir();

        file = new File(Environment.getExternalStorageDirectory().toString() + "/.AOPInternal/JsonsBackup");
        if (!file.exists())
            file.mkdir();

        startActivity(new Intent(this, QRActivity.class));
        finish();
    }

}
