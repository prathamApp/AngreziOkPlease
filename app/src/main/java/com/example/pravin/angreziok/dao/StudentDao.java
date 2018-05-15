package com.example.pravin.angreziok.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.pravin.angreziok.domain.Student;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    long insert(Student student);

    @Insert
    long[] insertAll(Student... students);

    @Update
    int update(Student student);

    @Delete
    void delete(Student student);

    @Delete
    void deleteAll(Student... students);

    @Query("select * from Student where StudentID = :studentID")
    Student getStudent(String studentID);

    @Query("select * from Student")
    List<Student> getAllStudents();

    @Query("select * from Student where NewFlag = 1")
    List<Student> getAllNewStudents();

    @Query("update Student set NewFlag=0 where NewFlag = 1")
    void setNewStudentsToOld();

    @Query("update Student set NewFlag=0 where StudentID = :studentID")
    void setFlagFalse(String studentID);

    @Query("select FirstName from Student where StudentID = :studentID")
    String getStudentName(String studentID);

    @Query("select FirstName from Student where StudentID = :studentID")
    String checkStudent(String studentID);

}
