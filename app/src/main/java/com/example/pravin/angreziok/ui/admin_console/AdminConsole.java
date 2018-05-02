package com.example.pravin.angreziok.ui.admin_console;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pravin.angreziok.R;

import com.example.pravin.angreziok.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminConsole extends BaseActivity implements AdminConsoleContract.AdminConsoleView{

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        adminTitle.setText("Admin Console");
        addAdminFlg = false;
        adminPresenter = new AdminConsolePresenterImpl(AdminConsole.this,this);
        addStatesData();
    }

    @Override
    public void onBackPressed() {
        if (addAdminFlg) {
            addAdminFlg=false;
            adminTitle.setText("Admin Console");
            ll_Operations.setVisibility(View.VISIBLE);
            ll_AddAdminForm.setVisibility(View.GONE);
        } else
            super.onBackPressed();
    }

    @OnClick(R.id.btn_transfer_data)
    public void transferData() {
        // Generate Json file
        adminPresenter.createJsonforTransfer();
        //transferFile(adminPresenter.getTransferFilename());
    }

    @OnClick(R.id.btn_self_push)
    public void pushCurrentTabData() {
        adminPresenter.currentPush = true;
        adminPresenter.createJsonforTransfer();
    }

    @OnClick(R.id.btn_push)
    public void pushToServer(){
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
    public void stopDialog(){
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
}