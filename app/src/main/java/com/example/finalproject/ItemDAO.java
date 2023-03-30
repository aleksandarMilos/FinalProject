package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(Item item);

    @Query("SELECT * FROM item_table")
    List<Item> getAllItem();

    //This Query is used to get us the already exisiting TodoList associated to a specific Course of a Specific user
    @Query("SELECT * FROM item_table WHERE usName = :username AND courseID = :courseID")
    List<Item> getAllTasksforUserCourse(String username, int courseID);

    @Update
    int updateData(Item item);

    @Delete
    int deleteData(Item item);
}
