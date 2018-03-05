package com.example.pravin.angreziok.activities;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.domain.Crl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_id)
    EditText et_id;
    @BindView(R.id.et_name)
    EditText et_name;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /* 1) In case migration needed and no problem with data loss then this would work
        */

        /*
        appDatabase =  Room.databaseBuilder(this,
        AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();*/


        /* 2) In case migration needed and want to keep data as it is then this would work
            but the case is
            SQLite supports a limited subset of ALTER TABLE.
             The ALTER TABLE command in SQLite allows the user to rename a table or to add a new column to an existing table.
             It is not possible to rename a column, remove a column, or add or remove constraints from a table.
         */

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
        //BackupDatabase.backup(this);
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Crl "
                    + " ADD COLUMN crl_surname TEXT");
        }
    };

    @OnClick(R.id.btn_add)
    public void addCrl() {
        String id, name;
        id = et_id.getText().toString();
        name = et_name.getText().toString();

        if (id != null && name != null && !(id.equalsIgnoreCase("") || name.equalsIgnoreCase(""))) {
            final Crl crl = new Crl();
            crl.setCrl_id(id);
            crl.setCrl_name(name);
            crl.setCrl_lastname("last");
            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    Boolean existFlag = false;
                    List<Crl> crlList;
                    crlList = appDatabase.getCrlDao().getAll();
                    for (Crl crlObj : crlList) {
                        if (crlObj.getCrl_id().equalsIgnoreCase(crl.getCrl_id())) {
                            existFlag = true;
                            break;
                        }
                    }
                    if (existFlag)
                        appDatabase.getCrlDao().update(crl);
                    else
                        appDatabase.getCrlDao().insert(crl);
                    return null;
                }
            }.execute();

            Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Problem getting text", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_displayLog)
    public void displayLogs() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(":::", appDatabase.getCrlDao().getAll().toString());
                return null;
            }
        }.execute();
    }
}
