package com.example.pravin.angreziok.ui.video_intro;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.dao.StatusDao;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Crl;
import com.example.pravin.angreziok.services.AppExitService;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;
import com.example.pravin.angreziok.util.PD_Utility;
import com.example.pravin.angreziok.util.SDCardUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.pravin.angreziok.database.AppDatabase.DB_NAME;

public class VideoIntro extends BaseActivity implements VideoIntroContract.VideoIntroView {

    @BindView(R.id.intro_videoView)
    VideoView videoView;

    String videoPath;
    public static AppDatabase appDatabase;
    VideoIntroContract.VideoIntroPresenter presenter;
    String appStartTime;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intro);
        ButterKnife.bind(this);
        appStartTime = AOPApplication.getCurrentDateTime(false, "");
        //The Android's default system path of your application database.
        createDataBase();


        // Reset Timer
        AOPApplication.resetTimer();
        AOPApplication.startTimer();

        presenter = new VideoIntroPresenterImpl(this, this, videoView);
        videoPath = PD_Utility.getExternalPath(this) + "Videos/intro.mp4";
        PD_Utility.showLog("ext_path::", videoPath);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter.playVideo(Uri.parse(videoPath));
        startService(new Intent(this, AppExitService.class));
    }

    private void addStartTime() {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    appDatabase = Room.databaseBuilder(VideoIntro.this,
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .build();

                    StatusDao statusDao = appDatabase.getStatusDao();
                    statusDao.updateValue("AppStartDateTime", appStartTime);
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
        BackupDatabase.backup(this);
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

        file = new File(Environment.getExternalStorageDirectory() + "/.AOPInternal/SelfUsageJsons");
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

    private void doInitialEntries() {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {

                    com.example.pravin.angreziok.domain.Status status;
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

                    status = new com.example.pravin.angreziok.domain.Status();
                    status.setStatusKey("AppStartDateTime");
                    status.setValue("NA");
                    appDatabase.getStatusDao().insert(status);

                    Crl crl1 = new Crl();
                    crl1.setCRLId("admin_crl");
                    crl1.setEmail("admin@admin");
                    crl1.setFirstName("Admin");
                    crl1.setLastName("Admin");
                    crl1.setMobile("0123456789");
                    crl1.setUserName("admin");
                    crl1.setPassword("pratham");
                    crl1.setState("NA");
                    crl1.setProgramId(2);
                    crl1.setNewCrl(true);
                    crl1.setCreatedBy("NA");

                    Crl crl2 = new Crl();
                    crl2.setCRLId("admin_crl");
                    crl2.setEmail("admin@admin");
                    crl2.setFirstName("Admin");
                    crl2.setLastName("Admin");
                    crl2.setMobile("0123456789");
                    crl2.setUserName("ll");
                    crl2.setPassword("ll");
                    crl2.setState("NA");
                    crl2.setProgramId(2);
                    crl2.setNewCrl(true);
                    crl2.setCreatedBy("NA");

                    appDatabase.getCrlDao().insertAll(crl1, crl2);

                    String sdCardPathString = null;
                    ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(VideoIntro.this);
                    for (String path : sdcard_path) {
                        if (new File(path + "/.AOP_External").exists()) {
                            sdCardPathString = path + "/.AOP_External/";
                            appDatabase.getStatusDao().updateValue("SdCardPath", "" + sdCardPathString);
                        }
                    }

                    BackupDatabase.backup(VideoIntro.this);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    addStartTime();
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDataBase() {
        try {
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                if (new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/angrezi_ok_please.db").exists()) {
                    copyDataBase();
                } else {
                    try {
                        appDatabase = Room.databaseBuilder(this,
                                AppDatabase.class, AppDatabase.DB_NAME)
                                .build();
                        doInitialEntries();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                addStartTime();
                Log.d("VidIntro", "createDataBase: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            File file = this.getDir("databases", Context.MODE_PRIVATE);
            String myPath = file.getAbsolutePath() + "/" + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */

    private void copyDataBase() throws IOException {
        try {
            //Open your local db as the input stream
            File file = this.getDir("databases", Context.MODE_PRIVATE);
            String myPath = file.getAbsolutePath() + "/" + DB_NAME;
            String toCopyPath = file.getAbsolutePath().replace("app_databases", "databases") + "/" + DB_NAME;
            SQLiteDatabase.openOrCreateDatabase(myPath, null);

            File input = new File(Environment.getExternalStorageDirectory().getPath() + "/angrezi_ok_please.db");
            InputStream myInput = new FileInputStream(input);
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(myPath);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

            addStartTime();

            Thread.sleep(500);
            copyFileUsingStream(new File(myPath), new File(toCopyPath));

            addStartTime();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }
}
