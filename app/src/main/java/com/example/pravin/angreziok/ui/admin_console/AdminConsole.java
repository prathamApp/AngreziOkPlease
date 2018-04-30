package com.example.pravin.angreziok.ui.admin_console;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Crl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminConsole extends BaseActivity {

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
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_console);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        adminTitle.setText("Admin Console");
        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        addAdminFlg = false;
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

    @OnClick(R.id.btn_add_admin)
    public void CreateAdmin() {
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
            insertInCrlTable(fName, lName, mNumber, uName, password, state, mailID);
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

/*        for (int i = 0; i < 29; i++)
            statelist.add(AllGroups.get(i).GroupName);*/

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

    public void insertInCrlTable(String fName, String lName, String mNumber, String uName, String password, String state, String mailID) {
        try {
            final Crl crl = new Crl();
            crl.setCRLId(UUID.randomUUID().toString());
            crl.setEmail(mailID);
            crl.setFirstName(fName);
            crl.setLastName(lName);
            crl.setMobile(mNumber);
            crl.setUserName(uName);
            crl.setPassword(password);
            crl.setState(state);
            crl.setProgramId(2);
            crl.setNewCrl(true);
            crl.setCreatedBy("");

            new AsyncTask<Object , Void ,Object>(){
                @Override
                protected Object doInBackground(Object... objects) {
                    appDatabase.getCrlDao().insert(crl);
                    BackupDatabase.backup(AdminConsole.this);
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}