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
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod;
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
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.gameCounter;
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.gameLevel;

public class SamajhKeBolo_G2_L1 extends BaseFragment implements SamajhKeBoloContract.SamajhKeBolo_G2_L1_View{

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
    ImageView iv_image1;
    @BindView(R.id.iv_image2)
    ImageView iv_image2;
    @BindView(R.id.iv_image3)
    ImageView iv_image3;
    @BindView(R.id.ib_r1g1_speaker)
    ImageButton ib_speaker;
    @BindView(R.id.konfettiView_r1g1)
    KonfettiView konfettiView;


    int questionConter = 0;

    SamajhKeBoloContract.SamajhKeBoloPresenter presenter;
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
        return inflater.inflate(R.layout.fragment_samajh_ke_bolo_g2_l1, container, false);
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
        presenter = new SamajhKeBoloPresenterImpl(getActivity(), this, ttsService);
        setOnClickListeners();
        path = presenter.getSdcardPath();
        presenter.doInitialWorkG2l1(path);
        setInitialScores();
        showDialog();
        //customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
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

    public void showDialog() {
        fadeOtherGroups();
        String teamName = SamajhKeBolo.playerModalArrayList.get(currentTeam).getStudentAlias();
        final String studentID = SamajhKeBolo.playerModalArrayList.get(currentTeam).getStudentID();
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
                presenter.showImagesG2L1(path, studentID);
            }
        });

    }

    private void setOnClickListeners() {
        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                presenter.g2_l1_checkAnswer(10, currentTeam, true);
                answerPostProcessing();
                mCountDownTimer.failure();
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

    public void answerPostProcessing() {

        iv_image1.setClickable(false);
        iv_image2.setClickable(false);
        iv_image3.setClickable(false);

        mCountDownTimer.pause();
        currentTeam += 1;
        if (currentTeam < SamajhKeBolo.playerModalArrayList.size()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_image1.setClickable(true);
                    iv_image2.setClickable(true);
                    iv_image3.setClickable(true);
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
                        bundle.putInt("count", SamajhKeBolo.list.get(gameCounter));
                        PD_Utility.showFragment(getActivity(), new fragment_intro_character(), R.id.cl_samajh_ke_bolo,
                                bundle, fragment_intro_character.class.getSimpleName());
                    } else {
                        Intent intent = new Intent(getActivity(), JodTod.class);
                        intent.putExtra("level", "l1");
                        bundle.putParcelableArrayList("SamajhKeBolo.playerModalArrayList", SamajhKeBolo.playerModalArrayList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }, 2500);

        }
    }

    @OnClick(R.id.ib_r1g1_speaker)
    public void playQuestion() {
        presenter.replayQuestionroundone();
    }

    @Override
    public void setQuestionImgsG2L1(final int readQuesNo, Bitmap... bitmaps) {
        try {
            questionConter++;
            iv_image1.setImageBitmap(bitmaps[0]);
            iv_image2.setImageBitmap(bitmaps[1]);
            iv_image3.setImageBitmap(bitmaps[2]);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.readQuestion(readQuesNo);
                }
            }, 1500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


