package com.example.pravin.angreziok.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.pravin.angreziok.BaseFragment;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G1_L1;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G1_L2;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G2_L1;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G2_L2;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G3_L1;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh_G3_L2;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G1_L1;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G1_L2;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G2_L1;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G2_L2;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G3_L1;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod_G3_L2;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo_G1_L1;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo_G1_L2;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo_G2_L1;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo_G2_L2;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo_G3_L1;
import com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo_G3_L2;
import com.example.pravin.angreziok.ui.start_data_confirmation.DataConfirmation;
import com.example.pravin.angreziok.util.PD_Utility;
import com.example.pravin.angreziok.util.SDCardUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class fragment_intro_character extends BaseFragment {

    @BindView(R.id.joker_videoView)
    VideoView joker_videoView;
    @BindView(R.id.tv_instructions)
    TextView instructionsTextView;
    @BindView(R.id.btn_skip_instructions)
    Button buttontSkip;
    @BindView(R.id.replay_button)
    Button replay_button;

    int frag = 0, count;
    String myFragmentRound, videoPath, gameLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_character, container, false);
    }

    @Override
    public void onResume() {
        if (DataConfirmation.fragmentPauseFlg) {
            DataConfirmation.fragmentPauseFlg = true;
            joker_videoView.start();
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        myFragmentRound = getArguments().getString("round", "");
        gameLevel = getArguments().getString("level", "");
        count = getArguments().getInt("count", 0);
        Log.d("games", "onViewCreated: " + count);

        if (gameLevel.equalsIgnoreCase("l2")) {
            switch (myFragmentRound) {
                case "R1":
                    frag = 1;
                    switch (count) {
                        case 0:
                            instructionsTextView.setText("Picture Game\n\nआप पर्दे पर एक चित्र देखेंगे|\n" +
                                    "उसका नाम अंग्रेजी में बताईयें|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/PictureGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 1:
                            instructionsTextView.setText("Action Game\n\nआप पर्दे पर एक क्रिया देखेंगे|\n" +
                                    "उस क्रिया का नाम अंग्रेजी में बताईयें|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/ActionGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 2:
                            instructionsTextView.setText("Pairs Game\n\n इस गेम में आप पर्दे पर दो चित्र देखेंगे|\n" +
                                    "मैं आपको पहले चित्र के बारें में बताउंगी| आप  मुझे उसी तरह दुसरे चित्र के बारें में बताओ|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/PairsGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                    }
                    break;
                case "R2":
                    frag = 2;
                    switch (count) {
                        case 0:
                            instructionsTextView.setText("Alphabets Game\n\nअब आप पर्दे पर एक अक्षर देखेंगे और उसकी आवाज़ सुनेंगे|\n" +
                                    "Timer ख़तम होने से पहले इस अक्षर से शुरू होने वाले ज्यादा से ज्यादा अंग्रेजी शब्द हमें बताईयें|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/AlphabetGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 1:
                            instructionsTextView.setText("Rhyming Game\n\nआप पर्दे पर एक शब्द देखेंगे जो मैं आपके लिए पढूंगी|\n" +
                                    "Timer ख़तम होने से पहले आपको उस शब्द की तरह आवाज़ करने वाले ज्यादा से ज्यादा अंग्रेजी शब्द हमें बताने है|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/RhymingGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 2:
                            instructionsTextView.setText("Listen and Spell\n\nइस गेम में आपको अक्षर से खाली स्थान भर कर एक शब्द बनाना है| और जानकारी के लिए यह विडियो देखे| ");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/Listen _Spell.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                    }
                    break;
                case "R3":
                    frag = 3;
                    switch (count) {
                        case 0:
                            instructionsTextView.setText("Where-When Game\n\nआपको एक चित्र के बारें में सवाल पूछा जायेगा| आपको उस सवाल का सही जवाब देना है|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/WhereGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 1:
                            instructionsTextView.setText("Say it in English \n\nइस गेम में आप अपनी मातृ भाषा में एक वाक्य सुनेंगे| आपको साथ में 2 अंग्रेजी वाक्य के विकल्प दिए जायेंगे| आपको सही अनुवाद चुनकर उस वाक्य को पढ़ कर सुनना है|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/SayItInEnglish.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 2:
                            instructionsTextView.setText("Compare Game\n\nइस गेम में आपसे एक सवाल पूछा जायेगा|आपको तीन विकल्प दिए जायेंगे| सही विकल्प चुन कर उस शब्द को बोल कर बताओ|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/CampareGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                    }
                    break;
            }
        } else {
            switch (myFragmentRound) {
                case "R1":
                    frag = 1;
                    switch (count) {
                        case 0:
                            instructionsTextView.setText("Picture Game\n\nआप पर्दे पर चार चित्र देखेंगे| मैं जो शब्द बोलूंगी उसे ध्यान से सुनो और  सही चित्र पर क्लिक करों|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/PictureGameL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 1:
                            instructionsTextView.setText("Action Game\n\nआप पर्दे पर चार क्रियायें देखेंगे| मैं जो क्रिया शब्द बोलूंगी उसे ध्यान से सुनो और सही चित्र पर क्लिक करों|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/ActionGameL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 2:
                            instructionsTextView.setText("Pairs Game\n\nइस गेम में आप पर्दे पर दो चित्र देखेंगे|\n " +
                                    "मैं आपको एक चित्र के बारें में बताउंगी| आपको उस चित्र पर क्लिक करना है|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/PairGameL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                    }
                    break;
                case "R2":
                    frag = 2;
                    switch (count) {
                        case 0:
                            instructionsTextView.setText("Alphabets Game\n\n\"अब आप पर्दे पर एक अक्षर देखेंगे और उसकी आवाज़ सुनेंगे| आप पर्दे पर चार चित्र भी देखेंगे|\n" +
                                    "आपको दिए गए अक्षर से शुरू होने वाले चित्र पर क्लिक करना है|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/AlphabetGameL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 1:
                            instructionsTextView.setText("Rhyming Game\n\n\"आप पर्दे पर तीन शब्द देखेंगे| मैं आपके लिए पहला शब्द पढूंगी| आप मुझे बाक़ी दो शब्द पढ़कर सुनाओ|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/RhymingGameL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 2:
                            instructionsTextView.setText("Listen and Spell\n\nइस गेम में आपको अक्षर से खाली स्थान भर कर एक शब्द बनाना है| और जानकारी के लिए यह विडियो देखे| ");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/ListenAndSpellL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                    }
                    break;
                case "R3":
                    frag = 3;
                    switch (count) {
                        case 0:
                            instructionsTextView.setText("Where-When Game\n\nइस गेम में आप पर्दे पर दो चित्र देखेंगे|\n" +
                                    "मैं आपको एक चित्र के बारें में बताउंगी| आपको उस चित्र पर क्लिक करना है|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/WhereGameL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 1:
                            instructionsTextView.setText("Say it in English \n\nइस गेम में आप अपनी मातृ भाषा में एक वाक्य सुनेंगे| आप हमें उस वाक्य का अंग्रेजी में सही अनुवाद बताएँ |");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/SayItInEnglishL1.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                        case 2:
                            instructionsTextView.setText("Compare Game\n\nइस गेम में आपसे एक सवाल पूछा जायेगा|आपको तीन विकल्प दिए जायेंगे| सही विकल्प चुन कर उस शब्द को बोल कर बताओ|");
                            videoPath = PD_Utility.getExternalPath(getActivity()) + "Videos/CampareGame.mp4";
                            playVideo(Uri.parse(videoPath));
                            break;
                    }
                    break;
            }

        }
    }

    public void playVideo(Uri path) {
//        MediaController mediaController = new MediaController(context);
//        mediaController.setAnchorView(videoView);
        try {
            joker_videoView.setVideoURI(path);
            joker_videoView.start();
            joker_videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    replay_button.setVisibility(View.VISIBLE);
                    buttontSkip.setText("Start");
                }
            });
        } catch (Exception e) {
            Log.e("Cant Play Video", e.getMessage());
            e.printStackTrace();
        }
//        videoView.setMediaController(mediaController);
        joker_videoView.requestFocus();
    }


    @OnClick(R.id.btn_skip_instructions)
    public void gotoNext() {
        DataConfirmation.fragmentPauseFlg = false;
        loadFragment(frag, count);
    }

    @OnClick(R.id.replay_button)
    public void replayVideo() {
        replay_button.setVisibility(View.GONE);
        buttontSkip.setText("Skip");
        playVideo(Uri.parse(videoPath));
    }

    public void loadFragment(int no, int count) {

        if (gameLevel.equalsIgnoreCase("l2")) {
            switch (no) {
                case 1:
                    switch (count) {
                        case 0:
                            PD_Utility.showFragment(getActivity(), new BoleToh_G1_L2(), R.id.cl_bole_toh,
                                    null, BoleToh_G1_L2.class.getSimpleName());
                            break;
                        case 1:
                            PD_Utility.showFragment(getActivity(), new BoleToh_G2_L2(), R.id.cl_bole_toh,
                                    null, BoleToh_G2_L2.class.getSimpleName());
                            break;
                        case 2:
                            PD_Utility.showFragment(getActivity(), new BoleToh_G3_L2(), R.id.cl_bole_toh,
                                    null, BoleToh_G3_L2.class.getSimpleName());
                            break;
                    }
                    break;
                case 2:
                    switch (count) {
                        case 0:
                            PD_Utility.showFragment(getActivity(), new JodTod_G1_L2(), R.id.cl_jod_tod,
                                    null, JodTod_G1_L2.class.getSimpleName());
                            break;
                        case 1:
                            PD_Utility.showFragment(getActivity(), new JodTod_G2_L2(), R.id.cl_jod_tod,
                                    null, JodTod_G2_L2.class.getSimpleName());
                            break;
                        case 2:
                            PD_Utility.showFragment(getActivity(), new JodTod_G3_L2(), R.id.cl_jod_tod,
                                    null, JodTod_G3_L2.class.getSimpleName());
                            break;
                    }
                    break;
                case 3:
                    switch (count) {
                        case 0:
                            PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G1_L2(), R.id.cl_samajh_ke_bolo,
                                    null, SamajhKeBolo_G1_L2.class.getSimpleName());
                            break;
                        case 1:
                            PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G3_L2(), R.id.cl_samajh_ke_bolo,
                                    null, SamajhKeBolo_G3_L2.class.getSimpleName());
                            break;
                        case 2:
                            PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G1_L2(), R.id.cl_samajh_ke_bolo,
                                    null, SamajhKeBolo_G2_L2.class.getSimpleName());
                            break;
                    }
                    break;
            }
        } else {
            switch (no) {
                case 1:
                    switch (count) {
                        case 0:
                            PD_Utility.showFragment(getActivity(), new BoleToh_G1_L1(), R.id.cl_bole_toh,
                                    null, BoleToh_G1_L1.class.getSimpleName());
                            break;
                        case 1:
                            PD_Utility.showFragment(getActivity(), new BoleToh_G2_L1(), R.id.cl_bole_toh,
                                    null, BoleToh_G2_L1.class.getSimpleName());
                            break;
                        case 2:
                            PD_Utility.showFragment(getActivity(), new BoleToh_G3_L1(), R.id.cl_bole_toh,
                                    null, BoleToh_G3_L1.class.getSimpleName());
                            break;
                    }
                    break;
                case 2:
                    switch (count) {
                        case 0:
                            PD_Utility.showFragment(getActivity(), new JodTod_G1_L1(), R.id.cl_jod_tod,
                                    null, JodTod_G1_L1.class.getSimpleName());
                            break;
                        case 1:
                            PD_Utility.showFragment(getActivity(), new JodTod_G2_L1(), R.id.cl_jod_tod,
                                    null, JodTod_G2_L1.class.getSimpleName());
                            break;
                        case 2:
                            PD_Utility.showFragment(getActivity(), new JodTod_G3_L1(), R.id.cl_jod_tod,
                                    null, JodTod_G3_L1.class.getSimpleName());
                            break;
                    }
                    break;
                case 3:
                    switch (count) {
                        case 0:
                            PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G1_L1(), R.id.cl_samajh_ke_bolo,
                                    null, SamajhKeBolo_G1_L1.class.getSimpleName());
                            break;
                        case 1:
                            PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G3_L1(), R.id.cl_samajh_ke_bolo,
                                    null, SamajhKeBolo_G3_L1.class.getSimpleName());
                            break;
                        case 2:
                            PD_Utility.showFragment(getActivity(), new SamajhKeBolo_G2_L1(), R.id.cl_samajh_ke_bolo,
                                    null, SamajhKeBolo_G2_L1.class.getSimpleName());
                            break;
                    }
                    break;
            }
        }
    }

    public String getSdcardPath() {
        String sdCardPathString = null;
        ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(getActivity());
        for (String path : sdcard_path) {
            if (new File(path + "/.AOP_External").exists()) {
                sdCardPathString = path + "/.AOP_External/";
            }
        }
        return sdCardPathString;
    }

    @Override
    public void onPause() {
        super.onPause();
        DataConfirmation.fragmentPauseFlg = true;
        joker_videoView.pause();
    }
}