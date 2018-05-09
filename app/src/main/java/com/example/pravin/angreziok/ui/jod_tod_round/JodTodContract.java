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

        void fragmentOnPause();

        String getSdcardPath();

        void set_g3_l2_data();

        void set_g1_l2_data();

        void set_g2_l2_data();

        void setWord_g2_l2();

        void g1_l2_checkAnswer(String ans);

        String g3_l2_getQuestionText(String studentId);

        String g1_l2_getQuestionText(String studentId);

        void g2_l2_checkAnswer(String result);

        String[] getOptions();

        String g2_l2_getQuestionText(String studentId);

        String g3_l2_getQuestionAudio();

        void checkFinalAnswer_g1_l2(float ansPer, String score, int currentTeam);

        void checkFinalAnswer_g2_l2(float ansPer, String score, int currentTeam);

        void checkFinalAnswer_g3_l2(String ans, int currentTeam);

        void playMusic(String fileName, String path);

        void setCurrentScore(int scoredMarks);
    }

    interface JodTod_G3_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setQuestionText(String questionString);

    }

    interface JodTod_G1_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setAnswer(String ans,String sttWord,boolean match);

        void setQuestionText(String questionString);
    }

    interface JodTod_G2_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion(String question);

        void setAnswer(String ans,String sttWord,boolean match);

        void hideOptionView();

        void setQuestionText(String questionString);
    }

}
