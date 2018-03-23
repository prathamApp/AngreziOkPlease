package com.example.pravin.angreziok.ui;

import android.content.Context;
import android.widget.Toast;

import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

/**
 * Created by Pravin on 23/03/2018.
 */

public class CustomCountDownTimer {
    CountDownTimerView mCountDownTimer;
    Context mContext;

    public CustomCountDownTimer(CountDownTimerView mCountDownTimer,Context mContext){
        this.mContext = mContext;
        this.mCountDownTimer = mCountDownTimer;
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        mCountDownTimer.setOnTimeFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
                Toast.makeText(mContext, "finish", Toast.LENGTH_SHORT).show();
                mCountDownTimer.success();
            }
        });

        mCountDownTimer.setOnEndAnimationFinish(new OnTimeFinish() {
            @Override
            public void onFinish() {
            }
        });

    }

    public void pauseTimer() {
        mCountDownTimer.pause();
    }

    public void continueTimer() {
        mCountDownTimer.resume();
    }

    public void stopTimer() {
        mCountDownTimer.stop();
    }

    public void startTimer(long time) {
        mCountDownTimer.start(time);
    }

    public void displaySuccess() {
        mCountDownTimer.success();
    }

    public void displayFailed() {
        mCountDownTimer.failure();
    }

    public void readyTimer() {
        mCountDownTimer.ready();
    }
}
