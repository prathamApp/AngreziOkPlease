package com.example.pravin.angreziok.ui.ChooseLevel;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseLevel extends BaseActivity {

    @BindView(R.id.tv_Level1)
    TextView tv_Level1;
    @BindView(R.id.tv_Level2)
    TextView tv_Level2;
    ArrayList<PlayerModal> playerModalArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");

    }

    @OnClick(R.id.tv_Level1)
    public void startGame(){
        Intent intent = new Intent(this, BoleToh.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("playerModalArrayList", playerModalArrayList);
        intent.putExtras(bundle);
        intent.putExtra("level","L1");
        finish();
        startActivity(intent);

    }

    @OnClick(R.id.tv_Level2)
    public void startGame1(){
        Intent intent = new Intent(this, BoleToh.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("playerModalArrayList", playerModalArrayList);
        intent.putExtras(bundle);
        intent.putExtra("level","L2");
        finish();
        startActivity(intent);

    }

}