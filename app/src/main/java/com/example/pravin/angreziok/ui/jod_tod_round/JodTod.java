package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.VideoView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.util.PD_Utility;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTod extends BaseActivity implements JodTodContract.JodTodView, MediaPlayer.OnCompletionListener {

    @BindView(R.id.round_intro_videoView)
    VideoView introVideo;
    @BindView(R.id.skip_button_intro)
    Button btn_skip;

    static ArrayList<PlayerModal> jodTodPlayerList;
    String videoPath;
    JodTodContract.JodTodPresenter presenter;
    static ArrayList<Integer> list = new ArrayList<Integer>();
    static int gameCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jod_tod);
        ButterKnife.bind(this);
        gameCounter = 0;

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        jodTodPlayerList = extraBundle.getParcelableArrayList("playerModalArrayList");
        for (int i = 0; i < 3; i++)
            list.add(new Integer(i));
        Collections.shuffle(list);
        Log.d("games", "\n\n\nShuffle: \n\n\n");
        for(int i =0 ; i<list.size(); i++)
            Log.d("games", "gamesNo: "+list.get(i));


        Collections.shuffle(jodTodPlayerList);
        for (int i = 0; i < jodTodPlayerList.size(); i++)
            Log.d("JodTodTAG", "jodTodPlayerList: " + jodTodPlayerList.get(i).getStudentAlias());

        videoPath = PD_Utility.getExternalPath(this) + "Videos/Jod_Tod_Round.mp4";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: " + videoPath);
        playVideo(Uri.parse(videoPath));
        presenter = new JodTodPresenterImpl(this);
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


    @OnClick(R.id.skip_button_intro)
    public void startGame() {
        loadFragment();
    }

    public static void animateView(View view, Context context) {
        Animation rubber = AnimationUtils.loadAnimation(context, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    @Override
    public void loadFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("round", "R2");
        bundle.putInt("count", list.get(gameCounter));
        PD_Utility.showFragment(JodTod.this, new fragment_intro_character(), R.id.cl_jod_tod,
                bundle, fragment_intro_character.class.getSimpleName());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        loadFragment();
    }

}
