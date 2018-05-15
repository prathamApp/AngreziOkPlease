package com.example.pravin.angreziok.ui.admin_console;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
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
import com.example.pravin.angreziok.util.FTPConnect;
import com.example.pravin.angreziok.util.FTPInterface;
import com.example.pravin.angreziok.util.MessageEvent;
import com.example.pravin.angreziok.util.Utility;

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

public class AdminConsole extends BaseActivity implements AdminConsoleContract.AdminConsoleView, FTPInterface.PushPullInterface {

    @BindView(R.id.ll_operations)
    LinearLayout ll_Operations;
    @BindView(R.id.ll_adminform)
    LinearLayout ll_AddAdminForm;

    @BindView(R.id.tv_admin_title)
    TextView adminTitle;

    @BindView(R.id.btn_add_admin)
    ImageButton btn_newAdmin;
    @BindView(R.id.btn_transfer_data)
    ImageButton btn_transferData;
    @BindView(R.id.btn_push)
    ImageButton btn_push;
    @BindView(R.id.btn_self_push)
    ImageButton btn_self_push;

    @BindView(R.id.et_crlFirstName)
    EditText et_FirstName;
    @BindView(R.id.et_crlLastName)
    EditText et_LastName;
    @BindView(R.id.et_crlMobNumber)
    EditText et_MobNumber;
    @BindView(R.id.et_crlMailId)
    EditText et_MailId;
    @BindView(R.id.et_crlUserName)
    EditText et_UserName;
    @BindView(R.id.et_crlPassword)
    EditText et_Password;
    @BindView(R.id.sp_crlState)
    Spinner sp_crlState;
    @BindView(R.id.btn_submit_crl)
    Button btn_Submit;

    ArrayList<String> path = new ArrayList<String>();
    FTPConnect ftpConnect;
    RelativeLayout receiveFtpDialogLayout;
    TextView tv_ssid, tv_ip, tv_port, tv_Details;
    Button btn_Disconnect;
    Dialog receiverDialog;
    private ProgressBar recievingProgress;

    RelativeLayout ftpDialogLayout;
    EditText edt_HostName;
    EditText edt_Port;
    Button btn_Connect;
    ListView lst_networks;
    private boolean NoDataToTransfer = false;


    boolean addAdminFlg = false;
    AdminConsolePresenterImpl adminPresenter;
    ProgressDialog progress;
    private String filename;
    BluetoothAdapter btAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
/*        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyWakelockTag");
        wakeLock.acquire();*/
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ftpConnect = new FTPConnect(AdminConsole.this, AdminConsole.this, AdminConsole.this);
        adminTitle.setText("Admin Console");
        addAdminFlg = false;
        adminPresenter = new AdminConsolePresenterImpl(AdminConsole.this, this);
        addStatesData();
    }

    @Override
    public void onBackPressed() {
        if (addAdminFlg) {
            addAdminFlg = false;
            adminTitle.setText("Admin Console");
            ll_Operations.setVisibility(View.VISIBLE);
            ll_AddAdminForm.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }

    @OnClick(R.id.btn_transfer_data)
    public void transferData() {
        // Generate Json file
        adminPresenter.transferFlg = true;
        adminPresenter.createJsonforTransfer();
        //transferFile(adminPresenter.getTransferFilename());

    }

    @Override
    public void WifiTransfer(){
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();
        if (!wifiEnabled) {
            wifiManager.setWifiEnabled(true);
        }

        // Operation
        // Display ftp dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_visible_wifi_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ftpDialogLayout = dialog.findViewById(R.id.ftpDialog);
        lst_networks = dialog.findViewById(R.id.lst_network);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        // Onlistener
        ArrayList<String> networkList = ftpConnect.scanNearbyWifi();

        lst_networks.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.lst_wifi_item, R.id.label, networkList));

        Button refresh = dialog.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Onlistener
                ArrayList<String> networkList = ftpConnect.scanNearbyWifi();
                Log.d("Network List :::", String.valueOf(networkList));
                lst_networks.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.lst_wifi_item, R.id.label, networkList));
            }
        });

        // listening to single list item on click
        lst_networks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String ssid = ((TextView) view).getText().toString();
                // check if pratham hotspot selected or not
                if (ssid.contains("PrathamHotSpot_")) {
                    // connect to wifi
                    ftpConnect.connectToPrathamHotSpot(ssid);
                    dialog.dismiss();
                    Toast.makeText(AdminConsole.this, "Wifi SSID : " + ssid, Toast.LENGTH_SHORT).show();
                    // Display ftp dialog
                    Dialog dialog = new Dialog(AdminConsole.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.connect_to_ftpserver_dialog);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                    ftpDialogLayout = dialog.findViewById(R.id.ftpDialog);
                    edt_HostName = dialog.findViewById(R.id.edt_HostName);
                    edt_Port = dialog.findViewById(R.id.edt_Port);
                    btn_Connect = dialog.findViewById(R.id.btn_Connect);
                    tv_Details = dialog.findViewById(R.id.tv_details);

                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    btn_Connect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            WifiInfo info = wifiManager.getConnectionInfo();
                            String ssid = info.getSSID();
                            if (ssid.contains("PrathamHotSpot_"))
                                ftpConnect.connectFTPHotspot("TransferUsage", "192.168.43.1", "8080");
                            else
                                Toast.makeText(AdminConsole.this, "Connected to Wrong Network !!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AdminConsole.this, "Invalid Network !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick(R.id.btn_self_push)
    public void pushCurrentTabData() {
        adminPresenter.currentPush = true;
        adminPresenter.createJsonforTransfer();
    }

    @OnClick(R.id.btn_push)
    public void pushToServer() {
        try {
            adminPresenter.pushToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateDialog(String msg) {
        progress = new ProgressDialog(AdminConsole.this);
        progress.setMessage(msg);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    @Override
    public void stopDialog() {
        progress.dismiss();
    }

    @Override
    public void transferFile(String filename) {

        generateDialog("Please Wait...");

        this.filename = filename;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(), "This device doesn't give bluetooth support.", Toast.LENGTH_LONG).show();
        } else {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 2000);
            startActivityForResult(discoveryIntent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000 && requestCode == 1) {
            String packageName = "", className = "";
            Boolean found = false;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String f = Environment.getExternalStorageDirectory() + "/.AOPInternal/UsageJsons/" + filename + ".json";
            File file = new File(f);
            int x = 0;
            if (file.exists()) {
                PackageManager pm = getPackageManager();
                List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
                if (appsList.size() > 0) {

                    for (ResolveInfo info : appsList) {
                        packageName = info.activityInfo.packageName;
                        if (packageName.equals("com.android.bluetooth")) {
                            className = info.activityInfo.name;
                            found = true;
                            break;// found
                        }
                    }
                    if (!found) {
                        Toast.makeText(this, "Bluetooth not in list", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Uri fileURI = FileProvider.getUriForFile(
                                    AdminConsole.this,
                                    AdminConsole.this.getApplicationContext()
                                            .getPackageName() + ".provider", file);
                            Log.d("filename::", fileURI + "");
                            intent.putExtra(Intent.EXTRA_STREAM, fileURI);
                        } else {
                            intent.setType("text/plain");
                            Log.d("filename::", Uri.fromFile(file) + "");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        }
                        intent.setClassName(packageName, className);
                        startActivityForResult(intent, 3);
                    }
                }
            }
        } else if (requestCode == 3) {
            filename = "";
            progress.dismiss();
            adminPresenter.clearRecordsOrNot();
        } else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "File not found in UsageJsons folder", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_add_admin)
    public void createAdmin() {
        addAdminFlg = true;
        adminTitle.setText("Add New CRL");
        ll_Operations.setVisibility(View.GONE);
        ll_AddAdminForm.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_submit_crl)
    public void submitCRL() {
        String fName, lName, mNumber, mailID, uName, password, state;
        fName = et_FirstName.getText().toString();
        lName = et_LastName.getText().toString();
        mNumber = et_MobNumber.getText().toString();
        mailID = et_MailId.getText().toString();
        uName = et_UserName.getText().toString();
        password = et_Password.getText().toString();
        state = sp_crlState.getSelectedItem().toString();

        if ((fName.length() != 0) && (lName.length() != 0) && (mNumber.length() == 10) && (uName.length() != 0) && (password.length() != 0) && (!state.equals("-- Select State --"))) {
            Toast.makeText(AdminConsole.this, "Submitted!!!", Toast.LENGTH_SHORT).show();
            adminPresenter.insertInCrlTable(fName, lName, mNumber, uName, password, state, mailID);
            clearForm(ll_AddAdminForm);
        } else {
            Toast.makeText(AdminConsole.this, "Enter proper details!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addStatesData() {
        List<String> statelist = new ArrayList<>();
        statelist.add("-- Select State --");
        statelist.add("Andhra Pradesh");
        statelist.add("Assam");
        statelist.add("Bengal");
        statelist.add("Gujarat");
        statelist.add("Karnataka");
        statelist.add("Madhya Pradesh");
        statelist.add("Maharashtra");
        statelist.add("Orissa");
        statelist.add("Punjab");
        statelist.add("Rajasthan");
        statelist.add("Tamil Nadu");
        statelist.add("Telangana");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_layout, statelist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sp_crlState.setAdapter(dataAdapter);
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
            }
            if (view instanceof Spinner) {
                ((Spinner) view).setSelection(0, true);
            }
            if (view instanceof ViewGroup && (((ViewGroup) view).getChildCount() > 0))
                clearForm((ViewGroup) view);
        }
    }

    @Override
    public void showDialog() {

        receiverDialog = new Dialog(AdminConsole.this);
        receiverDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        receiverDialog.setContentView(R.layout.receive_ftpserver_dialog);
        receiverDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        receiveFtpDialogLayout = receiverDialog.findViewById(R.id.receiveFtpDialog);
        tv_ssid = receiverDialog.findViewById(R.id.tv_SSID);
        recievingProgress = receiverDialog.findViewById(R.id.recievingProgress);
        tv_ip = receiverDialog.findViewById(R.id.tv_ipaddr);
        tv_port = receiverDialog.findViewById(R.id.tv_port);
        btn_Disconnect = receiverDialog.findViewById(R.id.btn_Disconnect);
        tv_Details = receiverDialog.findViewById(R.id.tv_details);

        tv_ssid.setText("SSID : " + AOPApplication.networkSSID);
        tv_ip.setText("IP : 192.168.43.1");
        tv_port.setText("Port : 8080");

        receiverDialog.setCanceledOnTouchOutside(false);
        receiverDialog.setCancelable(false);
        receiverDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        receiverDialog.show();

        btn_Disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop Server
                if (ftpConnect.checkServiceRunning()) {
                    ftpConnect.stopServer();
                }
                ftpConnect.turnOnOffHotspot(false);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
                try {
                    //FileUtils.deleteDirectory(new File(Environment.getExternalStorageDirectory() + "/.AOPInternal/UsageJsons"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                receiverDialog.dismiss();
            }
        });


    }


    @Override
    public void onFilesRecievedComplete(String typeOfFile, String filename) {
        String path = Environment.getExternalStorageDirectory().toString() + "/.AOPInternal/UsageJsons";
        File directory = new File(path);
        File[] files = directory.listFiles();
        int cnt = 0;
        String fileName = "";
        for (int i = 0; i < files.length; i++) {
                try {
                    fileName += "\n" + files[i].getName() + "   " + Integer.parseInt(String.valueOf(files[i].length() / 1024)) + " kb";
                    FileUtils.moveFileToDirectory(new File(files[i].getAbsolutePath()),
                            new File(Environment.getExternalStorageDirectory().toString() + "/.AOPInternal/JsonsBackup"), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cnt++;
        }
//        clearDBRecords();
        tv_Details.setText("\nFiles Transferred : " + cnt + fileName);
    }

    @OnClick(R.id.btn_receive_data)
    public void receiveData() {
        // get CRL Name by ID
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        if (!ftpConnect.checkServiceRunning()) {
            // Set HotSpot Name after crl name
            AOPApplication.networkSSID = "PrathamHotSpot_TEST";
            File f = new File(Environment.getExternalStorageDirectory() + "/.AOPInternal/ReceivedUsageJsons");
            if (!f.exists())
                f.mkdir();
            AOPApplication.setPath(Environment.getExternalStorageDirectory() + "/.AOPInternal/ReceivedUsageJsons");
            // Create FTP Server
            ftpConnect.createFTPHotspot();
        } else {
            Toast.makeText(AdminConsole.this, "Server already running", Toast.LENGTH_SHORT).show();
            ftpConnect.stopServer();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Eventbus
// This method will be called when a MessageEvent is posted (in the UI thread)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.message.equalsIgnoreCase("Recieved")) {
            filename = "";
            tv_Details.setText("");
            recievingProgress.setVisibility(View.VISIBLE);

            File transferSrc = new File(Environment.getExternalStorageDirectory() + "/.AOPInternal/ReceivedUsageJsons");
            if (transferSrc.exists() && transferSrc.listFiles().length > 0) {
                File[] files = transferSrc.listFiles();
                for (int i = 0; i < files.length; i++) {
//                    Utility.targetPath = Environment.getExternalStorageDirectory() + "/.AOPInternal/ReceivedUsageJsons";
//                    Utility.recievedFilePath = files[i].getAbsolutePath();
                    try {
                        filename += "\n" + files[i].getName() + "   " + Integer.parseInt(String.valueOf(files[i].length() / 1024)) + " kb";
//                        FileUtils.moveFileToDirectory(new File(Utility.recievedFilePath),
//                                new File(Utility.targetPath /*+ "/" + files[i].getName()*/), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        tv_Details.setText("Files Recieved...." + filename);
                    }
                }
                filename = "";
            }
            recievingProgress.setVisibility(View.GONE);
        } else if (event.message.equalsIgnoreCase("showCount")) {
            filename = "";
            recievingProgress.setVisibility(View.VISIBLE);
            tv_Details.setText("");
        } else if (event.message.equalsIgnoreCase("showDetails")) {
            tv_Details.setText("Files Received...." + filename);
            recievingProgress.setVisibility(View.GONE);
        } else if (event.message.equalsIgnoreCase("stopDialog")) {
            filename = "";
        }
    }

}