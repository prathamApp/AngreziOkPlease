package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.SDCardUtil;
import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.example.pravin.angreziok.util.PD_Utility;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView {

    public static TextToSpeechCustom playTTS;
    static MediaPlayer mp;
    static Context AOPContext;
    Context c;
    static String final_sd_path, sdCardPathString;

    BoleToh(){
        c=BoleToh.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        getSupportActionBar().hide();
        playTTS = new TextToSpeechCustom(this, 1.0f);
        ButterKnife.bind(this);
        loadFragment(1);
        AOPContext = this;
        ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(this);
        for (String path : sdcard_path) {
            final_sd_path = path;
            if (new File(final_sd_path + "/.AOP_External").exists()) {
                sdCardPathString = path + "/.AOP_External/";
            }
        }
    }

    @Override
    public void loadFragment(int no) {
        Bundle bundle = new Bundle();
        PD_Utility.showFragment(BoleToh.this, new BoleTohRoundOne(), R.id.cl_bole_toh,
                bundle, BoleTohRoundOne.class.getSimpleName());
    }

    public static void playMusic(String fileName) {
        try {
            mp = new MediaPlayer();
//            mp.setDataSource(sdCardPathString + "/StoryData/" + fileName);
            mp.setDataSource("file:///android_asset/sounds/" + fileName);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        BoleTohRoundOne BoleTohRoundFragmen = (BoleTohRoundOne) getSupportFragmentManager().findFragmentByTag(BoleTohRoundOne.class.getSimpleName());

        getSupportFragmentManager().popBackStack();
        if (BoleTohRoundFragmen != null && BoleTohRoundFragmen.isVisible()) {
            super.onBackPressed();
        }
    }
}
