package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserPassDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertData(UserPass userPass);

    @Query("SELECT * FROM UserPass_table")
    List<UserPass> getAllUserPass();

    //This is the query we'll be using to check existing Usernames in the database
    //https://stackoverflow.com/questions/10377781/return-boolean-value-on-sql-select-statement
    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM UserPass_table WHERE username == :username AND password == :password) THEN CAST (1 AS BIT) ELSE CAST (0 AS BIT) END") //Seems to work correctly!
    boolean checkUserPass(String username, String password);

    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM UserPass_table WHERE username == :username) THEN CAST (1 AS BIT) ELSE CAST (0 AS BIT) END") //This query is for when we create a User
    boolean checkUser(String username);

    @Update
    int updateData(UserPass userPass);

    @Delete
    int deleteData(UserPass userPass);
}
