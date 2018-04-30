package com.example.pravin.angreziok.ui.admin_console;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Crl;
import com.example.pravin.angreziok.services.TTSService;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleTohContract;
import java.util.UUID;

public class AdminConsolePresenterImpl implements AdminConsoleContract.AdminConsolePresenter{

    String ttsQuestion;
    float speechRate = 1.0f;
    public TTSService ttsService;
    Context mContext;
    String sdCardPathString;
    BoleTohContract.BoleToh_G2_L2_View boleTohG2L2View;
    private AppDatabase appDatabase;


    public AdminConsolePresenterImpl(Context mContext) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
    }

    @Override
    public void insertInCrlTable(String fName, String lName, String mNumber, String uName, String password, String state, String mailID) {
        try {
            final Crl crl = new Crl();
            crl.setCRLId(UUID.randomUUID().toString());
            crl.setEmail(mailID);
            crl.setFirstName(fName);
            crl.setLastName(lName);
            crl.setMobile(mNumber);
            crl.setUserName(uName);
            crl.setPassword(password);
            crl.setState(state);
            crl.setProgramId(2);
            crl.setNewCrl(true);
            crl.setCreatedBy("");

            new AsyncTask<Object , Void ,Object>(){
                @Override
                protected Object doInBackground(Object... objects) {
                    appDatabase.getCrlDao().insert(crl);
                    BackupDatabase.backup(mContext);
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
