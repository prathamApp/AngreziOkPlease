package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.CustomCountDownTimer;
import com.example.pravin.angreziok.ui.GifView;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BoleTohRoundTwo extends BaseFragment implements BoleTohContract.BoleTohRoundTwoView, RecognitionListener {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.iv_character_dialog_gif)
    GifView gifView;
    @BindView(R.id.tv_r1g2_answer)
    TextView answer;
    @BindView(R.id.ll_r1g2_sttoptions)
    LinearLayout options;
    @BindView(R.id.option1)
    TextView option1;
    @BindView(R.id.option2)
    TextView option2;
    @BindView(R.id.option3)
    TextView option3;
    @BindView(R.id.r1g2_sttOptions)
    LinearLayout optionsView;

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

        setDataForGame();
        playTTS();
        startTimer();
    }

    private void setDataForGame() {
        String path = presenter.getSdcardPath();
        presenter.setr1g2_data(path);
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

    @OnClick(R.id.ib_r1g2_speaker)
    public void soundClicked() {
        presenter.startTTS(text);
    }

    @OnClick(R.id.ib_r1g2_mic)
    public void micClicked() {
        startSTT();
    }

    @OnClick({R.id.option1, R.id.option2, R.id.option3})
    public void optionsClicked(View view){
            // TTS for the options clicked
        TextView option = (TextView)view;
        answer.setText(option.getText()+"");
        presenter.startTTS(option.getText()+"");
    }

    public void startSTT() {
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
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
        Toast.makeText(getActivity(), ""+ matches.get(0), Toast.LENGTH_SHORT).show();
        optionsView.setVisibility(View.VISIBLE);
        setAnswer(matches.get(0));
        presenter.r1g2_checkAnswer(matches.get(0));
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    @Override
    public void setActionGif(String path) {
        try {
            InputStream gif = new FileInputStream(path);
            gifView.setGifResource(gif);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAnswer(String ans) {
        answer.setText(ans);
    }

    @Override
    public void showOptions() {
        options.setVisibility(View.VISIBLE);
        String[] options = presenter.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
    }
}