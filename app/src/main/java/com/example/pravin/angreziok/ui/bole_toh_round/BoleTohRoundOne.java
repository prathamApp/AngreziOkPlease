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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    int questionConter=0;

    //    ArrayList <GenericModalGson> gsonPicGameData = new ArrayList<GenericModalGson>();
    BoleTohContract.BoleTohPresenter presenter;
    String path;
    static int speechCount;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new BoleTohPresenterImpl(getActivity(), this, BoleToh.playtts);
        setOnClickListeners();
        path = presenter.getSdcardPath();
        presenter.doInitialWork(path);
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

    public void showQrDialog() {
        speechCount = 0;
        String teamName = playerModalArrayList.get(currentTeam).getStudentAlias();
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = dialog.findViewById(R.id.dialog_tv_student_name);
        ImageView iv_close = dialog.findViewById(R.id.dialog_iv_close);
        text.setText("Next question would be for " + teamName);
        dialog.show();

        Button scanNextQR = dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initiateQuestion();
                presenter.setImage_r1g2();
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initiateQuestion();
                presenter.setImage_r1g2();
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
        presenter.r1g1_checkAnswer(1, path,questionConter);
    }

    @OnClick(R.id.iv_image2)
    public void setIv_image2() {
        presenter.r1g1_checkAnswer(2, path,questionConter);
    }

    @OnClick(R.id.iv_image3)
    public void setIv_image3() {
        presenter.r1g1_checkAnswer(3, path,questionConter);
    }

    @OnClick(R.id.iv_image4)
    public void setIv_image4() {
        presenter.r1g1_checkAnswer(4, path,questionConter);
    }

    @OnClick(R.id.ib_r1g1_speaker)
    public void playQuestion(){presenter.replayQuestionroundone();}

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
                mCountDownTimer.start(20000);
                BoleToh.animateView(mCountDownTimer,getActivity());
            }
        },1500);
    }
}


