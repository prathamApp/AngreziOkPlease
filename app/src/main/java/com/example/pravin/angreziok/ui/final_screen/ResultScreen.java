package com.example.pravin.angreziok.ui.final_screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.modalclasses.PlayerModal;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultScreen extends AppCompatActivity {

    @BindView(R.id.tv_num_first)
    TextView num1;
    @BindView(R.id.tv_num_second)
    TextView num2;
    @BindView(R.id.tv_num_third)
    TextView num3;
    @BindView(R.id.tv_num_fourth)
    TextView num4;

    @BindView(R.id.tv_team_first)
    LinearLayout megastarLayout;
    @BindView(R.id.tv_team_second)
    LinearLayout rockstarLayout;
    @BindView(R.id.tv_team_third)
    LinearLayout superstarLayout;
    @BindView(R.id.tv_team_four)
    LinearLayout allstarLayout;

    ArrayList<PlayerModal> playerModalArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("PlayerList");
    }
}
