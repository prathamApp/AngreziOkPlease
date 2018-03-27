package com.example.pravin.angreziok.ui.bole_toh_round;


import android.content.Context;
import android.graphics.Bitmap;

public interface BoleTohContract {

    public interface BoleTohView {
        void loadFragment(int no);
    }

    public interface BoleTohPresenter {
        String getSdcardPath();

        void startSTT(Context context);

        void doInitialWork(String path);

        void checkAnswer(int imageViewNum,String path);

        public int[] getUniqueRandomNumber(int min, int max, int numSize);

        void playMusic(String filename);

        void readQuestion(int questionToRead);
        void startTTS(String text);
    }

    public interface BoleTohRoundOneView {
        void setQuestionImages(int readQuesNo, Bitmap... bitmaps);
    }

    public interface BoleTohRoundTwoView {
    }

}
