package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Crl;
import com.example.pravin.angreziok.domain.Session;

import java.util.List;

@Dao
public interface SessionDao {

    @Insert
    long insert(Session session);

    @Insert
    long[] insertAll(Session... sessions);

    @Update
    int update(Session session);

    @Delete
    void delete(Session session);

    @Delete
    void deleteAll(Session... sessions);

    @Query("select * from Session")
    List<Session> getAllSessions();

    @Query("UPDATE Session SET toDate = :toDate where SessionID = :SessionID")
    void UpdateToDate(String SessionID, String toDate);

}
