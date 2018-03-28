package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.CustomCountDownTimer;
import com.example.pravin.angreziok.ui.GifView;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BoleTohRoundTwo extends BaseFragment implements BoleTohContract.BoleTohRoundTwoView, RecognitionListener {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.iv_character_dialog_gif)
    GifView gifView;

    String text;
    BoleTohContract.BoleTohPresenter presenter;
    private SpeechRecognizer speech = null;
    String language = "en-IN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bole_toh_round_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new BoleTohPresenterImpl(getActivity(), this, BoleToh.playtts);

        playTTS();
        startTimer();
        setActionGif();
    }

    private void playTTS() {
        text = "What is he doing?";
        presenter.startTTS(text);
    }

    private void startTimer() {
        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(mCountDownTimer, getActivity());
        customCountDownTimer.startTimer(30000);
        setTimerCallBack();
        BoleToh.animateView(mCountDownTimer, getActivity());
    }

    private void setActionGif() {
        gifView.setGifResource(R.drawable.anupam_well_done);
    }

    @OnClick(R.id.ib_r1g2_speaker)
    public void startTTS() {
        presenter.startTTS(text);
    }

    public void startSTT(Context context) {
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speech.startListening(intent);
    }

    private void setTimerCallBack() {
        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "finish", Toast.LENGTH_SHORT).show();
                mCountDownTimer.failure();
            }
        });
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.d(":::" + getActivity(), "onResults: " + matches.get(0));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}


