package com.example.pravin.angreziok.ui.bole_toh_round;

import android.os.Bundle;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.util.PD_Utility;

import butterknife.ButterKnife;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView {

    BoleTohContract.BoleTohPresenter presenter;
    public static TextToSpeechCustom playTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        presenter = new BoleTohPresenterImpl(this);
        playTTS = new TextToSpeechCustom(this, 1.0f);
        //playTTS.ttsFunction("Hello all");
        loadFragment(1);
    }

    @Override
    public void loadFragment(int no) {
//        Bundle bundle = new Bundle();
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
}
