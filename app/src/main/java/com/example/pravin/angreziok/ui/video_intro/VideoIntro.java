package com.example.pravin.angreziok.ui.video_intro;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.VideoView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;
import com.example.pravin.angreziok.util.PD_Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoIntro extends BaseActivity implements VideoIntroContract.VideoIntroView {

    @BindView(R.id.intro_videoView)
    VideoView videoView;

    String videoPath;
    private AppDatabase appDatabase;
    VideoIntroContract.VideoIntroPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intro);
        ButterKnife.bind(this);
        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        presenter = new VideoIntroPresenterImpl(this, this, videoView);
        videoPath = PD_Utility.getExternalPath(this) + "Videos/intro.mp4";
        PD_Utility.showLog("ext_path::", videoPath);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter.playVideo(Uri.parse(videoPath));

        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.d(":::",""+appDatabase.getCrlDao().getAllCrls().size());
                BackupDatabase.backup(VideoIntro.this);
                return null;
            }
        }.execute();
    }

    @OnClick(R.id.skip_button)
    public void skipVideo() {
        videoView.pause();
        videoView.stopPlayback();
        startActivity();
/*        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installIntent);*/
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(this, QRActivity.class));
        finish();
    }

}
