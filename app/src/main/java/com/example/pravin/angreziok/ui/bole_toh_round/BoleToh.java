package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView, MediaCallbacks {

    BoleTohContract.BoleTohPresenter presenter;
    static ArrayList<PlayerModal> playerModalArrayList;
    static int gameCounter = 0;
    String charIntroPath;
    MediaPlayerUtil mediaPlayerUtil;

    @BindView(R.id.round_intro_gifview)
    GifView introGifView;
    @BindView(R.id.skip_button_intro)
    Button btn_skip;
    static ArrayList<Integer> list = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");

        for (int i = 0; i < 3; i++)
            list.add(new Integer(i));
        Collections.shuffle(list);
        Log.d("games", "\n\n\nShuffle: \n\n\n");
        for (int i = 0; i < list.size(); i++)
            Log.d("games", "gamesNo: " + list.get(i));


        Collections.shuffle(playerModalArrayList);
        for (int i = 0; i < playerModalArrayList.size(); i++)
            Log.d("BoleTohTAG", "playerModalArrayList: " + playerModalArrayList.get(i).getStudentAlias());

        charIntroPath = PD_Utility.getExternalPath(this) + "charactersGif/";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: " + charIntroPath);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showGif(charIntroPath);
            }
        },500);
        presenter = new BoleTohPresenterImpl(this);
    }

    private void showGif(String charIntroPath) {
        try {
            InputStream gif = new FileInputStream(charIntroPath+"Bole-to-round-Intro.gif");
            introGifView.setGifResource(gif);
            playSound(charIntroPath);
        } catch (Exception e) {
            Log.e("Cant Play Video", e.getMessage());
            e.printStackTrace();
        }
    }

    private void playSound(String charIntroPath) {
        try {
            Log.d("SoundPth", "playMusic: " + charIntroPath + "Bole-to-round-Intro.wav");
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(this);
                mediaPlayerUtil.initCallback(BoleToh.this);
            }
            mediaPlayerUtil.playMedia(charIntroPath + "Bole-to-round-Intro.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @OnClick(R.id.skip_button_intro)
    public void startGame() {
        mediaPlayerUtil.pauseMedia();
        loadFragment();
    }

    @Override
    public void loadFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("round", "R1");
        bundle.putInt("count", list.get(gameCounter));
        PD_Utility.showFragment(BoleToh.this, new fragment_intro_character(), R.id.cl_bole_toh,
                bundle, fragment_intro_character.class.getSimpleName());
    }

    public static void animateView(View view, Context context) {
        Animation rubber = AnimationUtils.loadAnimation(context, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
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
}
