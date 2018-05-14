package com.example.pravin.angreziok.ui.tab_usage;

import android.content.Context;
import android.util.Log;

import com.example.pravin.angreziok.modalclasses.Usage;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Pravin on 19/03/2018.
 */

public class TabUsagePresenterImpl implements TabUsageContract.TabUsagePresenter {

    Context mContext;
    TabUsageContract.TabUsageView tabUsageView;
    String hours, minutes, seconds;


    public TabUsagePresenterImpl(Context context, TabUsageContract.TabUsageView tabUsageView) {
        mContext = context;
        this.tabUsageView = tabUsageView;
    }

    @Override
    public ArrayList<Usage> setData(ArrayList<JSONObject> usageData, ArrayList<Usage> listForAdapter) {
        try {
            for (int i = 0; i < usageData.size(); i++) {
                int currentSec = usageData.get(i).getInt("result");
                String FName = usageData.get(i).getString("FirstName");

                hours = String.format("%02d", currentSec / 3600) + "h";
                minutes = String.format("%02d", (currentSec % 3600) / 60) + "m";
                seconds = String.format("%02d", currentSec % 60) + "s";
                String currentTimeStamp = hours + " : " + minutes + " : " + seconds;
                Log.d("TabUsageActivity", "Name: " + FName + "     TimeStamp: " + currentTimeStamp);
                listForAdapter.add(new Usage(FName, currentTimeStamp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listForAdapter;
    }
}
