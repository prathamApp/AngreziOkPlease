package com.example.pravin.angreziok.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Student")
public class Student {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "StudentID")
    private String StudentID;
    @ColumnInfo(name = "StudentUID")
    private String StudentUID;
    @ColumnInfo(name = "FirstName")
    private String FirstName;
    @ColumnInfo(name = "MiddleName")
    private String MiddleName;
    @ColumnInfo(name = "LastName")
    private String LastName;
    @ColumnInfo(name = "Gender")
    private String Gender;
    @ColumnInfo(name = "regDate")
    private String regDate;
    @ColumnInfo(name = "Age")
    private int Age;
    @ColumnInfo(name = "villageName")
    private String villageName;
    @ColumnInfo(name = "newFlag")
    private int newFlag;
    @ColumnInfo(name = "DeviceId")
    private String DeviceId;

    @NonNull
    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(@NonNull String studentID) {
        StudentID = studentID;
    }

    public String getStudentUID() {
        return StudentUID;
    }

    public void setStudentUID(String studentUID) {
        StudentUID = studentUID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public int getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(int newFlag) {
        this.newFlag = newFlag;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }
}
