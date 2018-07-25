package com.example.pravin.angreziok.ui.start_menu;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.util.MediaPlayerUtil;


/**
 * Created by Pravin on 19/03/2018.
 */

public class QRPresenterImpl implements QRContract.StartMenuPresenter {

    Context mContext;
    QRContract.StartMenuView startMenuView;
    public MediaPlayerUtil mediaPlayerUtil;

    public QRPresenterImpl(Context context, QRContract.StartMenuView startMenu) {
        mContext = context;
        startMenuView = startMenu;
    }

    @Override
    public void displayToast() {
        Toast.makeText(mContext, "In presenter displayToast", Toast.LENGTH_SHORT).show();
    }

    public MediaPlayer mp;
    @Override
    public void playMusic() {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(AOPApplication.getExtPath()+ "/.AOP_External/Sounds/इसीलिए_उसने_मुझे_दरवाजे_का_ताला_खोलने_को_कहा.mp3");
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
