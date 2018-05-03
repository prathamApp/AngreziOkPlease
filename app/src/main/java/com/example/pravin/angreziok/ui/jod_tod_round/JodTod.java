package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.custom.GifView;
import com.example.pravin.angreziok.interfaces.MediaCallbacks;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.util.MediaPlayerUtil;
import com.example.pravin.angreziok.util.PD_Utility;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTod extends BaseActivity implements JodTodContract.JodTodView, MediaCallbacks {

    @BindView(R.id.skip_button_intro)
    Button btn_skip;

    static ArrayList<PlayerModal> jodTodPlayerList;
    String charIntroPath;
    MediaPlayerUtil mediaPlayerUtil;

    @BindView(R.id.round_intro_gifview)
    GifView introGifView;
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
        for (int i = 0; i < list.size(); i++)
            Log.d("games", "gamesNo: " + list.get(i));


        Collections.shuffle(jodTodPlayerList);
        for (int i = 0; i < jodTodPlayerList.size(); i++)
            Log.d("JodTodTAG", "jodTodPlayerList: " + jodTodPlayerList.get(i).getStudentAlias());

        charIntroPath = PD_Utility.getExternalPath(this) + "charactersGif/";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: " + charIntroPath);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showGif(charIntroPath);
            }
        }, 500);
        presenter = new JodTodPresenterImpl(this);
    }

    private void showGif(String charIntroPath) {
        try {
            InputStream gif = new FileInputStream(charIntroPath + "Jod_Tod_Round-Intro.gif");
            introGifView.setGifResource(gif);
            playSound(charIntroPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(String charIntroPath) {
        try {
            Log.d("SoundPth", "playMusic: " + charIntroPath + "Jod_Tod_Round-Intro.wav");
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(this);
                mediaPlayerUtil.initCallback(JodTod.this);
            }
            mediaPlayerUtil.playMedia(charIntroPath + "Jod_Tod_Round-Intro.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void onComplete() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFragment();
            }
        },1000);
    }

    @Override
    public void onBackPressed() {
        BaseActivity.playerModalArrayList = this.jodTodPlayerList;
        super.onBackPressed();
    }
}
