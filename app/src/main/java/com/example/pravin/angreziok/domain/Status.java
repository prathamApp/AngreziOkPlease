package com.example.pravin.angreziok.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Status")
public class Status {

    @ColumnInfo(name = "statusKey")
    private String statusKey;
    @NonNull
    @ColumnInfo(name = "value")
    private String value="";
    @ColumnInfo(name = "description")
    private String description;

    public String getstatusKey() {
        return statusKey;
    }

    public void setstatusKey(String statusKey) {
        this.statusKey = statusKey;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
