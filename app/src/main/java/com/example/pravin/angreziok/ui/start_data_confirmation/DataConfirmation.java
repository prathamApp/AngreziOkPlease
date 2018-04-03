package com.example.pravin.angreziok.ui.start_data_confirmation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataConfirmation extends AppCompatActivity {

    String totalStudents;
    ArrayList<PlayerModal> playerModalArrayList;

    @BindView(R.id.DataComTxt)
    TextView DataComTxt;

    String temp="LIST \n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_confirmation);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("studentList");

        for(int x =0;x<playerModalArrayList.size() ; x++){
            temp = temp+""+playerModalArrayList.get(x).studentName+"\n";
        }
        DataComTxt.setText(temp);
    }

    @OnClick(R.id.btn_startgm)
    public void skipGame(){        startActivity(new Intent(DataConfirmation.this, BoleToh.class));
    }
}
