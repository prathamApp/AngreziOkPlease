package com.example.pravin.angreziok.ui.jod_tod_round;

/**
 * Created by Pravin on 20/03/2018.
 */

public interface JodTodContract {

    interface JodTodView {

    }

    interface JodTodPresenter {

        void startTTS(String text);
    }

    interface JodTod_G3_L2_View {

        void setCelebrationView();

        void setCurrentScore();

        void initiateQuestion();

    }
}
