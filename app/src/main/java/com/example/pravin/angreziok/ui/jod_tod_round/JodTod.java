package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.util.PD_Utility;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTod extends BaseActivity implements JodTodContract.JodTodView {


    @BindView(R.id.round_intro_videoView)
    VideoView introVideo;
    @BindView(R.id.skip_button_intro)
    Button btn_skip;

    static ArrayList<PlayerModal> jodTodPlayerList;
    String videoPath;
    JodTodContract.JodTodPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jod_tod);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        jodTodPlayerList = extraBundle.getParcelableArrayList("playerModalArrayList");

        Collections.shuffle(jodTodPlayerList);
        for(int i=0; i<jodTodPlayerList.size(); i++)
            Log.d("JodTodTAG", "jodTodPlayerList: "+jodTodPlayerList.get(i).getStudentAlias());

        videoPath = PD_Utility.getExternalPath(this) + "Videos/Bole_to_round.mp4";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: "+videoPath);
        playVideo(Uri.parse(videoPath));
        presenter = new JodTodPresenterImpl(this);
        BoleToh.playtts = new TextToSpeechCustom(this, 1.0f);
    }

    private void playVideo(Uri videoPath) {
        try {
            introVideo.setVideoURI(videoPath);
            introVideo.start();
        } catch (Exception e) {
            Log.e("Cant Play Video", e.getMessage());
            e.printStackTrace();
        }
        introVideo.requestFocus();
    }

}
