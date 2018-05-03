package com.example.pravin.angreziok.ui.final_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.modalclasses.PlayerModal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    TextView tvTeam1;
    @BindView(R.id.tv_team_second)
    TextView tvTeam2;
    @BindView(R.id.tv_team_third)
    TextView tvTeam3;
    @BindView(R.id.tv_team_fourth)
    TextView tvTeam4;

    @BindView(R.id.tv_score_first)
    TextView tvScore1;
    @BindView(R.id.tv_score_second)
    TextView tvScore2;
    @BindView(R.id.tv_score_third)
    TextView tvScore3;
    @BindView(R.id.tv_score_fourth)
    TextView tvScore4;

    ArrayList<PlayerModal> playerModalArrayList;
    int[] rankArr;
    int rank = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("PlayerList");

        Collections.sort(playerModalArrayList, new myScoreComp());

        for (int i = 0; i < playerModalArrayList.size(); i++) {
            Log.d("ResultScreen", "             SCORE: " + playerModalArrayList.get(i).getStudentScore() + "      ALIAS: " + playerModalArrayList.get(i).getStudentAlias());
        }

        showResult();

    }

    private void showResult() {

        rankArr = new int[playerModalArrayList.size()];

        for (int i = 0; i < playerModalArrayList.size(); i++) {
            if (i > 0) {
                if (!playerModalArrayList.get(i - 1).getStudentScore().equalsIgnoreCase(playerModalArrayList.get(i).getStudentScore())) {
                    rank += 1;
                    rankArr[i] = rank;
                } else {
                    rankArr[i] = rank;
                }
            } else
                rankArr[i] = rank;

            switch (i) {
                case 0:
                    num1.setText("#" + rankArr[i]);
                    num1.setVisibility(View.VISIBLE);
                    tvScore1.setVisibility(View.VISIBLE);
                    tvScore1.setText(playerModalArrayList.get(i).getStudentScore());
                    tvTeam1.setVisibility(View.VISIBLE);
                    tvTeam1.setText(playerModalArrayList.get(i).getStudentName() + " : " + playerModalArrayList.get(i).getStudentAlias());
                    break;
                case 1:
                    num2.setText("#" + rankArr[i]);
                    num2.setVisibility(View.VISIBLE);
                    tvScore2.setVisibility(View.VISIBLE);
                    tvScore2.setText(playerModalArrayList.get(i).getStudentScore());
                    tvTeam2.setVisibility(View.VISIBLE);
                    tvTeam2.setText(playerModalArrayList.get(i).getStudentName() + " : " + playerModalArrayList.get(i).getStudentAlias());
                    break;
                case 2:
                    num3.setText("#" + rankArr[i]);
                    num3.setVisibility(View.VISIBLE);
                    tvScore3.setVisibility(View.VISIBLE);
                    tvScore3.setText(playerModalArrayList.get(i).getStudentScore());
                    tvTeam3.setVisibility(View.VISIBLE);
                    tvTeam3.setText(playerModalArrayList.get(i).getStudentName() + " : " + playerModalArrayList.get(i).getStudentAlias());
                    break;
                case 3:
                    num4.setText("#" + rankArr[i]);
                    num4.setVisibility(View.VISIBLE);
                    tvScore4.setVisibility(View.VISIBLE);
                    tvScore4.setText(playerModalArrayList.get(i).getStudentScore());
                    tvTeam4.setVisibility(View.VISIBLE);
                    tvTeam4.setText(playerModalArrayList.get(i).getStudentName() + " : " + playerModalArrayList.get(i).getStudentAlias());
                    break;
            }
        }

    }

    class myScoreComp implements Comparator<PlayerModal> {
        @Override
        public int compare(PlayerModal p1, PlayerModal p2) {
            if (Integer.parseInt(p1.getStudentScore()) < Integer.parseInt(p2.getStudentScore())) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
