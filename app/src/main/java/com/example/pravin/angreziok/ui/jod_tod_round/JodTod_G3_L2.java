package com.example.pravin.angreziok.ui.jod_tod_round;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import static com.example.pravin.angreziok.BaseActivity.ttsService;
import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.animateView;
import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.jodTodPlayerList;


public class JodTod_G3_L2 extends BaseFragment implements JodTodContract.JodTod_G3_L2_View {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.ll_g1_l2_allstar)
    LinearLayout allstarLayout;
    @BindView(R.id.ll_g1_l2_megastar)
    LinearLayout megastarLayout;
    @BindView(R.id.ll_g1_l2_rockstar)
    LinearLayout rockstarLayout;
    @BindView(R.id.ll_g1_l2_superstar)
    LinearLayout superstarLayout;
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
    @BindView(R.id.konfettiView_g1_l2)
    KonfettiView konfettiView;
    @BindView(R.id.iv_submit_ans)
    ImageView submitAnswer;
    @BindView(R.id.questionDiv)
    LinearLayout questionDiv;
    @BindView(R.id.tv_game_title)
    TextView gameTitle;

    String text, path;
    JodTodContract.JodTodPresenter presenter;
    int currentTeam;
    Dialog dialog;
    TextView currentTextView;
    boolean playingThroughTts = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jod_tod_g3_l2, container, false);
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

    @Override
    public void setGameTitleFromJson(String gameName) {
        gameTitle.setText(gameName);
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
                String question = presenter.g3_l2_getQuestionText(studentID);
                if (!playingThroughTts)
                    path = presenter.g3_l2_getQuestionAudio();
                initiateQuestion(question);
                setQuestionDynamically(question);
            }
        });
    }

    private void setQuestionDynamically(String questionText) {
        String letters[] = questionText.split("");
        int lettersLength = letters.length;
        questionDiv.removeAllViews();
        int isFirst = 0;
        for (int i = 1; i < lettersLength; i++) {
            String currentLetter = letters[i];
            final TextView textView = new TextView(getActivity());
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setAllCaps(true);
            textView.setHeight(45);
            textView.setWidth(45);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD_ITALIC);
            textView.setTextSize(25);
            if ((currentLetter.equalsIgnoreCase("a")
                    || currentLetter.equalsIgnoreCase("e")
                    || currentLetter.equalsIgnoreCase("i")
                    || currentLetter.equalsIgnoreCase("o")
                    || currentLetter.equalsIgnoreCase("u"))) {
                textView.setText(currentLetter);
                textView.setBackgroundResource(R.drawable.round_set_bg);
            } else {
                isFirst++;
                textView.setText(" ");
                textView.setBackgroundResource(R.drawable.round_identify_bg);
                if (isFirst == 1) {
                    textView.setBackgroundResource(0);
                    textView.setBackgroundResource(R.drawable.square_selected_bg);
                    currentTextView = textView;
                }
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (textView != currentTextView) {
                            currentTextView.setBackgroundResource(R.drawable.round_identify_bg);
                        }
                        textView.setBackgroundResource(0);
                        textView.setBackgroundResource(R.drawable.square_selected_bg);
                        currentTextView = textView;
                    }
                });
            }
            questionDiv.addView(textView);
        }
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
                .setPosition(-50f, +50f, -50f, -50f)
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
        if (playingThroughTts) {
            text = question;
        }
        playTTS();

    }

    private void setDataForGame() {
        presenter.set_g3_l2_data(2);
        showQuestion.setText("Fill the missing letters");
    }

    @Override
    public void setQuestionText(String questionString) {
        showQuestion.setText(questionString);
    }

    private void playTTS() {
        if (playingThroughTts)
            presenter.startTTS(text);
        else
            presenter.playMusic(path, presenter.getSdcardPath() + "Sounds/ListenSpellGame/");
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
        presenter.checkFinalAnswer_g3_l2(getFinalAnswer(), currentTeam);
        currentTeam += 1;
        if (currentTeam < jodTodPlayerList.size()) {
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

                    getActivity().findViewById(R.id.iv_submit_ans).setOnClickListener(null);

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

    private String getFinalAnswer() {
        String answer = "";
        for (int i = 0; i < questionDiv.getChildCount(); i++)
            if (questionDiv.getChildAt(i) instanceof TextView)
                answer += ((TextView) questionDiv.getChildAt(i)).getText();
        return answer;
    }


    @OnClick(R.id.ib_g1_l2_speaker)
    public void soundClicked() {
        playTTS();
    }

    @OnClick({R.id.A, R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I, R.id.J,
            R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R, R.id.S, R.id.T,
            R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z})
    public void keyClicked(View view) {
        TextView textView = (TextView) view;
        animateView(textView, getActivity());
//        Toast.makeText(getActivity(), "" + textView.getText(), Toast.LENGTH_SHORT).show();
        currentTextView.setText(textView.getText());
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