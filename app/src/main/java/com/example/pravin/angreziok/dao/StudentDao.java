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

    @Update
    int update(Student student);

    @Query("SELECT * FROM Student")
    List<Student> getAll();

    @Delete
    void deleteAll(Student... students);
}
