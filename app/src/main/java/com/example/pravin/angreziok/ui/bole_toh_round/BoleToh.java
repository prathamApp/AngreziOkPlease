package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.SDCardUtil;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.GifView;
import com.example.pravin.angreziok.util.PD_Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView {

    BoleTohContract.BoleTohPresenter presenter;
    public static TextToSpeechCustom playtts;
    static ArrayList<PlayerModal> playerModalArrayList;

    @BindView(R.id.tv_instructions)
    TextView tv_instructions;
    @BindView(R.id.btn_skip_instructions)
    Button btn_skip;
    @BindView(R.id.ib_replay_instructions)
    ImageButton btn_replay;
    @BindView(R.id.iv_anupam_gif)
    GifView anupamGif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");

        Collections.shuffle(playerModalArrayList);
        for(int i=0; i<playerModalArrayList.size(); i++)
            Log.d("BoleTohTAG", "playerModalArrayList: "+playerModalArrayList.get(i).getStudentAlias());

        presenter = new BoleTohPresenterImpl(this);
        playtts = new TextToSpeechCustom(this, 1.0f);

        showInstructions();


        //loadFragment(1);
    }

    @OnClick(R.id.btn_skip_instructions)
    public void startGame(){
        loadFragment(1);
    }


    private void showInstructions() {

        InputStream gif = null;
        try {
            gif = new FileInputStream(getSdcardPath()+"charactersGif/Balle-Balle.gif");
            anupamGif.setGifResource(gif);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getSdcardPath() {
        String sdCardPathString = null;
        ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(BoleToh.this);
        for (String path : sdcard_path) {
            if (new File(path + "/.AOP_External").exists()) {
                sdCardPathString = path + "/.AOP_External/";
            }
        }
        return sdCardPathString;
    }

    @Override
    public void loadFragment(int no) {
        PD_Utility.showFragment(BoleToh.this, new BoleToh_G1_L1(), R.id.cl_bole_toh,
                null, BoleToh_G1_L1.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        BoleToh_G1_L1 BoleTohRoundFragmen = (BoleToh_G1_L1) getSupportFragmentManager().findFragmentByTag(BoleToh_G1_L1.class.getSimpleName());

        getSupportFragmentManager().popBackStack();
        if (BoleTohRoundFragmen != null && BoleTohRoundFragmen.isVisible()) {
            super.onBackPressed();
        }
    }

    public static void animateView(View view,Context context) {
        Animation rubber = AnimationUtils.loadAnimation(context, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }
}
