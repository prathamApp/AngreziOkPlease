package com.example.pravin.angreziok.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.pravin.angreziok.R;

import butterknife.BindView;

public class StartMenu extends AppCompatActivity {

    @BindView(R.id.nav_options)
    RecyclerView nav_options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
    }
}
