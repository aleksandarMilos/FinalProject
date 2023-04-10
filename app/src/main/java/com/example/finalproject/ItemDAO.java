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
    //Method for inserting the task into the database when we press the "ADD TASK" button
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(Item item);

    //Method to delete the task from the database when we long-click a task and press "Yes" to the confirmation dialog
    @Delete
    int deleteData(Item item);

    //This Query is used to get us the already existing TodoList associated to a specific Course of a Specific user
    @Query("SELECT * FROM item_table WHERE usName = :username AND courseID = :courseID")
    List<Item> getAllTasksforUserCourse(String username, int courseID);

}
