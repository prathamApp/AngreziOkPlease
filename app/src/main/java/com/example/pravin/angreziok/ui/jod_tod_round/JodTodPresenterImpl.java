package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Context;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.pravin.angreziok.AOPApplication.getRandomNumber;
import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.jodTodPlayerList;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTodPresenterImpl implements JodTodContract.JodTodPresenter, MediaCallbacks {

    Context mContext;
    JodTodContract.JodTod_G3_L2_View jodTodG3L2View;
    JodTodContract.JodTod_G1_L2_View jodTodG1L2View;
    JodTodContract.JodTod_G2_L2_View jodTodG2L2View;
    public TTSService ttsService;
    GenericModalGson gsonListenAndSpellGameData, gsonAlphabetGameData,gsonRhymeGameData;
    List<GenericModalGson> g3l2QuestionData, g1l2QuestionData,g2l2QuestionData, g2l2SubList;
    int randomNumber1,randomNumber;
    String rhymeCheckWord, questionWord;
    MediaPlayerUtil mediaPlayerUtil;


    public JodTodPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G3_L2_View jodTod_g3_l2_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG3L2View = jodTod_g3_l2_view;
        this.ttsService = ttsService;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G1_L2_View jodTod_g1_l2_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG1L2View = jodTod_g1_l2_view;
        this.ttsService = ttsService;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G2_L2_View jodTod_g2_l2_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG2L2View = jodTod_g2_l2_view;
        this.ttsService = ttsService;
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
    public void setWord_g2_l2() {
        jodTodG2L2View.hideOptionView();
        randomNumber1 = getRandomNumber(0, g2l2QuestionData.size());
        rhymeCheckWord = g2l2QuestionData.get(randomNumber).getResourceText();
        g2l2SubList = g2l2QuestionData.get(randomNumber).getNodelist();
        randomNumber = getRandomNumber(0, g2l2SubList.size());
        questionWord = g2l2SubList.get(randomNumber).getResourceText();
        jodTodG2L2View.initiateQuestion(questionWord);
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
        gsonAlphabetGameData = fetchJsonData("RoundTwoGameOne", getSdcardPath());
        g1l2QuestionData = gsonAlphabetGameData.getNodelist();
    }

    @Override
    public void set_g2_l2_data() {
        // TODO Create json file for game three
        gsonRhymeGameData = fetchJsonData("RoundTwoGameTwo", getSdcardPath());
        g2l2QuestionData = gsonRhymeGameData.getNodelist();
    }

    @Override
    public void g1_l2_checkAnswer(String ans) {
        if (!ans.equalsIgnoreCase("") && !ans.equalsIgnoreCase(" ")) {
            String actualAns = "" + ans.charAt(0);
            jodTodG1L2View.setAnswer(actualAns, ans);
        }
    }

    @Override
    public void g2_l2_checkAnswer(String ans) {
        String rhymAns="";

        if (!ans.equalsIgnoreCase("") && !ans.equalsIgnoreCase(" ")) {
            int rhymeLen = rhymeCheckWord.length();
            if(ans.length()>rhymeLen) {
                for (int i = rhymeLen; i >= 0; i--) {
                    rhymAns= ans.charAt(i)+""+rhymAns;
                }
                Log.d("rhymAns", "rhymeCheckWord: "+rhymeCheckWord+"       g1_l2_checkAnswer: rhymAns : "+rhymAns);
            }
            String actualAns = "" + ans.charAt(0);
        }
    }

    @Override
    public String[] getOptions() {
        int[] optionsIds;
        int[] randomOptions;

/*        randomNumber = getRandomNumber(0, g2l2QuestionData.size());
        rhymeCheckWord = g2l2QuestionData.get(randomNumber).getResourceText();
        g2l2SubList = g2l2QuestionData.get(randomNumber).getNodelist();
        randomNumber = getRandomNumber(0, g2l2SubList.size());
        questionWord = g2l2SubList.get(randomNumber).getResourceText();*/

        do {
            optionsIds = getUniqueRandomNumber(0, g2l2QuestionData.size(), 2);
        } while (optionsIds[0] == randomNumber || optionsIds[1] == randomNumber);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = g2l2QuestionData.get(optionsIds[0]).getResourceText();
        optionsText[randomOptions[1]] = g2l2QuestionData.get(optionsIds[1]).getResourceText();
        optionsText[randomOptions[2]] = g2l2QuestionData.get(randomNumber).getResourceText();
        return optionsText;

    }

    public int[] getUniqueRandomNumber(int min, int max, int numSize) {
        int[] tempArray;
        if ((max - min) >= numSize) {
            tempArray = new int[numSize];
            ArrayList<Integer> list = new ArrayList<Integer>();

            for (int i = min; i < max; i++)
                list.add(new Integer(i));

            Collections.shuffle(list);
            for (int i = 0; i < numSize; i++) {
                System.out.println("===== : " + list.get(i));
                tempArray[i] = list.get(i);
            }
            return tempArray;
        } else
            return null;
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
    public String g2_l2_getQuestionText() {
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        return g1l2QuestionData.get(randomNumber).getResourceText();
    }

    @Override
    public void checkFinalAnswer_g1_l2(String socore, int currentTeam) {

        int currentTeamScore = Integer.parseInt(jodTodPlayerList.get(currentTeam).studentScore);
        currentTeamScore += Integer.parseInt(socore);

        jodTodPlayerList.get(currentTeam).setStudentScore(""+currentTeamScore);
        jodTodG1L2View.setCurrentScore();

        //  TODO Correct Answer Animation
        jodTodG3L2View.setCelebrationView();
        playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
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
