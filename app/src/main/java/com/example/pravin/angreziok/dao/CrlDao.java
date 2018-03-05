package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Crl;

import java.util.List;

@Dao
public interface CrlDao {

    @Insert
    long insert(Crl crl);

    @Update
    int update(Crl crl);

    @Query("SELECT * FROM Crl")
    List<Crl> getAll();

    @Delete
    void deleteAll(Crl... crls);
}
