package com.example.pravin.angreziok.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Crl")
public class Crl {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "crl_id")
    private String crl_id;
    @ColumnInfo(name = "crl_name")
    private String crl_name;
    @ColumnInfo(name = "crl_lastname")
    private String crl_lastname;

    public String getCrl_lastname() {
        return crl_lastname;
    }

    public void setCrl_lastname(String crl_lastname) {
        this.crl_lastname = crl_lastname;
    }

    public String getCrl_id() {
        return crl_id;
    }

    public void setCrl_id(String crl_id) {
        this.crl_id = crl_id;
    }

    public String getCrl_name() {
        return crl_name;
    }

    public void setCrl_name(String crl_name) {
        this.crl_name = crl_name;
    }


    @Override
    public String toString() {
        return "Crl{" +
                "crl_id='" + crl_id + '\'' +
                ", crl_name='" + crl_name + '\'' +
                ", crl_lastname='" + crl_lastname + '\'' +
                '}';
    }
}
