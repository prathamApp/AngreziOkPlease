package com.example.pravin.angreziok.contentplayer;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.pravin.angreziok.BaseActivity;
import com.example.pravin.angreziok.R;
/*TODO chnages
import com.example.pravin.angreziok.modalDBHelpers.AttendanceDBHelper;
import com.example.pravin.angreziok.modalDBHelpers.StatusDBHelper;
import com.example.pravin.angreziok.modalDBHelpers.StudentDBHelper;*/

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.tv_web_view_gname)
    TextView tv_GameName;
    @BindView(R.id.tv_web_view_sname)
    TextView tv_StudentName;

    WebView webView;
    String gamePath,currentGameName;
    static String webResId, studentId, gameLevel;
    /*TODO chnages
    StatusDBHelper statusDBHelper;
    AttendanceDBHelper attendanceDBHelper;
    StudentDBHelper studentDBHelper;*/

    TextToSpeechCustom tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        tts = new TextToSpeechCustom(this, 0.4f);
        webView = (WebView) findViewById(R.id.loadPage);

        /*TODO changes
        statusDBHelper = new StatusDBHelper(this);
        attendanceDBHelper = new AttendanceDBHelper(this);
        studentDBHelper = new StudentDBHelper(this);

        String curSession = statusDBHelper.getValue("CurrentSession");
        studentId = attendanceDBHelper.getCurrenStudentId(curSession);
        String studentName = studentDBHelper.getStudentName(studentId);
*/
        gamePath = getIntent().getStringExtra("path");
        webResId = getIntent().getStringExtra("resId");
        currentGameName = getIntent().getStringExtra("currentGameName");
        gameLevel = getIntent().getStringExtra("gameLevel");
        tv_GameName.setText("Game Name: "+currentGameName);
//      TODO changes  tv_StudentName.setText("Student Name: "+studentName);
        tv_GameName.setTextColor(Color.WHITE);
        tv_StudentName.setTextColor(Color.WHITE);
        createWebView(Uri.parse(gamePath));
    }

    @SuppressLint("JavascriptInterface")
    public void createWebView(Uri GamePath) {

        String myPath = GamePath.toString();
        webView.loadUrl(myPath);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new JSInterface(this, webView,tts,this), "Android");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                webView.setWebContentsDebuggingEnabled(true);
            }
        }
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.clearCache(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        webView.loadUrl("about:blank");
        if (tts!=null){
            tts.stopSpeaker();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

