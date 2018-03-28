package com.example.pravin.angreziok.ui.start_menu;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class StartMenu extends BaseActivity implements StartMenuContract.StartMenuView,
        ZXingScannerView.ResultHandler {

    @BindView(R.id.content_frame)
    ViewGroup content_frame;


    private AppDatabase appDatabase;
    StartMenuContract.StartMenuPresenter presenter;
    public ZXingScannerView startCameraScan;
    PlayerModal playerModal;
    List<PlayerModal> playerModalList;
    int totalStudents = 0;

    @Override
    protected void onResume() {
        super.onResume();

        startCameraScan.startCamera();
        startCameraScan.resumeCameraPreview(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        presenter = new StartMenuPresenterImpl(this, this);
        playerModalList = new ArrayList<>();
        initCamera();
        startCameraScan.startCamera();
        startCameraScan.resumeCameraPreview(this);
        /* 1) In case migration needed and no problem with data loss then this would work

        appDatabase =  Room.databaseBuilder(this,
        AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
         */


        /* 2) In case migration needed and want to keep data as it is then this would work
            but the case is
            SQLite supports a limited subset of ALTER TABLE.
             The ALTER TABLE command in SQLite allows the user to rename a table or to add a new column to an existing table.
             It is not possible to rename a column, remove a column, or add or remove constraints from a table.
         */

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
        presenter.displayToast();
    }

    public void initCamera() {
        startCameraScan = new ZXingScannerView(this);
        startCameraScan.setResultHandler(this);
        content_frame.addView((startCameraScan));
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
           /*
           Do migration queries here like below

           database.execSQL("ALTER TABLE Crl "
                    + " ADD COLUMN crl_surname TEXT");
          */
        }
    };

    @OnClick(R.id.btn_admin)
    public void gotoNext() {
    }

    @OnClick(R.id.btn_scanqr)
    public void startQrScan() {
        startCameraScan.startCamera();
        startCameraScan.resumeCameraPreview(this);
    }

    @OnClick(R.id.btn_start_game)
    public void gotoGame() {
        Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
        dataConfirmationIntent.putExtra("totalStudents", totalStudents);
        startActivity(dataConfirmationIntent);
        startActivity(new Intent(StartMenu.this, BoleToh.class));
    }

    @Override
    public void showToast() {
        Toast.makeText(this, "In mainview showToast", Toast.LENGTH_SHORT).show();
    }

    private String[] decodeStudentId(String text, String s) {
        return text.split(s);
    }

    @Override
    public void handleResult(Result result) {
        try {
            startCameraScan.stopCamera();
            Log.d("RawResult:::", "****" + result.getText());

//        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Pattern pattern = Pattern.compile("[A-Za-z0-9]+-[A-Za-z._]{2,50}");

//        Matcher mat = pattern2.matcher("04@-09-vale ketan 009");
            Matcher mat = pattern.matcher(result.getText());

            //"vivek.mitra@gmail.com"
            if (mat.matches()) {
                for (int i = 0; i < playerModalList.size(); i++) {
                    if (playerModalList.get(i).getStudentID().equalsIgnoreCase(decodeStudentId(result.getText(), "-").toString()))
                        Toast.makeText(this, "Already Scaned", Toast.LENGTH_SHORT).show();
                    else {
                        totalStudents++;
                        Toast.makeText(this, "" + totalStudents, Toast.LENGTH_SHORT).show();
                        if (totalStudents < 5) {
                            //Valid pattern
                            String[] id = decodeStudentId(result.getText(), "-");

                            String stdId = id[0];
                            //String stdFirstName = id[1];
                            String[] name = decodeStudentId(id[1], "_");
                            String stdFirstName = name[0];
                            String stdLastName = "";
                            if (name.length > 1)
                                stdLastName = name[1];

                            playerModal.setStudentID(stdId);
                            playerModal.setStudentName(stdFirstName);
                            playerModal.setStudentAlias("");
                            playerModal.setStudentScore("");

                            playerModalList.add(playerModal);

                            if (totalStudents == 4) {

                                Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
                                dataConfirmationIntent.putExtra("totalStudents", totalStudents);
                                startActivity(dataConfirmationIntent);
                                startActivity(new Intent(StartMenu.this, BoleToh.class));
                            }
                        }
                    }
                }
                startCameraScan.startCamera();
                startCameraScan.resumeCameraPreview(this);

            } else {
                BackupDatabase.backup(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}