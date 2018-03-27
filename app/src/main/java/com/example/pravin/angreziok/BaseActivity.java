package com.example.pravin.angreziok;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;

/**
 * Created by Pravin on 19/03/2018.
 */

public class BaseActivity extends AppCompatActivity {

    public static TextToSpeechCustom playTTS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playTTS = new TextToSpeechCustom(this, 1.0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
