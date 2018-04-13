package com.example.pravin.angreziok.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.custom.GifView;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G1_L2;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G2_L2;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G3_L2;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G1_L2;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G3_L2;
import com.example.pravin.angreziok.util.PD_Utility;
import com.example.pravin.angreziok.util.SDCardUtil;

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

    int frag = 0;
    String myFragment;

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
        myFragment = getArguments().getString("frag", "");

        switch (myFragment) {
            case "R1G1L2":
                instructionsTextView.setText("Picture Game");
                frag = 1;
                break;
            case "R1G2L2":
                instructionsTextView.setText("Action Game");
                frag = 2;
                break;
            case "R1G3L2":
                instructionsTextView.setText("Pairs Game");
                frag = 3;
                break;
            case "R2G1L2":
                instructionsTextView.setText("Alphabets Game");
                frag = 4;
                break;
            case "R2G2L2":
                instructionsTextView.setText("Rhyming Game");
                frag = 5;
                break;
            case "R2G3L2":
                instructionsTextView.setText("Listen and Spell Game");
                frag = 6;
                break;
            case "R3G1L2":
                instructionsTextView.setText("Where-When Game");
                frag = 7;
                break;
            case "R3G2L2":
                instructionsTextView.setText("Say-it Game");
                frag = 8;
                break;
            case "R3G3L2":
                instructionsTextView.setText("How to Ask Game");
                frag = 9;
        }
        try {
            InputStream gif = new FileInputStream(getSdcardPath() + "charactersGif/Balle-Balle.gif");
            charGif.setGifResource(gif);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_skip_instructions)
    public void gotoNext() {
        loadFragment(frag);
    }

    public void loadFragment(int no) {

        switch (no) {
            case 1:
                PD_Utility.showFragment(getActivity(), new BoleToh_G1_L2(), R.id.cl_bole_toh,
                        null, BoleToh_G1_L2.class.getSimpleName());
                break;
            case 2:
                PD_Utility.showFragment(getActivity(), new BoleToh_G2_L2(), R.id.cl_bole_toh,
                        null, BoleToh_G2_L2.class.getSimpleName());
                break;
            case 3:
                PD_Utility.showFragment(getActivity(), new BoleToh_G3_L2(), R.id.cl_bole_toh,
                        null, BoleToh_G3_L2.class.getSimpleName());
                break;
            case 4:
                PD_Utility.showFragment(getActivity(), new JodTod_G1_L2(), R.id.cl_jod_tod,
                        null, JodTod_G1_L2.class.getSimpleName());
                break;
            case 5:
//         TODO       PD_Utility.showFragment(getActivity(), new JodTod_G2_L2(), R.id.cl_jod_tod,
//                        null, JodTod_G2_L2.class.getSimpleName());
                break;
            case 6:
                PD_Utility.showFragment(getActivity(), new JodTod_G3_L2(), R.id.cl_jod_tod,
                        null, JodTod_G3_L2.class.getSimpleName());
                break;
            case 7:
//          TODO      PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G1_L2(), R.id.cl_samajh_ke_bolo,
//                        null, SamajhKeBolo_G1_L2.class.getSimpleName());
                break;
            case 8:
//          TODO      PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G2_L2(), R.id.cl_samajh_ke_bolo,
//                        null, SamajhKeBolo_G2_L2.class.getSimpleName());
                break;
            case 9:
//          TODO      PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G3_L2(), R.id.cl_samajh_ke_bolo,
//                        null, SamajhKeBolo_G3_L2.class.getSimpleName());
        }
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