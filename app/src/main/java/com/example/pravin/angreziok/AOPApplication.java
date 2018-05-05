package com.example.pravin.angreziok;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
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

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static AOPApplication aopApplication;
    static String ext_path;
    static Timer gpsTimer, gpsFixTimer;
    public static int gpsFixCount;
    private static final DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return dateTimeFormat.format(cal.getTime());
    }

    public String GetCurrentDateTime(boolean getSysTime) {
        if (getSysTime) {
            //
            Calendar cal = Calendar.getInstance();
            return dateFormat.format(cal.getTime());
        } else {
            //
            Log.d("GetCurrentDateTime ", "" + AOPApplication.getAccurateTimeStamp());
            return AOPApplication.getAccurateTimeStamp();
        }
    }

    public String GetCurrentDate() {
        Log.d("GetDate ", "" + AOPApplication.getAccurateDate());
        return AOPApplication.getAccurateDate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        aopApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static String getAccurateTimeStamp() {
        // String to Date
        StatusDBHelper statusDBHelper = new StatusDBHelper(mInstance);
        String gpsTime = statusDBHelper.getValue("GPSDateTime");
        Date gpsDateTime = null;
        try {
            gpsDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).parse(gpsTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Add Seconds to Gps Date Time
        Calendar addSec = Calendar.getInstance();
        addSec.setTime(gpsDateTime);
        addSec.add(addSec.SECOND, getTimerCount());
        String updatedTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH).format(addSec.getTime());

        return updatedTime;
    }

    public static String getAccurateDate() {
        // String to Date
        StatusDBHelper statusDBHelper = new StatusDBHelper(mInstance);
        String gpsTime = statusDBHelper.getValue("GPSDateTime");
        Date gpsDateTime = null;
        try {
            gpsDateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(gpsTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Add Seconds to Gps Date Time
        Calendar addSec = Calendar.getInstance();
        addSec.setTime(gpsDateTime);
        addSec.add(addSec.SECOND, getTimerCount());
        String updatedTime = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(addSec.getTime());

        return updatedTime;
    }

    public static void startGPSFixTimer() {
        gpsFixTimer = new Timer();
        gpsFixTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                gpsFixCount++;
            }
        }, 1000, 1000);
    }

    public static void resetGPSFixTimer() {
        if (gpsFixTimer != null) {
            gpsFixTimer.cancel();
            gpsFixCount = 0;
        } else {
            gpsFixCount = 00;
        }
    }

    public static int getGPSFixTimerCount() {
        return gpsFixCount;
    }

    public static UUID getUniqueID() {
        return UUID.randomUUID();
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

}
