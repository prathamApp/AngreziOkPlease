package com.example.pravin.angreziok.ui.jod_tod_round;

import android.app.Dialog;
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
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.example.pravin.angreziok.BaseActivity.sttService;
import static com.example.pravin.angreziok.BaseActivity.ttsService;
import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.jodTodPlayerList;


public class JodTod_G1_L2 extends BaseFragment implements JodTodContract.JodTod_G1_L2_View, SpeechResult {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.myflowlayout)
    FlowLayout flowLayout;
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
    int speechCount, currentTeam, score=0;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jod_tod_g1_l2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new JodTodPresenterImpl(getActivity(), this, ttsService);
        setInitialScores();
        setDataForGame();
        currentTeam = 0;
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
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.dialog_tv_student_name);
        ImageView iv_close = dialog.findViewById(R.id.dialog_iv_close);
        Button button = dialog.findViewById(R.id.dialog_btn_scan_qr);
        text.setText("Next question would be for " + teamName);
        button.setText("Ready ??");
        sttOptions.setVisibility(View.GONE);
        flowLayout.removeAllViews();

        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String question = presenter.g1_l2_getQuestionText();
                initiateQuestion(question);
                setQuestionDynamically(question);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String question = presenter.g1_l2_getQuestionText();
                initiateQuestion(question);
                setQuestionDynamically(presenter.g1_l2_getQuestionText());
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
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
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
    public void setAnswer(String ans, String sttWord) {

        final TextView textView = new TextView(getActivity());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setHeight(50);
        textView.setWidth(flowLayout.getWidth());
        textView.setTextSize(25);
        textView.setText("1:"+sttWord);
        Log.d("changeColor", "Ques : " + text + "    setAnswer : " + ans + "    sttWord : " + sttWord);

        if (text.equalsIgnoreCase("" + ans)) {
//            textView.setTextColor(Color.GREEN);
            score+=5;
        } else {
//            textView.setTextColor(Color.RED);
        }


        flowLayout.addView(textView);
    }

    private void setDataForGame() {
        presenter.set_g1_l2_data();
        showQuestion.setText("Say The Words Starting With");
    }

    private void playTTS() {
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
        JodTod.animateView(mCountDownTimer, getActivity());
    }

    @OnClick(R.id.iv_submit_ans)
    public void submitAns() {
        submitAnswer.setClickable(false);
        mCountDownTimer.pause();
//       TODO Check answer  presenter.checkFinalAnswer_g1_l2(answer.getText().toString(), currentTeam);
        currentTeam += 1;
        if (currentTeam < jodTodPlayerList.size()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    submitAnswer.setClickable(true);
                    score=0;
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
                    // TODO  Next Round start process  SamajhKeBolo
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
        sttService.initCallback(JodTod_G1_L2.this);
        sttService.startListening();
    }

    @Override
    public void onResult(String result) {
        sttOptions.setVisibility(View.VISIBLE);
        presenter.g1_l2_checkAnswer(result);
    }

    @OnClick(R.id.btn_tempskip)
    public void skipToNext() {
        Bundle bundle = new Bundle();
        bundle.putString("frag", "R2G2L2");
        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_jod_tod,
                bundle, fragment_intro_character.class.getSimpleName());
    }

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