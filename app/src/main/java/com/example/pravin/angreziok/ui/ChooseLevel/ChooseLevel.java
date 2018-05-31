package com.example.pravin.angreziok.ui.ChooseLevel;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.modalclasses.PlayerModal;
import com.example.pravin.angreziok.ui.admin_console.AdminConsoleContract;
import com.example.pravin.angreziok.ui.admin_console.AdminConsolePresenterImpl;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleToh;
import com.example.pravin.angreziok.ui.jod_tod_round.JodTod;
import com.example.pravin.angreziok.ui.shareConent.DashboardActivity;
import com.example.pravin.angreziok.util.FTPConnect;
import com.example.pravin.angreziok.util.FTPInterface;
import com.example.pravin.angreziok.util.MessageEvent;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseLevel extends BaseActivity {

    @BindView(R.id.tv_Level1)
    TextView tv_Level1;
    @BindView(R.id.tv_Level2)
    TextView tv_Level2;
    ArrayList<PlayerModal> playerModalArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        Bundle extraBundle = intent.getExtras();
        playerModalArrayList = extraBundle.getParcelableArrayList("playerModalArrayList");

    }

    @OnClick(R.id.tv_Level1)
    public void startGame(){
        Intent intent = new Intent(this, BoleToh.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("playerModalArrayList", playerModalArrayList);
        intent.putExtras(bundle);
        intent.putExtra("level","L1");
        finish();
        startActivity(intent);

    }

    @OnClick(R.id.tv_Level2)
    public void startGame1(){
        Intent intent = new Intent(this, BoleToh.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("playerModalArrayList", playerModalArrayList);
        intent.putExtras(bundle);
        intent.putExtra("level","L2");
        finish();
        startActivity(intent);

    }

}