package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

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
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.util.MediaPlayerUtil;
import com.example.pravin.angreziok.util.SDCardUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.pravin.angreziok.AOPApplication.getRandomNumber;
import static com.example.pravin.angreziok.BaseActivity.ttsService;
import static com.example.pravin.angreziok.ui.samajh_ke_bolo_round.SamajhKeBolo.playerModalArrayList;


public class SamajhKeBoloPresenterImpl implements SamajhKeBoloContract.SamajhKeBoloPresenter, MediaCallbacks {

    public TTSService ttsService;
    Context mContext;
    public MediaPlayerUtil mediaPlayerUtil;
    SamajhKeBoloContract.SamajhKeBolo_G1_L1_View samajhKeBoloG1L1View;
    SamajhKeBoloContract.SamajhKeBolo_G3_L1_View samajhKeBoloG3L1View;
    SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View;
    SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View;
    SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View;
    List<GenericModalGson> g1l1QuestionData, g2l2QuestionData, g1l2QuestionData, g3l2QuestionData, g3l1QuestionData, g3l2CurrentQuestionList, g1l2CurrentQuestionList, g3l1CurrentQuestionList;
    List<GenericModalGson> g1l1List;
    GenericModalGson whereWhenGameData, sayItGameData, askGameData;
    private AppDatabase appDatabase;
    ArrayList<String> resTextArray = new ArrayList<String>();
    ArrayList<String> resImageArray = new ArrayList<String>();
    ArrayList<String> resAudioArray = new ArrayList<String>();
    ArrayList<String> resIdArray = new ArrayList<String>();

    int randomNumber, scoredMarks, totalMarks = 25, readQuestionNo;
    String questionStartTime, studentID, resourceID, questionId,sdCardPathString,ttsQuestion;
    float speechRate = 1.0f;

    public SamajhKeBoloPresenterImpl(Context mContext) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G1_L1_View samajhKeBoloG1L1View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG1L1View = samajhKeBoloG1L1View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G3_L1_View samajhKeBoloG3L1View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG3L1View = samajhKeBoloG3L1View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG1L2View = samajhKeBoloG1L2View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View,
                              TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG2L2View = samajhKeBoloG2L2View;
        this.ttsService = ttsService;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    public SamajhKeBoloPresenterImpl(Context context, SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View,
                                     TTSService ttsService) {
        mContext = context;
        this.samajhKeBoloG3L2View = samajhKeBoloG3L2View;
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
    public void doInitialWorkG1l1(String path) {
        sdCardPathString = path;
        whereWhenGameData = fetchJsonData("RoundThreeGameOneLevelOne", path);
        g1l1QuestionData = whereWhenGameData.getNodelist();
        Log.d("SIZE", "doInitialWork: " + g1l1QuestionData.size());
    }



    @Override
    public void setCurrentScore(int currentMarks) {
        scoredMarks += currentMarks;
    }

    @Override
    public void set_g1_l2_data(String path) {
        whereWhenGameData = fetchJsonData("RoundThreeGameOne", path);
        g1l2QuestionData = whereWhenGameData.getNodelist();
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        g1l2CurrentQuestionList = g1l2QuestionData.get(randomNumber).getNodelist();
        String gameTitle = whereWhenGameData.getNodeTitle();
        samajhKeBoloG1L2View.setGameTitleFromJson(gameTitle);
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
    public void showImagesG1L1(String path, String studId) {
        try {
            readQuestionNo = getRandomNumber(0, g1l1QuestionData.size());
            g1l1List =  g1l1QuestionData.get(readQuestionNo).getNodelist();
            readQuestionNo= getRandomNumber(0, g1l1List.size());
            Collections.shuffle(g1l1List);
            int[] integerArray = getUniqueRandomNumber(0, g1l1List.size(), 2);
            String imagePath = sdCardPathString + "images/WhereGameL2/";

            resTextArray.clear();
            resIdArray.clear();
            resImageArray.clear();
            resAudioArray.clear();

            setQuestionStartTime();
            studentID = studId;
            resourceID = g1l1QuestionData.get(integerArray[readQuestionNo]).getResourceId();
            questionId = resourceID;

            for (int i = 0; i < 2; i++) {
                resTextArray.add(g1l1List.get(integerArray[i]).getResourceText());
                resImageArray.add(g1l1List.get(integerArray[i]).getResourceImage());
                resAudioArray.add(g1l1List.get(integerArray[i]).getResourceImage());
                resIdArray.add(g1l1List.get(integerArray[i]).getResourceId());
            }
            Bitmap[] bitmap = new Bitmap[]{BitmapFactory.decodeFile(imagePath + resImageArray.get(0)),
                    BitmapFactory.decodeFile(imagePath + resImageArray.get(1))};
            samajhKeBoloG1L1View.setQuestionImgs(readQuestionNo, bitmap);

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
                samajhKeBoloG1L1View.setCelebrationView();
                scoredMarks = 10;
                playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
                int currentTeamScore = Integer.parseInt(SamajhKeBolo.playerModalArrayList.get(currentTeam).studentScore);
                SamajhKeBolo.playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
                samajhKeBoloG1L1View.setCurrentScore();
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
    public void replayQuestionroundone() {
        readQuestion(readQuestionNo);
    }

    @Override
    public void setImage_gl_l2(String studId) {
        samajhKeBoloG1L2View.hideOptionView();
        Collections.shuffle(g1l2CurrentQuestionList);
        setQuestionStartTime();
        studentID = studId;
        resourceID = g1l2CurrentQuestionList.get(0).getResourceId();
        questionId = resourceID;
        String imagePath = getSdcardPath() + "images/WhereGameL2/" + g1l2CurrentQuestionList.get(0).getResourceImage();
        Log.d("imagePath", "setImage_gl_l2: " + imagePath);
        Toast.makeText(mContext, "actual ans: " + g1l2CurrentQuestionList.get(0).getResourceText(), Toast.LENGTH_SHORT).show();
        samajhKeBoloG1L2View.setQuestionImage(imagePath);
    }

    @Override
    public String getCurrentQuestion_g1_l2() {
        return g1l2CurrentQuestionList.get(randomNumber).getResourceQuestion();
    }

    @Override
    public void startTTS(String text) {
        ttsService.play(text);
    }

    @Override
    public void startTTSForCallbacks(String text) {
        ttsService.playAndOnDone(text, new Runnable() {
            @Override
            public void run() {
                samajhKeBoloG3L2View.timerInit();
            }
        });
    }

    @Override
    public String[] getOptions_g1_l2() {
        String[] options = new String[3];
        options[0] = g1l2CurrentQuestionList.get(0).getResourceText();
        options[1] = g1l2CurrentQuestionList.get(1).getResourceText();
        options[2] = g1l2CurrentQuestionList.get(2).getResourceText();
        List tempList = Arrays.asList(options);
        Collections.shuffle(tempList);
        return (String[]) tempList.toArray();
    }

    @Override
    public void g1_l2_checkAnswer(String ans) {
        String actualAns = g1l2CurrentQuestionList.get(0).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            samajhKeBoloG1L2View.setAnswer(ans);
        } else {
            samajhKeBoloG1L2View.showOptions_g1_l2();
        }
    }

    @Override
    public void checkFinalAnswer_g1_l2(String ans, int currentTeam) {

        if (ans.contains(g1l2CurrentQuestionList.get(0).getResourceText())) {
            samajhKeBoloG1L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            if (ans.split(" ").length > 1) {
                scoredMarks = 25;
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 25));
            } else {
                scoredMarks = 15;
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 15));
            }
            samajhKeBoloG1L2View.setCurrentScore();
        } else {
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
        scoredMarks = 0;
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
    public void setWords_g2_l2(String studId) {
        samajhKeBoloG2L2View.hideOptionView();
        randomNumber = getRandomNumber(0, g2l2QuestionData.size());
        setQuestionStartTime();
        studentID = studId;
        resourceID = g2l2QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;
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
        String gameTitle = sayItGameData.getNodeTitle();
        samajhKeBoloG2L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void set_g3_l2_data(String path) {
        askGameData = fetchJsonData("RoundThreeGameThree", path);
        g3l2QuestionData = askGameData.getNodelist();
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        g3l2CurrentQuestionList = g3l2QuestionData.get(randomNumber).getNodelist();
        String gameTitle = askGameData.getNodeTitle();
        samajhKeBoloG3L2View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void set_g3_l1_data(String path) {
        askGameData = fetchJsonData("RoundThreeGameThreeLevelOne", path);
        g3l1QuestionData = askGameData.getNodelist();
        g3l1CurrentQuestionList = g3l1QuestionData;
        String gameTitle = askGameData.getNodeTitle();
        samajhKeBoloG3L1View.setGameTitleFromJson(gameTitle);
    }

    @Override
    public void setQuestion_g3_l2(String studId) {
        samajhKeBoloG3L2View.hideOptionView();
        Collections.shuffle(g3l2CurrentQuestionList);
        setQuestionStartTime();
        studentID = studId;
        resourceID = g3l2CurrentQuestionList.get(0).getResourceId();
        questionId = resourceID;
        samajhKeBoloG3L2View.setQuestion(getCurrentQuestion_g3_l2(),getCurrentQuestionAudio_g3_l2(),getPrimaryQuestion_g3_l2());
        samajhKeBoloG3L2View.setQuestionWords(getOptions_g3_l2());
    }

    public String getPrimaryQuestion_g3_l2() {
        return g3l2QuestionData.get(randomNumber).getResourceText();
    }

    public String getCurrentQuestion_g3_l2() {
        return g3l2CurrentQuestionList.get(0).getResourceQuestion();
    }

    public String getCurrentQuestionAudio_g3_l2() {
        return g3l2CurrentQuestionList.get(0).getResourceAudio();
    }

    @Override
    public void checkFinalAnswer_g2_l2(String ans, int currentTeam) {
        if (ans.contains(g2l2QuestionData.get(randomNumber).getResourceType())) {
            samajhKeBoloG2L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            if (ans.split(" ").length > 1) {
                scoredMarks = 25;
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 25));
            } else {
                scoredMarks = 15;
                playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 15));
            }
            samajhKeBoloG2L2View.setCurrentScore();
        } else {
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
        scoredMarks = 0;
    }

    @Override
    public String[] getOptions_g2_l2() {
        return g2l2QuestionData.get(randomNumber).getResourceText().split(",");
    }

    @Override
    public String[] getOptions_g3_l2() {
        String[] options = new String[4];
        options[0] = g3l2CurrentQuestionList.get(0).getResourceText();
        options[1] = g3l2CurrentQuestionList.get(1).getResourceText();
        options[2] = g3l2CurrentQuestionList.get(2).getResourceText();
        options[3] = g3l2CurrentQuestionList.get(3).getResourceText();
        List tempList = Arrays.asList(options);
        Collections.shuffle(tempList);
        return (String[]) tempList.toArray();
    }

    @Override
    public void checkAnswerOfOptions(String answer, int currentTeam) {
        if (g3l2CurrentQuestionList.get(0).getResourceText().equalsIgnoreCase(answer)) {
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 15));
            scoredMarks = 15;
        }
        samajhKeBoloG3L2View.setCurrentScore();
        samajhKeBoloG3L2View.animateMic();
    }


    @Override
    public void checkAnswerOfStt(String answer, int currentTeam) {
        if (answer.equalsIgnoreCase(g3l2CurrentQuestionList.get(0).getResourceText())) {
            samajhKeBoloG3L2View.setCelebrationView();
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(playerModalArrayList.get(currentTeam).studentScore);
            playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            scoredMarks = 25;
            samajhKeBoloG3L2View.setCurrentScore();
        } else {
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

    @Override
    public String[] getOptions() {
        int[] optionsIds;
        int[] randomOptions;
        do {
            optionsIds = getUniqueRandomNumber(0, g3l1QuestionData.size(), 2);
        } while (optionsIds[0] == randomNumber || optionsIds[1] == randomNumber);
        String[] optionsText = new String[3];
        randomOptions = getUniqueRandomNumber(0, 3, 3);
        optionsText[randomOptions[0]] = g3l1QuestionData.get(optionsIds[0]).getResourceText();
        optionsText[randomOptions[1]] = g3l1QuestionData.get(optionsIds[1]).getResourceText();
        optionsText[randomOptions[2]] = g3l1QuestionData.get(randomNumber).getResourceText();
        return optionsText;
    }

    @Override
    public void setImage_g3_l1(String studId) {
        samajhKeBoloG3L1View.hideOptionView();
        randomNumber = getRandomNumber(0, g3l1QuestionData.size());
        String questionNative = g3l1QuestionData.get(randomNumber).getResourceQuestion();
        Toast.makeText(mContext, "actual ans: " + g3l1QuestionData.get(randomNumber).getResourceText(), Toast.LENGTH_SHORT).show();
        setQuestionStartTime();
        studentID = studId;
        resourceID = g3l1QuestionData.get(randomNumber).getResourceId();
        questionId = resourceID;
        samajhKeBoloG3L1View.setQuestionTextNative(questionNative);
        String questionString = g3l1QuestionData.get(randomNumber).getResourceQuestion();
        samajhKeBoloG3L1View.setQuestionText(questionString);
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

    @Override
    public void checkFinalAnswer_g3_l1(String ans, int currentTeam) {
        int scoredMarks, totalMarks = 10;
        if (g3l1QuestionData.get(randomNumber).getResourceText().equalsIgnoreCase(ans)) {
            //  TODO correct answer animation + increase score of group
            samajhKeBoloG3L1View.setCelebrationView();
            scoredMarks = 10;
            playMusic("Sounds/BilkulSahijawab.mp3", getSdcardPath());
            int currentTeamScore = Integer.parseInt(SamajhKeBolo.playerModalArrayList.get(currentTeam).studentScore);
            SamajhKeBolo.playerModalArrayList.get(currentTeam).setStudentScore(String.valueOf(currentTeamScore + 10));
            samajhKeBoloG3L1View.setCurrentScore();
        } else {
            //  TODO wrong answer animation
            scoredMarks = 0;
            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            playMusic("Sounds/wrong.mp3", getSdcardPath());
        }
        addScore(studentID, resourceID, 0, scoredMarks, totalMarks, questionStartTime, 0);
    }

    @Override
    public void g3_l1_checkAnswer(String ans) {
        String actualAns = g3l1QuestionData.get(randomNumber).getResourceText();

        if (ans.equalsIgnoreCase(actualAns)) {
            samajhKeBoloG3L1View.setAnswer(ans);
        } else {
            samajhKeBoloG3L1View.showOptions();
        }
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

    @Override
    public void fragmentOnPause() {
        if (mediaPlayerUtil != null)
            mediaPlayerUtil.stopMedia();
        if(ttsService != null )
            ttsService.stop();
    }

}
