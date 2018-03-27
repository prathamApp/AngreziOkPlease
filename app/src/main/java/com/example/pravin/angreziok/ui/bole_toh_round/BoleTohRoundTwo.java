package com.example.pravin.angreziok.ui.bole_toh_round;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.CustomCountDownTimer;
import com.example.pravin.angreziok.ui.GifView;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class BoleTohRoundTwo extends BaseFragment implements BoleTohContract.BoleTohRoundTwoView{

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;
    @BindView(R.id.iv_character_dialog_gif)
    GifView gifView;

    String text;
    BoleTohContract.BoleTohPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bole_toh_round_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new BoleTohPresenterImpl(getActivity(),this,BoleToh.playtts);

        setTimerCallBack();
        
        gifView.setGifResource(R.drawable.anupam_well_done);
        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
        customCountDownTimer.startTimer(30000);
        text = "What is he doing?";
        presenter.startTTS(text);
        BoleToh.animateView(mCountDownTimer,getActivity());
    }

    @OnClick(R.id.ib_r1g2_speaker)
    public void startTTS(){
        presenter.startTTS(text);
    }

    @OnClick(R.id.ib_r1g2_mic)
    public void startSTT(){
        presenter.startSTT(getActivity());
    }

    private void setTimerCallBack() {
        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                Toast.makeText(getActivity(), "finish", Toast.LENGTH_SHORT).show();
                mCountDownTimer.success();
            }
        });
    }
}


