package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Attendance;
import com.example.pravin.angreziok.domain.Crl;

import java.util.List;

@Dao
public interface AttendanceDao {

    @Insert
    long insert(Attendance attendance);

    @Insert
    long[] insertAll(Attendance... attendances);

    @Update
    int update(Attendance attendance);

    @Delete
    void delete(Attendance attendance);

    @Delete
    void deleteAll(Attendance... attendances);

    @Query("select * from Attendance")
    List<Attendance> getAllAttendanceEntries();

    @Query("select StudentID from Attendance where SessionID = :sessionId")
    String getStudentBySession(String sessionId);

}
