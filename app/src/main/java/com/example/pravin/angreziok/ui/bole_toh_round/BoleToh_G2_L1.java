package com.example.pravin.angreziok.ui.bole_toh_round;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.custom.GifView;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod;
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import java.io.FileInputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.example.pravin.angreziok.BaseActivity.ttsService;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.gameCounter;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.gameLevel;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playerModalArrayList;


public class BoleToh_G2_L1 extends BaseFragment implements BoleTohContract.BoleToh_G2_L1_View {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;

    @BindView(R.id.ll_r1g1_megastar)
    LinearLayout megastarLayout;
    @BindView(R.id.ll_r1g1_rockstars)
    LinearLayout rockstarLayout;
    @BindView(R.id.ll_r1g1_superstars)
    LinearLayout superstarLayout;
    @BindView(R.id.ll_r1g1_allstar)
    LinearLayout allstarLayout;

    @BindView(R.id.r1g1_megastar)
    TextView megaScore;
    @BindView(R.id.r1g1_rockstars)
    TextView rockScore;
    @BindView(R.id.r1g1_superstars)
    TextView superScore;
    @BindView(R.id.r1g1_allstar)
    TextView allScore;

    @BindView(R.id.ll_mic)
    LinearLayout layout_mic;
    @BindView(R.id.iv_image1)
    GifView iv_image1;
    @BindView(R.id.iv_image2)
    GifView iv_image2;
    @BindView(R.id.iv_image3)
    GifView iv_image3;
    @BindView(R.id.iv_image4)
    GifView iv_image4;
    @BindView(R.id.ib_r1g1_speaker)
    ImageButton ib_speaker;
    @BindView(R.id.konfettiView_r1g1)
    KonfettiView konfettiView;
    @BindView(R.id.tv_game_title)
    TextView gameTitle;
    @BindView(R.id.tv_question)
    TextView gameQuestion;

    int questionConter = 0;

    BoleTohContract.BoleTohPresenter presenter;
    String path;
    Dialog dialog;
    int currentTeam = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bole_toh_g2_l1, container, false);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new BoleTohPresenterImpl(getActivity(), this, ttsService);
        setOnClickListeners();
        path = presenter.getSdcardPath();
        presenter.doInitialWorkG2l1(path);
        setInitialScores();
        showDialog();
//        customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
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

    public void showDialog() {
        fadeOtherGroups();
        String teamName = playerModalArrayList.get(currentTeam).getStudentAlias();
        final String studentID = playerModalArrayList.get(currentTeam).getStudentID();
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

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mCountDownTimer.start(15000);
                BoleToh.animateView(mCountDownTimer, getActivity());
                presenter.showImagesG2L1(path, studentID);
            }
        });

    }

    private void setOnClickListeners() {
        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                mCountDownTimer.failure();
                presenter.g2_l1_checkAnswer(10, currentTeam, true);
                answerPostProcessing();
            }
        });

        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
            }
        });
    }

    @OnClick(R.id.iv_image1)
    public void setIv_image1() {
        presenter.g2_l1_checkAnswer(1, currentTeam, false);
        answerPostProcessing();
    }

    @OnClick(R.id.iv_image2)
    public void setIv_image2() {
        presenter.g2_l1_checkAnswer(2, currentTeam, false);
        answerPostProcessing();
    }

    @OnClick(R.id.iv_image3)
    public void setIv_image3() {
        presenter.g2_l1_checkAnswer(3, currentTeam, false);
        answerPostProcessing();
    }

    @OnClick(R.id.iv_image4)
    public void setIv_image4() {
        presenter.g2_l1_checkAnswer(4, currentTeam, false);
        answerPostProcessing();
    }

    public void answerPostProcessing() {

        iv_image1.setClickable(false);
        iv_image2.setClickable(false);
        iv_image3.setClickable(false);
        iv_image4.setClickable(false);

        mCountDownTimer.pause();
        currentTeam += 1;
        if (currentTeam < playerModalArrayList.size()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_image1.setClickable(true);
                    iv_image2.setClickable(true);
                    iv_image3.setClickable(true);
                    iv_image4.setClickable(true);
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
                    gameCounter += 1;
                    if (gameCounter <= 1) {
                        bundle.putString("round", "R1");
                        bundle.putString("level", "l1");
                        bundle.putInt("count", BoleToh.list.get(gameCounter));
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

    @Override
    public void setQuestion(String correctAns) {
        gameQuestion.setText("Where is "+correctAns+" ?");
    }

    @OnClick(R.id.ib_r1g1_speaker)
    public void playQuestion() {
        presenter.playQuestionAudio(2);
    }

    @Override
    public void setQuestionGifs(final int readQuesNo, String... mygifs) {
        try {
            questionConter++;
            iv_image1.setGifResource(new FileInputStream(mygifs[0]));
            iv_image2.setGifResource(new FileInputStream(mygifs[1]));
            iv_image3.setGifResource(new FileInputStream(mygifs[2]));
            iv_image4.setGifResource(new FileInputStream(mygifs[3]));

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.playQuestionAudio(2);
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


