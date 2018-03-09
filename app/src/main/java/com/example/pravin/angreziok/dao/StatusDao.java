package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Session;
import com.example.pravin.angreziok.domain.Status;

import java.util.List;

@Dao
public interface StatusDao {

    @Insert
    long insert(Status status);

    @Insert
    long[] insertAll(Status... statuses);

    @Update
    int update(Status status);

    @Delete
    void delete(Status status);

    @Delete
    void deleteAll(Status... statuses);

    @Query("select * from Status")
    List<Status> getAllStatuses();

    @Query("Select value from Status where statusKey = :key")
    String getValue(String key);

}
