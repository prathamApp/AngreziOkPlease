package com.example.pravin.angreziok.ui.start_menu;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;

import butterknife.ButterKnife;

public class StartMenu extends BaseActivity implements StartMenuContract.StartMenuView{

    private AppDatabase appDatabase;
    StartMenuContract.StartMenuPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        ButterKnife.bind(this);
        presenter = new StartMenuPresenterImpl(this,this);
        /* 1) In case migration needed and no problem with data loss then this would work

        appDatabase =  Room.databaseBuilder(this,
        AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
         */


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
        presenter.displayToast();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
           /*
           Do migration queries here like below

           database.execSQL("ALTER TABLE Crl "
                    + " ADD COLUMN crl_surname TEXT");
          */
        }
    };

    @Override
    public void showToast() {
        Toast.makeText(this, "In mainview showToast", Toast.LENGTH_SHORT).show();
    }
}
