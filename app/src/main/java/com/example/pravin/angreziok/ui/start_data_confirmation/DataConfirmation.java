package com.example.pravin.angreziok.ui.start_data_confirmation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pravin.angreziok.R;

public class DataConfirmation extends AppCompatActivity {

    String totalStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_confirmation);

        Intent i = getIntent();
        totalStudents = i.getStringExtra("StudentName");

    }
}
