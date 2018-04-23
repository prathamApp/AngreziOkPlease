package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

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

import static com.example.pravin.angreziok.AOPApplication.getRandomNumber;
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.playerModalArrayList;


public class SamajhKeBoloPresenterImpl implements SamajhKeBoloContract.SamajhKeBoloPresenter, MediaCallbacks {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TTSService ttsService;
    Context mContext;
    MediaPlayerUtil mediaPlayerUtil;
    SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View;
    SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View;
    SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View;
    List<GenericModalGson> g2l2QuestionData, g1l2QuestionData, g3l2QuestionData;
    GenericModalGson whereWhenGameData, sayItGameData, askGameData;

    int readQuestionNo, randomNumber;
    String sdCardPathString;

    public SamajhKeBoloPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG1L2View = samajhKeBoloG1L2View;
        this.ttsService = ttsService;
    }

    SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View,
                              TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG2L2View = samajhKeBoloG2L2View;
        this.ttsService = ttsService;
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG3L2View = samajhKeBoloG3L2View;
        this.ttsService = ttsService;
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
    public void set_g1_l2_data(String path) {
        whereWhenGameData = fetchJsonData("RoundThreeGameOne", path);
        g1l2QuestionData = whereWhenGameData.getNodelist();
    }

    public GenericModalGson fetchJsonData(String jasonName, String path) {
        GenericModalGson returnGsonData = null;
        try {
            InputStream is = new FileInputStream(path + "JsonFiles/" + jasonName + ".json");
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

    @Override
    public void setImage_gl_l2() {
        samajhKeBoloG1L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        String imagePath = getSdcardPath() + "PicGameImages/" + g1l2QuestionData.get(randomNumber).getResourceImage();
        Log.d("imagePath", "setImage_gl_l2: " + imagePath);
        Toast.makeText(mContext, "actual ans: " + g1l2QuestionData.get(randomNumber).getResourceText(), Toast.LENGTH_SHORT).show();
        samajhKeBoloG1L2View.setQuestionImage(imagePath);
    }

    @Override
    public String getCurrentQuestion_g1_l2() {
        return g1l2QuestionData.get(randomNumber).getResourceQuestion();
    }

    @Override
    public void startTTS(String text) {
        ttsService.play(text);
    }

    @Override
    public String[] getOptions_g1_l2() {
        int[] optionsIds;
        int[] randomOptions;
        do {
            optionsIds = getUniqueRandomNumber(0, g1l2QuestionData.size(), 2);
        } while (optionsIds[0] == randomNumber || optionsIds[1] == randomNumber);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = g1l2QuestionData.get(optionsIds[0]).getResourceText();
        optionsText[randomOptions[1]] = g1l2QuestionData.get(optionsIds[1]).getResourceText();
        optionsText[randomOptions[2]] = g1l2QuestionData.get(randomNumber).getResourceText();
        return optionsText;
    }

    @Override
    public void g1_l2_checkAnswer(String ans) {
        String actualAns = g1l2QuestionData.get(randomNumber).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            samajhKeBoloG1L2View.setAnswer(ans);
        } else {
            samajhKeBoloG1L2View.showOptions_g1_l2();
        }
    }

    @Override
    public void checkFinalAnswer_g1_l2(String ans, int currentTeam) {
        if (ans.contains(g1l2QuestionData.get(randomNumber).getResourceText())) {
            //  TODO correct answer animation + increase score of group
            samajhKeBoloG1L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            samajhKeBoloG1L2View.setCurrentScore();
        } else {
            //  TODO wrong answer animation
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
    }

    @Override
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
    public void playMusic(String fileName, String path) {
        try {
            Log.d("SoundPth", "playMusic: " + path + fileName);
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(mContext);
                mediaPlayerUtil.initCallback(SamajhKeBoloPresenterImpl.this);
            }
            mediaPlayerUtil.playMedia(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setWords_g2_l2() {
        samajhKeBoloG2L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g2l2QuestionData.size());
        samajhKeBoloG2L2View.setQuestionWords(getOptions_g2_l2());
    }

    @Override
    public String getCurrentQuestion_g2_l2() {
        return g2l2QuestionData.get(randomNumber).getResourceQuestion();
    }

    @Override
    public void set_g2_l2_data(String path) {
        sayItGameData = fetchJsonData("RoundThreeGameTwo", path);
        g2l2QuestionData = sayItGameData.getNodelist();
    }

    @Override
    public void set_g3_l2_data(String path) {
        askGameData = fetchJsonData("RoundThreeGameThree", path);
        g3l2QuestionData = askGameData.getNodelist();
    }

    @Override
    public void setWords_g3_l2(){
/*        samajhKeBoloG3L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        samajhKeBoloG3L2View.setQuestionWords(getOptions_g3_l2());*/
    }

    @Override
    public String getCurrentQuestion_g3_l2(){
    //TODO
    return null;
    }

    @Override
    public void checkFinalAnswer_g2_l2(String ans, int currentTeam) {
        if (ans.contains(g2l2QuestionData.get(randomNumber).getResourceType())) {
            //  TODO correct answer animation + increase score of group
            samajhKeBoloG2L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            if (ans.split(" ").length>1)
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            else
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 5));
            samajhKeBoloG2L2View.setCurrentScore();
        } else {
            //  TODO wrong answer animation
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
    }

    @Override
    public String[] getOptions_g2_l2() {
        return g2l2QuestionData.get(randomNumber).getResourceText().split(",");
    }

    @Override
    public String[] getOptions_g3_l2() {
        return g3l2QuestionData.get(randomNumber).getResourceText().split(",");
    }

    @Override
    public void g2_l2_checkAnswer(String ans) {
        String actualAns = g2l2QuestionData.get(randomNumber).getResourceType();
        String splittedSentence[] = ans.split("");

        if (splittedSentence.length > 1) {
            if (ans.contains(actualAns)) {
                samajhKeBoloG2L2View.setAnswer(ans);
            } else {
                samajhKeBoloG2L2View.showOptions_g2_l2();
            }
        } else {
            if (ans.equalsIgnoreCase(actualAns)) {
                samajhKeBoloG2L2View.setAnswer(ans);
            } else {
                samajhKeBoloG2L2View.showOptions_g2_l2();
            }
        }
    }

    @Override
    public void onComplete() {

    }
}
