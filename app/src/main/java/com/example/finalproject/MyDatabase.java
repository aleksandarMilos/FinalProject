package com.example.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Course.class}, version = 2) //running into error with version = 1, trying version = 2
public abstract class MyDatabase extends RoomDatabase { //Abstract class for our database
    public abstract CourseDAO courseDAO();
}
