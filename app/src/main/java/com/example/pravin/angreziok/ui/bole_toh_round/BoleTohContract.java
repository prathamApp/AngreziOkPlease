package com.example.pravin.angreziok.ui.bole_toh_round;


public interface BoleTohContract {

    public interface BoleTohView{
        void loadFragment(int no);
    }

    public interface BoleTohPresenter {
        void checkAnswers(String spokenWord);
    }

    public interface BoleTohRoundOneView {
        public int[] getUniqueRandomNumber(int min, int max, int numSize);
        void doInitialWork();
        void checkAnswer(int imageViewNum);
    }

    public interface BoleTohRoundTwoView {
    }

}