package com.example.pravin.angreziok.ui.final_screen;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.dao.SessionDao;
import com.example.pravin.angreziok.dao.StatusDao;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ResultScreen extends BaseActivity {

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

    @BindView(R.id.btn_replay)
    TextView btnReplay;

    @BindView(R.id.konfettiView_result)
    KonfettiView konfettiView;

    ArrayList<PlayerModal> playerModalArrayList;
    int[] rankArr;
    int rank = 1;
    public static AppDatabase appDatabase;

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
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                setCelebrationView();
            }
        }, 1000);

    }

    public void setCelebrationView() {
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(20, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(100, 4000L);
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

    @Override
    public void onBackPressed() {
        quitOrNot("backPress");
    }

    private void quitOrNot(final String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_quit);
        TextView dialogText = dialog.findViewById(R.id.dialog_tv_student_name);
        dialog.setCanceledOnTouchOutside(false);
        Button quitBtn = dialog.findViewById(R.id.dialog_btn_yes);
        Button cancelBtn = dialog.findViewById(R.id.dialog_btn_no);
        ImageView closeBtn = dialog.findViewById(R.id.iv_close_dialog);

        if(msg.equalsIgnoreCase("replay")){
            dialogText.setText("Replay Game ???");
            quitBtn.setText("Replay");
        }

        dialog.show();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(msg.equalsIgnoreCase("replay"))
                    restartGame();
                else
                    quitGame();
            }
        });
    }

    private void quitGame() {
        reInitiateScores();
        endSession();
        Intent qrScan = new Intent(this, QRActivity.class);
        finishAffinity();
        startActivity(qrScan);
    }

    private void endSession() {
        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    appDatabase = Room.databaseBuilder(ResultScreen.this,
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .build();

                    StatusDao statusDao = appDatabase.getStatusDao();
                    SessionDao sessionDao = appDatabase.getSessionDao();
                    String currentSession = statusDao.getValue("CurrentSession");
                    String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                    String sessionToDate = sessionDao.getToDate(currentSession);

                    if(sessionToDate.equalsIgnoreCase("na")) {
                        String timerTime = AOPApplication.getCurrentDateTime(true, AppStartDateTime);
                        appDatabase.getSessionDao().UpdateToDate(currentSession,timerTime);
                    }

                    BackupDatabase.backup(ResultScreen.this);

                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    @OnClick(R.id.btn_replay)
    public void replayVideo() {
        quitOrNot("replay");
    }

    private void reInitiateScores() {
        for (int i = 0; i < playerModalArrayList.size(); i++)
            playerModalArrayList.get(i).setStudentScore("0");
    }

    private void restartGame() {
        reInitiateScores();
        Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("studentList", playerModalArrayList);
        dataConfirmationIntent.putExtras(bundle);
        finishAffinity();
        startActivity(dataConfirmationIntent);
    }
}
