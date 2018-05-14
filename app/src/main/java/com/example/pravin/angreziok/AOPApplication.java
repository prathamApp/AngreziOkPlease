package com.example.pravin.angreziok;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.ui.video_intro.VideoIntro;

import net.vrallev.android.cat.Cat;

import org.apache.commons.net.ftp.FTPClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Pravin on 2 Feb 2018.
 */

public class AOPApplication extends Application {

    public static String networkSSID = "PrathamHotSpot-" + Build.SERIAL;
    public static FTPClient ftpClient;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static AOPApplication aopApplication;
    private static AppDatabase appDatabase;
    static String ext_path;
    private static final DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
    public static String path;

    @Override
    public void onCreate() {
        super.onCreate();
        aopApplication = this;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        AOPApplication.aopApplication.path = path;
    }

    public static String getVersion() {
        Context context = AOPApplication.aopApplication;
        String packageName = context.getPackageName();
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Cat.e("Unable to find the name " + packageName + " in the package");
            return null;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static UUID getUniqueID() {
        return UUID.randomUUID();
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

   /* public static String getCurrentDateTime(boolean timerTime) {
        if (timerTime) {
            return AOPApplication.getAccurateTimeStamp();
        } else {
            Calendar cal = Calendar.getInstance();
            return dateTimeFormat.format(cal.getTime());
        }
    }*/

    public static String getCurrentDateTime(boolean timerTime, String appStartTime) {
        if (timerTime) {
            return AOPApplication.getAccurateTimeStamp(appStartTime);
        } else {
            Calendar cal = Calendar.getInstance();
            return dateTimeFormat.format(cal.getTime());
        }
    }

    public static AOPApplication getInstance() {
        return aopApplication;
    }

    public static String getExtPath() {
        return ext_path;
    }

    public static void setExtPath(String path) {
        ext_path = path;
    }

    public static int getRandomNumber(int min, int max) {
        return min + (new Random().nextInt(max));
    }

    static int count;
    static Timer timer;

    public static void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count++;
            }
        }, 1000, 1000);
    }

    public static void resetTimer() {
        if (timer != null) {
            timer.cancel();
            count = 0;
        } else {
            count = 00;
        }
    }

    public static int getTimerCount() {
        return count;
    }

    /*public static String getAccurateTimeStamp() {
        // String to Date
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String gpsTime = VideoIntro.appDatabase.getStatusDao().getValue("AppStartDateTime");
                    Date gpsDateTime = dateTimeFormat.parse(gpsTime);
                    // Add Seconds to Gps Date Time
                    Calendar addSec = Calendar.getInstance();
                    addSec.setTime(gpsDateTime);
                    addSec.add(addSec.SECOND, getTimerCount());
                    return dateTimeFormat.format(addSec.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();
        return null;
    }*/

    public static String getAccurateTimeStamp(String appStartTime) {
        try {
            Log.d("TAG:::", "getAccurateTimeStamp: "+appStartTime);
            Date gpsDateTime = dateTimeFormat.parse(appStartTime);
            // Add Seconds to Gps Date Time
            Calendar addSec = Calendar.getInstance();
            addSec.setTime(gpsDateTime);
            Log.d("doInBackground", "------------------------------------------------------------------------getTimerCount: "+getTimerCount());
            addSec.add(addSec.SECOND, getTimerCount());

            return dateTimeFormat.format(addSec.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAccurateDate() {
        // String to Date
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String gpsTime = appDatabase.getStatusDao().getValue("GPSDateTime");
                    Date gpsDateTime = null;
                    gpsDateTime = dateFormat.parse(gpsTime);
                    // Add Seconds to Gps Date Time
                    Calendar addSec = Calendar.getInstance();
                    addSec.setTime(gpsDateTime);
                    addSec.add(addSec.SECOND, getTimerCount());
                    String updatedTime = dateFormat.format(addSec.getTime());
                    return updatedTime;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }.execute();
        return null;
    }

}
