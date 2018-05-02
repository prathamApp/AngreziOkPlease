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


public class JodTod_G2_L2 extends BaseFragment implements JodTodContract.JodTod_G2_L2_View, SpeechResult {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.myflowlayout)
    LinearLayout flowLayout;
    @BindView(R.id.ll_g1_l2_allstar)
    LinearLayout allstarLayout;
    @BindView(R.id.ll_g1_l2_megastar)
    LinearLayout megastarLayout;
    @BindView(R.id.ll_g1_l2_rockstar)
    LinearLayout rockstarLayout;
    @BindView(R.id.ll_g1_l2_superstar)
    LinearLayout superstarLayout;
    @BindView(R.id.r2_g1_l2_sttOptions)
    LinearLayout sttOptions;
    @BindView(R.id.g1_l2_megastar)
    TextView megaScore;
    @BindView(R.id.g1_l2_rockstar)
    TextView rockScore;
    @BindView(R.id.g1_l2_superstar)
    TextView superScore;
    @BindView(R.id.g1_l2_allstar)
    TextView allScore;
    @BindView(R.id.tv_question)
    TextView showQuestion;
    @BindView(R.id.iv_ques_img_r2_g1_l2)
    TextView tv_ques_img;
    @BindView(R.id.konfettiView_g1_l2)
    KonfettiView konfettiView;
    @BindView(R.id.iv_submit_ans)
    ImageView submitAnswer;

    String text;
    JodTodContract.JodTodPresenter presenter;
    int speechCount, currentTeam, score = 0,timeOfTimer=20000;
    float totalAnsCounter=0f,correctAnsCounter=0f;
    Dialog dialog;
    boolean timerEnd = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jod_tod_g2_l2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new JodTodPresenterImpl(getActivity(), this, ttsService);
        presenter.set_g2_l2_data();
        setInitialScores();
        setDataForGame();
        currentTeam = 0;
        totalAnsCounter=0;
        correctAnsCounter=0;
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
        sttOptions.setVisibility(View.GONE);
        flowLayout.removeAllViews();

        timerEnd=false;

        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                totalAnsCounter=0;
                correctAnsCounter=0;
                String question = presenter.g2_l2_getQuestionText(studentID);
                initiateQuestion(question);
                setQuestionDynamically(question);
            }
        });
    }

    private void setQuestionDynamically(String questionText) {
        // TODO Create views for setting question by extracting vowels from it
        tv_ques_img.setText(questionText);
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
                .setPosition(-50f, + 50f, -50f, -50f)
                .stream(500, 1000L);
    }


    @Override
    public void setCurrentScore() {
        setInitialScores();
        bounceView(getCurrentView());
    }

    public void bounceView(View view) {
        Animation rubber = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    @Override
    public void initiateQuestion(String question) {
        startTimer();
        text = question;
        playTTS();
    }

    @Override
    public void setAnswer(String ans, String sttWord, boolean match) {

        TextView textView = new TextView(getActivity());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setHeight(50);
        textView.setWidth(flowLayout.getWidth());
        Log.d("changeColor", "Ques : " + text + "    setAnswer : " + ans + "    sttWord : " + sttWord);
        totalAnsCounter+=1;

        if (ans.equalsIgnoreCase("true")) {
            textView.setTextColor(Color.GREEN);
            correctAnsCounter+=1;
            score += 5;
            presenter.setCurrentScore(5);
        } else {
            textView.setTextColor(Color.RED);
        }
        textView.setTextSize(25);
        textView.setText(sttWord);
        flowLayout.addView(textView);
    }

    @Override
    public void hideOptionView() {
        //TODO
        showQuestion.setVisibility(View.GONE);
    }

    private void setDataForGame() {
        presenter.set_g1_l2_data();
        showQuestion.setText("Say The Words Starting With");
    }

    @Override
    public void setQuestionText(String questionString){
        showQuestion.setText(questionString);
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
                timerEnd=true;submitAns();
            }
        });
        JodTod.animateView(mCountDownTimer, getActivity());
    }

    @OnClick(R.id.iv_submit_ans)
    public void submitAns() {
        submitAnswer.setClickable(false);
        mCountDownTimer.pause();
//       TODO Check answer  presenter.checkFinalAnswer_g1_l2(answer.getText().toString(), currentTeam);
        float finalPercentage=0f;
        Log.d("finalPercentage", "correctAnsCounter: " + correctAnsCounter);
        Log.d("finalPercentage", "totalAnsCounter: " + totalAnsCounter);

        if(correctAnsCounter>0 && totalAnsCounter>0)
            finalPercentage = (correctAnsCounter / totalAnsCounter) * 100f;
        String currScore = "" + score;
        Log.d("finalPercentage", "finalPercentage: " + finalPercentage);
        presenter.checkFinalAnswer_g2_l2(finalPercentage, currScore, currentTeam);

        currentTeam += 1;
        if (currentTeam < jodTodPlayerList.size()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    submitAnswer.setClickable(true);
                    score = 0;
                    showDialog();
                }
            }, 2500);
        } else {
            currentTeam = 0;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    getActivity().findViewById(R.id.iv_submit_ans).setOnClickListener(null);
                    // TODO  Next Round start process  G3
                    Bundle bundle = new Bundle();
                    JodTod.gameCounter += 1;
                    if (JodTod.gameCounter <= 1) {
                        bundle.putString("round", "R2");
                        bundle.putInt("count", JodTod.list.get(JodTod.gameCounter));
                        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_jod_tod,
                                bundle, fragment_intro_character.class.getSimpleName());
                    } else {
                        Intent intent = new Intent(getActivity(), SamajhKeBolo.class);
                        bundle.putParcelableArrayList("PlayerList", jodTodPlayerList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    /*Intent intent = new Intent(getActivity(), SamajhKeBolo.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("PlayerList", jodTodPlayerList);
                    intent.putExtras(bundle);
                    startActivity(intent);*/

                }
            }, 2500);

        }
    }

    public void startSTT() {
        sttService.initCallback(JodTod_G2_L2.this);
        sttService.startListening();
    }

    @Override
    public void onResult(String result) {
        sttOptions.setVisibility(View.VISIBLE);
        Log.d("JodTod", "STTResult: "+result);
        if(!timerEnd)
            presenter.g2_l2_checkAnswer(result);
    }

    /*@OnClick(R.id.btn_tempskip)
    public void skipToNext() {
        Bundle bundle = new Bundle();
        bundle.putString("frag", "R2G3L2");
        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_jod_tod,
                bundle, fragment_intro_character.class.getSimpleName());
    }*/

    @OnClick(R.id.ib_r2g1_mic)
    public void micClicked() {
        speechCount++;
        startSTT();
    }

    @OnClick(R.id.ib_g1_l2_speaker)
    public void soundClicked() {
        presenter.startTTS(text);
    }
}