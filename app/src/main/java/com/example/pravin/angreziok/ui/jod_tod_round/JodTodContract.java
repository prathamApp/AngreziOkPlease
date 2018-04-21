package com.example.pravin.angreziok.ui.jod_tod_round;

/**
 * Created by Pravin on 20/03/2018.
 */

public interface JodTodContract {

    interface JodTodView {
        void loadFragment();
    }

    interface JodTodPresenter {

        void startTTS(String text);

        String getSdcardPath();

        void set_g3_l2_data();

        void set_g1_l2_data();

        void set_g2_l2_data();

        void setWord_g2_l2();

        void g1_l2_checkAnswer(String ans);

        String g3_l2_getQuestionText();

        String g1_l2_getQuestionText();

        void g2_l2_checkAnswer(String result);

        String[] getOptions();

        String g2_l2_getQuestionText();

        void checkFinalAnswer_g1_l2(String ans, int currentTeam);

        void checkFinalAnswer_g3_l2(String ans, int currentTeam);

        void playMusic(String fileName, String path);
    }

    interface JodTod_G3_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

    }

    interface JodTod_G1_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setAnswer(String ans,String sttWord);

    }

    interface JodTod_G2_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setAnswer(String ans,String sttWord);

        void showOptions();

        void hideOptionView();

        void setQuestionText(String setQuestionText);

    }

}
