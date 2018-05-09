package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

import android.arch.persistence.room.Room;
import android.content.Context;
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
    SamajhKeBoloContract.SamajhKeBolo_G1_L2_View samajhKeBoloG1L2View;
    SamajhKeBoloContract.SamajhKeBolo_G3_L2_View samajhKeBoloG3L2View;
    SamajhKeBoloContract.SamajhKeBolo_G2_L2_View samajhKeBoloG2L2View;
    List<GenericModalGson> g2l2QuestionData, g1l2QuestionData, g3l2QuestionData, g3l2CurrentQuestionList, g1l2CurrentQuestionList;
    GenericModalGson whereWhenGameData, sayItGameData, askGameData;
    private AppDatabase appDatabase;

    int randomNumber, scoredMarks, totalMarks = 25;
    String questionStartTime, studentID, resourceID, questionId;

    public SamajhKeBoloPresenterImpl(Context mContext) {
        this.mContext = mContext;
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
    public void setCurrentScore(int currentMarks) {
        scoredMarks += currentMarks;
    }

    @Override
    public void set_g1_l2_data(String path) {
        whereWhenGameData = fetchJsonData("RoundThreeGameOne", path);
        g1l2QuestionData = whereWhenGameData.getNodelist();
        randomNumber = getRandomNumber(0, g1l2QuestionData.size());
        g1l2CurrentQuestionList = g1l2QuestionData.get(randomNumber).getNodelist();
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
    }

    @Override
    public void set_g3_l2_data(String path) {
        askGameData = fetchJsonData("RoundThreeGameThree", path);
        g3l2QuestionData = askGameData.getNodelist();
        randomNumber = getRandomNumber(0, g3l2QuestionData.size());
        g3l2CurrentQuestionList = g3l2QuestionData.get(randomNumber).getNodelist();
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

    public void addScore(String studentID, String resourceID, int questionId, int scoredMarks, int totalMarks, String startDateTime, int questionLevel) {
        try {
            final Score score = new Score();

            score.setSessionID("");
            score.setStudentID("" + studentID);
            score.setDeviceID("");
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
