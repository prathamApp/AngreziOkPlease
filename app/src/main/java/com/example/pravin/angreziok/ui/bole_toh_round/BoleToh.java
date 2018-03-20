package com.example.pravin.angreziok.ui.bole_toh_round;

import android.support.constraint.ConstraintLayout;
import android.os.Bundle;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.ui.bole_toh_round.bole_toh_fragments.Bole_toh_round_one;
import com.example.pravin.angreziok.util.PD_Utility;

public class BoleToh extends BaseActivity implements BoleTohContract.BoleTohView {

    ConstraintLayout cl_bole_toh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bole_toh);
        cl_bole_toh = (ConstraintLayout) findViewById(R.id.cl_bole_toh);

        loadFragment(1);
    }

    @Override
    public void loadFragment(int no) {
        Bundle bundle = new Bundle();
        PD_Utility.showFragment(BoleToh.this, new Bole_toh_round_one(), R.id.cl_bole_toh,
                bundle, Bole_toh_round_one.class.getSimpleName());

    }
}
