package com.example.pravin.angreziok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.services.STTService;
import com.example.pravin.angreziok.services.TTSService;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Pravin on 19/03/2018.
 */

public class BaseActivity extends AppCompatActivity {

    public static TTSService ttsService;
    public static STTService sttService;
    static ArrayList<PlayerModal> playerModalArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ttsService = new TTSService(getApplication());
        ttsService.setActivity(this);
        ttsService.setSpeechRate(0.7f);
        ttsService.setLanguage(new Locale("hi", "IN"));
        sttService = STTService.init(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("studentList", playerModalList);
        dataConfirmationIntent.putExtras(bundle);
        startActivity(dataConfirmationIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sttService.shutdown();
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
