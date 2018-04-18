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

        String g3_l2_getQuestionText();

        String g1_l2_getQuestionText();

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

    }

}
