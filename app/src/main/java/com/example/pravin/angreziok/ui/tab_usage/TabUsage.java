package com.example.pravin.angreziok.ui.tab_usage;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.adapters.TabUsageAdapter;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.modalclasses.Usage;
import com.example.pravin.angreziok.ui.start_menu.QRActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class TabUsage extends BaseActivity implements TabUsageContract.TabUsageView {

    ArrayList<JSONObject> usageData;
    ArrayList<Usage> listForAdapter;
    ArrayAdapter<Usage> listAdapter;
    ListView listView;
    AppDatabase appDatabase;
    TabUsageContract.TabUsagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_usage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.list_view);
        listForAdapter = new ArrayList<>();
        presenter = new TabUsagePresenterImpl(this, this);
        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        executeAsync();
    }

    private void executeAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Cursor cursor = appDatabase.getSessionDao().
                        getStudentUsageData();
                cursor.moveToFirst();
                ArrayList<JSONObject> allUsageData = new ArrayList<JSONObject>();
                JSONObject studentData = null;

                try {
                    while (!cursor.isAfterLast()) {
                        studentData = new JSONObject();
                        studentData.put("FirstName", cursor.getString(cursor.getColumnIndex("FirstName")));
                        studentData.put("result", cursor.getString(cursor.getColumnIndex("result")));
                        allUsageData.add(studentData);
                        cursor.moveToNext();
                    }
                    usageData = allUsageData;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                listForAdapter = presenter.setData(usageData, listForAdapter);
                setValuesToAdapter();
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @Override
    public void setValuesToAdapter() {
        listAdapter = new TabUsageAdapter(this, this.listForAdapter);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent qrScan = new Intent(this, QRActivity.class);
        finish();
        startActivity(qrScan);
    }

}
