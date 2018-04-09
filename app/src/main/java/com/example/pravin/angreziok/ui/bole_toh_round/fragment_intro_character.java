package com.example.pravin.angreziok.ui.bole_toh_round;

import android.app.Dialog;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.SDCardUtil;
import com.example.pravin.angreziok.ui.GifView;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleTohContract;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G1_L2;
import com.example.pravin.angreziok.util.PD_Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class fragment_intro_character extends BaseFragment {

    @BindView(R.id.iv_char_gif)
    GifView charGif;
    @BindView(R.id.tv_instructions)
    TextView instructionsTextView;
    @BindView(R.id.btn_skip_instructions)
    Button buttontSkip;

    String text;
    BoleTohContract.BoleTohPresenter presenter;
    private SpeechRecognizer speech = null;
    String language = "en-IN";
    int speechCount, currentTeam;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_character, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        InputStream gif = null;
        try {
            gif = new FileInputStream(getSdcardPath() + "charactersGif/Balle-Balle.gif");
            charGif.setGifResource(gif);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_skip_instructions)
    public void gotoNext() {
        //buttontSkip;
        loadFragment(1);
    }

    public void loadFragment(int no) {
        PD_Utility.showFragment(getActivity(), new BoleToh_G1_L2(), R.id.cl_bole_toh,
                null, BoleToh_G1_L2.class.getSimpleName());
    }

    public String getSdcardPath() {
        String sdCardPathString = null;
        ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(getActivity());
        for (String path : sdcard_path) {
            if (new File(path + "/.AOP_External").exists()) {
                sdCardPathString = path + "/.AOP_External/";
            }
        }
        return sdCardPathString;
    }


}