package com.example.pravin.angreziok.ui.bole_toh_round;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
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

public class BoleTohPresenterImpl implements BoleTohContract.BoleTohPresenter {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TextToSpeechCustom playTTS;
    Context mContext;
    BoleTohContract.BoleTohRoundTwoView boleTohRoundTwoView;
    BoleTohContract.BoleTohRoundOneView boleTohRoundOneView;
    List<GenericModalGson> r1g1QuestionData, r1g2QuestionData;
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

    BoleTohPresenterImpl(Context context, BoleTohContract.BoleTohRoundOneView boleTohRoundOneView, TextToSpeechCustom playTTS) {
        mContext = context;
        this.boleTohRoundOneView = boleTohRoundOneView;
        this.playTTS = playTTS;
    }

    BoleTohPresenterImpl(Context context, BoleTohContract.BoleTohRoundTwoView boleTohRoundTwoView, TextToSpeechCustom playTTS) {
        mContext = context;
        this.boleTohRoundTwoView = boleTohRoundTwoView;
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
        int[] integerArray = getUniqueRandomNumber(0, r1g1QuestionData.size(), 4);
        showImages(integerArray, sdCardPathString);
    }

    public void showImages(int[] integerArray, String sdCardPathString) {
        try {
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
            boleTohRoundOneView.setQuestionImages(readQuestionNo, bitmap);
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
    public void setr1g2_data(String path) {
        gsonActGameData = fetchJsonData("RoundOneGameTwo", path);
        r1g2QuestionData = gsonActGameData.getNodelist();
        Log.d("SIZE", "doInitialWork: " + r1g2QuestionData.size());
        setImage_r1g2(path);
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
    public void checkFinalAnswer(String ans) {
        if (r1g2QuestionData.get(r1g2RandomNo).getResourceText().equalsIgnoreCase(ans)) {
            //  TODO correct answer animation + increase score of group
            boleTohRoundTwoView.setCelebrationView();
            playMusic("Sounds/clap.mp3", getSdcardPath());
        } else {
            //  TODO wrong answer animation
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        /*setImage_r1g2(getSdcardPath());
        boleTohRoundTwoView.initiateQuestion();*/
    }

    @Override
    public void r1g2_checkAnswer(String ans) {
        String actualAns = r1g2QuestionData.get(r1g2RandomNo).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            boleTohRoundTwoView.setAnswer(ans);
        } else {
            boleTohRoundTwoView.showOptions();
        }
    }

    private void setImage_r1g2(String path) {
        boleTohRoundTwoView.hideOptionView();
        r1g2RandomNo = getRandomNumber(0, r1g2QuestionData.size());
        String imagePath = path + "PicGameImages/" + r1g2QuestionData.get(r1g2RandomNo).getResourceImage();
        Toast.makeText(mContext, "actual ans: " + r1g2QuestionData.get(r1g2RandomNo).getResourceText(), Toast.LENGTH_SHORT).show();
        boleTohRoundTwoView.setActionGif(imagePath);
    }

    @Override
    public void r1g1_checkAnswer(int imageViewNum, final String path) {
        String imageString = resTextArray.get(imageViewNum - 1);
        if (imageString.equalsIgnoreCase(ttsQuestion)) {
            playMusic("Sounds/BilkulSahijawab.mp3", path);
        } else {
            playMusic("Sounds/wrong.mp3", path);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] integerArray = getUniqueRandomNumber(0, r1g1QuestionData.size(), 4);
                showImages(integerArray, path);
            }
        }, 1000);
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
