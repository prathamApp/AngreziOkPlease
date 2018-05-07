package com.example.pravin.angreziok.ui.bole_toh_round;


import android.graphics.Bitmap;

public interface BoleTohContract {

    interface BoleTohView {
        void loadFragment();
    }

    interface BoleTohPresenter {
        String getSdcardPath();

        void doInitialWork(String path);

        void g1_l1_checkAnswer(int imageViewNum, int currentTeam, boolean timeOut);

        int[] getUniqueRandomNumber(int min, int max, int numSize);

        void playMusic(String filename, String path);

        void replayQuestionroundone();

        void readQuestion(int questionToRead);

        void startTTS(String text);

        void g3_l2_checkAnswer(String ans);

        void g2_l2_checkAnswer(String ans);

        void g1_l2_checkAnswer(String ans);

        void set_g2_l2_data(String path);

        void set_g1_l2_data(String path);

        void set_g3_l2_data(String path);

        void setImage_g2_l2(String studentID);

        void setImage_g3_l2(String studentID);

        void setImage_gl_l2(String studentID);

        String[] getOptions();

        String[] getOptions_g1_l2();

        String[] getOptions_g3_l2();

        void checkFinalAnswer_g2_l2(String ans, int currentTeam);

        void checkFinalAnswer_g1_l2(String ans, int currentTeam);

        void checkFinalAnswer_g3_l2(String s, int currentTeam);

        void showImages(String path);

        String getCurrentQuestion();

        String getCurrentHint();

    }

    interface BoleToh_G1_L1_View {
        void setQuestionImages(int readQuesNo, Bitmap... bitmaps);

        void setCelebrationView();

        void setCurrentScore();
    }

    interface BoleToh_G1_L2_View {
        void setAnswer(String ans);

        void showOptions_g1_l2();

        void initiateQuestion(String questionString);

        void hideOptionView();

        void setCelebrationView();

        void setCurrentScore();

        void setQuestionText(String questionString);

        void setQuestionImage(String path);
    }

    interface BoleToh_G2_L2_View {
        void setActionGif(String path);

        void setAnswer(String ans);

        void showOptions();

        void initiateQuestion();

        void hideOptionView();

        void setCelebrationView();

        void setCurrentScore();

        void setQuestionText(String questionString);

    }

    interface BoleToh_G3_L2_View {
        void setPairsImages(String hintImage, String questionImage);

        void setAnswer(String ans);

        void showOptions();

        void initiateQuestion();

        void hideOptionView();

        void setCelebrationView();

        void setCurrentScore();

        void setQuestionText(String questionString);

    }

}
