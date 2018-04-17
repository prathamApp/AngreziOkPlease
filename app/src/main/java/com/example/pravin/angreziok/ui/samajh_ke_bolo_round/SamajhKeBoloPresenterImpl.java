package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

import android.content.Context;
import com.example.pravin.angreziok.interfaces.MediaCallbacks;
import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.example.pravin.angreziok.services.TTSService;
import com.example.pravin.angreziok.util.MediaPlayerUtil;
import java.util.List;


public class SamajhKeBoloPresenterImpl implements SamajhKeBoloContract.SamajhKeBoloPresenter, MediaCallbacks {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TTSService ttsService;
    Context mContext;
    MediaPlayerUtil mediaPlayerUtil;
    SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View;
    SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View;
    SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View;
    List<GenericModalGson> g1l1QuestionData, g2l2QuestionData, g1l2QuestionData, g3l2QuestionData;
    GenericModalGson whereWhenGameData, sayItGameData, askGameData;

    int readQuestionNo, randomNumber;
    String sdCardPathString;

    public SamajhKeBoloPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG1L2View = samajhKeBoloG1L2View;
        this.ttsService= ttsService;
    }

    SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View,
                              TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG2L2View = samajhKeBoloG2L2View;
        this.ttsService= ttsService;
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG3L2View = samajhKeBoloG3L2View;
        this.ttsService= ttsService;
    }


    @Override
    public void onComplete() {

    }
}
