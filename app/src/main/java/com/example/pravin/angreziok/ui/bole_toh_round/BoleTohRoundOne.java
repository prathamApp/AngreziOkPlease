package com.example.pravin.angreziok.ui.bole_toh_round;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.CustomCountDownTimer;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoleTohRoundOne extends BaseFragment implements BoleTohContract.BoleTohRoundOneView{

    @BindView(R.id.mCountDownTimer)
    CountDownTimerView mCountDownTimer;

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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setOnClickListeners();
        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
        customCountDownTimer.startTimer(10000);
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

    @Override
    public void showImages() {

    }
}


