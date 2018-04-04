package com.example.pravin.angreziok.ui.bole_toh_round;


import android.graphics.Bitmap;

public interface BoleTohContract {

    interface BoleTohView {
        void loadFragment(int no);
    }

    interface BoleTohPresenter {
        String getSdcardPath();

        void doInitialWork(String path);

        void r1g1_checkAnswer(int imageViewNum, String path);

        int[] getUniqueRandomNumber(int min, int max, int numSize);

        void playMusic(String filename,String path);

        void replayQuestionroundone();

        void readQuestion(int questionToRead);

        void startTTS(String text);

        void r1g2_checkAnswer(String ans);

        void setr1g2_data(String path);

        String[] getOptions();

        void checkFinalAnswer(String ans);
    }

    interface BoleTohRoundOneView {
        void setQuestionImages(int readQuesNo, Bitmap... bitmaps);
    }

    interface BoleTohRoundTwoView {
        void setActionGif(String path);
        void setAnswer(String ans);
        void showOptions();
        void initiateQuestion();
        void hideOptionView();
        void setCelebrationView();
    }

}
