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

    @Insert
    long[] insertAll(Crl... crls);

    @Update
    int update(Crl crl);

    @Delete
    void delete(Crl crl);

    @Delete
    void deleteAll(Crl... crls);

    @Query("select * from Crl where CRLId = :crlID")
    Crl getCrl(String crlID);

    @Query("select * from Crl")
    List<Crl> getAllCrls();

    @Query("select * from Crl where newCrl = 1")
    List<Crl> getAllNewCrls();

    @Query("select FirstName from Crl where UserName = :uName and Password = :uPass")
    String checkCrls(String uName, String uPass);
}
