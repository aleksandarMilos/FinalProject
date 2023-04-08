package com.example.finalproject;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//This table is keeping track of Username and password
@Entity(tableName = "UserPass_table")
public class UserPass {
    //Table's Attributes (Columns)
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "Username")
    @NonNull
    private String username;

    @ColumnInfo(name = "Password")
    private String password;

    //Getters and Setters for those attributes
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }
}
