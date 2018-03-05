package com.example.pravin.angreziok.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.pravin.angreziok.domain.Student;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    private final List<Student> students;

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public StudentAdapter(@NonNull Context context, @LayoutRes int resource, List<Student> students) {
        super(context, resource, students);
        this.students = students;
    }

    public Student getStudentById(@Nullable String studId) {
        if (studId != null) {
            for (int i = 0; i < students.size(); i++) {
                if (studId == students.get(i).getStudentID()) {
                    return students.get(i);
                }
            }
        }
        return null;
    }
}
