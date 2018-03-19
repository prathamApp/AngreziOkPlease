package com.example.pravin.angreziok.ui.video_intro;

import android.net.Uri;

/**
 * Created by Pravin on 19/03/2018.
 */

public interface VideoIntroContract {

    public interface VideoIntroView{
        void startActivity();
    }

    public interface VideoIntroPresenter {
        void playVideo(Uri path);
    }
}
