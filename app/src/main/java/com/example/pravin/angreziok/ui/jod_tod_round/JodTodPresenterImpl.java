package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Context;
import android.widget.Toast;

import com.example.pravin.angreziok.interfaces.MediaCallbacks;
import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.example.pravin.angreziok.services.TTSService;
import com.example.pravin.angreziok.util.MediaPlayerUtil;
import com.example.pravin.angreziok.util.SDCardUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.jodTodPlayerList;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTodPresenterImpl implements JodTodContract.JodTodPresenter, MediaCallbacks {

    Context mContext;
    JodTodContract.JodTod_G3_L2_View jodTodG3L2View;
    JodTodContract.JodTod_G1_L2_View jodTodG1L2View;
    public TTSService ttsService;
    GenericModalGson gsonListenAndSpellGameData,gsonAlphabetGameData;
    List<GenericModalGson> g3l2QuestionData,g1l2QuestionData;
    int randomNumber;
    MediaPlayerUtil mediaPlayerUtil;



    public JodTodPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G3_L2_View jodTod_g3_l2_view,TTSService ttsService){
        mContext = context;
        this.jodTodG3L2View = jodTod_g3_l2_view;
        this.ttsService= ttsService;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G1_L2_View jodTod_g1_l2_view,TTSService ttsService){
        mContext = context;
        this.jodTodG1L2View = jodTod_g1_l2_view;
        this.ttsService= ttsService;
    }


    @Override
    public void startTTS(String text) {
        ttsService.play(text);
    }

    @Override
    public String getSdcardPath() {
        String sdCardPathString = null;
        ArrayList<String> sdcard_path = SDCardUtil.getExtSdCardPaths(mContext);
        for (String path : sdcard_path) {
            if (new File(path + "/.AOP_External").exists()) {
                sdCardPathString = path + "/.AOP_External/";
            }
        }
        return sdCardPathString;
    }

    @Override
    public void set_g3_l2_data() {
        // TODO Create json file for game three
        gsonListenAndSpellGameData = fetchJsonData("RoundTwoGameThree", getSdcardPath());
        g3l2QuestionData = gsonListenAndSpellGameData.getNodelist();
    }

    @Override
    public void set_g1_l2_data() {
        // TODO Create json file for game three
        gsonAlphabetGameData = fetchJsonData("RoundTwoGameThreeLevelTwo", getSdcardPath());
        g1l2QuestionData = gsonAlphabetGameData.getNodelist();
    }

    @Override
    public String g3_l2_getQuestionText() {
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        return g3l2QuestionData.get(randomNumber).getResourceText();
    }

    @Override
    public String g1_l2_getQuestionText() {
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        return g1l2QuestionData.get(randomNumber).getResourceText();
    }

    @Override
    public void checkFinalAnswer_g3_l2(String ans, int currentTeam) {
        if (g3l2QuestionData.get(randomNumber).getResourceText().equalsIgnoreCase(ans)) {
            jodTodG3L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(jodTodPlayerList.get(currentTeam).studentScore);
            jodTodPlayerList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            jodTodG3L2View.setCurrentScore();
        } else {
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
    }

    public GenericModalGson fetchJsonData(String jsonName, String path) {
        GenericModalGson returnGsonData = null;
        try {
            InputStream is = new FileInputStream(path + "JsonFiles/" + jsonName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObj = new JSONObject(new String(buffer));
            Gson gson = new Gson();
            returnGsonData = gson.fromJson(jsonObj.toString(), GenericModalGson.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnGsonData;
    }

    public static int getRandomNumber(int min, int max) {
        return min + (new Random().nextInt(max));
    }

    @Override
    public void playMusic(String fileName, String path) {
        try {
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(mContext);
                mediaPlayerUtil.initCallback(JodTodPresenterImpl.this);
            }
            mediaPlayerUtil.playMedia(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }
}
