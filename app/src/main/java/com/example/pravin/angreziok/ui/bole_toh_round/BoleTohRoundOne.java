package com.example.pravin.angreziok.ui.bole_toh_round;

import android.os.Bundle;
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
import com.example.pravin.angreziok.ui.CustomCountDownTimer;
import com.example.pravin.angreziok.util.PD_Utility;
import com.github.anastr.flattimelib.CountDownTimerView;
import com.github.anastr.flattimelib.intf.OnTimeFinish;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoleTohRoundOne extends BaseFragment implements BoleTohContract.BoleTohRoundOneView{

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
    @BindView(R.id.ib_speaker)
    ImageButton ib_speaker;

    @BindView(R.id.bt_temp_skip)
    Button bt_temp_skip;



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
        doInitialWork();
/*        CustomCountDownTimer customCountDownTimer = new CustomCountDownTimer(mCountDownTimer,getActivity());
        customCountDownTimer.startTimer(10000);*/
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

        bt_temp_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PD_Utility.showFragment(getActivity(), new BoleTohRoundTwo(), R.id.cl_bole_toh,
                        null, BoleTohRoundTwo.class.getSimpleName());
            }
        });

    }

    @Override
    public int[] getUniqueRandomNumber(int min, int max, int numSize) {
        int[] tempArray;
        if ((max - min) > numSize) {
            tempArray = new int[numSize];
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int i = min; i < max; i++)
                list.add(new Integer(i));

            Collections.shuffle(list);
            for (int i = 0; i < numSize; i++) {
                System.out.println("===== : " + list.get(i));
                tempArray[i] = list.get(i);
            }
            return tempArray;
        } else
            return null;
    }

    @Override
    public void doInitialWork() {

    }

    @Override
    public void showImages() {

    }
}


