package com.example.piandroid.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "des_user")
    String des_user;
    @ColumnInfo(name = "image")
    String image;
    @ColumnInfo(name = "isSent")
    Boolean isSent;
    @ColumnInfo(name = "message")
    String message;

    @ColumnInfo(name = "port")
    String port;
    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes_user() {
        return des_user;
    }

    public void setDes_user(String des_user) {
        this.des_user = des_user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}

