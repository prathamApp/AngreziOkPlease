package com.example.pravin.angreziok.ui.jod_tod_round;

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

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTod extends BaseActivity implements JodTodContract.JodTodView, MediaCallbacks {

    @BindView(R.id.skip_button_intro)
    Button btn_skip;

    static ArrayList<PlayerModal> jodTodPlayerList;
    String charIntroPath;
    static String gameLevel;
    MediaPlayerUtil mediaPlayerUtil;

    @BindView(R.id.round_intro_gifview)
    GifView introGifView;
    JodTodContract.JodTodPresenter presenter;
    static ArrayList<Integer> list = new ArrayList<Integer>();
    static int gameCounter = 0;
    static boolean dialogFlag=false;
    Boolean pauseFlg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jod_tod);
        ButterKnife.bind(this);
        btn_skip.setVisibility(View.GONE);
        gameCounter = 0;

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        jodTodPlayerList = extraBundle.getParcelableArrayList("playerModalArrayList");
        gameLevel = intent.getStringExtra("level");
        gameCounter = 0;
        list.clear();
        for (int i = 0; i < 3; i++)
            list.add(i);
        Collections.shuffle(list);
        Log.d("games", "\n\n\nShuffle: \n\n\n");
        for (int i = 0; i < list.size(); i++)
            Log.d("games", "gamesNo: " + list.get(i));


        Collections.shuffle(jodTodPlayerList);
        for (int i = 0; i < jodTodPlayerList.size(); i++)
            Log.d("JodTodTAG", "jodTodPlayerList: " + jodTodPlayerList.get(i).getStudentAlias());

        charIntroPath = PD_Utility.getExternalPath(this) + "charactersGif/";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: " + charIntroPath);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showGif(charIntroPath);
            }
        }, 100);
        presenter = new JodTodPresenterImpl(this);
    }

    private void showGif(String charIntroPath) {
        try {
            btn_skip.setVisibility(View.VISIBLE);
            InputStream gif = new FileInputStream(charIntroPath + "Jod_Tod_Round-Intro.gif");
            introGifView.setGifResource(gif);
            playSound(charIntroPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playSound(String charIntroPath) {
        try {
            Log.d("SoundPth", "playMusic: " + charIntroPath + "Jod_Tod_Round-Intro.wav");
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(this);
                mediaPlayerUtil.initCallback(JodTod.this);
            }
            mediaPlayerUtil.playMedia(charIntroPath + "Jod_Tod_Round-Intro.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.skip_button_intro)
    public void startGame(View view) {
        if (mediaPlayerUtil != null)
            mediaPlayerUtil.pauseMedia();
        view.setClickable(false);
        loadFragment();
    }

    public static void animateView(View view, Context context) {
        Animation rubber = AnimationUtils.loadAnimation(context, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    @Override
    public void loadFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("round", "R2");
        bundle.putString("level", gameLevel);
        bundle.putInt("count", list.get(gameCounter));
        PD_Utility.showFragment(JodTod.this, new fragment_intro_character(), R.id.cl_jod_tod,
                bundle, fragment_intro_character.class.getSimpleName());
    }


    @Override
    public void onComplete() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pauseFlg = false;
                loadFragment();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        if(!dialogFlag)
            quitOrNot();
    }

    private void quitOrNot() {
        dialogFlag=true;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_quit);
        TextView dialogText = dialog.findViewById(R.id.dialog_tv_student_name);
        dialog.setCancelable(false);
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
                dialogFlag=false;
                dialog.dismiss();
            }
        });

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerUtil != null)
                    mediaPlayerUtil.pauseMedia();
                dialogFlag=false;
                dialog.dismiss();
                restartGame();
            }
        });

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerUtil != null)
                    mediaPlayerUtil.pauseMedia();
                dialogFlag=false;
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
                    appDatabase = Room.databaseBuilder(JodTod.this,
                            AppDatabase.class, AppDatabase.DB_NAME)
                            .build();

                    StatusDao statusDao = appDatabase.getStatusDao();
                    SessionDao sessionDao = appDatabase.getSessionDao();
                    String currentSession = statusDao.getValue("CurrentSession");
                    String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                    String sessionToDate = sessionDao.getToDate(currentSession);

                    if (sessionToDate.equalsIgnoreCase("na")) {
                        String timerTime = AOPApplication.getCurrentDateTime(true, AppStartDateTime);
                        appDatabase.getSessionDao().UpdateToDate(currentSession, timerTime);
                    }

                    BackupDatabase.backup(JodTod.this);

                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.execute();
    }

    private void reInitiateScores() {
        for (int i = 0; i < jodTodPlayerList.size(); i++)
            jodTodPlayerList.get(i).setStudentScore("0");
    }

    private void restartGame() {
        reInitiateScores();
        Intent dataConfirmationIntent = new Intent(this, DataConfirmation.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("playerModalArrayList", jodTodPlayerList);
        dataConfirmationIntent.putExtras(bundle);
        finishAffinity();
        startActivity(dataConfirmationIntent);
    }

    @Override
    public void onPause() {
        pauseFlg = true;
        if (mediaPlayerUtil != null)
            mediaPlayerUtil.pauseMedia();
        super.onPause();
    }

    @Override
    public void onResume() {
        try {
            if (pauseFlg && !DataConfirmation.fragmentPauseFlg) {
                mediaPlayerUtil.resumeMedia();
            }
        } catch (Exception e) {
        }
        pauseFlg = false;
        super.onResume();
    }
}
