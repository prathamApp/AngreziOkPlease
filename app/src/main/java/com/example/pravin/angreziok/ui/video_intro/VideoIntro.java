package com.example.pravin.angreziok.ui.video_intro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.WindowManager;
import android.widget.VideoView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.start_menu.StartMenu;
import com.example.pravin.angreziok.util.PD_Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoIntro extends BaseActivity implements VideoIntroContract.VideoIntroView {

    @BindView(R.id.intro_videoView)
    VideoView videoView;

    String videoPath;


    VideoIntroContract.VideoIntroPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intro);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        presenter = new VideoIntroPresenterImpl(this, this, videoView);
        videoPath = PD_Utility.getExternalPath(this) + "Videos/intro.mp4";
        PD_Utility.showLog("ext_path::", videoPath);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter.playVideo(Uri.parse(videoPath));
    }

    @OnClick(R.id.skip_button)
    public void skipVideo() {
        startActivity();
/*        Intent installIntent = new Intent();
        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        startActivity(installIntent);*/
    }

    @Override
    public void startActivity() {
        finish();
        startActivity(new Intent(this, StartMenu.class));
    }

}
