package com.example.pravin.angreziok.ui.admin_console;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
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
                    List<Session> sessionsList= appDatabase.getSessionDao().getAllSessions();
                    sessionData = fillSessionData(sessionsList);
                    return null;
                }

                @Override
                protected void onPostExecute(Object obj) {
                    String requestString = generateRequestString(scoreData,attendanceData,newStudentData,newCrlData,sessionData);
                    String transferFileName = "AOP_Usage:"+ AOPApplication.getUniqueID().toString();
                    WriteSettings(mContext, requestString, transferFileName);
                    super.onPostExecute(obj);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateRequestString(JSONArray scoreData, JSONArray attendanceData, JSONArray newStudentData, JSONArray newCrlData, JSONArray sessionData) {
        String requestString="";
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
                    ", \"newCrlsData\": " + newCrlData+
                    ", \"sessionsData\": " + sessionData + "}";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return requestString;
    }

    private JSONArray fillSessionData(List<Session> sessionList) {
        JSONArray newSessionsData = null;
        JSONObject _sessionObj;
        try{
            for (int i = 0; i < sessionList.size(); i++) {
                _sessionObj = new JSONObject();
                _sessionObj.put("SessionID", sessionList.get(i).getSessionID());
                _sessionObj.put("fromDate", sessionList.get(i).getFromDate());
                _sessionObj.put("toDate", sessionList.get(i).getToDate());
                newSessionsData.put(_sessionObj);
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return newSessionsData;
    }


    private JSONArray fillNewCrlsData(List<Crl> crlsList) {
        JSONArray newCrlsData = null;
        JSONObject _crlObj;
        try{
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
        } catch (Exception e){
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
        }catch (Exception e){
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
            if (currentPush)
                MainPath = Environment.getExternalStorageDirectory() + "/.AOPInternal/SelfUsageJsons/" + fName + ".json";
            else
                MainPath = Environment.getExternalStorageDirectory() + "/.AOPInternal/UsageJsons/" + fName + ".json";
            String MainPath;
            File file = new File(MainPath);
            try {
                path.add(MainPath);
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
}
