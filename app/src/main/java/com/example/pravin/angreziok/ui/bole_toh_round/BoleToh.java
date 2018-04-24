package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.VideoView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyBounceInterpolator;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.fragment_intro_character;
import com.example.pravin.angreziok.util.PD_Utility;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView, MediaPlayer.OnCompletionListener {

    BoleTohContract.BoleTohPresenter presenter;
    static ArrayList<PlayerModal> playerModalArrayList;
    String videoPath;
    static int gameCounter = 0;

    @BindView(R.id.round_intro_videoView)
    VideoView introVideo;
    @BindView(R.id.skip_button_intro)
    Button btn_skip;
    static ArrayList<Integer> list = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");

        for (int i = 0; i < 3; i++)
            list.add(new Integer(i));
        Collections.shuffle(list);
        Log.d("games", "\n\n\nShuffle: \n\n\n");
        for(int i =0 ; i<list.size(); i++)
            Log.d("games", "gamesNo: "+list.get(i));


        Collections.shuffle(playerModalArrayList);
        for(int i=0; i<playerModalArrayList.size(); i++)
            Log.d("BoleTohTAG", "playerModalArrayList: "+playerModalArrayList.get(i).getStudentAlias());

        videoPath = PD_Utility.getExternalPath(this) + "Videos/Bole_to_round.mp4";
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.d("videoPath", "onCreate: "+videoPath);
        playVideo(Uri.parse(videoPath));
        presenter = new BoleTohPresenterImpl(this);
    }

    private void playVideo(Uri videoPath) {
        try {
            introVideo.setVideoURI(videoPath);
            introVideo.start();
        } catch (Exception e) {
            Log.e("Cant Play Video", e.getMessage());
            e.printStackTrace();
        }
        introVideo.requestFocus();
    }


    @OnClick(R.id.skip_button_intro)
    public void startGame(){
        loadFragment();
    }

    @Override
    public void loadFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("round", "R1");
        bundle.putInt("count", list.get(gameCounter));
        PD_Utility.showFragment(BoleToh.this, new fragment_intro_character(), R.id.cl_bole_toh,
                bundle, fragment_intro_character.class.getSimpleName());
    }

    public static void animateView(View view,Context context) {
        Animation rubber = AnimationUtils.loadAnimation(context, R.anim.popup);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 15);
        rubber.setInterpolator(interpolator);
        view.startAnimation(rubber);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        loadFragment();
    }
}
