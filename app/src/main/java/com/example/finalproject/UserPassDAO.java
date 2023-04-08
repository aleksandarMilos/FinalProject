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
    //For inserting that username/password aka when Creating a new User
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertData(UserPass userPass);

    //Query used for checking if the Username + Password Login matches that of what's in the database.
    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM UserPass_table WHERE username == :username AND password == :password) THEN CAST (1 AS BIT) ELSE CAST (0 AS BIT) END")
    boolean checkUserPass(String username, String password);

    //This query is for when we create a User, checks if User already exists
    @Query("SELECT CASE WHEN EXISTS(SELECT * FROM UserPass_table WHERE username == :username) THEN CAST (1 AS BIT) ELSE CAST (0 AS BIT) END")
    boolean checkUser(String username);


    //TODO Futurework, implement these methods for say updating existing username/password, and deleting a User through the app itself
    @Update
    int updateData(UserPass userPass);

    @Delete
    int deleteData(UserPass userPass);
}
