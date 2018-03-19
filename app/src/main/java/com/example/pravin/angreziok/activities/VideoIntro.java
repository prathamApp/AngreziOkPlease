package com.example.pravin.angreziok.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.util.SDCardUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoIntro  extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    @BindView(R.id.intro_videoView)
    VideoView videoView;
    String videoPath,final_sd_path;
    static String sdCardPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_intro);
        ButterKnife.bind(this);

        ArrayList<String> sdcard_path= SDCardUtil.getExtSdCardPaths(VideoIntro.this);
        for (String path:sdcard_path){
            final_sd_path = path;
            if(new File(final_sd_path + "/.AOP_External").exists()) {
                sdCardPath = final_sd_path + "/.AOP_External/";
                break;
            }
        }

        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoPath = sdCardPath + "Videos/intro.mp4";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playVideo(Uri.parse(videoPath));
    }

    public void playVideo(Uri path) {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        try {
            videoView.setVideoURI(path);
        } catch (Exception e) {
            Log.e("Cant Play Video", e.getMessage());
            e.printStackTrace();
        }
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Runtime rs = Runtime.getRuntime();
        rs.freeMemory();
        rs.gc();
        rs.freeMemory();
        this.finish();

        Intent intent = new Intent(this, StudentToTeamMapping.class);
        ((Activity) this).startActivity(intent);    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
    }
}
