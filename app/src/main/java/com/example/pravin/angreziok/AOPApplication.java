package com.example.pravin.angreziok;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Pravin on 2 Feb 2018.
 */

public class AOPApplication extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static AOPApplication aopApplication;
    private static Application wholeApplication;
    static String ext_path;
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

    @Override
    public void onCreate() {
        super.onCreate();
        aopApplication = this;
        wholeApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
