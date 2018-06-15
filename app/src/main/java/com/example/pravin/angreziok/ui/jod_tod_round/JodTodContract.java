package com.example.pravin.angreziok.ui.jod_tod_round;

import android.graphics.Bitmap;

/**
 * Created by Pravin on 20/03/2018.
 */

public interface JodTodContract {

    interface JodTodView {
        void loadFragment();
    }

    interface JodTodPresenter {

        void doInitialWorkG1l1(String path);

        void showImagesG1L1(String path, String stidId);

        void readQuestion(int questionToRead);

        String getQuestionAudio();

        void readLetter(int questionToRead);

        void g1_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut);

        void startTTS(String text);

        void fragmentOnPause();

        String getSdcardPath();

        void set_g3_l2_data(int level);

        void set_g1_l2_data();

        void set_g2_data(int level);

        void g1_l2_checkAnswer(String ans);

        String g3_l2_getQuestionText(String studentId);

        String g1_l2_getQuestionText(String studentId);

        void g2_l2_checkAnswer(String result);

        String[] getOptions();

        String g2_l2_getQuestionText(String studentId);

        String g2_l1_getQuestionText(String studId);

        String[] getWordsToSay();

        String g3_l2_getQuestionAudio();

        void checkFinalAnswer_g1_l2(float ansPer, String score, int currentTeam);

        void checkFinalAnswer_g2_l2(float ansPer, String score, int currentTeam);

        void checkFinalAnswer_g2_l1(boolean correct, int score, int currentTeam);

        void checkFinalAnswer_g3_l2(String ans, int currentTeam);

        void playMusic(String fileName, String path);

        void setCurrentScore(int scoredMarks);
    }

    interface JodTod_G3_L2_View {

        void setGameTitleFromJson(String gameName);

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setQuestionText(String questionString);

    }

    interface JodTod_G1_L1_View {
        void setQuestionImgs(int readQuesNo, Bitmap... bitmaps);

        void setCelebrationView();

        void setCurrentScore();

        void setQuestionDynamically(String questionText);

        void setGameTitleFromJson(String gameName);
    }

    interface JodTod_G1_L2_View {

        void setGameTitleFromJson(String gameName);

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setAnswer(String ans, String sttWord, boolean match);

        void setQuestionText(String questionString);
    }

    interface JodTod_G2_L2_View {

        void setGameTitleFromJson(String gameName);

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setAnswer(String ans, String sttWord, boolean match);

        void hideOptionView();

        void setQuestionText(String questionString);
    }

    interface JodTod_G2_L1_View {

        void setGameTitleFromJson(String gameName);

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String questionText, String[] wordsToSay);
    }

}
