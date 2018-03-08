package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Score;


@Dao
public interface ScoreDao {

    @Insert
    long insert(Score score);

    @Insert
    long[] insertAll(Score... scores);

    @Update
    int update(Score score);

    @Delete
    void delete(Score score);

    @Delete
    void deleteAll(Score... scores);

}
