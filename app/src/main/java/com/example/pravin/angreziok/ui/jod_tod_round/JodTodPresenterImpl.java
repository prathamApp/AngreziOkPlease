package com.example.pravin.angreziok.ui.jod_tod_round;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Score;
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
import static com.example.pravin.angreziok.ui.jod_tod_round.JodTod.jodTodPlayerList;

/**
 * Created by Pravin on 20/03/2018.
 */

public class JodTodPresenterImpl implements JodTodContract.JodTodPresenter, MediaCallbacks {

    Context mContext;
    JodTodContract.JodTod_G1_L2_View jodTodG1L2View;
    JodTodContract.JodTod_G1_L1_View jodTodG1L1View;
    JodTodContract.JodTod_G2_L2_View jodTodG2L2View;
    JodTodContract.JodTod_G3_L2_View jodTodG3L2View;
    JodTodContract.JodTod_G2_L1_View jodTodG2L1View;
    public TTSService ttsService;
    GenericModalGson gsonListenAndSpellGameData, gsonAlphabetGameData, gsonRhymeGameData;
    List<GenericModalGson> g3l2QuestionData, g1l2QuestionData, g2l2QuestionData, g2l2SubList,g1l1QuestionData,g1l1SubData;
    int randomNumber1, randomNumber,readQuestionNo;
    String rhymeCheckWord, questionWord, myAlphabet;
    String sdCardPathString, questionStartTime, studentID, resourceID, questionId;
    public MediaPlayerUtil mediaPlayerUtil;
    private AppDatabase appDatabase;
    int scoredMarks, totalMarks = 15;
    ArrayList<String> g1g2result;
    ArrayList<String> resTextArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();
    String ttsQuestion;
    float speechRate = 1.0f;

    public JodTodPresenterImpl(Context mContext) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G3_L2_View jodTod_g3_l2_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG3L2View = jodTod_g3_l2_view;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G1_L2_View jodTod_g1_l2_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG1L2View = jodTod_g1_l2_view;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        g1g2result = new ArrayList<String>();
    }
    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G1_L1_View jodTod_g1_l1_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG1L1View = jodTod_g1_l1_view;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        g1g2result = new ArrayList<String>();
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G2_L2_View jodTod_g2_l2_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG2L2View = jodTod_g2_l2_view;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        g1g2result = new ArrayList<String>();
    }

    public JodTodPresenterImpl(Context context, JodTodContract.JodTod_G2_L1_View jodTod_g2_l1_view, TTSService ttsService) {
        mContext = context;
        this.jodTodG2L1View = jodTod_g2_l1_view;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    @Override
    public void setCurrentScore(int currentMarks) {
        scoredMarks += currentMarks;
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
    public void doInitialWorkG1l1(String path) {
        sdCardPathString = path;
        gsonAlphabetGameData = fetchJsonData("RoundTwoGameOne", path);
        g1l1QuestionData = gsonAlphabetGameData.getNodelist();
        Log.d("SIZE", "doInitialWork: " + g1l1QuestionData.size());
    }

    @Override
    public void showImagesG1L1(String path, String studId) {
        try {
            Collections.shuffle(g1l1QuestionData);

            readQuestionNo = getRandomNumber(0, 4);
            int[] integerArray = getUniqueRandomNumber(0, g1l1QuestionData.size(), 4);
            String imagePath = sdCardPathString + "images/PicGameL2/";

            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();

            setQuestionStartTime();
            studentID = studId;
            resourceID = g1l1QuestionData.get(integerArray[readQuestionNo]).getResourceId();
            myAlphabet = g1l1QuestionData.get(integerArray[readQuestionNo]).getResourceText();
            jodTodG1L1View.setQuestionDynamically(myAlphabet);
            questionId = resourceID;

            for (int i = 0; i < 4; i++) {
                g1l1SubData = g1l1QuestionData.get(integerArray[i]).getNodelist();
                Collections.shuffle(g1l1SubData);
                resTextArray.add(g1l1SubData.get(0).getResourceText());
                resImageArray.add(g1l1SubData.get(0).getResourceImage());
                resAudioArray.add(g1l1SubData.get(0).getResourceImage());
                resIdArray.add(g1l1SubData.get(0).getResourceId());
            }
            Bitmap[] bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(0)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(1)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(2)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(3))};
            jodTodG1L1View.setQuestionImgs(readQuestionNo, bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void g1_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut) {
        int scoredMarks, totalMarks = 10;
        if (!timeOut) {
            String imageString = resTextArray.get(imageViewNum - 1);
            if (imageString.equalsIgnoreCase(ttsQuestion)) {
                jodTodG1L1View.setCelebrationView();
                scoredMarks = 10;
                playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
                int currentTeamScore = Integer.parseInt(jodTodPlayerList.get(currentTeam).studentScore);
                jodTodPlayerList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
                jodTodG1L1View.setCurrentScore();
            } else {
                //  TODO wrong answer animation
                scoredMarks = 0;
                Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
                playMusic("Sounds/wrong.mp3", getSdcardPath());
            }
        } else {
            //  TODO wrong answer animation
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
    }

    @Override
    public void readQuestion(int questionToRead) {
        ttsQuestion = resTextArray.get(questionToRead);
        Log.d("speechRate", "readQuestion: " + speechRate + "," + ttsQuestion);
        ttsService.play("Where is " + ttsQuestion);
    }

    @Override
    public void readLetter(int questionToRead) {
        ttsQuestion = resTextArray.get(questionToRead);
        Log.d("speechRate", "readQuestion: " + speechRate + "," + myAlphabet);
        ttsService.play("Which of these starts with, " + myAlphabet);
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
        String questionString = g2l2SubList.get(randomNumber).getResourceQuestion();
        jodTodG2L2View.setQuestionText(questionString);
    }


    @Override
    public void set_g3_l2_data(int level) {
        // TODO Create json file for game three
        if(level==0)
            gsonListenAndSpellGameData = fetchJsonData("RoundTwoGameThree", getSdcardPath());
        else
            gsonListenAndSpellGameData = fetchJsonData("RoundTwoGameThreeLevelOne", getSdcardPath());
        g3l2QuestionData = gsonListenAndSpellGameData.getNodelist();
        String gameTitle = gsonListenAndSpellGameData.getNodeTitle();
        jodTodG3L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void set_g1_l2_data() {
        // TODO Create json file for game three
        gsonAlphabetGameData = fetchJsonData("RoundTwoGameOne", getSdcardPath());
        g1l2QuestionData = gsonAlphabetGameData.getNodelist();
        String gameTitle = gsonAlphabetGameData.getNodeTitle();
        jodTodG1L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void set_g2_l2_data() {
        // TODO Create json file for game three
        gsonRhymeGameData = fetchJsonData("RoundTwoGameTwo", getSdcardPath());
        g2l2QuestionData = gsonRhymeGameData.getNodelist();
        String gameTitle = gsonRhymeGameData.getNodeTitle();
        jodTodG2L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void g1_l2_checkAnswer(String ans) {
        if (!ans.equalsIgnoreCase("") && !ans.equalsIgnoreCase(" ")) {
            String actualAns = "" + ans.charAt(0);
            boolean match = false;
            for (int i = 0; i < g1g2result.size(); i++) {
                if (g1g2result.get(i).equalsIgnoreCase(ans)) {
                    match = true;
                    break;
                }
            }
            if (!match)
                g1g2result.add(ans);
            jodTodG1L2View.setAnswer(actualAns, ans, match);
        }
    }

    @Override
    public void g2_l2_checkAnswer(String ans) {
        String rhymAns = "", ansRev = "", rhymeRev = "";

        if (!ans.equalsIgnoreCase("") && !ans.equalsIgnoreCase(" ")) {
            int rhymeLen = rhymeCheckWord.length();
            if (ans.length() > rhymeLen) {

                boolean match = false;
                for (int i = 0; i < g1g2result.size(); i++) {
                    if (g1g2result.get(i).equalsIgnoreCase(ans)) {
                        match = true;
                        break;
                    }
                }

                if (!match)
                    g1g2result.add(ans);

                Log.d("rhymAns", "ans: " + ans + "      Rhyme : " + rhymeCheckWord);
                Log.d("rhymAns", "ans.lastIndexOf(rhymeCheckWord): " + ans.lastIndexOf(rhymeCheckWord));

                if ((ans.lastIndexOf(rhymeCheckWord)) != -1 && (ans.substring(ans.lastIndexOf(rhymeCheckWord))).equalsIgnoreCase(rhymeCheckWord) && !match)
                    jodTodG2L2View.setAnswer("true", "" + ans, false);
                else
                    jodTodG2L2View.setAnswer("false", "" + ans, true);
            }
        }
    }

    @Override
    public String[] getOptions() {
        randomNumber = getRandomNumber(0, g2l2QuestionData.size());
        rhymeCheckWord = g2l2QuestionData.get(randomNumber).getResourceText();
        g2l2SubList = g2l2QuestionData.get(randomNumber).getNodelist();
        randomNumber = getRandomNumber(0, g2l2SubList.size());
        questionWord = g2l2SubList.get(randomNumber).getResourceText();

        return null;

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
    public String g3_l2_getQuestionText(String studId) {
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        String questionString = g3l2QuestionData.get(randomNumber).getResourceQuestion();
        setQuestionStartTime();
        studentID = studId;
        resourceID = g3l2QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;

        jodTodG3L2View.setQuestionText(questionString);
        return g3l2QuestionData.get(randomNumber).getResourceText();
    }

    @Override
    public String g3_l2_getQuestionAudio() {
        return g3l2QuestionData.get(randomNumber).getResourceAudio();
    }

    @Override
    public String g1_l2_getQuestionText(String studId) {
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        setQuestionStartTime();
        studentID = studId;
        resourceID = g1l2QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;

        String questionString = g1l2QuestionData.get(randomNumber).getResourceQuestion();
        jodTodG1L2View.setQuestionText(questionString);

        if (g1g2result.size() > 0) {
            g1g2result.clear();
        }
        g1g2result.add(g1l2QuestionData.get(randomNumber).getResourceText());

        return g1l2QuestionData.get(randomNumber).getResourceText();
    }

    @Override
    public String g2_l2_getQuestionText(String studId) {

        jodTodG2L2View.hideOptionView();

        randomNumber1 = getRandomNumber(0, g2l2QuestionData.size());
        rhymeCheckWord = g2l2QuestionData.get(randomNumber).getResourceText();
        g2l2SubList = g2l2QuestionData.get(randomNumber).getNodelist();
        randomNumber = getRandomNumber(0, g2l2SubList.size());
        questionWord = g2l2SubList.get(randomNumber).getResourceText();

        String questionString = g2l2QuestionData.get(randomNumber).getResourceQuestion();
        jodTodG2L2View.setQuestionText(questionString);

        if (g1g2result.size() > 0) {
            g1g2result.clear();
        }
        g1g2result.add(questionWord);

        setQuestionStartTime();
        studentID = studId;
        resourceID = g2l2SubList.get(randomNumber).getResourceId();
        questionId = resourceID;

        return questionWord;
        /*jodTodG2L2View.initiateQuestion(questionWord);*/
/*        randomNumber = getRandomNumber(0, g2l2QuestionData.size());
        return g2l2QuestionData.get(randomNumber).getResourceText();*/
    }

    @Override
    public void checkFinalAnswer_g1_l2(float scorePercentage, String score, int currentTeam) {

        int currentTeamScore = Integer.parseInt(jodTodPlayerList.get(currentTeam).studentScore);
        currentTeamScore += Integer.parseInt(score);
        jodTodPlayerList.get(currentTeam).setStudentScore("" + currentTeamScore);
        jodTodG1L2View.setCurrentScore();

        if (scorePercentage > 40) {
            //  TODO Correct Answer Animation
            jodTodG1L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
        } else {
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
        scoredMarks = 0;
    }

    @Override
    public void checkFinalAnswer_g2_l2(float scorePercentage, String score, int currentTeam) {

        int currentTeamScore = Integer.parseInt(jodTodPlayerList.get(currentTeam).studentScore);
        currentTeamScore += Integer.parseInt(score);

        jodTodPlayerList.get(currentTeam).setStudentScore("" + currentTeamScore);
        jodTodG2L2View.setCurrentScore();

        if (scorePercentage > 40) {
            //  TODO Correct Answer Animation
            jodTodG2L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
        } else {
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
        scoredMarks = 0;
    }


    @Override
    public void checkFinalAnswer_g3_l2(String ans, int currentTeam) {

        if (g3l2QuestionData.get(randomNumber).getResourceText().equalsIgnoreCase(ans)) {
            jodTodG3L2View.setCelebrationView();
            scoredMarks = 15;
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(jodTodPlayerList.get(currentTeam).studentScore);
            jodTodPlayerList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            jodTodG3L2View.setCurrentScore();
        } else {
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
        scoredMarks = 0;
    }

    private void setQuestionStartTime() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                questionStartTime = AOPApplication.getCurrentDateTime(true, AppStartDateTime);
                return null;
            }
        }.execute();
    }

    public void addScore(String studentID, String resourceID, int questionId, int scoredMarks, int totalMarks, String startDateTime, int questionLevel) {
        try {
            final Score score = new Score();
            score.setStudentID("" + studentID);
            score.setResourceID("" + resourceID);
            score.setQuestionId(questionId);
            score.setScoredMarks(scoredMarks);
            score.setTotalMarks(totalMarks);
            score.setStartDateTime("" + startDateTime);
            score.setLevel(0);

            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object... objects) {
                    String AppStartDateTime = appDatabase.getStatusDao().getValue("AppStartDateTime");
                    score.setSessionID(""+appDatabase.getStatusDao().getValue("CurrentSession"));
                    score.setDeviceID(""+appDatabase.getStatusDao().getValue("DeviceID"));
                    score.setEndDateTime("" + AOPApplication.getCurrentDateTime(true, AppStartDateTime));
                    appDatabase.getScoreDao().insert(score);
                    BackupDatabase.backup(mContext);
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
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
    public void onComplete() {    }

    @Override
    public void fragmentOnPause() {
        if (mediaPlayerUtil != null)
            mediaPlayerUtil.stopMedia();
        if(ttsService != null )
            ttsService.stop();
    }
}
