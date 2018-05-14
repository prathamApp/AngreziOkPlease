package com.example.pravin.angreziok.ui.admin_console;


import android.graphics.Bitmap;

public interface AdminConsoleContract {

    interface AdminConsoleView {

        void transferFile(String fileName);
        void generateDialog(String msg);
        void stopDialog();
        void WifiTransfer();
    }

    interface AdminConsolePresenter {
        void insertInCrlTable(String fName, String lName, String mNumber, String uName, String password, String state, String mailID);
        void createJsonforTransfer();
        String getTransferFilename();
    }
}
