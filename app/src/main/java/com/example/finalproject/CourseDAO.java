package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDAO {
    //When Course is added through FloatingActionButton
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(Course course);

    //When Course is deleted through Delete Switch on and "Yes" to AlertDialog
    @Delete
    int deleteData(Course course);

    //Obtains all the courses for that specific User "username"
    @Query("SELECT * FROM Course_table WHERE uName = :username")
    List<Course> getAllCourse(String username);

}
