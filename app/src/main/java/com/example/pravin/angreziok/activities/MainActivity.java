package com.example.pravin.angreziok.activities;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.animations.MyRotateAnimation;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Crl;

import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private MyRotateAnimation rotation;
    private StartNextRotate startNext;

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
//                .addMigrations(MIGRATION_1_2)
                .build();
        //BackupDatabase.backup(this);
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.d(":::",""+appDatabase.getCrlDao().getAllCrls().size());
                BackupDatabase.backup(MainActivity.this);
                return null;
            }
        }.execute();
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
           /*
           Do migration querries here like below

           database.execSQL("ALTER TABLE Crl "
                    + " ADD COLUMN crl_surname TEXT");
          */
        }
    };

    /*@OnClick(R.id.btn_add)
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
    }*/

/*    @OnClick(R.id.btn_displayLog)
    public void displayLogs() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.d(":::", appDatabase.getCrlDao().getAll().toString());
                return null;
            }
        }.execute();
    }*/

    public void rotateButton(View view) {
        /*RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        view.startAnimation(rotateAnimation);*/
        startRotation(0,360,view);
    }

    private void startRotation(float start, float end, View view) {
        // Calculating center point
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        Log.d("MainActivity", "centerX=" + centerX + ", centerY=" + centerY);
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //final Rotate3dAnimation rotation =new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        //Z axis is scaled to 0
        rotation = new MyRotateAnimation(start, end, centerX, centerY, 0f, true);
        rotation.setDuration(3000);
        rotation.setFillAfter(true);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //Uniform rotation
        rotation.setInterpolator(new LinearInterpolator());
        //Monitor settings
        startNext = new StartNextRotate(view);
        rotation.setAnimationListener(startNext);
        view.startAnimation(rotation);
    }

    private class StartNextRotate implements Animation.AnimationListener {
        View view;
        StartNextRotate(View view){
            this.view = view;
        }
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            Log.d("MainActivity", "onAnimationEnd......");
            view.startAnimation(rotation);
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }

}
