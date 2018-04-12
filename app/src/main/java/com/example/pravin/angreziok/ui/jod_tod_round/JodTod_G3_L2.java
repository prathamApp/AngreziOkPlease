package com.example.pravin.angreziok.ui.jod_tod_round;

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

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.example.pravin.angreziok.BaseActivity.ttsService;
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
    @BindView(R.id.tv_r1g1_question)
    TextView showQuestion;
    @BindView(R.id.konfettiView_g1_l2)
    KonfettiView konfettiView;

    String text;
    JodTodContract.JodTodPresenter presenter;
    int currentTeam;
    Dialog dialog;

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

        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initiateQuestion();
                // TODO
                //presenter.setImage_gl_l2();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initiateQuestion();
                // TODO
//                presenter.setImage_gl_l2();
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
    public void initiateQuestion() {
        // TODO
//        startTimer();
//        playTTS();
    }

    private void setDataForGame() {
//        TODO
//        String path = presenter.getSdcardPath();
//        presenter.set_g1_l2_data(path);
    }

    private void playTTS() {
//        TODO
//        text = "What is This?";
//        showQuestion.setText(text);
//        presenter.startTTS(text);
    }

    private void startTimer() {
        mCountDownTimer.ready();
        mCountDownTimer.start(15000);
        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
//                TODO
//                submitAns();
            }
        });
        JodTod.animateView(mCountDownTimer, getActivity());
    }

    @OnClick(R.id.ib_g1_l2_speaker)
    public void soundClicked() {
        presenter.startTTS(text);
    }
}