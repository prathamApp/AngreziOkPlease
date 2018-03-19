package com.example.pravin.angreziok.ui.start_menu;

/**
 * Created by Pravin on 19/03/2018.
 */

public interface StartMenuContract {

    public interface StartMenuView{
        void showToast();
    }

    public interface StartMenuPresenter{
        void displayToast();
    }
}
