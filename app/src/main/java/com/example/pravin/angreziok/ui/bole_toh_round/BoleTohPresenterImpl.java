package com.example.pravin.angreziok.ui.bole_toh_round;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

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

public class BoleTohPresenterImpl implements BoleTohContract.BoleTohPresenter, RecognitionListener {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TextToSpeechCustom playTTS;
    private SpeechRecognizer speech = null;
    String language = "en-IN";
    Context mContext;
    BoleTohContract.BoleTohRoundTwoView boleTohRoundTwoView;
    BoleTohContract.BoleTohRoundOneView boleTohRoundOneView;
    List<GenericModalGson> questionData;
    GenericModalGson gsonPicGameData;

    ArrayList<String> resTextArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();
    int readQuestionNo;
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
//            final_sd_path = path;
            if (new File(path + "/.AOP_External").exists()) {
                sdCardPathString = path + "/.AOP_External/";
            }
        }
        return sdCardPathString;
    }

    @Override
    public void startSTT(Context context) {
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speech.startListening(intent);
    }

    @Override
    public void doInitialWork(String path) {
        sdCardPathString = path;
        gsonPicGameData = fetchJsonData("RoundOneGameOne", path);
        questionData = gsonPicGameData.getNodelist();
        Log.d("SIZE", "doInitialWork: " + questionData.size());
        int[] integerArray = getUniqueRandomNumber(0, questionData.size(), 4);
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
                resTextArray.add(questionData.get(integerArray[i]).getResourceText());
                resImageArray.add(questionData.get(integerArray[i]).getResourceImage());
                resAudioArray.add(questionData.get(integerArray[i]).getResourceImage());
                resIdArray.add(questionData.get(integerArray[i]).getResourceId());
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
    public void startTTS(String text) {
        playTTS.ttsFunction(text);
    }

    @Override
    public void checkAnswer(int imageViewNum, final String path) {
        String imageString = resTextArray.get(imageViewNum - 1);
        if (imageString.equalsIgnoreCase(ttsQuestion)) {
            playMusic("Sounds/correct.mp3", path);
        } else {
            playMusic("Sounds/wrong.mp3", path);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] integerArray = getUniqueRandomNumber(0, questionData.size(), 4);
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
        if ((max - min) > numSize) {
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

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

}
