package com.example.pravin.angreziok.ui.bole_toh_round;

import android.app.Dialog;
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
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playerModalArrayList;


public class BoleTohRoundOne extends BaseFragment implements BoleTohContract.BoleTohRoundOneView {

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
    @BindView(R.id.iv_image4)
    ImageView iv_image4;
    @BindView(R.id.ib_r1g1_speaker)
    ImageButton ib_speaker;
    @BindView(R.id.bt_temp_skip)
    Button bt_temp_skip;
    @BindView(R.id.konfettiView_r1g1)
    KonfettiView konfettiView;


    int questionConter = 0;

    //    ArrayList <GenericModalGson> gsonPicGameData = new ArrayList<GenericModalGson>();
    BoleTohContract.BoleTohPresenter presenter;
    String path;
    Dialog dialog;
    int currentTeam = 0;
//    CustomCountDownTimer customCountDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bole_toh_round_one, container, false);
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
        presenter = new BoleTohPresenterImpl(getActivity(), this, BoleToh.playtts);
        setOnClickListeners();
        path = presenter.getSdcardPath();
        presenter.doInitialWork(path);
        setInitialScores();
        showDialog();
//        customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
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

        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mCountDownTimer.start(15000);
                BoleToh.animateView(mCountDownTimer, getActivity());
                presenter.showImages(path);
                //initiateQuestion();
                //presenter.setImage_r1g2();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mCountDownTimer.start(15000);
                BoleToh.animateView(mCountDownTimer, getActivity());
                presenter.showImages(path);
                //initiateQuestion();
                //presenter.setImage_r1g2();
            }
        });

    }

    private void setOnClickListeners() {
        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "finish", Toast.LENGTH_SHORT).show();
                mCountDownTimer.failure();
            }
        });

        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
            }
        });
    }

    @OnClick(R.id.bt_temp_skip)
    public void setBt_temp_skip() {
        PD_Utility.showFragment(getActivity(), new BoleTohRoundTwo(), R.id.cl_bole_toh,
                null, BoleTohRoundTwo.class.getSimpleName());
    }

    @OnClick(R.id.iv_image1)
    public void setIv_image1() {
/*                Animation pop = AnimationUtils.loadAnimation(getActivity(), R.anim.popup);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                pop.setInterpolator(interpolator);
                iv_image1.startAnimation(pop);*/
        presenter.r1g1_checkAnswer(1, currentTeam, false);
        answerPostProcessing();
    }

    @OnClick(R.id.iv_image2)
    public void setIv_image2() {
        presenter.r1g1_checkAnswer(2, currentTeam, false);
        answerPostProcessing();
    }

    @OnClick(R.id.iv_image3)
    public void setIv_image3() {
        presenter.r1g1_checkAnswer(3, currentTeam, false);
        answerPostProcessing();
    }

    @OnClick(R.id.iv_image4)
    public void setIv_image4() {
        presenter.r1g1_checkAnswer(4, currentTeam, false);
        answerPostProcessing();
    }

    public void answerPostProcessing(){

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
                    //TODO display Score screen after final round
                }
            }, 2500);

        }
    }

    @OnClick(R.id.ib_r1g1_speaker)
    public void playQuestion() {
        presenter.replayQuestionroundone();
    }

    @Override
    public void setQuestionImages(final int readQuesNo, Bitmap... bitmaps) {
        questionConter++;
        iv_image1.setImageBitmap(bitmaps[0]);
        iv_image2.setImageBitmap(bitmaps[1]);
        iv_image3.setImageBitmap(bitmaps[2]);
        iv_image4.setImageBitmap(bitmaps[3]);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.readQuestion(readQuesNo);
            }
        }, 1500);
    }
}


