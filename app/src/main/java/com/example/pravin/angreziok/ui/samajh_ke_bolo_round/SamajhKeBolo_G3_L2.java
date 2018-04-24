package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.interfaces.SpeechResult;
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


public class SamajhKeBolo_G3_L2 extends BaseFragment implements SamajhKeBoloContract.SamajhKeBolo_G3_L2_View, SpeechResult {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.g3_l2_sttOptions)
    LinearLayout answerView;
    @BindView(R.id.tv_g3_l2_answer)
    TextView answer;
    @BindView(R.id.ll_g3_l2_allstar)
    LinearLayout allstarLayout;
    @BindView(R.id.ll_g3_l2_megastar)
    LinearLayout megastarLayout;
    @BindView(R.id.ll_g3_l2_rockstar)
    LinearLayout rockstarLayout;
    @BindView(R.id.ll_g3_l2_superstar)
    LinearLayout superstarLayout;
    @BindView(R.id.g3_l2_megastar)
    TextView megaScore;
    @BindView(R.id.g3_l2_rockstar)
    TextView rockScore;
    @BindView(R.id.g3_l2_superstar)
    TextView superScore;
    @BindView(R.id.g3_l2_allstar)
    TextView allScore;
    @BindView(R.id.option1)
    TextView option1;
    @BindView(R.id.iv_option1)
    ImageView iv_option1;
    @BindView(R.id.option2)
    TextView option2;
    @BindView(R.id.iv_option2)
    ImageView iv_option2;
    @BindView(R.id.option3)
    TextView option3;
    @BindView(R.id.iv_option3)
    ImageView iv_option3;
    @BindView(R.id.option4)
    TextView option4;
    @BindView(R.id.iv_option4)
    ImageView iv_option4;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.tv_r3g3_question)
    TextView showQuestion;
    @BindView(R.id.konfettiView_g3_l2)
    KonfettiView konfettiView;

    String text;
    SamajhKeBoloContract.SamajhKeBoloPresenter presenter;
    int speechCount, currentTeam;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_samajh_ke_bolo_g3_l2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new SamajhKeBoloPresenterImpl(getActivity(), this, ttsService);
        setInitialScores();
        setDataForGame();
        speechCount = 0;
        currentTeam = 0;
        showDialog();
    }

    private void setInitialScores() {
        Log.d(":::", "setInitialScores: " + SamajhKeBolo.playerModalArrayList.toString());
        for (int i = 0; i < SamajhKeBolo.playerModalArrayList.size(); i++) {
            switch (SamajhKeBolo.playerModalArrayList.get(i).studentAlias) {
                case "Rockstars":
                    rockstarLayout.setVisibility(View.VISIBLE);
                    rockScore.setText(SamajhKeBolo.playerModalArrayList.get(i).studentScore);
                    break;
                case "Megastars":
                    megastarLayout.setVisibility(View.VISIBLE);
                    megaScore.setText(SamajhKeBolo.playerModalArrayList.get(i).studentScore);
                    break;
                case "Superstars":
                    superstarLayout.setVisibility(View.VISIBLE);
                    superScore.setText(SamajhKeBolo.playerModalArrayList.get(i).studentScore);
                    break;
                case "Allstars":
                    allstarLayout.setVisibility(View.VISIBLE);
                    allScore.setText(SamajhKeBolo.playerModalArrayList.get(i).studentScore);
            }
        }
    }

    private View getCurrentView() {
        View view = null;

        switch (SamajhKeBolo.playerModalArrayList.get(currentTeam).studentAlias) {
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
        speechCount = 0;
        String teamName = SamajhKeBolo.playerModalArrayList.get(currentTeam).getStudentAlias();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.dialog_tv_student_name);
        Button button = dialog.findViewById(R.id.dialog_btn_scan_qr);
        text.setText("Next question would be for " + teamName);
        button.setText("Ready ??");

        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.setQuestion_g3_l2();
                initiateQuestion();
            }
        });
    }

    private void fadeOtherGroups() {
        megastarLayout.setBackgroundResource(R.drawable.team_faded);
        rockstarLayout.setBackgroundResource(R.drawable.team_faded);
        allstarLayout.setBackgroundResource(R.drawable.team_faded);
        superstarLayout.setBackgroundResource(R.drawable.team_faded);

        switch (SamajhKeBolo.playerModalArrayList.get(currentTeam).getStudentAlias()) {
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

    @Override
    public void hideOptionView() {
        answerView.setVisibility(View.GONE);
    }

    @Override
    public void setQuestion(String questionText) {
        text = questionText;
        question.setText(questionText);
    }

    public void bounceView(View view) {
        Animation rubber = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    @Override
    public void initiateQuestion() {
        startTimer();
        playTTS();
    }

    private void setDataForGame() {
        String path = presenter.getSdcardPath();
        presenter.set_g3_l2_data(path);
        showQuestion.setText("Listen and answer properly");
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
        SamajhKeBolo.animateView(mCountDownTimer, getActivity());
    }

    @OnClick(R.id.ib_g3_l2_speaker)
    public void soundClicked() {
        presenter.startTTS(text);
    }

    @OnClick(R.id.ib_g3_l2_mic)
    public void micClicked() {
        speechCount++;
        if (speechCount <= 2)
            startSTT();
        else
            Toast.makeText(getActivity(), "Can be used only 2 Times", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.option1, R.id.option2, R.id.option3, R.id.option4})
    public void optionsClicked(View view) {
        // TTS for the options clicked
        TextView option = (TextView) view;
        View parent = (View) option.getParent();
        parent.setBackgroundResource(R.drawable.custom_dialog_bg3);
        presenter.startTTS(option.getText() + "");
    }

    @OnClick({R.id.iv_option1, R.id.iv_option2, R.id.iv_option3, R.id.iv_option4})
    public void options_iv_clicked(View view) {
        switch (view.getId()) {
            case R.id.iv_option1:
                presenter.checkAnswerOfOptions(option1.getText().toString());
                break;
            case R.id.iv_option2:
                presenter.checkAnswerOfOptions(option2.getText().toString());
                break;
            case R.id.iv_option3:
                presenter.checkAnswerOfOptions(option3.getText().toString());
                break;
            case R.id.iv_option4:
                presenter.checkAnswerOfOptions(option4.getText().toString());
                break;
        }
    }

    @OnClick(R.id.iv_g3_l2_submit_ans)
    public void submitAns() {
        //TODO post processing
  /*      submitAnswer.setClickable(false);
        mCountDownTimer.pause();
        presenter.checkFinalAnswer_g3_l2(answer.getText().toString(), currentTeam);
        currentTeam += 1;
        if (currentTeam < playerModalArrayList.size()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    submitAnswer.setClickable(true);
                    showDialog();
                }
            }, 2500);
        } else {
            currentTeam = 0;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //TODO display Score screen after final round
                    getActivity().findViewById(R.id.iv_g3_l2_submit_ans).setOnClickListener(null);

                    Bundle bundle = new Bundle();
                    bundle.putString("frag", "R1G2L2");
                    PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_bole_toh,
                            bundle, fragment_intro_character.class.getSimpleName());

                }
            }, 2500);

        }*/
    }

    public void startSTT() {
        sttService.initCallback(SamajhKeBolo_G3_L2.this);
        sttService.startListening();
    }

    @Override
    public void setQuestionWords(String[] words) {
        try {
            option1.setText(words[0]);
            option2.setText(words[1]);
            option3.setText(words[2]);
            option4.setText(words[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAnswer(String ans) {
        answer.setText(ans);
    }

    @Override
    public void onResult(String result) {
        answerView.setVisibility(View.VISIBLE);
        answer.setText(result);
    }
}