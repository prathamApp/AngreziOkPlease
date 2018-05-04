package com.example.pravin.angreziok.ui.start_menu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Attendance;
import com.example.pravin.angreziok.domain.Session;
import com.example.pravin.angreziok.domain.Student;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.admin_console.AdminConsole;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;
import com.example.pravin.angreziok.ui.tab_usage.TabUsage;
import com.example.pravin.angreziok.ui.video_intro.VideoIntro;
import com.example.pravin.angreziok.util.SDCardUtil;
import com.google.zxing.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRActivity extends BaseActivity implements QRContract.StartMenuView,
        ZXingScannerView.ResultHandler {

    @BindView(R.id.content_frame)
    ViewGroup content_frame;
    @BindView(R.id.tv_stud_one)
    TextView tv_stud_one;
    @BindView(R.id.tv_stud_two)
    TextView tv_stud_two;
    @BindView(R.id.tv_stud_three)
    TextView tv_stud_three;
    @BindView(R.id.tv_stud_four)
    TextView tv_stud_four;
    @BindView(R.id.btn_start_game)
    Button btn_start_game;


    private AppDatabase appDatabase;
    QRContract.StartMenuPresenter presenter;
    PlayerModal playerModal;
    int totalStudents = 0;
    Dialog dialog;
    Boolean setStud = false;
    ArrayList<PlayerModal> playerModalList;
    public ZXingScannerView mScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(R.layout.activity_start_menu);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        presenter = new QRPresenterImpl(this, this);
        playerModalList = new ArrayList<>();
        mScannerView.setResultHandler(this);
        content_frame.addView((mScannerView));

        initCamera();
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
//                .addMigrations(MIGRATION_1_2)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mScannerView.stopCamera();
    }

    public void initCamera() {
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);
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

    @OnClick(R.id.btn_stats)
    public void showStudentsUsage() {
        startActivity(new Intent(this, TabUsage.class));
    }

    @OnClick(R.id.btn_reset_btn)
    public void resetQrList() {

        playerModalList.clear();
        totalStudents = 0;

        tv_stud_one.setText("");
        tv_stud_two.setText("");
        tv_stud_three.setText("");
        tv_stud_four.setText("");

        tv_stud_one.setVisibility(View.GONE);
        tv_stud_two.setVisibility(View.GONE);
        tv_stud_three.setVisibility(View.GONE);
        tv_stud_four.setVisibility(View.GONE);

        scanNextQRCode();
    }

    @OnClick(R.id.iv_admin)
    public void gotoNext() {
        showCrlDialog();
    }

    @OnClick(R.id.btn_start_game)
    public void gotoGame() {
        mScannerView.stopCamera();
        Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("studentList", playerModalList);
        dataConfirmationIntent.putExtras(bundle);
        startActivity(dataConfirmationIntent);
    }

    @Override
    public void showToast() {
        Toast.makeText(this, "In mainview showToast", Toast.LENGTH_SHORT).show();
    }

    private String[] decodeStudentId(String text, String s) {
        return text.split(s);
    }

    public void scanNextQRCode() {
        if (mScannerView != null) {
            mScannerView.stopCamera();
            mScannerView.startCamera();
            mScannerView.resumeCameraPreview(this);
        }
    }

    public void showQrDialog(String studentName) {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_for_qrscan);
        dialog.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);
        TextView text = (TextView) dialog.findViewById(R.id.dialog_tv_student_name);
        text.setText("Hi " + studentName);

        dialog.show();

        Button scanNextQR = (Button) dialog.findViewById(R.id.dialog_btn_scan_qr);

        scanNextQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogClick();
            }
        });

    }

    public void showCrlDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_crl_login);
        dialog.setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);

        final TextView userName = (TextView) dialog.findViewById(R.id.dialog_et_uname);
        final TextView password = (TextView) dialog.findViewById(R.id.dialog_et_pwd);
        Button login = (Button) dialog.findViewById(R.id.dialog_btn_login);
        Button cancle = (Button) dialog.findViewById(R.id.dialog_btn_cancle);
        ImageView cross = (ImageView) dialog.findViewById(R.id.iv_close_dialog);
        dialog.show();

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                String userNameString = ""+userName.getText();
                String passwordString = ""+password.getText();

                if(userNameString.equalsIgnoreCase(passwordString)){
                    Intent Intent = new Intent(QRActivity.this, AdminConsole.class);
                    startActivity(Intent);
                }
            }
        });
    }

    public void dialogClick() {

        if (totalStudents == 4) {
            showStudentName(totalStudents);
/*                    Intent dataConfirmationIntent = new Intent(QRActivity.this, DataConfirmation.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("studentList", playerModalList);
                    dataConfirmationIntent.putExtras(bundle);
                    startActivity(dataConfirmationIntent);*/
        } else {
            if (setStud) {
                setStud = false;
                showStudentName(totalStudents);
            }
            scanNextQRCode();
        }
    }

    private void showStudentName(int totalStudents) {

        switch (totalStudents) {
            case 1:
                tv_stud_one.setVisibility(View.VISIBLE);
                tv_stud_one.setText("" + playerModalList.get(0).getStudentName());
                break;
            case 2:
                tv_stud_two.setVisibility(View.VISIBLE);
                tv_stud_two.setText("" + playerModalList.get(1).getStudentName());
                break;
            case 3:
                tv_stud_three.setVisibility(View.VISIBLE);
                tv_stud_three.setText("" + playerModalList.get(2).getStudentName());
                break;
            case 4:
                tv_stud_four.setVisibility(View.VISIBLE);
                tv_stud_four.setText("" + playerModalList.get(3).getStudentName());
                break;
        }
    }

    @Override
    public void handleResult(Result result) {
        try {
            boolean dulicateQR = false;
            mScannerView.stopCamera();
            Log.d("RawResult:::", "****" + result.getText());

//        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
//        Matcher mat = pattern2.matcher("04@-09-vale ketan 009");
            Pattern pattern = Pattern.compile("[A-Za-z0-9]+-[A-Za-z._]{2,50}");
            Matcher mat = pattern.matcher(result.getText());

            if (mat.matches()) {
                btn_start_game.setVisibility(View.VISIBLE);
                if (playerModalList.size() <= 0)
                    qrEntryProcess(result);
                else {
                    for (int i = 0; i < playerModalList.size(); i++) {
                        String[] currentIdArr = decodeStudentId(result.getText(), "-");
                        String currId = currentIdArr[0];
                        if (playerModalList.get(i).getStudentID().equalsIgnoreCase("" + currId)) {
                            Toast.makeText(this, "Already Scanned", Toast.LENGTH_SHORT).show();
                            showQrDialog(", This QR Was Already Scanned");
                            setStud = false;
                            dulicateQR = true;
                            break;
                        }
                    }
                    if (!dulicateQR) {
                        qrEntryProcess(result);
                    }
                }
            } else {
                scanNextQRCode();
                BackupDatabase.backup(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void qrEntryProcess(Result result) {
        totalStudents++;
        String sid = "", sname = "", sscore = "", salias = "";
        playerModal = new PlayerModal(sid, sname, sscore, salias);
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
            playerModal.setStudentScore("0");
            playerModal.setStudentAlias("");

            playerModalList.add(playerModal);
            enterStudentData(stdId,stdFirstName);
            //scanNextQRCode();
            setStud = true;
            showQrDialog(stdFirstName);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void enterStudentData(final String stdId, final String stdFirstName) {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Student student = new Student();
                    Attendance attendance = new Attendance();

                    student.setStudentID(""+stdId);
                    student.setFirstName(""+stdFirstName);
                    student.setNewFlag(1);
                    String studentName = appDatabase.getStudentDao().checkStudent(""+stdId);

                    String currentSession = appDatabase.getStatusDao().getValue("CurrentSession");
                    attendance.setSessionID(""+currentSession);
                    attendance.setStudentID(""+stdId);

                    if(studentName == null) {
                        appDatabase.getStudentDao().insert(student);
                    }

                    appDatabase.getAttendanceDao().insert(attendance);

                    BackupDatabase.backup(QRActivity.this);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }
}