package com.example.pravin.angreziok.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Score")
public class Score {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ScoreId")
    private String ScoreId;
    @ColumnInfo(name = "SessionID")
    private String SessionID;
    @ColumnInfo(name = "StudentID")
    private String StudentID;
    @ColumnInfo(name = "DeviceID")
    private String DeviceID;
    @ColumnInfo(name = "ResourceID")
    private String ResourceID;
    @ColumnInfo(name = "QuestionId")
    private int QuestionId;
    @ColumnInfo(name = "ScoredMarks")
    private int ScoredMarks;
    @ColumnInfo(name = "TotalMarks")
    private int TotalMarks;
    @ColumnInfo(name = "StartDateTime")
    private String StartDateTime;
    @ColumnInfo(name = "EndDateTime")
    private String EndDateTime;
    @ColumnInfo(name = "Level")
    private int Level;

    @Override
    public String toString() {
        return "Score{" +
                "ScoreId='" + ScoreId + '\'' +
                ", SessionID='" + SessionID + '\'' +
                ", StudentID='" + StudentID + '\'' +
                ", DeviceID='" + DeviceID + '\'' +
                ", ResourceID='" + ResourceID + '\'' +
                ", QuestionId=" + QuestionId +
                ", ScoredMarks=" + ScoredMarks +
                ", TotalMarks=" + TotalMarks +
                ", StartDateTime='" + StartDateTime + '\'' +
                ", EndDateTime='" + EndDateTime + '\'' +
                ", Level=" + Level +
                '}';
    }

    @NonNull
    public String getScoreId() {
        return ScoreId;
    }

    public void setScoreId(@NonNull String scoreId) {
        ScoreId = scoreId;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getResourceID() {
        return ResourceID;
    }

    public void setResourceID(String resourceID) {
        ResourceID = resourceID;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public int getScoredMarks() {
        return ScoredMarks;
    }

    public void setScoredMarks(int scoredMarks) {
        ScoredMarks = scoredMarks;
    }

    public int getTotalMarks() {
        return TotalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        TotalMarks = totalMarks;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }
}
