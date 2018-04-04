package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.util.PD_Utility;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView {

    BoleTohContract.BoleTohPresenter presenter;
    public static TextToSpeechCustom playtts;
    static ArrayList<PlayerModal> playerModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");

        presenter = new BoleTohPresenterImpl(this);
        playtts = new TextToSpeechCustom(this, 1.0f);
        loadFragment(1);
    }

    @Override
    public void loadFragment(int no) {
        PD_Utility.showFragment(BoleToh.this, new BoleTohRoundOne(), R.id.cl_bole_toh,
                null, BoleTohRoundOne.class.getSimpleName());
    }

    @Override
    public void onBackPressed() {
        BoleTohRoundOne BoleTohRoundFragmen = (BoleTohRoundOne) getSupportFragmentManager().findFragmentByTag(BoleTohRoundOne.class.getSimpleName());

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
