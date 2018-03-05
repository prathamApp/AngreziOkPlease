package com.example.pravin.angreziok.contentplayer;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

import static com.example.pravin.angreziok.contentplayer.TextToSpeechCustom.textToSpeech;


/**
 * Created by Pravin on 22 Feb 2018.
 */

// --- TEXT TO SPEECH ---

public class TtsListener implements TextToSpeech.OnInitListener {
    float spRate;
    public TtsListener(float spRate) {
        this.spRate = spRate;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(new Locale("hi", "IN"));
            textToSpeech.setSpeechRate(spRate);
        } else {
            textToSpeech = null;
            Toast.makeText(TextToSpeechCustom.mContext, "Failed to initialize TTS engine.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
