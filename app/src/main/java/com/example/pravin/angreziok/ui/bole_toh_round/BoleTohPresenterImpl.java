package com.example.pravin.angreziok.ui.bole_toh_round;

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

import static com.example.pravin.angreziok.AOPApplication.getRandomNumber;
import static com.example.pravin.angreziok.ui.bole_toh_round.BoleToh.playerModalArrayList;

public class BoleTohPresenterImpl implements BoleTohContract.BoleTohPresenter, MediaCallbacks {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TTSService ttsService;
    String questionAudioPath;
    Context mContext;
    String[] optionsAudio = new String[3];
    public MediaPlayerUtil mediaPlayerUtil;
    BoleTohContract.BoleToh_G1_L2_View boleTohG1L2View;
    BoleTohContract.BoleToh_G3_L2_View boleTohG3L2View;
    BoleTohContract.BoleToh_G2_L2_View boleTohG2L2View;
    BoleTohContract.BoleToh_G1_L1_View boleTohG1L1View;
    BoleTohContract.BoleToh_G2_L1_View boleTohG2L1View;
    BoleTohContract.BoleToh_G3_L1_View boleTohG3L1View;
    List<GenericModalGson> g1l1QuestionData, g2l2QuestionData, g1l2QuestionData, g3l2QuestionData, currentPairList;
    GenericModalGson gsonPicGameData, gsonActGameData, gsonPairGameData;

    ArrayList<String> resTextArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();
    int readQuestionNo, randomNumber;
    String sdCardPathString, questionStartTime, studentID, resourceID, questionId;
    private AppDatabase appDatabase;

    public BoleTohPresenterImpl(Context mContext) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G1_L1_View boleTohG1L1View,
                         TTSService ttsService) {
        mContext = context;
        this.boleTohG1L1View = boleTohG1L1View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G2_L2_View boleTohG2L2View,
                         TTSService ttsService) {
        mContext = context;
        this.boleTohG2L2View = boleTohG2L2View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G1_L2_View boleTohG1L2View,
                                TTSService ttsService) {
        mContext = context;
        this.boleTohG1L2View = boleTohG1L2View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G3_L2_View boleTohG3L2View,
                                TTSService ttsService) {
        mContext = context;
        this.boleTohG3L2View = boleTohG3L2View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G2_L1_View boleTohG2L1View,
                                TTSService ttsService) {
        mContext = context;
        this.boleTohG2L1View = boleTohG2L1View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public BoleTohPresenterImpl(Context context, BoleTohContract.BoleToh_G3_L1_View boleTohG3L1View,
                                TTSService ttsService) {
        mContext = context;
        this.boleTohG3L1View = boleTohG3L1View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
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
        g1l1QuestionData = gsonPicGameData.getNodelist();
        String gameTitle = gsonPicGameData.getNodeTitle();
        boleTohG1L1View.setGameTitleFromJson(gameTitle);
        Log.d("SIZE", "doInitialWork: " + g1l1QuestionData.size());
    }

    @Override
    public void doInitialWorkG2l1(String path) {
        sdCardPathString = path;
        gsonActGameData = fetchJsonData("RoundOneGameTwo", path);
        g2l2QuestionData = gsonActGameData.getNodelist();
        String gameTitle = gsonActGameData.getNodeTitle();
        boleTohG2L1View.setGameTitleFromJson(gameTitle);
        Log.d("SIZE", "doInitialWork: " + g2l2QuestionData.size());
    }

    @Override
    public void doInitialWorkG3l1(String path) {
        sdCardPathString = path;
        gsonPairGameData = fetchJsonData("RoundOneGameThree", path);
        g3l2QuestionData = gsonPairGameData.getNodelist();
        String gameTitle = gsonPairGameData.getNodeTitle();
        boleTohG3L1View.setGameTitleFromJson(gameTitle);
        Log.d("SIZE", "doInitialWork: " + g3l2QuestionData.size());
    }

    @Override
    public void showImagesG1L1(String sdCardPathString, String studId) {
        try {
            int[] integerArray = getUniqueRandomNumber(0, g1l1QuestionData.size(), 4);
            readQuestionNo = getRandomNumber(0, 4);
            String imagePath = sdCardPathString + "images/PicGameL2/";

            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();

            setQuestionStartTime();
            studentID = studId;
            resourceID = g1l1QuestionData.get(integerArray[readQuestionNo]).getResourceId();
            questionId = resourceID;

            for (int i = 0; i < 4; i++) {
                resTextArray.add(g1l1QuestionData.get(integerArray[i]).getResourceText());
                resImageArray.add(g1l1QuestionData.get(integerArray[i]).getResourceImage());
                resAudioArray.add(g1l1QuestionData.get(integerArray[i]).getResourceImage());
                resIdArray.add(g1l1QuestionData.get(integerArray[i]).getResourceId());
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
    public void showImagesG2L1(String path, String studId) {
        try {
            int[] integerArray = getUniqueRandomNumber(0, g2l2QuestionData.size(), 4);
            readQuestionNo = getRandomNumber(0, 4);
            String imagePath = sdCardPathString + "images/ActionL2/";

            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();

            setQuestionStartTime();
            studentID = studId;
            resourceID = g2l2QuestionData.get(integerArray[readQuestionNo]).getResourceId();
            questionId = resourceID;

            for (int i = 0; i < 4; i++) {
                resTextArray.add(g2l2QuestionData.get(integerArray[i]).getResourceText());
                resImageArray.add(g2l2QuestionData.get(integerArray[i]).getResourceImage());
                resAudioArray.add(g2l2QuestionData.get(integerArray[i]).getResourceImage());
                resIdArray.add(g2l2QuestionData.get(integerArray[i]).getResourceId());
            }
            String[] gifPaths = new String[]{(imagePath + resImageArray.get(0)),
                    (imagePath + resImageArray.get(1)),
                    (imagePath + resImageArray.get(2)),
                    (imagePath + resImageArray.get(3))};
            boleTohG2L1View.setQuestionGifs(readQuestionNo, gifPaths);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showImagesG3L1(String path, String studId) {
        try {
            readQuestionNo = getRandomNumber(0, g3l2QuestionData.size());
            currentPairList =  g3l2QuestionData.get(readQuestionNo).getNodelist();
            readQuestionNo= getRandomNumber(0, currentPairList.size());
            Collections.shuffle(currentPairList);
            int[] integerArray = getUniqueRandomNumber(0, currentPairList.size(), 2);
            String imagePath = sdCardPathString + "images/PairsGameL2/";

            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();

            setQuestionStartTime();
            studentID = studId;
            resourceID = g3l2QuestionData.get(integerArray[readQuestionNo]).getResourceId();
            questionId = resourceID;

            for (int i = 0; i < 2; i++) {
                resTextArray.add(currentPairList.get(integerArray[i]).getResourceText());
                resImageArray.add(currentPairList.get(integerArray[i]).getResourceImage());
                resAudioArray.add(currentPairList.get(integerArray[i]).getResourceImage());
                resIdArray.add(currentPairList.get(integerArray[i]).getResourceId());
            }
            Bitmap[] bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(0)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(1))};
            boleTohG3L1View.setQuestionImgs(readQuestionNo, bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readQuestion(int questionToRead) {
        ttsQuestion = resTextArray.get(questionToRead);
        Log.d("speechRate", "readQuestion: " + speechRate + "," + ttsQuestion);
        ttsService.play("Where is " + ttsQuestion);
    }

    @Override
    public void replayQuestionroundone() {
        readQuestion(readQuestionNo);
    }

    @Override
    public void startTTS(String text) {
        ttsService.play(text);
    }

    @Override
    public void set_g1_l2_data(String path) {
        gsonActGameData = fetchJsonData("RoundOneGameOne", path);
        g1l2QuestionData = gsonActGameData.getNodelist();
        String gameTitle = gsonActGameData.getNodeTitle();
        boleTohG1L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void set_g2_l2_data(String path) {
        gsonActGameData = fetchJsonData("RoundOneGameTwo", path);
        g2l2QuestionData = gsonActGameData.getNodelist();
        String gameTitle = gsonActGameData.getNodeTitle();
        boleTohG2L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void set_g3_l2_data(String path) {
        gsonPairGameData = fetchJsonData("RoundOneGameThree", path);
        g3l2QuestionData = gsonPairGameData.getNodelist();
        String gameTitle = gsonPairGameData.getNodeTitle();
        boleTohG3L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public String[] getOptions() {
        int[] optionsIds;
        int[] randomOptions;
        do {
            optionsIds = getUniqueRandomNumber(0, g2l2QuestionData.size(), 2);
        } while (optionsIds[0] == randomNumber || optionsIds[1] == randomNumber);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = g2l2QuestionData.get(optionsIds[0]).getResourceText();
        optionsText[randomOptions[1]] = g2l2QuestionData.get(optionsIds[1]).getResourceText();
        optionsText[randomOptions[2]] = g2l2QuestionData.get(randomNumber).getResourceText();

        optionsAudio[randomOptions[0]] = g2l2QuestionData.get(optionsIds[0]).getResourceAudio();
        optionsAudio[randomOptions[1]] = g2l2QuestionData.get(optionsIds[1]).getResourceAudio();
        optionsAudio[randomOptions[2]] = g2l2QuestionData.get(randomNumber).getResourceAudio();
        return optionsText;
    }

    @Override
    public void playOptionAudio(int optionNo){
        playMusic(optionsAudio[optionNo],getSdcardPath()+"Sounds/ActionGame/");
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
    public String[] getOptions_g3_l2() {
        int[] optionsIds;
        int[] randomOptions;
        do {
            optionsIds = getUniqueRandomNumber(0, g3l2QuestionData.size(), 2);
        } while (optionsIds[0] == randomNumber || optionsIds[1] == randomNumber);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = g3l2QuestionData.get(optionsIds[0]).getNodelist().get(0).getResourceText();
        optionsText[randomOptions[1]] = g3l2QuestionData.get(optionsIds[1]).getNodelist().get(1).getResourceText();
        optionsText[randomOptions[2]] = currentPairList.get(1).getResourceText();
        return optionsText;
    }

    @Override
    public void g1_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut) {
        int scoredMarks, totalMarks = 10;
        if (!timeOut) {
            String imageString = resTextArray.get(imageViewNum - 1);
            if (imageString.equalsIgnoreCase(ttsQuestion)) {
                boleTohG1L1View.setCelebrationView();
                scoredMarks = 10;
                playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
                int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
                boleTohG1L1View.setCurrentScore();
            } else {
                scoredMarks = 0;
                Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
                playMusic("Sounds/wrong.mp3", getSdcardPath());
            }
        } else {
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
        boleTohG1L1View.answerPostProcessing();
    }

    @Override
    public void g2_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut) {
        int scoredMarks, totalMarks = 10;
        if (!timeOut) {
            String imageString = resTextArray.get(imageViewNum - 1);
            if (imageString.equalsIgnoreCase(ttsQuestion)) {
                boleTohG2L1View.setCelebrationView();
                scoredMarks = 10;
                playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
                int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
                boleTohG2L1View.setCurrentScore();
            } else {
                scoredMarks = 0;
                Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
                playMusic("Sounds/wrong.mp3", getSdcardPath());
            }
        } else {
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
    }

    @Override
    public void g3_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut) {
        int scoredMarks, totalMarks = 10;
        if (!timeOut) {
            String imageString = resTextArray.get(imageViewNum - 1);
            if (imageString.equalsIgnoreCase(ttsQuestion)) {
                boleTohG3L1View.setCelebrationView();
                scoredMarks = 10;
                playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
                int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
                boleTohG3L1View.setCurrentScore();
            } else {
                scoredMarks = 0;
                Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
                playMusic("Sounds/wrong.mp3", getSdcardPath());
            }
        } else {
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
    }

    @Override
    public void checkFinalAnswer_g1_l2(String ans, int currentTeam) {
        int scoredMarks, totalMarks = 10;
        if (g1l2QuestionData.get(randomNumber).getResourceText().equalsIgnoreCase(ans)) {
            boleTohG1L2View.setCelebrationView();
            scoredMarks = 10;
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            boleTohG1L2View.setCurrentScore();
        } else {
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
    }

    @Override
    public String getQuestionAudio() {
        return questionAudioPath;
    }

    @Override
    public void checkFinalAnswer_g2_l2(String ans, int currentTeam) {
        int scoredMarks, totalMarks = 10;
        if (g2l2QuestionData.get(randomNumber).getResourceText().equalsIgnoreCase(ans)) {
            //  TODO correct answer animation + increase score of group
            boleTohG2L2View.setCelebrationView();
            scoredMarks = 10;
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            boleTohG2L2View.setCurrentScore();
        } else {
            //  TODO wrong answer animation
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);

    }

    @Override
    public void checkFinalAnswer_g3_l2(String ans, int currentTeam) {
        int scoredMarks, totalMarks = 10;
        if (currentPairList.get(1).getResourceText().equalsIgnoreCase(ans)) {
            boleTohG3L2View.setCelebrationView();
            scoredMarks = 10;
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            boleTohG3L2View.setCurrentScore();
        } else {
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
    }

    @Override
    public void g1_l2_checkAnswer(String ans) {
        String actualAns = g1l2QuestionData.get(randomNumber).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            boleTohG1L2View.setAnswer(ans);
        } else {
            boleTohG1L2View.showOptions_g1_l2();
        }
    }

    @Override
    public void g2_l2_checkAnswer(String ans) {
        String actualAns = g2l2QuestionData.get(randomNumber).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            boleTohG2L2View.setAnswer(ans);
        } else {
            boleTohG2L2View.showOptions();
        }
    }

    @Override
    public void g3_l2_checkAnswer(String ans) {
        String actualAns = currentPairList.get(1).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            boleTohG3L2View.setAnswer(ans);
        } else {
            boleTohG3L2View.showOptions();
        }
    }

    @Override
    public String getCurrentQuestion() {
        return currentPairList.get(1).getResourceQuestion();
    }

    @Override
    public String getCurrentHint() {
        return currentPairList.get(0).getResourceQuestion() + "," + currentPairList.get(0).getResourceText();
    }

    @Override
    public void setImage_gl_l2(String studId) {
        boleTohG1L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        String questionString = g1l2QuestionData.get(randomNumber).getResourceQuestion();
        String imagePath = getSdcardPath() + "images/PicGameL2/" + g1l2QuestionData.get(randomNumber).getResourceImage();
        questionAudioPath = getSdcardPath() + "Sounds/PicGame/" + g1l2QuestionData.get(randomNumber).getResourceAudio();
        Log.d("imagePath", "setImage_gl_l2: " + imagePath+"       SoundPath"+ questionAudioPath);
        setQuestionStartTime();
        studentID = studId;
        resourceID = g1l2QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;
        Toast.makeText(mContext, "actual ans: " + g1l2QuestionData.get(randomNumber).getResourceText(), Toast.LENGTH_SHORT).show();
        boleTohG1L2View.setQuestionImage(imagePath);
        boleTohG1L2View.setQuestionText(questionString);
    }

    @Override
    public void setImage_g2_l2(String studId) {
        boleTohG2L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g2l2QuestionData.size());
        String imagePath = getSdcardPath() + "images/ActionL2/" + g2l2QuestionData.get(randomNumber).getResourceImage();
        Toast.makeText(mContext, "actual ans: " + g2l2QuestionData.get(randomNumber).getResourceText(), Toast.LENGTH_SHORT).show();
        setQuestionStartTime();
        studentID = studId;
        resourceID = g2l2QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;
        boleTohG2L2View.setActionGif(imagePath);
        String questionString = g2l2QuestionData.get(randomNumber).getResourceQuestion();
        String questionAudio = g2l2QuestionData.get(randomNumber).getResourceAudio();
        boleTohG2L2View.setQuestionText(questionString,questionAudio);
    }

    @Override
    public void setImage_g3_l2(String studId) {
        boleTohG3L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        String path = getSdcardPath();
        currentPairList = g3l2QuestionData.get(randomNumber).getNodelist();
        Collections.shuffle(currentPairList);
        String hintImagePath = path + "images/PairsGameL2/" + currentPairList.get(0).getResourceImage();
        String questionImagePath = path + "images/PairsGameL2/" + currentPairList.get(1).getResourceImage();
        Toast.makeText(mContext, "Hint:::" + currentPairList.get(0).getResourceText(), Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, "Question:::" + currentPairList.get(1).getResourceText(), Toast.LENGTH_SHORT).show();
        setQuestionStartTime();
        studentID = studId;
        resourceID = g3l2QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;
        boleTohG3L2View.setPairsImages(hintImagePath, questionImagePath);
        String questionString = currentPairList.get(0).getResourceQuestion();
        boleTohG3L2View.setQuestionText(questionString);
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
                    score.setSessionID("" + appDatabase.getStatusDao().getValue("CurrentSession"));
                    score.setDeviceID("" + appDatabase.getStatusDao().getValue("DeviceID"));
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
            Log.d("SoundPth", "playMusic: " + path + fileName);
            if (mediaPlayerUtil == null) {
                mediaPlayerUtil = new MediaPlayerUtil(mContext);
                mediaPlayerUtil.initCallback(BoleTohPresenterImpl.this);
            }
            mediaPlayerUtil.playMedia(path + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void fragmentOnPause() {
        if (mediaPlayerUtil != null)
            mediaPlayerUtil.stopMedia();
        if (ttsService != null)
            ttsService.stop();
    }
}
