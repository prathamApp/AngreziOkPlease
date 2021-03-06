package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.interfaces.SpeechResult;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.final_screen.ResultScreen;
import com.example.pravin.angreziok.ui.fragment_intro_character;
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
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.gameLevel;
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.playerModalArrayList;


public class SamajhKeBolo_G1_L2 extends BaseFragment implements SamajhKeBoloContract.SamajhKeBolo_G1_L2_View, SpeechResult {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.tv_g1_l2_answer)
    TextView answer;
    @BindView(R.id.ll_g1_l2_sttoptions)
    LinearLayout options;
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
    @BindView(R.id.option1)
    TextView option1;
    @BindView(R.id.option2)
    TextView option2;
    @BindView(R.id.option3)
    TextView option3;
    @BindView(R.id.g1_l2_sttOptions)
    LinearLayout optionsView;
    @BindView(R.id.konfettiView_g1_l2)
    KonfettiView konfettiView;
    @BindView(R.id.iv_g1_l2_submit_ans)
    ImageView submitAnswer;
    @BindView(R.id.qusetionImage)
    ImageView qusetionImage;
    @BindView(R.id.tv_g1_l2_question)
    TextView questionText;
    @BindView(R.id.tv_game_title)
    TextView gameTitle;

    String text;
    SamajhKeBoloContract.SamajhKeBoloPresenter presenter;
    int speechCount, currentTeam;
    Dialog dialog;
    String[] optionsAudio;
    Integer[] shuffledOptions;
    boolean playingThroughTts = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_samajh_ke_bolo_g1_l2, container, false);
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
        final String studentID = playerModalArrayList.get(currentTeam).getStudentID();
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.setImage_gl_l2(studentID);
                initiateQuestion();
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
                .setPosition(-50f,+ 50f, -50f, -50f)
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
        if (playingThroughTts)
            text = presenter.getCurrentQuestion_g1_l2();
        else
            text = presenter.getCurrentQuestionAudio();

        questionText.setText(presenter.getCurrentQuestion_g1_l2());
        playTTS();
    }

    private void setDataForGame() {
        String path = presenter.getSdcardPath();
        presenter.set_g1_l2_data(path);
        questionText.setText("Listen and answer properly");
    }

    private void playTTS() {
        if (playingThroughTts)
            presenter.startTTS(text);
        else
            presenter.playMusic(text,presenter.getSdcardPath()+"Sounds/WhereGame/");
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

    @OnClick(R.id.ib_g1_l2_speaker)
    public void soundClicked() {
        playTTS();
    }

    @OnClick(R.id.ib_g1_l2_mic)
    public void micClicked() {
        speechCount++;
        if (speechCount <= 2)
            startSTT();
        else
            Toast.makeText(getActivity(), "Can be used only 2 Times", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.option1)
    public void option1Clicked(View view) {
        // TTS for the options clicked
        TextView option = (TextView) view;
        playOptions(shuffledOptions[0],option.getText()+"");
    }

    @OnClick(R.id.option2)
    public void option2Clicked(View view) {
        // TTS for the options clicked
        TextView option = (TextView) view;
        playOptions(shuffledOptions[1],option.getText()+"");
    }

    @OnClick(R.id.option3)
    public void option3Clicked(View view) {
        // TTS for the options clicked
        TextView option = (TextView) view;
        playOptions(shuffledOptions[2],option.getText()+"");
    }

    public void playOptions(int optionNo,String optionText){
        answer.setText(optionText);
        if (playingThroughTts)
            presenter.startTTS(optionText);
        else
            presenter.playMusic(optionsAudio[optionNo],presenter.getSdcardPath()+"Sounds/WhereGame/");
    }

    @OnClick(R.id.iv_g1_l2_submit_ans)
    public void submitAns() {
        submitAnswer.setClickable(false);
        mCountDownTimer.pause();
        presenter.checkFinalAnswer_g1_l2(answer.getText().toString(), currentTeam);
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
                    getActivity().findViewById(R.id.iv_g1_l2_submit_ans).setOnClickListener(null);
                    Bundle bundle = new Bundle();
                    SamajhKeBolo.gameCounter += 1;
                    if (SamajhKeBolo.gameCounter <= 1) {
                        bundle.putString("round", "R3");
                        bundle.putString("level", gameLevel);
                        bundle.putInt("count", SamajhKeBolo.list.get(SamajhKeBolo.gameCounter));
                        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_samajh_ke_bolo,
                                bundle, fragment_intro_character.class.getSimpleName());
                    } else {
                        Intent intent = new Intent(getActivity(), ResultScreen.class);
                        bundle.putParcelableArrayList("PlayerList", playerModalArrayList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }, 2500);
        }
    }

    public void startSTT() {
        sttService.initCallback(SamajhKeBolo_G1_L2.this);
        sttService.startListening();
    }

    @Override
     public void hideOptionView() {
         optionsView.setVisibility(View.GONE);
     }

     @Override
     public void setQuestionImage(String path) {
         try {
             Bitmap[] bitmap = {BitmapFactory.decodeFile("" + path)};
             qusetionImage.setImageBitmap(bitmap[0]);
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
     public void showOptions_g1_l2() {
         options.setVisibility(View.VISIBLE);
         shuffledOptions = presenter.getShuffledOptions();
         String[] options = presenter.getOptions_g1_l2();
         optionsAudio = presenter.getOptionsAudio();
         option1.setText(options[shuffledOptions[0]]);
         option2.setText(options[shuffledOptions[1]]);
         option3.setText(options[shuffledOptions[2]]);
     }

    @Override
    public void onResult(String result) {
        optionsView.setVisibility(View.VISIBLE);
        setAnswer(result);
        presenter.g1_l2_checkAnswer(result);
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