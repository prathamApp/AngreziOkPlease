package com.example.pravin.angreziok.ui.start_menu;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by Pravin on 19/03/2018.
 */

public class StartMenuPresenter implements StartMenuContract.StartMenuPresenter {

    Context mContext;
    StartMenuContract.StartMenuView startMenuView;

    public StartMenuPresenter(Context context, StartMenuContract.StartMenuView startMenu) {
        mContext = context;
        startMenuView = startMenu;
    }

    @Override
    public void displayToast() {
        Toast.makeText(mContext, "In presenter displayToast", Toast.LENGTH_SHORT).show();
        startMenuView.showToast();
    }
}
