package com.example.pravin.angreziok.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.pravin.angreziok.dao.CrlDao;
import com.example.pravin.angreziok.domain.Crl;

@Database(entities = {Crl.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "angrezi_ok_please";

    public abstract CrlDao getCrlDao();

}
