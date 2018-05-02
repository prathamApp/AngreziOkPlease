package com.example.pravin.angreziok.ui.admin_console;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.pravin.angreziok.database.AppDatabase;
import com.example.pravin.angreziok.database.BackupDatabase;
import com.example.pravin.angreziok.domain.Attendance;
import com.example.pravin.angreziok.domain.Crl;
import com.example.pravin.angreziok.domain.Score;
import com.example.pravin.angreziok.services.TTSService;
import com.example.pravin.angreziok.ui.bole_toh_round.BoleTohContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;

public class AdminConsolePresenterImpl implements AdminConsoleContract.AdminConsolePresenter {

    String ttsQuestion;
    float speechRate = 1.0f;
    public TTSService ttsService;
    Context mContext;
    String sdCardPathString;
    BoleTohContract.BoleToh_G2_L2_View boleTohG2L2View;
    private AppDatabase appDatabase;


    public AdminConsolePresenterImpl(Context mContext) {
        this.mContext = mContext;
        appDatabase = Room.databaseBuilder(mContext,
                AppDatabase.class, AppDatabase.DB_NAME)
                .build();
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
//        try {
//            new AsyncTask<Object, Void, Object>() {
//
//                JSONArray scoreData = new JSONArray(),
//                        attendanceData = new JSONArray(),
//                        studentData = new JSONArray(),
//                        crlData = new JSONArray(),
//                        sessionData = new JSONArray();
//
//                @Override
//                protected Object doInBackground(Object... objects) {
//                    List<Score> scores = appDatabase.getScoreDao().getAllScores();
//                    scoreData = fillScoreData(scores);
//                    List<Attendance> attendances = appDatabase.getAttendanceDao().getAllAttendanceEntries();
//                    attendanceData = fillAttendanceData(attendances);
//                    return null;
//                }
//            }.execute();
//
//
//                    if (attendanceData == null) {
//                    } else {
//                        for (int i = 0; i < attendanceData.length(); i++) {
//                            JSONObject jsonObject = attendanceData.getJSONObject(i);
//                            jsonObject.put("SessionID", jsonObject.get("SessionID"));
//                            jsonObject.put("StudentID", jsonObject.get("StudentID"));
//                        }
//
//                        //pravin
//                        //For New Students data
//                        List<Student> studentsList = studentDBHelper.GetAllNewStudents();
//                        Log.d("student_list_size::", String.valueOf(studentDBHelper.GetAllNewStudents().size()));
//                        JSONObject studentObj;
//                        if (studentData != null) {
//                            for (int i = 0; i < studentsList.size(); i++) {
//                                studentObj = new JSONObject();
//                                studentObj.put("StudentID", studentsList.get(i).StudentID);
//                                studentObj.put("StudentUID", studentsList.get(i).StudentUID == null ? "" : studentsList.get(i).StudentUID);
//                                studentObj.put("FirstName", studentsList.get(i).FirstName);
//                                studentObj.put("MiddleName", studentsList.get(i).MiddleName);
//                                studentObj.put("LastName", studentsList.get(i).LastName);
//                                studentObj.put("Gender", studentsList.get(i).Gender);
//                                studentObj.put("regDate", studentsList.get(i).regDate);
//                                studentObj.put("Age", studentsList.get(i).Age);
//                                studentObj.put("villageName", studentsList.get(i).villageName);
//                                studentObj.put("newFlag", studentsList.get(i).newFlag);
//                                studentObj.put("DeviceId", studentsList.get(i).DeviceId);
//                                studentData.put(studentObj);
//                            }
//                        }
//
//                        //pravin
//                        //For New Crls data
//                        List<Crl> crlsList = crlDBHelper.GetAllNewCrl();
//                        JSONObject crlObj;
//                        if (crlData != null) {
//                            for (int i = 0; i < crlsList.size(); i++) {
//                                crlObj = new JSONObject();
//                                crlObj.put("CRLId", crlsList.get(i).CRLId);
//                                crlObj.put("FirstName", crlsList.get(i).FirstName);
//                                crlObj.put("LastName", crlsList.get(i).LastName);
//                                crlObj.put("UserName", crlsList.get(i).UserName);
//                                crlObj.put("Password", crlsList.get(i).Password);
//                                crlObj.put("ProgramId", crlsList.get(i).ProgramId);
//                                crlObj.put("Mobile", crlsList.get(i).Mobile);
//                                crlObj.put("State", crlsList.get(i).State);
//                                crlObj.put("Email", crlsList.get(i).Email);
//                                crlObj.put("CreatedBy", crlsList.get(i).CreatedBy);
//                                crlObj.put("newCrl", !crlsList.get(i).newCrl);
//                                crlData.put(crlObj);
//                            }
//                        }
//
//                        List<KksSession> sessionList = sessionDBHelper.GetAllSession();
//                        JSONObject sessionObj;
//                        if (sessionData != null) {
//                            for (int i = 0; i < sessionList.size(); i++) {
//                                sessionObj = new JSONObject();
//                                sessionObj.put("SessionID", sessionList.get(i).SessionID);
//                                sessionObj.put("fromDate", sessionList.get(i).fromDate);
//                                sessionObj.put("toDate", sessionList.get(i).toDate);
//                                sessionData.put(sessionObj);
//                            }
//                        }
//
//                        List<Level> levelList = levelDBHelper.GetAllLevelData();
//                        JSONObject levelObj;
//                        if (levelData != null) {
//                            for (int i = 0; i < levelList.size(); i++) {
//                                levelObj = new JSONObject();
//                                levelObj.put("StudentID", levelList.get(i).getStudentID());
//                                levelObj.put("BaseLevel", levelList.get(i).getBaseLevel());
//                                levelObj.put("CurrentLevel", levelList.get(i).getCurrentLevel());
//                                levelData.put(levelObj);
//                            }
//                        }
//
//                        JSONObject metaDataObj = new JSONObject();
//                        metaDataObj.put("ScoreCount", scores.size());
//                        metaDataObj.put("AttendanceCount", attendanceData.length());
//                        metaDataObj.put("NewStudentsCount", studentData.length());
//                        metaDataObj.put("NewCrlsCount", crlData.length());
//                        metaDataObj.put("LevelsCount", levelData.length());
//                        metaDataObj.put("SessionsCount", sessionData.length());
//                        metaDataObj.put("TransId", KksApplication.getUniqueID());
//                        metaDataObj.put("DeviceId", deviceID);
//                        metaDataObj.put("MobileNumber", "0");
//
//                        String requestString = "{ \"metadata\": " + metaDataObj +
//                                ", \"scoreData\": " + scoreData +
//                                ", \"attendanceData\": " + attendanceData +
//                                ", \"newStudentsData\": " + studentData +
//                                ", \"newCrlsData\": " + crlData +
//                                ", \"levelsData\": " + levelData +
//                                ", \"sessionsData\": " + sessionData + "}";
//                        transferFileName = transferFileName + KksApplication.getUniqueID().toString();
//                        WriteSettings(this, requestString, transferFileName);
//                    }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private JSONArray fillAttendanceData(List<Attendance> attendances) {
        JSONArray attendanceData = null;
        try{
            for (int i = 0; i < attendances.size(); i++) {
                JSONObject _obj = new JSONObject();
                Attendance _attendance = attendances.get(i);
                _obj.put("AttendanceID", _attendance.getAttendanceID());
                _obj.put("SessionID", _attendance.getSessionID());
                _obj.put("StudentID", _attendance.getStudentID());
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return attendanceData;
    }

    private JSONArray fillScoreData(List<Score> scores) {
        JSONArray scoreData = null;
        try {
            for (int i = 0; i < scores.size(); i++) {
                JSONObject _obj = new JSONObject();
                Score _score = scores.get(i);
                _obj.put("ScoreId",_score.getScoreId());
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
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return scoreData;
    }


//    public void WriteSettings(Context context, String data, String fName) {
//
//        FileOutputStream fOut = null;
//        OutputStreamWriter osw = null;
//
//        try {
//            String MainPath;
//            if (currentPush)
//                MainPath = Environment.getExternalStorageDirectory() + "/.KKSInternal/SelfUsageJsons/" + fName + ".json";
//            else
//                MainPath = Environment.getExternalStorageDirectory() + "/.KKSInternal/UsageJsons/" + fName + ".json";
//            File file = new File(MainPath);
//            try {
//                path.add(MainPath);
//                fOut = new FileOutputStream(file);
//                osw = new OutputStreamWriter(fOut);
//                osw.write(data);
//                osw.flush();
//                osw.close();
//                fOut.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (currentPush) {
//                pushToServer();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "Settings not saved", Toast.LENGTH_SHORT).show();
//        }
//    }
}
