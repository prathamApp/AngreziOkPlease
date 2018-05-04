package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.example.pravin.angreziok.domain.Crl;
import com.example.pravin.angreziok.domain.Session;

import org.json.JSONObject;

import java.util.ArrayList;
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

    @Query("select toDate from Session where SessionID = :SessionID")
    String getToDate(String SessionID);

    String query = "select FirstName,time from Student x INNER JOIN(select StudentID,sum(seconds) as time from(select b.StudentID, (strftime('%s',(substr(toDate, 7, 4)||'-'||substr(toDate, 4,2)||'-'||substr(toDate, 1,2)||' '||substr(toDate,11) )) - strftime('%s',(substr(fromDate, 7, 4)||'-'||substr(fromDate, 4,2)||'-'||substr(fromDate, 1,2)||' '||substr(fromDate,11))))" +
            " as seconds from Session a left outer join Attendance b on a.SessionID = b.SessionID) group by StudentID) y on x.StudentID = y.StudentID order by time desc";

    @Query(query)
    Cursor getStudentUsageData();
}
