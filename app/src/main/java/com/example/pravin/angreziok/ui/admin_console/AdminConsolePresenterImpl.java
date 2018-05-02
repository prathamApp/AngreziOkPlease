package com.example.pravin.angreziok.ui.admin_console;

import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.example.pravin.angreziok.AOPApplication;
import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Attendance;
import com.example.pravin.angreziok.domain.Crl;
import com.example.pravin.angreziok.domain.Score;
import com.example.pravin.angreziok.domain.Session;
import com.example.pravin.angreziok.domain.Student;
import com.example.pravin.angreziok.services.SyncUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminConsolePresenterImpl implements AdminConsoleContract.AdminConsolePresenter {

    Context mContext;
    private AppDatabase appDatabase;
    AdminConsoleContract.AdminConsoleView adminConsoleView;
    File[] filesForBackup;
    String pushFileName,pushAPI,transferFileName;
    int cnt = 0, allFiles = 0;
    int[] fileCount;
    static boolean sentFlag = false;
    boolean currentPush = false;

    public AdminConsolePresenterImpl(Context mContext, AdminConsoleContract.AdminConsoleView adminConsoleView) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
        this.adminConsoleView = adminConsoleView;
        pushFileName = "AOP_Usage:";
        pushAPI = "http://www.aok.openiscool.org/api/kahaniya/pushdata";
    }

    @Override
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

            new AsyncTask<Object, Void, Object>() {
                @Override
                protected Object doInBackground(Object... objects) {
                    appDatabase.getCrlDao().insert(crl);
                    BackupDatabase.backup(mContext);
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createJsonforTransfer() {
        try {
            new AsyncTask<Object, Void, Object>() {

                JSONArray scoreData = new JSONArray(),
                        attendanceData = new JSONArray(),
                        newStudentData = new JSONArray(),
                        newCrlData = new JSONArray(),
                        sessionData = new JSONArray();

                @Override
                protected Object doInBackground(Object... objects) {
                    List<Score> scoresList = appDatabase.getScoreDao().getAllScores();
                    scoreData = fillScoreData(scoresList);
                    List<Attendance> attendancesList = appDatabase.getAttendanceDao().getAllAttendanceEntries();
                    attendanceData = fillAttendanceData(attendancesList);
                    List<Student> studentsList = appDatabase.getStudentDao().getAllNewStudents();
                    newStudentData = fillNewStudentsData(studentsList);
                    List<Crl> crlsList = appDatabase.getCrlDao().getAllNewCrls();
                    newCrlData = fillNewCrlsData(crlsList);
                    List<Session> sessionsList = appDatabase.getSessionDao().getAllSessions();
                    sessionData = fillSessionData(sessionsList);
                    return null;
                }

                @Override
                protected void onPostExecute(Object obj) {
                    String requestString = generateRequestString(scoreData, attendanceData, newStudentData, newCrlData, sessionData);
                    transferFileName = "AOP_Usage:" + AOPApplication.getUniqueID().toString();
                    WriteSettings(mContext, requestString, transferFileName);
                    super.onPostExecute(obj);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTransferFilename() {
        return transferFileName;
    }

    private String generateRequestString(JSONArray scoreData, JSONArray attendanceData, JSONArray newStudentData, JSONArray newCrlData, JSONArray sessionData) {
        String requestString = "";
        try {
            JSONObject metaDataObj = new JSONObject();
            metaDataObj.put("ScoreCount", scoreData.length());
            metaDataObj.put("AttendanceCount", attendanceData.length());
            metaDataObj.put("NewStudentsCount", newStudentData.length());
            metaDataObj.put("NewCrlsCount", newCrlData.length());
            metaDataObj.put("SessionsCount", sessionData.length());
            metaDataObj.put("TransId", AOPApplication.getUniqueID());
            metaDataObj.put("DeviceId", "Take From Status");//TODO DeviceID
            metaDataObj.put("MobileNumber", "0");

            requestString = "{ \"metadata\": " + metaDataObj +
                    ", \"scoreData\": " + scoreData +
                    ", \"attendanceData\": " + attendanceData +
                    ", \"newStudentsData\": " + newStudentData +
                    ", \"newCrlsData\": " + newCrlData +
                    ", \"sessionsData\": " + sessionData + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return requestString;
    }

    private JSONArray fillSessionData(List<Session> sessionList) {
        JSONArray newSessionsData = null;
        JSONObject _sessionObj;
        try {
            for (int i = 0; i < sessionList.size(); i++) {
                _sessionObj = new JSONObject();
                _sessionObj.put("SessionID", sessionList.get(i).getSessionID());
                _sessionObj.put("fromDate", sessionList.get(i).getFromDate());
                _sessionObj.put("toDate", sessionList.get(i).getToDate());
                newSessionsData.put(_sessionObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newSessionsData;
    }


    private JSONArray fillNewCrlsData(List<Crl> crlsList) {
        JSONArray newCrlsData = null;
        JSONObject _crlObj;
        try {
            for (int i = 0; i < crlsList.size(); i++) {
                _crlObj = new JSONObject();
                _crlObj.put("CRLId", crlsList.get(i).getCRLId());
                _crlObj.put("FirstName", crlsList.get(i).getFirstName());
                _crlObj.put("LastName", crlsList.get(i).getLastName());
                _crlObj.put("UserName", crlsList.get(i).getUserName());
                _crlObj.put("Password", crlsList.get(i).getPassword());
                _crlObj.put("ProgramId", crlsList.get(i).getProgramId());
                _crlObj.put("Mobile", crlsList.get(i).getMobile());
                _crlObj.put("State", crlsList.get(i).getState());
                _crlObj.put("Email", crlsList.get(i).getEmail());
                _crlObj.put("CreatedBy", crlsList.get(i).getCreatedBy());
                _crlObj.put("newCrl", !crlsList.get(i).getNewCrl());
                newCrlsData.put(_crlObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newCrlsData;
    }

    private JSONArray fillNewStudentsData(List<Student> studentsList) {
        JSONArray newStudentsData = null;
        JSONObject _studentObj;
        try {
            for (int i = 0; i < studentsList.size(); i++) {
                _studentObj = new JSONObject();
                _studentObj.put("StudentID", studentsList.get(i).getStudentID());
                _studentObj.put("StudentUID", studentsList.get(i).getStudentUID());
                _studentObj.put("FirstName", studentsList.get(i).getFirstName());
                _studentObj.put("MiddleName", studentsList.get(i).getMiddleName());
                _studentObj.put("LastName", studentsList.get(i).getLastName());
                _studentObj.put("Gender", studentsList.get(i).getGender());
                _studentObj.put("regDate", studentsList.get(i).getRegDate());
                _studentObj.put("Age", studentsList.get(i).getAge());
                _studentObj.put("villageName", studentsList.get(i).getVillageName());
                _studentObj.put("newFlag", studentsList.get(i).getNewFlag());
                _studentObj.put("DeviceId", studentsList.get(i).getDeviceId());
                newStudentsData.put(_studentObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newStudentsData;
    }

    private JSONArray fillAttendanceData(List<Attendance> attendances) {
        JSONArray attendanceData = null;
        JSONObject _obj;
        try {
            for (int i = 0; i < attendances.size(); i++) {
                _obj = new JSONObject();
                Attendance _attendance = attendances.get(i);
                _obj.put("AttendanceID", _attendance.getAttendanceID());
                _obj.put("SessionID", _attendance.getSessionID());
                _obj.put("StudentID", _attendance.getStudentID());
                attendanceData.put(_obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return attendanceData;
    }

    private JSONArray fillScoreData(List<Score> scores) {
        JSONArray scoreData = null;
        JSONObject _obj;
        try {
            for (int i = 0; i < scores.size(); i++) {
                _obj = new JSONObject();
                Score _score = scores.get(i);
                _obj.put("ScoreId", _score.getScoreId());
                _obj.put("SessionID", _score.getSessionID());
                _obj.put("StudentID", _score.getStudentID());
                _obj.put("DeviceID", _score.getDeviceID());
                _obj.put("ResourceID", _score.getResourceID());
                _obj.put("QuestionID", _score.getQuestionId());
                _obj.put("ScoredMarks", _score.getScoredMarks());
                _obj.put("TotalMarks", _score.getTotalMarks());
                _obj.put("StartDateTime", _score.getStartDateTime());
                _obj.put("EndDateTime", _score.getEndDateTime());
                _obj.put("Level", _score.getLevel());
                scoreData.put(_obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return scoreData;
    }


    public void WriteSettings(Context context, String data, String fName) {

        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try {
            String MainPath;
            if (currentPush) {
                MainPath = Environment.getExternalStorageDirectory() + "/.AOPInternal/SelfUsageJsons/" + fName + ".json";
            } else {
                MainPath = Environment.getExternalStorageDirectory() + "/.AOPInternal/UsageJsons/" + fName + ".json";
            }
            File file = new File(MainPath);
            try {
                fOut = new FileOutputStream(file);
                osw = new OutputStreamWriter(fOut);
                osw.write(data);
                osw.flush();
                osw.close();
                fOut.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentPush) {
                pushToServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Settings not saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void pushToServer() {

        cnt = 0;
        allFiles = 0;

        // Checking Internet Connection
        SyncUtility syncUtility = new SyncUtility(mContext);

        if (SyncUtility.isDataConnectionAvailable(mContext)) {

            Toast.makeText(mContext, "Connected to the Internet !!!", Toast.LENGTH_SHORT).show();

            //Moving to Receive usage
            String path;
            if (currentPush) {
                path = Environment.getExternalStorageDirectory().toString() + "/.KKSInternal/SelfUsageJsons";
            } else {
                path = Environment.getExternalStorageDirectory().toString() + "/Bluetooth";
            }

            String destFolder = Environment.getExternalStorageDirectory() + "/.KKSInternal/JsonsBackup";

            Log.d("path", "pushToServer: " + path);

            File blueToothDir = new File(path);
            if (!blueToothDir.exists() && !currentPush) {
                Toast.makeText(mContext, "Bluetooth folder does not exist", Toast.LENGTH_SHORT).show();
            } else {
                adminConsoleView.generateDialog("Please Wait...");
                File[] files = blueToothDir.listFiles();
                filesForBackup = blueToothDir.listFiles();

                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().contains(pushFileName))
                        allFiles++;
                }
                fileCount = new int[files.length];
                Toast.makeText(mContext, "Pushing data to server Please wait...", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().contains(pushFileName)) {
                        try {
                            startPushing(convertToString(files[i]), syncUtility, i, destFolder);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (cnt == 0) {
                                adminConsoleView.stopDialog();
                                Toast.makeText(mContext, "No files available !!!", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }
        } else {
            Toast.makeText(mContext, "Please Connect to the Internet !!!", Toast.LENGTH_SHORT).show();
        }
    }

    public String convertToString(File file) throws IOException {
        int length = (int) file.length();
        FileInputStream in = null;
        byte[] bytes = new byte[length];
        try {
            in = new FileInputStream(file);
            in.read(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        String contents = new String(bytes);
        return contents;
    }

    public void startPushing(String jasonDataToPush, SyncUtility syncUtility, int fileNo, String destinationFolder){
        ArrayList<String> arrayListToTransfer = new ArrayList<String>();

        arrayListToTransfer.add(jasonDataToPush);

        Log.d("pushedJson :::", jasonDataToPush);

        new AsyncTaskRunner(syncUtility, jasonDataToPush, fileNo, destinationFolder).execute();

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        SyncUtility syncUtility;
        String jasonDataToPush, folderForBackup;
        int currentFileNo;


        public AsyncTaskRunner(SyncUtility syncUtility, String jasonDataToPush, int currentFileNo, String folderForBackup) {
            this.syncUtility = syncUtility;
            this.jasonDataToPush = jasonDataToPush;
            this.currentFileNo = currentFileNo;
            this.folderForBackup = folderForBackup;
        }

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                String pushResult = syncUtility.sendData(pushAPI, jasonDataToPush);
                Log.d("pushResult", pushResult);
                if (pushResult.equalsIgnoreCase("success")) {
                    ////// increment count
                    cnt++;
                    fileCutPaste(filesForBackup[currentFileNo], folderForBackup);

                    if(cnt == allFiles)
                        sentFlag = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            if (cnt == allFiles) {
                if(currentPush)
                    clearRecordsOrNot();
                currentPush = false;
                adminConsoleView.stopDialog();

                // Show count on screen cnt
                Toast.makeText(mContext, "Data succesfully pushed to the server !!!", Toast.LENGTH_LONG).show();
                Toast.makeText(mContext, "Files moved in pushedUsage folder !!!", Toast.LENGTH_SHORT).show();

            } else if (!sentFlag && (cnt == allFiles) ) {
                adminConsoleView.stopDialog();
                Toast.makeText(mContext, "Data NOT pushed to the server !!!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

    public void clearRecordsOrNot() {

        final String dialogTitle, msgQuestion, negativeMsg,positiveMsg,successToast,failedToast;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);//Theme_Material_Dialog_Alert,Theme_DeviceDefault_Dialog_Alert,Theme_DeviceDefault_Light_DarkActionBar
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        if(currentPush){
            dialogTitle = "<font color='#2E96BB'>PUSH SUCCESSFUL ?</font>";
            msgQuestion = "CLEAR RECORDS IF SUCCESSFUL????\n\n If you click on 'PUSH SUCCESSFUL' then Data will be Deleted!!!\n If you click 'PUSH FAILED' the Data will persist";
            negativeMsg="PUSH FAILED";
            positiveMsg="PUSH SUCCESSFUL";
            successToast = "DATA CLEARED";
            failedToast = "DATA NOT CLEAR ";
        }
        else{
            dialogTitle = "<font color='#2E96BB'>SHARE SUCCESSFUL ?</font>";
            msgQuestion = "If you see 'File received successfully' message on master tab,\nClick SHARE SUCCESSFUL.\n\nWARNING : If you click SHARE SUCCESSFUL without receiving\n data on master tab, Data will be LOST !!!";
            negativeMsg="SHARE FAILED";
            positiveMsg="SHARE SUCCESSFUL";
            successToast = "File Transferred Successfully!!!";
            failedToast = "File Not Transferred !!!";
        }
        builder.setTitle(Html.fromHtml(""+dialogTitle))
                .setMessage(""+msgQuestion)
                .setNegativeButton(""+negativeMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        Toast.makeText(mContext, ""+failedToast, Toast.LENGTH_SHORT).show();
                        //Delete
                        if(!currentPush){
                            File f = new File(Environment.getExternalStorageDirectory() + "/.KKSInternal/UsageJsons/" + transferFileName+ ".json");
                            f.delete();
                        }
                    }
                })

                .setPositiveButton(""+positiveMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        new AsyncTask<Object,Void,Object>(){

                            @Override
                            protected Object doInBackground(Object... objects) {
                                appDatabase.getScoreDao().deleteAll();
                                return null;
                            }
                        }.execute();
                        BackupDatabase.backup(mContext);
                        Toast.makeText(mContext, ""+successToast, Toast.LENGTH_SHORT).show();
                        if(!currentPush){
                            File f = new File(Environment.getExternalStorageDirectory() + "/.AOPInternal/UsageJsons/" + transferFileName+ ".json");
                            String destFolder = Environment.getExternalStorageDirectory() + "/.AOPInternal/JsonsBackup";
                            fileCutPaste(f,destFolder);
                        }
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();
    }


    public static void fileCutPaste(File toMove, String destFolder) {
        try {
            File destinationFolder = new File(destFolder);
            File destinationFile = new File(destFolder + "/" + toMove.getName());
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
            }
            FileInputStream fileInputStream = new FileInputStream(toMove);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);

            int bufferSize;
            byte[] bufffer = new byte[512];
            while ((bufferSize = fileInputStream.read(bufffer)) > 0) {
                fileOutputStream.write(bufffer, 0, bufferSize);
            }
            toMove.delete();
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
