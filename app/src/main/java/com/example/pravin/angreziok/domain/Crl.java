package com.example.pravin.angreziok.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Crl")
public class Crl {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "CrlTableId")
    private int CrlTableId;
    @ColumnInfo(name = "CRLId")
    private String CRLId;
    @ColumnInfo(name = "FirstName")
    private String FirstName;
    @ColumnInfo(name = "LastName")
    private String LastName;
    @ColumnInfo(name = "UserName")
    private String UserName;
    @ColumnInfo(name = "Password")
    private String Password;
    @ColumnInfo(name = "ProgramId")
    private int ProgramId;
    @ColumnInfo(name = "Mobile")
    private String Mobile;
    @ColumnInfo(name = "State")
    private String State;
    @ColumnInfo(name = "Email")
    private String Email;
    @ColumnInfo(name = "CreatedBy")
    private String CreatedBy;
    @ColumnInfo(name = "newCrl")
    private boolean newCrl;


    @Override
    public String toString() {
        return "Crl{" +
                "CrlTableId=" + CrlTableId +
                ", CRLId='" + CRLId + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", ProgramId=" + ProgramId +
                ", Mobile='" + Mobile + '\'' +
                ", State='" + State + '\'' +
                ", Email='" + Email + '\'' +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", newCrl=" + newCrl +
                '}';
    }


    public int getCrlTableId() {
        return CrlTableId;
    }

    public void setCrlTableId(int crlTableId) {
        CrlTableId = crlTableId;
    }

    public String getCRLId() {
        return CRLId;
    }

    public void setCRLId(String CRLId) {
        this.CRLId = CRLId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getProgramId() {
        return ProgramId;
    }

    public void setProgramId(int programId) {
        ProgramId = programId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public boolean isNewCrl() {
        return newCrl;
    }

    public void setNewCrl(boolean newCrl) {
        this.newCrl = newCrl;
    }
}
