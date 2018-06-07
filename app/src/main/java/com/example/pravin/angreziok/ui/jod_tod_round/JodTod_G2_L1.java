package com.example.pravin.angreziok.ui.jod_tod_round;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.interfaces.SpeechResult;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.example.pravin.angreziok.BaseActivity.sttService;
import static com.example.pravin.angreziok.BaseActivity.ttsService;
import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.jodTodPlayerList;


public class JodTod_G2_L1 extends BaseFragment implements JodTodContract.JodTod_G2_L1_View, SpeechResult {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.ll_g2_l1_allstar)
    LinearLayout allstarLayout;
    @BindView(R.id.ll_g2_l1_megastar)
    LinearLayout megastarLayout;
    @BindView(R.id.ll_g2_l1_rockstar)
    LinearLayout rockstarLayout;
    @BindView(R.id.ll_g2_l1_superstar)
    LinearLayout superstarLayout;
    @BindView(R.id.q1)
    LinearLayout q1_layout;
    @BindView(R.id.q2)
    LinearLayout q2_layout;
    @BindView(R.id.g2_l1_megastar)
    TextView megaScore;
    @BindView(R.id.g2_l1_rockstar)
    TextView rockScore;
    @BindView(R.id.g2_l1_superstar)
    TextView superScore;
    @BindView(R.id.g2_l1_allstar)
    TextView allScore;
    @BindView(R.id.tv_question)
    TextView showQuestion;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.question1)
    TextView question1;
    @BindView(R.id.ans1)
    TextView ans1;
    @BindView(R.id.question2)
    TextView question2;
    @BindView(R.id.ans2)
    TextView ans2;
    @BindView(R.id.iv_mike1)
    ImageView mike1;
    @BindView(R.id.iv_mike2)
    ImageView mike2;
    @BindView(R.id.konfettiView_g2_l1)
    KonfettiView konfettiView;
    @BindView(R.id.tv_game_title)
    TextView gameTitle;

    String text;
    JodTodContract.JodTodPresenter presenter;
    int currentTeam, score = 0, timeOfTimer = 20000, clickedMike;
    float totalAnsCounter = 0f, correctAnsCounter = 0f;
    Dialog dialog;
    boolean timerEnd = false, firstAns, secondAns;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jod_tod_g2_l1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new JodTodPresenterImpl(getActivity(), this, ttsService);
        presenter.set_g2_data(2);
        setInitialScores();
        setDataForGame();
        currentTeam = 0;
        totalAnsCounter = 0;
        correctAnsCounter = 0;
        showDialog();
    }

    private void setInitialScores() {
        Log.d(":::", "setInitialScores: " + jodTodPlayerList.toString());
        for (int i = 0; i < jodTodPlayerList.size(); i++) {
            switch (jodTodPlayerList.get(i).studentAlias) {
                case "Rockstars":
                    rockstarLayout.setVisibility(View.VISIBLE);
                    rockScore.setText(jodTodPlayerList.get(i).studentScore);
                    break;
                case "Megastars":
                    megastarLayout.setVisibility(View.VISIBLE);
                    megaScore.setText(jodTodPlayerList.get(i).studentScore);
                    break;
                case "Superstars":
                    superstarLayout.setVisibility(View.VISIBLE);
                    superScore.setText(jodTodPlayerList.get(i).studentScore);
                    break;
                case "Allstars":
                    allstarLayout.setVisibility(View.VISIBLE);
                    allScore.setText(jodTodPlayerList.get(i).studentScore);
            }
        }
    }

    private View getCurrentView() {
        View view = null;

        switch (jodTodPlayerList.get(currentTeam).studentAlias) {
            case "Rockstars":
                view = rockstarLayout;
                break;
            case "Megastars":
                view = megastarLayout;
                break;
            case "Superstars":
                view = superstarLayout;
                break;
            case "Allstars":
                view = allstarLayout;
        }
        return view;
    }

    public void showDialog() {
        fadeOtherGroups();
        String teamName = jodTodPlayerList.get(currentTeam).getStudentAlias();
        final String studentID = jodTodPlayerList.get(currentTeam).getStudentID();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.dialog_tv_student_name);
        Button button = dialog.findViewById(R.id.dialog_btn_scan_qr);
        text.setText("Next question would be for " + teamName);
        button.setText("Ready ??");
        timerEnd = false;

        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String question = presenter.g2_l1_getQuestionText(studentID);
                String[] wordsToSay = presenter.getWordsToSay();
                initiateQuestion(question, wordsToSay);
            }
        });
    }

    private void fadeOtherGroups() {
        megastarLayout.setBackgroundResource(R.drawable.team_faded);
        rockstarLayout.setBackgroundResource(R.drawable.team_faded);
        allstarLayout.setBackgroundResource(R.drawable.team_faded);
        superstarLayout.setBackgroundResource(R.drawable.team_faded);

        switch (jodTodPlayerList.get(currentTeam).getStudentAlias()) {
            case "Megastars":
                megastarLayout.setBackgroundResource(R.drawable.team_one);
                break;
            case "Rockstars":
                rockstarLayout.setBackgroundResource(R.drawable.team_two);
                break;
            case "Superstars":
                superstarLayout.setBackgroundResource(R.drawable.team_three);
                break;
            case "Allstars":
                allstarLayout.setBackgroundResource(R.drawable.team_four);
        }
    }

    public void bounceView(View view) {
        Animation rubber = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    private void setDataForGame() {
        showQuestion.setText("Say these Words");
    }

    private void playTTS() {
        presenter.startTTS(text);
    }

    private void startTimer() {
        mCountDownTimer.ready();
        mCountDownTimer.start(timeOfTimer);
        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                timerEnd = true;
                submitAns();
            }
        });
        JodTod.animateView(mCountDownTimer, getActivity());
    }

    private void reinitializeOptions() {
        ((View)question1.getParent()).setBackgroundResource(0);
        ((View)question2.getParent()).setBackgroundResource(0);
        ((View)ans1.getParent()).setBackgroundResource(0);
        ((View)ans2.getParent()).setBackgroundResource(0);
    }

    public void submitAns() {
        mCountDownTimer.pause();
        boolean correct = false;
        score = 0;
        if (firstAns) {
            score = 10;
            correct = true;
        }
        if (secondAns) {
            score = 15;
            correct = true;
        }

        presenter.checkFinalAnswer_g2_l1(correct, score, currentTeam);

        currentTeam += 1;
        if (currentTeam < jodTodPlayerList.size()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    score = 0;
                    firstAns = secondAns = false;
                    showDialog();
                }
            }, 2500);
        } else {
            currentTeam = 0;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    JodTod.gameCounter += 1;
                    if (JodTod.gameCounter <= 1) {
                        bundle.putString("round", "R2");
                        bundle.putString("level", JodTod.gameLevel);
                        bundle.putInt("count", JodTod.list.get(JodTod.gameCounter));
                        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_jod_tod,
                                bundle, fragment_intro_character.class.getSimpleName());
                    } else {
                        Intent intent = new Intent(getActivity(), SamajhKeBolo.class);
                        intent.putExtra("level","L2");
                        bundle.putParcelableArrayList("playerModalArrayList", jodTodPlayerList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }, 2500);
        }
    }

    public void startSTT() {
        sttService.initCallback(JodTod_G2_L1.this);
        sttService.startListening();
    }

    @Override
    public void setGameTitleFromJson(String gameName) {
        gameTitle.setText(gameName);
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
                .setPosition(-50f, +50f, -50f, -50f)
                .stream(500, 1000L);
    }

    @Override
    public void setCurrentScore() {
        setInitialScores();
        bounceView(getCurrentView());
    }

    @Override
    public void initiateQuestion(String questionText, String[] wordsToSay) {
        startTimer();
        question.setText(questionText);
        question1.setText(wordsToSay[0]);
        question2.setText(wordsToSay[1]);
        text = questionText;
        playTTS();
    }

    @Override
    public void onResult(String result) {
        if (!timerEnd) {
            if (clickedMike == 1) {
                ans1.setText(result);
                if (result.equalsIgnoreCase(question1.getText().toString())) {
                    firstAns = true;
                    q1_layout.setBackgroundResource(R.drawable.green_answer_background);
                } else {
                    firstAns = false;
                    q1_layout.setBackgroundResource(R.drawable.red_answer_background);
                }
            } else {
                ans2.setText(result);
                if (result.equalsIgnoreCase(question2.getText().toString())) {
                    secondAns = true;
                    q2_layout.setBackgroundResource(R.drawable.green_answer_background);
                } else {
                    secondAns = false;
                    q2_layout.setBackgroundResource(R.drawable.red_answer_background);
                }
            }
        }
    }

    @OnClick(R.id.ib_g2_l1_speaker)
    public void soundClicked() {
        presenter.startTTS(text);
    }

    @OnClick(R.id.iv_mike1)
    public void mike1Clicked() {
        clickedMike = 1;
        startSTT();
    }

    @OnClick(R.id.iv_mike2)
    public void mike2Clicked() {
        clickedMike = 2;
        startSTT();
    }

    @Override
    public void onResume() {
        try {
            if (DataConfirmation.fragmentPauseFlg) {
                DataConfirmation.fragmentPauseFlg = false;
                mCountDownTimer.resume();
            }
        } catch (Exception e) {
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        DataConfirmation.fragmentPauseFlg = true;
        mCountDownTimer.pause();
        presenter.fragmentOnPause();
    }

}