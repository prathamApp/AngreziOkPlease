package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Score;

import java.util.List;

@Dao
public interface ScoreDao {

    @Insert
    long insert(Score score);

    @Update
    int update(Score score);

    @Query("SELECT * FROM Score")
    List<Score> getAll();

    @Delete
    void deleteAll(Score... scores);
}
