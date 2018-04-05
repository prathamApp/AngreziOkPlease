package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
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
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playerModalArrayList;


public class BoleTohRoundTwo extends BaseFragment implements BoleTohContract.BoleTohRoundTwoView, RecognitionListener {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.iv_character_dialog_gif)
    GifView gifView;
    @BindView(R.id.tv_r1g2_answer)
    TextView answer;
    @BindView(R.id.ll_r1g2_sttoptions)
    LinearLayout options;
    @BindView(R.id.ll_r1g2_allstar)
    LinearLayout allstarLayout;
    @BindView(R.id.ll_r1g2_megastar)
    LinearLayout megastarLayout;
    @BindView(R.id.ll_r1g2_rockstar)
    LinearLayout rockstarLayout;
    @BindView(R.id.ll_r1g2_superstar)
    LinearLayout superstarLayout;
    @BindView(R.id.r1g2_megastar)
    TextView megaScore;
    @BindView(R.id.r1g2_rockstar)
    TextView rockScore;
    @BindView(R.id.r1g2_superstar)
    TextView superScore;
    @BindView(R.id.r1g2_allstar)
    TextView allScore;

    @BindView(R.id.option1)
    TextView option1;
    @BindView(R.id.option2)
    TextView option2;
    @BindView(R.id.option3)
    TextView option3;
    @BindView(R.id.r1g2_sttOptions)
    LinearLayout optionsView;
    @BindView(R.id.konfettiView)
    KonfettiView konfettiView;

    String text;
    BoleTohContract.BoleTohPresenter presenter;
    private SpeechRecognizer speech = null;
    String language = "en-IN";
    int speechCount,currentTeam;
//    CustomCountDownTimer customCountDownTimer;

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
        setInitialScores();
        setDataForGame();
        speechCount = 0;
        initiateQuestion();
    }

    private void setInitialScores() {
        Log.d(":::", "setInitialScores: " + playerModalArrayList.toString());
        for (int i = 0; i < playerModalArrayList.size(); i++) {
            switch (playerModalArrayList.get(i).studentAlias) {
                case "Rockstars":
                    rockstarLayout.setVisibility(View.VISIBLE);
                    rockScore.setText(playerModalArrayList.get(i).studentScore);
                    break;
                case "Megastars":
                    megastarLayout.setVisibility(View.VISIBLE);
                    megaScore.setText(playerModalArrayList.get(i).studentScore);
                    break;
                case "Superstars":
                    superstarLayout.setVisibility(View.VISIBLE);
                    superScore.setText(playerModalArrayList.get(i).studentScore);
                    break;
                case "Allstars":
                    allstarLayout.setVisibility(View.VISIBLE);
                    allScore.setText(playerModalArrayList.get(i).studentScore);
            }
        }
    }

    @Override
    public void setCelebrationView() {
        konfettiView.setVisibility(View.VISIBLE);

        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1500L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(500, 1000L);
    }

    @Override
    public void initiateQuestion() {
        startTimer();
        playTTS();
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
        mCountDownTimer.ready();
        mCountDownTimer.start(15000);
        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                submitAns();
            }
        });
        BoleToh.animateView(mCountDownTimer, getActivity());
    }

    @OnClick(R.id.ib_r1g2_speaker)
    public void soundClicked() {
        presenter.startTTS(text);
    }

    @OnClick(R.id.ib_r1g2_mic)
    public void micClicked() {
        speechCount++;
        if (speechCount <= 2)
            startSTT();
        else
            Toast.makeText(getActivity(), "Can be used only 2 Times", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.option1, R.id.option2, R.id.option3})
    public void optionsClicked(View view) {
        // TTS for the options clicked
        TextView option = (TextView) view;
        answer.setText(option.getText() + "");
        presenter.startTTS(option.getText() + "");
    }

    @OnClick(R.id.iv_r1g2_submit_ans)
    public void submitAns() {
        mCountDownTimer.pause();
        presenter.checkFinalAnswer(answer.getText().toString());
    }

    public void startSTT() {
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speech.startListening(intent);
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
        Toast.makeText(getActivity(), "Spoken" + matches.get(0), Toast.LENGTH_SHORT).show();
        optionsView.setVisibility(View.VISIBLE);
        setAnswer(matches.get(0));
        presenter.r1g2_checkAnswer(matches.get(0));
    }

    @Override
    public void hideOptionView() {
        optionsView.setVisibility(View.GONE);
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
        options.setVisibility(View.GONE);
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