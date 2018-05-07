package com.example.pravin.angreziok.ui.start_data_confirmation;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.dao.SessionDao;
import com.example.pravin.angreziok.dao.StatusDao;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataConfirmation extends BaseActivity implements DataConfirmationContract.DataConfirmationView {

    ArrayList<PlayerModal> playerModalArrayList;
    String temp = "LIST \n";
    int playerCount = 0;
    ArrayList<String> teamNames;

    @BindView(R.id.tv_student_one)
    TextView studentOne;
    @BindView(R.id.tv_student_two)
    TextView studentTwo;
    @BindView(R.id.tv_student_three)
    TextView studentThree;
    @BindView(R.id.tv_student_four)
    TextView studentFour;

    @BindView(R.id.tv_team_one)
    TextView teamOne;
    @BindView(R.id.tv_team_two)
    TextView teamTwo;
    @BindView(R.id.tv_team_three)
    TextView teamThree;
    @BindView(R.id.tv_team_four)
    TextView teamFour;
    public static AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_confirmation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        teamNames = new ArrayList<String>();

        teamNames.add("Rockstars");
        teamNames.add("Megastars");
        teamNames.add("Superstars");
        teamNames.add("Allstars");

        playerModalArrayList = extraBundle.getParcelableArrayList("studentList");
        playerCount = playerModalArrayList.size();
        setStudentsAndTeams();

    }

    public void setStudentsAndTeams() {
        Collections.shuffle(teamNames);

        for (int i = 0; i < playerCount; i++) {
            if (i == 0) {
                studentOne.setVisibility(View.VISIBLE);
                studentOne.setText("" + playerModalArrayList.get(i).studentName);
                teamOne.setVisibility(View.VISIBLE);
                teamOne.setText("" + teamNames.get(i));
            }
            if (i == 1) {
                studentTwo.setVisibility(View.VISIBLE);
                studentTwo.setText("" + playerModalArrayList.get(i).studentName);
                teamTwo.setVisibility(View.VISIBLE);
                teamTwo.setText("" + teamNames.get(i));
            }
            if (i == 2) {
                studentThree.setVisibility(View.VISIBLE);
                studentThree.setText("" + playerModalArrayList.get(i).studentName);
                teamThree.setVisibility(View.VISIBLE);
                teamThree.setText("" + teamNames.get(i));
            }
            if (i == 3) {
                studentFour.setVisibility(View.VISIBLE);
                studentFour.setText("" + playerModalArrayList.get(i).studentName);
                teamFour.setVisibility(View.VISIBLE);
                teamFour.setText("" + teamNames.get(i));
            }
        }
    }

    @OnClick({R.id.tv_startgame,R.id.ll_startgame})
    public void startGame(){
        for(int i=0; i<playerCount; i++) {

            playerModalArrayList.get(i).setStudentAlias(""+teamNames.get(i));
            Log.d("DataConfirmationTAG", "StudNames: "+playerModalArrayList.get(i).getStudentName()+"  TeamName: "+playerModalArrayList.get(i).getStudentAlias());

            Intent intent = new Intent(this, SamajhKeBolo.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("playerModalArrayList", playerModalArrayList);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    }

    private void endSession() {
        new AsyncTask<Object, Void, Object>() {

            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    appDatabase = Room.databaseBuilder(DataConfirmation.this,
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

                    BackupDatabase.backup(DataConfirmation.this);

                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }


    @Override
    public void onBackPressed() {
        endSession();
        Intent dataConfirmationIntent = new Intent(this, QRActivity.class);
        this.finish();
        startActivity(dataConfirmationIntent);
    }

    @OnClick({R.id.ll_changeTeam, R.id.tv_change_team})
    public void shuffleTeams() {
        setStudentsAndTeams();
    }
}
