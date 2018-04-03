package com.example.pravin.angreziok.ui.start_data_confirmation;

import android.net.Uri;

import com.google.zxing.Result;

/**
 * Created by Pravin on 19/03/2018.
 */

public interface DataConfirmationContract {

    public interface DataConfirmationView{}

    public interface DataConfirmationPresenter{
        void qrEntryProcess(Result result);
    }

}
