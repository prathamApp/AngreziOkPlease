package com.example.pravin.angreziok.ui.jod_tod_round;

import android.content.Context;

import com.example.pravin.angreziok.modalclasses.GenericModalGson;
import com.example.pravin.angreziok.services.TTSService;
import com.example.pravin.angreziok.util.SDCardUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTodPresenterImpl implements JodTodContract.JodTodPresenter {

    Context mContext;
    JodTodContract.JodTod_G3_L2_View jodTodG3L2View;
    public TTSService ttsService;
    GenericModalGson gsonListenAndSpellGameData;
    List<GenericModalGson> g3l2QuestionData;
    int randomNumber;


    public JodTodPresenterImpl(Context mContext) {
        this.mContext = mContext;
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G3_L2_View jodTod_g3_l2_view,TTSService ttsService){
        mContext = context;
        this.jodTodG3L2View = jodTod_g3_l2_view;
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
        gsonListenAndSpellGameData = fetchJsonData("RoundOneGameThree", getSdcardPath());
        g3l2QuestionData = gsonListenAndSpellGameData.getNodelist();
    }

    @Override
    public String g3_l2_getQuestionText() {
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        return g3l2QuestionData.get(randomNumber).getResourceText();
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
}
