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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertData(Course course);

    @Query("SELECT * FROM Course_table WHERE uName = :username") //Get's all the courses for that User that inputted them themselves
    List<Course> getAllCourse(String username);

    @Update
    int updateData(Course course);

    @Delete
    int deleteData(Course course);
}
