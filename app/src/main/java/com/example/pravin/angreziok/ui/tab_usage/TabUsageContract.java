package com.example.pravin.angreziok.ui.tab_usage;

import com.example.pravin.angreziok.modalclasses.Usage;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pravin on 19/03/2018.
 */

public interface TabUsageContract {

    interface TabUsageView {
        void setValuesToAdapter();
    }

    interface TabUsagePresenter {
        ArrayList<Usage> setData(ArrayList<JSONObject> usageData, ArrayList<Usage> listForAdapter);
    }
}
