package com.example.pravin.angreziok.ui.bole_toh_round;


public interface BoleTohContract {

    public interface BoleTohView{
        void loadFragment(int no);
    }

    public interface BoleTohPresenter {
    }

    public interface BoleTohRoundOneView {
        void doInitalWork();
        void showImages();
    }

    public interface BoleTohRoundTwoView {
    }

}
