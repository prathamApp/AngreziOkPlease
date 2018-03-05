package com.example.pravin.angreziok.contentplayer;

import android.content.Context;

public class SdCardPath {

    //    String sdCardPath,rootPath;
    static Context context;

    public SdCardPath(Context context) {
        this.context = context;
    }

    public static String getSdCardPath() {
        //Todo changes
        /*String sdCardPath;

        StatusDBHelper statusDBHelper = new StatusDBHelper(context);
        sdCardPath = statusDBHelper.getValue("SdCardPath");
        sdCardPath = sdCardPath + "/.KKSGames/";
        return sdCardPath;*/
        return "";
    }
}
