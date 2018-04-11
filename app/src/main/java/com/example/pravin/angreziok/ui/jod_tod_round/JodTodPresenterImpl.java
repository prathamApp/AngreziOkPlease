package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Context;

import com.example.pravin.angreziok.services.TTSService;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTodPresenterImpl implements JodTodContract.JodTodPresenter {

    Context mContext;
    JodTodContract.JodTod_G3_L2_View jodTodG3L2View;
    public TTSService ttsService;

    public JodTodPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G3_L2_View jodTod_g3_l2_view,TTSService ttsService){
        mContext = context;
        this.jodTodG3L2View = jodTod_g3_l2_view;
        this.ttsService= ttsService;
    }

    @Override
    public void startTTS(String text) {
        ttsService.play(text);
    }
}
