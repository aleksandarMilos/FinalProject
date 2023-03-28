package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity; //We imported this
import androidx.room.PrimaryKey;

@Entity(tableName = "UserPass_table")
public class UserPass {

    //This table is keeping track of Username and password
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Username")
    @NonNull
    private String username;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    @ColumnInfo(name = "Password")
    private String password;

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }




}
