package com.example.pravin.angreziok.ui.bole_toh_round;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
    }
}
