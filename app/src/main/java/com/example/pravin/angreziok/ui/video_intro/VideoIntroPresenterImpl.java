package com.example.pravin.angreziok.ui.video_intro;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Pravin on 19/03/2018.
 */

public class VideoIntroPresenterImpl implements VideoIntroContract.VideoIntroPresenter,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    VideoIntroContract.VideoIntroView videoIntroView;
    Context context;
    VideoView videoView;

    public VideoIntroPresenterImpl(VideoIntroContract.VideoIntroView videoIntroView, Context context, VideoView videoView) {
        this.videoIntroView = videoIntroView;
        this.context = context;
        this.videoView = videoView;
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
    }

    @Override
    public void playVideo(Uri path) {
        MediaController mediaController = new MediaController(context);
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
        videoIntroView.startActivity();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
    }
}
