package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.pravin.angreziok.SDCardUtil;
import com.example.pravin.angreziok.contentplayer.TextToSpeechCustom;
import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.pravin.angreziok.AOPApplication.getRandomNumber;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playerModalArrayList;

public class BoleTohPresenterImpl implements BoleTohContract.BoleTohPresenter {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TextToSpeechCustom playTTS;
    Context mContext;
    BoleTohContract.BoleToh_G1_L2_View boleTohG1L2View ;
    BoleTohContract.BoleToh_G3_L2_View boleTohG3L2View ;
    BoleTohContract.BoleToh_G2_L2_View boleTohG2L2View;
    BoleTohContract.BoleToh_G1_L1_View boleTohG1L1View;
    List<GenericModalGson> r1g1QuestionData, r1g2QuestionData,g1l2QuestionData;
    GenericModalGson gsonPicGameData, gsonActGameData;

    ArrayList<String> resTextArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();
    int readQuestionNo, r1g2RandomNo;
    String sdCardPathString;

    public BoleTohPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G1_L1_View boleTohG1L1View, TextToSpeechCustom playTTS) {
        mContext = context;
        this.boleTohG1L1View = boleTohG1L1View;
        this.playTTS = playTTS;
    }

    BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G2_L2_View boleTohG2L2View, TextToSpeechCustom playTTS) {
        mContext = context;
        this.boleTohG2L2View = boleTohG2L2View;
        this.playTTS = playTTS;
    }

    public BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G1_L2_View boleTohG1L2View, TextToSpeechCustom playTTS) {
        mContext = context;
        this.boleTohG1L2View = boleTohG1L2View;
        this.playTTS = playTTS;
    }

    public BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G3_L2_View boleTohG3L2View, TextToSpeechCustom playTTS) {
        mContext = context;
        this.boleTohG3L2View = boleTohG3L2View;
        this.playTTS = playTTS;
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
    public void doInitialWork(String path) {
        sdCardPathString = path;
        gsonPicGameData = fetchJsonData("RoundOneGameOne", path);
        r1g1QuestionData = gsonPicGameData.getNodelist();
        Log.d("SIZE", "doInitialWork: " + r1g1QuestionData.size());
        showImages(sdCardPathString);
    }

    public void showImages(String sdCardPathString) {
        try {
            int[] integerArray = getUniqueRandomNumber(0, r1g1QuestionData.size(), 4);
            readQuestionNo = getRandomNumber(0, 4);
            String imagePath = sdCardPathString + "PicGameImages/";
            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();
            for (int i = 0; i < 4; i++) {
                resTextArray.add(r1g1QuestionData.get(integerArray[i]).getResourceText());
                resImageArray.add(r1g1QuestionData.get(integerArray[i]).getResourceImage());
                resAudioArray.add(r1g1QuestionData.get(integerArray[i]).getResourceImage());
                resIdArray.add(r1g1QuestionData.get(integerArray[i]).getResourceId());
            }
            Bitmap[] bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(0)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(1)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(2)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(3))};
            boleTohG1L1View.setQuestionImages(readQuestionNo, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void readQuestion(int questionToRead) {
        ttsQuestion = resTextArray.get(questionToRead);
        Log.d("speechRate", "readQuestion: " + speechRate + "," + ttsQuestion);
        playTTS.ttsFunction("Where is " + ttsQuestion, "hin");
//            playMusic("StoriesAudio/"+ resAudioArray.get(questionToRead));
    }

    @Override
    public void replayQuestionroundone() {
        readQuestion(readQuestionNo);
    }

    @Override
    public void startTTS(String text) {
        playTTS.ttsFunction(text);
    }

    @Override
    public void set_g2_l2_data(String path) {
        gsonActGameData = fetchJsonData("RoundOneGameTwo", path);
        r1g2QuestionData = gsonActGameData.getNodelist();
    }

    @Override
    public void set_g1_l2_data(String path) {
        gsonActGameData = fetchJsonData("RoundOneGameOne", path);
        g1l2QuestionData = gsonActGameData.getNodelist();
    }

    @Override
    public String[] getOptions() {
        int[] optionsIds;
        int[] randomOptions;
        do {
            optionsIds = getUniqueRandomNumber(0, r1g2QuestionData.size(), 2);
        } while (optionsIds[0] == r1g2RandomNo || optionsIds[1] == r1g2RandomNo);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = r1g2QuestionData.get(optionsIds[0]).getResourceText();
        optionsText[randomOptions[1]] = r1g2QuestionData.get(optionsIds[1]).getResourceText();
        optionsText[randomOptions[2]] = r1g2QuestionData.get(r1g2RandomNo).getResourceText();
        return optionsText;
    }

    @Override
    public String[] getOptions_g1_l2() {
        int[] optionsIds;
        int[] randomOptions;
        do {
            optionsIds = getUniqueRandomNumber(0, g1l2QuestionData.size(), 2);
        } while (optionsIds[0] == r1g2RandomNo || optionsIds[1] == r1g2RandomNo);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = g1l2QuestionData.get(optionsIds[0]).getResourceText();
        optionsText[randomOptions[1]] = g1l2QuestionData.get(optionsIds[1]).getResourceText();
        optionsText[randomOptions[2]] = g1l2QuestionData.get(r1g2RandomNo).getResourceText();
        return optionsText;
    }

    @Override
    public void checkFinalAnswer_g2_l2(String ans, int currentTeam) {
        if (r1g2QuestionData.get(r1g2RandomNo).getResourceText().equalsIgnoreCase(ans)) {
            //  TODO correct answer animation + increase score of group
            boleTohG2L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            boleTohG2L2View.setCurrentScore();
        } else {
            //  TODO wrong answer animation
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        /*setImage_g2_l2(getSdcardPath());
        boleTohG2L2View.initiateQuestion();*/
    }

    @Override
    public void checkFinalAnswer_g1_l2(String ans, int currentTeam) {
        if (g1l2QuestionData.get(r1g2RandomNo).getResourceText().equalsIgnoreCase(ans)) {
            //  TODO correct answer animation + increase score of group
            boleTohG2L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            boleTohG2L2View.setCurrentScore();
        } else {
            //  TODO wrong answer animation
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        /*setImage_g2_l2(getSdcardPath());
        boleTohG2L2View.initiateQuestion();*/
    }

    @Override
    public void g2_l2_checkAnswer(String ans) {
        String actualAns = r1g2QuestionData.get(r1g2RandomNo).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            boleTohG2L2View.setAnswer(ans);
        } else {
            boleTohG2L2View.showOptions();
        }
    }

    @Override
    public void g1_l2_checkAnswer(String ans) {
        String actualAns = g1l2QuestionData.get(r1g2RandomNo).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            boleTohG1L2View.setAnswer(ans);
        } else {
            boleTohG1L2View.showOptions();
        }
    }

    @Override
    public void setImage_g2_l2() {
        boleTohG2L2View.hideOptionView();
        r1g2RandomNo = getRandomNumber(0, r1g2QuestionData.size());
        String imagePath = getSdcardPath() + "PicGameImages/" + r1g2QuestionData.get(r1g2RandomNo).getResourceImage();
        Toast.makeText(mContext, "actual ans: " + r1g2QuestionData.get(r1g2RandomNo).getResourceText(), Toast.LENGTH_SHORT).show();
        boleTohG2L2View.setActionGif(imagePath);
    }

    @Override
    public void setImage_gl_l2() {
        boleTohG1L2View.hideOptionView();
        r1g2RandomNo = getRandomNumber(0, g1l2QuestionData.size());
        String imagePath = getSdcardPath() + "PicGameImages/" + g1l2QuestionData.get(r1g2RandomNo).getResourceImage();
        Toast.makeText(mContext, "actual ans: " + g1l2QuestionData.get(r1g2RandomNo).getResourceText(), Toast.LENGTH_SHORT).show();
        boleTohG1L2View.setQuestionImage(imagePath);
    }

    @Override
    public void g1_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut) {
        if (!timeOut) {
            String imageString = resTextArray.get(imageViewNum - 1);
            if (imageString.equalsIgnoreCase(ttsQuestion)) {
                boleTohG1L1View.setCelebrationView();
                playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
                int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
                boleTohG1L1View.setCurrentScore();
            } else {
                //  TODO wrong answer animation
                Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
                playMusic("Sounds/wrong.mp3", getSdcardPath());
            }
        } else {
            //  TODO wrong answer animation
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
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
            MediaPlayer mp;
            mp = new MediaPlayer();
            Log.d("SoundPth", "playMusic: " + path + fileName);
            mp.setDataSource(path + fileName);
//            mp.setDataSource("file:///android_asset/sounds/" + fileName);
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
