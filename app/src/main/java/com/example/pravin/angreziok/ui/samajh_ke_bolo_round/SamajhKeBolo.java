package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.custom.GifView;
import com.example.pravin.angreziok.dao.SessionDao;
import com.example.pravin.angreziok.dao.StatusDao;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.interfaces.MediaCallbacks;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;
import com.example.pravin.angreziok.util.MediaPlayerUtil;
import com.example.pravin.angreziok.util.PD_Utility;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SamajhKeBolo extends BaseActivity implements SamajhKeBoloContract.SamajhKeBoloView, MediaCallbacks {

    SamajhKeBoloContract.SamajhKeBoloPresenter presenter;
    static ArrayList<PlayerModal> playerModalArrayList;
    String charIntroPath;
    MediaPlayerUtil mediaPlayerUtil;

    @BindView(R.id.round_intro_gifview)
    GifView introGifView;
    @BindView(R.id.skip_button_intro)
    Button btn_skip;
    static ArrayList<Integer> list = new ArrayList<Integer>();
    static int gameCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samajh_ke_bolo);
        ButterKnife.bind(this);
        btn_skip.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");
        for (int i = 0; i < 3; i++)
            list.add(new Integer(i));
        Collections.shuffle(list);
        Log.d("games", "\n\n\nShuffle: \n\n\n");
        for (int i = 0; i < list.size(); i++)
            Log.d("games", "gamesNo: " + list.get(i));

        Collections.shuffle(playerModalArrayList);
        for (int i = 0; i < playerModalArrayList.size(); i++)
            Log.d("SamajhKeBoloTAG", "PlayerList: " + playerModalArrayList.get(i).getStudentAlias());

        // TODO post video in videos folder
        charIntroPath = PD_Utility.getExternalPath(this) + "charactersGif/";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: " + charIntroPath);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showGif(charIntroPath);
            }
        }, 500);
        presenter = new SamajhKeBoloPresenterImpl(this);
    }

    private void showGif(String charIntroPath) {
        try {
            btn_skip.setVisibility(View.VISIBLE);
            InputStream gif = new FileInputStream(charIntroPath + "Samaz-ke-bolo-Round-Intro.gif");
            introGifView.setGifResource(gif);
            playSound(charIntroPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(String charIntroPath) {
        try {
            Log.d("SoundPth", "playMusic: " + charIntroPath + "Samaz-ke-bolo-Round-Intro.wav");
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(this);
                mediaPlayerUtil.initCallback(SamajhKeBolo.this);
            }
            mediaPlayerUtil.playMedia(charIntroPath + "Samaz-ke-bolo-Round-Intro.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.skip_button_intro)
    public void startGame() {
        if (mediaPlayerUtil != null)
            mediaPlayerUtil.pauseMedia();
        loadFragment();
    }

    @Override
    public void loadFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("round", "R3");
        bundle.putInt("count", list.get(gameCounter));
        PD_Utility.showFragment(SamajhKeBolo.this, new fragment_intro_character(), R.id.cl_samajh_ke_bolo,
                bundle, fragment_intro_character.class.getSimpleName());
    }

    public static void animateView(View view, Context context) {
        Animation rubber = AnimationUtils.loadAnimation(context, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    @Override
    public void onComplete() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFragment();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        quitOrNot();
    }

    private void quitOrNot() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_quit);
        TextView dialogText = dialog.findViewById(R.id.dialog_tv_student_name);
        dialog.setCanceledOnTouchOutside(false);
        Button replayBtn = dialog.findViewById(R.id.dialog_btn_yes);
        Button quitBtn = dialog.findViewById(R.id.dialog_btn_no);
        ImageView closeBtn = dialog.findViewById(R.id.iv_close_dialog);

        replayBtn.setText("Replay");
        quitBtn.setText("Quit");
        dialogText.setText("What do you wish to do???");

        dialog.show();


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerUtil != null)
                    mediaPlayerUtil.pauseMedia();
                dialog.dismiss();
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerUtil != null)
                    mediaPlayerUtil.pauseMedia();
                dialog.dismiss();
                restartGame();
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerUtil != null)
                    mediaPlayerUtil.pauseMedia();
                dialog.dismiss();
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
                    AppDatabase appDatabase;
                    appDatabase = Room.databaseBuilder(SamajhKeBolo.this,
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

                    BackupDatabase.backup(SamajhKeBolo.this);

                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
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
