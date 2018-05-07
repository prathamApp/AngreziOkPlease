package com.example.pravin.angreziok.ui.samajh_ke_bolo_round;

public interface SamajhKeBoloContract {

    interface SamajhKeBoloView {
        void loadFragment();
    }

    interface SamajhKeBoloPresenter {

        String getSdcardPath();
        void set_g1_l2_data(String path);
        void setImage_gl_l2(String studentID);
        String getCurrentQuestion_g1_l2();
        void startTTS(String text);
        String[] getOptions_g1_l2();
        int[] getUniqueRandomNumber(int min, int max, int numSize);
        void g1_l2_checkAnswer(String ans);
        void checkFinalAnswer_g1_l2(String ans, int currentTeam);
        void playMusic(String fileName, String path);

        void setWords_g2_l2(String studentID);
        String getCurrentQuestion_g2_l2();
        void set_g2_l2_data(String path);
        void checkFinalAnswer_g2_l2(String ans, int currentTeam);
        String[] getOptions_g2_l2();
        void g2_l2_checkAnswer(String result);

        void set_g3_l2_data(String path);
        void setQuestion_g3_l2(String studentID);
        String[] getOptions_g3_l2();
        void checkAnswerOfOptions(String answer, int currentTeam);
        void checkAnswerOfStt(String answer,int currentTeam);

        void setCurrentScore(int scoredMarks);
    }

    interface SamajhKeBolo_G1_L2_View {
        void setCelebrationView();
        void setCurrentScore();
        void initiateQuestion();
        void setAnswer(String ans);
        void hideOptionView();
        void setQuestionImage(String path);
        void showOptions_g1_l2();
    }

    interface SamajhKeBolo_G2_L2_View {
        void setCelebrationView();
        void setCurrentScore();
        void initiateQuestion();
        void hideOptionView();
        void setQuestionWords(String[] words);
        void setAnswer(String ans);
        void showOptions_g2_l2();
    }

    interface SamajhKeBolo_G3_L2_View {
        void setCelebrationView();
        void setCurrentScore();
        void initiateQuestion();
        void hideOptionView();
        void setQuestion(String question, String questionAudio, String primaryQuestion);
        void setQuestionWords(String[] words);
        void setAnswer(String ans);
        void animateMic();
        void timerInit();
    }

}
