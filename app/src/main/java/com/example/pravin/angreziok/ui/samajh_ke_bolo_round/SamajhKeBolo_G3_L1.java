package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.interfaces.SpeechResult;
import com.example.pravin.angreziok.ui.final_screen.ResultScreen;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod;
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
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.gameCounter;
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.gameLevel;
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.playerModalArrayList;

public class SamajhKeBolo_G3_L1 extends BaseFragment implements SamajhKeBoloContract.SamajhKeBolo_G3_L1_View, SpeechResult {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.iv_character_dialog_gif)
    TextView questionTextView;
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
    @BindView(R.id.konfettiView_r1g2)
    KonfettiView konfettiView;
    @BindView(R.id.iv_r1g2_submit_ans)
    ImageView submitAnswer;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.tv_game_title)
    TextView gameTitle;

    String text,audioPath;
    SamajhKeBoloContract.SamajhKeBoloPresenter presenter;
    int speechCount, currentTeam, timeOfTimer = 20000;
    Dialog dialog;
    boolean playingThroughTts = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_samajh_ke_bolo_g3_l1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        DataConfirmation.fragmentPauseFlg = false;
        presenter = new SamajhKeBoloPresenterImpl(getActivity(), this, ttsService);
        setInitialScores();
        setDataForGame();
        speechCount = 0;
        currentTeam = 0;
        mCountDownTimer.setClickable(false);
        showDialog();
    }

    @Override
    public void setGameTitleFromJson(String gameName) {
        gameTitle.setText(gameName);
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

    private View getCurrentView() {
        View view = null;

        switch (playerModalArrayList.get(currentTeam).studentAlias) {
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
        answer.setText("");
        speechCount = 0;
        String teamName = playerModalArrayList.get(currentTeam).getStudentAlias();
        dialog = new Dialog(getActivity());
        final String studentID = playerModalArrayList.get(currentTeam).getStudentID();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.dialog_tv_student_name);
        Button button = dialog.findViewById(R.id.dialog_btn_scan_qr);
        text.setText("Next question would be for " + teamName);
        button.setText("Ready ??");

        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.setImage_g3_l1(studentID);
            }
        });

    }

    private void fadeOtherGroups() {
        megastarLayout.setBackgroundResource(R.drawable.team_faded);
        rockstarLayout.setBackgroundResource(R.drawable.team_faded);
        allstarLayout.setBackgroundResource(R.drawable.team_faded);
        superstarLayout.setBackgroundResource(R.drawable.team_faded);

        switch (playerModalArrayList.get(currentTeam).getStudentAlias()) {
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
                .setPosition(-50f, 50f, -50f, -50f)
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
    public void initiateQuestion() {
        startTimer();
        if(playingThroughTts)
            playTTS();
        else
            presenter.playMusic("",audioPath);
    }

    private void setDataForGame() {
        String path = presenter.getSdcardPath();
        presenter.set_g3_l1_data(path);
    }

    private void playTTS() {
        presenter.startTTS(text);
    }

    @Override
    public void setQuestionText(String questionString, String questionAudioPath) {
        text = questionString;
        audioPath = questionAudioPath;
        initiateQuestion();
        question.setText(questionString);
    }


    private void startTimer() {
        mCountDownTimer.ready();
        mCountDownTimer.start(timeOfTimer);
        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                submitAns();
            }
        });
        SamajhKeBolo.animateView(mCountDownTimer, getActivity());
    }

    @OnClick(R.id.ib_r1g2_speaker)
    public void soundClicked() {
        if(playingThroughTts)
            presenter.startTTS(text);
        else
            presenter.playMusic("",audioPath);
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
        submitAnswer.setClickable(false);
        mCountDownTimer.pause();
        presenter.checkFinalAnswer_g3_l1(answer.getText().toString(), currentTeam);

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
                    getActivity().findViewById(R.id.iv_r1g2_submit_ans).setOnClickListener(null);
                    Bundle bundle = new Bundle();
                    gameCounter += 1;
                    if (gameCounter <= 0) {
                        bundle.putString("round", "R1");
                        bundle.putString("level", "l1");
                        bundle.putInt("count", SamajhKeBolo.list.get(gameCounter));
                        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_bole_toh,
                                bundle, fragment_intro_character.class.getSimpleName());
                    } else {
                        Intent intent = new Intent(getActivity(), JodTod.class);
                        intent.putExtra("level","l1");
                        bundle.putParcelableArrayList("playerModalArrayList", playerModalArrayList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }, 2500);

        }
    }

    public void startSTT() {
        sttService.initCallback(SamajhKeBolo_G3_L1.this);
        sttService.startListening();
    }

    @Override
    public void onResult(String result) {
        optionsView.setVisibility(View.VISIBLE);
        setAnswer(result);
        presenter.g3_l1_checkAnswer(result);
    }

    @Override
    public void hideOptionView() {
        optionsView.setVisibility(View.GONE);
    }

    @Override
    public void setQuestionTextNative(String path) {
        try {
            questionTextView.setText(path);
        } catch (Exception e) {
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