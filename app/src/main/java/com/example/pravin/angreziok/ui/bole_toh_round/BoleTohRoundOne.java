package com.example.pravin.angreziok.ui.bole_toh_round;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BoleTohRoundOne extends BaseFragment implements BoleTohContract.BoleTohRoundOneView {

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
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

    //    ArrayList <GenericModalGson> gsonPicGameData = new ArrayList<GenericModalGson>();
    BoleTohContract.BoleTohPresenter presenter;
    String path;

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
/*        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
        customCountDownTimer.startT`imer(10000);*/
    }

    private void setOnClickListeners() {
        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "finish", Toast.LENGTH_SHORT).show();
                mCountDownTimer.success();
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
        presenter.checkAnswer(1, path);
    }

    @OnClick(R.id.iv_image2)
    public void setIv_image2() {
        presenter.checkAnswer(2, path);
    }

    @OnClick(R.id.iv_image3)
    public void setIv_image3() {
        presenter.checkAnswer(3, path);
    }

    @OnClick(R.id.iv_image4)
    public void setIv_image4() {
        presenter.checkAnswer(4, path);
    }

    @OnClick(R.id.ib_r1g1_speaker)
    public void playQuestion(){}

    @Override
    public void setQuestionImages(final int readQuesNo, Bitmap... bitmaps) {
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
        },1500);
    }
}


